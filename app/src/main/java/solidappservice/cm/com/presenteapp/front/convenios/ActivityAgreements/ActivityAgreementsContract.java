package solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements;

import java.text.ParseException;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseConvenios;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Resumen;
import solidappservice.cm.com.presenteapp.entities.convenios.response.ResponseResumen;
import solidappservice.cm.com.presenteapp.entities.nequi.dto.SuscriptionData;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiGeneral;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseSuscriptionStatus;
import solidappservice.cm.com.presenteapp.front.base.main.ActivityMainContract;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsContract;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public interface ActivityAgreementsContract {

    interface View{
        void configureSideBar();

        void validateSuscriptionNequi();
        void saveSuscriptionData(SuscriptionData datosSuscripcion, String statusSuscription);
        void getTimeExpiration();
        void resultTimeExpiration(Integer timeExpiration);
        void calculeMinutes(Integer days, Integer hour, Integer minute, Integer second);
        void getTimeOfSuscription();
        void activateButtonWarning(boolean activate);

        void setFragment(IFragmentCoordinator.Pantalla pantalla);
        void fetchAgreements();
        void showResultAgreements(Resumen resumen);
        void showContentAgreements();
        void hideContentAgreements();
        void showCircularProgressBar(String message);
        void hideCircularProgressBar();
        void showErrorWithRefresh();
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void validateSuscriptionNequi(BaseRequest body);
        void getTimeExpiration();
        void getTimeOfsuscription(BaseRequestNequi request);
        void fetchAgreements(BaseRequest baseRequest);
    }

    interface Model{
        void getSuscriptionNequi(BaseRequest baseRequest, APIListener listener);
        void getTimeExpiration(final APIListener listener);
        void getMinutesOfSuscription(BaseRequestNequi body,  final APIListener listener);
        void getAgreements(BaseRequest baseRequest, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessGetSuscriptionNequi(Response<BaseResponseNequi<T>> response);
        <T> void onErrorGetSuscriptionNequi(Response<BaseResponseNequi<T>> response);
        void onFailureGetSuscriptionNequi(Throwable t, boolean isErrorTimeOut);

        <T> void onSuccessGetTimeExpiration(Response<ResponseNequiGeneral> response);
        <T> void onErrorGetTimeExpiration(Response<ResponseNequiGeneral> response);

        <T>void onSuccessGetTimeOfSuscription(Response<BaseResponseNequi<ResponseSuscriptionStatus>> response) throws ParseException;
        <T>void onFailureGetTimeOfSuscription(Response<BaseResponseNequi<T>> response);

        <T> void onSuccessAgreements(Response<BaseResponseConvenios<ResponseResumen>> response);
        <T> void onErrorAgreements(Response<BaseResponseConvenios<ResponseResumen>> response);
        void onFailureAgreements(Throwable t, boolean isErrorTimeOut);

        <T> void onExpiredTokenConvenios(Response<BaseResponseConvenios<ResponseResumen>> response);
        <T> void onExpiredTokenNequi(Response<BaseResponseNequi<T>> response);
    }

}
