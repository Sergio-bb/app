package solidappservice.cm.com.presenteapp.front.pagoobligaciones.FragmentPaymentCredits;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.pagoobligaciones.ProductoSpinnerAdapter;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProductos;
import solidappservice.cm.com.presenteapp.entities.pagoobligaciones.request.RequestEnviarPago;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA 27/11/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS 17/09/2021.
 */
public class FragmentPaymentCreditsView extends Fragment implements FragmentPaymentCreditsContract.View{

    private FragmentPaymentCreditsPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private ProgressDialog pd;
    private FirebaseAnalytics firebaseAnalytics;
    private List<ResponseProductos> productsToPay;
    private boolean isProcessPayment;
    int acutal=0;
    int anterior=0;

    @BindView(R.id.spinnerProducto)
    Spinner spinnerProducto;
    @BindView(R.id.rbSaldo)
    RadioButton rbSaldo;
    @BindView(R.id.lblSaldoValue)
    TextView lblSaldoValue;
    @BindView(R.id.rbOtroVal)
    RadioButton rbOtroVal;
    @BindView(R.id.txtValorApagar)
    EditText txtValorApagar;
    @BindView(R.id.spinnerProductoDebitar)
    Spinner spinnerProductoDebitar;
    @BindView(R.id.btnAceptar)
    Button btnAceptar;
    //private RadioButton rbValCuota;
    //private TextView lblCuotaValue;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de abono de credito");
        firebaseAnalytics.logEvent("pantalla_abono_credito", params);
        View view = inflater.inflate(R.layout.fragment_paymentcredits, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentPaymentCreditsPresenter(this, new FragmentPaymentCreditsModel());
        context = (ActivityBase) getActivity();
        state = context.getState();
        pd = new ProgressDialog(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null || state.getUsuario() == null){
            context.salir();
        }else{
            if(!isProcessPayment){
                fetchPendingPayments();
            }
//            Usuario usuario = state.getUsuario();
//            new EstadoCuentaTask().execute(usuario.cedula, usuario.token);
        }
    }


    @OnClick(R.id.btnAceptar)
    public void onClickAceptar(View v) {
//        enviarPago();
        validateDataPayment();
    }

    @OnTextChanged(R.id.txtValorApagar)
    public void onTextChangedValorPago(CharSequence s, int start, int before, int count) {
        String textToEdit = txtValorApagar.getText().toString();
        if(textToEdit.contains("$")){
            StringBuffer stb = new StringBuffer();
            String[] st = textToEdit.split("");
            if(st[st.length-2].equals(",") || st[st.length-2].equals(".")){

                for(int i=0; i<st.length-2; i++){
                    stb.append(st[i]);
                }
            }else if(st[st.length-3].equals(",") || st[st.length-3].equals(".")){

                for(int i=0; i<st.length-3; i++){
                    stb.append(st[i]);
                }
            }
            textToEdit = stb.toString();
        }
        textToEdit = textToEdit.replace("$", "");
        textToEdit = textToEdit.replace(",", "");
        textToEdit = textToEdit.replace(".", "");

        acutal = textToEdit.length();
        if(acutal!=anterior){
            anterior = acutal;
            DecimalFormat formato = new DecimalFormat("#,###");
            textToEdit = !TextUtils.isEmpty(textToEdit) ? formato.format(Double.parseDouble(textToEdit)) : "";
            txtValorApagar.setText(textToEdit);
            txtValorApagar.setSelection(textToEdit.length());
//            _formter(textToEdit.length(), textToEdit, txtValorApagar);
        }
    }

