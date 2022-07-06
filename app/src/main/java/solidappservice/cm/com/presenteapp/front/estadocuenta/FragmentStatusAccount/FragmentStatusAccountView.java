package solidappservice.cm.com.presenteapp.front.estadocuenta.FragmentStatusAccount;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.estadocuenta.ProductosAdapter;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseConsultaAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestActualizarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestConsultarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.dto.ProductosPorTipoDR;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProductos;
import solidappservice.cm.com.presenteapp.entities.mensajes.request.RequestEnviarMensaje;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 24/11/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public class FragmentStatusAccountView extends Fragment implements FragmentStatusAccountContract.View {

    private FragmentStatusAccountPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private ProgressDialog pd;
    private FirebaseAnalytics firebaseAnalytics;
    private ArrayList<ProductosPorTipoDR> tipoDRs;
    private String valorSolicitudAN;

    @BindView(R.id.list_productos)
    ListView list_productos;

    @BindView(R.id.layout_circular_progress_bar)
    LinearLayout layoutCircularProgressBar;
    @BindView(R.id.circular_progress_bar)
    ProgressBar circularProgressBar;
    @BindView(R.id.text_circular_progress_Bar)
    TextView textCircularProgressBar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de estado de cuenta");
        firebaseAnalytics.logEvent("pantalla_estado_cuenta", params);
        View view = inflater.inflate(R.layout.fragment_statusaccount, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentStatusAccountPresenter(this, new FragmentStatusAccountModel());
        context = (ActivityBase) getActivity();
        state = context.getState();
        pd = new ProgressDialog(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if (state == null || state.getUsuario() == null) {
            context.salir();
        } else {
//            Usuario usuario = state.getUsuario();
//            new MovimientosANTask().execute(usuario.cedula, usuario.token);
            fetchSalaryAdvanceMovements();
        }
    }

    @OnItemClick(R.id.list_productos)
    public void onItemClickListProducts(AdapterView<?> parent, View view, int position, long id) {
        state.setProductosDetalle(tipoDRs.get(position).getProductos());
        state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_6_STATUS_ACCOUNT_PRODUCTS_TAG);
    }

    @Override
    public void fetchSalaryAdvanceMovements() {
        try {
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchSalaryAdvanceMovements(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        } catch (Exception ex) {
            showDataFetchError("");
        }
    }

    @Override
    public void processSalaryAdvancePending(Integer idFlujo, String valorSolicitado) {
        try {
            this.valorSolicitudAN = valorSolicitado;
            Encripcion encripcion = Encripcion.getInstance();
            presenter.processSalaryAdvancePending(new RequestConsultarAdelantoNomina(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    idFlujo
            ));
        } catch (Exception ex) {
            showDataFetchError("");
        }
    }

    @Override
    public void updateSalaryAdvanceStatus(ResponseConsultaAdelantoNomina consulta) {
        try {
            Encripcion encripcion = Encripcion.getInstance();
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
            presenter.updateSalaryAdvanceStatus(new RequestActualizarAdelantoNomina(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    Integer.parseInt(consulta.getV_k_flujo()),
                    "C",
                    consulta.getN_error(),
                    consulta.getK_numdoc(),
                    format2.format(format1.parse(consulta.getF_primera()))
            ));
        } catch (Exception ex) {
            showDataFetchError("");
        }
    }

    @Override
    public void sendSalaryAdvanceNotification() {
        try {
            Encripcion encripcion = Encripcion.getInstance();
            SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            String fechaInicio = formatFecha.format(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, 3);
            String fechaFinal = formatFecha.format(calendar.getTime());
            presenter.sendSalaryAdvanceNotification(new RequestEnviarMensaje(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    fechaInicio,
                    fechaFinal,
                    "Solicitud de adelanto de nómina",
                    "El adelanto de nómina por el valor de $" + valorSolicitudAN + " ha sido exitoso, válida la transacción en los movimientos de tu cuenta de nómina."
            ));
        } catch (Exception ex) {
            showDataFetchError("");
        }
    }

    @Override
    public void fetchAccountStatus() {
        try {
            if (state != null && state.getProductos() != null && state.getProductos().size() > 0) {
                hideCircularProgressBar();
                showAccountStatus(state.getProductos());
            } else {
                Encripcion encripcion = Encripcion.getInstance();
                presenter.fetchAccountStatus(new BaseRequest(
                        encripcion.encriptar(state.getUsuario().getCedula()),
                        state.getUsuario().getToken()
                ));
            }
        } catch (Exception ex) {
            showDataFetchError("");
        }
        /*try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchAccountStatus(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch (Exception ex){
            showDataFetchError("");
        }*/
    }

    @Override
    public void showAccountStatus(List<ResponseProductos> cuentas) {
        try {
            list_productos.setVisibility(View.VISIBLE);
            state.setProductos(cuentas);
            ArrayList<ProductosPorTipoDR> productosPorTipoDRs = new ArrayList<>();

            //CREAMOS UN MAP PARA ORGANIZAR LOS PRODUCTOS POR N_TIPODR
            Map<String, ArrayList<ResponseProductos>> tipoDRs = new HashMap<>();
            for (ResponseProductos p : cuentas) {
                if (!tipoDRs.containsKey(p.getN_tipodr())) {
                    tipoDRs.put(p.getN_tipodr(), new ArrayList<ResponseProductos>());
                }
                p.setExpanded(false);
                tipoDRs.get(p.getN_tipodr()).add(p);
            }

            //RECORREMOS EL MAP, PARA CREAR LOS OBJETOS ProductoPorTipoDR
            final Iterator<String> i = tipoDRs.keySet().iterator();
            while (i.hasNext()) {
                ProductosPorTipoDR p = new ProductosPorTipoDR();
                p.setN_tipodr(i.next());
                p.setProductos(tipoDRs.get(p.getN_tipodr()));
                ;
                productosPorTipoDRs.add(p);
            }
            this.tipoDRs = productosPorTipoDRs;
            ProductosAdapter pa = new ProductosAdapter(context, productosPorTipoDRs);
            list_productos.setAdapter(pa);
        } catch (Exception ex) {
            context.makeErrorDialog("Error cargando los productos");
        }
    }

    @Override
    public void hideAccountStatus() {
        list_productos.setVisibility(View.GONE);
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
    public void showCircularProgressBar(String textProgressBar) {
        layoutCircularProgressBar.setVisibility(View.VISIBLE);
        textCircularProgressBar.setText(textProgressBar);
    }

    @Override
    public void hideCircularProgressBar() {
        layoutCircularProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorTimeOut() {
        String message = "Ha ocurrido un error. Intenta de nuevo y si el error persiste, contacta a PRESENTE.";
        if (state != null && state.getMensajesRespuesta() != null && state.getMensajesRespuesta().size() > 0) {
            for (ResponseMensajesRespuesta rm : state.getMensajesRespuesta()) {
                if (rm.getIdMensaje() == 6) {
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
                context.onBackPressed();
                dialog.dismiss();
            }
        });
        d.show();
    }


    @Override
    public void showDataFetchError(String message) {
        if (TextUtils.isEmpty(message)) {
            message = "Ha ocurrido un error. Intenta de nuevo y si el error persiste, contacta a PRESENTE.";
            if (state != null && state.getMensajesRespuesta() != null && state.getMensajesRespuesta().size() > 0) {
                for (ResponseMensajesRespuesta rm : state.getMensajesRespuesta()) {
                    if (rm.getIdMensaje() == 7) {
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
                dialog.dismiss();
                context.onBackPressed();
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

    //Obtiene los movimientos de adelanto de nomina
//    private class MovimientosANTask extends AsyncTask<String, String, String> {
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
//                return networkHelper.writeService(param, SincroHelper.OBTENER_MOVIMIENTOS_ADELANTO_NOMINA);
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
//            procesarJsonMovimientosAN(result);
//        }
//    }
//
//    private void procesarJsonMovimientosAN(String jsonRespuesta) {
//        try {
//            ArrayList<ResponseMovimientos> listamovimientos = SincroHelper.procesarJsonMovimientosAN(jsonRespuesta);
//
//            if(listamovimientos != null && listamovimientos.size() > 0){
//
//                boolean pendiente = false;
//                String numeroflujo = "";
//
//                for (ResponseMovimientos m : listamovimientos) {
//                    ResponseMovimientos movimientos = new ResponseMovimientos();
//                    if (m.getI_estado().equals("A")) {
//                        pendiente = true;
//                        valorSolicitudAN = m.getV_solicitado().toString();
//                        numeroflujo = m.getK_flujo().toString();
//                    }
//                }
//
//                if(pendiente){
//                    GlobalState state = context.getState();
//                    Usuario usuario = state.getUsuario();
//                    new ConsultaSolicitudANTask().execute(usuario.cedula, usuario.token, numeroflujo);
//                } else {
//                    actualizarEstadoCuenta();
//                }
//
//            } else {
//                actualizarEstadoCuenta();
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
//
//    //Consulta el estado de la solicitud de adelanto nomina si existe alguna en proceso
//    private class ConsultaSolicitudANTask extends AsyncTask<String, String, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                Encripcion encripcion = Encripcion.getInstance();
//                JSONObject param = new JSONObject();
//                param.put("cedula", encripcion.encriptar(params[0]));
//                param.put("token", params[1]);
//                param.put("v_k_flujo", Integer.parseInt(params[2]));
//                return networkHelper.writeService(param, SincroHelper.CONSULTAR_ADELANTAR_NOMINA);
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
//            procesarJsonConsultaSolicitudAN(result);
//        }
//    }
//
//    private void procesarJsonConsultaSolicitudAN(String jsonRespuesta) {
//        try {
//            AdelantoNominaConsulta adelanto = SincroHelper.procesarJsonConsultaSolicitudAN(jsonRespuesta);
//
//            if(adelanto != null){
//                if(!adelanto.n_resultado.equals("ERROR")){
//
//                    GlobalState state = context.getState();
//                    Usuario usuario = state.getUsuario();
//                    Encripcion encripcion = Encripcion.getInstance();
//
//                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
//                    SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
//
//                    JSONObject param = new JSONObject();
//                    param.put("cedula", encripcion.encriptar(usuario.cedula));
//                    param.put("token", usuario.token);
//                    param.put("k_flujo", adelanto.v_k_flujo);
//                    param.put("i_estado", "C");
//                    param.put("n_error", adelanto.n_error);
//                    param.put("k_numdoc", adelanto.k_numdoc);
//                    param.put("f_primcuota", format2.format(format1.parse(adelanto.f_primera)));
//                    new ActualizarAdelantoNominaTask().execute(param);
//                } else {
//                    actualizarEstadoCuenta();
//                }
//            } else {
//                actualizarEstadoCuenta();
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
//
//
//
//
//    //Actualiza la tabla de adelanto de nomina en caso que ya se hubiera realizado la solicitud
//    private class ActualizarAdelantoNominaTask extends AsyncTask<JSONObject, String, String> {
//
//        @Override
//        protected String doInBackground(JSONObject... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                return networkHelper.writeService(params[0], SincroHelper.ACTUALIZAR_ADELANTAR_NOMINA);
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
//            procesarJsonActualizarAdelantoNomina(result);
//        }
//    }
//
//    private void procesarJsonActualizarAdelantoNomina(String jsonRespuesta) {
//        try {
//            String respuesta = SincroHelper.procesarJsonActualizarAN(jsonRespuesta);
//
//            if(respuesta.equals("OK")){
//
//                GlobalState state = context.getState();
//                Usuario usuario = state.getUsuario();
//                Encripcion encripcion = Encripcion.getInstance();
//
//                SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yyyy");
//                Date date = new Date();
//
//                String fechaInicio = formatFecha.format(date);
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(date);
//                calendar.add(Calendar.DAY_OF_YEAR , 3);
//                String fechaFinal = formatFecha.format(calendar.getTime());
//
//                JSONObject paramMensaje = new JSONObject();
//                paramMensaje.put("cedula", encripcion.encriptar(usuario.cedula));
//                paramMensaje.put("token", usuario.token);
//                paramMensaje.put("f_inicio", fechaInicio);
//                paramMensaje.put("f_final", fechaFinal);
//                paramMensaje.put("n_tipo", "Solicitud de adelanto de nómina ");
//                paramMensaje.put("n_mensaj", "El adelanto de nómina por el valor de $"+valorSolicitudAN+" ha sido exitoso, válida la transacción en los movimientos de tu cuenta de nómina.");
//                new EnviarNotificacionANTask().execute(paramMensaje);
//            }else{
//                actualizarEstadoCuenta();
//            }
//
//        } catch (Exception e) {
//            context.makeErrorDialog(e.getMessage());
//        }
//    }
//
//
//    private class EnviarNotificacionANTask extends AsyncTask<JSONObject, String, String> {
//
//        @Override
//        protected String doInBackground(JSONObject... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                return networkHelper.writeService(params[0], SincroHelper.ENVIAR_NOTIFICACION_ADELANTO_NOMINA);
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
//            String respuesta = SincroHelper.procesarJsonNotificacionInbox(jsonRequisitos);
//            actualizarEstadoCuenta();
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
//    //Actualiza el estado de cuenta
//    private void actualizarEstadoCuenta(){
//        GlobalState state = context.getState();
//        Usuario usuario = state.getUsuario();
//        new EstadoCuentaTask().execute(usuario.cedula, usuario.token);
//    }
//
//
//    private void cargarProductos(ArrayList<ResponseProducto> productos) {
//        try {
//            ArrayList<ProductosPorTipoDR> productosPorTipoDRs = new ArrayList<>();
//
//            //CREAMOS UN MAP PARA ORGANIZAR LOS PRODUCTOS POR N_TIPODR
//            Map<String, ArrayList<ResponseProducto>> tipoDRs = new HashMap<>();
//            for (ResponseProducto p : productos) {
//                if (!tipoDRs.containsKey(p.getN_tipodr())) {
//                    tipoDRs.put(p.getN_tipodr(), new ArrayList<ResponseProducto>());
//                }
//                p.setExpanded(false);
//                tipoDRs.get(p.getN_tipodr()).add(p);
//            }
//
//            //RECORREMOS EL MAP, PARA CREAR LOS OBJETOS ProductoPorTipoDR
//            final Iterator<String> i = tipoDRs.keySet().iterator();
//            while (i.hasNext()) {
//                ProductosPorTipoDR p = new ProductosPorTipoDR();
//                p.n_tipodr = i.next();
//                p.productos = tipoDRs.get(p.n_tipodr);
//                productosPorTipoDRs.add(p);
//            }
//            this.tipoDRs = productosPorTipoDRs;
//            ProductosAdapter pa = new ProductosAdapter(context, productosPorTipoDRs);
//            list_productos.setAdapter(pa);
//        } catch (Exception ex) {
//            context.makeErrorDialog("Error cargando los productos");
//        }
//    }
//
//    private class EstadoCuentaTask extends AsyncTask<String, String, String> {
//
//        String cedula = null;
//        String token = null;
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
//                param.put("cedula", cedula = encripcion.encriptar(params[0]));
//                param.put("token", token = params[1]);
////                return networkHelper.writeService(param, SincroHelper.ESTADO_CUENTA);
//                return networkHelper.writeService(param, SincroHelper.CONSULTAR_CUENTAS);
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
//            procesarJsonProductos(result);
//        }
//    }
//
//    private void procesarJsonProductos(String jsonRespuesta) {
//        try {
//            ArrayList<ResponseProducto> productos = SincroHelper.procesarJsonEstadoCuenta(jsonRespuesta);
//            cargarProductos(productos);
//            context.getState().setProductos(productos);
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
}
