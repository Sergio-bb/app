package solidappservice.cm.com.presenteapp.front.login.FragmentLogin;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.banercomercial.response.ResponseBanerComercial;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.dispositivo.apirequest.Dispositivo;
import solidappservice.cm.com.presenteapp.entities.dispositivo.apiresponse.ResponseValidarDispositivo;
import solidappservice.cm.com.presenteapp.entities.login.Request.RequestLogin;
import solidappservice.cm.com.presenteapp.entities.login.Response.Usuario;
import solidappservice.cm.com.presenteapp.entities.mensajes.response.ResponseObtenerMensajes;
import solidappservice.cm.com.presenteapp.entities.mensajesbanner.ResponseMensajesBanner;
import solidappservice.cm.com.presenteapp.front.splash.ActivitySplash.ActivitySplashContract;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 20/09/2021.
 */
public interface FragmentLoginContract {

    interface View{
        void validateLogin(String usuario, String clave);
        void resultValidateLogin(Usuario usuario);
        void fetchBannerMessages();
        void loadBannerMessages(List<ResponseMensajesBanner> bannerMessages);
        void setPageAdapter();
        void fetchLoginImage();
        void loadLoginImage(String loginImage);
        void validateRegisterDevice();
        void resultValidateRegisterDevice(ResponseValidarDispositivo validateDevice);
        void showMessageUpdateApp(String versionCodeFirebase);
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showDataFetchError(String message);
        void showErrorTimeOut();
    }

    interface Presenter{
        void validateLogin(RequestLogin request);
        void fetchBannerMessages();
        void fetchLoginImage();
        void validateRegisterDevice(Dispositivo dispositivo);
    }

    interface Model{
        void validateLogin(RequestLogin body, final APIListener listener);
        void getBannerMessages(final APIListener listener);
        void getLoginImage(final APIListener listener);
        void validateRegisterDevice(Dispositivo body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessValidateLogin(RequestLogin login, Response<BaseResponse<T>> response);
        <T> void onErrorValidateLogin(Response<BaseResponse<T>> response);
        <T> void onSuccessBannerMessages(Response<BaseResponse<T>> response);
        <T> void onSuccessLoginImage(Response<BaseResponse<T>> response);
        <T> void onSuccessValidateRegisterDevice(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
