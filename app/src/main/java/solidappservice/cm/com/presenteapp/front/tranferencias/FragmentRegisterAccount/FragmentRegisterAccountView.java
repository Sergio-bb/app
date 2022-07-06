package solidappservice.cm.com.presenteapp.front.tranferencias.FragmentRegisterAccount;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.entities.transferencias.response.ResponseBanco;
import solidappservice.cm.com.presenteapp.entities.transferencias.response.ResponseCuentasInscritas;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.transferencias.request.RequestRegisterAccount;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 20/05/2016.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 11/09/2021.
 */
public class FragmentRegisterAccountView extends Fragment implements FragmentRegisterAccountContract.View{

    private FragmentRegisterAccountPresenter presenter;
    private ActivityTabsView context;
    private GlobalState state;
    private ProgressDialog pd;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.spBancoDestino)
    Spinner spBancoDestino;
    @BindView(R.id.txtCedulaDestinatario)
    EditText txtCedulaDestinatario;
    @BindView(R.id.txtCuentaDestinatario)
    EditText txtCuentaDestinatario;
    @BindView(R.id.txtNombreCuentaDestinatario)
    EditText txtNombreCuentaDestinatario;
    @BindView(R.id.btnInscribirCuenta)
    Button btnInscribirCuenta;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de inscripcion cuentas");
        firebaseAnalytics.logEvent("pantalla_inscripcion_cuentas", params);
        context = (ActivityTabsView) getActivity();
        View view = inflater.inflate(R.layout.fragment_transfers_registeraccount,container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentRegisterAccountPresenter(this, new FragmentRegisterAccountModel());
        context = (ActivityTabsView) getActivity();
        state = context.getState();
        pd = new ProgressDialog(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null || state.getUsuario() == null){
            context.salir();
        }else {
            List<ResponseBanco> responseBancos = state.getBancos();
            if(responseBancos != null && responseBancos.size() > 0){
//                cargarBancos(bancos);
                showBanks(responseBancos);
            }else{
//                new ConsultarCuentasTask().execute();
                fetchBanks();
            }
        }
    }

    @OnTextChanged(R.id.txtCedulaDestinatario)
    public void afterTextChanged(Editable arg0) {
        enabledRegisterAccountButton();
    }

    @OnClick(R.id.btnInscribirCuenta)
    public void onClickRegisterAccount(){
//        InscribirCuenta();
        if(!TextUtils.isEmpty(txtCedulaDestinatario.getText()) &&
                txtCedulaDestinatario.getText().toString().equals(state.getUsuario().getCedula())){
            txtCedulaDestinatario.setError("No es posible inscribir tu propia cuenta");
        }else{
            fetchRegisteredAccounts();
        }
    }

    @Override
    public void fetchBanks(){
        try{
            presenter.fetchBanks();
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void showBanks(List<ResponseBanco> listaResponseBancos){
        state.setBancos(listaResponseBancos);
        ArrayAdapter<ResponseBanco> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, android.R.id.text1, listaResponseBancos);
        spBancoDestino.setAdapter(adapter);
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
    public void validateRepeatAccounts(List<ResponseCuentasInscritas> responseCuentasInscritas){
        boolean isCuentaRepetida = false;
        if(responseCuentasInscritas != null){
            for (ResponseCuentasInscritas ci: responseCuentasInscritas) {
                if (ci.getAanumnit().equals(txtCedulaDestinatario.getText().toString())) {
                    isCuentaRepetida = true;
                }
            }
            if(!isCuentaRepetida){
//                new InscribirCuentaTask().execute(jsonInscribirCuentas);
                registerAccount();
            } else{
                context.makeErrorDialog("Esta cuenta ya se encuentra inscrita");
            }
        }
    }

    @Override
    public void registerAccount(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            String idDispositivo = context.obtenerIdDispositivo();
            String cedulaInscripcion = txtCedulaDestinatario.getText().toString();
            String nombreCuenta = txtNombreCuentaDestinatario.getText().toString();

            if(TextUtils.isEmpty(idDispositivo)
                    || TextUtils.isEmpty(nombreCuenta)
                    || TextUtils.isEmpty(cedulaInscripcion)){
                context.makeDialog("Todos los campos son obligatorios, por favor verifica los datos ingresados");
                return;
            }
            presenter.registerAccount(new RequestRegisterAccount(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    idDispositivo,
                    "A",
                    nombreCuenta,
                    encripcion.encriptar(((ResponseBanco)spBancoDestino.getSelectedItem()).getCodigo()),
                    "0",
                    encripcion.encriptar(cedulaInscripcion)
            ));
        }catch (Exception ex){
            enabledRegisterAccountButton();
            showDataFetchError("");
        }
    }

    @Override
    public void showResultRegisterAccount(){
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle(context.getResources().getString(R.string.app_name));
        d.setIcon(R.mipmap.icon_presente);
        View view = context.getCurrentFocus();
        if(view!=null){
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        d.setMessage("Tu solicitud se envió con éxito");
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                context.onBackPressed();
            }
        });
        txtCedulaDestinatario.setText("");
        txtCuentaDestinatario.setText("");
        txtNombreCuentaDestinatario.setText("");
        d.show();
    }

    @Override
    public void disabledRegisterAccountButton() {
        btnInscribirCuenta.setEnabled(false);
    }

    @Override
    public void enabledRegisterAccountButton(){
        btnInscribirCuenta.setEnabled(true);
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
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_17_TRANSFERS_MENU_TAG);
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
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_17_TRANSFERS_MENU_TAG);
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

