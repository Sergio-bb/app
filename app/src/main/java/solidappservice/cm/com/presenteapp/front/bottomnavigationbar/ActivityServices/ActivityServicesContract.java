package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityServices;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponseServicios;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 28/08/2021
 */
public interface ActivityServicesContract {

    interface View{
        void showFeaturedService1(ResponseServicios servicio);
        void showFeaturedService2(ResponseServicios servicio);
        void showServices(List<ResponseServicios> servicios);
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showDataFetchError(String message);
        void showErrorTimeOut();
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchServices();
    }

    interface Model{
        void getServices(final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
