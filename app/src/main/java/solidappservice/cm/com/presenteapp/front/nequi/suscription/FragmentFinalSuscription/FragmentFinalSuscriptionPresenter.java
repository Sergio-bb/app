package solidappservice.cm.com.presenteapp.front.nequi.suscription.FragmentFinalSuscription;

import androidx.annotation.NonNull;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseSuscriptionStatus;

public class FragmentFinalSuscriptionPresenter implements FragmentFinalSuscriptionContract.APIListener, FragmentFinalSuscriptionContract.Presenter{

    FragmentFinalSuscriptionView view;
    FragmentFinalSuscriptionModel model;

    public FragmentFinalSuscriptionPresenter(@NonNull FragmentFinalSuscriptionView view, @NonNull FragmentFinalSuscriptionModel model) {
        this.view = view;
        this.model = model;
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
        long diffDays = diff / (60 * 60 * 1000 * 24);
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        calculeMinutes((int)diffDays, (int)diffHours, (int)diffMinutes, (int) diffSeconds);
    }

    @Override
    public void calculeMinutes(Integer days, Integer hour, Integer minute, Integer second){
        if(days != null && days <= 0 &&
            hour != null && hour <=0 &&
                minute != null && minute < 15){
            view.initializeCounter(minute, second);
        }else{
            view.initializeCounter(15, 0);
        }
    }

    @Override
    public <T> void onFailureGetTimeOfSuscription(Response<BaseResponseNequi<T>> response) {
        calculeMinutes(null, null, null, null);
    }
}
