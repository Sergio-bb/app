package solidappservice.cm.com.presenteapp.front.convenios.ActivityHtmlViewer;

import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseConvenios;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Resumen;
import solidappservice.cm.com.presenteapp.entities.convenios.response.ResponseResumen;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultaSuscripcion;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiGeneral;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseSuscriptionStatus;
import solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.ActivityAgreementsContract;
import solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.ActivityAgreementsView;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public class ActivityHtmlPresenter implements ActivityHtmlContract.Presenter,
        ActivityHtmlContract.APIListener{

    ActivityHtmlView view;
    ActivityHtmlModel model;

    public ActivityHtmlPresenter(@NonNull ActivityHtmlView view, @NonNull ActivityHtmlModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void validateSuscriptionNequi(BaseRequest baseRequest) {
        model.getSuscriptionNequi(baseRequest, this);
    }
    @Override
    public <T> void onSuccessGetSuscriptionNequi(Response<BaseResponseNequi<T>> response) {
        try{
            BaseResponseNequi<ResponseConsultaSuscripcion> consulta = (BaseResponseNequi<ResponseConsultaSuscripcion>)response.body();
            if(consulta.getResult() == null){
                view.saveSuscriptionData(null, null);
            }else{
                ResponseConsultaSuscripcion.GetSuscriptionNequi suscriptionNequi = consulta.getResult().getResultNequi();
                String status = suscriptionNequi.getResponseMessage().getResponseBody().getAny().getGetSubscriptionRS().getSubscription().getStatus();
                if(status.equals("0") || status.equals("2") || status.equals("3") ){
                    view.saveSuscriptionData(null, status);
                }else if(status.equals("1")){
                    view.saveSuscriptionData(consulta.getResult().getDatosSuscripcion(), status);
                }
                else{
                    view.saveSuscriptionData(null, null);
                }
            }
        }catch (Exception ex){
            view.saveSuscriptionData(null, null);
        }
    }
    @Override
    public <T> void onErrorGetSuscriptionNequi(Response<BaseResponseNequi<T>> response) {
        view.saveSuscriptionData(null, null);
    }
    @Override
    public void onFailureGetSuscriptionNequi(Throwable t, boolean isErrorTimeOut) {
        view.saveSuscriptionData(null, null);
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
            model.getMinutesOfSuscription(request, this);
        }
    }
    @Override
    public <T> void onSuccessGetTimeOfSuscription(Response<BaseResponseNequi<ResponseSuscriptionStatus>> response) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date fechaSuscription = format.parse(response.body().getResult().getF_FechaCreacion());
        Date fechaActual = new  Date();
        long diff = fechaActual.getTime() - fechaSuscription.getTime();
        long diffDays = diff * 24;
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        view.calculeMinutes((int)diffDays, (int)diffHours, (int)diffMinutes, (int) diffSeconds);
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
