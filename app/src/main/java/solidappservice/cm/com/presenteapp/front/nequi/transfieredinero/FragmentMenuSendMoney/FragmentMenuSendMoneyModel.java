package solidappservice.cm.com.presenteapp.front.nequi.transfieredinero.FragmentMenuSendMoney;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultaSuscripcion;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apinequi.ApiNequi;

public class FragmentMenuSendMoneyModel implements FragmentMenuSendMoneyContract.Model{

    @Override
    public void getSuscriptionNequi(BaseRequest body, final FragmentMenuSendMoneyContract.APIListener listener) {

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
                            listener.onExpiredToken(response);
                        }else{
                            listener.onSuccessGetSuscriptionNequi(response);
                        }
                    }else {
                        listener.onErrorGetSuscriptionNequi(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponseNequi<ResponseConsultaSuscripcion>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailureGetSuscriptionNequiNequi(t, true);
                    }else{
                        listener.onFailureGetSuscriptionNequiNequi(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onErrorGetSuscriptionNequi(null);
        }
    }

//    @Override
//    public void validateSuscriptionNequi(BaseRequest body, final FragmentMenuSendMoneyContract.APIListener listener) {
//
//        try {
//            OkHttpClient client = new OkHttpClient().newBuilder()
//                    .readTimeout(60, TimeUnit.SECONDS)
//                    .connectTimeout(60, TimeUnit.SECONDS)
//                    .build();
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(NetworkHelper.NEQUI_WS)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(client)
//                    .build();
//
//            ApiNequi service = retrofit.create(ApiNequi.class);
//            Call<BaseResponseNequi<ResponseConsultaSuscripcion>> call = service.consultarSuscripcion(body);
//            call.enqueue(new Callback<BaseResponseNequi<ResponseConsultaSuscripcion>>() {
//
//                @Override
//                public void onResponse(Call<BaseResponseNequi<ResponseConsultaSuscripcion>> call, Response<BaseResponseNequi<ResponseConsultaSuscripcion>> response) {
//                    if(response.isSuccessful()){
//                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
//                            listener.onExpiredToken(response);
//                        }else{
//                            listener.onSuccessValidateSuscriptionNequi(response);
//                        }
//                    }else {
//                        listener.onErrorValidateSuscriptionNequi(null);
//                    }
//                }
//                @Override
//                public void onFailure(Call<BaseResponseNequi<ResponseConsultaSuscripcion>> call, Throwable t) {
//                    if(t instanceof IOException){
//                        listener.onFailureValidateSuscriptionNequi(t, true);
//                    }else{
//                        listener.onFailureValidateSuscriptionNequi(t, false);
//                    }
//                }
//            });
//        } catch (Exception e) {
//            listener.onErrorValidateSuscriptionNequi(null);
//        }
//    }

}
