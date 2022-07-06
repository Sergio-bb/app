package solidappservice.cm.com.presenteapp.front.base;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.login.Response.Usuario;
import solidappservice.cm.com.presenteapp.front.login.ActivityValidateCode.ActivityValidateCodeView;
import solidappservice.cm.com.presenteapp.front.login.FragmentForgotPassword.FragmentForgotPasswordView;
import solidappservice.cm.com.presenteapp.front.terminosycondiciones.ActivityTermsAndConditions.ActivityTermsAndConditionsView;
import solidappservice.cm.com.presenteapp.front.tutorial.ActivityTutorial.ActivityTutorialView;
import solidappservice.cm.com.presenteapp.front.menufinanzas.FragmentFinanceMenu.FragmentFinanceMenuView;
import solidappservice.cm.com.presenteapp.front.actualizaciondatos.ActivityUpdatePersonalData.ActivityUpdatePersonalDataView;
import solidappservice.cm.com.presenteapp.front.login.FragmentLogin.FragmentLoginView;
import solidappservice.cm.com.presenteapp.front.menuprincipal.FragmentHome.FragmentHomeView;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 23/11/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 22/09/2021
 */

public class ActivityMainView extends ActivityBase{

    private GlobalState state;
    private ActivityBase context;
    private int tabIndex;

    @BindView(R.id.header)
    public ImageView header;
    @BindView(R.id.btn_back)
    public ImageButton btn_back;
    @BindView(R.id.btnSalir)
    public TextView btnSalir;

