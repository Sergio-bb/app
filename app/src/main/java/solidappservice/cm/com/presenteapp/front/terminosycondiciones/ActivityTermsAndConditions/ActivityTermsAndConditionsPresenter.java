package solidappservice.cm.com.presenteapp.front.terminosycondiciones.ActivityTermsAndConditions;

import android.util.Log;

import androidx.annotation.NonNull;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.tyc.request.RequestAceptaTyC;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 20/09/2021.
 */
public class ActivityTermsAndConditionsPresenter implements ActivityTermsAndConditionsContract.Presenter,
        ActivityTermsAndConditionsContract.APIListener{

    ActivityTermsAndConditionsView view;
    ActivityTermsAndConditionsModel model;

    public ActivityTermsAndConditionsPresenter(@NonNull ActivityTermsAndConditionsView view, @NonNull ActivityTermsAndConditionsModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void registerAcceptedTermsAndConditions(RequestAceptaTyC request) {
        view.disabledAcceptButton();
        view.disabledCancelButton();
        model.registerAcceptedTermsAndConditions(request, this);
    }

    @Override
    public <T> void onSuccess(Response<BaseResponse<T>> response) {
        try{
            String result = (String) response.body().getResultado();
            if(result != null && result.equals("OK")){
                view.resultRegisterAcceptedTermsAndConditions();
            }else{
                view.enableAcceptButton();
                view.enableCancelButton();
            }
        }catch (Exception ex){
            view.enableAcceptButton();
            view.enableCancelButton();
        }
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.enableAcceptButton();
        view.enableCancelButton();
        view.showExpiredToken(response.body().getErrorToken());
    }

    @Override
    public <T> void onError(Response<BaseResponse<T>> response) {
        view.enableAcceptButton();
        view.enableCancelButton();
        if(response != null){
            view.showDataFetchError("Lo sentimos", response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.enableAcceptButton();
        view.enableCancelButton();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }

}
