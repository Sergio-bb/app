package solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentValidateData;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.DatosAsociado;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.RequestActualizarDatos;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.actualizaciondatos.ActivityUpdatePersonalData.ActivityUpdatePersonalDataView;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 11/07/2021.
 */
public class FragmentValidateDataView extends Fragment implements FragmentValidateDataContract.View, View.OnClickListener{

    private FragmentValidateDataPresenter presenterValidateData;
    private ActivityUpdatePersonalDataView baseView;
    private ActivityBase context;
    private GlobalState state;
    private ProgressDialog pd;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.tv_act_datos_nombre)
    TextView tvNombre;
    @BindView(R.id.tv_act_datos_direccion)
    TextView tvDireccion;
    @BindView(R.id.tv_act_datos_departamento)
    TextView tvDepartamento;
    @BindView(R.id.tv_act_datos_ciudad)
    TextView tvCiudad;
    @BindView(R.id.tv_act_datos_coreo)
    TextView tvCorreo;
    @BindView(R.id.tv_act_datos_contacto)
    TextView  tvNumeroContacto;
    @BindView(R.id.btnContinuar_actDatos_validar)
    Button btnContinuar;
    @BindView(R.id.btnEditar_actDatos_validar)
    Button btnEditar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de validar de actualización de datos");
        firebaseAnalytics.logEvent("pantalla_act_datos_validar", params);
        View view = inflater.inflate(R.layout.fragment_updatepersonaldata_validatedata, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenterValidateData = new FragmentValidateDataPresenter(this, new FragmentValidateDataModel());
        baseView = (ActivityUpdatePersonalDataView) getActivity();
        context = (ActivityBase) getActivity();
        state = context.getState();
        pd = new ProgressDialog(context);
        if (baseView != null) {
            baseView.buttonBack.setVisibility(View.VISIBLE);
            baseView.buttonBack.setOnClickListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null || state.getUsuario() == null){
            context.salir();
            return;
        } else {
            processPersonalData();;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                if(context != null && context.getState().getFragmentAnterior() == IFragmentCoordinator.Pantalla.MenuPrincipal){
                    context.finish();
                }else{
                    baseView.basePresenter.loadFragmentUpdatePersonalData(IFragmentCoordinator.Pantalla.ActDatosEditData);
                }
                break;
        }
    }

    @OnClick(R.id.btnContinuar_actDatos_validar)
    public void onClickContinuar(){
        if(baseView.isDatosEditados){
            baseView.basePresenter.loadFragmentUpdatePersonalData(IFragmentCoordinator.Pantalla.ActDatosVerifyCode);
        } else {
            if(baseView.actualizaPrimeraVez.equals("Y")){
                baseView.basePresenter.loadFragmentUpdatePersonalData(IFragmentCoordinator.Pantalla.ActDatosVerifyCode);
            } else {
                updatePersonalData();
            }
        }
    }

    @OnClick(R.id.btnEditar_actDatos_validar)
    public void onClickEditar(){
        if(context != null && context.getState().getFragmentAnterior() == IFragmentCoordinator.Pantalla.MenuPrincipal){
            context.finish();
        }else{
            baseView.basePresenter.loadFragmentUpdatePersonalData(IFragmentCoordinator.Pantalla.ActDatosEditData);
        }
    }

    @Override
    public void processPersonalData() {
        if(baseView != null){
            switch (baseView.actualizaPrimeraVez){
                case "Y":
                    if(baseView.datosAsociado != null){
                        tvNombre.setText(baseView.datosAsociado.getNombreCompleto());
                        tvDireccion.setText(String.format("%s %s", baseView.datosAsociado.getDireccion(), baseView.datosAsociado.getBarrio()));
                        tvDepartamento.setText(baseView.datosAsociado.getNombreDepartamento());
                        tvCiudad.setText(baseView.datosAsociado.getNombreCiudad());
                        tvCorreo.setText(baseView.datosAsociado.getEmail());
                        tvNumeroContacto.setText(baseView.datosAsociado.getCelular());
                    } else {
                        baseView.basePresenter.loadFragmentUpdatePersonalData(IFragmentCoordinator.Pantalla.ActDatosEditData);
                    }
                    break;
                case "N":
                    if(baseView.isDatosEditados){
                        if(baseView.datosAsociado != null){
                            tvNombre.setText(baseView.datosAsociado.getNombreCompleto());
                            tvDireccion.setText(String.format("%s %s", baseView.datosAsociado.getDireccion(), baseView.datosAsociado.getBarrio()));
                            tvDepartamento.setText(baseView.datosAsociado.getNombreDepartamento());
                            tvCiudad.setText(baseView.datosAsociado.getNombreCiudad());
                            tvCorreo.setText(baseView.datosAsociado.getEmail());
                            tvNumeroContacto.setText(baseView.datosAsociado.getCelular());
                        } else {
                            fetchPersonalData();
                        }
                    } else {
                        fetchPersonalData();
                    }
                    break;
            }
        } else{
            baseView.basePresenter.loadFragmentUpdatePersonalData(IFragmentCoordinator.Pantalla.ActDatosEditData);
        }
    }

    @Override
    public void showPersonalData(DatosAsociado datos) {
        if(baseView != null){
            if(datos != null){
                baseView.datosAsociado = datos;
                tvNombre.setText(baseView.datosAsociado.getNombreCompleto());
                tvDireccion.setText(String.format("%s %s", datos.getDireccion(), datos.getBarrio()));
                tvDepartamento.setText(datos.getNombreDepartamento());
                tvCiudad.setText(datos.getNombreCiudad());
                tvCorreo.setText(datos.getEmail());
                tvNumeroContacto.setText(datos.getCelular());
            }
        } else{
            baseView.basePresenter.loadFragmentUpdatePersonalData(IFragmentCoordinator.Pantalla.ActDatosEditData);
        }
    }

    @Override
    public void fetchPersonalData() {
        try{
            GlobalState state = context.getState();
            Encripcion encripcion = Encripcion.getInstance();
            presenterValidateData.fetchPersonalData(new BaseRequest(
                encripcion.encriptar(state.getUsuario().getCedula()),
                state.getUsuario().getToken()
            ));
        }catch(Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void updatePersonalData() {
        try{
            GlobalState state = context.getState();
            Encripcion encripcion = Encripcion.getInstance();
            baseView.datosAsociado.setIdRegistroDispositivo(state.getIdDispositivoRegistrado());
            baseView.datosAsociado.setCanal("APP PRESENTE");
            presenterValidateData.updatePersonalData(new RequestActualizarDatos(
                encripcion.encriptar(state.getUsuario().getCedula()),
                state.getUsuario().getToken(),
                baseView.datosAsociado.getIdRegistroDispositivo(),
                baseView.datosAsociado.getNombreCompleto(),
                baseView.datosAsociado.getDireccion(),
                baseView.datosAsociado.getCelular(),
                baseView.datosAsociado.getEmail(),
                baseView.datosAsociado.getBarrio(),
                baseView.datosAsociado.getIdCiudad(),
                baseView.datosAsociado.getIdDepartamento(),
                baseView.datosAsociado.getIdPais(),
                baseView.datosAsociado.getIp(),
                baseView.datosAsociado.getCanal()
            ));
        }catch(Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void resultUpdatePersonalData() {
        baseView.basePresenter.loadFragmentUpdatePersonalData(IFragmentCoordinator.Pantalla.ActDatosFinal);
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
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle(context.getResources().getString(R.string.app_name));
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(message);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                baseView.finish();
            }
        });
        d.show();
    }

    @Override
    public void showDataFetchError(String message) {
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
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle(context.getResources().getString(R.string.app_name));
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(message);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                baseView.finish();
            }
        });
        d.show();
    }

    @Override
    public void showExpiredToken(String message) {
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle("Sesión finalizada");
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(message);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                context.salir();
            }
        });
        d.show();
    }

    @Override
    public void showProgressDialog(String message) {
        pd.setTitle(context.getResources().getString(R.string.app_name));
        pd.setMessage(message);
        pd.setIcon(R.mipmap.icon_presente);
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    public void hideProgressDialog() {
        pd.dismiss();
    }

}
