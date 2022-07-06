package solidappservice.cm.com.presenteapp.front.adelantonomina.FragmentSolicity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.adelantonomina.PagerAdapterAdelantoNomina;
import solidappservice.cm.com.presenteapp.adapters.adelantonomina.MovimientosAdelantoNominaAdapter;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestLogs;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestNoCumple;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseMovimientos;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseTips;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseTopes;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseValidarRequisitos;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.tools.CirclePageIndicator;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.helpers.NumberTextWatcher;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 19/10/2020.
 */
public class FragmentSolicityView extends Fragment implements FragmentSolicityContract.View{

    private FragmentSolicityPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private ProgressDialog pd;
    private FirebaseAnalytics firebaseAnalytics;

    float mStartDragX;

    @BindView(R.id.lblMonto_maximo)
    TextView lblMontoMaximo;
    @BindView(R.id.lblvalores_minmax)
    TextView lblValoresMinMax;
    @BindView(R.id.lblTerminos)
    TextView lblTerminos;
    @BindView(R.id.txtMonto_Solicitar)
    EditText txtMontoASolicitar;
    @BindView(R.id.btnSolicitar)
    Button btnSolicitar;
    @BindView(R.id.lblMovimientos_anteriores)
    LinearLayout buttonMovimientosAnteriores;
    @BindView(R.id.list_movimientosan)
    ListView listMovimientos;
    @BindView(R.id.scroll_adelanto)
    ScrollView scrollMovimientos;

