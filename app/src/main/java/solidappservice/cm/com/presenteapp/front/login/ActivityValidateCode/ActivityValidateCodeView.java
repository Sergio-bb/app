package solidappservice.cm.com.presenteapp.front.login.ActivityValidateCode;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.CodigoVerificacion;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.RequestEnviarCodigo;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.RequestValidarCodigo;
import solidappservice.cm.com.presenteapp.entities.dispositivo.apirequest.Dispositivo;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 21/09/2021.
 */
public class ActivityValidateCodeView extends ActivityBase implements DialogInterface.OnCancelListener, ActivityValidateCodeContract.View {

    private ActivityValidateCodePresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private ProgressDialog pd;
    private Contador timer;
    private CodigoVerificacion codigo;

    @BindView(R.id.layout_main)
    RelativeLayout layout_main;

    @BindView(R.id.tv_tiempoexpiracion)
    TextView tiempoExpiracion;
    @BindView(R.id.tv_mensajeCodigo)
    TextView mensajeCodigo;
    @BindView(R.id.box_tipos_envio_codigo)
    LinearLayout boxTiposEnvioCodigo;
    @BindView(R.id.box_campos_codigo)
    LinearLayout boxCamposCodigo;
    @BindView(R.id.btn_enviar_correo)
    LinearLayout buttonEnviarCorreo;
    @BindView(R.id.btn_enviar_celular)
    LinearLayout buttonEnviarCelular;

    @BindView(R.id.circular_progress_bar)
    ProgressBar circularProgressBar;
    @BindView(R.id.text_circular_progress_Bar)
    TextView textCircularProgressBar;

    @BindView(R.id.et_codigo1)
    EditText etCodigo1;
    @BindView(R.id.et_codigo2)
    EditText etCodigo2;
    @BindView(R.id.et_codigo3)
    EditText etCodigo3;
    @BindView(R.id.et_codigo4)
    EditText etCodigo4;

