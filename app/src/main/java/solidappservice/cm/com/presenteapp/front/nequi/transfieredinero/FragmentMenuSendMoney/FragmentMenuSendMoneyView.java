package solidappservice.cm.com.presenteapp.front.nequi.transfieredinero.FragmentMenuSendMoney;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.nequi.dto.SuscriptionData;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.nequi.suscription.ActivitySuscription.ActivitySuscriptionView;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

public class FragmentMenuSendMoneyView extends Fragment implements FragmentMenuSendMoneyContract.View {

    private FirebaseAnalytics firebaseAnalytics;
//    private ProgressDialog pd;
    private Dialog pd;
    private GlobalState state;
    private ActivityBase context;
    private boolean isSuscriptionRequired = true;
    private FragmentMenuSendMoneyPresenter presenterEnviaDinero;

    @BindView(R.id.layoutTransferenciasPresente)
    LinearLayout layoutTransferenciasPresente;
    @BindView(R.id.buttonTransferenciasPresente)
    Button buttonTransferenciasPresente;
    @BindView(R.id.containerTransferPresente)
    LinearLayout containerTransferPresente;
    @BindView(R.id.btnInscribirCuenta)
    LinearLayout btnInscribirCuenta;
    @BindView(R.id.btnEliminarCuenta)
    LinearLayout btnEliminarCuenta;
    @BindView(R.id.btnEnviarDinero)
    LinearLayout btnEnviarDinero;