    public static final int TERMS_AND_CONDITIONS = 902;
    public static final int TUTORIAL = 903;
    public static final int UPDATE_PERSONAL_DATA = 904;
    public static final int VALIDATE_CODE = 905;

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
        if (!NetworkHelper.isConnectionAvailable(this)) {
            Builder d = new Builder(this);
            d.setTitle(getResources().getString(R.string.app_name));
            d.setIcon(R.mipmap.icon_presente);
            d.setMessage("Para hacer uso de la aplicación debe poseer conexión a Internet");
            d.setCancelable(false);
            d.setPositiveButton("Aceptar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            reiniciarEstado();
                            finish();
                        }
                    });
            d.show();
        }
        context = this;
        state = getState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        state = getState();
        if(state != null && !state.validarEstado()){
            setFragment(Pantalla.Ingreso);
        }else if (state != null && state.getFragmentActual() != null) {
            setFragment(state.getFragmentActual());
        }else{
            setFragment(Pantalla.Ingreso);
        }
        if(!obtenerConfiguracionTutorial()){
            Intent i = new Intent(this, ActivityTutorialView.class);
            startActivityForResult(i, TUTORIAL);
        }
    }

    @OnClick(R.id.btnSalir)
    public void onClickSalir(View v) {
        onBackPressed();
    }

    @OnClick(R.id.btn_back)
    public void onClickBack(View v) {
        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TERMS_AND_CONDITIONS){
            if(resultCode != RESULT_OK){
                reiniciarEstado();
                setFragment(IFragmentCoordinator.Pantalla.Ingreso);
//                finish();
            } else {
                if(state.getFragmentActual() == Pantalla.MenuPrincipal){
                    Usuario usuario = state.getUsuario();
                    if(usuario != null &&
                            (usuario.getDatosActualizados().getActualizaPrimeraVez().equals("Y") ||
                                    usuario.getDatosActualizados().getTieneDatosActualizados().equals("N"))){
                        Intent i = new Intent(this, ActivityUpdatePersonalDataView.class);
                        i.putExtra("actualizaPrimeraVez", usuario.getDatosActualizados().getActualizaPrimeraVez());
                        i.putExtra("datosActualizados", usuario.getDatosActualizados().getTieneDatosActualizados());
                        startActivityForResult(i, UPDATE_PERSONAL_DATA);
                    } else {
                        setFragment(IFragmentCoordinator.Pantalla.MenuPrincipal);
                    }
                }
            }
        }
        if(requestCode == TUTORIAL) {
            guardarConfiguracionTutorial(resultCode == RESULT_OK);
        }
        if(requestCode == UPDATE_PERSONAL_DATA) {
           setFragment(IFragmentCoordinator.Pantalla.MenuPrincipal);
        }
        if(requestCode == VALIDATE_CODE){
            if (resultCode != Activity.RESULT_OK) {
                context.reiniciarEstado();
            }else{
                Usuario usuario = state.getUsuario();
                setFragment(IFragmentCoordinator.Pantalla.MenuPrincipal);
                if(usuario.getAceptoUltimosTyC().equals("N")){
                    Intent i = new Intent(context, ActivityTermsAndConditionsView.class);
                    startActivityForResult(i, TERMS_AND_CONDITIONS);
                    return;
                } else if(usuario != null && usuario.getDatosActualizados().equals("N")){
                    Intent i = new Intent(context, ActivityUpdatePersonalDataView.class);
                    i.putExtra("actualizaPrimeraVez", usuario.getDatosActualizados().getActualizaPrimeraVez());
                    i.putExtra("datosActualizados", usuario.getDatosActualizados().getTieneDatosActualizados());
                    startActivityForResult(i, UPDATE_PERSONAL_DATA);
                    return;
                } else {
                    setFragment(IFragmentCoordinator.Pantalla.MenuPrincipal);
                }
            }
        }
    }

    public void showActivityValidateCode() {
        if (state.validarEstado()) {
            Intent i = new Intent(this, ActivityValidateCodeView.class);
            startActivityForResult(i, VALIDATE_CODE);
        }
    }

    @Override
    public void verMenuPrincipal() {
        if (state.validarEstado()) {
            setFragment(Pantalla.MenuPrincipal);
        }
    }

    @Override
    public void verRecuperarClave() {
        if (state.validarEstado()) {
            setFragment(Pantalla.RecuperarContrasena);
        }
    }

    @Override
    public void verMenuFinanzas() {
        if (state.validarEstado()) {
            setFragment(Pantalla.MenuFinanzas);
        }
    }

    @Override
    public void verEstadoCuenta() {
        if (state.validarEstado()) {
            tabIndex = ActivityTabsView.TAB_0_STATUS_ACCOUNT_TAG;
            setFragment(Pantalla.Tabs);
        }
    }

    @Override
    public void verTransacciones() {
        if (state.validarEstado()) {
            tabIndex = ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG;
            setFragment(Pantalla.Tabs);
        }
    }

    @Override
    public void verTarjetaPresente() {
        if (state.validarEstado()) {
            tabIndex = ActivityTabsView.TAB_2_PRESENTE_CARD_MENU_TAG;
            setFragment(Pantalla.Tabs);
        }
    }

    @Override
    public void verMisMensajes() {
        if (state.validarEstado()) {
            tabIndex = ActivityTabsView.TAB_3_INBOX_TAG;
            setFragment(Pantalla.Tabs);
        }
    }

    @Override
    public void verGeoReferenciacion() {
        if (state.validarEstado()) {
            tabIndex = 4;
            setFragment(Pantalla.Tabs);
        }
    }

    @Override
    public void verCandidatos() {
        if (state.validarEstado()) {
            setFragment(Pantalla.Candidatos);
        }
    }

    @Override
    public void verVotacion() {
        if (state.validarEstado()) {
            setFragment(Pantalla.Votacion);
        }
    }
//
//    @Override
//    public void verBorrarCuentas() {
//        if (state.validarEstado()) {
//            setFragment(Pantalla.BorrarCuentas);
//        }
//    }
//
//    @Override
//    public void verInscribirCuentas() {
//        if (state.validarEstado()) {
//            setFragment(Pantalla.InscripcionCuentas);
//        }
//    }

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
                startActivity(intent);
                return;
            default:
                fragment = new FragmentLoginView();
                break;
        }

        if (state != null && state.getFragmentActual() != null) {
            state.setFragmentAnterior(state.getFragmentActual());
        }

        hideKeyBoard();
        state.setFragmentActual(pantalla);
        if(fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
    }
    //endregion

    public int getUnreadMessagesCount(){
        GlobalState state =  getState();
        if(state != null){
            return state.getUnreadMessagesCount();
        }else {
            return 0;
        }
    }


    //region COORDINADOR PANTALLAS PRINCIPALES
