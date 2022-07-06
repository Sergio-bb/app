package solidappservice.cm.com.presenteapp.front.nequi.transfieredinero.FragmentMenuSendMoney;

import android.util.Log;

import androidx.annotation.NonNull;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultaSuscripcion;

public class FragmentMenuSendMoneyPresenter implements FragmentMenuSendMoneyContract.Presenter, FragmentMenuSendMoneyContract.APIListener{

    FragmentMenuSendMoneyView view;
    FragmentMenuSendMoneyModel model;

    public FragmentMenuSendMoneyPresenter(@NonNull FragmentMenuSendMoneyView view, @NonNull FragmentMenuSendMoneyModel model){
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchSuscriptionNequi(BaseRequest baseRequest) {
        view.hideSectionTransferNequiMenu();
        view.hideContentSendMoneyMenu();
        view.showCircularProgressBar("Un momento");
        model.getSuscriptionNequi(baseRequest, this);
    }
    @Override
    public <T> void onSuccessGetSuscriptionNequi(Response<BaseResponseNequi<T>> response) {
        try{
            BaseResponseNequi<ResponseConsultaSuscripcion> consulta = (BaseResponseNequi<ResponseConsultaSuscripcion>)response.body();
            if(consulta.getResult() == null){
                view.hideCircularProgressBar();
                view.requiredSuscription();
                view.showSectionTransferNequiMenu();
            }else{
                ResponseConsultaSuscripcion.GetSuscriptionNequi suscriptionNequi = consulta.getResult().getResultNequi();
                String status = suscriptionNequi.getResponseMessage().getResponseBody().getAny().getGetSubscriptionRS().getSubscription().getStatus();
                view.hideCircularProgressBar();
                if(status != null && (status.equals("0") || status.equals("2") || status.equals("3"))){
                    view.requiredSuscription();
                    view.showSectionTransferNequiMenu();
                }else if(status != null && status.equals("1")){
                    view.notRequiredSuscription();
                    view.saveSuscriptionData(consulta.getResult().getDatosSuscripcion());
                    view.showSectionTransferNequiMenu();
                }else{
                    view.hideSectionTransferNequiMenu();
                }
            }
            view.showContentSendMoneyMenu();
        }catch(Exception ex){
            view.hideCircularProgressBar();
            view.hideSectionTransferNequiMenu();
            view.showContentSendMoneyMenu();
        }
    }
    @Override
    public <T> void onErrorGetSuscriptionNequi(Response<BaseResponseNequi<T>> response) {
        view.hideCircularProgressBar();
        view.hideSectionTransferNequiMenu();
        view.showContentSendMoneyMenu();
    }

    @Override
    public void onFailureGetSuscriptionNequiNequi(Throwable t, boolean isErrorTimeOut) {
        view.hideCircularProgressBar();
        view.hideSectionTransferNequiMenu();
        view.showContentSendMoneyMenu();
    }

//    @Override
//    public void validateSuscriptionNequi(BaseRequest baseRequest) {
//        view.showProgressDialog("Un momento...");
//        model.validateSuscriptionNequi(baseRequest, this);
//    }
//    @Override
//    public <T> void onSuccessValidateSuscriptionNequi(Response<BaseResponseNequi<T>> response) {
//        view.hideProgressDialog();
//        try{
//            BaseResponseNequi<ResponseConsultaSuscripcion> consulta = (BaseResponseNequi<ResponseConsultaSuscripcion>)response.body();
//            if(consulta.getResult() == null){
//                view.redirectSuscriptionRequired();
//            }else{
//                ResponseConsultaSuscripcion.GetSuscriptionNequi suscriptionNequi = consulta.getResult().getResultNequi();
//                String status = suscriptionNequi.getResponseMessage().getResponseBody().getAny().getGetSubscriptionRS().getSubscription().getStatus();
//                if(status.equals("0") ){
////                    view.showDataFetchError("Suscripción en proceso","Suscripción se encuentra pendiente de aceptar.");
//                    view.redirectSuscriptionRequired();
//                }else if(status.equals("2")){
//                    view.redirectSuscriptionRequired();
//                }else if(status.equals("3")){
////                    view.showDataFetchError("Suscripción expirada", "La solicitud de suscripción ha expirado.");
//                    view.redirectSuscriptionRequired();
//                }else{
//                    view.showDataFetchError("Lo sentimos", "");
//                }
//            }
//        }catch(Exception ex){
//            view.showDataFetchError("Lo sentimos", "");
//        }
//    }
//    @Override
//    public <T> void onErrorValidateSuscriptionNequi(Response<BaseResponseNequi<T>> response) {
//        view.hideCircularProgressBar();
//        view.showSectionTransferNequiMenu();
//        String errorNequi = "Se ha producido un error, inténtalo nuevamente en unos minutos.";
//        if(response != null && response.body() != null && response.body().getMessage()!= null){
//            errorNequi = response.body().getMessage();
//        }
//        view.showDataFetchError("Lo sentimos", errorNequi);
//    }
//    @Override
//    public void onFailureValidateSuscriptionNequi(Throwable t, boolean isErrorTimeOut) {
//        view.hideCircularProgressBar();
//        view.showSectionTransferNequiMenu();
//        if(isErrorTimeOut){
//            view.showErrorTimeOut();
//        }else{
//            view.showDataFetchError("Lo sentimos", "");
//        }
//    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponseNequi<T>> response) {
        view.hideCircularProgressBar();
        view.showExpiredToken(response.body().getErrorToken());
    }
}
