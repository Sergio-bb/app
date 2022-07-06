package solidappservice.cm.com.presenteapp.front.nequi.suscription.FragmentSecondSuscription;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.DatosAsociado;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestSendSubscription;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultaSuscripcion;

public interface FragmentSecondSuscriptionContract {

    interface View{
        void showSendingRequet();
        void hiddenSendingRequest();
        void processPersonalData();
        void fetchPersonalData();
        void showPersonalData(DatosAsociado datos);
        void sendSubscriptionNotificacion();
        void resultSendSubscriptionNotificacion(boolean isSendSuscripcion);
        void showResumePersonalData();
        void hideResumePersonalData();
        void enableButtonSend();
        void disabledButtonSend();
        void showButtonSend();
        void hideButtonSend();
        void showCircularProgressBar();
        void hideCircularProgressBar();
        void showCircularProgressBarSuscription();
        void hideCircularProgressBarSuscription();
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchPersonalData(BaseRequest base);
        void sendSubscriptionNotificacion(RequestSendSubscription base);
    }

    interface Model{
        void getPersonalData(BaseRequest body, final APIListener listener);
        void sendSubscriptionNotificacion(RequestSendSubscription body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessSendSubscriptionNotificacion(Response<BaseResponseNequi<T>> response);
        <T> void onErrorSendSubscriptionNotificacion(Response<BaseResponseNequi<T>> response);
        void onFailureSendSubscriptionNotificacion(Throwable t, boolean isErrorTimeOut);

       <T> void onSuccessPersonalData(Response<BaseResponse<T>> response);
       <T> void onError(Response<BaseResponse<T>> response);
       void onFailure(Throwable t, boolean isErrorTimeOut);

       <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onExpiredTokenNequi(Response<BaseResponseNequi<T>> response);
    }

}