    @BindView(R.id.layoutTransferenciasNequi)
    LinearLayout layoutTransferenciasNequi;
    @BindView(R.id.buttonTransferenciasNequi)
    Button buttonTransferenciasNequi;
    @BindView(R.id.containerTransferNequi)
    LinearLayout containerTransferNequi;
    @BindView(R.id.containerSuscriptionNequi)
    LinearLayout containerSuscriptionNequi;
    @BindView(R.id.btnEnviarANequi)
    LinearLayout btnEnviarAnequi;
    @BindView(R.id.btnTraerDeNequi)
    LinearLayout btnTraerDeNequi;


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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción","Interacción con panatalla menu de transferir dinero");
        firebaseAnalytics.logEvent("pantalla_menu_transfiere_dinero",params);
        View View = inflater.inflate(R.layout.fragment_nequi_menu_sendmoney, container, false);
        ButterKnife.bind(this,View);
        setControl();
        return  View;
    }

    protected void setControl(){
        presenterEnviaDinero = new FragmentMenuSendMoneyPresenter(this, new FragmentMenuSendMoneyModel());
        context = (ActivityBase)getActivity();
        state = context.getState();
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchSuscriptionNequi();
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        if(state != null && state.isActiveStateSuscriptions() && (state.isActiveButtonPaymentDispersiones() || state.isActiveButtonPaymentSuscriptions())){
            fetchSuscriptionNequi();
            layoutTransferenciasNequi.setVisibility(View.VISIBLE);

            if(state.isActiveButtonPaymentDispersiones()){
                btnEnviarAnequi.setVisibility(View.VISIBLE);
            }else{
                btnEnviarAnequi.setVisibility(View.GONE);
            }

            if(state.isActiveButtonPaymentSuscriptions()){
                btnTraerDeNequi.setVisibility(View.VISIBLE);
            }else{
                btnTraerDeNequi.setVisibility(View.GONE);
            }
        }else{
            layoutTransferenciasNequi.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null || state.getUsuario() == null){
            context.salir();
            return;
        }
    }

    @OnClick(R.id.containerSuscriptionNequi)
    public void onClickSuscripcion(){
//        validateSuscriptionNequi();
        try{
            Intent i = new Intent(context, ActivitySuscriptionView.class);
            startActivity(i);
        }catch(Exception ex){
            showDataFetchError("Lo sentimos", "Tenemos problemas para conectarnos con Nequi, intentálo de nuevo más tarde");
        }
    }

    @OnClick(R.id.btnInscribirCuenta)
    public void onClickInscribirCuenta(){
        btnInscribirCuenta.setBackgroundColor(Color.parseColor("#F2F2F2"));
        state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_15_TRANSFERS_REGISTER_ACCOUNT_TAG);
    }

    @OnClick(R.id.btnEliminarCuenta)
    public void onClickEliminarCuenta(){
        btnEliminarCuenta.setBackgroundColor(Color.parseColor("#F2F2F2"));
        state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_16_TRANSFERS_DELETE_ACCOUNT_TAG);
    }

    @OnClick(R.id.btnEnviarDinero)
    public void onClickEnviarDinero(){
        btnEnviarDinero.setBackgroundColor(Color.parseColor("#F2F2F2"));
        state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_18_TRANSFERS_MAKE_TRANSFER_TAG);
    }

    @OnClick(R.id.btnEnviarANequi)
    public void onClickEnviarDineroaNequi(){
        btnEnviarAnequi.setBackgroundColor(Color.parseColor("#F2F2F2"));
        state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_24_NEQUI_DISPERSIONES_TAG);
    }

    @OnClick(R.id.btnTraerDeNequi)
    public void onClickTraerDineroDeNequi(){
        btnTraerDeNequi.setBackgroundColor(Color.parseColor("#F2F2F2"));
        state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_25_NEQUI_SUSCRIPTIONS_PAYMENT_TAG);
    }


    @OnClick(R.id.buttonTransferenciasPresente)
    public void onClickTransferenciasPresente(){
        if(containerTransferPresente.getVisibility() == View.VISIBLE){
            shrinkSectionPresenteTransfer(); //Oculta button transferencias presente
        }else{
            shrinkSectionNequiTransfer();//Oculta button transferencias nequi
            expandSectionPresenteTransfer();//Muestra button transferencias presente
        }
    }

    @OnClick(R.id.buttonTransferenciasNequi)
    public void onClickTransferenciasNequi(){
        if(containerTransferNequi.getVisibility() == View.VISIBLE || containerSuscriptionNequi.getVisibility() == View.VISIBLE){
            shrinkSectionNequiTransfer();//Oculta button transferencias nequi
        }else{
            shrinkSectionPresenteTransfer();//Oculta button transferencias presente
            expandSectionNequiTransfer();//Muestra button transferencias nequi
        }
    }

    @Override
    public void expandSectionPresenteTransfer() {
        layoutTransferenciasPresente.setBackground(getResources().getDrawable(R.drawable.transferencias_menos));
        containerTransferPresente.setVisibility(View.VISIBLE);
    }

    @Override
    public void shrinkSectionPresenteTransfer() {
        layoutTransferenciasPresente.setBackground(getResources().getDrawable(R.drawable.transferencia_mas));
        containerTransferPresente.setVisibility(View.GONE);
    }

    @Override
    public void expandSectionNequiTransfer(){
        layoutTransferenciasNequi.setBackground(getResources().getDrawable(R.drawable.transferencias_menos));
        if(isSuscriptionRequired) {
            containerSuscriptionNequi.setVisibility(View.VISIBLE);
            containerTransferNequi.setVisibility(View.GONE);
        }else{
            containerSuscriptionNequi.setVisibility(View.GONE);
            containerTransferNequi.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void shrinkSectionNequiTransfer(){
        layoutTransferenciasNequi.setBackground(getResources().getDrawable(R.drawable.transferencia_mas));
        containerTransferNequi.setVisibility(View.GONE);
        containerSuscriptionNequi.setVisibility(View.GONE);
    }

    @Override
    public void fetchSuscriptionNequi(){
        try{
            if(state != null && state.getDatosSuscripcion() != null){
                hideCircularProgressBar();
                showContentSendMoneyMenu();
                notRequiredSuscription();
                containerSuscriptionNequi.setVisibility(View.GONE);
                containerTransferNequi.setVisibility(View.GONE);
                showSectionTransferNequiMenu();
            } else if(state != null &&
                    !TextUtils.isEmpty(state.getStatusSuscription()) &&
                        (state.getStatusSuscription().equals("0") ||
                            state.getStatusSuscription().equals("2") ||
                                state.getStatusSuscription().equals("3"))){
                hideCircularProgressBar();
                showContentSendMoneyMenu();
                requiredSuscription();
                showSectionTransferNequiMenu();
            }else{
                presenterEnviaDinero.fetchSuscriptionNequi(new BaseRequest(
                        Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
                        state.getUsuario().getToken()
                ));
            }
        }catch(Exception ex){
            hideCircularProgressBar();
            hideSectionTransferNequiMenu();
            showContentSendMoneyMenu();
        }
    }

    @Override
    public void requiredSuscription() {
        isSuscriptionRequired = true;
    }

    @Override
    public void notRequiredSuscription() {
        isSuscriptionRequired = false;
    }

    @Override
    public void saveSuscriptionData(SuscriptionData datosSuscripcion){
        state.setDatosSuscripcion(datosSuscripcion);
    }

//    @Override
//    public void validateSuscriptionNequi(){
//        try{
//            presenterEnviaDinero.validateSuscriptionNequi(new BaseRequest(
//                    Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
//                    state.getUsuario().getToken()
//            ));
//        }catch(Exception ex){
//            showDataFetchError("Lo sentimos", "");
//            showErrorWithRefresh();
//        }
//    }

//    @Override
//    public void redirectSuscriptionRequired(){
//        Intent intent_cero = new Intent(context, ActivitySuscriptionView.class);
//        startActivity(intent_cero);
//    }

    @Override
    public void showContentSendMoneyMenu(){
        pullToRefresh.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideContentSendMoneyMenu(){
        pullToRefresh.setVisibility(View.GONE);
    }

    @Override
    public void showSectionTransferNequiMenu() {
        layoutTransferenciasNequi.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSectionTransferNequiMenu(){
        layoutTransferenciasNequi.setVisibility(View.GONE);
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
    public void showProgressDialog(String messageDialog) {
        pd = new Dialog(context);
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.setCanceledOnTouchOutside(false);
        pd.setContentView(R.layout.pop_up_loading);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView contentMessage = (TextView) pd.findViewById(R.id.lbl_content_message);
        contentMessage.setText(messageDialog);
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
                dialog.dismiss();
            }
        });
        dialog.show();
    }


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
