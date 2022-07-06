package solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentVerifyCode;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.CodigoVerificacion;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.RequestActualizarDatos;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.RequestEnviarCodigo;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.RequestEnviarCodigoPhone;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.RequestValidarCodigo;
import solidappservice.cm.com.presenteapp.entities.dispositivo.apirequest.Dispositivo;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 11/07/2021.
 */
public interface FragmentVerifyCodeContract {

    interface View{
        void sendVerificationCodeEmail();
        void sendVerificationCodePhone();
        void sendSms();
        void resendVerificationCode();
        void enableSectionVerificationCode();
        void disabledSectionVerificationCode();
        void validateVerificationCode();
        void updateInformation();
        void initializeCounter();
        void showDialogError(String title, String content);
        void showDialogCodeSent();
        void showProgressDialog(String message);
        void hideProgressDialog();
        void resultSendVerificationCode(CodigoVerificacion codigo);
        void resultRegisterDevice(String idRegistroDispositivo);
        void resultUpdatePersonalData();
        void showCircularProgressBar(String textProgressBar);
        void hideCircularProgressBar();
        void showBoxTypesCodeSend();
        void hideBoxTypesCodeSend();
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void sendVerificationCodeEmail(RequestEnviarCodigo codigo);
        void sendVerificationCodePhone(RequestEnviarCodigo codigo);
        void sendSms(RequestEnviarCodigoPhone codigo);
        void validateVerificationCode(RequestValidarCodigo codigo);
        void updatePersonalData(RequestActualizarDatos datos);
        void registerDevice(Dispositivo dispositivo);
        void resendVerificationCode();
    }

    interface Model{
        void sendVerificationCodeEmail(RequestEnviarCodigo body, final APIListener listener);
        void sendVerificationCodePhone(RequestEnviarCodigo body, final APIListener listener);
        void validateVerificationCode(RequestValidarCodigo body, final APIListener listener);
        void updatePersonalData(RequestActualizarDatos body, final APIListener listener);
        void registerDevice(Dispositivo body, final APIListener listener);
        void sendSms(RequestEnviarCodigoPhone body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessSendVerificationCodeEmail(Response<BaseResponse<T>> response);
        <T> void onSuccessSendVerificationCodePhone(Response<BaseResponse<T>> response);
        <T> void onSuccessValidateVerificationCode(Response<BaseResponse<T>> response);
        <T> void onSuccessUpdatePersonalData(Response<BaseResponse<T>> response);
        <T> void onSuccessRegisterDevice(Response<BaseResponse<T>> response);
        <T> void onSuccessSendSms(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
