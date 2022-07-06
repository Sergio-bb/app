package solidappservice.cm.com.presenteapp.front.mismensajes.FragmentReadMessage;

import android.util.Log;

import androidx.annotation.NonNull;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.mensajes.request.RequestActualizarMensaje;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 15/09/2021.
 */
public class FragmentReadMessagePresenter implements FragmentReadMessageContract.Presenter,
        FragmentReadMessageContract.APIListener{

    FragmentReadMessageView view;
    FragmentReadMessageModel model;

    public FragmentReadMessagePresenter(@NonNull FragmentReadMessageView view, @NonNull FragmentReadMessageModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchUpdateStatusMessage(RequestActualizarMensaje request) {
        model.updateStatusMessage(request, this);
    }

    @Override
    public <T> void onSuccess(Response<BaseResponse<T>> response) {
        try{
            String resultUpdateMessage = (String) response.body().getResultado();
            view.showUpdateResultStatusMessages();
        }catch (Exception ex){
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.showExpiredToken(response.body().getErrorToken());
    }

    @Override
    public <T> void onError(Response<BaseResponse<T>> response) {
        if(response != null){
            view.showDataFetchError("Lo sentimos", response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }

}