    @OnCheckedChanged(R.id.rbOtroVal)
    public void onCheckedChangedOtroVal(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            rbSaldo.setChecked(false);
            txtValorApagar.setText("");
            txtValorApagar.setEnabled(true);
        }
    }

    @OnCheckedChanged(R.id.rbSaldo)
    public void onCheckedChangedSaldo(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            rbOtroVal.setChecked(false);
            txtValorApagar.setText(lblSaldoValue.getText());
            txtValorApagar.setEnabled(false);
        }
    }

    @OnItemSelected(R.id.spinnerProducto)
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        validarPagoProducto(position);
        showPaymentDetails(position);
    }

    @OnItemSelected(R.id.spinnerProductoDebitar)
    public void onItemSelectedDebit(AdapterView<?> parent, View view, int position, long id) {
        if(position > 0){
            enabledAcceptButton();
        }
    }

    @Override
    public void showPaymentDetails(int positionPayment) {
        if (positionPayment > 0) {
            ResponseProductos producto = productsToPay.get(positionPayment);
            enabledCheckboxSaldo(true);
            enabledCheckboxOtroValor(true);
            lblSaldoValue.setText(context.getMoneda(producto.getV_saldo()));
            txtValorApagar.setText("");
            txtValorApagar.setEnabled(false);
            rbSaldo.setChecked(false);
            rbOtroVal.setChecked(false);
            spinnerProductoDebitar.setEnabled(true);
        } else {
            enabledCheckboxSaldo(false);
            enabledCheckboxOtroValor(false);
            txtValorApagar.setText("");
            txtValorApagar.setEnabled(false);
            lblSaldoValue.setText("");
        }
    }

    @Override
    public void fetchPendingPayments(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchPendingPayments(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void showResultPendingPayments(){
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle("No es posible realizar el pago");
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage("Tienes un pago en proceso, inténtalo de nuevo más tarde.");
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isProcessPayment = false;
                txtValorApagar.setText("");
                txtValorApagar.setEnabled(false);
                context.getState().getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
                dialog.dismiss();
            }
        });
        d.show();
    }

    @Override
    public void fetchProducts(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchProducts(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void showProductstoDebit(List<ResponseProductos> products){
        state.setProductos(products);
        List<ResponseProductos> productsToDebit = new ArrayList<>();
        ResponseProductos p = new ResponseProductos();
        p.setN_produc("Selecciona la cuenta");
        p.setN_tipodr("");
        p.setA_numdoc("");
        productsToDebit.add(0, p);
        for (ResponseProductos pro : products) {
            if (pro.getI_debito() != null && pro.getI_debito().equals("Y") && pro.getV_saldo() > 0) {
                productsToDebit.add(pro);
            }
        }
        ProductoSpinnerAdapter adapter = new ProductoSpinnerAdapter(context, productsToDebit, true);
        spinnerProductoDebitar.setAdapter(adapter);
        spinnerProductoDebitar.setEnabled(false);
    }

    @Override
    public void showProductstoPay(List<ResponseProductos> products){
        productsToPay = new ArrayList<>();
        ResponseProductos p = new ResponseProductos();
        p.setN_produc("Selecciona el producto");
        p.setN_tipodr("");
        p.setA_numdoc("");
        productsToPay.add(0, p);
        for (ResponseProductos pro : products) {
            if (pro.getI_debito() != null && pro.getI_debito().equals("N")) {
                productsToPay.add(pro);
            }
        }
        ProductoSpinnerAdapter adapter = new ProductoSpinnerAdapter(context, productsToPay, false);
        spinnerProducto.setAdapter(adapter);
    }

    @Override
    public void validateDataPayment(){
        try {
            int posicion = spinnerProducto.getSelectedItemPosition();
            boolean i_pagsup;

            if (posicion <= 0) {
                context.makeErrorDialog("Selecciona el producto a pagar");
                return;
            }

            final ResponseProductos productoApagar = (ResponseProductos) spinnerProducto.getSelectedItem();
            if (productoApagar == null) {
                context.makeErrorDialog("Selecciona el producto a pagar");
                return;
            }

            if (!rbSaldo.isChecked() && !rbOtroVal.isChecked()) {
                context.makeErrorDialog("Selecciona una opción de pago");
                return;
            }

            Editable valor = txtValorApagar.getText();
            if (valor == null || TextUtils.isEmpty(valor)) {
                context.makeErrorDialog("Ingresa el valor a pagar");
                return;
            }

            posicion = spinnerProductoDebitar.getSelectedItemPosition();
            if (posicion <= 0) {
                context.makeErrorDialog("Selecciona la cuenta a debitar");
                return;
            }

            final ResponseProductos productoAdebitar = (ResponseProductos) spinnerProductoDebitar.getSelectedItem();
            if (productoAdebitar == null) {
                context.makeErrorDialog("Selecciona la cuenta a debitar");
                return;
            }

            String v_cuota = txtValorApagar.getText().toString();
            v_cuota = v_cuota.replace(",", "");
            v_cuota = v_cuota.replace(".", "");
            v_cuota = v_cuota.replace("$", "");

            long d_valor;
            try {
                d_valor = Long.parseLong(v_cuota);
            } catch (Exception e) {
                try {
                    d_valor = Long.parseLong(valor.toString());
                } catch (Exception ex) {
                    context.makeErrorDialog("Verifica el valor a pagar, ingrese un valor correcto");
                    return;
                }
            }

            i_pagsup = productoApagar.getI_pagsup() != null && productoApagar.getI_pagsup().equals("Y");

            if (rbOtroVal.isChecked()) {
                if (!i_pagsup) {

                    if (d_valor > productoApagar.getV_saldo()) {
                        context.makeErrorDialog("Ingresa un valor a pagar inferior o igual al saldo");
                        return;
                    }
                }
            }

            if (d_valor > productoAdebitar.getV_saldo()) {
                context.makeErrorDialog("Tu saldo en esta cuenta es insuficiente para realizar este abono");
                return;
            }

            final double _valorAbono = d_valor;
            showDialogConfirmPayment(_valorAbono, productoApagar, productoAdebitar);

        } catch (Exception e){
            context.makeErrorDialog(e.getMessage());
        }
    }

    @Override
    public void showDialogConfirmPayment(Double paymentValue, ResponseProductos productToPay, ResponseProductos productoToDebit){
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle(context.getResources().getString(R.string.app_name));
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage("Tu abono será de " + context.getMoneda(paymentValue));
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    makePayment(paymentValue, productToPay, productoToDebit);
                } catch (Exception e) {
                    context.makeErrorDialog(e.getMessage());
                }
            }
        });
        d.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        d.show();
    }

    @Override
    public void makePayment(Double paymentValue, ResponseProductos productToPay, ResponseProductos productoToDebit){
        try{
            isProcessPayment = true;
            Encripcion encripcion = Encripcion.getInstance();
            presenter.makePayment(new RequestEnviarPago(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    context.obtenerIdDispositivo(),
                    paymentValue,
                    productToPay.getK_tipodr(),
                    productToPay.getN_tipodr(),
                    productToPay.getA_tipodr(),
                    encripcion.encriptar(productToPay.getA_numdoc()),
                    productToPay.getN_produc(),
                    encripcion.encriptar(productoToDebit.getA_numdoc())
            ));
        }catch (Exception ex){
            enabledAcceptButton();
            showDataFetchError("Upps, se ha producido un error, inténtalo nuevamente en unos minutos.");
        }
    }

    @Override
    public void showResultPayment(String resultPayment){
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle(context.getResources().getString(R.string.app_name));
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(resultPayment);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                isProcessPayment = false;
                txtValorApagar.setText("");
                txtValorApagar.setEnabled(false);
                enabledCheckboxSaldo(false);
                enabledCheckboxOtroValor(false);
                context.getState().getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
            }
        });
        d.show();
    }

    @Override
    public void enabledCheckboxSaldo(boolean enabled){
        rbSaldo.setEnabled(enabled);
    }
    @Override
    public void enabledCheckboxOtroValor(boolean enabled){
        rbOtroVal.setEnabled(enabled);
    }

    @Override
    public void disabledAcceptButton() {
        btnAceptar.setEnabled(false);
    }

    @Override
    public void enabledAcceptButton() {
        btnAceptar.setEnabled(true);
    }

    @Override
    public void showProgressDialog(String message) {
        pd.setTitle(context.getResources().getString(R.string.app_name));
        pd.setMessage(message);
        pd.setIcon(R.mipmap.icon_presente);
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    public void hideProgressDialog() {
        pd.dismiss();
    }

    @Override
    public void showErrorTimeOut() {
        String message = "Ha ocurrido un error. Intenta de nuevo y si el error persiste, contacta a PRESENTE.";
        if(state != null && state.getMensajesRespuesta() != null && state.getMensajesRespuesta().size()>0){
            for(ResponseMensajesRespuesta rm : state.getMensajesRespuesta()){
                if(rm.getIdMensaje() == 6){
                    message = rm.getMensaje();
                }
            }
        }
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle(context.getResources().getString(R.string.app_name));
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(message);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isProcessPayment = false;
                txtValorApagar.setText("");
                txtValorApagar.setEnabled(false);
                enabledCheckboxSaldo(false);
                enabledCheckboxOtroValor(false);
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
                dialog.dismiss();
            }
        });
        d.show();
    }

    @Override
    public void showDataFetchError(String message) {
        if(TextUtils.isEmpty(message)){
            message = "Ha ocurrido un error. Intenta de nuevo y si el error persiste, contacta a PRESENTE.";
            if(state != null && state.getMensajesRespuesta() != null && state.getMensajesRespuesta().size()>0){
                for(ResponseMensajesRespuesta rm : state.getMensajesRespuesta()){
                    if(rm.getIdMensaje() == 7){
                        message = rm.getMensaje();
                    }
                }
            }
        }
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle(context.getResources().getString(R.string.app_name));
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(message);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isProcessPayment = false;
                txtValorApagar.setText("");
                txtValorApagar.setEnabled(false);
                enabledCheckboxSaldo(false);
                enabledCheckboxOtroValor(false);
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
                dialog.dismiss();
            }
        });
        d.show();
    }

    @Override
    public void showExpiredToken(String message) {
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle("Sesión finalizada");
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(message);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                context.salir();
            }
        });
        d.show();
    }



