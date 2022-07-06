package solidappservice.cm.com.presenteapp.front.nequi.suscription.FragmentFinalSuscription;

import java.text.ParseException;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseSuscriptionStatus;

public interface FragmentFinalSuscriptionContract {

    interface View{
        void initializeCounter(Integer minute, Integer second);
        void getTimeOfSuscription();
    }

    interface Presenter{
        void getTimeOfsuscription(BaseRequestNequi request);
        void calculeMinutes(Integer days, Integer hour, Integer minute, Integer second);
    }

    interface Model{
        void getMinutesOfSuscription(BaseRequestNequi body, final APIListener listener );
    }
    interface  APIListener{
        <T>void onSuccessGetTimeOfSuscription(Response<BaseResponseNequi<ResponseSuscriptionStatus>> response) throws ParseException;
        <T>void onFailureGetTimeOfSuscription(Response<BaseResponseNequi<T>> response);
    }

}
