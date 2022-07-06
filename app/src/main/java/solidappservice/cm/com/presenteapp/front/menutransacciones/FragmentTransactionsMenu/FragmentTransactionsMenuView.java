package solidappservice.cm.com.presenteapp.front.menutransacciones.FragmentTransactionsMenu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseMovimientos;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;


/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 25/11/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 17/09/2021.
 */
public class FragmentTransactionsMenuView extends Fragment implements FragmentTransactionsMenuContract.View {

    private FragmentTransactionsMenuPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private ProgressDialog pd;
    private FirebaseAnalytics firebaseAnalytics;
    private String dependenciaUsuario;
    private List<String> listDependencias;
    boolean isDendenciaActiva = false;
    private String urlCV;

    @BindView(R.id.layoutSolicitudAhorro)
    LinearLayout btnSolicitudAhorro;
    @BindView(R.id.layoutAbonosCreditos)
    LinearLayout btnAbonosAcreditos;
    @BindView(R.id.layoutSolicitudCv)
    LinearLayout btnSolicitudCv;
    @BindView(R.id.layoutTransferencias)
    LinearLayout btnTransferencias;
    @BindView(R.id.layoutAdelantoNomina)
    LinearLayout btnAdelantoNomina;
    @BindView(R.id.layoutMenuTransacciones)
    LinearLayout layoutMenuTransacciones;

    @BindView(R.id.separator_item1)
    View separator1;
    @BindView(R.id.separator_item2)
    View separator2;
    @BindView(R.id.separator_item3)
    View separator3;
    @BindView(R.id.separator_item4)
    View separator4;
    @BindView(R.id.separator_item5)
    View separator5;

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
        context = (ActivityBase) getActivity();
        pd = new ProgressDialog(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de transacciones");
        firebaseAnalytics.logEvent("pantalla_transacciones", params);
        View view = inflater.inflate(R.layout.fragment_transactionmenu, container,false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentTransactionsMenuPresenter(this, new FragmentTransactionsMenuModel());
        context = (ActivityBase) getActivity();
        state = context.getState();
        pd = new ProgressDialog(context);
        listDependencias = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
//        new obtenerEstadosButtonsTask().execute();
        fetchButtonStateAdvanceSalary();
//        fetchButtonActionAdvanceSalary();
//        fetchButtonStateResorts();
    }

    @OnClick(R.id.layoutSolicitudAhorro)
    public void onClickSolicitudAhorro(View v) {
        btnSolicitudAhorro.setBackgroundColor(Color.parseColor("#F2F2F2"));
        state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_4_SAVINGS_SOLICITY_TAG);
    }

