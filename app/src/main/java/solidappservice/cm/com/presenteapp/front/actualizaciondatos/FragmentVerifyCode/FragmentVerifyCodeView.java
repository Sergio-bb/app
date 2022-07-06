package solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentVerifyCode;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.CodigoVerificacion;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.RequestActualizarDatos;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.RequestEnviarCodigo;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.RequestEnviarCodigoPhone;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.RequestValidarCodigo;
import solidappservice.cm.com.presenteapp.entities.dispositivo.apirequest.Dispositivo;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.actualizaciondatos.ActivityUpdatePersonalData.ActivityUpdatePersonalDataView;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.HeightProviderFragment;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 11/07/2021.
 */
public class FragmentVerifyCodeView extends Fragment implements FragmentVerifyCodeContract.View, View.OnClickListener {

    private FragmentVerifyCodePresenter presenterVerifyCode;
    private ActivityUpdatePersonalDataView baseView;
    private ActivityBase context;
    private GlobalState state;
    private ProgressDialog pd;
    private Contador timer;
    private CodigoVerificacion codigo;
    private Boolean isSendToEmail;
    private Boolean isSendToPhone;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.view_final_confirmar)
    View viewFinal;

    @BindView(R.id.et_codigo1)
    EditText etCodigo1;
    @BindView(R.id.et_codigo2)
    EditText etCodigo2;
    @BindView(R.id.et_codigo3)
    EditText etCodigo3;
    @BindView(R.id.et_codigo4)
    EditText etCodigo4;
    @BindView(R.id.tiempo_expiracion)
    TextView tiempoExpiracion;
    @BindView(R.id.circular_progress_bar)
    ProgressBar circularProgressBar;
    @BindView(R.id.text_circular_progress_Bar)
    TextView textCircularProgressBar;

    @BindView(R.id.btn_enviar_correo)
    LinearLayout buttonEnviarCodigoCorreo;
    @BindView(R.id.btn_enviar_celular)
    LinearLayout buttonEnviarCodigoCelular;

    @BindView(R.id.btn_validar_codigo)
    Button btnValidarCodigo;
    @BindView(R.id.btn_reenviar_codigo)
    TextView btnReenviarCodigo;
    @BindView(R.id.box_tipos_envio_codigo)
    LinearLayout boxTiposEnvioCodigo;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de confirmar de actualización de datos");
        firebaseAnalytics.logEvent("pantalla_act_datos_confirmar", params);
        View view = inflater.inflate(R.layout.fragment_updatepersonaldata_verifycode, container, false);
        ButterKnife.bind(this, view);
        new HeightProviderFragment(this).init().setHeightListener(new HeightProviderFragment.HeightListener() {
            @Override
            public void onHeightChanged(int height) {
                viewFinal.setLayoutParams(new LinearLayout.LayoutParams(100,height));
            }
        });
        setControls();
        return view;
    }

    protected void setControls() {
        presenterVerifyCode = new FragmentVerifyCodePresenter(this, new FragmentVerifyCodeModel());
        baseView = (ActivityUpdatePersonalDataView) getActivity();
        context = (ActivityBase) getActivity();
        state = context.getState();
        pd = new ProgressDialog(context);
        disabledSectionVerificationCode();
        isSendToEmail = false;
        isSendToPhone = false;
        if (baseView != null) {
            baseView.buttonBack.setVisibility(View.VISIBLE);
            baseView.buttonBack.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                baseView.basePresenter.loadFragmentUpdatePersonalData(IFragmentCoordinator.Pantalla.ActDatosValidateData);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null || state.getUsuario() == null){
            context.salir();
            return;
        }
    }

    @OnClick(R.id.btn_reenviar_codigo)
    public void onClickReenviarCodigo(){
        resendVerificationCode();
    }

    @OnClick(R.id.btn_validar_codigo)
    public void onClickValidarCodigo(){
        validateVerificationCode();
    }

    @OnClick(R.id.btn_enviar_correo)
    public void onClickSentVerificationCodeMail(){
        sendVerificationCodeEmail();
    }

    @OnClick(R.id.btn_enviar_celular)
    public void onClickSentVerificationCodePhone(){
        sendVerificationCodePhone();
//        sendSms();
    }

    @OnTextChanged(R.id.et_codigo1)
    void afterTextChangedEtCodigo1(Editable arg0) {
        etCodigo2.requestFocus();
        if(!TextUtils.isEmpty(etCodigo1.getText()) && !TextUtils.isEmpty(etCodigo2.getText()) &&
                !TextUtils.isEmpty(etCodigo3.getText()) && !TextUtils.isEmpty(etCodigo4.getText())){
            btnValidarCodigo.setEnabled(true);
            btnValidarCodigo.setTextColor(Color.parseColor("#000000"));
            btnValidarCodigo.setBackground(getResources().getDrawable(R.drawable.btn_green));
        }else{
            btnValidarCodigo.setEnabled(false);
            btnValidarCodigo.setBackground(getResources().getDrawable(R.drawable.backgroun_border_radius_gris));
        }
    }

    @OnTextChanged(R.id.et_codigo2)
    void afterTextChangedEtCodigo2(Editable arg0) {
        etCodigo3.requestFocus();
        if(!TextUtils.isEmpty(etCodigo1.getText()) && !TextUtils.isEmpty(etCodigo2.getText()) &&
                !TextUtils.isEmpty(etCodigo3.getText()) && !TextUtils.isEmpty(etCodigo4.getText())){
            btnValidarCodigo.setEnabled(true);
            btnValidarCodigo.setTextColor(Color.parseColor("#000000"));
            btnValidarCodigo.setBackground(getResources().getDrawable(R.drawable.btn_green));
        }else{
            btnValidarCodigo.setEnabled(false);
            btnValidarCodigo.setBackground(getResources().getDrawable(R.drawable.backgroun_border_radius_gris));
        }
    }

    @OnTextChanged(R.id.et_codigo3)
    void afterTextChangedEtCodigo3(Editable arg0) {
        etCodigo4.requestFocus();
        if(!TextUtils.isEmpty(etCodigo1.getText()) && !TextUtils.isEmpty(etCodigo2.getText()) &&
                !TextUtils.isEmpty(etCodigo3.getText()) && !TextUtils.isEmpty(etCodigo4.getText())){
            btnValidarCodigo.setEnabled(true);
            btnValidarCodigo.setTextColor(Color.parseColor("#000000"));
            btnValidarCodigo.setBackground(getResources().getDrawable(R.drawable.btn_green));
        }else{
            btnValidarCodigo.setEnabled(false);
            btnValidarCodigo.setBackground(getResources().getDrawable(R.drawable.backgroun_border_radius_gris));
        }
    }

    @OnTextChanged(R.id.et_codigo4)
    void afterTextChangedEtCodigo4(Editable arg0) {
        if(!TextUtils.isEmpty(etCodigo1.getText()) && !TextUtils.isEmpty(etCodigo2.getText()) &&
                !TextUtils.isEmpty(etCodigo3.getText()) && !TextUtils.isEmpty(etCodigo4.getText())){
            btnValidarCodigo.setEnabled(true);
            btnValidarCodigo.setTextColor(Color.parseColor("#000000"));
            btnValidarCodigo.setBackground(getResources().getDrawable(R.drawable.btn_green));
        }else{
            btnValidarCodigo.setEnabled(false);
            btnValidarCodigo.setBackground(getResources().getDrawable(R.drawable.backgroun_border_radius_gris));
        }
    }

    @OnFocusChange(R.id.et_codigo1)
    protected void onFocusChangeEtCodigo1(View v, boolean hasFocus) {
        if (hasFocus) {
            etCodigo1.requestFocus();
                if(!TextUtils.isEmpty(etCodigo1.getText())){
                    etCodigo1.setText("");
                    etCodigo1.requestFocus();
                }
        }
    }

    @OnFocusChange(R.id.et_codigo2)
    protected void onFocusChangeEtCodigo2(View v, boolean hasFocus) {
        if (hasFocus) {
            etCodigo2.requestFocus();
                if(!TextUtils.isEmpty(etCodigo2.getText())){
                    etCodigo2.setText("");
                    etCodigo2.requestFocus();
                }
        }
    }

    @OnFocusChange(R.id.et_codigo3)
    protected void onFocusChangeEtCodigo3(View v, boolean hasFocus) {
        if (hasFocus) {
            etCodigo3.requestFocus();
            if(!TextUtils.isEmpty(etCodigo3.getText())) {
                etCodigo3.setText("");
                etCodigo3.requestFocus();
            }
        }
    }

    @OnFocusChange(R.id.et_codigo4)
    protected void onFocusChangeEtCodigo4(View v, boolean hasFocus) {
        if (hasFocus) {
            etCodigo4.requestFocus();
            if(!TextUtils.isEmpty(etCodigo4.getText())){
                etCodigo4.setText("");
                etCodigo4.requestFocus();
            }
        }
    }

    @Override
    public void sendVerificationCodeEmail() {
        try{
            GlobalState state = context.getState();
            Encripcion encripcion = Encripcion.getInstance();
            isSendToEmail = true;
            isSendToPhone = false;
            presenterVerifyCode.sendVerificationCodeEmail(new RequestEnviarCodigo(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    1,
                    "",
                    baseView.datosAsociado.getEmail()

            ));
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void sendVerificationCodePhone() {
        try{
            GlobalState state = context.getState();
            Encripcion encripcion = Encripcion.getInstance();
            isSendToEmail = false;
            isSendToPhone = true;
            presenterVerifyCode.sendVerificationCodePhone(new RequestEnviarCodigo(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    2,
                    baseView.datosAsociado.getCelular(),
                    ""
            ));
        }catch(Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void sendSms() {
        presenterVerifyCode.sendSms(new RequestEnviarCodigoPhone(
                "Tu código de verificación es 1234",
                "1",
                baseView.datosAsociado.getCelular()
        ));
    }

    @Override
    public void validateVerificationCode() {
        try{
            GlobalState state = context.getState();
            Encripcion encripcion = Encripcion.getInstance();
            String codigoIngresado = etCodigo1.getText().toString()+
                    etCodigo2.getText().toString()+
                    etCodigo3.getText().toString()+
                    etCodigo4.getText().toString();

            presenterVerifyCode.validateVerificationCode(new RequestValidarCodigo(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    codigo.getIdCodigo(),
                    codigoIngresado
            ));
        }catch(Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void updateInformation() {
        try {
            GlobalState state = context.getState();
            Encripcion encripcion = Encripcion.getInstance();
            if (baseView != null & baseView.actualizaPrimeraVez.equals("Y")) {
                presenterVerifyCode.registerDevice(new Dispositivo(
                        encripcion.encriptar(state.getUsuario().getCedula()),
                        state.getUsuario().getToken(),
                        context.getFabricante(),
                        context.getModelo(),
                        context.getIdDispositivo(),
                        Build.VERSION.SDK_INT <= Build.VERSION_CODES.O ? "" : context.getImei(),
                        Build.VERSION.SDK_INT <= Build.VERSION_CODES.O ? "" : context.getCelPrincipal(),
                        state.isHmsSystem() ? "Android HMS" : "Android GMS",
                        context.getVersionSistemaOperativo())
                );
            } else {
                baseView.datosAsociado.setIdRegistroDispositivo(state.getIdDispositivoRegistrado());
                baseView.datosAsociado.setCanal("APP PRESENTE");
                presenterVerifyCode.updatePersonalData(new RequestActualizarDatos(
                        encripcion.encriptar(state.getUsuario().getCedula()),
                        state.getUsuario().getToken(),
                        baseView.datosAsociado.getIdRegistroDispositivo(),
                        baseView.datosAsociado.getNombreCompleto(),
                        baseView.datosAsociado.getDireccion().replace("#", ""),
                        baseView.datosAsociado.getCelular(),
                        baseView.datosAsociado.getEmail(),
                        baseView.datosAsociado.getBarrio(),
                        baseView.datosAsociado.getIdCiudad(),
                        baseView.datosAsociado.getIdDepartamento(),
                        baseView.datosAsociado.getIdPais(),
                        baseView.datosAsociado.getIp(),
                        baseView.datosAsociado.getCanal()
                ));
            }
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void resultRegisterDevice(String idRegistroDispositivo) {
        try{
            GlobalState state = context.getState();
            Encripcion encripcion = Encripcion.getInstance();
            state.setIdDispositivoRegistrado(idRegistroDispositivo);
            baseView.datosAsociado.setIdRegistroDispositivo(idRegistroDispositivo);
            baseView.datosAsociado.setCanal("APP PRESENTE");
            presenterVerifyCode.updatePersonalData(new RequestActualizarDatos(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    baseView.datosAsociado.getIdRegistroDispositivo(),
                    baseView.datosAsociado.getNombreCompleto(),
                    baseView.datosAsociado.getDireccion().replace("#", ""),
                    baseView.datosAsociado.getCelular(),
                    baseView.datosAsociado.getEmail(),
                    baseView.datosAsociado.getBarrio(),
                    baseView.datosAsociado.getIdCiudad(),
                    baseView.datosAsociado.getIdDepartamento(),
                    baseView.datosAsociado.getIdPais(),
                    baseView.datosAsociado.getIp(),
                    baseView.datosAsociado.getCanal()
            ));
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void resultUpdatePersonalData() {
        baseView.basePresenter.loadFragmentUpdatePersonalData(IFragmentCoordinator.Pantalla.ActDatosFinal);
    }

    @Override
    public void resultSendVerificationCode(CodigoVerificacion codigo) {
        this.codigo = codigo;
    }

    @Override
    public void initializeCounter() {
        tiempoExpiracion.setTextColor(Color.parseColor("#4A4A49"));
        if (timer != null) {
            timer.cancel();
        }
        timer = new Contador(180000,1000);
        timer.start();
        context.makeSToast("Código enviado");
    }

    @Override
    public void showDialogError(String title, String content) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_message);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView titleMessage = (TextView) dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText(Html.fromHtml("<b>"+title+"</b>"));
        TextView contentMessage = (TextView) dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(content);
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
    public void showDialogCodeSent() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_message);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView titleMessage = (TextView) dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setTextColor(Color.parseColor("#53A653"));
        titleMessage.setText(Html.fromHtml("<b>Envío exitoso</b>"));
        TextView contentMessage = (TextView) dialog.findViewById(R.id.lbl_content_message);
        View separator = (View) dialog.findViewById(R.id.separator_green);
        separator.setBackgroundResource(R.drawable.separator_green);
        contentMessage.setText(String.format("Hemos enviado a tu \n %s \n el código para continuar el proceso",
                isSendToEmail ? "correo " + baseView.datosAsociado.getEmail() :
                isSendToPhone ? "celular " + baseView.datosAsociado.getCelular() : ""));
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
    public void resendVerificationCode() {
        presenterVerifyCode.resendVerificationCode();
    }

    @Override
    public void enableSectionVerificationCode() {
        etCodigo1.setEnabled(true);
        etCodigo2.setEnabled(true);
        etCodigo3.setEnabled(true);
        etCodigo4.setEnabled(true);
        btnReenviarCodigo.setEnabled(true);
    }

    @Override
    public void disabledSectionVerificationCode() {
        btnValidarCodigo.setEnabled(false);
        btnValidarCodigo.setBackground(getResources().getDrawable(R.drawable.backgroun_border_radius_gris));
        etCodigo1.setEnabled(false);
        etCodigo2.setEnabled(false);
        etCodigo3.setEnabled(false);
        etCodigo4.setEnabled(false);
        btnReenviarCodigo.setEnabled(false);
    }

    @Override
    public void showBoxTypesCodeSend() {
        boxTiposEnvioCodigo.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBoxTypesCodeSend() {
        boxTiposEnvioCodigo.setVisibility(View.GONE);
    }

    @Override
    public void showCircularProgressBar(String textProgressBar) {
        circularProgressBar.setVisibility(View.VISIBLE);
        textCircularProgressBar.setText(textProgressBar);
        textCircularProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCircularProgressBar() {
        circularProgressBar.setVisibility(View.GONE);
        textCircularProgressBar.setVisibility(View.GONE);
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

    public class Contador extends CountDownTimer {

        public Contador(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            tiempoExpiracion.setText("Código caducado");
            tiempoExpiracion.setTextColor(Color.RED);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long minutos =  millisUntilFinished / 60000;
            long segundos = millisUntilFinished % 60000 / 1000;
            tiempoExpiracion.setText(
                String.format("%s:%s",
                        "Caducará en: " + (minutos < 10 ? "0" + minutos : minutos), segundos < 10 ? "0" + segundos : segundos));
        }
    }

}
