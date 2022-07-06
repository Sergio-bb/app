package solidappservice.cm.com.presenteapp.front.nequi.suscription.FragmentSecondSuscription;

import android.app.Dialog;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.DatosAsociado;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestSendSubscription;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultaSuscripcion;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.nequi.suscription.ActivitySuscription.ActivitySuscriptionView;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

public class FragmentSecondSuscriptionView extends Fragment implements FragmentSecondSuscriptionContract.View {

    private ActivityBase context;
    private ActivitySuscriptionView baseView;
    private GlobalState state;
    private FirebaseAnalytics firebaseAnalytics;
    private FragmentSecondSuscriptionPresenter presentersecond;

    @BindView(R.id.tv_datos_nombre)
    TextView tv_nombre;
    @BindView(R.id.tv_datos_Cedula)
    TextView tv_cedula;
    @BindView(R.id.tv_datos_contacto)
    TextView tv_contacto;
    @BindView(R.id.buttonEnviarSuscripcion)
    Button buttonEnviarSuscripcion;
    @BindView(R.id.btnAtras)
    Button btnAtras;

    @BindView(R.id.layout_personaldata)
    LinearLayout layoutDatosPersonales;
    @BindView(R.id.circular_progress_bar)
    ProgressBar circularProgressBar;
    @BindView(R.id.layout_circular_progress_bar_suspcripcion)
    LinearLayout circularProgressBarSuscripcion;

    Dialog dialogSendingRequest ;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de segundo paso suscricion nequi");
        firebaseAnalytics.logEvent("pantalla_second_suscripcion", params);
        View view = inflater.inflate(R.layout.fragment_nequi_suscripcion_second, container, false);
        ButterKnife.bind(this, view);
        setControl();
        return view;
    }

    protected void setControl() {
        presentersecond = new FragmentSecondSuscriptionPresenter(this, new FragmentSecondSuscriptionModel());
        context = (ActivityBase) getActivity();
        baseView = (ActivitySuscriptionView)  getActivity();
        state = context.getState();
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if (state == null || state.getUsuario() == null) {
            context.salir();
        } else {
              processPersonalData();
        }
    }

    public void processPersonalData() {
        if (baseView != null) {
             if (baseView.datosAsociado != null) {
                 tv_nombre.setText(baseView.datosAsociado.getNombreCompleto());
                 tv_cedula.setText(state.getUsuario().getCedula());
                 tv_contacto.setText(baseView.datosAsociado.getCelular());
                 showResumePersonalData();
                 enableButtonSend();
            } else {
                  fetchPersonalData();
             }
        } else {
            baseView.basePresenter.loadFragmentSuscripcionNequi(IFragmentCoordinator.Pantalla.SuscNequiOne);
        }
    }

    @Override
    public void fetchPersonalData() {
        GlobalState state = context.getState();
        Encripcion encripcion = Encripcion.getInstance();
        presentersecond.fetchPersonalData(new BaseRequest(
                encripcion.encriptar(state.getUsuario().getCedula()),
                state.getUsuario().getToken()
        ));
    }

    @Override
    public void showPersonalData(DatosAsociado datos) {
        if(baseView != null){
            if(datos != null){
                baseView.datosAsociado = datos;
                tv_nombre.setText(datos.getNombreCompleto());
                tv_cedula.setText(state.getUsuario().getCedula());
                tv_contacto.setText(datos.getCelular());
            }
        } else{
            baseView.basePresenter.loadFragmentSuscripcionNequi(IFragmentCoordinator.Pantalla.SuscNequiInicio);
        }
    }

    @OnClick(R.id.buttonEnviarSuscripcion)
    public void onClikEnviarSuscripcion(){
        sendSubscriptionNotificacion();
    }

    @OnClick(R.id.btnAtras)
    public void onClikAtras(){
        baseView.basePresenter.loadFragmentSuscripcionNequi(IFragmentCoordinator.Pantalla.SuscNequiOne);
    }

    @Override
    public void sendSubscriptionNotificacion() {
        try{
            presentersecond.sendSubscriptionNotificacion(new RequestSendSubscription(
                    Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    baseView.datosAsociado.getCelular()
            ));
        }catch(Exception ex){
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void resultSendSubscriptionNotificacion(boolean isSendSuscripcion) {
        if(baseView != null && isSendSuscripcion) {
            baseView.basePresenter.loadFragmentSuscripcionNequi(IFragmentCoordinator.Pantalla.SuscNequiFinal);
        }
    }

    @Override
    public void showResumePersonalData(){
        layoutDatosPersonales.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideResumePersonalData(){
        layoutDatosPersonales.setVisibility(View.GONE);
    }

    @Override
    public void enableButtonSend(){
        buttonEnviarSuscripcion.setEnabled(true);
    }

    @Override
    public void disabledButtonSend(){
        buttonEnviarSuscripcion.setEnabled(false);
    }

    @Override
    public void showButtonSend(){
        buttonEnviarSuscripcion.setVisibility(View.VISIBLE);
        btnAtras.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideButtonSend(){
        buttonEnviarSuscripcion.setVisibility(View.GONE);
        btnAtras.setVisibility(View.GONE);
    }
    @Override
    public void showCircularProgressBarSuscription() {
        circularProgressBarSuscripcion.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCircularProgressBarSuscription() {
        if(circularProgressBarSuscripcion != null) {
            circularProgressBarSuscripcion.setVisibility(View.GONE);
        }
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
        buttonClose.setOnClickListener(view -> {
            baseView.basePresenter.loadFragmentSuscripcionNequi(IFragmentCoordinator.Pantalla.SuscNequiInicio);
            dialog.dismiss();
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
        buttonClose.setOnClickListener(view -> {
            baseView.finish();
            dialog.dismiss();
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


    //Pendientes
    @Override
    public void showSendingRequet() {
        dialogSendingRequest= new Dialog(context);
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
    @Override
    public  void hiddenSendingRequest(){
        if(dialogSendingRequest != null){
            dialogSendingRequest.hide();
        }
    }
}