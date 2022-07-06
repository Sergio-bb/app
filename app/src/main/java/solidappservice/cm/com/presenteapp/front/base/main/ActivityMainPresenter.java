package solidappservice.cm.com.presenteapp.front.base.main;

import android.os.Looper;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultaSuscripcion;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseSuscriptionStatus;

public class ActivityMainPresenter implements ActivityMainContract.Presenter, ActivityMainContract.APIListener{

    ActivityMainView view;
    ActivityMainModel model;

    public ActivityMainPresenter(ActivityMainView view, ActivityMainModel model) {
        this.view = view;
        this.model = model;
    }

//    @Override
//    public void validateSuscriptionNequi(BaseRequest baseRequest) {
//        model.getSuscriptionNequi(baseRequest, this);
//    }
//    @Override
//    public <T> void onSuccessGetSuscriptionNequi(Response<BaseResponseNequi<T>> response) {
//        try{
//            BaseResponseNequi<ResponseConsultaSuscripcion> consulta = (BaseResponseNequi<ResponseConsultaSuscripcion>)response.body();
//            if(consulta.getResult() == null){
//                view.saveSuscriptionData(null, null);
//            }else{
//                ResponseConsultaSuscripcion.GetSuscriptionNequi suscriptionNequi = consulta.getResult().getResultNequi();
//                String status = suscriptionNequi.getResponseMessage().getResponseBody().getAny().getGetSubscriptionRS().getSubscription().getStatus();
//                if(status.equals("0") || status.equals("2") || status.equals("3") ){
//                    view.saveSuscriptionData(null, status);
//                }else if(status.equals("1")){
//                    view.saveSuscriptionData(consulta.getResult().getDatosSuscripcion(), status);
//                }else{
//                    view.saveSuscriptionData(null, null);
//                }
//            }
//        }catch (Exception ex){
//            view.saveSuscriptionData(null, null);
//        }
//    }
//    @Override
//    public <T> void onErrorGetSuscriptionNequi(Response<BaseResponseNequi<T>> response) {
//        view.saveSuscriptionData(null, null);
//    }
//    @Override
//    public void onFailureGetSuscriptionNequi(Throwable t) {
//        view.saveSuscriptionData(null, null);
//    }
//
//
//    @Override
//    public void getTimeOfsuscription(BaseRequestNequi request){
//        if(request!= null){
//            model.getMinutesOfSuscription(request, this);
//        }
//    }
//
//    @Override
//    public <T> void onSuccessGetTimeOfSuscription(Response<BaseResponseNequi<ResponseSuscriptionStatus>> response) throws ParseException {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        Date fechaSuscription = format.parse(response.body().getResult().getF_FechaCreacion());
//        Date fechaActual = new  Date();
//        long diff = fechaActual.getTime() - fechaSuscription.getTime();
//        long diffDays = diff * 24;
//        long diffSeconds = diff / 1000;
//        long diffMinutes = diff / (60 * 1000);
//        long diffHours = diff / (60 * 60 * 1000);
//        calculeMinutes((int)diffDays, (int)diffHours, (int)diffMinutes, (int) diffSeconds);
//    }
//
//    @Override
//    public <T> void onFailureGetTimeOfSuscription(Response<BaseResponseNequi<T>> response) {
//        calculeMinutes(null, null, null, null);
//    }
//
//    @Override
//    public void calculeMinutes(Integer days, Integer hour, Integer minute, Integer second){
//        if(days != null && days >= 0 &&
//            hour != null && hour <=0 &&
//            minute != null && minute < 15){
//            int milisecons = 900000-((minute*60000)+(second));
//            view.activateButtonWarning(true);
//            new android.os.Handler(Looper.getMainLooper()).postDelayed(
//                    () -> view.activateButtonWarning(false),milisecons);
//        } else {
//            view.activateButtonWarning((false));
//        }
//    }


}
