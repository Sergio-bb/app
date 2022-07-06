package solidappservice.cm.com.presenteapp.front.nequi.qr.ActivityCameraQR;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestGetDataCommerceQR;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseDataCommerceQR;
import solidappservice.cm.com.presenteapp.front.nequi.qr.FragmentQRCamera.FragmentQRCameraContract;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apinequi.ApiNequi;

public class ActivityQRCameraModel implements ActivityQRCameraContract.Model {

    @Override
    public void getDataCommerceQR(RequestGetDataCommerceQR body, ActivityQRCameraContract.APIListener listener) {
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
            Call<BaseResponseNequi<ResponseDataCommerceQR>> call = service.datospagoQR(body);
            call.enqueue(new Callback<BaseResponseNequi<ResponseDataCommerceQR>>() {

                @Override
                public void onResponse(Call<BaseResponseNequi<ResponseDataCommerceQR>> call, Response<BaseResponseNequi<ResponseDataCommerceQR>> response) {
                    if(response.isSuccessful()){
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            try {
                                listener.onExpiredToken(response);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else if (response.body() != null && response.body().getResult() != null){
                            listener.onSuccessDataCommerceQR(response);
                        }else {
                            listener.onErrorDataCommerceQR(response);
                        }
                    }else{
                        listener.onErrorDataCommerceQR(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponseNequi<ResponseDataCommerceQR>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailureDataCommerceQR(t, true);
                    }else{
                        listener.onFailureDataCommerceQR(t, false);
                    }

                }

            });
        } catch (Exception e) {
            listener.onErrorDataCommerceQR(null);
        }
    }


}
