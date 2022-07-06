package solidappservice.cm.com.presenteapp.front.splash.ActivitySplash;

import android.graphics.Bitmap;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.mensajesbanner.ResponseMensajesBanner;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiGeneral;
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
        void fetchResponseMessages();
        void loadResponseMessages(List<ResponseMensajesRespuesta> responseMessages);
        void fetchBannerMessages();
        void loadBannerMessages(List<ResponseMensajesBanner> bannerMessages);
        void fetchLoginImage();
        void loadLoginImage(String loginImage);
        void fetchAppVersion();
        void loadAppVersion(String appVersion);
        void fetchTermsAndConditions();
        void loadTermsAndConditions(ReponseTyC terminos);
        void fetchButtonStatePaymentQR();
        void isActiveButtonPaymentQR(boolean isActiveButtonQR);
        void fetchButtonStatePaymentDispersiones();
        void isActiveButtonPaymentDispersiones(boolean isActiveButtonDispersion);
        void fetchStateButtonPaymentSuscriptions();
        void isActiveButtonPaymentSuscriptions(boolean isActiveButtonSuscriptions);
        void fetchStateNequiBalance();
        void isActiveStateNequiBalance(boolean isActiveNequiBalance);
        void fetchStateSuscriptions();
        void isActiveStateSuscriptions(boolean isActiveSuscription);
        void loadIsHmsAvailable(boolean isHmsSystem);
        Bitmap drawableFromUrl(String url) throws IOException;
        void showCircularProgressBar();
        void hideCircularProgressBar();
        void tempThread(int delay);
    }

    interface Presenter{
        void fetchResponseMessages();
        void fetchBannerMessages();
        void fetchLoginImage();
        void fetchAppVersion();
        void fetchTermsAndConditions();
        void fetchButtonStatePaymentQR();
        void fetchButtonStatePaymentDispersiones();
        void fetchStateButtonPaymentSuscriptions();
        void fetchStateNequiBalance();
        void fetchStateSuscriptions();

    }

    interface Model{
        void getResponseMessages(final APIListener listener);
        void getBannerMessages(final APIListener listener);
        void getLoginImage(final APIListener listener);
        void getAppVersion(final APIListener listener);
        void getTermsAndConditions(final APIListener listener);
        void getButtonStatePaymentQR(final APIListener listener);
        void getButtonStatePaymentDispersiones(final APIListener listener);
        void getStateButtonPaymentSuscriptions(final APIListener listener);
        void getStateNequiBalance(final APIListener listener);
        void getStateSuscriptions(final APIListener listener);
    }

    interface APIListener{

        <T> void onSuccessResponseMessages(Response<BaseResponse<T>> response);
        <T> void onErrorResponseMessages(Response<BaseResponse<T>> response);

        <T> void onSuccessBannerMessages(Response<BaseResponse<T>> response);
        <T> void onErrorBannerMessages(Response<BaseResponse<T>> response);

        <T> void onSuccessLoginImage(Response<BaseResponse<T>> response);
        <T> void onErrorLoginImage(Response<BaseResponse<T>> response);

        <T> void onSuccessAppVersion(Response<BaseResponse<T>> response);
        <T> void onErrorAppVersion(Response<BaseResponse<T>> response);

        <T> void onSuccessTermsAndConditions(Response<BaseResponse<T>> response);
        <T> void onErrorTermsAndConditions(Response<BaseResponse<T>> response);

        <T>  void onSuccessButtonStatePaymentQR(Response<ResponseNequiGeneral> response);
        <T>  void onErrorButtonStatePaymentQR(Response<ResponseNequiGeneral> response);

        <T>  void onSuccessButtonStatePaymentDispersiones(Response<ResponseNequiGeneral> response);
        <T>  void onErrorButtonStatePaymentDispersiones(Response<ResponseNequiGeneral> response);

        <T> void onSuccessButtonStatePaymentSuscriptions(Response<ResponseNequiGeneral> response);
        <T> void onErrorButtonStatePaymentSuscriptions(Response<ResponseNequiGeneral> response);

        <T> void onSuccessStateNequiBalance(Response<ResponseNequiGeneral> response);
        <T> void onErrorStateNequiBalance(Response<ResponseNequiGeneral> response);

        <T> void onSuccessStateSuscriptions(Response<ResponseNequiGeneral> response);
        <T> void onErrorStateSuscriptions(Response<ResponseNequiGeneral> response);
    }

}
