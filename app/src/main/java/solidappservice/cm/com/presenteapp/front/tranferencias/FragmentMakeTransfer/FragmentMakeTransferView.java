package solidappservice.cm.com.presenteapp.front.tranferencias.FragmentMakeTransfer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.pagoobligaciones.ProductoSpinnerAdapter;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.entities.transferencias.request.RequestMakeTransfer;
import solidappservice.cm.com.presenteapp.entities.transferencias.response.ResponseCuentasInscritas;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProductos;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.helpers.NumberTextWatcher;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 03/11/2016.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 11/09/2021.
 */
public class FragmentMakeTransferView extends Fragment implements FragmentMakeTransferContract.View {

    private FragmentMakeTransferPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private ProgressDialog pd;
    private FirebaseAnalytics firebaseAnalytics;
    private List<ResponseProductos> cuentasPropias;

    @BindView(R.id.spCuentaOrigen)
    Spinner spCuentaOrigen;
    @BindView(R.id.spCuentaDestino)
    Spinner spCuentaDestino;
    @BindView(R.id.txtValorTransferencia)
    EditText txtValorTransferencia;
    @BindView(R.id.button_transferir)
    Button buttonMakeTransfer;
    @BindView(R.id.txtAvaliable)
    TextView txtAvaliable;
    @BindView(R.id.txtAvaliableSaldo)
    TextView txtAvaliableSaldo;
    @BindView(R.id.tv_cuenta_destino)
    TextView tv_cuenta_destino;
    @BindView(R.id.tv_valor_transferencia)
    TextView tv_valor_transferencia;
    @BindView(R.id.lbl_inscribir_cuenta)
    TextView lblInscribirCuenta;
    @BindView(R.id.lbl_borrar_cuenta_inscrita)
    TextView lblborrarCuenta;
    @BindView(R.id.txt3)
    TextView txt3;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de transferencia");
        firebaseAnalytics.logEvent("pantalla_transferencia", params);
        View view = inflater.inflate(R.layout.fragment_transfers_maketransfer, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentMakeTransferPresenter(this, new FragmentMakeTransferModel());
        context = (ActivityTabsView) getActivity();
        state = context.getState();
        pd = new ProgressDialog(context);

        spCuentaDestino.setEnabled(false);
        String vlrMinimo = "Valor mínimo a transferir: $5.000 ";
        String vlrDisponible = "Valor disponible: ";
        String vlrCero = "$0.00";
        if (state.getUsuario() != null){
            txt3.setText(vlrMinimo);
        }else {
            context.onBackPressed();
        }
        txtAvaliable.setText(vlrDisponible);
        txtAvaliableSaldo.setText(vlrCero);
        txtValorTransferencia.addTextChangedListener(new NumberTextWatcher(txtValorTransferencia));
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null || state.getUsuario() == null){
            context.salir();
        }else{
//            cargarCuentas();
            fetchIncompleteTransfers();
        }
    }

    @OnItemSelected(R.id.spCuentaOrigen)
    public void onItemSelectedOriginAccount(AdapterView<?> parent, View view, int position, long id){
        String vlrDisponible = "Valor disponible: ";
        String vlrCero = "$0.00";
        if (position != 0){
            double tope = this.cuentasPropias.get(position).getV_transf();
            tv_cuenta_destino.setTextColor(Color.parseColor("#444444"));
            txtAvaliable.setText(vlrDisponible);
            txtAvaliableSaldo.setText(context.getMoneda(tope));
            spCuentaDestino.setEnabled(true);
        }else{
            tv_cuenta_destino.setTextColor(Color.parseColor("#cccccc"));
            spCuentaDestino.setSelection(0);
            spCuentaDestino.setEnabled(false);
            txtAvaliable.setText(vlrDisponible);
            txtAvaliableSaldo.setText(vlrCero);
        }
    }

    @OnItemSelected(R.id.spCuentaDestino)
    public void onItemSelectedDestinationAccount(AdapterView<?> parent, View view, int position, long id){
        if(position > 0){
            txtValorTransferencia.setEnabled(true);
            tv_valor_transferencia.setTextColor(Color.parseColor("#000000"));
        } else {
            tv_valor_transferencia.setTextColor(Color.parseColor("#cccccc"));
            txtValorTransferencia.setText("");
            txtValorTransferencia.setEnabled(false);
        }
    }

    @OnTextChanged(R.id.txtValorTransferencia)
    public void afterTextChanged(Editable arg0) {
        enabledMakeTransferButton();
    }

    @OnClick(R.id.button_transferir)
    public void onClickMakeTransfer(){
//        transferir();
        validateDataTransfer();
    }

    @OnClick(R.id.lbl_inscribir_cuenta)
    public void onClicRegisterAccount(){
        state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_15_TRANSFERS_REGISTER_ACCOUNT_TAG);
    }

    @OnClick(R.id.lbl_borrar_cuenta_inscrita)
    public void onClickDeleteAccount(){
        state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_16_TRANSFERS_DELETE_ACCOUNT_TAG);
    }

    @Override
    public void fetchIncompleteTransfers(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchIncompleteTransfers(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void showResultIncompleteTransfers(){
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle("No es posible realizar la transferencia");
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage("Tienes una transferencia en proceso, inténtalo de nuevo más tarde.");
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.getState().getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
                dialog.dismiss();
            }
        });
        d.show();
    }

    @Override
    public void fetchRegisteredAccounts(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchRegisteredAccounts(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void showRegisteredAccounts(List<ResponseCuentasInscritas> cuentasInscritas){
        try {
            ResponseCuentasInscritas first = new ResponseCuentasInscritas();
            first.setNnasocia("");
            first.setN_numcta("Seleccionar un producto");
            cuentasInscritas.add(0, first);
            if (spCuentaDestino != null) {
                ArrayAdapter<ResponseCuentasInscritas> adapter = new ArrayAdapter<ResponseCuentasInscritas>(context, R.layout.list_item_spinner, cuentasInscritas);
                spCuentaDestino.setAdapter(adapter);
            }
        } catch (Exception ex) {
            context.makeErrorDialog("Error cargando las cuentas destino");
        }
    }

    @Override
    public void fetchAccounts(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchAccounts(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void showAccounts(List<ResponseProductos> cuentasPropias){
        try {
            if (spCuentaOrigen != null) {
                List<ResponseProductos> cuentasPropiasFinal = new ArrayList<>();
                ResponseProductos first = new ResponseProductos();
                first.setN_tipodr("Seleccionar un producto");
                first.setN_produc("Seleccionar un producto");
                first.setA_numdoc("");
                cuentasPropiasFinal.add(first);
                for (ResponseProductos ec : cuentasPropias) {
                    if (ec.getI_debito() != null
                            && ec.getI_debito().equals("Y")) {
                        cuentasPropiasFinal.add(ec);
                    }
                }
                ProductoSpinnerAdapter adapter = new ProductoSpinnerAdapter(context, cuentasPropiasFinal, true);
                spCuentaOrigen.setAdapter(adapter);
                this.cuentasPropias = cuentasPropiasFinal;
            }
        } catch (Exception ex) {
            context.makeErrorDialog("Error cargando las cuentas de origen");
        }
    }

    @Override
    public void validateDataTransfer(){
        try {
            Object _cuentaOrigen = spCuentaOrigen.getSelectedItem();
            Object _cuentaDestino = spCuentaDestino.getSelectedItem();

            ResponseProductos cuentaOrigen = null;
            ResponseCuentasInscritas cuentaDestino = null;

            if (_cuentaDestino != null && _cuentaOrigen != null) {
                cuentaOrigen = (ResponseProductos) _cuentaOrigen;
                cuentaDestino = (ResponseCuentasInscritas) _cuentaDestino;
            }

            if (cuentaOrigen == null || cuentaDestino == null
                    || cuentaOrigen.getN_tipodr().equals("")
                    || cuentaDestino.getNnasocia().equals("")) {
                context.makeErrorDialog(context.getResources().getString(R.string.error_cuenta_origen));
                return;
            }

            double valorTransferencia;
            if (txtValorTransferencia.getText() == null
                    || TextUtils.isEmpty(txtValorTransferencia.getText().toString())) {
                context.makeErrorDialog(context.getResources().getString(R.string.error_valor_transfer));
                return;
            } else {
                try {
                    String cleanString = txtValorTransferencia.getText().toString().replaceAll("[$,.]", "");
                    valorTransferencia = Double.parseDouble(cleanString);
                } catch (Exception e) {
                    context.makeErrorDialog(context.getResources().getString(R.string.error_valor_transfer));
                    return;
                }
            }

            if (valorTransferencia <= 0) {
                context.makeErrorDialog(context.getResources().getString(R.string.error_valor_transfer));
                return;
            }

            if (valorTransferencia < 5000) {
                context.makeErrorDialog(context.getResources().getString(R.string.error_valor_min_transfer));
                return;
            }

            double v_transf = cuentaOrigen.getV_transf();
            if (valorTransferencia > v_transf){
                context.makeDialog("Atención",context.getResources().getString(R.string.alerta_valor_disponible) + ": " + context.getMoneda(cuentaOrigen.getV_transf()));
                return;
            }

            final double _valorTransferencia = valorTransferencia;
            final ResponseProductos auxcuentaOrigen = cuentaOrigen;
            final ResponseCuentasInscritas auxcuentaDestino = cuentaDestino;
            showDialogConfirmTransfer(_valorTransferencia, auxcuentaOrigen, auxcuentaDestino);


        } catch (Exception ex) {
            context.makeErrorDialog(ex.getMessage());
        }
    }

    @Override
    public void showDialogConfirmTransfer(Double valorTransferencia, ResponseProductos cuentaOrigen, ResponseCuentasInscritas cuentaDestino){
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle(context.getResources().getString(R.string.app_name));
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage("Tu transferencia será por un valor de " + context.getMoneda(valorTransferencia));
        d.setCancelable(false);
        d.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            makeTransfer(valorTransferencia, cuentaOrigen, cuentaDestino);
                        } catch (Exception e) {
                            context.makeErrorDialog(e.getMessage());
                        }
                    }
                });
        d.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        d.show();
    }

    @Override
    public void makeTransfer(Double valorTransferencia, ResponseProductos cuentaOrigen, ResponseCuentasInscritas cuentaDestino){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.makeTransfer(new RequestMakeTransfer(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    context.obtenerIdDispositivo(),
                    cuentaDestino.getAanumnit_o(),
                    cuentaOrigen.getA_numdoc(),
                    valorTransferencia,
                    cuentaDestino.getK_idterc(),
                    cuentaDestino.getK_idterc_tit(),
                    cuentaDestino.getN_numcta(),
                    new Date().getTime()
            ));
        }catch (Exception ex){
            enabledMakeTransferButton();
            showDataFetchError("");
        }
    }

    @Override
    public void showResultTransfer(String resultMessage){
        try {
            AlertDialog.Builder d = new AlertDialog.Builder(context);
            d.setTitle(context.getResources().getString(R.string.app_name));
            d.setIcon(R.mipmap.icon_presente);
            d.setMessage(resultMessage);
            d.setCancelable(false);
            d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
                }
            });
            d.show();
        } catch (Exception e) {
            context.makeErrorDialog(e.getMessage());
        }
    }

    @Override
    public void disabledMakeTransferButton() {
        buttonMakeTransfer.setEnabled(false);
    }

    @Override
    public void enabledMakeTransferButton(){
        buttonMakeTransfer.setEnabled(true);
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
//    private void cargarCuentas() {
//        GlobalState state = context.getState();
//        Usuario usuario = state.getUsuario();
//        new ConsultarCuentasTask().execute(usuario.cedula, usuario.token);
//    }
//
//    class ConsultarCuentasTask extends AsyncTask<String, String, String> {
//
//        String jsonEstadoCuenta;
//        String jsonCuentasInscritas;
//        String jsonTransferenciasPendientes = null;
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setMessage("Consultando cuentas...");
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper(context.obtenerConfiguracionSecureUrl());
//
//                JSONObject param = new JSONObject();
//                param.put("cedula", Encripcion.getInstance().encriptar(params[0]));
//                param.put("token", params[1]);
//
//                jsonEstadoCuenta = networkHelper.writeService(param,
//                        SincroHelper.CONSULTAR_CUENTAS);
////                jsonEstadoCuenta = networkHelper.writeService(param,
////                        SincroHelper.ESTADO_CUENTA);
//
//                jsonCuentasInscritas = networkHelper.writeService(param,
//                        SincroHelper.CONSULTA_CUENTAS_INSCRITAS);
//
//                jsonTransferenciasPendientes = networkHelper.writeService(param,
//                        SincroHelper.CONSULTAR_TRANSFERENCIAS);
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
//            procesarJsonProductos(jsonEstadoCuenta, jsonCuentasInscritas, jsonTransferenciasPendientes);
//        }
//    }
//
//    private void procesarJsonProductos(String jsonEstadoCuenta, String jsonCuentasInscritas, String jsonTransferenciasPendientes) {
//        try {
//
//            ArrayList<Transferencias> transferencias = SincroHelper.procesarJsonObtenerTransferenciasPendientes(jsonTransferenciasPendientes);
//
//            if(transferencias != null && transferencias.size()>0){
//                AlertDialog.Builder d = new AlertDialog.Builder(context);
//                d.setTitle("No es posible realizar la transferencia");
//                d.setIcon(R.mipmap.icon_presente);
//                d.setMessage("Tienes una transferencia en proceso, inténtalo de nuevo más tarde.");
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
//                ArrayList<ResponseCuentasInscritas> listado_cuentas_inscritas
//                        = SincroHelper.procesarJsonCuentasInscritas(jsonCuentasInscritas);
//                ArrayList<ResponseProducto> listado_estado_cuenta
//                        = SincroHelper.procesarJsonEstadoCuenta(jsonEstadoCuenta);
//                cargarCuentasPropias(listado_estado_cuenta);
//                cargarCuentasInscritas(listado_cuentas_inscritas);
//            }
//        }  catch (Exception e) {
//            context.makeErrorDialog(e.getMessage());
//        }
//    }
//
//    private void cargarCuentasPropias( ArrayList<ResponseProducto> listado_estado_cuenta) {
//        try {
//            if (spCuentaOrigen != null) {
//                final ArrayList<ResponseProducto> listado_selected = new ArrayList<ResponseProducto>();
//
//                ResponseProducto first = new ResponseProducto();
//                first.setN_tipodr("Seleccionar un producto");
//                first.setN_produc("Seleccionar un producto");
//                first.setA_numdoc("");
//                listado_selected.add(first);
//
//                for (ResponseProducto ec : listado_estado_cuenta) {
//                    if (ec.getI_debito() != null
//                            && ec.getI_debito().equals("Y")) {
//                        listado_selected.add(ec);
//                    }
//                }
//
//                ProductoSpinnerAdapter adapter = new ProductoSpinnerAdapter(context, listado_selected, true);
//                spCuentaOrigen.setAdapter(adapter);
//                spCuentaOrigen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        String vlrDisponible = "Valor disponible: ";
//                        String vlrCero = "$0.00";
//                        if (position != 0){
//                            double tope = listado_selected.get(position).getV_transf();
//                            tv_cuenta_destino.setTextColor(Color.parseColor("#444444"));
//                            txtAvaliable.setText(vlrDisponible);
//                            txtAvaliableSaldo.setText(context.getMoneda(tope));
//                            spCuentaDestino.setEnabled(true);
//                        }else{
//                            tv_cuenta_destino.setTextColor(Color.parseColor("#cccccc"));
//                            spCuentaDestino.setSelection(0);
//                            spCuentaDestino.setEnabled(false);
//                            txtAvaliable.setText(vlrDisponible);
//                            txtAvaliableSaldo.setText(vlrCero);
//                        }
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });
//            }
//        } catch (Exception ex) {
//            context.makeErrorDialog("Error cargando las cuentas de origen");
//        }
//    }
//
//    private void cargarCuentasInscritas(
//            ArrayList<ResponseCuentasInscritas> listado_cuentas_inscritas) {
//        try {
//
//            ResponseCuentasInscritas first = new ResponseCuentasInscritas();
//            first.setNnasocia("");
//            first.setN_numcta("Seleccionar un producto");
//            listado_cuentas_inscritas.add(0, first);
//
//            if (spCuentaDestino != null) {
//                ArrayAdapter<ResponseCuentasInscritas> adapter = new ArrayAdapter<ResponseCuentasInscritas>(context, R.layout.list_item_spinner,listado_cuentas_inscritas);
//                spCuentaDestino.setAdapter(adapter);
//
//
//            }
//        } catch (Exception ex) {
//            context.makeErrorDialog("Error cargando las cuentas destino");
//        }
//    }
//
//    private class TransferirTask extends AsyncTask<JSONObject, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setMessage("Transfiriendo...");
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(JSONObject... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper(context.obtenerConfiguracionSecureUrl());
//                return networkHelper.writeService(params[0],
//                        SincroHelper.REALIZAR_TRANSFERENCIA);
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
//            procesarResultTransferencia(result);
//        }
//    }
//
//    private void procesarResultTransferencia(String result) {
//        try {
//            result = SincroHelper.procesarJsonCrearSolicitudAhorro(result);
//            AlertDialog.Builder d = new AlertDialog.Builder(context);
//            d.setTitle(context.getResources().getString(R.string.app_name));
//            d.setIcon(R.mipmap.icon_presente);
//            d.setMessage(result);
//            d.setCancelable(false);
//            d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    context.onBackPressed();
//                }
//            });
//            d.show();
//        }  catch (Exception e) {
//            context.makeErrorDialog(e.getMessage());
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        GlobalState state = context.getState();
//        switch (v.getId()) {
//            case R.id.button_transferir:
//                transferir();
//                break;
//            case R.id.lbl_inscribir_cuenta:
//                state.getmTabHost().setCurrentTab(18);
//                break;
//            case R.id.lbl_borrar_cuenta_inscrita:
//                state.getmTabHost().setCurrentTab(19);
//                break;
//            default:
//                break;
//        }
//    }
//
//    private void transferir() {
//        try {
//            Object _cuentaOrigen = spCuentaOrigen.getSelectedItem();
//            Object _cuentaDestino = spCuentaDestino.getSelectedItem();
//
//            ResponseProducto cuentaOrigen = null;
//            ResponseCuentasInscritas cuentaDestino = null;
//
//            if (_cuentaDestino != null && _cuentaOrigen != null) {
//                cuentaOrigen = (ResponseProducto) _cuentaOrigen;
//                cuentaDestino = (ResponseCuentasInscritas) _cuentaDestino;
//            }
//
//            if (cuentaOrigen == null || cuentaDestino == null
//                    || cuentaOrigen.getN_tipodr().equals("")
//                    || cuentaDestino.getNnasocia().equals("")) {
//                context.makeErrorDialog(context.getResources().getString(R.string.error_cuenta_origen));
//                return;
//            }
//
//            double valorTransferencia;
//            if (txtValorTransferencia.getText() == null
//                    || TextUtils.isEmpty(txtValorTransferencia.getText().toString())) {
//                context.makeErrorDialog(context.getResources().getString(R.string.error_valor_transfer));
//                return;
//            } else {
//                try {
//                    String cleanString = txtValorTransferencia.getText().toString().replaceAll("[$,.]", "");
//                    valorTransferencia = Double.parseDouble(cleanString);
//                } catch (Exception e) {
//                    context.makeErrorDialog(context.getResources().getString(R.string.error_valor_transfer));
//                    return;
//                }
//            }
//
//            if (valorTransferencia <= 0) {
//                context.makeErrorDialog(context.getResources().getString(R.string.error_valor_transfer));
//                return;
//            }
//
//            if (valorTransferencia < 5000) {
//                context.makeErrorDialog(context.getResources().getString(R.string.error_valor_min_transfer));
//                return;
//            }
//
//            double v_transf = cuentaOrigen.getV_transf();
//            if (valorTransferencia > v_transf){
//                context.makeDialog("Atención",context.getResources().getString(R.string.alerta_valor_disponible) + ": " + context.getMoneda(cuentaOrigen.getV_transf()));
//                return;
//            }
//

//            final double _valorTransferencia = valorTransferencia;
//            final ResponseProducto auxcuentaOrigen = cuentaOrigen;
//            final ResponseCuentasInscritas auxcuentaDestino = cuentaDestino;
//
//            AlertDialog.Builder d = new AlertDialog.Builder(context);
//            d.setTitle(context.getResources().getString(R.string.app_name));
//            d.setIcon(R.mipmap.icon_presente);
//            d.setMessage("Tu transferencia será por un valor de " + context.getMoneda(valorTransferencia));
//            d.setCancelable(false);
//            d.setPositiveButton("Aceptar",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            try {
//
//                                JSONObject param = new JSONObject();
//                                GlobalState state = context.getState();
//                                Usuario usuario = state.getUsuario();
//                                param.put("cedula", Encripcion.getInstance().encriptar(usuario.cedula));
//                                param.put("token", usuario.token);
//                                param.put("id_dispositivo", context.obtenerIdDispositivo());
//                                param.put("aanumnit_o", auxcuentaDestino.getAanumnit_o());
//                                param.put("a_numdoc", auxcuentaOrigen.getA_numdoc());
//                                param.put("v_valor", _valorTransferencia);
//                                param.put("k_idterc", auxcuentaDestino.getK_idterc());
//                                param.put("k_idterc_tit", auxcuentaDestino.getK_idterc_tit());
//                                param.put("n_numcta", auxcuentaDestino.getN_numcta());
//                                param.put("f_solici", new Date().getTime());
//
//                                new TransferirTask().execute(param);
//
//                            } catch (Exception e) {
//                                context.makeErrorDialog(e.getMessage());
//                            }
//                        }
//                    });
//            d.setNegativeButton("Cancelar",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                        }
//                    });
//            d.show();
//
//        } catch (Exception ex) {
//            context.makeErrorDialog(ex.getMessage());
//        }
//    }
}
