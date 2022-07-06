package solidappservice.cm.com.presenteapp.front.solicitudahorros.ActivityProductRequestDate;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

/**
 * CREADO POR MIGUEL DAVID CABEZAS 17/09/2021.
 **/
public interface ActivityProductRequestDateContract {

    interface View{
        void showYears();
        void showMonths(int selectedYear);
        void showDays(int month, int year);
        int selectMonthIndex(String month);
        void showDataFetchError(String title, String message);
    }

    interface Presenter{
    }

    interface Model{
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
