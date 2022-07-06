package solidappservice.cm.com.presenteapp.front.nequi.suscription.ActivitySuscription;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultaSuscripcion;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiGeneral;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseSuscriptionStatus;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;

public class ActivitySuscriptionPresenter implements ActivitySuscriptionContract.Presenter, ActivitySuscriptionContract.APIListener {

    ActivitySuscriptionView view;
    ActivitySuscriptionModel model;

    public ActivitySuscriptionPresenter(ActivitySuscriptionView view, ActivitySuscriptionModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void loadFragmentSuscripcionNequi(IFragmentCoordinator.Pantalla pantalla) {
        view.showFragmentSuscripcion(pantalla);
    }

    @Override
    public void validateSuscriptionNequi(BaseRequest baseRequest) {
        view.hideContentSuscription();
        view.showCircularProgressBar("Un momento...");
        model.getSuscriptionNequi(baseRequest, this);
    }
    @Override
    public <T> void onSuccessGetSuscriptionNequi(Response<BaseResponseNequi<T>> response) {
        try{
            BaseResponseNequi<ResponseConsultaSuscripcion> consulta = (BaseResponseNequi<ResponseConsultaSuscripcion>)response.body();
            if(consulta.getResult() == null){
                view.hideCircularProgressBar();
                view.showContentSuscription();
                view.resultStatusSuscription(null);
            }else{
                ResponseConsultaSuscripcion.GetSuscriptionNequi suscriptionNequi = consulta.getResult().getResultNequi();
                String status = suscriptionNequi.getResponseMessage().getResponseBody().getAny().getGetSubscriptionRS().getSubscription().getStatus();
                if(status != null && status.equals("0")){
                    view.resultStatusSuscription(status);
                }else{
                    view.hideCircularProgressBar();
                    view.showContentSuscription();
                    view.resultStatusSuscription(null);
                }
            }
        }catch (Exception ex){
            view.hideCircularProgressBar();
            view.showContentSuscription();
            view.resultStatusSuscription(null);
        }
    }
    @Override
    public <T> void onErrorGetSuscriptionNequi(Response<BaseResponseNequi<T>> response) {
        view.hideCircularProgressBar();
        view.showContentSuscription();
        view.resultStatusSuscription(null);
    }
    @Override
    public void onFailureGetSuscriptionNequi(Throwable t, boolean isErrorTimeOut) {
        view.hideCircularProgressBar();
        view.showContentSuscription();
        view.resultStatusSuscription(null);
    }


    @Override
    public void  getTimeExpiration(){
        model.getTimeExpiration(this);
    }
    @Override
    public <T> void onSuccessGetTimeExpiration(Response<ResponseNequiGeneral> response){
        try{
            if(response != null && response.body() != null && response.body().getN_ValorParametro() != null
                    && response.body().getN_ValorParametro() != ""){
                view.resultTimeExpiration(Integer.parseInt(response.body().getN_ValorParametro()));
            }else{
                view.resultTimeExpiration(15);
            }
            view.getTimeOfSuscription();
        }catch(Exception ex){
            view.resultTimeExpiration(15);
            view.getTimeOfSuscription();
        }
    }
    @Override
    public <T> void onErrorGetTimeExpiration(Response<ResponseNequiGeneral> response) {
        view.resultTimeExpiration(15);
        view.getTimeOfSuscription();
    }


    @Override
    public void getTimeOfsuscription(BaseRequestNequi request){
        if(request!= null){
            view.hideContentSuscription();
            view.showCircularProgressBar("Un momento...");
            model.getMinutesOfSuscription(request, this);
        }
    }
    @Override
    public <T> void onSuccessGetTimeOfSuscription(Response<BaseResponseNequi<ResponseSuscriptionStatus>> response) throws ParseException {
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date fechaSuscription = format.parse(response.body().getResult().getF_FechaCreacion());
            Date fechaActual = new  Date();
            long diff = fechaActual.getTime() - fechaSuscription.getTime();
            long diffDays = diff * 24;
            long diffSeconds = diff / 1000;
            long diffMinutes = diff / (60 * 1000);
            long diffHours = diff / (60 * 60 * 1000);
            view.calculeMinutes((int)diffDays, (int)diffHours, (int)diffMinutes, (int) diffSeconds);
        }catch(Exception ex){
            view.calculeMinutes(null, null, null, null);
        }
    }
    @Override
    public <T> void onFailureGetTimeOfSuscription(Response<BaseResponseNequi<T>> response) {
        view.calculeMinutes(null, null, null, null);
    }


    @Override
    public <T> void onExpiredTokenNequi(Response<BaseResponseNequi<T>> response) {
        view.showExpiredToken(response.body().getErrorToken());
    }
}
