package solidappservice.cm.com.presenteapp.front.login.ActivityValidateCode;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.CodigoVerificacion;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.RequestActualizarDatos;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.RequestEnviarCodigo;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.RequestEnviarCodigoPhone;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.RequestValidarCodigo;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.dispositivo.apirequest.Dispositivo;
import solidappservice.cm.com.presenteapp.entities.tyc.request.RequestAceptaTyC;
import solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentVerifyCode.FragmentVerifyCodeContract;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 21/09/2021.
 */
public interface ActivityValidateCodeContract {

    interface View{
        void validateEnteredCode();
        void sendVerificationCode(int typeOfCode);
        void resultSendVerificationCode(CodigoVerificacion codigo);
        void validateVerificationCode();
        void registerDevice();
        void resultRegisterDevice(String idRegistroDispositivo);
        void initializeCounter();
        void showTextError(String textError);
        void hideTextError();
        void showBoxTypesCodeSend();
        void hideBoxTypesCodeSend();
        void showBoxCodeFields();
        void hideBoxCodeFields();
        void showCircularProgressBar(String textProgressBar);
        void hideCircularProgressBar();
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void sendVerificationCode(RequestEnviarCodigo codigo);
        void validateVerificationCode(RequestValidarCodigo codigo);
        void registerDevice(Dispositivo dispositivo);
    }

    interface Model{
        void sendVerificationCode(RequestEnviarCodigo body, final APIListener listener);
        void validateVerificationCode(RequestValidarCodigo body, final APIListener listener);
        void registerDevice(Dispositivo body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessSendVerificationCode(Response<BaseResponse<T>> response);
        <T> void onSuccessValidateVerificationCode(Response<BaseResponse<T>> response);
        <T> void onSuccessRegisterDevice(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}