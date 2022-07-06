package solidappservice.cm.com.presenteapp.front.adelantonomina.FragmentDetail;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestInsertarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestLogs;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestProcesarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 19/10/2020.
 */
public class FragmentDetailView extends Fragment implements FragmentDetailContract.View{

    private FragmentDetailPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private ProgressDialog pd;
    private FirebaseAnalytics firebaseAnalytics;

    private String valorsolicitado;
    private String valorComision;
    private Integer numeroflujo;
    private String aceptacionterminos;
    private String fechaaceptacion;

    @BindView(R.id.lblValor_Solicitado)
    TextView lblValor_Solicitado;
    @BindView(R.id.lblValor_Comision)
    TextView lblValor_Comision;
    @BindView(R.id.btnAdelantarN)
    Button btnAdelantarN;
    @BindView(R.id.chk_acepto_terminosadelanto)
    CheckBox chk_acepto_terminosadelanto;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de adelanto nomina detalle");
        firebaseAnalytics.logEvent("pantalla_adelanto_nomina_detalle", params);
        View view = inflater.inflate(R.layout.fragment_salaryadvance_detail, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentDetailPresenter(this, new FragmentDetailModel());
        context = (ActivityBase) getActivity();
        state = context.getState();
        pd = new ProgressDialog(context);
        DecimalFormat formato = new DecimalFormat("#,###");
        valorsolicitado = formato.format(Integer.parseInt(state.getValorSolicitado()));
        lblValor_Solicitado.setText("Valor solicitado a consignar: $"+valorsolicitado);
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null || state.getUsuario() == null){
            context.salir();
        } else {
            fetchCommissionValue();
        }
    }

    @OnClick(R.id.btnAdelantarN)
    public void onClickAdelantarNomina(){
        fetchRegisterSalaryAdvance();
    }

    @OnCheckedChanged(R.id.chk_acepto_terminosadelanto)
    public void onCheckedChangedAceptoTerminos(CompoundButton buttonView, boolean isChecked){
        if(isChecked){
            btnAdelantarN.setEnabled(true);
            btnAdelantarN.setBackground(getResources().getDrawable(R.drawable.btn_yellow_internal));
            aceptacionterminos = "S";
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            fechaaceptacion = format.format(date);
        }else{
            aceptacionterminos = "N";
            fechaaceptacion = "";
            btnAdelantarN.setEnabled(false);
            btnAdelantarN.setBackgroundColor(getResources().getColor(R.color.gris));
        }
    }

    @Override
    public void fetchCommissionValue(){
        try{
            presenter.fetchCommissionValue();
        }catch (Exception ex){
            showDataFetchError("PRESENTE","");
        }
    }

    @Override
    public void showCommissionValue(String valorComision){
        DecimalFormat formato = new DecimalFormat("#,###");
        this.valorComision = formato.format(Integer.parseInt(valorComision));
        lblValor_Comision.setText(String.format("Costo de la transacción: $%s", this.valorComision));
    }

    @Override
    public void fetchRegisterSalaryAdvance(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date fechasolicitud = new Date();
            Integer valorfinal = Integer.parseInt(valorsolicitado.replaceAll("[,.]", ""))+Integer.parseInt(valorComision.replaceAll("[,.]", ""));
            presenter.fetchRegisterSalaryAdvance(new RequestInsertarAdelantoNomina(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    format.format(fechasolicitud),
                    Integer.parseInt(valorsolicitado.replaceAll("[,.]", "")),
                    valorfinal,
                    state.getTopes().getV_cupo(),
                    "E",
                    "",
                    "",
                    aceptacionterminos,
                    fechaaceptacion,
                    context.getIP()
            ));
        }catch(Exception ex){
            showDataFetchError("PRESENTE","");
        }
    }

    @Override
    public void fetchProcessSalaryAdvance(String numeroTransaccion){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date fechasolicitd = new Date();
            Integer valorfinal = Integer.parseInt(valorsolicitado.replaceAll("[,.]", ""))+Integer.parseInt(valorComision.replaceAll("[,.]", ""));
            presenter.fetchProcessSalaryAdvance(new RequestProcesarAdelantoNomina(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    valorfinal.toString(),
                    format.format(fechasolicitd),
                    numeroTransaccion
            ));
        } catch (Exception ex){
            showDataFetchError("PRESENTE","");
        }
    }

    @Override
    public void enterLogs(String accion, String descripcion){
        try{
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Encripcion encripcion = Encripcion.getInstance();
            presenter.enterLogs(new RequestLogs(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    accion,
                    descripcion,
                    format.format(new Date())
            ));
        }catch(Exception ex){
            showDataFetchError("PRESENTE","");
        }
    }

    @Override
    public void showSuccessfulSalaryAdvance(){
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle("Solicitud Enviada");
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage("Tu solicitud ha sido enviada con éxito. El valor que te será consignado en tu cuenta de nómina es de $" + valorsolicitado +
                " valida la transacción en los movimientos de tu cuenta de nómina en unos minutos.");
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                state.setRequisitos(null);
                state.setTopes(null);
                state.setValorSolicitado(null);
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG); //Regresar al menu principal
            }
        });
        d.show();
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
                state.setRequisitos(null);
                state.setTopes(null);
                state.setValorSolicitado(null);
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
                dialog.dismiss();
            }
        });
        d.show();
    }

    @Override
    public void showDataFetchError(String title, String message) {
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
        d.setTitle(title);
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(message);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                state.setRequisitos(null);
                state.setTopes(null);
                state.setValorSolicitado(null);
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

    //Obtiene el valor de la comisión
//    private class obtenerValorComisionTask extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Validando datos...");
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                return networkHelper.readService(SincroHelper.OBTENER_VALOR_COMISION);
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
//            procesarResultValorComision(result);
//            pd.dismiss();
//        }
//    }
//
//    private void procesarResultValorComision(String result) {
//        try {
//            valorComision = SincroHelper.procesarJsonValorComision(result);
//            DecimalFormat formato = new DecimalFormat("#,###");
//            valorComision = formato.format(Integer.parseInt(valorComision));
//            lblValor_Comision.setText("Costo de la transacción: $"+valorComision);
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
//
//    //Realizar la solicitud de adelanto
//    private void AdelantarNomina() {
//        try{
//            GlobalState state = context.getState();
//            Usuario usuario = state.getUsuario();
//            Encripcion encripcion = Encripcion.getInstance();
//            JSONObject param = new JSONObject();
//
//            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//            Date fechasolicitud = new Date();
//            Integer valorfinal = Integer.parseInt(valorsolicitado.replaceAll("[,.]", ""))+Integer.parseInt(valorComision.replaceAll("[,.]", ""));
//            String ip = getIP();
//
//            param.put("cedula", encripcion.encriptar(usuario.cedula));
//            param.put("token", usuario.token);
//            param.put("f_solicitud", format.format(fechasolicitud));
//            param.put("v_solicitado", Integer.parseInt(valorsolicitado.replaceAll("[,.]", "")));
//            param.put("v_valorcre", valorfinal);
//            param.put("v_cupo", state.getTopes().getV_cupo());
//            param.put("i_estado", "E");
//            param.put("n_error", " ");
//            param.put("k_flujo", " ");
//            param.put("i_aceptacion", aceptacionterminos);
//            param.put("f_aceptacion", fechaaceptacion);
//            param.put("ip", ip);
//
//            new InsertarAdelantarNominaTask().execute(param);
//
//        } catch(Exception ex) {
//            context.makeErrorDialog(ex.getMessage());
//        }
//    }
//
//
//    //Inserta el adelanto de nomina
//    private class InsertarAdelantarNominaTask extends AsyncTask<JSONObject, String, String> {
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
//                return networkHelper.writeService(params[0], SincroHelper.INSERTAR_ADELANTAR_NOMINA);
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
//            procesarResultInsertAdelantarNomina(result);
//        }
//    }
//
//    private void procesarResultInsertAdelantarNomina(String result) {
//        try {
//            String numeroTransa = SincroHelper.procesarJsonInsertarAdelantoNomina(result);
//
//            if(numeroTransa != null) {
//
//                GlobalState state = context.getState();
//                Usuario usuario = state.getUsuario();
//                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//                Date fechasolicitd = new Date();
//                Integer valorfinal = Integer.parseInt(valorsolicitado.replaceAll("[,.]", ""))+Integer.parseInt(valorComision.replaceAll("[,.]", ""));
//
//                Encripcion encripcion = Encripcion.getInstance();
//                JSONObject param = new JSONObject();
//                param.put("cedula", encripcion.encriptar(usuario.cedula));
//                param.put("token", usuario.token);
//                param.put("v_monto", valorfinal.toString());
//                param.put("f_solici", format.format(fechasolicitd));
//                param.put("k_nroSolici", numeroTransa);
//                new AdelantarNominaTask().execute(param);
//
//            } else {
//                Encripcion encripcion = Encripcion.getInstance();
//                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//                Date fecharegistro = new Date();
//
//                JSONObject param = new JSONObject();
//                Usuario usuario = state.getUsuario();
//                param.put("cedula", encripcion.encriptar(usuario.cedula));
//                param.put("token", usuario.token);
//                param.put("n_accion", "ADELANTO DE NOMINA FALLIDO");
//                param.put("n_descr", "A ocurrido un error al insertar la solicitud del adelanto de nómina");
//                param.put("f_registro", format.format(fecharegistro));
//
//                pd.dismiss();
//                new EnviarLogAdelantoTask().execute(param);
//
//                AlertDialog.Builder d = new AlertDialog.Builder(context);
//                d.setTitle("Solicitud Fallida");
//                d.setIcon(R.mipmap.icon_presente);
//                d.setMessage("Tu solicitud no se ha enviado, por favor inténtalo de nuevo más tarde");
//                d.setCancelable(false);
//                d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        state.setRequisitos(null);
//                        state.setTopes(null);
//                        state.setValorSolicitado(null);
//                        state.getmTabHost().setCurrentTab(1); //Regresar al menu principal
//                    }
//                });
//                d.show();
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
//
//            context.makeErrorDialog(ex.getMessage());
//        }
//    }
//
//
//    private class AdelantarNominaTask extends AsyncTask<JSONObject, String, String> {
//
//        @Override
//        protected String doInBackground(JSONObject... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                return networkHelper.writeService(params[0], SincroHelper.ADELANTAR_NOMINA_SOLICITUD);
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
//            procesarResultAdelantarNomina(result);
//        }
//    }
//
//
//    private void procesarResultAdelantarNomina(String result){
//        try {
//            AdelantoNomina resultadoan = SincroHelper.procesarJsonAdelantarNomina(result);
//
//            Encripcion encripcion = Encripcion.getInstance();
//            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//            JSONObject param = new JSONObject();
//            Usuario usuario = state.getUsuario();
//
//            if(resultadoan != null && resultadoan.n_resultado.equals("OK")){
//                param.put("cedula", encripcion.encriptar(usuario.cedula));
//                param.put("token", usuario.token);
//                param.put("n_accion", "ADELANTO DE NOMINA EXITOSA");
//                param.put("n_descr", "Numero de flujo " + numeroflujo==null || numeroflujo==0 ? "" : numeroflujo);
//                param.put("f_registro", format.format(new Date()));
//                new EnviarLogAdelantoTask().execute(param);
//            }else {
//                param = new JSONObject();
//                param.put("cedula", encripcion.encriptar(usuario.cedula));
//                param.put("token", usuario.token);
//                param.put("n_accion", "ADELANTO DE NOMINA ENVIADO PERO SIN RESPUESTA");
//                param.put("n_descr", "El adelanto de nomina se envio, pero no se obtuve respuesta de la base de datos");
//                param.put("f_registro", format.format(new Date()));
//                new EnviarLogAdelantoTask().execute(param);
//            }
//
//            AlertDialog.Builder d = new AlertDialog.Builder(context);
//            d.setTitle("Solicitud Enviada");
//            d.setIcon(R.mipmap.icon_presente);
//            d.setMessage("Tu solicitud ha sido enviada con éxito. El valor que te será consignado en tu cuenta de nómina es de $" + valorsolicitado +
//                    " valida la transacción en los movimientos de tu cuenta de nómina en unos minutos.");
//            d.setCancelable(false);
//            d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    state.setRequisitos(null);
//                    state.setTopes(null);
//                    state.setValorSolicitado(null);
//                    state.getmTabHost().setCurrentTab(1); //Regresar al menu principal
//                }
//            });
//            d.show();
//
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
//
//            Encripcion encripcion = Encripcion.getInstance();
//            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//            Usuario usuario = state.getUsuario();
//
//            try {
//                JSONObject param = new JSONObject();
//                param.put("cedula", encripcion.encriptar(usuario.cedula));
//                param.put("token", usuario.token);
//                param.put("n_accion", "ADELANTO DE NOMINA ENVIADO PERO SIN RESPUESTA");
//                param.put("n_descr", "El adelanto de nomina se envio, pero no se obtuve respuesta de la base de datos");
//                param.put("f_registro", format.format(new Date()));
//                new EnviarLogAdelantoTask().execute(param);
//            } catch (JSONException e) {}
//
//            AlertDialog.Builder d = new AlertDialog.Builder(context);
//            d.setTitle("Solicitud Enviada");
//            d.setIcon(R.mipmap.icon_presente);
//            d.setMessage("Tu solicitud ha sido enviada con éxito. El valor que te será consignado en tu cuenta de Nómina es de $" + valorsolicitado +
//                    ". Valida la transacción en los movimientos de tu cuenta de Nómina en unos minutos.");
//            d.setCancelable(false);
//            d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    state.setRequisitos(null);
//                    state.setTopes(null);
//                    state.setValorSolicitado(null);
//                    state.getmTabHost().setCurrentTab(1); //Regresar al menu principal
//                }
//            });
//            d.show();
//        }
//    }
//
//
//    //Envia a la tabla LOG todas las peticiones fallidas y exitosas
//    private class EnviarLogAdelantoTask extends AsyncTask<JSONObject, String, String> {
//
//        @Override
//        protected String doInBackground(JSONObject... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                return networkHelper.writeService(params[0], SincroHelper.ENVIAR_LOG_ADELANTO);
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
//            procesarResultLog(result);
//        }
//    }
//
//    public void procesarResultLog(String result){
//        String r = result;
//    }
//
//
//    public static String getIP(){
//        List<InetAddress> addrs;
//        String address = "";
//        try{
//            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
//            for(NetworkInterface intf : interfaces){
//                addrs = Collections.list(intf.getInetAddresses());
//                for(InetAddress addr : addrs){
//                    if(!addr.isLoopbackAddress() && addr instanceof Inet4Address){
//                        address = addr.getHostAddress().toUpperCase(new Locale("es", "CO"));
//                    }
//                }
//            }
//        }catch (Exception e){
//            address = "";
//        }
//        return address;
//    }

}
