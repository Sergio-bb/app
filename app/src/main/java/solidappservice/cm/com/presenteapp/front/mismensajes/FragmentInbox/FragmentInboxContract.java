package solidappservice.cm.com.presenteapp.front.mismensajes.FragmentInbox;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.mensajes.response.ResponseObtenerMensajes;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 15/09/2021.
 */
public interface FragmentInboxContract {

    interface View{
        void fetchMessages();
        void showMessages(List<ResponseObtenerMensajes> inbox);
        void hideMessages();
        void showTextFragmentExpanded();
        void hideTextFragmentExpanded();
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showCircularProgressBar(String message);
        void hideCircularProgressBar();
        void showDataFetchError(String message);
        void showErrorTimeOut();
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchMessages(BaseRequest baseRequest);
    }

    interface Model{
        void getMessages(BaseRequest body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