//    @Override
//    public void ingresar(Usuario responseUsuario) {
//        try {
////            Usuario usuario = SincroHelper.procesarJsonLogin(respuesta);
//            Usuario usuario = responseUsuario;
//
////            Usuario usuario = new Usuario();
////            usuario.nombre = "Usuario";
////            usuario.token = "513beef0-aae4-474a-9818-bf0562e66907";
////            usuario.clave = clave;
////            usuario.cedula = cedula;
////            usuario.topeTransacciones = "1200000";
////            usuario.aceptoUltimosTyC = "Y";
////            DatosActualizados datos = new DatosActualizados();
////            datos.setActualizaPrimeraVez("N");
////            datos.setTieneDatosActualizados("Y");
////            datos.setFechaUltimaActualizacion(new Date());
////            usuario.datosActualizados = datos;
////            state.setUsuario(usuario);
//            setFragment(IFragmentCoordinator.Pantalla.MenuPrincipal);
//
//            if (usuario != null && !TextUtils.isEmpty(usuario.getNombreAsociado()) && !TextUtils.isEmpty(usuario.getToken())) {
//                Encripcion encripcion = Encripcion.getInstance();
//                usuario.setClave(encripcion.desencriptar(usuario.getClave()));
//                usuario.setCedula(encripcion.desencriptar(usuario.getCedula()));
//                state.setTopeTransacciones(usuario.getTopeTransacciones());
//                state.setUsuario(usuario);
////                getCelPrincipal();
//                if (state.validarEstado()) {
////                    setFragment(IFragmentCoordinator.Pantalla.MenuPrincipal);
//                    switch (usuario.getDatosActualizados().getActualizaPrimeraVez()){
//                        case "Y":
//                                setFragment(IFragmentCoordinator.Pantalla.MenuPrincipal);
//                                if(usuario.getAceptoUltimosTyC().equals("N")){
//                                    Intent i = new Intent(this, ActivityTermsAndConditionsView.class);
//                                    startActivityForResult(i, TERMS_AND_CONDITIONS);
//                                    return;
//                                } else {
//                                    Intent i = new Intent(this, ActivityUpdatePersonalDataView.class);
//                                    i.putExtra("actualizaPrimeraVez", usuario.getDatosActualizados().getActualizaPrimeraVez());
//                                    i.putExtra("datosActualizados", usuario.getDatosActualizados().getTieneDatosActualizados());
//                                    startActivityForResult(i, UPDATE_PERSONAL_DATA);
//                                }
//                            break;
//                        case "N":
////                            validarDispositivo();
//                            break;
//                        default:
//                            setFragment(IFragmentCoordinator.Pantalla.MenuPrincipal);
//                            break;
//                    }
//                }
//            } else {
//                makeErrorDialog("Usuario inválido");
//            }
////            setFragment(IFragmentCoordinator.Pantalla.MenuPrincipal);
//
////        }catch (ErrorTokenException e){
////            Builder d = new Builder(this);
////            d.setTitle("Sesión finalizada");
////            d.setIcon(R.mipmap.icon_presente);
////            d.setMessage(e.getMessage());
////            d.setCancelable(false);
////            d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
////                public void onClick(DialogInterface dialog, int id) {
////                    salir();
////                }
////            });
////            d.show();
//        } catch (Exception ex) {
//            makeErrorDialog(ex.getMessage());
//        }
//    }



    /*class URLTask extends AsyncTask<String, String, String> {

        ActivityBase context;

        public URLTask(ActivityBase context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            try {
                if (NetworkHelper.isConnectionAvailable(context)) {

                    NetworkHelper helper = new NetworkHelper();
                    String jsonUrl = helper.readService(SincroHelper.URL_SERVICIO);

                    url = SincroHelper.procesarJsonUrlServicio(jsonUrl);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return url;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result!=null){
                context.guardarConfiguracionSecureUrl(result);
            }
        }
    }*/
}
