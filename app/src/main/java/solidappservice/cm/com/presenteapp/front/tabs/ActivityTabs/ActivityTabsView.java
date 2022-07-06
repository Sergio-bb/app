package solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTabHost;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.login.Response.Usuario;
import solidappservice.cm.com.presenteapp.entities.mensajes.response.ResponseObtenerMensajes;
import solidappservice.cm.com.presenteapp.entities.nequi.dto.SuscriptionData;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.actualizaciondatos.ActivityUpdatePersonalData.ActivityUpdatePersonalDataView;
import solidappservice.cm.com.presenteapp.front.base.main.ActivityMainView;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityDirectory.ActivityDirectoryView;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityGeoreferencing.ActivityLocationsGms.ActivityLocationsGmsView;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityGeoreferencing.ActivityLocationsHms.ActivityLocationsHmsView;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityPortfolio.ActivityPortfolioProducts.ActivityPortfolioProductsView;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityServices.ActivityServicesView;
import solidappservice.cm.com.presenteapp.front.nequi.suscription.ActivitySuscription.ActivitySuscriptionView;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.tools.tabsbasecontainers.*;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**R
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 19/09/2021.
 */
public class ActivityTabsView extends ActivityBase implements ActivityTabsContract.View{

    private ActivityTabsPresenter presenter;
    private ActivityBase context;
    private GlobalState state;

    public static final int TAB_0_STATUS_ACCOUNT_TAG = 0; //FragmentStatusAccountView
    public static final int TAB_1_TRANSACTIONS_MENU_TAG = 1;//FragmentTransactionsMenuView
    public static final int TAB_2_PRESENTE_CARD_MENU_TAG = 2;//FragmentPresenteCardMenuView
    public static final int TAB_3_INBOX_TAG = 3;//FragmentInboxView
    public static final int TAB_4_SAVINGS_SOLICITY_TAG = 4;//FragmentSavingsSolicityView
    public static final int TAB_5_INBOX_READ_MESSAGE_TAG = 5;//FragmentReadMessageView
    public static final int TAB_6_STATUS_ACCOUNT_PRODUCTS_TAG = 6;//FragmentProductsView
    public static final int TAB_7_STATUS_ACCOUNT_MOVEMENTS_PRODUCT_TAG = 7;//FragmentMovementsProductsView
    public static final int TAB_8_PAYMENTS_CREDITS_TAG = 8;//FragmentPaymentCreditsView
    public static final int TAB_9_SOLICITY_RESORTS_TAG = 9;//FragmentSolicityResortsView
    public static final int TAB_10_PRESENTE_CARD_ACTIVE_TAG = 10;//FragmentActivateCardView
    public static final int TAB_11_PRESENTE_CARD_BLOCK_TAG = 11;//FragmentBlockCardView
    public static final int TAB_12_PRESENTE_CARD_MOVEMENTS_PRODUCT_TAG = 12;//FragmentMovementsProductsCardView
    public static final int TAB_13_PRESENTE_CARD_PRODUCTS_TAG = 13;//FragmentProductsCardView
    public static final int TAB_14_PRESENTE_CARD_SECURITY_MENU_TAG = 14;//FragmentSecurityCardMenuView
    public static final int TAB_15_TRANSFERS_REGISTER_ACCOUNT_TAG = 15;//FragmentRegisterAccountView
    public static final int TAB_16_TRANSFERS_DELETE_ACCOUNT_TAG = 16;//FragmentDeleteAccountView
    public static final int TAB_17_TRANSFERS_MENU_TAG = 17;//FragmentTransfersMenuView
    public static final int TAB_18_TRANSFERS_MAKE_TRANSFER_TAG = 18;//FragmentMakeTransferView
    public static final int TAB_19_ADVANCE_SALARY_SOLICITY_TAG = 19;//FragmentSolicityView
    public static final int TAB_20_ADVANCE_SALARY_DETAIL_TAG = 20;//FragmentDetailView
    public static final int TAB_21_PRESENTE_CARD_REPLACEMENT_TAG = 21;//FragmentReplacementCardView
    public static final int TAB_22_PRESENTE_CARD_REPLACEMENTDETAIL_TAG = 22;//FragmentReplacementCardDetailView