//
//    private void validarPagoProducto(int posicion) {
//        if (posicion > 0) {
//            ResponseProducto producto = productosApagar.get(posicion);
//            lblSaldoValue.setText(context.getMoneda(producto.getV_saldo()));
//            /*if (producto.v_vencid > 0) {
//                lblCuotaValue.setText(context.getMoneda(producto.v_vencid));
//            } else {
//                lblCuotaValue.setText(context.getMoneda(producto.v_cuota));
//            }*/
//
//            txtValorApagar.setText("");
//            txtValorApagar.setEnabled(false);
//            rbSaldo.setChecked(false);
//            //rbValCuota.setChecked(false);
//            rbOtroVal.setChecked(false);
//            spinnerProductoDebitar.setEnabled(true);
//        } else {
//            txtValorApagar.setText("");
//            txtValorApagar.setEnabled(false);
//            //lblCuotaValue.setText("");
//            lblSaldoValue.setText("");
//        }
//    }
//
//    class EstadoCuentaTask extends AsyncTask<String, String, String> {
//
//        String jsonEstadoCuenta = null;
//        String jsonPagosPendientes = null;
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Actualizando estado de cuenta...");
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                Encripcion encripcion = Encripcion.getInstance();
//                JSONObject param = new JSONObject();
//                param.put("cedula", encripcion.encriptar(params[0]));
//                param.put("token", params[1]);
//
////                jsonEstadoCuenta = networkHelper.writeService(param, SincroHelper.ESTADO_CUENTA);
//                jsonEstadoCuenta = networkHelper.writeService(param, SincroHelper.CONSULTAR_CUENTAS);
//                jsonPagosPendientes = networkHelper.writeService(param, SincroHelper.CONSULTAR_ABONOS_CREDITO);
//
//                return "OK";
//            } catch (Exception e) {
//                return e.getMessage();
//            }
//        }
//
//        @Override
//        protected void onProgressUpdate(String... values) {
//            pd.setMessage(values[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            pd.dismiss();
//            procesarJsonRespuesta(jsonEstadoCuenta, jsonPagosPendientes);
//        }
//    }
//
//    private void procesarJsonRespuesta(String jsonEstadoCuenta, String jsonPagosPendientes) {
//        try {
//
//            ArrayList<AbonoCreditos> abonos = SincroHelper.procesarJsonObtenerAbonosPendientes(jsonPagosPendientes);
//
//            if(abonos != null && abonos.size()>0){
//                AlertDialog.Builder d = new AlertDialog.Builder(context);
//                d.setTitle("No es posible realizar el pago");
//                d.setIcon(R.mipmap.icon_presente);
//                d.setMessage("Tienes un pago en proceso, inténtalo de nuevo más tarde.");
//                d.setCancelable(false);
//                d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        context.getState().getmTabHost().setCurrentTab(1);
//                        dialog.dismiss();
//                    }
//                });
//                d.show();
//            }else {
//                ArrayList<ResponseProducto> productos = SincroHelper.procesarJsonEstadoCuenta(jsonEstadoCuenta);
//                cargarProductosApagar(productos);
//                cargarProductosAdebitar(productos);
//                context.getState().setProductos(productos);
//            }
//
//        } catch (ErrorTokenException e) {
//            AlertDialog.Builder d = new AlertDialog.Builder(context);
//            d.setTitle("Sesión finalizada");
//            d.setIcon(R.mipmap.icon_presente);
//            d.setMessage(e.getMessage());
//            d.setCancelable(false);
//            d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    context.salir();
//                }
//            });
//            d.show();
//        } catch (Exception e) {
//            context.makeErrorDialog(e.getMessage());
//        }
//    }
//
//    private void cargarProductosApagar(ArrayList<ResponseProducto> productos) {
//        productosApagar = new ArrayList<>();
//        ResponseProducto p = new ResponseProducto();
//        p.setN_produc("Selecciona el producto");
//        p.setN_tipodr("");
//        p.setA_numdoc("");
//        productosApagar.add(0, p);
//
//        for (ResponseProducto pro : productos) {
//            if (pro.getI_debito() != null && pro.getI_debito().equals("N")) {
//                productosApagar.add(pro);
//            }
//        }
//
//        ProductoSpinnerAdapter adapter = new ProductoSpinnerAdapter(context, productosApagar, false);
//        spinnerProducto.setAdapter(adapter);
//    }
//
//    private void cargarProductosAdebitar(ArrayList<ResponseProducto> productos) {
//        ArrayList<ResponseProducto> productosDebitar = new ArrayList<>();
//        ResponseProducto p = new ResponseProducto();
//        p.setN_produc("Selecciona la cuenta");
//        p.setN_tipodr("");
//        p.setA_numdoc("");
//        productosDebitar.add(0, p);
//
//        for (ResponseProducto pro : productos) {
//            if (pro.getI_debito() != null && pro.getI_debito().equals("Y") && pro.getV_saldo() > 0) {
//                productosDebitar.add(pro);
//            }
//        }
//
//        ProductoSpinnerAdapter adapter = new ProductoSpinnerAdapter(context, productosDebitar, true);
//        spinnerProductoDebitar.setAdapter(adapter);
//        spinnerProductoDebitar.setEnabled(false);
//    }
//
//    private void enviarPago() {
//        try {
//
//            int posicion = spinnerProducto.getSelectedItemPosition();
//            boolean i_pagsup;
//
//            if (posicion <= 0) {
//                context.makeErrorDialog("Selecciona el producto a pagar");
//                return;
//            }
//
//            final ResponseProducto productoApagar = (ResponseProducto) spinnerProducto.getSelectedItem();
//            if (productoApagar == null) {
//                context.makeErrorDialog("Selecciona el producto a pagar");
//                return;
//            }
//
//            /*if (!rbSaldo.isChecked() && !rbValCuota.isChecked() && !rbOtroVal.isChecked()) {
//                context.makeErrorDialog("Selecciona una opción de pago");
//                return;
//            }*/
//
//            if (!rbSaldo.isChecked() && !rbOtroVal.isChecked()) {
//                context.makeErrorDialog("Selecciona una opción de pago");
//                return;
//            }
//
//            Editable valor = txtValorApagar.getText();
//            if (valor == null || TextUtils.isEmpty(valor)) {
//                context.makeErrorDialog("Ingresa el valor a pagar");
//                return;
//            }
//
//            posicion = spinnerProductoDebitar.getSelectedItemPosition();
//            if (posicion <= 0) {
//                context.makeErrorDialog("Selecciona la cuenta a debitar");
//                return;
//            }
//
//            final ResponseProducto productoAdebitar = (ResponseProducto) spinnerProductoDebitar.getSelectedItem();
//            if (productoAdebitar == null) {
//                context.makeErrorDialog("Selecciona la cuenta a debitar");
//                return;
//            }
//
//
//            String v_cuota = txtValorApagar.getText().toString();
//            v_cuota = v_cuota.replace(",", "");
//            v_cuota = v_cuota.replace(".", "");
//            v_cuota = v_cuota.replace("$", "");
//
//
//            long d_valor;
//            try {
//                d_valor = Long.parseLong(v_cuota);
//            } catch (Exception e) {
//                try {
//                    d_valor = Long.parseLong(valor.toString());
//                } catch (Exception ex) {
//                    context.makeErrorDialog("Verifica el valor a pagar, ingrese un valor correcto");
//                    return;
//                }
//            }
//
//            i_pagsup = productoApagar.getI_pagsup().equals("Y");
//
//
//            if (rbOtroVal.isChecked()) {
//                if (!i_pagsup) {
//
//                    /*if (d_valor > productoApagar.v_cuota && productoApagar.v_vencid <= 0) {
//                        context.makeErrorDialog("Ingresa un valor a pagar inferior o igual a la cuota");
//                        return;
//                    } else if (productoApagar.v_vencid > 0 && d_valor > productoApagar.v_vencid) {
//                        context.makeErrorDialog("Ingresa un valor a pagar inferior o igual a la cuota");
//                        return;
//                    }*/
//                    if (d_valor > productoApagar.getV_saldo()) {
//                        context.makeErrorDialog("Ingresa un valor a pagar inferior o igual al saldo");
//                        return;
//                    }
//                }
//            }
//
//            if (d_valor > productoAdebitar.getV_saldo()) {
//                context.makeErrorDialog("Tu saldo en esta cuenta es insuficiente para realizar este abono");
//                return;
//            }
//
//            final double _valorAbono = d_valor;
//            AlertDialog.Builder d = new AlertDialog.Builder(context);
//            d.setTitle(context.getResources().getString(R.string.app_name));
//            d.setIcon(R.mipmap.icon_presente);
//            d.setMessage("Tu abono será de " + context.getMoneda(_valorAbono));
//            d.setCancelable(false);
//            d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//
//                    try {
//                        Encripcion encripcion = Encripcion.getInstance();
//                        JSONObject pago = new JSONObject();
//                        pago.put("id_dispositivo", context.obtenerIdDispositivo());
//                        pago.put("v_valor", _valorAbono);
//                        pago.put("k_tipodr", productoApagar.getK_tipodr());
//                        pago.put("n_tipodr", productoApagar.getN_tipodr());
//                        pago.put("a_tipodr", productoApagar.getA_tipodr());
//                        pago.put("a_numdoc", encripcion.encriptar(productoApagar.getA_numdoc()));
//                        pago.put("n_produc", productoApagar.getN_produc());
//                        pago.put("a_numcta", encripcion.encriptar(productoAdebitar.getA_numdoc()));//a_numcta = a_numdoc en origen
////                        pago.put("token", context.getState().getUsuario().token + productoAdebitar.k_tipodr);
//                        pago.put("token", context.getState().getUsuario().token);
//                        pago.put("cedula", encripcion.encriptar(context.getState().getUsuario().cedula));
//                        new EnviarAbonoTask().execute(pago);
//
//                    } catch (Exception e) {
//                        context.makeErrorDialog(e.getMessage());
//                    }
//                }
//            });
//            d.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                }
//            });
//            d.show();
//        } catch (Exception e){
//            context.makeErrorDialog(e.getMessage());
//        }
//    }
//
//
//    class EnviarAbonoTask extends AsyncTask<JSONObject, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Realizando el pago...");
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(JSONObject... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                return networkHelper.writeService(params[0], SincroHelper.ABONO_CREDITO);
//            } catch (Exception e) {
//                return e.getMessage();
//            }
//        }
//
//        @Override
//        protected void onProgressUpdate(String... values) {
//            pd.setMessage(values[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            pd.dismiss();
//            procesarResultAbono(result);
//        }
//    }
//
//    private void procesarResultAbono(String result) {
//        try {
//            result = SincroHelper.procesarJsonCrearSolicitudAhorro(result);
//            AlertDialog.Builder d = new AlertDialog.Builder(context);
//            d.setTitle(context.getResources().getString(R.string.app_name));
//            d.setIcon(R.mipmap.icon_presente);
//            d.setMessage(result);
//            d.setCancelable(false);
//            d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    context.getState().getmTabHost().setCurrentTab(1);
//                }
//            });
//            d.show();
//        } catch (ErrorTokenException e) {
//            AlertDialog.Builder d = new AlertDialog.Builder(context);
//            d.setTitle("Sesión finalizada");
//            d.setIcon(R.mipmap.icon_presente);
//            d.setMessage(e.getMessage());
//            d.setCancelable(false);
//            d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    context.salir();
//                }
//            });
//            d.show();
//        } catch (Exception e) {
//            context.makeErrorDialog(e.getMessage());
//        }
//    }

    //    public void _formter(int len, String textToEdit, EditText txtEdit){
//        DecimalFormat fr;
//        DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
//        separadoresPersonalizados.setDecimalSeparator(',');
//
//        switch (len){
//
//            case 1:
//                fr = new DecimalFormat("#", separadoresPersonalizados);
//                String fa = fr.format(Long.parseLong(textToEdit));
//                txtEdit.setText(fa);
//                break;
//
//            case 2:
//                fr = new DecimalFormat("##", separadoresPersonalizados);
//                txtEdit.setText( fr.format(Long.parseLong(textToEdit)));
//                break;
//
//            case 3:
//                fr = new DecimalFormat("###", separadoresPersonalizados);
//                txtEdit.setText( fr.format(Long.parseLong(textToEdit)));
//                break;
//
//            case 4:
//                fr = new DecimalFormat("#,###", separadoresPersonalizados);
//                txtEdit.setText( fr.format(Long.parseLong(textToEdit)));
//                break;
//
//            case 5:
//                fr = new DecimalFormat("##,###", separadoresPersonalizados);
//                txtEdit.setText( fr.format(Long.parseLong(textToEdit)));
//                break;
//
//            case 6:
//                fr = new DecimalFormat("###,###", separadoresPersonalizados);
//                txtEdit.setText( fr.format(Long.parseLong(textToEdit)));
//                break;
//
//            case 7:
//                fr = new DecimalFormat("#,###,###", separadoresPersonalizados);
//                txtEdit.setText( fr.format(Long.parseLong(textToEdit)));
//                break;
//
//            case 8:
//                fr = new DecimalFormat("##,###,###", separadoresPersonalizados);
//                txtEdit.setText( fr.format(Long.parseLong(textToEdit)));
//                break;
//
//            case 9:
//                fr = new DecimalFormat("###,###,###", separadoresPersonalizados);
//                txtEdit.setText( fr.format(Long.parseLong(textToEdit)));
//                break;
//
//            case 10:
//                fr = new DecimalFormat("#,###,###,###", separadoresPersonalizados);
//                txtEdit.setText( fr.format(Long.parseLong(textToEdit)));
//                break;
//        }
//
//        txtEdit.setSelection(txtEdit.length());
//    }
}
