package solidappservice.cm.com.presenteapp.front.menuprincipal.FragmentHome;

import android.animation.LayoutTransition;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.home.ImageHomeSlideAdapter;
import solidappservice.cm.com.presenteapp.entities.banercomercial.response.ResponseBannerComercial;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.mensajes.response.ResponseObtenerMensajes;
import solidappservice.cm.com.presenteapp.entities.login.Response.Usuario;
import solidappservice.cm.com.presenteapp.entities.nequi.dto.SuscriptionData;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.base.main.ActivityMainView;
import solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.ActivityAgreementsView;
import solidappservice.cm.com.presenteapp.front.nequi.qr.ActivityCameraQR.ActivityQRCameraView;
import solidappservice.cm.com.presenteapp.front.nequi.suscription.ActivitySuscription.ActivitySuscriptionView;
import solidappservice.cm.com.presenteapp.front.popups.PopUp;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.tools.BadgeView;
import solidappservice.cm.com.presenteapp.tools.helpers.DialogHelpers;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 24/11/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 17/09/2021.
 */
public class FragmentHomeView extends Fragment implements FragmentHomeContract.View {

    private FragmentHomePresenter presenter;
    private ActivityMainView context;
    private ActivityBase activityBase;
    private GlobalState state;
    private FirebaseAnalytics firebaseAnalytics;
    private String urlCV;
    private Handler sliderHandler = new Handler();
    private boolean openAlert = false;

    @BindView(R.id.lblHolaUsuario)
    TextView lblHolaUsuario;
    @BindView(R.id.row_cant_messages)
    ImageView row_cant_messages;
    @BindView(R.id.layout_hola_usuario)
    LinearLayout holaUsuario;

    @BindView(R.id.titleBannerComercial)
    LinearLayout titleBannerComercial;
//    @BindView(R.id.viewPagerHome)
    ViewPager2 viewPagerHome;

    @BindView(R.id.imgb_finanzas)
    LinearLayout imgb_finanzas;
    @BindView(R.id.imgb_alianzas)
    LinearLayout imgb_alianzas;
    @BindView(R.id.imgb_centrosvacacionales)
    LinearLayout imgb_centrosvacacionales;

    @BindView(R.id.buttonTransferencias)
    LinearLayout btnEnviardinero;
    @BindView(R.id.buttonAbrirAhorro)
    LinearLayout btnAbrirahorro;
    @BindView(R.id.buttonMisMensajes)
    LinearLayout btnMisMensajes;
    @BindView(R.id.buttonPagosQR)
    LinearLayout btnPagosQR;

    GridLayout circularProgressBarOptions;
    GridLayout contentOptions;

    @BindView(R.id.relative_alerta_tarea)
    LinearLayout lateralAlert;

    @BindView(R.id.circular_progress_bar)
    ProgressBar circularProgressBar;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de menu principal");
        firebaseAnalytics.logEvent("pantalla_menu_principal", params);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPagerHome = (ViewPager2) view.findViewById(R.id.viewPagerHome);
        circularProgressBarOptions = (GridLayout) view.findViewById(R.id.circular_progress_bar_options);
        contentOptions = (GridLayout) view.findViewById(R.id.contentOptions);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentHomePresenter(this, new FragmentHomeModel());
        context = (ActivityMainView) getActivity();
        activityBase = (ActivityBase)getActivity();
        state = context.getState();
        if (context != null) {
            context.btn_back.setVisibility(View.GONE);
            context.header.setImageResource(R.drawable.logo_login);
        }
        if (lblHolaUsuario != null && state != null &&
                state.getUsuario() != null && state.getUsuario().getNombreAsociado() != null) {
            lblHolaUsuario.setText(lblHolaUsuario.getText().toString().replace("#usuario#", state.getUsuario().getNombreAsociado()));
        }

