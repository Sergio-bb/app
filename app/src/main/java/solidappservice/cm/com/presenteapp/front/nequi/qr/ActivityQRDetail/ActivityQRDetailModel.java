package solidappservice.cm.com.presenteapp.front.nequi.qr.ActivityQRDetail;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultaSuscripcion;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiGeneral;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseSuscriptionStatus;
import solidappservice.cm.com.presenteapp.front.base.main.ActivityMainContract;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsContract;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apinequi.ApiNequi;

public class ActivityQRDetailModel implements ActivityQRDetailContract.Model {

    @Override
    public void getSuscriptionNequi(BaseRequest body, final ActivityQRDetailContract.APIListener listener) {

        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.NEQUI_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            ApiNequi service = retrofit.create(ApiNequi.class);
            Call<BaseResponseNequi<ResponseConsultaSuscripcion>> call = service.consultarSuscripcion(body);
            call.enqueue(new Callback<BaseResponseNequi<ResponseConsultaSuscripcion>>() {

                @Override
                public void onResponse(Call<BaseResponseNequi<ResponseConsultaSuscripcion>> call, Response<BaseResponseNequi<ResponseConsultaSuscripcion>> response) {
                    if(response.isSuccessful()){
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredTokenNequi(response);
                        }else if(response.isSuccessful()){
                            listener.onSuccessGetSuscriptionNequi(response);
                        }else {
                            listener.onErrorGetSuscriptionNequi(response);
                        }
                    }else {
                        listener.onErrorGetSuscriptionNequi(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponseNequi<ResponseConsultaSuscripcion>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailureGetSuscriptionNequi(t, true);
                    }else{
                        listener.onFailureGetSuscriptionNequi(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onErrorGetSuscriptionNequi(null);
        }
    }

    @Override
    public void getTimeExpiration(final ActivityQRDetailContract.APIListener listener ){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkHelper.NEQUI_WS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiNequi service = retrofit.create(ApiNequi.class);
        try{
            Call<ResponseNequiGeneral> call = service.getTimeExpiration();
            call.enqueue(new Callback<ResponseNequiGeneral>(){
                @Override
                public void  onResponse(Call<ResponseNequiGeneral> call, Response<ResponseNequiGeneral> response) {
                    if (response.isSuccessful()) {
                        if(response.body() != null){
                            listener.onSuccessGetTimeExpiration(response);
                        }else{
                            listener.onErrorGetTimeExpiration(response);
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseNequiGeneral> call, Throwable t) {
                    listener.onErrorGetTimeExpiration(null);
                }
            });
        }catch(Exception e){
            listener.onErrorGetTimeExpiration(null);
        }
    }

    @Override
    public void getMinutesOfSuscription(BaseRequestNequi body, final ActivityQRDetailContract.APIListener listener ){
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
