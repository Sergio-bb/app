package solidappservice.cm.com.presenteapp.front.menuprincipal.FragmentHome;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.banercomercial.response.ResponseBanerComercial;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.mensajes.response.ResponseObtenerMensajes;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 17/09/2021.
 */
public interface FragmentHomeContract {

    interface View{
        void fetchButtonStateAgreements();
        void showButtonAgreements();
        void hideButtonAgreements();
        void fetchCommercialBanner();
        void showCommercialBanner(ResponseBanerComercial commercialBanner);
        void hideCommercialBanner();
        void fetchMessageInbox();
        void showMessageInbox(List<ResponseObtenerMensajes> messagesInbox);
        void showCircularProgressBar();
        void hideCircularProgressBar();
        void showDataFetchError(String message);
        void showErrorTimeOut();
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchButtonStateAgreements();
        void fetchCommercialBanner(BaseRequest baseRequest);
        void fetchMessageInbox(BaseRequest baseRequest);
    }

    interface Model{
        void getCommercialBanner(BaseRequest body, final APIListener listener);
        void getMessageInbox(BaseRequest body, final APIListener listener);
        void getButtonStateAgreements(final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessCommercialBanner(Response<BaseResponse<T>> response);
        <T> void onSuccessMessageInbox(Response<BaseResponse<T>> response);
        <T> void onSuccessButtonStateAgreements(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
