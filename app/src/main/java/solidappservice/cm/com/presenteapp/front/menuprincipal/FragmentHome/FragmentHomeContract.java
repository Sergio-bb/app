package solidappservice.cm.com.presenteapp.front.menuprincipal.FragmentHome;

import java.text.ParseException;
import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.banercomercial.response.ResponseBannerComercial;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.mensajes.response.ResponseObtenerMensajes;
import solidappservice.cm.com.presenteapp.entities.nequi.dto.SuscriptionData;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiGeneral;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseSuscriptionStatus;
import solidappservice.cm.com.presenteapp.front.base.main.ActivityMainContract;
import solidappservice.cm.com.presenteapp.front.menutransacciones.FragmentTransactionsMenu.FragmentTransactionsMenuContract;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 17/09/2021.
 */
public interface FragmentHomeContract {

    interface View{
        void fetchButtonStateAgreements();
        void showButtonAgreements();
        void hideButtonAgreements();
        void fetchButtonStateResorts();
        void showButtonResorts(String urlLinkResort);
        void hideButtonResorts();

        void fetchButtonStateTransfers();
        void showButtonTransfers();
        void hideButtonTransfers();
        void fetchButtonStateSavings();
        void showButtonSavings();
        void hideButtonSavings();

        void fetchMessageInbox();
        void showMessageInbox(List<ResponseObtenerMensajes> messagesInbox);
        void showNotificationBadge();
        void showButtonMessageInbox();
        void hideButtonMessageInbox();
        void showCircularProgressBarOptions();
        void hideCircularProgressBarOptions();
        void showContentOptions();
        void hideContentOptions();

        void validateSuscriptionNequi();
        void saveSuscriptionData(SuscriptionData datosSuscripcion, String statusSuscription);
        void getTimeExpiration();
        void resultTimeExpiration(Integer timeExpiration);
        void calculeMinutes(Integer days, Integer hour, Integer minute, Integer second);
        void getTimeOfSuscription();
        void activateAlert(boolean activate);

        void fetchCommercialBanner();
        void showCommercialBanner(List<ResponseBannerComercial> commercialBanners);
        void hideCommercialBanner();
        void showCircularProgressBar();
        void hideCircularProgressBar();

        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchButtonStateAgreements();
        void fetchButtonStateResorts();
        void fetchMessageInbox(BaseRequest baseRequest);

        void fetchButtonStateTransfers();
        void fetchButtonStateSavings();

        void fetchCommercialBanner(BaseRequest baseRequest);

        void validateSuscriptionNequi(BaseRequest body);
        void getTimeExpiration();
        void getTimeOfsuscription(BaseRequestNequi baseRequest);
    }

    interface Model{
        void getButtonStateAgreements(final APIListener listener);
        void getButtonStateResorts(final APIListener listener);
        void getMessageInbox(BaseRequest body, final APIListener listener);

        void getButtonStateTransfers(final APIListener listener);
        void getButtonStateSavings(final APIListener listener);

        void getCommercialBanner(BaseRequest body, final APIListener listener);

        void getSuscriptionNequi(BaseRequest baseRequest, APIListener listener);
        void getTimeExpiration(final APIListener listener);
        void getMinutesOfSuscription(BaseRequestNequi body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessButtonStateAgreements(Response<BaseResponse<T>> response);
        <T> void onErrorButtonStateAgreements(Response<BaseResponse<T>> response);
        <T> void onSuccessButtonStateResorts(Response<BaseResponse<T>> response);
        <T> void onErrorButtonStateResorts(Response<BaseResponse<T>> response);

        <T> void onSuccessButtonStateTransfers(Response<BaseResponse<T>> response);
        <T> void onErrorButtonStateTransfers(Response<BaseResponse<T>> response);
        <T> void onSuccessButtonStateSavings(Response<BaseResponse<T>> response);
        <T> void onErrorButtonStateSavings(Response<BaseResponse<T>> response);
        <T> void onSuccessMessageInbox(Response<BaseResponse<T>> response);
        <T> void onErrorMessageInbox(Response<BaseResponse<T>> response);

        <T> void onSuccessCommercialBanner(Response<BaseResponse<T>> response);
        <T> void onErrorCommercialBanner(Response<BaseResponse<T>> response);

        <T> void onSuccessGetTimeExpiration(Response<ResponseNequiGeneral> response);
        <T> void onErrorGetTimeExpiration(Response<ResponseNequiGeneral> response);
        <T> void onSuccessGetSuscriptionNequi(Response<BaseResponseNequi<T>> response);
        <T> void onErrorGetSuscriptionNequi(Response<BaseResponseNequi<T>> response);
        void onFailureGetSuscriptionNequi(Throwable t);
        <T>void onSuccessGetTimeOfSuscription(Response<BaseResponseNequi<ResponseSuscriptionStatus>> response) throws ParseException;
        <T>void onFailureGetTimeOfSuscription(Response<BaseResponseNequi<T>> response);

        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onExpiredTokenNequi(Response<BaseResponseNequi<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
