package solidappservice.cm.com.presenteapp.front.pagoobligaciones.FragmentPaymentCredits;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
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
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
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
//    private ProgressDialog pd;
    private Dialog pd;
    private FirebaseAnalytics firebaseAnalytics;
    private List<ResponseProducto> productsToPay;
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

    @BindView(R.id.contentPagoObligaciones)
    ScrollView contentPagoObligaciones;
    @BindView(R.id.layout_circular_progress_bar)
    LinearLayout layoutCircularProgressBar;
    @BindView(R.id.circular_progress_bar)
    ProgressBar circularProgressBar;
    @BindView(R.id.text_circular_progress_Bar)
    TextView textCircularProgressBar;
    @BindView(R.id.imageReferesh)
    ImageView buttonReferesh;

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
        fetchPendingPayments();
    }

    @OnClick(R.id.imageReferesh)
    public void onClickRefresh(){
        fetchPendingPayments();
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null || state.getUsuario() == null){
            context.salir();
        }
    }


    @OnClick(R.id.btnAceptar)
    public void onClickAceptar(View v) {
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
            ResponseProducto producto = productsToPay.get(positionPayment);
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
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void showResultPendingPayments(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_error);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView titleMessage = (TextView) dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText("No es posible realizar el pago");
        TextView contentMessage = (TextView) dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText("Tienes un pago en proceso, inténtalo de nuevo más tarde.");
        ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                txtValorApagar.setText("");
                txtValorApagar.setEnabled(false);
                enabledCheckboxSaldo(false);
                enabledCheckboxOtroValor(false);
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
            }
        });
        dialog.show();
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
            showDialogError("Lo sentimos", "");
            showErrorWithRefresh();
        }
    }

    @Override
    public void showProductstoDebit(List<ResponseProducto> products){
        state.setProductos(products);
        List<ResponseProducto> productsToDebit = new ArrayList<>();
        ResponseProducto p = new ResponseProducto();
        p.setN_produc("Selecciona la cuenta");
        p.setN_tipodr("");
        p.setA_numdoc("");
        productsToDebit.add(0, p);
        for (ResponseProducto pro : products) {
            if (pro.getI_debito() != null && pro.getI_debito().equals("Y") && pro.getV_saldo() > 0) {
                productsToDebit.add(pro);
            }
        }
        ProductoSpinnerAdapter adapter = new ProductoSpinnerAdapter(context, productsToDebit, true);
        spinnerProductoDebitar.setAdapter(adapter);
        spinnerProductoDebitar.setEnabled(false);
    }

    @Override
    public void showProductstoPay(List<ResponseProducto> products){
        productsToPay = new ArrayList<>();
        ResponseProducto p = new ResponseProducto();
        p.setN_produc("Selecciona el producto");
        p.setN_tipodr("");
        p.setA_numdoc("");
        productsToPay.add(0, p);
        for (ResponseProducto pro : products) {
            if (pro.getI_debito() != null && pro.getI_debito().equals("N")) {
                productsToPay.add(pro);
            }
        }
        if(productsToPay != null && productsToPay.size()>1){
            ProductoSpinnerAdapter adapter = new ProductoSpinnerAdapter(context, productsToPay, false);
            spinnerProducto.setAdapter(adapter);
        }else {
            showDataFetchError("No tienes pagos pendientes", "En este momento no tienes pagos pendientes");
        }
    }

    @Override
    public void validateDataPayment(){
        try {
            int posicion = spinnerProducto.getSelectedItemPosition();
            boolean i_pagsup;

            if (posicion <= 0) {
                showDataFetchError("Datos incpmpletos", "Selecciona el producto a pagar");
                return;
            }

            final ResponseProducto productoApagar = (ResponseProducto) spinnerProducto.getSelectedItem();
            if (productoApagar == null) {
                showDataFetchError("Datos incpmpletos", "Selecciona el producto a pagar");
                return;
            }

            if (!rbSaldo.isChecked() && !rbOtroVal.isChecked()) {
                showDataFetchError("Datos incpmpletos", "Selecciona una opción de pago");
                return;
            }

            Editable valor = txtValorApagar.getText();
            if (valor == null || TextUtils.isEmpty(valor)) {
                showDataFetchError("Datos incpmpletos", "Ingresa el valor a pagar");
                return;
            }

            posicion = spinnerProductoDebitar.getSelectedItemPosition();
            if (posicion <= 0) {
                showDataFetchError("Datos incpmpletos", "Selecciona la cuenta a debitar");
                return;
            }

            final ResponseProducto productoAdebitar = (ResponseProducto) spinnerProductoDebitar.getSelectedItem();
            if (productoAdebitar == null) {
                showDataFetchError("Datos incpmpletos", "Selecciona la cuenta a debitar");
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
                    showDataFetchError("Datos incpmpletos", "Verifica el valor a pagar, ingrese un valor correcto");
                    return;
                }
            }

            i_pagsup = productoApagar.getI_pagsup() != null && productoApagar.getI_pagsup().equals("Y");

            if (rbOtroVal.isChecked()) {
                if (!i_pagsup) {

                    if (d_valor > productoApagar.getV_saldo()) {
                        showDataFetchError("Datos incpmpletos", "Ingresa un valor a pagar inferior o igual al saldo");
                        return;
                    }
                }
            }

            if (d_valor > productoAdebitar.getV_saldo()) {
                showDataFetchError("Saldo insuficiente", "Tu saldo en esta cuenta es insuficiente para realizar este abono");
                return;
            }

            final double _valorAbono = d_valor;
            showDialogConfirmPayment(_valorAbono, productoApagar, productoAdebitar);

        } catch (Exception e){
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void showDialogConfirmPayment(Double paymentValue, ResponseProducto productToPay, ResponseProducto productoToDebit){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_confirm);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView titleMessage = (TextView) dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText("¿Confirma tu solicitud?");
        TextView contentMessage = (TextView) dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText("Tu abono será de " + context.getMoneda(paymentValue));
        ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.buttonClose);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button buttonAceptar = (Button) dialog.findViewById(R.id.btnAceptar);
        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                makePayment(paymentValue, productToPay, productoToDebit);
            }
        });
        dialog.show();
    }

    @Override
    public void makePayment(Double paymentValue, ResponseProducto productToPay, ResponseProducto productoToDebit){
        try{
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
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void showResultPayment(String resultPayment){
        if(!TextUtils.isEmpty(resultPayment)){
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.pop_up_success);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView titleMessage = (TextView) dialog.findViewById(R.id.titleSuccess);
            titleMessage.setText("Pago exitoso");
            TextView contentMessage = (TextView) dialog.findViewById(R.id.contentSuccess);
            contentMessage.setText(resultPayment);
            ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.buttonClose);
            buttonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    txtValorApagar.setText("");
                    txtValorApagar.setEnabled(false);
                    enabledCheckboxSaldo(false);
                    enabledCheckboxOtroValor(false);
                    dialog.dismiss();
                    context.getState().getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
                }
            });
            dialog.show();
        }else{
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.pop_up_error);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView titleMessage = (TextView) dialog.findViewById(R.id.lbl_title_message);
            titleMessage.setText("Lo sentimos");
            TextView contentMessage = (TextView) dialog.findViewById(R.id.lbl_content_message);
            contentMessage.setText("Tu pago no se ha realizado con exito, inténtalo nuevamente en unos minutos.");
            ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.button_close);
            buttonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    txtValorApagar.setText("");
                    txtValorApagar.setEnabled(false);
                    enabledCheckboxSaldo(false);
                    enabledCheckboxOtroValor(false);
                    dialog.dismiss();
                    state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
                }
            });
            dialog.show();
        }
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
    public void showSectionPaymentsCredits(){
        contentPagoObligaciones.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideSectionPaymentsCredits(){
        contentPagoObligaciones.setVisibility(View.GONE);
    }

    @Override
    public void showCircularProgressBar(String textProgressBar) {
        layoutCircularProgressBar.setVisibility(View.VISIBLE);
        textCircularProgressBar.setText(textProgressBar);
    }

    @Override
    public void hideCircularProgressBar() {
        layoutCircularProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorWithRefresh(){
        contentPagoObligaciones.setVisibility(View.GONE);
        layoutCircularProgressBar.setVisibility(View.VISIBLE);
        circularProgressBar.setVisibility(View.GONE);
        textCircularProgressBar.setText("Ha ocurrido un error, inténtalo de nuevo ");
        buttonReferesh.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgressDialog(String message) {
        pd = new Dialog(context);
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.setCanceledOnTouchOutside(false);
        pd.setContentView(R.layout.pop_up_loading);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView contentMessage = (TextView) pd.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        pd.show();
    }

    @Override
    public void hideProgressDialog() {
        pd.dismiss();
    }

    @Override
    public void showDialogError(String title, String message){
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
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_error);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView titleMessage = (TextView) dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText(title);
        TextView contentMessage = (TextView) dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                txtValorApagar.setText("");
                txtValorApagar.setEnabled(false);
                enabledCheckboxSaldo(false);
                enabledCheckboxOtroValor(false);
            }
        });
        dialog.show();
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
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_error);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView titleMessage = (TextView) dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText("Lo sentimos");
        TextView contentMessage = (TextView) dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtValorApagar.setText("");
                txtValorApagar.setEnabled(false);
                enabledCheckboxSaldo(false);
                enabledCheckboxOtroValor(false);
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void showDataFetchError(String title, String message){
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
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_error);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView titleMessage = (TextView) dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText(title);
        TextView contentMessage = (TextView) dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtValorApagar.setText("");
                txtValorApagar.setEnabled(false);
                enabledCheckboxSaldo(false);
                enabledCheckboxOtroValor(false);
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void showExpiredToken(String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_closedsession);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button buttonClosedSession = (Button) dialog.findViewById(R.id.btnVolverAIngresar);
        buttonClosedSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                context.salir();
            }
        });
        dialog.show();
    }
}