        if(state != null && state.isActiveButtonPaymentQR()){
            btnPagosQR.setVisibility(View.VISIBLE);
        }else{
            btnPagosQR.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (state != null && state.getDatosSuscripcion() == null && state.isActiveStateSuscriptions()) {
            validateSuscriptionNequi();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        Usuario usuario = state.getUsuario();
        if(usuario == null){
            context.reiniciarEstado();
            context.setFragment(IFragmentCoordinator.Pantalla.Ingreso);
            return;
        }
        fetchButtonStateAgreements();
        fetchButtonStateTransfers();
        fetchCommercialBanner();
        sliderHandler.postDelayed(sliderRunnable, 5000);
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @OnClick(R.id.buttonMisMensajes)
    public void onClickMisMensajes(View v) {
        context.showScreenInbox();
    }

    @OnClick(R.id.buttonTransferencias)
    public void onClickSendMoney(View v) {
        Intent intent = new Intent(activityBase, ActivityTabsView.class);
        intent.putExtra("TabIndex", ActivityTabsView.TAB_23_NEQUI_MENU_SEND_MONEY_TAG);
        context.startActivityForResult(intent, GlobalState.TABS);
    }

    @OnClick(R.id.buttonAbrirAhorro)
    public void onClickOpenSavings(View v) {
        Intent intent = new Intent(activityBase, ActivityTabsView.class);
        intent.putExtra("TabIndex", ActivityTabsView.TAB_4_SAVINGS_SOLICITY_TAG);
        context.startActivityForResult(intent, GlobalState.TABS);
    }

    @OnClick(R.id.buttonPagosQR)
    public void onClickPaymentQr(View v) {
        Intent intentQR = new Intent(context, ActivityQRCameraView.class);
        context.startActivityForResult(intentQR, GlobalState.QR);
    }

    @OnClick(R.id.imgb_alianzas)
    public void onClickAlianzas(View v) {
        Intent intent_convenios = new Intent(context, ActivityAgreementsView.class);
        context.startActivityForResult(intent_convenios, GlobalState.ACTIVITY_AGREEMENTS);
    }

    @OnClick(R.id.imgb_finanzas)
    public void onClickFinanzas(View v) {
        context.showScreenFinanceMenu();
    }

    @OnClick(R.id.imgb_centrosvacacionales)
    public void onClickResorts(View v) {
        try{
            if(urlCV != null && !urlCV.isEmpty()){
                Encripcion encripcion = new Encripcion();
                String tokenSession = encripcion.encryptAES(state.getUsuario().getCedula()+":"+state.getUsuario().getToken());
                String url = urlCV.replace("${tokenSession}", tokenSession).replace("${origin}", "APPPRESENTE-ANDROID");
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        }catch (Exception e){
            showDataFetchError("Lo sentimos","Tenemos un problema al cargar el sitio, intentálo de nuevo más tarde.");
            Log.d("Error", e.getMessage());
        }
    }

    @OnClick(R.id.textViewTienesPendiente)
    public void onClickPendientTask(View v){
        try {
            Intent i = new Intent(context, ActivitySuscriptionView.class);
            context.startActivityForResult(i, GlobalState.NEQUI);
        }catch (Exception e ){
            String Ex = e.getMessage();
        }
    }

    @OnClick(R.id.button_alert)
    public void onClickButtonAlert(View v) {
        openAlert = !openAlert;
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
               0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                60
        );
        holaUsuario.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        holaUsuario.setLayoutParams(param);
    }

    @OnClick(R.id.flecha_alerta)
    public void onClickFlechaAlerta(){
        openAlert = !openAlert;
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                94
        );
        holaUsuario.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        holaUsuario.setLayoutParams(param);
    }

    @Override
    public void fetchButtonStateAgreements() {
        try{
            presenter.fetchButtonStateAgreements();
        }catch (Exception ex){
            hideCircularProgressBar();
            hideButtonAgreements();
            fetchButtonStateResorts();
        }
    }

    @Override
    public void showButtonAgreements() {
        imgb_alianzas.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideButtonAgreements() {
        imgb_alianzas.setVisibility(View.GONE);
    }

    @Override
    public void fetchButtonStateResorts(){
        try{
            presenter.fetchButtonStateResorts();
        }catch (Exception ex){
            hideCircularProgressBar();
            hideButtonResorts();
        }
    }

    @Override
    public void showButtonResorts(String urlLinkResort){
        this.urlCV = urlLinkResort;
        imgb_centrosvacacionales.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideButtonResorts(){
        imgb_centrosvacacionales.setVisibility(View.GONE);
    }


    @Override
    public void fetchButtonStateTransfers() {
        try{
            presenter.fetchButtonStateTransfers();
        }catch (Exception ex){
            hideButtonTransfers();
            fetchButtonStateSavings();
        }
    }
    @Override
    public void showButtonTransfers() {
        btnEnviardinero.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideButtonTransfers() {
        btnEnviardinero.setVisibility(View.GONE);
    }


    @Override
    public void fetchButtonStateSavings() {
        try{
            presenter.fetchButtonStateSavings();
        }catch (Exception ex){
            hideButtonSavings();
            fetchMessageInbox();
        }
    }
    @Override
    public void showButtonSavings() {
        btnAbrirahorro.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideButtonSavings() {
        btnAbrirahorro.setVisibility(View.GONE);
    }


    @Override
    public void fetchMessageInbox(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchMessageInbox(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch (Exception ex){
            showButtonMessageInbox();
            hideCircularProgressBarOptions();
            showContentOptions();
        }
    }

    @Override
    public void showMessageInbox(List<ResponseObtenerMensajes> messagesInbox){
        state.setMensajesBuzon(messagesInbox);
        showNotificationBadge();
    }
    @Override
    public void showButtonMessageInbox() {
        btnMisMensajes.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideButtonMessageInbox() {
        btnMisMensajes.setVisibility(View.GONE);
    }

    @Override
    public void showNotificationBadge(){
        int numberOfUnReadMessages = 0;
        if(state.getMensajesBuzon() != null && state.getMensajesBuzon().size() > 0){
            int counter = 0;
            for(ResponseObtenerMensajes m : state.getMensajesBuzon()){
                if(m.getLeido().equals("N")){
                    counter++;
                }
            }
            numberOfUnReadMessages = counter;
        }
        int whidth = numberOfUnReadMessages>=10 ? 60 : 45;
        if (numberOfUnReadMessages > 0 && row_cant_messages != null) {
            BadgeView badgeView = new BadgeView(context, row_cant_messages);
            badgeView.setText(String.valueOf(numberOfUnReadMessages));
            badgeView.setTextSize(10);
            badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
            badgeView.setGravity(Gravity.END|Gravity.TOP);
            badgeView.setWidth(whidth);
            badgeView.setHeight(40);
            badgeView.setBadgeMargin(-1, -1);
            badgeView.setBadgeBackgroundColor(Color.parseColor("#FFEA00"));
            TranslateAnimation anim = new TranslateAnimation(-100, 0, 0, 0);
            anim.setInterpolator(new BounceInterpolator());
            anim.setDuration(1000);
            badgeView.toggle(anim, null);
        }
    }


    @Override
    public void showCircularProgressBarOptions() {
        circularProgressBarOptions.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCircularProgressBarOptions() {
        circularProgressBarOptions.setVisibility(View.GONE);
    }

    @Override
    public void showContentOptions() {
        contentOptions.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideContentOptions() {
        contentOptions.setVisibility(View.GONE);
    }


    @Override
    public void validateSuscriptionNequi(){
        try{
            presenter.validateSuscriptionNequi(new BaseRequest(
                    Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch(Exception ex){
            saveSuscriptionData(null, null);
        }
    }

    @Override
    public void saveSuscriptionData(SuscriptionData datosSuscripcion, String statusSuscription){
        state.setDatosSuscripcion(datosSuscripcion);
        state.setStatusSuscription(statusSuscription);
        context.saveSuscriptionData(datosSuscripcion, statusSuscription);
        if(state.getFragmentActual() == IFragmentCoordinator.Pantalla.MenuPrincipal &&
                state.getStatusSuscription() != null && state.getStatusSuscription().equals("0")){
            getTimeExpiration();
        }else{
            activateAlert(false);
        }
    }

    @Override
    public void getTimeExpiration(){
        try{
            presenter.getTimeExpiration();
        }catch(Exception ex){
            resultTimeExpiration(15);
            getTimeOfSuscription();
        }
    }

    @Override
    public void resultTimeExpiration(Integer timeExpiration){
        state.setTiempoExpiracionAutorizacion(timeExpiration);
    }

    @Override
    public void getTimeOfSuscription(){
        presenter.getTimeOfsuscription(new BaseRequestNequi(
                Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
                state.getUsuario().getToken()
        ));
    }

    @Override
    public void calculeMinutes(Integer days, Integer hour, Integer minute, Integer second){
        if(days != null && days >= 0 &&
                hour != null && hour <=0 &&
                minute != null && minute < state.getTiempoExpiracionAutorizacion()){
            int milisecons = hour+(minute*60000)+(second*1000);
            activateAlert(true);
            context.activateButtonWarning(true);
            new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    activateAlert(false);
                    context.activateButtonWarning(false);
                }
            }, milisecons);
        }else{
            state.setStatusSuscription("");
            context.activateButtonWarning(false);
            activateAlert(false);
        }
    }

    @Override
    public void activateAlert(boolean activate){
        if(activate) {
            lateralAlert.setVisibility(View.VISIBLE);
        }else{
            lateralAlert.setVisibility(View.GONE);
        }
    }

    @Override
    public void fetchCommercialBanner(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchCommercialBanner(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch (Exception ex){
            hideCircularProgressBar();
            viewPagerHome.setVisibility(View.GONE);
            titleBannerComercial.setVisibility(View.GONE);
        }
    }

    @Override
    public void showCommercialBanner(List<ResponseBannerComercial> commercialBanners){
        if(commercialBanners != null && commercialBanners.size()>0){
            viewPagerHome.setVisibility(View.VISIBLE);
            viewPagerHome.setAdapter(new ImageHomeSlideAdapter(context, commercialBanners, viewPagerHome, state.getUsuario()));
            viewPagerHome.setClipToPadding(false);
            viewPagerHome.setClipChildren(false);
            viewPagerHome.setOffscreenPageLimit(3);
            viewPagerHome.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
            compositePageTransformer.addTransformer(new MarginPageTransformer(40));
            compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                @Override
                public void transformPage(@NonNull View page, float position) {
                    float r = 1 - Math.abs(position);
                    page.setScaleY(0.85f + r * 0.15f);
                }
            });
            viewPagerHome.setPageTransformer(compositePageTransformer);

            viewPagerHome.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    sliderHandler.removeCallbacks(sliderRunnable);
                    sliderHandler.postDelayed(sliderRunnable, 5000);
                }
            });
        }else{
            viewPagerHome.setVisibility(View.GONE);
            titleBannerComercial.setVisibility(View.GONE);
        }

    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPagerHome.setCurrentItem(viewPagerHome.getCurrentItem() + 1);
        }
    };

    @Override
    public void hideCommercialBanner(){
        viewPagerHome.setVisibility(View.GONE);
    }

    @Override
    public void showCircularProgressBar() {
        circularProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCircularProgressBar() {
        circularProgressBar.setVisibility(View.GONE);
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
        final Dialog dialog = getDialog(context, R.layout.pop_up_error);
        TextView titleMessage = dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText("Lo sentimos");
        TextView contentMessage =  dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        ImageButton buttonClose = dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(view -> dialog.dismiss());
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
        final Dialog dialog = getDialog(context, R.layout.pop_up_error);
        TextView titleMessage = dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText(title);
        TextView contentMessage = dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        ImageButton buttonClose =  dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void showExpiredToken(String message) {
        if(state.getUsuario() != null){
            context.reiniciarEstado();
            context.setFragment(IFragmentCoordinator.Pantalla.Ingreso);
            final Dialog dialog = getDialog(context, R.layout.pop_up_closedsession);
            Button buttonClosedSession = (Button) dialog.findViewById(R.id.btnVolverAIngresar);
            buttonClosedSession.setOnClickListener(view -> dialog.dismiss());
            dialog.show();
        }
    }

    private Dialog getDialog(ActivityMainView context, Integer idPopUp){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(idPopUp);
        return dialog;
    }
}
