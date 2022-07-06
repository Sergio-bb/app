package solidappservice.cm.com.presenteapp.front.mismensajes.FragmentInbox;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.mensajes.response.ResponseObtenerMensajes;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 15/09/2021.
 */
public class FragmentInboxPresenter implements FragmentInboxContract.Presenter,
        FragmentInboxContract.APIListener{

    FragmentInboxView view;
    FragmentInboxModel model;

    public FragmentInboxPresenter(@NonNull FragmentInboxView view, @NonNull FragmentInboxModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchMessages(BaseRequest baseRequest) {
        view.showCircularProgressBar("Actualizando mensajes...");
        model.getMessages(baseRequest, this);
    }

    @Override
    public <T> void onSuccess(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        try{
            List<ResponseObtenerMensajes> messages = (List<ResponseObtenerMensajes>) response.body().getResultado();
            if(messages != null && messages.size()>0){
                view.hideTextFragmentExpanded();
                view.showMessages(messages);
            }else{
                view.hideMessages();
                view.showTextFragmentExpanded();
            }
        }catch (Exception ex){
            view.showDataFetchError("");
        }
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        view.showExpiredToken(response.body().getErrorToken());
    }

    @Override
    public <T> void onError(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        if(response != null){
            view.showDataFetchError(response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("");
        }
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideCircularProgressBar();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("");
        }
    }

}
