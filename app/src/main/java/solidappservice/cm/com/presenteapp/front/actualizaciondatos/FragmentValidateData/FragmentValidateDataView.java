package solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentValidateData;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.RelativeLayout;
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
    private Dialog pd;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.sectionValidateData)
    RelativeLayout sectionValidateData;
    @BindView(R.id.layout_circular_progress_bar)
    LinearLayout layoutCircularProgressBar;
    @BindView(R.id.circular_progress_bar)
    ProgressBar circularProgressBar;
    @BindView(R.id.text_circular_progress_Bar)
    TextView textCircularProgressBar;
    @BindView(R.id.imageReferesh)
    ImageView buttonReferesh;

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
        } else {
            processPersonalData();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            if (context != null && context.getState().getFragmentAnterior() == IFragmentCoordinator.Pantalla.MenuPrincipal) {
                context.finish();
            } else {
                baseView.basePresenter.loadFragmentUpdatePersonalData(IFragmentCoordinator.Pantalla.ActDatosEditData);
            }
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
                        hideCircularProgressBar();
                        showSectionValidateData();
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
                            hideCircularProgressBar();
                            showSectionValidateData();
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
            showDataFetchError("Lo sentimos", "");
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
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void resultUpdatePersonalData() {
        baseView.basePresenter.loadFragmentUpdatePersonalData(IFragmentCoordinator.Pantalla.ActDatosFinal);
    }

    @Override
    public void showSectionValidateData() {
        sectionValidateData.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSectionValidateData() {
        sectionValidateData.setVisibility(View.GONE);
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
        sectionValidateData.setVisibility(View.GONE);
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
                baseView.finish();
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
                baseView.finish();
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

    @Override
    public void showProgressDialog(String message) {
        pd = new Dialog(context);
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.setCanceledOnTouchOutside(false);
        pd.setContentView(R.layout.pop_up_loading);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView contentMessage = (TextView) pd.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        pd.show();
    }

    @Override
    public void hideProgressDialog() {
        if(pd != null){
            pd.dismiss();
        }
    }
}
