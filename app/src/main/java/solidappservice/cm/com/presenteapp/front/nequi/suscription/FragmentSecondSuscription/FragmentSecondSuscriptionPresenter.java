package solidappservice.cm.com.presenteapp.front.nequi.suscription.FragmentSecondSuscription;

import androidx.annotation.NonNull;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.DatosAsociado;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseConsultarDatosAsociado;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestSendSubscription;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultaSuscripcion;

public class FragmentSecondSuscriptionPresenter implements FragmentSecondSuscriptionContract.Presenter, FragmentSecondSuscriptionContract.APIListener {

    FragmentSecondSuscriptionView view;
    FragmentSecondSuscriptionModel model;

    public FragmentSecondSuscriptionPresenter(@NonNull FragmentSecondSuscriptionView view, @NonNull FragmentSecondSuscriptionModel model) {
        this.view = view;
        this.model = model;
    }
    @Override
    public void sendSubscriptionNotificacion(RequestSendSubscription base){
        view.hideButtonSend();
        view.showCircularProgressBarSuscription();
        model.sendSubscriptionNotificacion(base,this);
    }

    @Override
    public <T> void onSuccessSendSubscriptionNotificacion(Response<BaseResponseNequi<T>> response) {
        try{
            BaseResponseNequi<ResponseConsultaSuscripcion> consulta = (BaseResponseNequi<ResponseConsultaSuscripcion>)response.body();
            view.hideCircularProgressBarSuscription();
            view.showButtonSend();
            view.resultSendSubscriptionNotificacion(true);
        }catch(Exception ex){
            view.showCircularProgressBar();
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public <T> void onErrorSendSubscriptionNotificacion(Response<BaseResponseNequi<T>> response) {
//        view.hiddenSendingRequest();
        view.hideCircularProgressBarSuscription();
        if(response != null){
            if(response.body() != null && response.body().getMessage() != null && response.body().getMessage().contains("20-08A")){
                view.showDataFetchError("Lo sentimos", "Este número no corresponde a una cuenta de Nequi activa, verifica tu información y inténtalo de nuevo mas tarde.");
            }else{
                view.showDataFetchError("Lo sentimos", response.body().getMessage());
            }
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void onFailureSendSubscriptionNotificacion(Throwable t, boolean isErrorTimeOut) {
        view.hideCircularProgressBarSuscription();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void fetchPersonalData(BaseRequest base) {
        view.hideResumePersonalData();
        view.showCircularProgressBar();
        view.disabledButtonSend();
        model.getPersonalData(base, this);
    }

    @Override
    public <T> void onSuccessPersonalData(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        try{
            ResponseConsultarDatosAsociado datos = (ResponseConsultarDatosAsociado) response.body().getResultado();
            DatosAsociado datosAsociado = new DatosAsociado(datos.getNombreCompleto(),
                    datos.getDireccion(),
                    datos.getCelular(),
                    datos.getEmail(),
                    datos.getBarrio(),
                    datos.getIdCiudad(),
                    datos.getNombreCiudad(),
                    datos.getIdDepartamento(),
                    datos.getNombreDepartamento(),
                    datos.getIdPais(),
                    datos.getNombrePais()
            );
            view.showResumePersonalData();
            view.enableButtonSend();
            view.showPersonalData(datosAsociado);
        }catch(Exception ex){
            view.showCircularProgressBar();
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public <T> void onError(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        view.showDataFetchError("Lo sentimos","Se ha producido un error, inténtalo nuevamente en unos minutos.");
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideCircularProgressBar();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public <T> void onExpiredTokenNequi(Response<BaseResponseNequi<T>> response) {
        view.hideCircularProgressBar();
        view.showExpiredToken(response.body().getErrorToken());
    }
    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        view.showExpiredToken(response.body().getErrorToken());
    }
}
