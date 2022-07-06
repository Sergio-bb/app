package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityGeoreferencing.ActivityPointsAttention;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponseLocationsAgencies;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 28/08/2021.
 */
public interface ActivityPointsAttentionContract {

    interface View{
        void showPointAttention(String nombrePunto, ResponseLocationsAgencies agencia);
        String showDescriptionAgencie(List<ResponseLocationsAgencies.Contacto> contactos);
        void showDataFetchError(String title, String message);
    }

    interface Presenter{
        void getPointAttention(String nombreAgencia, List<ResponseLocationsAgencies> agencias);
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
