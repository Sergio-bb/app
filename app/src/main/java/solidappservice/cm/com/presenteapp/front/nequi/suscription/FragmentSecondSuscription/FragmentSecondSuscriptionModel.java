package solidappservice.cm.com.presenteapp.front.nequi.suscription.FragmentSecondSuscription;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseConsultarDatosAsociado;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestSendSubscription;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultaSuscripcion;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apinequi.ApiNequi;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

public class FragmentSecondSuscriptionModel implements FragmentSecondSuscriptionContract.Model {

    OkHttpClient client = new OkHttpClient().newBuilder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(NetworkHelper.DIRECCION_WS)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    ApiPresente service = retrofit.create(ApiPresente.class);


    Retrofit retrofitNequi = new Retrofit.Builder()
            .baseUrl(NetworkHelper.NEQUI_WS)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();
    ApiNequi serviceNequi = retrofitNequi.create(ApiNequi.class);

    @Override
    public void getPersonalData(BaseRequest body, FragmentSecondSuscriptionContract.APIListener listener) {
        try {
            Call<BaseResponse<ResponseConsultarDatosAsociado>> call = service.getPersonalData(body);
            call.enqueue(new Callback<BaseResponse<ResponseConsultarDatosAsociado>>() {
                @Override
                public void onResponse(Call<BaseResponse<ResponseConsultarDatosAsociado>> call, Response<BaseResponse<ResponseConsultarDatosAsociado>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()) {
                            listener.onExpiredToken(response);
                        } else if (response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()) {
                            listener.onError(response);
                        } else {
                            listener.onSuccessPersonalData(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseConsultarDatosAsociado>> call, Throwable t) {
                    listener.onFailure(t, t instanceof IOException);
                }
            });
        } catch (Exception e) {
            listener.onError(null);
        }
    }

    @Override
    public void sendSubscriptionNotificacion(RequestSendSubscription body, FragmentSecondSuscriptionContract.APIListener listener) {
        try {
            Call<BaseResponseNequi<ResponseConsultaSuscripcion>> call = serviceNequi.vincularCuenta(body);
            call.enqueue(new Callback<BaseResponseNequi<ResponseConsultaSuscripcion>>() {
                @Override
                public void onResponse(Call<BaseResponseNequi<ResponseConsultaSuscripcion>> call, Response<BaseResponseNequi<ResponseConsultaSuscripcion>> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if(response.body().isResponse()){
                            listener.onSuccessSendSubscriptionNotificacion(response);
                        }else if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredTokenNequi(response);
                        }else{
                            listener.onErrorSendSubscriptionNotificacion(response);
                        }
                    } else {
                        listener.onErrorSendSubscriptionNotificacion(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponseNequi<ResponseConsultaSuscripcion>> call, Throwable t) {
                    listener.onFailureSendSubscriptionNotificacion(t, t instanceof IOException);
                }
            });
        } catch (Exception e) {
            listener.onErrorSendSubscriptionNotificacion(null);
        }
    }
}