//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//
//        switch (id) {
//            case R.id.btnInscribirCuenta:
//                InscribirCuenta();
//                break;
//        }
//    }
//
//    public void cargarBancos(ArrayList<Banco> lista){
//        ArrayAdapter<Banco> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, android.R.id.text1, lista);
//        spBancoDestino.setAdapter(adapter);
//    }
//
//    private class ConsultarCuentasTask extends AsyncTask<String, String, String> {
//
//        String jsonBancos;
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
//                jsonBancos =  networkHelper.readService(SincroHelper.CONSULTA_BANCOS);
//                return "";
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
//            procesarJsonRespuesta(jsonBancos);
//        }
//    }
//
//    private void procesarJsonRespuesta(String jsonBancos) {
//        try {
//            ArrayList<Banco> listadoBancos = SincroHelper.procesarJsonBancos(jsonBancos);
//            cargarBancos(listadoBancos);
//            context.getState().setBancos(listadoBancos);
//        }  catch (Exception e) {
//            context.makeErrorDialog(e.getMessage());
//        }
//    }
//
//
//
//    private void InscribirCuenta() {
//
//        try {
//            GlobalState state = context.getState();
//            Usuario usuario = state.getUsuario();
//            if (usuario == null) {
//                return;
//            }
//
//            String idDispositivo = context.obtenerIdDispositivo();
//            String cedulaInscripcion = txtCedulaDestinatario.getText().toString();
//            String nombreCuenta = txtNombreCuentaDestinatario.getText().toString();
//
//            if(TextUtils.isEmpty(idDispositivo)
//                    || TextUtils.isEmpty(nombreCuenta)
//                    || TextUtils.isEmpty(cedulaInscripcion)){
//                context.makeDialog("Todos los campos son obligatorios, por favor verifica los datos ingresados");
//                return;
//            }
//
//            JSONObject param = new JSONObject();
//            param.put("cedula", Encripcion.getInstance().encriptar(usuario.cedula));
//            param.put("token", usuario.token);
//            param.put("idDispositivo", idDispositivo);
//            param.put("tipoCuenta", "A");
//            param.put("nombreCuenta", nombreCuenta);
//            param.put("codigoBanco", Encripcion.getInstance().encriptar(((Banco)spBancoDestino.getSelectedItem()).Codigo));
//            param.put("numeroCuenta", "0");
//            param.put("cedulaInscripcion", Encripcion.getInstance().encriptar(cedulaInscripcion));
//            new ConsultarCuentasInscritasTask().execute(param);
//
//        } catch (Exception e) {
//            context.makeErrorDialog("Se produjo un error en la inscripción de la cuenta, por favor intenta nuevamente");
//        }
//    }
//
//
//    private class ConsultarCuentasInscritasTask extends AsyncTask<JSONObject, String, String> {
//
//        String jsonCuentasInscritas;
//        JSONObject jsonInscribirCuentas;
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setMessage("Consultando cuenta...");
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(JSONObject... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper(context.obtenerConfiguracionSecureUrl());
//                jsonInscribirCuentas = params[0];
//
//                JSONObject param = new JSONObject();
//                param.put("cedula", jsonInscribirCuentas.get("cedula").toString());
//                param.put("token", jsonInscribirCuentas.get("token").toString());
//                jsonCuentasInscritas = networkHelper.writeService(param, SincroHelper.CONSULTA_CUENTAS_INSCRITAS);
//                return "OK";
//
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
//            procesarJsonProductos(jsonCuentasInscritas, jsonInscribirCuentas);
//        }
//    }
//
//    private void procesarJsonProductos(String jsonCuentasInscritas, JSONObject jsonInscribirCuentas) {
//        try {
//            ArrayList<CuentasInscritas> listado_cuentas_inscritas = SincroHelper.procesarJsonCuentasInscritas(jsonCuentasInscritas);
//            boolean validarCuentasRepetidas = false;
//
//            if(listado_cuentas_inscritas != null){
//
//                for (CuentasInscritas ci: listado_cuentas_inscritas) {
//                    if (ci.getAanumnit().equals(txtCedulaDestinatario.getText().toString())) {
//                        validarCuentasRepetidas = true;
//                    }
//                }
//
//                if(!validarCuentasRepetidas){
//                    new InscribirCuentaTask().execute(jsonInscribirCuentas);
//                } else{
//                    context.makeErrorDialog("Esta cuenta ya se encuentra inscrita");
//                }
//            }
//
//        }  catch (Exception e) {
//            context.makeErrorDialog(e.getMessage());
//        }
//    }
//
//
////    Tarea que inscribe las cuentas
//    private class InscribirCuentaTask extends AsyncTask<JSONObject, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setMessage("Inscribiendo cuenta...");
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(JSONObject... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper(context.obtenerConfiguracionSecureUrl());
//                return networkHelper.writeService(params[0], SincroHelper.INSCRIBIR_CUENTA);
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
//            procesarJsonRespuestaInscripcion(result);
//        }
//    }
//
//
//    private void procesarJsonRespuestaInscripcion(String jsonRespuesta) {
//
//        try {
//            String result = SincroHelper.procesarJsonCrearSolicitudAhorro(jsonRespuesta);
//            AlertDialog.Builder d = new AlertDialog.Builder(context);
//            d.setTitle(context.getResources().getString(R.string.app_name));
//            d.setIcon(R.mipmap.icon_presente);
//
//            if(result.equalsIgnoreCase("OK"))
//            {
//                View view = context.getCurrentFocus();
//                if(view!=null){
//                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                    if (imm != null) {
//                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                    }
//                }
//                d.setMessage("Tu solicitud se envió con éxito");
//                d.setCancelable(false);
//                d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        context.onBackPressed();
//                    }
//                });
//
//                txtCedulaDestinatario.setText("");
//                txtCuentaDestinatario.setText("");
//                txtNombreCuentaDestinatario.setText("");
//            }else{
//                d.setMessage(result);
//                d.setCancelable(false);
//                d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//
//                    }
//                });
//            }
//            d.show();
//
//        }  catch (Exception e) {
//            context.makeErrorDialog(e.getMessage());
//        }
//    }

}