package solidappservice.cm.com.presenteapp.front.nequi.suscription.ActivitySuscription;

import java.text.ParseException;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiGeneral;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseSuscriptionStatus;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsContract;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;

public interface ActivitySuscriptionContract {

    interface View{
        void showFragmentSuscripcion(IFragmentCoordinator.Pantalla pantalla);
        void validateSuscriptionNequi();
        void resultStatusSuscription(String status);
        void getTimeExpiration();
        void resultTimeExpiration(Integer timeExpiration);
        void calculeMinutes(Integer days, Integer hour, Integer minute, Integer second);
        void getTimeOfSuscription();

        void showContentSuscription();
        void hideContentSuscription();

        void showCircularProgressBar(String message);
        void hideCircularProgressBar();

        void showExpiredToken(String message);
    }

    interface Presenter{
        void validateSuscriptionNequi(BaseRequest body);
        void getTimeExpiration();
        void getTimeOfsuscription(BaseRequestNequi request);
        void loadFragmentSuscripcionNequi(IFragmentCoordinator.Pantalla pantalla);
    }

    interface Model{
        void getSuscriptionNequi(BaseRequest baseRequest, final APIListener listener);
        void getTimeExpiration(final APIListener listener);
        void getMinutesOfSuscription(BaseRequestNequi body,  final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessGetSuscriptionNequi(Response<BaseResponseNequi<T>> response);
        <T> void onErrorGetSuscriptionNequi(Response<BaseResponseNequi<T>> response);
        void onFailureGetSuscriptionNequi(Throwable t, boolean isErrorTimeOut);

        <T> void onSuccessGetTimeExpiration(Response<ResponseNequiGeneral> response);
        <T> void onErrorGetTimeExpiration(Response<ResponseNequiGeneral> response);

        <T>void onSuccessGetTimeOfSuscription(Response<BaseResponseNequi<ResponseSuscriptionStatus>> response) throws ParseException;
        <T>void onFailureGetTimeOfSuscription(Response<BaseResponseNequi<T>> response);

        <T> void onExpiredTokenNequi(Response<BaseResponseNequi<T>> response);
    }

}