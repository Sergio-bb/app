package solidappservice.cm.com.presenteapp.front.centrosvacacionales.FragmentResortsSolicity;

import android.os.Bundle;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.centrosvacacionales.request.RequestSolicitarCentroVacacional;
import solidappservice.cm.com.presenteapp.entities.centrosvacacionales.response.ResponseCentroVacacional;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.centrosvacacionales.response.ResponseDetalleCentroVacacional;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 16/06/2021.
 */
public interface FragmentResortsSolicityContract {

    interface View{
        void fetchResorts();
        void showResorts(List<ResponseCentroVacacional> inbox);
        void showDetailResort(ResponseCentroVacacional centroVacacional);
        void showDatePicker(Bundle bundle);
        void validateData();
        void solicityResort(ResponseCentroVacacional resort, ResponseDetalleCentroVacacional detail, Integer days,
                            Integer numberOfAdults, Integer numberOfKids);
        void showResultSolicityResort(String result);
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showDataFetchError(String message);
        void showErrorTimeOut();
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchResorts(BaseRequest baseRequest);
        void solicityResort(RequestSolicitarCentroVacacional request);
    }

    interface Model{
        void getResorts(BaseRequest body, final APIListener listener);
        void solicityResort(RequestSolicitarCentroVacacional request, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessResorts(Response<BaseResponse<T>> response);
        <T> void onSuccessSolicityResorts(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
