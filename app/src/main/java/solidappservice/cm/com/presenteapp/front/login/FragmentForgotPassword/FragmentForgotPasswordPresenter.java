package solidappservice.cm.com.presenteapp.front.login.FragmentForgotPassword;

import android.util.Log;

import androidx.annotation.NonNull;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.login.Request.RequestForgotPassword;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 20/09/2021.
 */
public class FragmentForgotPasswordPresenter implements FragmentForgotPasswordContract.Presenter,
        FragmentForgotPasswordContract.APIListener{

    FragmentForgotPasswordView view;
    FragmentForgotPasswordModel model;

    public FragmentForgotPasswordPresenter(@NonNull FragmentForgotPasswordView view, @NonNull FragmentForgotPasswordModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void recoverPassword(RequestForgotPassword body) {
        view.showProgressDialog("Enviando solicitud...");
        model.recoverPassword(body,this);
    }

    @Override
    public <T> void onSuccess(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            String result = (String)response.body().getResultado();
            if(result != null){
                view.resultRecoverPassword(result);
            }else{
                view.showDataFetchError("");
            }
        }catch (Exception ex){
            view.showDataFetchError("");
        }
    }

    @Override
    public <T> void onError(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        if(response != null){
            view.showDataFetchError(response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("");
        }
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideProgressDialog();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("");
        }
    }

}
