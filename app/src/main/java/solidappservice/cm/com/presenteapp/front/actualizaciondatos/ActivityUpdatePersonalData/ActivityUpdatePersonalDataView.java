package solidappservice.cm.com.presenteapp.front.actualizaciondatos.ActivityUpdatePersonalData;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.DatosAsociado;
import solidappservice.cm.com.presenteapp.front.menuprincipal.FragmentHome.FragmentHomeView;
import solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentEditData.FragmentEditDataView;
import solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentFinal.FragmentFinalView;
import solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentStart.FragmentStartView;
import solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentValidateData.FragmentValidateDataView;
import solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentVerifyCode.FragmentVerifyCodeView;
import solidappservice.cm.com.presenteapp.front.login.FragmentLogin.FragmentLoginView;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 11/07/2021.
 */
public class ActivityUpdatePersonalDataView extends ActivityBase implements ActivityUpdatePersonalDataContract.View{

    public ActivityUpdatePersonalDataContract.Presenter basePresenter;
    private ProgressDialog pd;
    private ActivityBase context;

    public String actualizaPrimeraVez;
    public String datosActualizados;
    public DatosAsociado datosAsociado;
    public Boolean isDatosEditados;

    @BindView(R.id.btn_back)
    public ImageButton buttonBack;
    @BindView(R.id.container_actualizacion_datos)
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepersonaldata);
        actualizaPrimeraVez = getIntent().getStringExtra("actualizaPrimeraVez");
        datosActualizados = getIntent().getStringExtra("datosActualizados");
        ButterKnife.bind(this);
        setControls();
    }

    @Override
    protected void setControls() {
        basePresenter = new ActivityUpdatePersonalDataPresenter(this);
        context = this;
        datosAsociado = new DatosAsociado();
        isDatosEditados = false;
        basePresenter.loadFragmentUpdatePersonalData(Pantalla.ActDatosStart);
    }

    @OnClick(R.id.btn_back)
    public void onBackPreseed(){
        GlobalState state = getState();
        if(context != null && state != null){
            if(state.getFragmentActual() == Pantalla.ActDatosStart){
                context.salir();
            }
            if(state.getFragmentActual() == Pantalla.ActDatosEditData){
                basePresenter.loadFragmentUpdatePersonalData(Pantalla.ActDatosStart);
            }
            if(state.getFragmentActual() == Pantalla.ActDatosValidateData){
                basePresenter.loadFragmentUpdatePersonalData(Pantalla.ActDatosEditData);
            }
            if(state.getFragmentActual() == Pantalla.ActDatosVerifyCode){
                basePresenter.loadFragmentUpdatePersonalData(Pantalla.ActDatosValidateData);
            }
        }else{
            context.salir();
        }
    }

    @Override
    public void showFragmentUpdatePersonalData(IFragmentCoordinator.Pantalla pantalla) {
        GlobalState state = getState();
        Fragment fragment;
        switch (pantalla) {
            case ActDatosStart:
                fragment = new FragmentStartView();
                break;
            case ActDatosEditData:
                fragment = new FragmentEditDataView();
                break;
            case ActDatosValidateData:
                fragment = new FragmentValidateDataView();
                break;
            case ActDatosVerifyCode:
                fragment = new FragmentVerifyCodeView();
                break;
            case ActDatosFinal:
                fragment = new FragmentFinalView();
                break;
            case MenuPrincipal:
                fragment = new FragmentHomeView();
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
        fragmentManager.beginTransaction().replace(R.id.container_actualizacion_datos, fragment).commit();
    }


//    @Override
//    protected void setControls() {
//        context = this;
//        state = getState();
//        pd = new ProgressDialog(context);
//        isDatosEditados = false;
//        setFragment(Pantalla.ActDatosInicio);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        GlobalState state = context.getState();
//        if(state == null){
//            context.salir();
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        if(context != null && state != null){
//            if(state.getFragmentActual() == Pantalla.ActDatosInicio){
//                context.salir();
//            }
//            if(state.getFragmentActual() == Pantalla.ActDatosEditar){
//                setFragment(Pantalla.ActDatosInicio);
//            }
//            if(state.getFragmentActual() == Pantalla.ActDatosValidar){
//                setFragment(Pantalla.ActDatosEditar);
//            }
//            if(state.getFragmentActual() == Pantalla.ActDatosConfirmar){
//            }
//            if(state.getFragmentActual() == Pantalla.ActDatosGuardar){
//            }
//        }else{
//            context.salir();
//        }
//    }
//
//
//    @Override
//    public void setFragment(Pantalla pantalla) {
//        Fragment fragment;
//        switch (pantalla) {
//            case ActDatosInicio:
//                fragment = new FragmentActDatosInicio();
//                break;
//            case ActDatosEditar:
//                fragment = new FragmentActDatosEditar();
//                break;
//            case ActDatosValidar:
//                fragment = new FragmentActDatosValidar();
//                break;
//            case ActDatosConfirmar:
//                fragment = new FragmentActDatosConfirmar();
//                break;
//            case ActDatosGuardar:
//                fragment = new FragmentActDatosGuardar();
//                break;
//            case MenuPrincipal:
//                fragment = new FragmentMenuPrincipal();
//                break;
//            default:
//                fragment = new FragmentLogin();
//                break;
//        }
//
//        if (state != null && state.getFragmentActual() != null) {
//            state.setFragmentAnterior(state.getFragmentActual());
//        }
//
//        hideKeyBoard();
//        state.setFragmentActual(pantalla);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.container_actualizacion_datos, fragment).commit();
//    }
//
//
//
//    //----------------------------------------------------------------------------------
//    //------------------------------Registrar dispositivo-------------------------------
//    public void registrarDispositivo(){
//        try{
//            Usuario usuario = state.getUsuario();
//            Encripcion encripcion = Encripcion.getInstance();
//            JSONObject param = new JSONObject();
//            param.put("cedula", encripcion.encriptar(usuario.cedula));
//            param.put("token", usuario.token);
//            param.put("fabricante", context.getFabricante());
//            param.put("modelo", context.getModelo());
//            param.put("idDispositivo", context.getIdDispositivo());
//            param.put("imei", context.getImei());
//            param.put("celPrincipal", context.getCelPrincipal());
//            param.put("sistemaOperativo", state.isHmsSystem() ? "Android HMS" : "Android GMS");
//            param.put("versionSistemaOperativo", context.getVersionSistemaOperativo());
//            new RegistrarDispositivoTask().execute(param);
//        }catch (Exception e){
//            context.makeSToast("Error al actualizar datos");
//        }
//    }
//
//    //Registrar nuevo dispositivo
//    private class RegistrarDispositivoTask extends AsyncTask<JSONObject, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Registrando dispositivo...");
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(JSONObject... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                return networkHelper.writeService(params[0], SincroHelper.REGISTRAR_DISPOSITIVO);
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
//            procesarResultRegistrarDispositivo(result);
//            pd.dismiss();
//        }
//    }
//
//    private void procesarResultRegistrarDispositivo(String result) {
//        try {
//            String idRegistroDispositivo = SincroHelper.procesarJsonRegistrarDispositivo(result);
//            if(!TextUtils.isEmpty(idRegistroDispositivo)){
//                state.setIdDispositivoRegistrado(idRegistroDispositivo);
//                actualizarDatos(idRegistroDispositivo);
//            } else {
//                context.makeErrorDialog("Error actualizando los datos, intenta de nuevo más tarde");
//            }
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
//        } catch (Exception ex) {
//            context.makeErrorDialog(ex.getMessage());
//        }
//    }
//
//
//
//
//    //-----------------------------------------------------------------------------
//    //------------------------------Actualizar Datos-------------------------------
//    public void actualizarDatos(String idRegistroDispositivo){
//        try{
//            Usuario usuario = state.getUsuario();
//            Encripcion encripcion = Encripcion.getInstance();
//            JSONObject param = new JSONObject();
//            param.put("cedula", encripcion.encriptar(usuario.cedula));
//            param.put("token", usuario.token);
//            param.put("idRegistroDispositivo", idRegistroDispositivo);
//            param.put("nombreCompleto", datosAsociado.getNombreCompleto());
//            param.put("direccion", datosAsociado.getDireccion());
//            param.put("celular", datosAsociado.getCelular());
//            param.put("email", datosAsociado.getEmail());
//            param.put("barrio", datosAsociado.getBarrio());
//            param.put("idCiudad", datosAsociado.getIdCiudad());
//            param.put("idDepartamento", datosAsociado.getIdDepartamento());
//            param.put("idPais", datosAsociado.getIdPais());
//            param.put("ip", context.getLocalIpAddress());
//            param.put("canal", "APP PRESENTE");
//            new ActualizarDatosTask().execute(param);
//        }catch (Exception e){
//            context.makeSToast("Error al actualizar datos");
//        }
//    }
//
//    private class ActualizarDatosTask extends AsyncTask<JSONObject, String, String> {
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Actualizando datos...");
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(JSONObject... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                return networkHelper.writeService(params[0], SincroHelper.ACTUALIZAR_DATOS_ASOCIADO);
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
//            procesarResultActualizarDatosAsociado(result);
//            pd.dismiss();
//        }
//    }
//
//    private void procesarResultActualizarDatosAsociado(String result) {
//        try {
//            String datosActualizados = SincroHelper.procesarJsonActualizarDatos(result);
//            if(!TextUtils.isEmpty(datosActualizados)){
//                setFragment(Pantalla.ActDatosGuardar);
//            } else {
//                context.makeErrorDialog("Error actualizando los datos, intenta de nuevo más tarde");
//            }
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
//        } catch (Exception ex) {
//            context.makeErrorDialog(ex.getMessage());
//        }
//    }

}
