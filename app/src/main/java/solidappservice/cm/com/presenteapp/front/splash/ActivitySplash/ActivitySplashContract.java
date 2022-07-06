package solidappservice.cm.com.presenteapp.front.splash.ActivitySplash;

import android.graphics.Bitmap;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.mensajesbanner.ResponseMensajesBanner;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.entities.tyc.response.ReponseTyC;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 19/09/2021.
 */
public interface ActivitySplashContract {

    interface View{
        void initialStateVariables();
        void startApp();
        void fetchBannerMessages();
        void loadBannerMessages(List<ResponseMensajesBanner> bannerMessages);
        void fetchLoginImage();
        void loadLoginImage(String loginImage);
        void fetchAppVersion();
        void loadAppVersion(String appVersion);
        void fetchTermsAndConditions();
        void loadTermsAndConditions(ReponseTyC terminos);
        void fetchResponseMessages();
        void loadResponseMessages(List<ResponseMensajesRespuesta> responseMessages);
        void loadIsHmsAvailable(boolean isHmsSystem);
        Bitmap drawableFromUrl(String url) throws IOException;
        void showCircularProgressBar();
        void hideCircularProgressBar();
        void tempThread(int delay);
    }

    interface Presenter{
        void fetchBannerMessages();
        void fetchLoginImage();
        void fetchAppVersion();
        void fetchTermsAndConditions();
        void fetchResponseMessages();
    }

    interface Model{
        void getBannerMessages(final APIListener listener);
        void getLoginImage(final APIListener listener);
        void getAppVersion(final APIListener listener);
        void getTermsAndConditions(final APIListener listener);
        void getResponseMessages(final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessBannerMessages(Response<BaseResponse<T>> response);
        <T> void onSuccessLoginImage(Response<BaseResponse<T>> response);
        <T> void onSuccessAppVersion(Response<BaseResponse<T>> response);
        <T> void onSuccessTermsAndConditions(Response<BaseResponse<T>> response);
        <T> void onSuccessResponseMessages(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t);
    }

}
