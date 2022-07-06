package solidappservice.cm.com.presenteapp.front.menutransacciones.FragmentTransactionsMenu;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.tools.RearrangingGridLayout;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;


/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 25/11/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 17/09/2021.
 */
public class FragmentTransactionsMenuView extends Fragment implements FragmentTransactionsMenuContract.View {

    private FragmentTransactionsMenuPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private FirebaseAnalytics firebaseAnalytics;
    private String dependenciaUsuario;
    private List<String> listDependencias;
    private RearrangingGridLayout rglSections;
    boolean isDendenciaActiva = false;

    @BindView(R.id.layoutSolicitudAhorro)
    LinearLayout btnSolicitudAhorro;
    @BindView(R.id.layoutAbonosCreditos)
    LinearLayout btnAbonosAcreditos;
    @BindView(R.id.layoutAdelantoNomina)
    LinearLayout btnAdelantoNomina;
    @BindView(R.id.layoutEnviaDinero)
    LinearLayout btnEnviarDinero;

    @BindView(R.id.layout_circular_progress_bar)
    LinearLayout layoutCircularProgressBar;
    @BindView(R.id.circular_progress_bar)
    ProgressBar circularProgressBar;
    @BindView(R.id.text_circular_progress_Bar)
    TextView textCircularProgressBar;
    @BindView(R.id.imageReferesh)
    ImageView buttonReferesh;
    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de transacciones");
        firebaseAnalytics.logEvent("pantalla_transacciones", params);
        View view = inflater.inflate(R.layout.fragment_transactionmenu, container,false);
        ButterKnife.bind(this, view);
        rglSections = view.findViewById(R.id.gridLayoutSections);
        rglSections.saveViews();
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentTransactionsMenuPresenter(this, new FragmentTransactionsMenuModel());
        context = (ActivityBase) getActivity();
        state = context.getState();
        listDependencias = new ArrayList<>();
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                state.setEstadoAdelantoNomina(null);
                state.setListaDependenciasActivas(null);
                state.setDependenciaAsociado(null);
                fetchButtonStateAdvanceSalary();
                pullToRefresh.setRefreshing(false);
            }
        });
        fetchButtonStateAdvanceSalary();
    }

    @Override
    public void onResume() {
        super.onResume();
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

    @OnClick(R.id.imageReferesh)
    public void onClickRefresh(){
        state.setEstadoAdelantoNomina(null);
        state.setListaDependenciasActivas(null);
        state.setDependenciaAsociado(null);
        fetchButtonStateAdvanceSalary();
    }

    @OnClick(R.id.layoutAdelantoNomina)
    public void onClickAdelantoNomina(View v) {
        validateSalaryAdvanceStatus();
    }

    @OnClick(R.id.layoutEnviaDinero)
    public void onClickEnviarDinero(View v) {
        btnEnviarDinero.setBackgroundColor(Color.parseColor("#F2F2F2"));
        state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_23_NEQUI_MENU_SEND_MONEY_TAG);
    }

    @Override
    public void fetchButtonStateAdvanceSalary(){
        try{
            if(state != null && !TextUtils.isEmpty(state.getEstadoAdelantoNomina())){
                if(state.getEstadoAdelantoNomina().equals("Y")){
                    showButtonAdvanceSalary();
                    fetchButtonActionAdvanceSalary();
                }else{
                    hideCircularProgressBar();
                    showTransactionMenu();
                    hideButtonAdvanceSalary();
                }
            }else{
                presenter.fetchButtonStateAdvanceSalary();
            }
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
            showErrorWithRefresh();
        }
    }

    @Override
    public void fetchButtonActionAdvanceSalary(){
        try{
            if(state != null && state.getListaDependenciasActivas()!= null){
                this.listDependencias = state.getListaDependenciasActivas();
                fetchAssociatedDependency();
            }else{
                presenter.fetchButtonActionAdvanceSalary();
            }
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
            showErrorWithRefresh();
        }
    }

    @Override
    public void changeButtonActionAdvanceSalary(List<String> listDependencies){
        this.listDependencias = listDependencies;
    }

    @Override
    public void fetchAssociatedDependency(){
        try{
            if(state != null && !TextUtils.isEmpty(state.getDependenciaAsociado())){
                showResultAssociatedDependency(state.getDependenciaAsociado());
                fetchButtonStateTransfers();
            }else{
                Encripcion encripcion = Encripcion.getInstance();
                presenter.fetchAssociatedDependency(new BaseRequest(
                        encripcion.encriptar(state.getUsuario().getCedula()),
                        state.getUsuario().getToken()
                ));
            }
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
            showErrorWithRefresh();
        }
    }

    @Override
    public void showResultAssociatedDependency(String associatedDependency){
        state.setDependenciaAsociado(associatedDependency);
        this.dependenciaUsuario = associatedDependency;
        if (associatedDependency != null){
            isDendenciaActiva = listDependencias.size() == 0 || listDependencias.contains(associatedDependency);
        }
    }

    @Override
    public void showButtonAdvanceSalary(){
        rglSections.showViewAtIndex(2);
    }

    @Override
    public void hideButtonAdvanceSalary(){
        rglSections.hideViewAtIndex(2);
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
            state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_19_ADVANCE_SALARY_SOLICITY_TAG);
        }
    }

    @Override
    public void fetchButtonStateTransfers() {
        try{
            presenter.fetchButtonStateTransfers();
        }catch (Exception ex){
            hideButtonTransfers();
        }
    }
    @Override
    public void showButtonTransfers() {

        rglSections.showViewAtIndex(1);
    }
    @Override
    public void hideButtonTransfers() {
        rglSections.hideViewAtIndex(1);
    }


    @Override
    public void fetchButtonStateSavings() {
        try{
            presenter.fetchButtonStateSavings();
        }catch (Exception ex){
            hideButtonSavings();
        }
    }
    @Override
    public void showButtonSavings() {
        rglSections.showViewAtIndex(0);
    }
    @Override
    public void hideButtonSavings() {
        rglSections.hideViewAtIndex(0);
    }

    @Override
    public void fetchButtonStatePaymentCredits() {
        try{
            presenter.fetchButtonStatePaymentCredits();
        }catch (Exception ex){
            hideButtonPaymentCredits();
            hideCircularProgressBar();
        }
    }
    @Override
    public void showButtonPaymentCredits() {
        rglSections.showViewAtIndex(3);
    }
    @Override
    public void hideButtonPaymentCredits() {
        rglSections.hideViewAtIndex(3);
    }

    @Override
    public void showTransactionMenu(){
        pullToRefresh.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTransactionMenu(){
        pullToRefresh.setVisibility(View.GONE);
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
    public void showErrorWithRefresh(){
        pullToRefresh.setVisibility(View.GONE);
        layoutCircularProgressBar.setVisibility(View.VISIBLE);
        circularProgressBar.setVisibility(View.GONE);
        textCircularProgressBar.setText("Ha ocurrido un error, inténtalo de nuevo ");
        buttonReferesh.setVisibility(View.VISIBLE);
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
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void showDataFetchError(String title, String message){
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
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void showExpiredToken(String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_closedsession);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button buttonClosedSession = (Button) dialog.findViewById(R.id.btnVolverAIngresar);
        buttonClosedSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                context.salir();
            }
        });
        dialog.show();
    }

}