package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.ReplacementCard.FragmentReplacementCardDetail;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.dto.ReposicionTarjeta;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.request.RequestMensajeReposicionTarjeta;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.request.RequestReposicionTarjeta;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseDependenciasAsociado;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 23/07/2020.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public class FragmentReplacementCardDetailView extends Fragment implements FragmentReplacementCardDetailContract.View{

    private FragmentReplacementCardDetailPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private ProgressDialog pd;
    private FirebaseAnalytics firebaseAnalytics;
    private ResponseDependenciasAsociado dependenciasAsociado;

    @BindView(R.id.rt_codigo_dependencia)
    EditText rt_codigo_dependencia;
    @BindView(R.id.rt_nombre_dependencia)
    EditText rt_nombre_dependencia;
    @BindView(R.id.valor_reposicion_tarjeta)
    TextView valor_reposicion_tarjeta;
    @BindView(R.id.btnEditar)
    Button btnEditar;
    @BindView(R.id.btnContinuar)
    Button btnContinuar;

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
        params.putString("Descripción", "Interacción con pantalla de detalle reposición tarjeta pte");
        firebaseAnalytics.logEvent("pantalla_reposicion_detalle_tarjeta_pte", params);
        View view = inflater.inflate(R.layout.fragment_presentecard_replacementdetail, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentReplacementCardDetailPresenter(this, new FragmentReplacementCardDetailModel());
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
        } else {
            fetchAssociatedDependence();
            presenter.fechReplacementCardValue();
//            ObtenerDatosAsociado();
//            new obtenerValorRepoTarjetaTask().execute();
        }
    }

    @OnClick(R.id.btnEditar)
    public void onClickEditar(){
        rt_codigo_dependencia.setEnabled(true);
        rt_codigo_dependencia.setBackgroundResource(R.drawable.backgroun_border_radius);
        rt_nombre_dependencia.setEnabled(true);
        rt_nombre_dependencia.setBackgroundResource(R.drawable.backgroun_border_radius);
    }

    @OnClick(R.id.btnContinuar)
    public void onClickContinuar(){
        Boolean validarDatos = true;

        if(TextUtils.isEmpty(rt_codigo_dependencia.getText())){
            rt_codigo_dependencia.setError("Campo requerido");
            validarDatos = false;
        }

        if(TextUtils.isEmpty(rt_nombre_dependencia.getText())){
            rt_nombre_dependencia.setError("Campo requerido");
            validarDatos = false;
        }

        if(validarDatos){
            showDialogConfirmReplacementCard();
        }
    }

    @Override
    public void showDialogConfirmReplacementCard(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_pregunta_confirmar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final Button buttonaceptar = (Button) dialog.findViewById(R.id.btnAceptar);
        final Button buttoncancelar = (Button) dialog.findViewById(R.id.btnCancelar);
        buttonaceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                solicityReplacementCard();
//                InsertarReposicionTarjeta();
            }
        });
        buttoncancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void fetchAssociatedDependence(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchAssociatedDependence(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch (Exception ex){
            showDataFetchError(ex.getMessage()+"Upps, se ha producido un error, inténtalo nuevamente en unos minutos.");
        }
    }

    @Override
    public void showAssociatedDependence(ResponseDependenciasAsociado dependenciasAsociado){
        try {
//            reposicionTarjeta = state.getReposiciontarjeta();
//            reposicionTarjeta = SincroHelper.procesarJsonReposicionTarjeta(jsonRequisitos, reposicionTarjeta);
            this.dependenciasAsociado = dependenciasAsociado;
            if (dependenciasAsociado != null){
                if((rt_codigo_dependencia.getText().toString().equals("")
                        || rt_codigo_dependencia.getText().toString().equals(dependenciasAsociado.getCodigodependencia()))
                        && (rt_nombre_dependencia.getText().toString().equals("")
                        || rt_nombre_dependencia.getText().toString().equals(dependenciasAsociado.getN_DEPENDENCIA()))){
                    rt_codigo_dependencia.setText(dependenciasAsociado.getCodigodependencia());
                    rt_nombre_dependencia.setText(dependenciasAsociado.getN_DEPENDENCIA());
                }
            }
        } catch (Exception ex) {
            context.makeErrorDialog(ex.getMessage());
        }
    }

    @Override
    public void showReplacementCardValue(int valueReplacementeCard){
        DecimalFormat formato = new DecimalFormat("#,###");
        String valorReposicion = formato.format(valueReplacementeCard);
        valor_reposicion_tarjeta.setText("$"+valorReposicion);
    }

    @Override
    public void solicityReplacementCard(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            ReposicionTarjeta reposicion = state.getReposicionTarjeta();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            String fechasolicitud = format.format(date);
            String i_depmodificada = "S";
            if(rt_codigo_dependencia.getText().toString().equals(dependenciasAsociado.getCodigodependencia()) &&
                    rt_nombre_dependencia.getText().toString().equals(dependenciasAsociado.getN_DEPENDENCIA())){
                i_depmodificada = "N";
            }
            presenter.solicityReplacementCard(new RequestReposicionTarjeta(
                encripcion.encriptar(state.getUsuario().getCedula()),
                state.getUsuario().getToken(),
                dependenciasAsociado.getN_NOMBR1(),
                dependenciasAsociado.getN_NOMBR2(),
                dependenciasAsociado.getN_APELL1(),
                dependenciasAsociado.getN_APELL2(),
                "",
                dependenciasAsociado.getN_CIUDAD(),
                rt_codigo_dependencia.getText().toString(),
                rt_nombre_dependencia.getText().toString(),
                "1111111",
                "GERENTE Y/O ADMINISTRADOR",
                "2",
                reposicion.getD_email(),
                reposicion.getT_telcel(),
                fechasolicitud,
                i_depmodificada
            ));
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void sendMessageCardReplacementSuccessful(){
        try{
            SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            Encripcion encripcion = Encripcion.getInstance();
            String fechaInicio = formatFecha.format(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR , 3);
            String fechaFinal = formatFecha.format(calendar.getTime());
            presenter.sendMessageCardReplacementSuccessful(new RequestMensajeReposicionTarjeta(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    fechaInicio,
                    fechaFinal,
                    "Reposición de Tarjeta Presente",
                    "La solicitud de reposición de tu Tarjeta PRESENTE ha sido exitosa. La nueva tarjeta será enviada en 12 días hábiles a la dependencia que estas inscrito o nos indicaste."
            ));
        }catch (Exception ex){}
    }

    @Override
    public void showResultReplacementCard(){
        sendMessageCardReplacementSuccessful();
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_respuesta_confirmar);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView mensaje = (TextView) dialog.findViewById(R.id.mensaje_popup);
        mensaje.setText(Html.fromHtml("La nueva  tarjeta será enviada en <b>12 días hábiles a la dependencia</b> en la que estás inscrito o nos indicaste."));
        Button buttonAceptar = (Button) dialog.findViewById(R.id.btnAceptar);
        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.getState().getmTabHost().setCurrentTab(ActivityTabsView.TAB_2_PRESENTE_CARD_MENU_TAG);
                dialog.dismiss();
            }
        });
        dialog.show();
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
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_2_PRESENTE_CARD_MENU_TAG);
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
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_2_PRESENTE_CARD_MENU_TAG);
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
//            case R.id.btnEditar:
//                rt_codigo_dependencia.setEnabled(true);
//                rt_codigo_dependencia.setBackgroundResource(R.drawable.backgroun_border_radius);
//                rt_nombre_dependencia.setEnabled(true);
//                rt_nombre_dependencia.setBackgroundResource(R.drawable.backgroun_border_radius);
//                break;
//            case R.id.btnContinuar:
//
//                Boolean validarDatos = true;
//
//                if(TextUtils.isEmpty(rt_codigo_dependencia.getText())){
//                    rt_codigo_dependencia.setError("Campo requerido");
//                    validarDatos = false;
//                }
//
//                if(TextUtils.isEmpty(rt_nombre_dependencia.getText())){
//                    rt_nombre_dependencia.setError("Campo requerido");
//                    validarDatos = false;
//                }
//
//                if(validarDatos){
//
//                    final Dialog dialog = new Dialog(getContext());
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.setContentView(R.layout.pop_up_pregunta_confirmar);
//                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//                    final Button buttonaceptar = (Button) dialog.findViewById(R.id.btnAceptar);
//                    final Button buttoncancelar = (Button) dialog.findViewById(R.id.btnCancelar);
//                    buttonaceptar.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            dialog.dismiss();
//                            InsertarReposicionTarjeta();
//                        }
//                    });
//                    buttoncancelar.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            dialog.dismiss();
//                        }
//                    });
//
//                    dialog.show();
//                    break;
//                } else {
//                    break;
//                }
//        }
//    }
//
//
//
//
//
//    //Obtiene los datos del asociado
//    private void ObtenerDatosAsociado() {
//        GlobalState state = context.getState();
//        Usuario usuario = state.getUsuario();
//        new ObtenerDatosAsociadoTask().execute(usuario.cedula, usuario.token);
//    }
//
//    private class ObtenerDatosAsociadoTask extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Un momento, por favor...");
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
//
//                param.put("cedula", encripcion.encriptar(params[0]));
//                param.put("token", params[1]);
//                return networkHelper.writeService(param, SincroHelper.OBTENER_DATOS_ASOCIADO_TARJETA);
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
//            procesarResultDatosAsociado(result);
//            pd.dismiss();
//        }
//    }
//
//    private void procesarResultDatosAsociado(String jsonRequisitos) {
//        try {
//
//            reposicionTarjeta = state.getReposiciontarjeta();
//            reposicionTarjeta = SincroHelper.procesarJsonReposicionTarjeta(jsonRequisitos, reposicionTarjeta);
//            state.setReposiciontarjeta(reposicionTarjeta);
//
//            if (reposicionTarjeta != null){
//                if((rt_codigo_dependencia.getText().toString().equals("")
//                        || rt_codigo_dependencia.getText().toString().equals(reposicionTarjeta.getK_coddependencia()))
//                        && (rt_nombre_dependencia.getText().toString().equals("")
//                        || rt_nombre_dependencia.getText().toString().equals(reposicionTarjeta.getN_dependencia()))){
//
//                    rt_codigo_dependencia.setText(reposicionTarjeta.getK_coddependencia());
//                    rt_nombre_dependencia.setText(reposicionTarjeta.getN_dependencia());
//                }
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
//        } catch (Exception ex) {
//            context.makeErrorDialog(ex.getMessage());
//        }
//    }
//
//
//    //Obtiene el valor de la reposición de la tarjeta
//    private class obtenerValorRepoTarjetaTask extends AsyncTask<String, String, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                return networkHelper.readService(SincroHelper.OBTENER_VALOR_REPOSICION_TARJETA);
//            } catch (Exception e) {
//                return e.getMessage();
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            procesarResultValorRepoTarjeta(result);
//        }
//    }
//
//    private void procesarResultValorRepoTarjeta(String result) {
//        try {
//            String valorReposicion = SincroHelper.procesarJsonValorRepoTarjeta(result);
//            DecimalFormat formato = new DecimalFormat("#,###");
//            valorReposicion = formato.format(Integer.parseInt(valorReposicion));
//            valor_reposicion_tarjeta.setText("$"+valorReposicion);
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
//        } catch (Exception ex) {
//            context.makeErrorDialog(ex.getMessage());
//        }
//    }
//
//
//
//    //Envia los datos a la tabla
//    private void InsertarReposicionTarjeta() {
//
//        try {
//            GlobalState state = context.getState();
//            Usuario usuario = state.getUsuario();
//            Encripcion encripcion = Encripcion.getInstance();
//            ReposicionTarjeta reposicion = state.getReposiciontarjeta();
//
//            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//            SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yyyy");
//            Date date = new Date();
//
//            String fechasolicitud = format.format(date);
//            String i_depmodificada = "S";
//
//            if(rt_codigo_dependencia.getText().toString().equals(reposicion.getK_coddependencia()) &&
//                    rt_nombre_dependencia.getText().toString().equals(reposicion.getN_dependencia())){
//                i_depmodificada = "N";
//            }
//
//            JSONObject param = new JSONObject();
//            param.put("cedula", encripcion.encriptar(usuario.cedula));
//            param.put("token", usuario.token);
//            param.put("n_nombr1", reposicion.getN_nombr1());
//            param.put("n_nombr2", reposicion.getN_nombr2());
//            param.put("n_apell1", reposicion.getN_apell1());
//            param.put("n_apell2", reposicion.getN_apell2());
//            param.put("n_numplastico", "");
//            param.put("n_ciudad", reposicion.getN_ciudad());
//            param.put("k_coddependencia", rt_codigo_dependencia.getText().toString());
//            param.put("n_dependencia", rt_nombre_dependencia.getText().toString());
//            param.put("k_ccostos", "1111111");
//            param.put("i_estpla", "GERENTE Y/O ADMINISTRADOR");
//            param.put("k_tipotransa", "2");
//            param.put("d_email", reposicion.getD_email());
//            param.put("t_telcel", reposicion.getT_telcel());
//            param.put("f_solicitud", fechasolicitud);
//            param.put("i_depmodificada", i_depmodificada);
//            new InsertarReposicionTarjetaTask().execute(param);
//
//            String fechaInicio = formatFecha.format(date);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            calendar.add(Calendar.DAY_OF_YEAR , 3);
//            String fechaFinal = formatFecha.format(calendar.getTime());
//
//            JSONObject paramMensaje = new JSONObject();
//            paramMensaje.put("cedula", encripcion.encriptar(usuario.cedula));
//            paramMensaje.put("token", usuario.token);
//            paramMensaje.put("f_inicio", fechaInicio);
//            paramMensaje.put("f_final", fechaFinal);
//            paramMensaje.put("n_tipo", "Reposición de Tarjeta Presente");
//            paramMensaje.put("n_mensaj", "La solicitud de reposición de tu Tarjeta PRESENTE ha sido exitosa. La nueva tarjeta será enviada en 12 días hábiles a la dependencia que estas inscrito o nos indicaste.");
//            new EnviarNotificacionTarjetaTask().execute(paramMensaje);
//
//        } catch (Exception e) {
//            e.getMessage();
//        }
//    }
//
//    private class InsertarReposicionTarjetaTask extends AsyncTask<JSONObject, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Enviando solicitud...");
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(JSONObject... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                return networkHelper.writeService(params[0], SincroHelper.INSERTAR_REPOSICION_TARJETA);
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
//            procesarResultInsertarReposicionTarjeta(result);
//            pd.dismiss();
//        }
//    }
//
//    private void procesarResultInsertarReposicionTarjeta(String jsonRequisitos) {
//        try {
//
//            String respuesta = SincroHelper.procesarJsonInsertarReposicionTarjeta(jsonRequisitos);
//
//            if(respuesta.equals("OK")){
//
//                final Dialog dialog = new Dialog(getContext());
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.pop_up_respuesta_confirmar);
//                dialog.setCancelable(false);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//                TextView mensaje = (TextView) dialog.findViewById(R.id.mensaje_popup);
//                mensaje.setText(Html.fromHtml("La nueva  tarjeta será enviada en <b>12 días hábiles a la dependencia</b> en la que estás inscrito o nos indicaste."));
//
//                Button buttonAceptar = (Button) dialog.findViewById(R.id.btnAceptar);
//                buttonAceptar.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        context.getState().getmTabHost().setCurrentTab(2);
//                        dialog.dismiss();
//                    }
//                });
//
//                dialog.show();
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
//        } catch (Exception ex) {
//            context.makeErrorDialog(ex.getMessage());
//        }
//    }
//
//
//    private class EnviarNotificacionTarjetaTask extends AsyncTask<JSONObject, String, String> {
//
//        @Override
//        protected String doInBackground(JSONObject... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                return networkHelper.writeService(params[0], SincroHelper.ENVIAR_NOTIFICACION_REPOSICION_TARJETA);
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
//            procesarResultNotificacionReposicionTarjeta(result);
//            pd.dismiss();
//        }
//    }
//
//    private void procesarResultNotificacionReposicionTarjeta(String jsonRequisitos) {
//        try {
//
//            String respuesta = SincroHelper.procesarJsonNotificacionReposicionTarjeta(jsonRequisitos);
//
//            if(respuesta.equals("OK")){
//
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
//        } catch (Exception ex) {
//            context.makeErrorDialog(ex.getMessage());
//        }
//    }

}
