package solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentValidateData;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.DatosAsociado;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.RequestActualizarDatos;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 11/07/2021.
 */
public interface FragmentValidateDataContract {

    interface View{
        void fetchPersonalData();
        void updatePersonalData();
        void resultUpdatePersonalData();
        void processPersonalData();
        void showPersonalData(DatosAsociado data);
        void showSectionValidateData();
        void hideSectionValidateData();
        void showCircularProgressBar(String message);
        void hideCircularProgressBar();
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showErrorWithRefresh();
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchPersonalData(BaseRequest base);
        void updatePersonalData(RequestActualizarDatos actualizarDatos);
    }

    interface Model{
        void getPersonalData(BaseRequest body, final APIListener listener);
        void updatePersonalData(RequestActualizarDatos body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessPersonalData(Response<BaseResponse<T>> response);
        <T> void onSuccessUpdatePersonalData(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
