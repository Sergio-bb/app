package solidappservice.cm.com.presenteapp.front.nequi.transfieredinero.ActivityDialogNequiBalance;

import androidx.annotation.NonNull;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseGetAuthorizacionBalance;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseSendAuthorizacionBalance;

public class ActivityDialogNequiBalancePresenter implements ActivityDialogNequiBalanceContract.Presenter, ActivityDialogNequiBalanceContract.APIListener{

    ActivityDialogNequiBalanceView view;
    ActivityDialogNequiBalanceModel model;

    public ActivityDialogNequiBalancePresenter(@NonNull ActivityDialogNequiBalanceView view, @NonNull ActivityDialogNequiBalanceModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void sendAuthorizationNequiBalance(BaseRequestNequi baseRequest){
        view.hideSectionButtons();
        view.showCircularProgressBar("Enviando autorización");
        model.sendAuthorizationNequiBalance(baseRequest, this);
    }
    @Override
    public <T> void onSuccessSendAuthorization(Response<BaseResponseNequi<T>> response) {
        try{
            BaseResponseNequi<ResponseSendAuthorizacionBalance> sendAutorizacion = (BaseResponseNequi<ResponseSendAuthorizacionBalance>)response.body();
            if(sendAutorizacion != null){
                view.hideCircularProgressBar();
                view.showSectionExpirationTime();
                view.initializeCounter();
                view.resultSendAuthorizationNequiBalance(true);
            }else{
                view.resultSendAuthorizationNequiBalance(false);
            }
        }catch(Exception ex){
            view.resultSendAuthorizationNequiBalance(false);
        }
    }
    @Override
    public <T> void onErrorSendAuthorization(Response<BaseResponseNequi<T>> response) {
        view.resultSendAuthorizationNequiBalance(false);
    }


//    @Override
//    public void fetchAuthorizationNequiBalance(BaseRequestNequi baseRequest){
//       model.getAuthorizationNequiBalance(baseRequest, this);
//    }
//    @Override
//    public <T> void onSuccessGetAuthorization(Response<BaseResponseNequi<T>> response) {
//        try{
//            BaseResponseNequi<ResponseGetAuthorizacionBalance> autorizacion = (BaseResponseNequi<ResponseGetAuthorizacionBalance>)response.body();
//            if(autorizacion != null){
//                if(autorizacion.getResult() != null && autorizacion.getResult().getData() != null
//                        && autorizacion.getResult().getData().getStatus() != null){
//                    view.resultGetAuthorizationNequiBalance(autorizacion.getResult().getData().getStatus());
//
//                }else{
//                    view.resultGetAuthorizationNequiBalance("ERROR");
//                }
//            }else{
//                view.resultGetAuthorizationNequiBalance("ERROR");
//            }
//        }catch(Exception ex){
//            view.resultGetAuthorizationNequiBalance("ERROR");
//        }
//    }
//    @Override
//    public <T> void onErrorGetAuthorization(Response<BaseResponseNequi<T>> response) {
//        view.resultGetAuthorizationNequiBalance("ERROR");
//    }


    @Override
    public <T> void onExpiredToken(Response<BaseResponseNequi<T>> response) {

        view.showExpiredToken(response.body().getErrorToken());
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
