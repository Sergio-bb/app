package solidappservice.cm.com.presenteapp.front.solicitudahorros.ActivityProductRequestDate;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.solicitudahorros.request.RequestEnviarSolicitudAhorro;
import solidappservice.cm.com.presenteapp.entities.solicitudahorros.response.ResponseTiposAhorro;

/**
 * CREADO POR MIGUEL DAVID CABEZAS 17/09/2021.
 **/
public interface ActivityProductRequestDateContract {

    interface View{
        void showYears();
        void showMonths(int selectedYear);
        void showDays(int month, int year);
        int selectMonthIndex(String month);
    }

    interface Presenter{
    }

    interface Model{
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t);
    }

}
