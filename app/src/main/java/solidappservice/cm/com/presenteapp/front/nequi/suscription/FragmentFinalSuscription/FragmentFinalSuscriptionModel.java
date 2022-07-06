package solidappservice.cm.com.presenteapp.front.nequi.suscription.FragmentFinalSuscription;
import java.sql.Time;
import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseSuscriptionStatus;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apinequi.ApiNequi;

public class FragmentFinalSuscriptionModel implements FragmentFinalSuscriptionContract.Model {

    @Override
    public void getMinutesOfSuscription(BaseRequestNequi body, final FragmentFinalSuscriptionContract.APIListener listener ){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkHelper.NEQUI_WS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiNequi service = retrofit.create(ApiNequi.class);
        try{
            Call<BaseResponseNequi<ResponseSuscriptionStatus>> call = service.getSuscriptionDate(body);
            call.enqueue(new Callback<BaseResponseNequi<ResponseSuscriptionStatus>>(){
                @Override
                public void  onResponse(Call<BaseResponseNequi<ResponseSuscriptionStatus>> call, Response<BaseResponseNequi<ResponseSuscriptionStatus>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getResult() != null){
                            try {
                                listener.onSuccessGetTimeOfSuscription(response);
                            } catch (ParseException e) {
                                listener.onFailureGetTimeOfSuscription(response);
                            }
                        }else{
                            listener.onFailureGetTimeOfSuscription(response);
                        }
                    }
                }
                @Override
                public void onFailure(Call<BaseResponseNequi<ResponseSuscriptionStatus>> call, Throwable t) {
                    listener.onFailureGetTimeOfSuscription(null);
                }
            });
        }catch(Exception e){
            listener.onFailureGetTimeOfSuscription(null);
        }
    }
}
