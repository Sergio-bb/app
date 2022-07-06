package solidappservice.cm.com.presenteapp.front.mismensajes.FragmentReadMessage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;


import butterknife.BindView;
import butterknife.ButterKnife;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.mensajes.request.RequestActualizarMensaje;
import solidappservice.cm.com.presenteapp.entities.mensajes.response.ResponseObtenerMensajes;
import solidappservice.cm.com.presenteapp.entities.login.Response.Usuario;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 30/11/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 15/09/2021.
 */
public class FragmentReadMessageView extends Fragment implements FragmentReadMessageContract.View{

    private FragmentReadMessagePresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private FirebaseAnalytics firebaseAnalytics;
    private ResponseObtenerMensajes mensaje;

    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtPara)
    TextView txtPara;
    @BindView(R.id.webView)
    WebView webView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de leer mensajes");
        firebaseAnalytics.logEvent("pantalla_leer_mensajes", params);
        View view = inflater.inflate(R.layout.fragment_inbox_readmessage, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentReadMessagePresenter(this, new FragmentReadMessageModel());
        context = (ActivityBase) getActivity();
        state = context.getState();
    }

    @Override
    public void onResume() {
        super.onResume();
        mensaje = context.getState().getMensajesBuzonSeleccionado();
        if (mensaje != null) {
            showMessage(mensaje);
        }
    }

    @Override
    public void showMessage(ResponseObtenerMensajes mensaje) {
        txtTitle.setText(mensaje.getTitulo());
        GlobalState state = context.getState();
        if (state == null || state.getUsuario() == null) {
            context.salir();
            return;
        }
        Usuario usuario = state.getUsuario();
        String para = "Para: " + usuario.getNombreAsociado();
        txtPara.setText(para);

        String styles = "<style>" +
                "body{padding:2px 10px;}"+
                ".card{text-align:justify;margin-top:13px;padding:4px 14px;font-size:17px;color:#303030;}" +
                ".separator{height:0.7px;background-color:#CFCFCF;margin-top:29px;margin-left:15px;margin-right:15px;}" +
                "</style>";

        StringBuilder sb = new StringBuilder();
        sb.append("<html>"+styles+"<body>");
        sb.append("<div class='card'>");
        sb.append(mensaje.getContenido());
        sb.append("</div>");
        sb.append("<div class='separator'></div>");
        sb.append("</body></html>");

        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);

        //ACTUALIZAR MENSAJE SI
        if (mensaje.getLeido().equals("N")) {
            fetchUpdateStatusMessage(mensaje.getIdMensaje());
        }
    }

    @Override
    public void fetchUpdateStatusMessage(String idMessage){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchUpdateStatusMessage(new RequestActualizarMensaje(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    idMessage
            ));
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void showUpdateResultStatusMessages(){
        mensaje.setLeido("Y");
        //Contamos y mostramos los mensajes sin leer
        context.showUnreadMessages(mensaje);
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
                context.setFragment(IFragmentCoordinator.Pantalla.MenuPrincipal);
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
                context.setFragment(IFragmentCoordinator.Pantalla.MenuPrincipal);
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
//    private void mostrarMensaje(ResponseObtenerMensajes mensaje) {
//        txtTitle.setText(mensaje.getTitulo());
//        GlobalState state = context.getState();
//        if (state == null || state.getUsuario() == null) {
//            context.salir();
//            return;
//        }
//        Usuario usuario = state.getUsuario();
//        String para = "Para: " + usuario.nombre;
//        txtPara.setText(para);
//        webView.getSettings().setDefaultTextEncodingName("utf-8");
//        webView.loadDataWithBaseURL(null, mensaje.getContenido(), "text/html", "utf-8", null);
//
//        //ACTUALIZAR MENSAJE SI
//        if (mensaje.getLeido().equals("N")) {
//            new ActualizarTask().execute(usuario.cedula, usuario.token, mensaje.getIdMensaje());
//        }
//    }
//
//    private class ActualizarTask extends AsyncTask<String, String, String> {
//
//        String cedula = null;
//        String token = null;
//        String idMensaje = null;
//
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                Encripcion encripcion = Encripcion.getInstance();
//                JSONObject param = new JSONObject();
//                param.put("cedula", cedula = encripcion.encriptar(params[0]));
//                param.put("token", token = params[1]);
//                param.put("idMensaje", idMensaje = params[2]);
//                return networkHelper.writeService(param, SincroHelper.ACTUALIZAR_MENSAJE);
//            } catch (Exception e) {
//                return e.getMessage();
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            try {
//                SincroHelper.procesarJsonActualizarMensaje(result);
//                mensaje.setLeido("Y");
//                //Contamos y mostramos los mensajes sin leer
//                context.showUnreadMessages(mensaje);
//            } catch (ErrorTokenException e) {
//                AlertDialog.Builder d = new AlertDialog.Builder(context);
//                d.setTitle("Sesión finalizada");
//                d.setIcon(R.mipmap.icon_presente);
//                d.setMessage(e.getMessage());
//                d.setCancelable(false);
//                d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        context.salir();
//                    }
//                });
//                d.show();
//            } catch (Exception e) {
//                context.makeSToast("No se ha podido marcar el mensaje como leído");
//            }
//        }
//    }
}
