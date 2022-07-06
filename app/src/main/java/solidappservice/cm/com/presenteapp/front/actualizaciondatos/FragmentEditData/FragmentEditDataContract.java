package solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentEditData;


import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.DatosAsociado;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseFormatoDirecciones;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseUbicaciones;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 11/07/2021.
 */
public interface FragmentEditDataContract {

    interface View{
        void fetchPersonalData();
        void fetchLocations();
        void fetchAddressFormat();
        void processPersonalData();
        void showPersonalData(DatosAsociado data);
        void showLocations(ResponseUbicaciones locations);
        void showAddressFormat(ResponseFormatoDirecciones addressFormat);
        void showContentEditData();
        void hideContentEditData();
        void showCircularProgressBar(String message);
        void hideCircularProgressBar();
        void showErrorWithRefresh();
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchPersonalData(BaseRequest baseRequest);
        void fetchLocations();
        void fetchAddressFormat();
    }

    interface Model{
        void getPersonalData(BaseRequest body, final APIListener listener);
        void getLocations(final APIListener listener);
        void getAddressFormat(final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessPersonalData(Response<BaseResponse<T>> response);
        <T> void onSuccessLocations(Response<BaseResponse<T>> response);
        <T> void onSuccessAddressFormat(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
