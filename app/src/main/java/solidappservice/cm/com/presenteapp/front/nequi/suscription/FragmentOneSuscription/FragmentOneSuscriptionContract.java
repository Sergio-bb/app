package solidappservice.cm.com.presenteapp.front.nequi.suscription.FragmentOneSuscription;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.DatosAsociado;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

public interface FragmentOneSuscriptionContract {

    interface View{
        void fetchPersonalData();
        void processPersonalData();
        void showPersonalData(DatosAsociado data);
        void enabledButtonNext();
        void disabledButtonNext();
        void showSuscriptionPhone();
        void hideSuscriptionPhone();
        void showCircularProgressBar();
        void hideCircularProgressBar();
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchPersonalData(BaseRequest base);

    }

    interface Model{
        void getPersonalData(BaseRequest body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