    @BindView(R.id.button_close)
    ImageButton buttonAceptar;
    @BindView(R.id.btnEnviarCod_actDatos_confirmar)
    TextView btnReenviarCodigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_code);
        ButterKnife.bind(this);
        setControls();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void setControls() {
        if (android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        presenter = new ActivityValidateCodePresenter(this, new ActivityValidateCodeModel());
        context = this;
        state = context.getState();
        pd = new ProgressDialog(context);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @OnClick(R.id.button_close)
    public void onClickClose(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    @OnClick(R.id.btnEnviarCod_actDatos_confirmar)
    public void onClickResendCode(View view) {
        boxTiposEnvioCodigo.setVisibility(View.VISIBLE);
        boxCamposCodigo.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_enviar_correo)
    public void onClickSendCodeMail(View view) {
        sendVerificationCode(1);
    }

    @OnClick(R.id.btn_enviar_celular)
    public void onClickSendCodePhone(View view) {
        sendVerificationCode(2);
    }

    @OnTextChanged(value = R.id.et_codigo1, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChangedCodigo1(Editable arg0) {
        etCodigo2.requestFocus();
        validateEnteredCode();
    }

    @OnTextChanged(value = R.id.et_codigo2, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChangedCodigo2(Editable arg0) {
        etCodigo3.requestFocus();
        validateEnteredCode();
    }

    @OnTextChanged(value = R.id.et_codigo3, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChangedCodigo3(Editable arg0) {
        etCodigo4.requestFocus();
        validateEnteredCode();
    }

    @OnTextChanged(value = R.id.et_codigo4, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChangedCodigo4(Editable arg0) {
        validateEnteredCode();
    }

    @OnFocusChange(R.id.et_codigo1)
    public void onFocusChangeCodigo1(View v, boolean hasFocus) {
        if(hasFocus){
            etCodigo1.requestFocus();
            if(!TextUtils.isEmpty(etCodigo1.getText())){
                etCodigo1.setText("");
                etCodigo1.requestFocus();
            }
        }
    }

    @OnFocusChange(R.id.et_codigo2)
    public void onFocusChangeCodigo2(View v, boolean hasFocus) {
        if(hasFocus){
            etCodigo2.requestFocus();
            if(!TextUtils.isEmpty(etCodigo2.getText())){
                etCodigo2.setText("");
                etCodigo2.requestFocus();
            }
        }
    }

    @OnFocusChange(R.id.et_codigo3)
    public void onFocusChangeCodigo3(View v, boolean hasFocus) {
        if(hasFocus){
            etCodigo3.requestFocus();
            if(!TextUtils.isEmpty(etCodigo3.getText())) {
                etCodigo3.setText("");
                etCodigo3.requestFocus();
            }
        }
    }

    @OnFocusChange(R.id.et_codigo4)
    public void onFocusChangeCodigo4(View v, boolean hasFocus) {
        if(hasFocus){
            etCodigo4.requestFocus();
            if(!TextUtils.isEmpty(etCodigo4.getText())){
                etCodigo4.setText("");
                etCodigo4.requestFocus();
            }
        }
    }

    @Override
    public void validateEnteredCode(){
        if(!TextUtils.isEmpty(etCodigo3.getText()) && !TextUtils.isEmpty(etCodigo4.getText())){
            String codigoIngresado = etCodigo1.getText().toString()+
                    etCodigo2.getText().toString()+
                    etCodigo3.getText().toString()+
                    etCodigo4.getText().toString();
//            if(codigoIngresado.equals(codigo.getCodigo())){
//                dialog.dismiss();
//                registrarDispositivo();
            validateVerificationCode();
//            }else{
//                mensajeCodigo.setText("Código inválido");
//                mensajeCodigo.setTextColor(Color.RED);
//            }
        }
    }

    @Override
    public void sendVerificationCode(int typeOfCode) {
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.sendVerificationCode(new RequestEnviarCodigo(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    3,
                    typeOfCode
            ));
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void resultSendVerificationCode(CodigoVerificacion codigo) {
        this.codigo = codigo;
    }

    @Override
    public void validateVerificationCode() {
        try{
            Encripcion encripcion = Encripcion.getInstance();
            String codigoIngresado = etCodigo1.getText().toString()+
                    etCodigo2.getText().toString()+
                    etCodigo3.getText().toString()+
                    etCodigo4.getText().toString();

            presenter.validateVerificationCode(new RequestValidarCodigo(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    codigo.getIdCodigo(),
                    codigoIngresado
            ));
        }catch(Exception ex){
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void registerDevice() {
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.registerDevice(new Dispositivo(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    context.getFabricante(),
                    context.getModelo(),
                    context.getIdDispositivo(),
                    Build.VERSION.SDK_INT <= Build.VERSION_CODES.O ? "" : context.getImei(),
                    Build.VERSION.SDK_INT <= Build.VERSION_CODES.O ? "" : context.getCelPrincipal(),
                    state.isHmsSystem() ? "Android HMS" : "Android GMS",
                    context.getVersionSistemaOperativo()
            ));
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void resultRegisterDevice(String idRegistroDispositivo) {
        try{
            if(!TextUtils.isEmpty(idRegistroDispositivo)){
                state.setIdDispositivoRegistrado(idRegistroDispositivo);
                setResult(RESULT_OK);
                finish();
            }else{
                showDataFetchError("Lo sentimos", "Error registrando el dispositivo, intenta de nuevo más tarde");
            }
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        onCancel(null);
    }

    @Override
    public void initializeCounter() {
        tiempoExpiracion.setTextColor(Color.parseColor("#4A4A49"));
        if (timer != null) {
            timer.cancel();
        }
        timer = new Contador(180000,1000);
        timer.start();
    }

    @Override
    public void showTextError(String textError){
        mensajeCodigo.setText(textError);
        mensajeCodigo.setTextColor(Color.RED);
    }

    @Override
    public void hideTextError() {
        mensajeCodigo.setText("");
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
    public void showBoxCodeFields() {
        boxCamposCodigo.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBoxCodeFields() {
        boxCamposCodigo.setVisibility(View.GONE);
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
                finish();
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
                finish();
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
            tiempoExpiracion.setText(String.format(
                    "Este código caducará en: %s:%s",
                    minutos < 10 ? "0" + minutos : minutos, segundos < 10 ? "0" + segundos : segundos));
        }
    }

}