    public static final int TAB_23_NEQUI_MENU_SEND_MONEY_TAG = 23;//FragmentMenuSendMoneyView
    public static final int TAB_24_NEQUI_DISPERSIONES_TAG = 24;//FragmentDispersionesView
    public static final int TAB_25_NEQUI_SUSCRIPTIONS_PAYMENT_TAG = 25;//FragmentSuscriptionsPaymentView

    public static final int ITEM_TAB_INDEX_STATUSACCOUNT = 0;
    public static final int ITEM_TAB_INDEX_TRANSACTIONS = 1;
    public static final int ITEM_TAB_INDEX_PRESENTE_CARD = 2;
    public static final int ITEM_TAB_INDEX_INBOX = 3;

    private FragmentTabHost mTabHost;
    private int tabIndex;
    private int tabSelected;
    private ViewGroup viewGroup;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @BindView(R.id.btnPortafolio)
    ImageButton btnPortafolio;
    @BindView(R.id.btnPreguntasFrecuentes)
    ImageButton btnPreguntasFrecuentes;
    @BindView(R.id.btnDirectorio)
    ImageButton btnDirectorio;
    @BindView(R.id.btnEncuentranos)
    ImageButton btnEncuentranos;
    @BindView(R.id.btn_back)
    public ImageButton btnBack;

