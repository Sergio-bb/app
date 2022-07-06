package solidappservice.cm.com.presenteapp.front.adelantonomina.FragmentSolicity;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
    private ActivityTabsView baseView;
    private GlobalState state;
    private FirebaseAnalytics firebaseAnalytics;

    float mStartDragX;

    @BindView(R.id.circular_progress_bar_topes)
    ProgressBar circularProgressBarTopes;
    @BindView(R.id.lblMonto_maximo)
    TextView lblMontoMaximo;
    @BindView(R.id.tvValoresTopes)
    TextView tvValoresTopes;
    @BindView(R.id.lblTerminos)
    TextView lblTerminos;
    @BindView(R.id.etMontoASolicitar)
    EditText etMontoASolicitar;
    @BindView(R.id.btnSolicitar)
    Button btnSolicitar;
    @BindView(R.id.llMovimientosAnteriores)
    LinearLayout buttonMovimientosAnteriores;
    @BindView(R.id.list_movimientosan)
    ListView listMovimientos;
    @BindView(R.id.contentCircularProgressBarMovements)
    LinearLayout layoutCircularProgressBarMovements;
    @BindView(R.id.circular_progress_bar_movements)
    ProgressBar circularProgressBarMovements;
    @BindView(R.id.text_circular_progress_Bar_movements)
    TextView textCircularProgressBarMovements;
    @BindView(R.id.imageReferesh_movements)
    ImageView buttonRefereshMovements;
    @BindView(R.id.pullToRefreshMovements)
    SwipeRefreshLayout pullToRefreshMovements;


    @BindView(R.id.contentAdelantoNomina)
    ScrollView contentAdelantoNomina;

    @BindView(R.id.contentCircularProgressBar)
    LinearLayout contentCircularProgressBar;
    @BindView(R.id.circularProgressBar)
    ProgressBar circularProgressBar;
    @BindView(R.id.tvCircularProgressBar)
    TextView tvCircularProgressBar;
    @BindView(R.id.imageReferesh)
    ImageView buttonReferesh;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        baseView = (ActivityTabsView) getActivity();
        state = context.getState();
        lblTerminos.setMovementMethod(LinkMovementMethod.getInstance());
        etMontoASolicitar.addTextChangedListener(new NumberTextWatcher(etMontoASolicitar));
        pullToRefreshMovements.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchMoves();
                pullToRefreshMovements.setRefreshing(false);
            }
        });
        fetchPendingSalaryAdvance();
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null || state.getUsuario() == null){
            context.salir();
        }
    }

    @OnClick(R.id.llMovimientosAnteriores)
    public void onClickMovimientosAnteriores(){
        fetchMoves();
    }

    @OnClick(R.id.btnSolicitar)
    public void onClickSolicitar(){
        boolean requestAmountIsValid = validateRequestedAmount();
        if(requestAmountIsValid){
            state.setValorSolicitado(etMontoASolicitar.getText().toString().replaceAll("[$,.]", ""));
            presenter.fetchTips();
            state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_20_ADVANCE_SALARY_DETAIL_TAG);
        }
    }

    @OnClick(R.id.imageReferesh)
    public void onClickRefresh(){
        validateRequirements();
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
            showDataFetchError("Lo sentimos", "Se ha producido un error, inténtalo nuevamente en unos minutos.");
            showErrorWithRefresh();
        }
    }

    @Override
    public void showResultPendingSalaryAdvance(List<ResponseMovimientos> listPendingMovements){
        showDataFetchError("No es posible solicitar este producto:", "Tienes una solicitud en proceso, válida la transacción en los movimientos de tu cuenta de nómina o inténtalo de nuevo más tarde.");
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
            showDataFetchError("Lo sentimos", "Se ha producido un error, inténtalo nuevamente en unos minutos.");
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
            showDataFetchError("Lo sentimos", "Se ha producido un error, inténtalo nuevamente en unos minutos.");
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
            showDataFetchError("Lo sentimos", "Se ha producido un error, inténtalo nuevamente en unos minutos.");
        }
    }
    @Override
    public void showCircularProgressBarDebtCapacity(){
        circularProgressBarTopes.setVisibility(View.VISIBLE);
        lblMontoMaximo.setVisibility(View.GONE);
        etMontoASolicitar.setEnabled(false);
        btnSolicitar.setEnabled(false);
        buttonMovimientosAnteriores.setVisibility(View.GONE);
    }
    @Override
    public void hideCircularProgressBarDebtCapacity(){
        circularProgressBarTopes.setVisibility(View.GONE);
        lblMontoMaximo.setVisibility(View.VISIBLE);
        etMontoASolicitar.setEnabled(true);
        btnSolicitar.setEnabled(true);
        buttonMovimientosAnteriores.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDebtCapacity(ResponseTopes topes){
        state.setTopes(topes);
        DecimalFormat formato = new DecimalFormat("#,###");
        String valorcupo = formato.format(topes.getV_cupo());
        String valormax = formato.format(topes.getV_maximo());
        String valormin = formato.format(topes.getV_minimo());
        lblMontoMaximo.setText(String.format("$%s", valorcupo));
        tvValoresTopes.setText(String.format("Recuerda que el monto mínimo a solicitar es $%s y máximo $%s por transacción.", valormin, valormax));
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
            showErrorWithRefreshMovements();
        }
    }

    @Override
    public void showMoves(List<ResponseMovimientos> movimientos){
        if(movimientos != null && movimientos.size() > 0){
            pullToRefreshMovements.setVisibility(View.VISIBLE);
            MovimientosAdelantoNominaAdapter adapter = new MovimientosAdelantoNominaAdapter(getContext(), movimientos);
            listMovimientos.setAdapter(adapter);
            contentAdelantoNomina.setOnTouchListener(new View.OnTouchListener() {
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
        }else{
            layoutCircularProgressBarMovements.setVisibility(View.VISIBLE);
            circularProgressBarMovements.setVisibility(View.GONE);
            textCircularProgressBarMovements.setText("No hay movimientos disponibles");
        }
    }
    @Override
    public void showCircularProgressBarMovements(String textProgressBar) {
        layoutCircularProgressBarMovements.setVisibility(View.VISIBLE);
        textCircularProgressBarMovements.setText(textProgressBar);
    }
    @Override
    public void hideCircularProgressBarMovements() {
        layoutCircularProgressBarMovements.setVisibility(View.GONE);
    }
    @Override
    public void showErrorWithRefreshMovements(){
        pullToRefreshMovements.setVisibility(View.GONE);
        layoutCircularProgressBarMovements.setVisibility(View.VISIBLE);
        circularProgressBarMovements.setVisibility(View.GONE);
        textCircularProgressBarMovements.setText("Ha ocurrido un error, inténtalo de nuevo ");
        buttonRefereshMovements.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.imageReferesh_movements)
    public void onClickRefreshMovements(){
        fetchMoves();
    }


    @Override
    public boolean validateRequestedAmount(){
        String valorsolicitado = etMontoASolicitar.getText().toString().replaceAll("[$,.]", "");
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
        etMontoASolicitar.setError(messageError);
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
//            showDataFetchError("Lo sentimos", "Se ha producido un error, inténtalo nuevamente en unos minutos.");
        }
    }




    @Override
    public void showContentSalaryAdvance(){
        contentAdelantoNomina.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideContentSalaryAdvance(){
        contentAdelantoNomina.setVisibility(View.GONE);
    }

    @Override
    public void showCircularProgressBar(String textProgressBar) {
        contentCircularProgressBar.setVisibility(View.VISIBLE);
        tvCircularProgressBar.setText(textProgressBar);
    }

    @Override
    public void hideCircularProgressBar() {
        contentCircularProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorWithRefresh(){
        contentAdelantoNomina.setVisibility(View.GONE);
        contentCircularProgressBar.setVisibility(View.VISIBLE);
        circularProgressBar.setVisibility(View.GONE);
        tvCircularProgressBar.setText("Ha ocurrido un error, inténtalo de nuevo ");
        buttonReferesh.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDialogError(String title, String message) {
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
                state.setRequisitos(null);
                state.setTopes(null);
                state.setValorSolicitado(null);
                dialog.dismiss();
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
                state.setRequisitos(null);
                state.setTopes(null);
                state.setValorSolicitado(null);
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
                dialog.dismiss();
            }
        });
        dialog.show();
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
                state.setRequisitos(null);
                state.setTopes(null);
                state.setValorSolicitado(null);
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void showExpiredToken(String message) {
        baseView.reiniciarEstado();
        baseView.finish();
        if(state.getUsuario() != null){
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.pop_up_closedsession);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button buttonClosedSession = (Button) dialog.findViewById(R.id.btnVolverAIngresar);
            buttonClosedSession.setOnClickListener(view -> dialog.dismiss());
            dialog.show();
        }
    }
}
