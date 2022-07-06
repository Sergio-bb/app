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
        void fetchServices();
        void showServices(List<ResponseServicios> servicios);
        void showFeaturedService1(ResponseServicios servicio);
        void showFeaturedService2(ResponseServicios servicio);
        void showSectionServices();
        void hideSectionServices();
        void showCircularProgressBar(String message);
        void hideCircularProgressBar();
        void showErrorWithRefresh();
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
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
