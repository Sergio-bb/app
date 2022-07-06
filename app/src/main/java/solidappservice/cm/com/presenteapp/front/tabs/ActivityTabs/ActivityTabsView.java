package solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.fragment.app.FragmentTabHost;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.front.base.ActivityMainView;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityDirectory.ActivityDirectoryView;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityGeoreferencing.ActivityLocationsGms.ActivityLocationsGmsView;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityGeoreferencing.ActivityLocationsHms.ActivityLocationsHmsView;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityPortfolio.ActivityPortfolioProducts.ActivityPortfolioProductsView;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityServices.ActivityServicesView;
import solidappservice.cm.com.presenteapp.tools.tabsbasecontainers.PaymentCreditsViewContainerFragment;
import solidappservice.cm.com.presenteapp.tools.tabsbasecontainers.PresenteCardActiveViewContainerFragment;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.tools.tabsbasecontainers.TransfersRegisterAccountViewContainerFragment;
import solidappservice.cm.com.presenteapp.tools.tabsbasecontainers.SalaryAdvanceSolicityViewContainerFragment;
import solidappservice.cm.com.presenteapp.tools.tabsbasecontainers.SalaryAdvanceDetailViewContainerFragement;
import solidappservice.cm.com.presenteapp.tools.tabsbasecontainers.BaseContainerFragment;
import solidappservice.cm.com.presenteapp.tools.tabsbasecontainers.PresenteCardBlockViewContainerFragment;
import solidappservice.cm.com.presenteapp.tools.tabsbasecontainers.InboxViewContainerFragment;
import solidappservice.cm.com.presenteapp.tools.tabsbasecontainers.TransfersDeleteAccountContainerFragment;
import solidappservice.cm.com.presenteapp.tools.tabsbasecontainers.StatusAccountProductsViewContainerFragment;
import solidappservice.cm.com.presenteapp.tools.tabsbasecontainers.StatusAccountViewContainerFragment;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.tabsbasecontainers.InboxReadMessageViewContainerFragment;
import solidappservice.cm.com.presenteapp.tools.tabsbasecontainers.TransfersMenuViewContainerFragment;
import solidappservice.cm.com.presenteapp.tools.tabsbasecontainers.StatusAccountMovementsProductsViewContainerFragment;
import solidappservice.cm.com.presenteapp.tools.tabsbasecontainers.PresenteCardMovementesProductsViewContainerFragment;
import solidappservice.cm.com.presenteapp.tools.tabsbasecontainers.TransfersMakeViewContainerFragment;
import solidappservice.cm.com.presenteapp.tools.tabsbasecontainers.PresenteCardReplacementViewContainerFragment;
import solidappservice.cm.com.presenteapp.tools.tabsbasecontainers.PresenteCardReplacementeDetailViewContainerFragment;
import solidappservice.cm.com.presenteapp.tools.tabsbasecontainers.SavingsSolicityViewContainerFragment;
import solidappservice.cm.com.presenteapp.tools.tabsbasecontainers.ResortsSolicityViewContainerFragment;
import solidappservice.cm.com.presenteapp.tools.tabsbasecontainers.PresenteCardMenuViewContainerFragment;
import solidappservice.cm.com.presenteapp.tools.tabsbasecontainers.PresenteCardProductsViewContainerFragment;
import solidappservice.cm.com.presenteapp.tools.tabsbasecontainers.PresenteCardSecurityViewContainerFragment;
import solidappservice.cm.com.presenteapp.tools.tabsbasecontainers.TransactionsMenuViewContainerFragment;

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

    public static final int ITEM_TAB_INDEX_STATUSACCOUNT = 0;
    public static final int ITEM_TAB_INDEX_TRANSACTIONS = 1;
    public static final int ITEM_TAB_INDEX_PRESENTE_CARD = 2;
    public static final int ITEM_TAB_INDEX_INBOX = 3;

    private FragmentTabHost mTabHost;
    private int tabIndex;
    private ViewGroup viewGroup;

    @BindView(R.id.btnPortafolio)
    ImageButton btnPortafolio;
    @BindView(R.id.btnPreguntasFrecuentes)
    ImageButton btnPreguntasFrecuentes;
    @BindView(R.id.btnDirectorio)
    ImageButton btnDirectorio;
    @BindView(R.id.btnEncuentranos)
    ImageButton btnEncuentranos;
    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.btnSalir)
    TextView btnSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewGroup = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_tabs, null);
        setContentView(R.layout.activity_tabs);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            tabIndex = bundle.getInt("TabIndex", 0);
        }
        ButterKnife.bind(this);
        setControls();
    }

    @Override
    protected void setControls() {
        presenter = new ActivityTabsPresenter(this);
        context = this;
        state = (GlobalState) getApplicationContext();
        mTabHost = findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        Resources res = getResources();

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
        mTabHost.setCurrentTab(tabIndex);

        if(tabIndex == TAB_4_SAVINGS_SOLICITY_TAG ||
                tabIndex == TAB_9_SOLICITY_RESORTS_TAG){
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(state!= null && !state.validarEstado()){
            Intent intent = new Intent(this, ActivityMainView.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.btnPortafolio)
    public void onClickPortafolio(View v) {
        Intent intent_p = new Intent(this, ActivityPortfolioProductsView.class);
        startActivity(intent_p);
    }
    @OnClick(R.id.btnPreguntasFrecuentes)
    public void onClickPreguntasFrecuentes(View v) {
//        String url = "https://www.presente.com.co/contactenos";
//        Intent intent_pr = new Intent(Intent.ACTION_VIEW);
//        intent_pr.setData(Uri.parse(url));
//        startActivity(intent_pr);
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
        if(state != null && state.isHmsSystem()){
            Intent intent = new Intent(this, ActivityLocationsHmsView.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, ActivityLocationsGmsView.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.btn_back)
    public void onClickBack(View v) {
        onBackPressed();
    }

    @OnClick(R.id.btnSalir)
    public void onClickSalir(View v) {
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle(getResources().getString(R.string.app_name));
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage("¿Desea cerrar sesión?");
        d.setCancelable(false);
        d.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        context.reiniciarEstado();
                        state.setmTabHost(null);
                        finish();
                    }
                });
        d.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        d.show();
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
            int ca = state.getUnreadMessagesCount();
            if(ca > 0){
                c.setText((ca > 9 ? String.valueOf(ca):" "+String.valueOf(ca)+" "));
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

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Let it be
    }

}
