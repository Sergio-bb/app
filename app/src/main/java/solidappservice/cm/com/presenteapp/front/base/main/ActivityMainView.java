package solidappservice.cm.com.presenteapp.front.base.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.entities.login.Response.Usuario;
import solidappservice.cm.com.presenteapp.entities.nequi.dto.SuscriptionData;
import solidappservice.cm.com.presenteapp.front.actualizaciondatos.ActivityUpdatePersonalData.ActivityUpdatePersonalDataView;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityDirectory.ActivityDirectoryView;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityGeoreferencing.ActivityLocationsGms.ActivityLocationsGmsView;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityGeoreferencing.ActivityLocationsHms.ActivityLocationsHmsView;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityPortfolio.ActivityPortfolioProducts.ActivityPortfolioProductsView;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityServices.ActivityServicesView;
import solidappservice.cm.com.presenteapp.front.login.ActivityValidateCode.ActivityValidateCodeView;
import solidappservice.cm.com.presenteapp.front.login.FragmentForgotPassword.FragmentForgotPasswordView;
import solidappservice.cm.com.presenteapp.front.login.FragmentLogin.FragmentLoginView;
import solidappservice.cm.com.presenteapp.front.menufinanzas.FragmentFinanceMenu.FragmentFinanceMenuView;
import solidappservice.cm.com.presenteapp.front.menuprincipal.FragmentHome.FragmentHomeView;
import solidappservice.cm.com.presenteapp.front.nequi.suscription.ActivitySuscription.ActivitySuscriptionView;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.front.terminosycondiciones.ActivityTermsAndConditions.ActivityTermsAndConditionsView;
import solidappservice.cm.com.presenteapp.front.tutorial.ActivityTutorial.ActivityTutorialView;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;
import solidappservice.cm.com.presenteapp.tools.helpers.DialogHelpers;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 23/11/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 22/09/2021
 */

public class ActivityMainView extends ActivityBase implements ActivityMainContract.View{

    private ActivityMainPresenter presenter;
    private GlobalState state;
    private ActivityBase context;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private int tabIndex;
    public MenuItem btnNequi;
    public TextView nombre_usuario;

    @BindView(R.id.header)
    public ImageView header;
    @BindView(R.id.btn_back)
    public ImageButton btn_back;
    @BindView(R.id.footer)
    public LinearLayout footer;

    @BindView(R.id.btnPortafolio)
    ImageButton btnPortafolio;
    @BindView(R.id.btnPreguntasFrecuentes)
    ImageButton btnPreguntasFrecuentes;
    @BindView(R.id.btnDirectorio)
    ImageButton btnDirectorio;
    @BindView(R.id.btnEncuentranos)
    ImageButton btnEncuentranos;

