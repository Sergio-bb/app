package solidappservice.cm.com.presenteapp.front.nequi.transfieredinero.ActivityDialogNequiBalance;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;


/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 31/07/2018.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public class ActivityDialogNequiBalanceView extends ActivityBase implements ActivityDialogNequiBalanceContract.View{

    private ActivityDialogNequiBalancePresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private Contador timer;
    private boolean isSuccesfulSendAuthorization;
//    private Handler getAuthorizationNequiBalanceHandler = new IncomingHandler();

    @BindView(R.id.layoutButtons)
    LinearLayout layoutButtons;
    @BindView(R.id.layoutTiempoAutorizacion)
    LinearLayout layoutTiempoAutorizacion;
    @BindView(R.id.btnEnviarSolicitud)
    Button btnEnviarSolicitud;
    @BindView(R.id.btnAhoraNo)
    Button btnAhoraNo;
    @BindView(R.id.buttonClose)
    ImageButton buttonClose;
    @BindView(R.id.counter_timer)
    TextView counterTimer;

    @BindView(R.id.layout_circular_progress_bar)
    LinearLayout layoutCircularProgressBar;
    @BindView(R.id.circular_progress_bar)
    ProgressBar circularProgressBar;
    @BindView(R.id.text_circular_progress_Bar)
    TextView textCircularProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pop_up_nequi_get_balance);
        ButterKnife.bind(this);
        setControls();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void setControls() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        presenter = new ActivityDialogNequiBalancePresenter(this, new ActivityDialogNequiBalanceModel());
        context = this;
        state = context.getState();
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @OnClick(R.id.btnAhoraNo)
    public void onClickAhoraNo() {
//        setResult(RESULT_CANCELED);
//        stopValidateGeAuthorization();
        finish();
    }

    @OnClick(R.id.btnEnviarSolicitud)
    public void onClickEnviarSolicitud() {
        sendAuthorizationNequiBalance();
    }

    @OnClick(R.id.buttonClose)
    public void onClickButtonClose(){
//        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void sendAuthorizationNequiBalance(){
        try {
            presenter.sendAuthorizationNequiBalance(new BaseRequestNequi(
                Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
                state.getUsuario().getToken(),
                state.getDatosSuscripcion().getTelefonoSuscripcion()
            ));
        } catch (Exception ex) {
//            setResult(GlobalState.ERROR_AUTHORIZATION_NEQUI_BALANCE); //ERROR
            finish();
        }
    }

    @Override
    public void resultSendAuthorizationNequiBalance(boolean isSuccesfulSendAuthorization){
        if(isSuccesfulSendAuthorization){
            this.isSuccesfulSendAuthorization = true;
        }else{
//            setResult(GlobalState.ERROR_AUTHORIZATION_NEQUI_BALANCE); //ERROR
//            stopValidateGeAuthorization();
            finish();
        }
    }

//    @Override
//    public void fetchGetAuthorizationNequiBalance(){
//        try {
//            presenter.fetchAuthorizationNequiBalance(new BaseRequestNequi(
//                    "",
//                    state.getUsuario().getCedula()
//            ));
//        } catch (Exception ex) {
//            setResult(GlobalState.ERROR_AUTHORIZATION_NEQUI_BALANCE); //ERROR
//            finish();
//        }
//    }
//
//    @Override
//    public void resultGetAuthorizationNequiBalance(String status){
//        switch(status){
//            case "1":
//                if (timer != null) {
//                    timer.onFinish();
//                }
//                setResult(RESULT_OK);
//                stopValidateGeAuthorization();
//                finish();
//                break;
//            case "2":
//                if (timer != null) {
//                    timer.onFinish();
//                }
//                setResult(GlobalState.REFUSED_AUTHORIZATION_NEQUI_BALANCE);
//                stopValidateGeAuthorization();
//                finish();
//        }
//    }

    @Override
    public void initializeCounter() {
        counterTimer.setTextColor(Color.parseColor("#4A4A49"));
        if (timer != null) {
            timer.cancel();
        }
        timer = new Contador(900000,1000);
        timer.start();
    }

    @Override
    public void showSectionExpirationTime() {
        layoutTiempoAutorizacion.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSectionExpirationTime() {
        layoutTiempoAutorizacion.setVisibility(View.GONE);
    }

    @Override
    public void showSectionButtons() {
        layoutButtons.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSectionButtons() {
        layoutButtons.setVisibility(View.GONE);
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
                onBackPressed();
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
//                stopValidateGeAuthorization();
                onBackPressed();
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
//                stopValidateGeAuthorization();
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
            counterTimer.setText("00:00");
            counterTimer.setTextColor(Color.RED);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long minutos =  millisUntilFinished / 60000;
            long segundos = millisUntilFinished % 60000 / 1000;
            counterTimer.setText(String.format("%s:%s", (minutos < 10 ? "0" + minutos : minutos), segundos < 10 ? "0" + segundos : segundos));
        }
    }

//    @Override
//    public void initializeValidateGeAuthorization() {
//        getAuthorizationNequiBalanceHandler.postDelayed(fetchGetAuthorizationNequiBalanceCallBack, 10000);
//    }
//
//    @Override
//    public void stopValidateGeAuthorization() {
//        getAuthorizationNequiBalanceHandler.removeCallbacks(fetchGetAuthorizationNequiBalanceCallBack);
//    }
//
//    private final Runnable fetchGetAuthorizationNequiBalanceCallBack = new Runnable() {
//        @Override
//        public void run() {
//            fetchGetAuthorizationNequiBalance();
//        }
//    };
//
//    private class IncomingHandler extends Handler {
//        IncomingHandler() {
//        }
//        @Override
//        public void handleMessage(Message msg) {
//            Log.d("IncomingHandler", msg.toString());
//        }
//    }

}