    private TextView lblValor_Solicitado;
    private ViewPager viewPagerTips;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de adelanto nomina");
        firebaseAnalytics.logEvent("pantalla_adelanto_nomina", params);
        View view = inflater.inflate(R.layout.fragment_salaryadvance_solicity, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentSolicityPresenter(this, new FragmentSolicityModel());
        context = (ActivityBase) getActivity();
        state = context.getState();
        pd = new ProgressDialog(context);
        lblTerminos.setMovementMethod(LinkMovementMethod.getInstance());
        txtMontoASolicitar.addTextChangedListener(new NumberTextWatcher(txtMontoASolicitar));
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null || state.getUsuario() == null){
            context.salir();
        } else {
            txtMontoASolicitar.setText("");
            validateRequirements();
//            ValidarRequisitos();
        }
    }

    @OnClick(R.id.lblMovimientos_anteriores)
    public void onClickMovimientosAnteriores(){
        fetchMoves();
    }

    @OnClick(R.id.btnSolicitar)
    public void onClickSolicitar(){
        boolean requestAmountIsValid = validateRequestedAmount();
        if(requestAmountIsValid){
            state.setValorSolicitado(txtMontoASolicitar.getText().toString().replaceAll("[$,.]", ""));
            presenter.fetchTips();
//            new ObtenerTipsTask().execute();
            state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_20_ADVANCE_SALARY_DETAIL_TAG);
        }
    }

    @Override
    public void fetchMoves(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchMoves(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch(Exception ex){
            showDataFetchError("PRESENTE","");
        }
    }

    @Override
    public void showMoves(List<ResponseMovimientos> movimientos){
        if(movimientos != null && movimientos.size() > 0){
            MovimientosAdelantoNominaAdapter adapter = new MovimientosAdelantoNominaAdapter(getContext(), movimientos);
            listMovimientos.setAdapter(adapter);
            scrollMovimientos.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    listMovimientos.getParent()
                            .requestDisallowInterceptTouchEvent(false);
                    return false;
                }
            });
            listMovimientos.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
        }
    }

    @Override
    public boolean validateRequestedAmount(){
        String valorsolicitado = txtMontoASolicitar.getText().toString().replaceAll("[$,.]", "");
        String valorMaximo = lblMontoMaximo.getText().toString();
        ResponseTopes topes = state.getTopes();
        DecimalFormat formato = new DecimalFormat("#,###");
        int valorCupo = topes.getV_cupo();
        int valorMax = topes.getV_maximo();
        int valorMin = topes.getV_minimo();
        return presenter.validateRequestedAmount(valorsolicitado != null && !valorsolicitado.isEmpty() ? Integer.parseInt(valorsolicitado) : 0
                , valorMaximo, valorCupo, valorMax, valorMin);
    }

    @Override
    public void showErrorRequestedAmount(String messageError){
        txtMontoASolicitar.setError(messageError);
    }

    @Override
    public void showDialogTips(List<ResponseTips> tips){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_tips);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final List<ResponseTips> finalTips = tips;
        if(finalTips != null && finalTips.size() != 0){
            PagerAdapterAdelantoNomina adapter = new PagerAdapterAdelantoNomina(getContext(), finalTips);
            viewPagerTips = (ViewPager) dialog.findViewById(R.id.pager);
            ImageView buttonleft = (ImageView) dialog.findViewById(R.id.img_arrow_left);
            ImageView buttonright = (ImageView) dialog.findViewById(R.id.img_arrow_right);
            final Button buttonentendido = (Button) dialog.findViewById(R.id.btnEntendido);
            final CirclePageIndicator pageIndicator = (CirclePageIndicator)dialog.findViewById(R.id.pager_indicator);
            viewPagerTips.setAdapter(adapter);
            pageIndicator.setViewPager(viewPagerTips);
            pageIndicator.setCurrentItem(0);

            buttonleft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewPagerTips.getCurrentItem() != 0) {
                        buttonentendido.setVisibility(View.GONE);
                        viewPagerTips.setCurrentItem(viewPagerTips.getCurrentItem() - 1);
                    }
                }
            });

            buttonright.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewPagerTips.getCurrentItem() == (finalTips.size() - 1)) {
                        viewPagerTips.setCurrentItem(0);
                        buttonentendido.setVisibility(View.GONE);
                    } else {
                        buttonentendido.setVisibility(View.GONE);
                        viewPagerTips.setCurrentItem(viewPagerTips.getCurrentItem() + 1);
                        if(viewPagerTips.getCurrentItem() == (finalTips.size() - 1)){
                            pageIndicator.setPadding(0,0,0,90);
                            buttonentendido.setVisibility(View.VISIBLE);
                        } else {
                            buttonentendido.setVisibility(View.GONE);
                        }
                    }
                }
            });

            viewPagerTips.setOnTouchListener(new android.view.View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent ev) {
                    float x = ev.getX();
                    switch (ev.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mStartDragX = x;
                            break;
                        case MotionEvent.ACTION_MOVE:


                            //Left -> Desliza a la derecha

                            if (mStartDragX < x && viewPagerTips.getCurrentItem() != 0) {
                                buttonentendido.setVisibility(View.GONE);
                            }

                            if (mStartDragX < x && viewPagerTips.getCurrentItem() == (viewPagerTips.getAdapter().getCount()-1)) {
                                buttonentendido.setVisibility(View.GONE);
                            }

                            //Right -> Desliza a la izquierda
                            if(mStartDragX > x && viewPagerTips.getCurrentItem() == (viewPagerTips.getAdapter().getCount()-1)) {
                                viewPagerTips.setCurrentItem(0);
                                buttonentendido.setVisibility(View.GONE);
                            }

                            if (mStartDragX > x && viewPagerTips.getCurrentItem() != (viewPagerTips.getAdapter().getCount()-1)) {

                                if(viewPagerTips.getAdapter().getCount()==1){
                                    pageIndicator.setPadding(0,0,0,90);
                                    buttonentendido.setVisibility(View.VISIBLE);
                                }
                                if(viewPagerTips.getAdapter().getCount()>1){

                                    if(viewPagerTips.getCurrentItem() == viewPagerTips.getAdapter().getCount()-2){
                                        pageIndicator.setPadding(0,0,0,90);
                                        buttonentendido.setVisibility(View.VISIBLE);
                                    }
                                    else{
                                        buttonentendido.setVisibility(View.GONE);
                                    }
                                };
                            }

                            break;
                    }
                    return false;
                }
            });


            buttonentendido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }

    @Override
    public void validateRequirements(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.validateRequirements(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch(Exception ex){
            showDataFetchError("PRESENTE","");
        }
    }

    @Override
    public void fetchDebtCapacity(ResponseValidarRequisitos requisitos){
        try{
            state.setRequisitos(requisitos);
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchDebtCapacity(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch (Exception ex){
            showDataFetchError("PRESENTE","");
        }
    }

    @Override
    public void fetchReasonsNotMeetsRequirements(ResponseValidarRequisitos requisitos){
        try{
            state.setRequisitos(requisitos);
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchReasonsNotMeetsRequirements(new RequestNoCumple(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    requisitos.getK_identificador(),
                    requisitos.getCumple()
            ));
        }catch (Exception ex){
            showDataFetchError("PRESENTE","");
        }
    }

    @Override
    public void enterLogs(String accion, String descripcion){
        try {
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
    public void showDebtCapacity(ResponseTopes topes){
        state.setTopes(topes);
        DecimalFormat formato = new DecimalFormat("#,###");
        String valorcupo = formato.format(topes.getV_cupo());
        String valormax = formato.format(topes.getV_maximo());
        String valormin = formato.format(topes.getV_minimo());
        lblMontoMaximo.setText(String.format("$%s", valorcupo));
        lblValoresMinMax.setText(String.format("Recuerda que el monto mínimo a solicitar es $%s y máximo $%s por transacción.", valormin, valormax));
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

//    @Override
//    public void onClick(View v) {
//
//        int id = v.getId();

//        switch (id) {
//            case R.id.lblMovimientos_anteriores:
//                verMovimientos();
//                break;
//            case R.id.btnSolicitar:
//                String valorsolicitado = txtMonto_Solicitar.getText().toString().replaceAll("[$,.]", "");
//                Topes topes = state.getTopes();
//                DecimalFormat formato = new DecimalFormat("#,###");
//                String valormax = formato.format(topes.v_maximo);
//                String valormin = formato.format(topes.v_minimo);
//
//
//                if(topes.v_cupo <= 0 || Integer.parseInt(valorsolicitado) > topes.v_cupo){
//                    txtMonto_Solicitar.setError("No tienes cupo para realizar esta solicitud");
//                    break;
//                }
//
//                if(TextUtils.isEmpty(lblMonto_maximo.getText().toString())){
//                    txtMonto_Solicitar.setError("Tu cupo no se ha cargado correctamente");
//                    break;
//                }
//
//                if(!TextUtils.isEmpty(valorsolicitado) && (Integer.parseInt(valorsolicitado) > topes.v_maximo ||
//                        Integer.parseInt(valorsolicitado) < topes.v_minimo)){
//                    txtMonto_Solicitar.setError("Debes ingresar un monto entre $"+valormin+" y $"+valormax);
//                    break;
//                }
//
//                if(TextUtils.isEmpty(valorsolicitado)) {
//                    txtMonto_Solicitar.setError("Debes ingresar un monto");
//                    break;
//                }
//
//                if(topes.v_cupo > 0 && !TextUtils.isEmpty(valorsolicitado) && Integer.parseInt(valorsolicitado) <= topes.v_maximo &&
//                        Integer.parseInt(valorsolicitado) >= topes.v_minimo &&  Integer.parseInt(valorsolicitado) <= topes.v_cupo) {
//                    state.setValorSolicitado(valorsolicitado);
//                    new ObtenerTipsTask().execute();
//                    state.getmTabHost().setCurrentTab(23);
//                    break;
//                }
//        }
//    }


//    //Valida si cumple los requisitos
//    private void ValidarRequisitos() {
//        GlobalState state = context.getState();
//        Usuario usuario = state.getUsuario();
//        new ValidarRequisitosTask().execute(usuario.cedula, usuario.token);
//    }
//
//    private class ValidarRequisitosTask extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Validando requisitos...");
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
//                return networkHelper.writeService(param, SincroHelper.VALIDAR_REQUISITOS);
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
//            procesadorJSONAN(result, "Requisitos");
//        }
//    }
//
//
//
//    //Obtiene los topes
//    private void obtenerTopes() {
//        GlobalState state = context.getState();
//        Usuario usuario = state.getUsuario();
//        new ObtenerTopesTask().execute(usuario.cedula, usuario.token);
//    }
//
//    private class ObtenerTopesTask extends AsyncTask<String, String, String> {
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
//                return networkHelper.writeService(param, SincroHelper.OBTENER_TOPES);
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
//            procesadorJSONAN(result, "Topes");
//            pd.dismiss();
//        }
//    }
//
//
//    //Obtiene la razón por la que no cumple
//    private class ValidarNoCumpleTask extends AsyncTask<String, String, String> {
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
//                param.put("k_identificador", params[2]);
//                param.put("n_requisito", params[3]);
//
//                return networkHelper.writeService(param, SincroHelper.OBTENER_NO_CUMPLE_REQUISITOS);
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
//            procesadorJSONAN(result,"ValidarNoCumple");
//            pd.dismiss();
//        }
//    }
//
//
//
//    //Realiza el cuadro de Tips
//    private class ObtenerTipsTask extends AsyncTask<String, String, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                return networkHelper.readService(SincroHelper.OBTENER_TIPS);
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
//            procesadorJSONAN(result, "Tips");
//        }
//    }
//
//    private void mostrarDialogoTips(ArrayList<TipsAN> listatips) {
//        final Dialog dialog = new Dialog(getContext());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.pager_layout);
//        dialog.setCancelable(false);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        final List<TipsAN> tipsANArray = listatips;
//        if(tipsANArray != null && tipsANArray.size() != 0){
//            PagerAdapterAdelantoNomina adapter = new PagerAdapterAdelantoNomina(getContext(), tipsANArray);
//            ViewPagerTips = (ViewPager) dialog.findViewById(R.id.pager);
//            ImageView buttonleft = (ImageView) dialog.findViewById(R.id.img_arrow_left);
//            ImageView buttonright = (ImageView) dialog.findViewById(R.id.img_arrow_right);
//            final Button buttonentendido = (Button) dialog.findViewById(R.id.btnEntendido);
//            final CirclePageIndicator pageIndicator = (CirclePageIndicator)dialog.findViewById(R.id.pager_indicator);
//            ViewPagerTips.setAdapter(adapter);
//            pageIndicator.setViewPager(ViewPagerTips);
//            pageIndicator.setCurrentItem(0);
//
//            buttonleft.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (ViewPagerTips.getCurrentItem() != 0) {
//                        buttonentendido.setVisibility(View.GONE);
//                        ViewPagerTips.setCurrentItem(ViewPagerTips.getCurrentItem() - 1);
//                    }
//                }
//            });
//
//            buttonright.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (ViewPagerTips.getCurrentItem() == (tipsANArray.size() - 1)) {
//                        ViewPagerTips.setCurrentItem(0);
//                        buttonentendido.setVisibility(View.GONE);
//                    } else {
//                        buttonentendido.setVisibility(View.GONE);
//                        ViewPagerTips.setCurrentItem(ViewPagerTips.getCurrentItem() + 1);
//                        if(ViewPagerTips.getCurrentItem() == (tipsANArray.size() - 1)){
//                            pageIndicator.setPadding(0,0,0,90);
//                            buttonentendido.setVisibility(View.VISIBLE);
//                        } else {
//                            buttonentendido.setVisibility(View.GONE);
//                        }
//                    }
//                }
//            });
//
//            ViewPagerTips.setOnTouchListener(new android.view.View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent ev) {
//                    float x = ev.getX();
//                    switch (ev.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//                            mStartDragX = x;
//                            break;
//                        case MotionEvent.ACTION_MOVE:
//
//
//                            //Left -> Desliza a la derecha
//
//                            if (mStartDragX < x && ViewPagerTips.getCurrentItem() != 0) {
//                                buttonentendido.setVisibility(View.GONE);
//                            }
//
//                            if (mStartDragX < x && ViewPagerTips.getCurrentItem() == (ViewPagerTips.getAdapter().getCount()-1)) {
//                                buttonentendido.setVisibility(View.GONE);
//                            }
//
//                            //Right -> Desliza a la izquierda
//                            if(mStartDragX > x && ViewPagerTips.getCurrentItem() == (ViewPagerTips.getAdapter().getCount()-1)) {
//                                ViewPagerTips.setCurrentItem(0);
//                                buttonentendido.setVisibility(View.GONE);
//                            }
//
//                            if (mStartDragX > x && ViewPagerTips.getCurrentItem() != (ViewPagerTips.getAdapter().getCount()-1)) {
//
//                                if(ViewPagerTips.getAdapter().getCount()==1){
//                                    pageIndicator.setPadding(0,0,0,90);
//                                    buttonentendido.setVisibility(View.VISIBLE);
//                                }
//                                if(ViewPagerTips.getAdapter().getCount()>1){
//
//                                    if(ViewPagerTips.getCurrentItem() == ViewPagerTips.getAdapter().getCount()-2){
//                                        pageIndicator.setPadding(0,0,0,90);
//                                        buttonentendido.setVisibility(View.VISIBLE);
//                                    }
//                                    else{
//                                        buttonentendido.setVisibility(View.GONE);
//                                    }
//                                };
//                            }
//
//                            break;
//                    }
//                    return false;
//                }
//            });
//
//
//            buttonentendido.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog.dismiss();
//                }
//            });
//
//            dialog.show();
//        }
//    }
//
//
//    //Envia a la tabla LOG todas las peticiones que no cumplen requisitos
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
//            procesadorJSONAN(result, "LogAdelanto");
//            pd.dismiss();
//        }
//    }
//
//    //Obtiene los movimientos realizados
//    private void verMovimientos() {
//        GlobalState state = context.getState();
//        Usuario usuario = state.getUsuario();
//        new MovimientosANTask().execute(usuario.cedula, usuario.token);
//    }
//
//    private class MovimientosANTask extends AsyncTask<String, String, String> {
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
//            procesadorJSONAN(result, "Movimientos");
//            pd.dismiss();
//        }
//    }
//
//    private void procesadorJSONAN(String jsonRespuesta, String task) {
//        try {
//
//            switch(task){
//
//                case "Requisitos":
//
//                    RequisitosAN requisitos = SincroHelper.procesarJsonValidarRequisitosAN(jsonRespuesta);
//
//                    if (requisitos != null){
//                        state.setRequisitos(requisitos);
//
//                        if(requisitos.cumple.equals("CUMPLE")){
//                            obtenerTopes();
//                        } else {
//                            GlobalState state = context.getState();
//                            Usuario usuario = state.getUsuario();
//                            new ValidarNoCumpleTask().execute(usuario.cedula, usuario.token, requisitos.k_identificador, requisitos.cumple);
//                        }
//                    }else{
//                        Encripcion encripcion = Encripcion.getInstance();
//                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//                        JSONObject param = new JSONObject();
//                        Usuario usuario = state.getUsuario();
//                        param.put("cedula", encripcion.encriptar(usuario.cedula));
//                        param.put("token", usuario.token);
//                        param.put("n_accion", "ASOCIADO NO CUMPLE REQUISITOS");
//                        param.put("n_descr", "Error obteniendo los requisitos");
//                        param.put("f_registro", format.format(new Date()));
//                        new EnviarLogAdelantoTask().execute(param);
//
//                        AlertDialog.Builder d = new AlertDialog.Builder(context);
//                        d.setTitle("No es posible solicitar este producto:");
//                        d.setIcon(R.mipmap.icon_presente);
//                        d.setMessage("Ha ocurrido un error cargando la información inténtalo de nuevo en unos minutos");
//                        d.setCancelable(false);
//                        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                state.setRequisitos(null);
//                                state.setTopes(null);
//                                state.setValorSolicitado(null);
//                                state.getmTabHost().setCurrentTab(1);
//                                dialog.dismiss();
//                            }
//                        });
//                        d.show();
//                    }
//
//                    break;
//
//                case "Topes":
//
//                    Topes topes = SincroHelper.procesarJsonObtenerTopessAN(jsonRespuesta);
//
//                    if (topes != null){
//                        state.setTopes(topes);
//                        DecimalFormat formato = new DecimalFormat("#,###");
//                        String valorcupo = formato.format(topes.getValorCupo());
//                        String valormax = formato.format(topes.getValorMaximo());
//                        String valormin = formato.format(topes.getValorMinimo());
//
//                        if(topes.getValorCupo() <= 0){
//                            AlertDialog.Builder d = new AlertDialog.Builder(context);
//                            d.setTitle("No es posible solicitar este producto:");
//                            d.setIcon(R.mipmap.icon_presente);
//                            d.setMessage("No tienes cupo para solicitar este producto.");
//                            d.setCancelable(false);
//                            d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    state.setRequisitos(null);
//                                    state.setTopes(null);
//                                    state.setValorSolicitado(null);
//                                    state.getmTabHost().setCurrentTab(1);
//                                    dialog.dismiss();
//                                }
//                            });
//                            d.show();
//                        }else{
//                            lblMonto_maximo.setText("$"+valorcupo);
//                            lblvalores_minmax.setText("Recuerda que el monto mínimo a solicitar es $"+valormin+" y máximo $"+valormax+ " por transacción.");
//                        }
//                    } else {
//                        Encripcion encripcion = Encripcion.getInstance();
//                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//                        JSONObject param = new JSONObject();
//                        Usuario usuario = state.getUsuario();
//                        param.put("cedula", encripcion.encriptar(usuario.cedula));
//                        param.put("token", usuario.token);
//                        param.put("n_accion", "ASOCIADO NO CUMPLE REQUISITOS");
//                        param.put("n_descr", "Error obteniendo los topes");
//                        param.put("f_registro", format.format(new Date()));
//                        new EnviarLogAdelantoTask().execute(param);
//
//                        AlertDialog.Builder d = new AlertDialog.Builder(context);
//                        d.setTitle("No es posible solicitar este producto:");
//                        d.setIcon(R.mipmap.icon_presente);
//                        d.setMessage("Ha ocurrido un error cargando la información inténtalo de nuevo en unos minutos");
//                        d.setCancelable(false);
//                        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                state.setRequisitos(null);
//                                state.setTopes(null);
//                                state.setValorSolicitado(null);
//                                state.getmTabHost().setCurrentTab(1);
//                                dialog.dismiss();
//                            }
//                        });
//                        d.show();
//                    }
//                    break;
//
//                case "Movimientos":
//
//                    ArrayList<MovimientosAN> listamovimientos = SincroHelper.procesarJsonMovimientosAN(jsonRespuesta);
//                    ArrayList<MovimientosAN> listaMovimientosContabilizados = new ArrayList<>();
//
//                    if(listamovimientos!= null & listamovimientos.size()>0){
//
//                        for (MovimientosAN m : listamovimientos) {
//                            if (m.i_estado.equals("C")) {
//                                listaMovimientosContabilizados.add(m);
//                            }
//                        }
//
//                        MovimientosANAdapter adapter = new MovimientosANAdapter(getContext(), listaMovimientosContabilizados);
//                        list_movimientosan.setAdapter(adapter);
//
//                        scroll_adelanto.setOnTouchListener(new View.OnTouchListener() {
//
//                            public boolean onTouch(View v, MotionEvent event) {
//                                list_movimientosan.getParent()
//                                        .requestDisallowInterceptTouchEvent(false);
//                                return false;
//                            }
//                        });
//
//                        list_movimientosan.setOnTouchListener(new View.OnTouchListener() {
//
//                            public boolean onTouch(View v, MotionEvent event) {
//                                v.getParent().requestDisallowInterceptTouchEvent(true);
//                                return false;
//                            }
//                        });
//
//
//                    }
//                    break;
//
//                case "Tips":
//
//                    ArrayList<TipsAN> tipsANArray = SincroHelper.procesarJsonObtenerTipsAN(jsonRespuesta);
//
//                    if(tipsANArray!=null){
//                        mostrarDialogoTips(tipsANArray);
//                    }
//
//                    break;
//
//                case "ValidarNoCumple":
//
//                    ArrayList<RequisitosNoCumpleAN> nocumple = SincroHelper.procesarJsonNoCumpleRequisitosAN(jsonRespuesta);
//
//                    if(nocumple != null && nocumple.size() > 0){
//
//                        String nc = "";
//                        for (RequisitosNoCumpleAN item: nocumple) {
//                            nc += "• "+item.n_observacion+"\n";
//                        }
//
//                        nc += "\n Si tienes alguna duda comunicate con tu gestor";
//
//                        Encripcion encripcion = Encripcion.getInstance();
//                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//                        JSONObject param = new JSONObject();
//                        Usuario usuario = state.getUsuario();
//                        param.put("cedula", encripcion.encriptar(usuario.cedula));
//                        param.put("token", usuario.token);
//                        param.put("n_accion", "ASOCIADO NO CUMPLE REQUISITOS");
//                        param.put("n_descr", nc);
//                        param.put("f_registro", format.format(new Date()));
//                        new EnviarLogAdelantoTask().execute(param);
//
//                        AlertDialog.Builder d = new AlertDialog.Builder(context);
//                        d.setTitle("No es posible solicitar este producto:");
//                        d.setIcon(R.mipmap.icon_presente);
//                        d.setMessage(nc);
//                        d.setCancelable(false);
//                        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                state.setRequisitos(null);
//                                state.setTopes(null);
//                                state.setValorSolicitado(null);
//                                state.getmTabHost().setCurrentTab(1);
//                                dialog.dismiss();
//                            }
//                        });
//                        d.show();
//                    }else{
//                        Encripcion encripcion = Encripcion.getInstance();
//                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//                        JSONObject param = new JSONObject();
//                        Usuario usuario = state.getUsuario();
//                        param.put("cedula", encripcion.encriptar(usuario.cedula));
//                        param.put("token", usuario.token);
//                        param.put("n_accion", "ASOCIADO NO CUMPLE REQUISITOS");
//                        param.put("n_descr", "No se obtuvieron los motivos por las que el usuario no cumple los requisitos.");
//                        param.put("f_registro", format.format(new Date()));
//                        new EnviarLogAdelantoTask().execute(param);
//
//                        AlertDialog.Builder d = new AlertDialog.Builder(context);
//                        d.setTitle("No es posible solicitar este producto:");
//                        d.setIcon(R.mipmap.icon_presente);
//                        d.setMessage("Upps, en este momento no es posible solicitar este producto, por favor inténtalo más tarde. ");
//                        d.setCancelable(false);
//                        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                state.setRequisitos(null);
//                                state.setTopes(null);
//                                state.setValorSolicitado(null);
//                                state.getmTabHost().setCurrentTab(1);
//                                dialog.dismiss();
//                            }
//                        });
//                        d.show();
//                    }
//                    break;
//
//                case "LogAdelanto":
//
//                    String result = jsonRespuesta;
//
//                    break;
//
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
//        } catch (Exception e) {
//            state.getmTabHost().setCurrentTab(1);
//            state.setRequisitos(null);
//            state.setTopes(null);
//            state.setValorSolicitado(null);
//            context.makeErrorDialog(e.getMessage());
//        }
//    }


}