    @BindView(R.id.toolbarSideBar)
    Toolbar toolbarSideBar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.nombreUsuario)
    TextView nombreUsuario;

    @BindView(R.id.layout_buton_vincula)
    LinearLayout containerButtonNequi;
    @BindView(R.id.warning_icon)
    RelativeLayout warningIcon;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setControls();
        FirebaseMessaging.getInstance().subscribeToTopic("general");
    }

    @Override
    protected void setControls() {
        context = this;
        if (!NetworkHelper.isConnectionAvailable(this)) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.pop_up_network_not_availabe);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.buttonClose);
            buttonClose.setOnClickListener(view -> {
                reiniciarEstado();
                finish();
                dialog.dismiss();
            });
            Button buttonAceptar = (Button) dialog.findViewById(R.id.btnAceptar);
            buttonAceptar.setOnClickListener(view -> {
                reiniciarEstado();
                finish();
                dialog.dismiss();
            });
            dialog.show();
        }
        presenter = new ActivityMainPresenter(this, new ActivityMainModel());
        state = getState();
        configureSideBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        state = getState();
        if(state != null && !state.validarEstado()){
            setFragment(Pantalla.Ingreso);
        }
        if(!obtenerConfiguracionTutorial()){
            Intent i = new Intent(this, ActivityTutorialView.class);
            startActivityForResult(i, GlobalState.TUTORIAL);
        }
        if(state != null && state.getDatosSuscripcion() != null){
            buttonVinculaCuentaNequi.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GlobalState.TERMS_AND_CONDITIONS){
            if(resultCode != RESULT_OK){
                reiniciarEstado();
                setFragment(IFragmentCoordinator.Pantalla.Ingreso);
            } else {
                if(state.getFragmentActual() == Pantalla.MenuPrincipal){
                    Usuario usuario = state.getUsuario();
                    if(usuario != null &&
                            (usuario.getDatosActualizados().getActualizaPrimeraVez().equals("Y") ||
                                    usuario.getDatosActualizados().getTieneDatosActualizados().equals("N"))){
                        Intent i = new Intent(this, ActivityUpdatePersonalDataView.class);
                        i.putExtra("actualizaPrimeraVez", usuario.getDatosActualizados().getActualizaPrimeraVez());
                        i.putExtra("datosActualizados", usuario.getDatosActualizados().getTieneDatosActualizados());
                        startActivityForResult(i, GlobalState.UPDATE_PERSONAL_DATA);
                    } else {
                        setFragment(IFragmentCoordinator.Pantalla.MenuPrincipal);
                    }
                }
            }
        }
        if(requestCode == GlobalState.TUTORIAL) {
            guardarConfiguracionTutorial(resultCode == RESULT_OK);
        }
        if(requestCode == GlobalState.TABS){
            showScreenHome();
        }
        if(requestCode == GlobalState.UPDATE_PERSONAL_DATA) {
            setFragment(IFragmentCoordinator.Pantalla.MenuPrincipal);
        }
        if(requestCode == GlobalState.VALIDATE_CODE){
            if (resultCode != Activity.RESULT_OK) {
                context.reiniciarEstado();
            }else{
                Usuario usuario = state.getUsuario();
                setFragment(IFragmentCoordinator.Pantalla.MenuPrincipal);
                if(usuario.getAceptoUltimosTyC().equals("N")){
                    Intent i = new Intent(context, ActivityTermsAndConditionsView.class);
                    startActivityForResult(i, GlobalState.TERMS_AND_CONDITIONS);
                    return;
                } else if(usuario != null && usuario.getDatosActualizados().equals("N")){
                    Intent i = new Intent(context, ActivityUpdatePersonalDataView.class);
                    i.putExtra("actualizaPrimeraVez", usuario.getDatosActualizados().getActualizaPrimeraVez());
                    i.putExtra("datosActualizados", usuario.getDatosActualizados().getTieneDatosActualizados());
                    startActivityForResult(i, GlobalState.UPDATE_PERSONAL_DATA);
                    return;
                } else {
                    setFragment(IFragmentCoordinator.Pantalla.MenuPrincipal);
                }
            }
        }
        if(requestCode == GlobalState.NEQUI || requestCode == GlobalState.ACTIVITY_AGREEMENTS || requestCode == GlobalState.QR) {
            if(resultCode == GlobalState.CLOSED_SESSION){
                setFragment(IFragmentCoordinator.Pantalla.Ingreso);
            }else{
                setFragment(IFragmentCoordinator.Pantalla.MenuPrincipal);
            }
        }
    }

    @OnClick(R.id.btn_back)
    public void onClickBack(View v) {
        onBackPressed();
    }

    @OnClick(R.id.btnPortafolio)
    public void onClickPortafolio(View v) {
        Intent intent_p = new Intent(context, ActivityPortfolioProductsView.class);
        startActivity(intent_p);
    }

    @OnClick(R.id.btnPreguntasFrecuentes)
    public void onClickPreguntasFrecuentes(View v) {
        Intent intent_s = new Intent(context, ActivityServicesView.class);
        startActivity(intent_s);
    }

    @OnClick(R.id.btnDirectorio)
    public void onClickDirectorio(View v) {
        Intent intent_dir = new Intent(context, ActivityDirectoryView.class);
        startActivity(intent_dir);
    }

    @OnClick(R.id.btnEncuentranos)
    public void onClickEncuentranos(View v) {
        Intent intent;
        if(state != null && state.isHmsSystem()){
            intent = new Intent(this, ActivityLocationsHmsView.class);
        }else{
            intent = new Intent(context, ActivityLocationsGmsView.class);
        }
        startActivity(intent);
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
        startActivityForResult(intent, GlobalState.UPDATE_PERSONAL_DATA);
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
        final Dialog dialog = new Dialog(context);
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
                setFragment(IFragmentCoordinator.Pantalla.Ingreso);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @SuppressLint("ResourceType")
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
    public void showDialogValidateCode() {
        if (state.validarEstado()) {
            Intent i = new Intent(this, ActivityValidateCodeView.class);
            startActivityForResult(i, GlobalState.VALIDATE_CODE);
        }
    }

    @Override
    public void showScreenHome() {
        if (state.validarEstado()) {
            setFragment(Pantalla.MenuPrincipal);
        }
    }

    @Override
    public void showScreenForgotPassword() {
        if (state.validarEstado()) {
            setFragment(Pantalla.RecuperarContrasena);
        }
    }

    @Override
    public void showScreenFinanceMenu() {
        if (state.validarEstado()) {
            setFragment(Pantalla.MenuFinanzas);
        }
    }

    @Override
    public void showScreenStatusAccount() {
        if (state.validarEstado()) {
            tabIndex = ActivityTabsView.TAB_0_STATUS_ACCOUNT_TAG;
            setFragment(Pantalla.Tabs);
        }
    }

    @Override
    public void showScreenTransactionsMenu() {
        if (state.validarEstado()) {
            tabIndex = ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG;
            setFragment(Pantalla.Tabs);
        }
    }

    @Override
    public void showScreenPresenteCard() {
        if (state.validarEstado()) {
            tabIndex = ActivityTabsView.TAB_2_PRESENTE_CARD_MENU_TAG;
            setFragment(Pantalla.Tabs);
        }
    }

    @Override
    public void showScreenInbox() {
        if (state.validarEstado()) {
            tabIndex = ActivityTabsView.TAB_3_INBOX_TAG;
            setFragment(Pantalla.Tabs);
        }
    }

    @Override
    public void setFragment(IFragmentCoordinator.Pantalla pantalla) {
        Fragment fragment = null;

        switch (pantalla) {
            case RecuperarContrasena:
                fragment = new FragmentForgotPasswordView();
                break;
            case Ingreso:
                fragment = new FragmentLoginView();
                break;
            case MenuPrincipal:
                fragment = new FragmentHomeView();
                break;
            case MenuFinanzas:
                fragment = new FragmentFinanceMenuView();
                break;
            case Tabs:
                Intent intent = new Intent(this, ActivityTabsView.class);
                intent.putExtra("TabIndex", tabIndex);
                startActivityForResult(intent, GlobalState.TABS);
                return;
            default:
                pantalla = Pantalla.Ingreso;
                fragment = new FragmentLoginView();
                break;
        }

        if (state != null && state.getFragmentActual() != null) {
            state.setFragmentAnterior(state.getFragmentActual());
        }

        hideKeyBoard();
        state.setFragmentActual(pantalla);

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

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.content_frame, fragment).commit();

    }

    @Override
    public void showDataFetchError(String title, String message){
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
