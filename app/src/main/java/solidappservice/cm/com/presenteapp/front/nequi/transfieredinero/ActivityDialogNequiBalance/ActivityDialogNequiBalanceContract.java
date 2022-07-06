package solidappservice.cm.com.presenteapp.front.nequi.transfieredinero.ActivityDialogNequiBalance;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public interface ActivityDialogNequiBalanceContract {

    interface View{
        void sendAuthorizationNequiBalance();
        void resultSendAuthorizationNequiBalance(boolean IsSuccesfulSendAuthorization);
        void initializeCounter();
//        void initializeValidateGeAuthorization();
//        void stopValidateGeAuthorization();
        void showSectionButtons();
        void hideSectionButtons();
        void showSectionExpirationTime();
        void hideSectionExpirationTime();
//        void fetchGetAuthorizationNequiBalance();
//        void resultGetAuthorizationNequiBalance(String status);
        void showCircularProgressBar(String message);
        void hideCircularProgressBar();
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void sendAuthorizationNequiBalance(BaseRequestNequi baseRequest);
//        void fetchAuthorizationNequiBalance(BaseRequestNequi baseRequest);
    }

    interface Model{
        void sendAuthorizationNequiBalance(BaseRequestNequi baseRequest, final APIListener listener);
//        void getAuthorizationNequiBalance(BaseRequestNequi baseRequest, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessSendAuthorization(Response<BaseResponseNequi<T>> response);
        <T> void onErrorSendAuthorization(Response<BaseResponseNequi<T>> response);

//        <T> void onSuccessGetAuthorization(Response<BaseResponseNequi<T>> response);
//        <T> void onErrorGetAuthorization(Response<BaseResponseNequi<T>> response);

        <T> void onExpiredToken(Response<BaseResponseNequi<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}

