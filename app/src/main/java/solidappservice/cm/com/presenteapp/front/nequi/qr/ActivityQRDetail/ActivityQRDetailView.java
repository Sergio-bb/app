package solidappservice.cm.com.presenteapp.front.nequi.qr.ActivityQRDetail;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
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

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.login.Response.Usuario;
import solidappservice.cm.com.presenteapp.entities.nequi.dto.SuscriptionData;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.actualizaciondatos.ActivityUpdatePersonalData.ActivityUpdatePersonalDataView;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.login.FragmentLogin.FragmentLoginView;
import solidappservice.cm.com.presenteapp.front.nequi.qr.FragmentQRDetail.FragmentQRDetailView;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.front.nequi.suscription.ActivitySuscription.ActivitySuscriptionView;
import solidappservice.cm.com.presenteapp.front.terminosycondiciones.ActivityTermsAndConditions.ActivityTermsAndConditionsView;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

public class ActivityQRDetailView extends ActivityBase implements ActivityQRDetailContract.View{

    private ActivityQRDetailPresenter presenter;
    private GlobalState state;
    private ActivityQRDetailView context;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @BindView(R.id.btn_back)
    ImageButton btn_back;

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
        setContentView(R.layout.activity_nequi_qr_detail);
        ButterKnife.bind(this);
        setControls();
        FirebaseMessaging.getInstance().subscribeToTopic("general");
    }

    @Override
    protected void setControls() {
        presenter = new ActivityQRDetailPresenter(this, new ActivityQRDetailModel());
        context = this;
        state = getState();
        configureSideBar();
        showFragmentTransferQR(Pantalla.NequiQRDetail);
        if(state != null && state.getDatosSuscripcion() != null) {
            buttonVinculaCuentaNequi.setVisibility(View.GONE);
        }else{
            if(state != null && state.isActiveStateSuscriptions()){
                validateSuscriptionNequi();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
                setResult(GlobalState.CLOSED_SESSION);
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
    public void showFragmentTransferQR(Pantalla pantalla) {
        GlobalState state = getState();
        Fragment fragment;
        switch (pantalla) {
            case NequiQRDetail:
                fragment = new FragmentQRDetailView();
                break;
            default:
                fragment = new FragmentLoginView();
                break;
        }
        if (state != null && state.getFragmentActual() != null) {
            state.setFragmentAnterior(state.getFragmentActual());
        }
        hideKeyBoard();
        state.setFragmentActual(pantalla);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frameQr, fragment).commit();
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

