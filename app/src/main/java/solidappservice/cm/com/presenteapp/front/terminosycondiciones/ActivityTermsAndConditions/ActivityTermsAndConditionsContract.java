package solidappservice.cm.com.presenteapp.front.terminosycondiciones.ActivityTermsAndConditions;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.tyc.request.RequestAceptaTyC;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 20/09/2021.
 */
public interface ActivityTermsAndConditionsContract {

    interface View{
        void showTermsAndConditions();
        void registerAcceptedTermsAndConditions();
        void resultRegisterAcceptedTermsAndConditions();
        String getIp();
        void enableAcceptButton();
        void disabledAcceptButton();
        void enableCancelButton();
        void disabledCancelButton();
        void showDataFetchError(String message);
        void showErrorTimeOut();
        void showExpiredToken(String message);
    }

    interface Presenter{
        void registerAcceptedTermsAndConditions(RequestAceptaTyC baseRequest);
    }

    interface Model{
        void registerAcceptedTermsAndConditions(RequestAceptaTyC body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