    @OnClick(R.id.layoutAbonosCreditos)
    public void onClickPagoObligaciones(View v) {
        btnAbonosAcreditos.setBackgroundColor(Color.parseColor("#F2F2F2"));
        state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_8_PAYMENTS_CREDITS_TAG);
    }

    @OnClick(R.id.layoutSolicitudCv)
    public void onClickCentrosVacacionales(View v) {
        Encripcion encripcion = new Encripcion();
        String tokenSession = encripcion.encryptAES(state.getUsuario().getCedula()+":"+state.getUsuario().getToken());
        if(urlCV != null && !urlCV.isEmpty()){
            String url = urlCV.replace("${tokenSession}", tokenSession).replace("${origin}", "APPPRESENTE-ANDROID");
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
//        btnSolicitudCv.setBackgroundColor(Color.parseColor("#F2F2F2"));
//        state.getmTabHost().setCurrentTab(11);
    }

    @OnClick(R.id.layoutTransferencias)
    public void onClickTransferencias(View v) {
        btnTransferencias.setBackgroundColor(Color.parseColor("#F2F2F2"));
        state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_18_TRANSFERS_MAKE_TRANSFER_TAG);
    }

    @OnClick(R.id.layoutAdelantoNomina)
    public void onClickAdelantoNomina(View v) {
        validateSalaryAdvanceStatus();
        //validarOnPressedAdelanto();
    }

    @Override
    public void fetchButtonStateAdvanceSalary(){
        try{
            presenter.fetchButtonStateAdvanceSalary();
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void showButtonAdvanceSalary(){
        btnAdelantoNomina.setVisibility(View.VISIBLE);
        separator3.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideButtonAdvanceSalary(){
        btnAdelantoNomina.setVisibility(View.GONE);
        separator3.setVisibility(View.VISIBLE);
    }

    @Override
    public void fetchButtonActionAdvanceSalary(){
        try{
            presenter.fetchButtonActionAdvanceSalary();
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void changeButtonActionAdvanceSalary(List<String> listDependencies){
        this.listDependencias = listDependencies;
    }

    @Override
    public void fetchButtonStateResorts(){
        try{
            presenter.fetchButtonStateResorts();
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void showButtonResorts(String urlLinkResort){
        this.urlCV = urlLinkResort;
        btnSolicitudCv.setVisibility(View.VISIBLE);
        separator5.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideButtonResorts(){
        btnSolicitudCv.setVisibility(View.GONE);
        separator5.setVisibility(View.GONE);
    }


    @Override
    public void fetchButtonStateTransfers() {
        try{
            presenter.fetchButtonStateTransfers();
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void showButtonTransfers() {
        btnTransferencias.setVisibility(View.VISIBLE);
        separator2.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideButtonTransfers() {
        btnTransferencias.setVisibility(View.GONE);
        separator2.setVisibility(View.GONE);
    }

    @Override
    public void fetchButtonStateSavings() {
        try{
            presenter.fetchButtonStateSavings();
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void showButtonSavings() {
        btnSolicitudAhorro.setVisibility(View.VISIBLE);
        separator1.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideButtonSavings() {
        btnSolicitudAhorro.setVisibility(View.GONE);
        separator1.setVisibility(View.GONE);
    }

    @Override
    public void fetchButtonStatePaymentCredits() {
        try{
            presenter.fetchButtonStatePaymentCredits();
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void showButtonPaymentCredits() {
        btnAbonosAcreditos.setVisibility(View.VISIBLE);
        separator4.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideButtonPaymentCredits() {
        btnAbonosAcreditos.setVisibility(View.GONE);
        separator4.setVisibility(View.GONE);
    }


    @Override
    public void fetchAssociatedDependency(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchAssociatedDependency(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void showResultAssociatedDependency(String associatedDependency){
        this.dependenciaUsuario = associatedDependency;
        if (associatedDependency != null){
            isDendenciaActiva = listDependencias.size() == 0 || listDependencias.contains(associatedDependency);
        }
    }

    @Override
    public void validateSalaryAdvanceStatus() {
        if(!isDendenciaActiva || dependenciaUsuario == null || dependenciaUsuario.isEmpty()){
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.popup_adelantonotavailabe);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            final Button buttonentendido = (Button) dialog.findViewById(R.id.btnEntendido);
            buttonentendido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle params = new Bundle();
                    params.putString("Descripción", "Interacción con el botón  entendido del popup 'Adelanto de nómina no disponible'");
                    firebaseAnalytics.logEvent("popup_adelantonomina_nodisponible", params);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }else{
//            GlobalState state = context.getState();
//            Usuario usuario = state.getUsuario();
//            new validarSolicitudesPendientesTask().execute(usuario.cedula, usuario.token);
            fetchPendingSalaryAdvance();
        }
    }

    @Override
    public void fetchPendingSalaryAdvance(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchPendingSalaryAdvance(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void showResultPendingSalaryAdvance(List<ResponseMovimientos> listPendingMovements){
        if(listPendingMovements != null && listPendingMovements.size() > 0){
            AlertDialog.Builder d = new AlertDialog.Builder(context);
            d.setTitle("No es posible solicitar este producto:");
            d.setIcon(R.mipmap.icon_presente);
            d.setMessage("Tienes una solicitud en proceso, válida la transacción en los movimientos de tu cuenta de nómina o inténtalo de nuevo más tarde.");
            d.setCancelable(false);
            d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            d.show();
        } else{
            btnAdelantoNomina.setBackgroundColor(Color.parseColor("#F2F2F2"));
            state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_19_ADVANCE_SALARY_SOLICITY_TAG);
        }
    }

    @Override
    public void showTransactionMenu(){
        layoutMenuTransacciones.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTransactionMenu(){
        layoutMenuTransacciones.setVisibility(View.GONE);
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
                dialog.dismiss();
                context.finish();
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
                dialog.dismiss();
                context.finish();
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





//    public void redirigirAlPortalUnico(){
//        Encripcion encripcion = new Encripcion();
//        String tokenSession = encripcion.encryptAES(state.getUsuario().cedula+":"+state.getUsuario().token);
//        if(urlCV != null && !urlCV.isEmpty()){
//            String url = urlCV.replace("${tokenSession}", tokenSession).replace("${origin}", "APPPRESENTE-ANDROID");
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.setData(Uri.parse(url));
//            startActivity(i);
//        }
//    }
//
//    //Obtiene el estado del button adelanto nomina y las dependencias
//    private class obtenerEstadosButtonsTask extends AsyncTask<String, String, String> {
//
//        String resultEstadoAN;
//        String resultDependenciasActivas;
//        String resultEstadoCV;
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Un momento...");
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                resultEstadoAN = networkHelper.readService(SincroHelper.OBTENER_ESTADO_ADELANTO_NOMINA);
//                resultDependenciasActivas = networkHelper.readService(SincroHelper.OBTENER_DEPENDENCIAS_ADELANTO_NOMINA);
//                resultEstadoCV = networkHelper.readService(SincroHelper.OBTENER_PARAMETROS_APP+12);
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
//            procesarResultEstadosAdelantoN(resultEstadoAN, resultDependenciasActivas, resultEstadoCV);
//        }
//    }
//
//    private void procesarResultEstadosAdelantoN(String resultEstadoAN, String resultDependenciasActivas, String resultEstadoCV) {
//        try {
//            listDependencias = new ArrayList<>();
//            String estado = SincroHelper.procesarJsonEstadoAdelantoN(resultEstadoAN);
//            listDependencias = SincroHelper.procesarJsonDependenciasActivasAdelantoN(resultDependenciasActivas);
//            ResponseParametrosAPP estadoButtonCV = SincroHelper.procesarJsonParametrosGenerales(resultEstadoCV);
//
//            if (estado.equals("Y")) {
//                ObtenerDependenciaAsociado();
//                btnAdelantoNomina.setVisibility(View.VISIBLE);
//            } else {
//                pd.dismiss();
//                btnAdelantoNomina.setVisibility(View.GONE);
//            }
//
//            if(estadoButtonCV != null){
//                if(estadoButtonCV.getEstado() != null && estadoButtonCV.getEstado().equals("Y")){
//                    btnSolicitudCv.setVisibility(View.VISIBLE);
//                }else{
//                    btnSolicitudCv.setVisibility(View.GONE);
//                }
//                urlCV = estadoButtonCV.getValue1();
//            }
//
//        } catch (ErrorTokenException e) {
//            pd.dismiss();
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
//            pd.dismiss();
//            context.makeErrorDialog(ex.getMessage());
//        }
//    }
//
//    private void ObtenerDependenciaAsociado() {
//        GlobalState state = context.getState();
//        Usuario usuario = state.getUsuario();
//        new ObtenerDependenciaAsociadoTask().execute(usuario.cedula, usuario.token);
//    }
//
//    private class ObtenerDependenciaAsociadoTask extends AsyncTask<String, String, String> {
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
//            dependenciaUsuario = SincroHelper.procesarJsonObtenerDependenciaAsociado(jsonRequisitos);
//            if (dependenciaUsuario != null){
//                isDendenciaActiva = listDependencias.size() == 0 || listDependencias.contains(dependenciaUsuario);
//            }
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
//
//    //Valida si el boton esta disponbile o no
//    private void validarOnPressedAdelanto() {
//
//        if(! isDendenciaActiva || dependenciaUsuario == null || dependenciaUsuario.isEmpty()){
//            final Dialog dialog = new Dialog(getContext());
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.setContentView(R.layout.popup_adelantonotavailabe);
//            dialog.setCancelable(false);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            final Button buttonentendido = (Button) dialog.findViewById(R.id.btnEntendido);
//            buttonentendido.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Bundle params = new Bundle();
//                    params.putString("Descripción", "Interacción con el botón  entendido del popup 'Adelanto de nómina no disponible'");
//                    firebaseAnalytics.logEvent("popup_adelantonomina_nodisponible", params);
//                    dialog.dismiss();
//                }
//            });
//            dialog.show();
//        }else{
//            GlobalState state = context.getState();
//            Usuario usuario = state.getUsuario();
//            new validarSolicitudesPendientesTask().execute(usuario.cedula, usuario.token);
//        }
//    }
//
//    private class validarSolicitudesPendientesTask extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Un momento...");
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
//            procesarSolicitudesPendientes(result);
//            pd.dismiss();
//        }
//    }
//
//    private void procesarSolicitudesPendientes(String jsonRespuesta) {
//        try {
//            ArrayList<ResponseMovimientos> listamovimientos = SincroHelper.procesarJsonMovimientosAN(jsonRespuesta);
//            ArrayList<ResponseMovimientos> listaMovimientosEnCurso = new ArrayList<>();
//
//            if(listamovimientos!= null & listamovimientos.size()>0){
//
//                for (ResponseMovimientos m : listamovimientos) {
//
//                    SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
//                    String fecha = dt1.format(new Date());
//                    Date date = dt1.parse(fecha);
//
//                    if ((m.getI_estado().equals("E") || m.getI_estado().equals("A")) && (m.getF_solictud().after(date) || m.getF_solictud().equals(date) )) {
//                        listaMovimientosEnCurso.add(m);
//                    }
//                }
//
//                if(listaMovimientosEnCurso.size() > 0){
//                    AlertDialog.Builder d = new AlertDialog.Builder(context);
//                    d.setTitle("No es posible solicitar este producto:");
//                    d.setIcon(R.mipmap.icon_presente);
//                    d.setMessage("Tienes una solicitud en proceso, válida la transacción en los movimientos de tu cuenta de nómina o inténtalo de nuevo más tarde.");
//                    d.setCancelable(false);
//                    d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    d.show();
//                } else{
//                    btnAdelantoNomina.setBackgroundColor(Color.parseColor("#F2F2F2"));
//                    state.getmTabHost().setCurrentTab(22);
//                }
//
//            }
//            else{
//                btnAdelantoNomina.setBackgroundColor(Color.parseColor("#F2F2F2"));
//                state.getmTabHost().setCurrentTab(22);
//            }
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
//            context.makeErrorDialog(ex.getMessage());
//        }
//    }


}