package solidappservice.cm.com.presenteapp.front.nequi.transfieredinero.ActivityDialogNequiBalance;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseGetAuthorizacionBalance;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseSendAuthorizacionBalance;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apinequi.ApiNequi;

public class ActivityDialogNequiBalanceModel implements ActivityDialogNequiBalanceContract.Model{

    @Override
    public void sendAuthorizationNequiBalance(BaseRequestNequi body, ActivityDialogNequiBalanceContract.APIListener listener) {
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
            Call<BaseResponseNequi<ResponseSendAuthorizacionBalance>> call = service.sendAuthorizationNequiBalance(body);
            call.enqueue(new Callback<BaseResponseNequi<ResponseSendAuthorizacionBalance>>() {
                @Override
                public void onResponse(Call<BaseResponseNequi<ResponseSendAuthorizacionBalance>> call, Response<BaseResponseNequi<ResponseSendAuthorizacionBalance>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else{
                            listener.onSuccessSendAuthorization(response);
                        }
                    } else {
                        listener.onErrorSendAuthorization(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponseNequi<ResponseSendAuthorizacionBalance>> call, Throwable t) {
                    listener.onErrorSendAuthorization(null);

                }
            });
        } catch (Exception e) {
            listener.onErrorSendAuthorization(null);
        }
    }

//    @Override
//    public void getAuthorizationNequiBalance(BaseRequestNequi body, ActivityDialogNequiBalanceContract.APIListener listener) {
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
//            ApiNequi service = retrofit.create(ApiNequi.class);
//            Call<BaseResponseNequi<ResponseGetAuthorizacionBalance>> call = service.getAuthorizationNequiBalance(body);
//            call.enqueue(new Callback<BaseResponseNequi<ResponseGetAuthorizacionBalance>>() {
//                @Override
//                public void onResponse(Call<BaseResponseNequi<ResponseGetAuthorizacionBalance>> call, Response<BaseResponseNequi<ResponseGetAuthorizacionBalance>> response) {
//                    Log.i("response", String.valueOf(response));
//                    if (response.isSuccessful()) {
//                        listener.onSuccessGetAuthorization(response);
//                    } else {
//                        listener.onErrorGetAuthorization(response);
//                    }
//                }
//                @Override
//                public void onFailure(Call<BaseResponseNequi<ResponseGetAuthorizacionBalance>> call, Throwable t) {
//                    listener.onFailure(t);
//
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