    @BindView(R.id.toolbarSideBar)
    Toolbar toolbarSideBar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.nombreUsuario)
    TextView nombreUsuario;
    @BindView(R.id.buttonVinculaCuentaNequi)
    LinearLayout buttonVinculaCuentaNequi;
    @BindView(R.id.buttonConoceTuGestor)
    LinearLayout buttonConoceTuGestor;
    @BindView(R.id.buttonCentroDeAyuda)
    LinearLayout buttonCentroDeAyuda;
    @BindView(R.id.buttonInformacionPersonal)
    LinearLayout buttonInformacionPersonal;
    @BindView(R.id.sectionFechaIngreso)
    LinearLayout sectionFechaIngreso;
    @BindView(R.id.tvFechaIngreso)
    TextView textFechaIngreso;
    @BindView(R.id.btnCerrarSession)
    TextView btnCerrarSession;

    @BindView(R.id.layout_buton_vincula)
    LinearLayout containerButtonNequi;
    @BindView(R.id.warning_icon)
    RelativeLayout warningIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewGroup = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_tabs, null);
        setContentView(R.layout.activity_tabs);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            tabIndex = bundle.getInt("TabIndex", 0);
            tabSelected = bundle.getInt("TabSelected", 0);
        }
        ButterKnife.bind(this);
        setControls();
    }

    @Override
    protected void setControls() {
        presenter = new ActivityTabsPresenter(this, new ActivityTabsModel());
        context = this;
        state = (GlobalState) getApplicationContext();
        mTabHost = findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        //ESTADO DE CUENTA
        View tabview = createTabView(mTabHost, "Est Cuenta", viewGroup);
        mTabHost.addTab(createTabSpec(TAB_0_STATUS_ACCOUNT_TAG, tabview), StatusAccountViewContainerFragment.class, null);
        //TRANSACCIONES
        tabview = createTabView(mTabHost, "Transacciones", viewGroup);
        mTabHost.addTab(createTabSpec(TAB_1_TRANSACTIONS_MENU_TAG, tabview), TransactionsMenuViewContainerFragment.class, null);
        //TARGETA PRESENTE
        tabview = createTabView(mTabHost, "Tarjeta", viewGroup);
        mTabHost.addTab(createTabSpec(TAB_2_PRESENTE_CARD_MENU_TAG, tabview), PresenteCardMenuViewContainerFragment.class, null);
        //MIS MENSAJES
        tabview = createTabView(mTabHost, "Mensajes", viewGroup);
        mTabHost.addTab(createTabSpec(TAB_3_INBOX_TAG, tabview), InboxViewContainerFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(TAB_4_SAVINGS_SOLICITY_TAG)).setIndicator("SA"), SavingsSolicityViewContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(TAB_5_INBOX_READ_MESSAGE_TAG)).setIndicator("LM"), InboxReadMessageViewContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(TAB_6_STATUS_ACCOUNT_PRODUCTS_TAG)).setIndicator("VP"), StatusAccountProductsViewContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(TAB_7_STATUS_ACCOUNT_MOVEMENTS_PRODUCT_TAG)).setIndicator("VM"), StatusAccountMovementsProductsViewContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(TAB_8_PAYMENTS_CREDITS_TAG)).setIndicator("AC"), PaymentCreditsViewContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(TAB_9_SOLICITY_RESORTS_TAG)).setIndicator("CV"), ResortsSolicityViewContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(TAB_10_PRESENTE_CARD_ACTIVE_TAG)).setIndicator("AT"), PresenteCardActiveViewContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(TAB_11_PRESENTE_CARD_BLOCK_TAG)).setIndicator("BT"), PresenteCardBlockViewContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(TAB_12_PRESENTE_CARD_MOVEMENTS_PRODUCT_TAG)).setIndicator("BT"), PresenteCardMovementesProductsViewContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(TAB_13_PRESENTE_CARD_PRODUCTS_TAG)).setIndicator("TP"), PresenteCardProductsViewContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(TAB_14_PRESENTE_CARD_SECURITY_MENU_TAG)).setIndicator("TS"), PresenteCardSecurityViewContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(TAB_15_TRANSFERS_REGISTER_ACCOUNT_TAG)).setIndicator("IC"), TransfersRegisterAccountViewContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(TAB_16_TRANSFERS_DELETE_ACCOUNT_TAG)).setIndicator("BC"), TransfersDeleteAccountContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(TAB_17_TRANSFERS_MENU_TAG)).setIndicator("TR"), TransfersMenuViewContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(TAB_18_TRANSFERS_MAKE_TRANSFER_TAG)).setIndicator("RT"), TransfersMakeViewContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(TAB_19_ADVANCE_SALARY_SOLICITY_TAG)).setIndicator("AN"), SalaryAdvanceSolicityViewContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(TAB_20_ADVANCE_SALARY_DETAIL_TAG)).setIndicator("AD"), SalaryAdvanceDetailViewContainerFragement.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(TAB_21_PRESENTE_CARD_REPLACEMENT_TAG)).setIndicator("RT"), PresenteCardReplacementViewContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(TAB_22_PRESENTE_CARD_REPLACEMENTDETAIL_TAG)).setIndicator("RD"), PresenteCardReplacementeDetailViewContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(TAB_23_NEQUI_MENU_SEND_MONEY_TAG)).setIndicator("ED"), NequiMenuSendMoneyContainerFragment.class,null);
        mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(TAB_24_NEQUI_DISPERSIONES_TAG)).setIndicator("EDN"), NequiDispersionesContainerFragment.class,null);
        mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(TAB_25_NEQUI_SUSCRIPTIONS_PAYMENT_TAG)).setIndicator("EFN"), NequiSuscriptionsPaymentContainerFragment.class,null);
        mTabHost.setCurrentTab(tabIndex);

        if(tabIndex == TAB_4_SAVINGS_SOLICITY_TAG ||
                tabIndex == TAB_9_SOLICITY_RESORTS_TAG ||
                tabIndex== TAB_23_NEQUI_MENU_SEND_MONEY_TAG){
            mTabHost.getTabWidget().getChildAt(ITEM_TAB_INDEX_TRANSACTIONS).setBackgroundColor(Color.parseColor("#333333"));
            ViewGroup view = (ViewGroup) mTabHost.getTabWidget().getChildAt(ITEM_TAB_INDEX_TRANSACTIONS);
            TextView tv = (TextView) view.getChildAt(1);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
        }else{
            mTabHost.getTabWidget().getChildAt(tabIndex).setBackgroundColor(Color.parseColor("#333333"));
            ViewGroup view = (ViewGroup) mTabHost.getTabWidget().getChildAt(tabIndex);
            TextView tv = (TextView) view.getChildAt(1);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
        }

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                try{
                    tabIndex = validateTabSelected(Integer.parseInt(tabId));
                }catch(Exception ex){
                    tabIndex = validateTabSelected(0);
                }
                //CAMBIAR FONDO DEL TAB SELECCIONADO
                mTabHost.getTabWidget().getChildAt(ITEM_TAB_INDEX_STATUSACCOUNT).setBackgroundColor(Color.parseColor("#ffffff"));
                mTabHost.getTabWidget().getChildAt(ITEM_TAB_INDEX_TRANSACTIONS).setBackgroundColor(Color.parseColor("#ffffff"));
                mTabHost.getTabWidget().getChildAt(ITEM_TAB_INDEX_PRESENTE_CARD).setBackgroundColor(Color.parseColor("#ffffff"));
                mTabHost.getTabWidget().getChildAt(ITEM_TAB_INDEX_INBOX).setBackgroundColor(Color.parseColor("#ffffff"));

                for(int i=0;i<mTabHost.getTabWidget().getChildCount();i++)
                {
                    ViewGroup view = (ViewGroup) mTabHost.getTabWidget().getChildAt(i);
                    TextView tv = (TextView) view.getChildAt(1);
                    tv.setTextColor(Color.parseColor("#000000"));
                }

                mTabHost.getTabWidget().getChildAt(tabIndex).setBackgroundColor(Color.parseColor("#333333"));
                ViewGroup viewGroup = (ViewGroup) mTabHost.getTabWidget().getChildAt(tabIndex);
                TextView tv = (TextView) viewGroup.getChildAt(1);
                tv.setTextColor(Color.parseColor("#FFFFFF"));

                state.setFechaSeleccionada(null);
                View view = context.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    if(imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
//            View tab = mTabHost.getTabWidget().getChildAt(i);
//            final TextView tv = tab.getRootView().findViewById(android.R.id.title);
            ViewGroup tab = (ViewGroup) mTabHost.getTabWidget().getChildAt(i);
            final TextView tv = (TextView) tab.getChildAt(1);
            if (tv != null)
                tv.setTextSize(10);
            if(i>3){
                tab.setVisibility(View.GONE);
            }
        }
        state.setmTabHost(mTabHost);
        configureSideBar();
    }

    @Override
    public void onStart(){
        super.onStart();
        if(state != null && state.getDatosSuscripcion() != null) {
            buttonVinculaCuentaNequi.setVisibility(View.GONE);
        }else{
            if(state != null && state.isActiveStateSuscriptions()){
                validateSuscriptionNequi();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(state!= null && !state.validarEstado()){
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        boolean isPopFragment = false;
        String tabTag = mTabHost.getCurrentTabTag();
        int currentTabTag = 0;
        try{
            currentTabTag = Integer.parseInt(tabTag);
        }catch(Exception ex){}

        switch(currentTabTag){
            case TAB_0_STATUS_ACCOUNT_TAG:
                isPopFragment = ((BaseContainerFragment)getSupportFragmentManager().findFragmentByTag(String.valueOf(TAB_0_STATUS_ACCOUNT_TAG))).popFragment();
                break;
            case TAB_1_TRANSACTIONS_MENU_TAG:
                isPopFragment = ((BaseContainerFragment)getSupportFragmentManager().findFragmentByTag(String.valueOf(TAB_1_TRANSACTIONS_MENU_TAG))).popFragment();
                break;
            case TAB_2_PRESENTE_CARD_MENU_TAG:
                isPopFragment = ((BaseContainerFragment)getSupportFragmentManager().findFragmentByTag(String.valueOf(TAB_2_PRESENTE_CARD_MENU_TAG))).popFragment();
                break;
            case TAB_3_INBOX_TAG:
                isPopFragment = ((BaseContainerFragment)getSupportFragmentManager().findFragmentByTag(String.valueOf(TAB_3_INBOX_TAG))).popFragment();
                break;
            case TAB_4_SAVINGS_SOLICITY_TAG:
                if(state.getFragmentActual().equals(Pantalla.MenuPrincipal)){
                    state.setFechaSeleccionada(null);
                    finish();
                }else {
                    state.getmTabHost().setCurrentTab(TAB_1_TRANSACTIONS_MENU_TAG);
                    state.setFechaSeleccionada(null);
                }
                return;
            case TAB_5_INBOX_READ_MESSAGE_TAG:
                state.getmTabHost().setCurrentTab(TAB_3_INBOX_TAG);
                state.setMensajesBuzonSeleccionado(null);
                return;
            case TAB_6_STATUS_ACCOUNT_PRODUCTS_TAG:
                state.setProductosDetalle(null);
                state.getmTabHost().setCurrentTab(TAB_0_STATUS_ACCOUNT_TAG);
                return;
            case TAB_7_STATUS_ACCOUNT_MOVEMENTS_PRODUCT_TAG:
                state.getmTabHost().setCurrentTab(TAB_6_STATUS_ACCOUNT_PRODUCTS_TAG);
                state.setProductoSeleccionado(null);
                return;
            case TAB_8_PAYMENTS_CREDITS_TAG:
            case TAB_17_TRANSFERS_MENU_TAG:
            case TAB_9_SOLICITY_RESORTS_TAG:
                state.getmTabHost().setCurrentTab(TAB_1_TRANSACTIONS_MENU_TAG);
                return;
            case TAB_10_PRESENTE_CARD_ACTIVE_TAG:
            case TAB_11_PRESENTE_CARD_BLOCK_TAG:
                state.getmTabHost().setCurrentTab(TAB_14_PRESENTE_CARD_SECURITY_MENU_TAG);
                return;
            case TAB_12_PRESENTE_CARD_MOVEMENTS_PRODUCT_TAG:
                state.setProductoSeleccionado(null);
                state.getmTabHost().setCurrentTab(TAB_13_PRESENTE_CARD_PRODUCTS_TAG);
                return;
            case TAB_13_PRESENTE_CARD_PRODUCTS_TAG:
            case TAB_14_PRESENTE_CARD_SECURITY_MENU_TAG:
                state.setProductoSeleccionado(null);
                state.getmTabHost().setCurrentTab(TAB_2_PRESENTE_CARD_MENU_TAG);
                return;
            case TAB_21_PRESENTE_CARD_REPLACEMENT_TAG:
                state.getmTabHost().setCurrentTab(TAB_2_PRESENTE_CARD_MENU_TAG);
                return;
            case TAB_15_TRANSFERS_REGISTER_ACCOUNT_TAG:
            case TAB_16_TRANSFERS_DELETE_ACCOUNT_TAG:
            case TAB_18_TRANSFERS_MAKE_TRANSFER_TAG:
            case TAB_24_NEQUI_DISPERSIONES_TAG:
            case TAB_25_NEQUI_SUSCRIPTIONS_PAYMENT_TAG:
                state.getmTabHost().setCurrentTab(TAB_23_NEQUI_MENU_SEND_MONEY_TAG);
                return;
            case TAB_19_ADVANCE_SALARY_SOLICITY_TAG:
                state.setRequisitos(null);
                state.getmTabHost().setCurrentTab(TAB_1_TRANSACTIONS_MENU_TAG);
                return;
            case TAB_20_ADVANCE_SALARY_DETAIL_TAG:
                state.getmTabHost().setCurrentTab(TAB_19_ADVANCE_SALARY_SOLICITY_TAG);
                return;
            case TAB_22_PRESENTE_CARD_REPLACEMENTDETAIL_TAG:
                state.getmTabHost().setCurrentTab(TAB_21_PRESENTE_CARD_REPLACEMENT_TAG);
                return;
            case TAB_23_NEQUI_MENU_SEND_MONEY_TAG:
                if(state.getFragmentActual().equals(Pantalla.MenuPrincipal)){
                    finish();
                }else {
                    state.getmTabHost().setCurrentTab(TAB_1_TRANSACTIONS_MENU_TAG);
                }
                return;
            default:
                state.setProductosDetalle(null);
                state.getmTabHost().setCurrentTab(TAB_0_STATUS_ACCOUNT_TAG);
                return;
        }
        if (!isPopFragment) {
            state.setmTabHost(null);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GlobalState.NEQUI) {
            if(resultCode == GlobalState.CLOSED_SESSION){
                setFragment(IFragmentCoordinator.Pantalla.Ingreso);
            }else{
                setFragment(IFragmentCoordinator.Pantalla.MenuPrincipal);
            }
        }
    }

    @OnClick(R.id.btnPortafolio)
    public void onClickPortafolio(View v) {
        Intent intent_p = new Intent(this, ActivityPortfolioProductsView.class);
        startActivity(intent_p);
    }
    @OnClick(R.id.btnPreguntasFrecuentes)
    public void onClickPreguntasFrecuentes(View v) {
        Intent intent_s = new Intent(this, ActivityServicesView.class);
        startActivity(intent_s);
    }

    @OnClick(R.id.btnDirectorio)
    public void onClickDirectorio(View v) {
        Intent intent_dir = new Intent(this, ActivityDirectoryView.class);
        startActivity(intent_dir);
    }

    @OnClick(R.id.btnEncuentranos)
    public void onClickEncuentranos(View v) {
        Intent intent;
        if(state != null && state.isHmsSystem()){
            intent = new Intent(this, ActivityLocationsHmsView.class);
        }else{
            intent = new Intent(this, ActivityLocationsGmsView.class);
        }
        startActivity(intent);
    }

    @OnClick(R.id.btn_back)
    public void onClickBack(View v) {
        onBackPressed();
    }

    @OnClick(R.id.toolbarSideBar)
    public void onClickToolbar(){
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            drawerLayout.openDrawer(Gravity.RIGHT);
        }
    }

    @OnClick(R.id.buttonInformacionPersonal)
    public void onClickInformacionPersonal(){
        drawerLayout.closeDrawer(Gravity.RIGHT);
        Usuario usuario = state.getUsuario();
        Intent intent = new Intent(context, ActivityUpdatePersonalDataView.class);
        intent.putExtra("actualizaPrimeraVez", usuario.getDatosActualizados().getActualizaPrimeraVez());
        intent.putExtra("datosActualizados", usuario.getDatosActualizados().getTieneDatosActualizados());
        startActivity(intent);
    }

    @OnClick(R.id.buttonCentroDeAyuda)
    public void onClickCentroDeAyuda(){
        drawerLayout.closeDrawer(Gravity.RIGHT);
        String urlCentroAyuda = GlobalState.UrlCentroAyuda;
        Uri uri = Uri.parse(urlCentroAyuda);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @OnClick(R.id.buttonConoceTuGestor)
    public void onClickConoceTuGestor(){
        drawerLayout.closeDrawer(Gravity.RIGHT);
        String urlConoceGestor = GlobalState.UrlConoceGestor;
        Uri uri_Gestor = Uri.parse(urlConoceGestor);
        Intent intent_gestor = new Intent(Intent.ACTION_VIEW, uri_Gestor);
        startActivity(intent_gestor);
    }

    @OnClick(R.id.buttonVinculaCuentaNequi)
    public void onClickVinculaCuentaNequi(){
        try{
            String status = state.getStatusSuscription() == null ? "" : state.getStatusSuscription();
            switch (status) {
                case "1":
                    break;
                case "0":
                    showSendingRequet();
                    break;
                default:
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    Intent i = new Intent(context, ActivitySuscriptionView.class);
                    startActivityForResult(i, GlobalState.NEQUI);
                    break;
            }
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "Se ha producido un error, inténtalo nuevamente en unos minutos.");
        }
    }

    @OnClick(R.id.btnCerrarSession)
    public void onClickCerrarSesion(){
        drawerLayout.closeDrawer(Gravity.RIGHT);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_confirm);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView titleMessage = (TextView) dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText("Cerrar sesión");
        TextView contentMessage = (TextView) dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText("¿Estás seguro que quieres cerrar la sesión?");
        ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.buttonClose);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button buttonAceptar = (Button) dialog.findViewById(R.id.btnAceptar);
        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reiniciarEstado();
                finish();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void configureSideBar(){
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.icon_menu, R.drawable.icon_close_black) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                toolbarSideBar.setNavigationIcon(R.drawable.icon_menu);
            }
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                toolbarSideBar.setNavigationIcon(R.drawable.icon_close_black);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        if(state == null || state.getUsuario() == null) {
            toolbarSideBar.setVisibility(View.GONE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            nombreUsuario.setVisibility(View.GONE);
            sectionFechaIngreso.setVisibility(View.GONE);
        }else{
            toolbarSideBar.setVisibility(View.VISIBLE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            nombreUsuario.setVisibility(View.VISIBLE);
            nombreUsuario.setText(state.getUsuario().getNombreAsociado());
            sectionFechaIngreso.setVisibility(View.VISIBLE);
            textFechaIngreso.setText(state.getUsuario().getFechaUltimoIngreso());
            textFechaIngreso.setVisibility(View.VISIBLE);
        }
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
        if(datosSuscripcion != null){
            buttonVinculaCuentaNequi.setVisibility(View.GONE);
        }else{
            buttonVinculaCuentaNequi.setVisibility(View.VISIBLE);
        }

        if(state.getStatusSuscription() != null && state.getStatusSuscription().equals("0")){
            getTimeExpiration();
        }else{
            activateButtonWarning(false);
        }
    }

    @Override
    public void getTimeExpiration(){
        try{
            if(state != null && state.getTiempoExpiracionAutorizacion() != null && state.getTiempoExpiracionAutorizacion() > 0){
                getTimeOfSuscription();
            }else{
                presenter.getTimeExpiration();
            }
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
            activateButtonWarning(true);
            new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    activateButtonWarning(false);
                }
            }, milisecons);
        }else{
            state.setStatusSuscription("");
            activateButtonWarning(false);
        }
    }

    @Override
    public void activateButtonWarning(boolean activate){
        if(activate){
            containerButtonNequi.setMinimumHeight(70);
            warningIcon.setVisibility(View.VISIBLE);
        }else{
            containerButtonNequi.setMinimumHeight(60);
            warningIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public int validateTabSelected(int tab){
        switch (tab) {
            case TAB_1_TRANSACTIONS_MENU_TAG:
            case TAB_4_SAVINGS_SOLICITY_TAG:
            case TAB_8_PAYMENTS_CREDITS_TAG:
            case TAB_9_SOLICITY_RESORTS_TAG:
            case TAB_15_TRANSFERS_REGISTER_ACCOUNT_TAG:
            case TAB_16_TRANSFERS_DELETE_ACCOUNT_TAG:
            case TAB_17_TRANSFERS_MENU_TAG:
            case TAB_18_TRANSFERS_MAKE_TRANSFER_TAG:
            case TAB_19_ADVANCE_SALARY_SOLICITY_TAG:
            case TAB_20_ADVANCE_SALARY_DETAIL_TAG:
            case TAB_23_NEQUI_MENU_SEND_MONEY_TAG:
            case TAB_24_NEQUI_DISPERSIONES_TAG:
            case TAB_25_NEQUI_SUSCRIPTIONS_PAYMENT_TAG:
                return 1;
            case TAB_2_PRESENTE_CARD_MENU_TAG:
            case TAB_13_PRESENTE_CARD_PRODUCTS_TAG:
            case TAB_21_PRESENTE_CARD_REPLACEMENT_TAG:
            case TAB_14_PRESENTE_CARD_SECURITY_MENU_TAG:
            case TAB_10_PRESENTE_CARD_ACTIVE_TAG:
            case TAB_11_PRESENTE_CARD_BLOCK_TAG:
            case TAB_12_PRESENTE_CARD_MOVEMENTS_PRODUCT_TAG:
            case TAB_22_PRESENTE_CARD_REPLACEMENTDETAIL_TAG:
                return 2;
            case TAB_3_INBOX_TAG:
            case TAB_5_INBOX_READ_MESSAGE_TAG:
                return 3;
            default:
                return ITEM_TAB_INDEX_STATUSACCOUNT;
        }
    }

    @Override
    public Drawable createDrawable(String textTab){
        switch (textTab){
            case "Est Cuenta":
                return getResources().getDrawable(R.drawable.tab_state_indicator_estado_cuenta);
            case "Transacciones":
                return getResources().getDrawable(R.drawable.tab_state_indicator_transacciones);
            case "Tarjeta":
                return getResources().getDrawable(R.drawable.tab_state_indicator_tarjeta_pte);
            case "Mensajes":
                return getResources().getDrawable(R.drawable.tab_state_indicator_mensajes);
            case "Encuéntranos":
                return getResources().getDrawable(R.drawable.tab_state_indicator_georeferenciacion);
            default:
                return getResources().getDrawable(R.drawable.tab_state_indicator_estado_cuenta);
        }
    }

    @Override
    public View createTabView(TabHost context, final String text, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context.getContext()).inflate(R.layout.menu_tab, viewGroup, false);
        TextView tv = view.findViewById(R.id.title);
        tv.setText(text);
        ImageView icon = view.findViewById(R.id.icon);
        icon.setImageDrawable(createDrawable(text));
        TextView c = view.findViewById(R.id.cantMessages);
        if(text != null && text.equals("Mensajes")){
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

            if (numberOfUnReadMessages > 0) {
                c.setText((numberOfUnReadMessages > 9 ? String.valueOf(numberOfUnReadMessages):" "+String.valueOf(numberOfUnReadMessages)+" "));
                c.setVisibility(View.VISIBLE);
            }else{
                c.setText("");
                c.setVisibility(View.GONE);
            }
        }
        return view;
    }

    public TabHost.TabSpec createTabSpec(int tabTag, View tabView){
        final TextView view = new TextView(context);
        TabHost.TabSpec tabSpec = mTabHost.newTabSpec(String.valueOf(tabTag)).setIndicator(tabView);
        tabSpec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return view;
            }
        });
        return tabSpec;
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
        final Dialog dialog = new Dialog(this);
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
        final Dialog dialog = new Dialog(this);
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
        final Dialog dialog = new Dialog(this);
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
    public void showSendingRequet() {
        Dialog dialogSendingRequest= new Dialog(context);
        dialogSendingRequest.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSendingRequest.setCanceledOnTouchOutside(false);
        dialogSendingRequest.setContentView(R.layout.pop_up_closedsession);
        dialogSendingRequest.setCancelable(false);
        dialogSendingRequest.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button buttonClosedSession = dialogSendingRequest.findViewById(R.id.btncerrarEnviandoSolicitud);
        buttonClosedSession.setOnClickListener(view -> {
            dialogSendingRequest.dismiss();
            context.salir();
        });
        dialogSendingRequest.show();
    }

}
