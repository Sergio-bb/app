package solidappservice.cm.com.presenteapp.front.mismensajes.FragmentReadMessage;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.mensajes.request.RequestActualizarMensaje;
import solidappservice.cm.com.presenteapp.entities.mensajes.response.ResponseObtenerMensajes;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 15/09/2021.
 */
public interface FragmentReadMessageContract {

    interface View{
        void showMessage(ResponseObtenerMensajes inbox);
        void fetchUpdateStatusMessage(String idMessage);
        void showUpdateResultStatusMessages();
        void showDataFetchError(String message);
        void showErrorTimeOut();
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchUpdateStatusMessage(RequestActualizarMensaje request);
    }

    interface Model{
        void updateStatusMessage(RequestActualizarMensaje body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
