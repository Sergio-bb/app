package solidappservice.cm.com.presenteapp.front.terminosycondiciones.ActivityTermsAndConditions;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.entities.tyc.request.RequestAceptaTyC;
import solidappservice.cm.com.presenteapp.entities.tyc.response.ReponseTyC;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 09/12/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 17/07/2020.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 20/09/2021.
 */
public class ActivityTermsAndConditionsView extends ActivityBase implements DialogInterface.OnCancelListener, ActivityTermsAndConditionsContract.View {

    private ActivityTermsAndConditionsPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private ProgressDialog pd;
    private ReponseTyC termsAndConditions;

    @BindView(R.id.btnAceptarTerminosUso)
    Button btnAceptarTerminosUso;
    @BindView(R.id.btnCancelarTerminosUso)
    Button btnCancelarTerminosUso;
    @BindView(R.id.text)
    TextView textoTerminos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termsandconditions);
        ButterKnife.bind(this);
        setControls();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void setControls() {
        if (android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        presenter = new ActivityTermsAndConditionsPresenter(this, new ActivityTermsAndConditionsModel());
        context = this;
        state = context.getState();
        pd = new ProgressDialog(context);
        if(state.getFragmentActual() != Pantalla.MenuPrincipal){
            btnCancelarTerminosUso.setVisibility(View.GONE);
            btnAceptarTerminosUso.setText("Cerrar");
        }
        showTermsAndConditions();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        guardarConfiguracionTerminos(false);
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        onCancel(null);
    }

    @OnClick(R.id.btnAceptarTerminosUso)
    public void onClickAceptar(View arg0) {
        if(state.getFragmentActual() == Pantalla.MenuPrincipal){
            registerAcceptedTermsAndConditions();
        }else {
            guardarConfiguracionTerminos(true);
            setResult(RESULT_OK);
            finish();
        }
    }

    @OnClick(R.id.btnCancelarTerminosUso)
    public void onClickCancelar(View arg0) {
        if(state.getFragmentActual() == Pantalla.MenuPrincipal){
            state.reiniciarEstado();
        }else{
            guardarConfiguracionTerminos(false);
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    @Override
    public void showTermsAndConditions(){
        ReponseTyC terminos = state.getTerminos();
        this.termsAndConditions = terminos;
        String textoTerminosCondiciones = terminos.getN_termycond().replace("\\n", "<br />");
        textoTerminos.setText(Html.fromHtml(textoTerminosCondiciones));
    }

    @Override
    public void registerAcceptedTermsAndConditions(){
        try{
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Encripcion encripcion = Encripcion.getInstance();
            presenter.registerAcceptedTermsAndConditions(new RequestAceptaTyC(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    termsAndConditions.getK_termycond(),
                    format.format(new Date()),
                    "S",
                    getIP()
            ));
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void resultRegisterAcceptedTermsAndConditions(){
        guardarConfiguracionTerminos(true);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public String getIp(){
        List<InetAddress> addrs;
        String address = "";
        try{
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for(NetworkInterface intf : interfaces){
                addrs = Collections.list(intf.getInetAddresses());
                for(InetAddress addr : addrs){
                    if(!addr.isLoopbackAddress() && addr instanceof Inet4Address){
                        address = addr.getHostAddress().toUpperCase(new Locale("es", "CO"));
                    }
                }
            }
        }catch (Exception e){
            address = "";
        }
        return address;
    }

    @Override
    public void enableAcceptButton(){
        btnAceptarTerminosUso.setEnabled(true);
        btnAceptarTerminosUso.setBackgroundResource(R.drawable.backgroun_border_radius_yellow);
    }

    @Override
    public void disabledAcceptButton(){
        btnAceptarTerminosUso.setEnabled(false);
        btnAceptarTerminosUso.setBackgroundColor(Color.parseColor("#EEECEC"));
    }

    @Override
    public void enableCancelButton(){
        btnCancelarTerminosUso.setEnabled(true);
        btnCancelarTerminosUso.setBackgroundResource(R.drawable.button_menu_ppal);
    }

    @Override
    public void disabledCancelButton(){
        btnCancelarTerminosUso.setEnabled(false);
        btnCancelarTerminosUso.setBackgroundColor(Color.parseColor("#EEECEC"));
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
                if(state.getFragmentActual() == Pantalla.MenuPrincipal){
                    state.reiniciarEstado();
                }else{
                    guardarConfiguracionTerminos(false);
                    setResult(RESULT_CANCELED);
                }
                finish();
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
                if(state.getFragmentActual() == Pantalla.MenuPrincipal){
                    state.reiniciarEstado();
                }else{
                    guardarConfiguracionTerminos(false);
                    setResult(RESULT_CANCELED);
                }
                finish();
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
                if(state.getFragmentActual() == Pantalla.MenuPrincipal){
                    state.reiniciarEstado();
                }else{
                    guardarConfiguracionTerminos(false);
                    setResult(RESULT_CANCELED);
                }
                finish();
            }
        });
        d.show();
    }

    //Inserta los terminos y condiciones del usuario que los acepta
//    private class insertarTerminosUsuarioTask extends AsyncTask<String, String, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                Encripcion encripcion = Encripcion.getInstance();
//                JSONObject param = new JSONObject();
//                param.put("cedula", encripcion.encriptar(params[0]));
//                param.put("token", params[1]);
//                param.put("k_termycond", Integer.parseInt(params[2]));
//                param.put("f_aceptacion", params[3]);
//                param.put("i_aceptacion", params[4]);
//                param.put("ip", params[5]);
//                return networkHelper.writeService(param, SincroHelper.INSERTAR_TERMINOS_ACEPTADOS);
//            } catch (Exception e) {
//                return e.getMessage();
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            procesarResultJSONInsertTerminosUsuario(result);
//            pd.dismiss();
//        }
//    }
//
//    private void procesarResultJSONInsertTerminosUsuario(String result){
//        try {
//            String respuesta = SincroHelper.procesarJsonInsertTerminosUsuario(result);
//
//            if(respuesta.equals("OK")){
//                setResult(RESULT_OK);
//                finish();
//            }else{
//                btnAceptarTerminosUso.setBackgroundResource(R.drawable.backgroun_border_radius_yellow);
//                btnCancelarTerminosUso.setBackgroundResource(R.drawable.button_menu_ppal);
//                btnAceptarTerminosUso.setEnabled(true);
//                btnCancelarTerminosUso.setEnabled(true);
//            }
//
//        } catch (Exception ex) {
//            btnAceptarTerminosUso.setBackgroundResource(R.drawable.backgroun_border_radius_yellow);
//            btnCancelarTerminosUso.setBackgroundResource(R.drawable.button_menu_ppal);
//            btnAceptarTerminosUso.setEnabled(true);
//            btnCancelarTerminosUso.setEnabled(true);
//            ex.printStackTrace();
//        }
//    }
//
//

}