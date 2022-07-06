package solidappservice.cm.com.presenteapp.front.login.FragmentForgotPassword;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.login.Request.RequestForgotPassword;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 20/09/2021.
 */
public interface FragmentForgotPasswordContract {

    interface View{
        void recoverPassword(String cedula);
        void resultRecoverPassword(String result);
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
    }

    interface Presenter{
        void recoverPassword(RequestForgotPassword body);
    }

    interface Model{
        void recoverPassword(RequestForgotPassword body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
