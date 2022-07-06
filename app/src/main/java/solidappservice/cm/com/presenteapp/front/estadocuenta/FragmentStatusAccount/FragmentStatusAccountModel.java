package solidappservice.cm.com.presenteapp.front.estadocuenta.FragmentStatusAccount;

import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseConsultaAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestActualizarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestConsultarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseMovimientos;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProductos;
import solidappservice.cm.com.presenteapp.entities.mensajes.request.RequestEnviarMensaje;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public class FragmentStatusAccountModel implements FragmentStatusAccountContract.Model{

    @Override
    public void getSalaryAdvanceMovements(BaseRequest body, final FragmentStatusAccountContract.APIListener listener) {
        try {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .addInterceptor(loggingInterceptor)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<List<ResponseMovimientos>>> call = service.getMoves(body);
            call.enqueue(new Callback<BaseResponse<List<ResponseMovimientos>>>() {
                @Override
                public void onResponse(Call<BaseResponse<List<ResponseMovimientos>>> call, Response<BaseResponse<List<ResponseMovimientos>>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessSalaryAdvanceMovements(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<List<ResponseMovimientos>>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailure(t, true);
                    }else{
                        listener.onError(null);
                    }
                }
            });
        } catch (Exception e) {
            listener.onError(null);
        }
    }

    @Override
    public void processSalaryAdvancePending(RequestConsultarAdelantoNomina body, final FragmentStatusAccountContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseConsultaAdelantoNomina>> call = service.checkSalaryAdvance(body);
            call.enqueue(new Callback<BaseResponse<ResponseConsultaAdelantoNomina>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseConsultaAdelantoNomina>> call, Response<BaseResponse<ResponseConsultaAdelantoNomina>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            //Log.i("errorEstadoC","1");
                            listener.onErrorSalaryAdvance(response);
                        }else{
                            listener.onSuccessProcessSalaryAdvancePending(response);
                        }
                    } else {
                        listener.onErrorSalaryAdvance(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseConsultaAdelantoNomina>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailureSalaryAdvance(t, true);
                    }else{
                        listener.onFailureSalaryAdvance(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onError(null);
        }
    }

    @Override
    public void updateSalaryAdvanceStatus(RequestActualizarAdelantoNomina body, final FragmentStatusAccountContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<String>> call = service.updateSalaryAdvance(body);
            call.enqueue(new Callback<BaseResponse<String>>() {

                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessUpdateSalaryAdvanceStatus(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailure(t, true);
                    }else{
                        listener.onError(null);
                    }
                }
            });
        } catch (Exception e) {
            listener.onError(null);
        }
    }

    @Override
    public void sendSalaryAdvanceNotification(RequestEnviarMensaje body, final FragmentStatusAccountContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<String>> call = service.sendNotification(body);
            call.enqueue(new Callback<BaseResponse<String>>() {

                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            response.body().setResultado("MENSAJE");
                            listener.onSuccessSendSalaryAdvanceNotification(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailure(t, true);
                    }else{
                        listener.onError(null);
                    }
                }
            });
        } catch (Exception e) {
            listener.onError(null);
        }
    }

    @Override
    public void getAccountStatus(BaseRequest body, final FragmentStatusAccountContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<List<ResponseProductos>>> call = service.getAccounts(body);
            call.enqueue(new Callback<BaseResponse<List<ResponseProductos>>>() {

                @Override
                public void onResponse(Call<BaseResponse<List<ResponseProductos>>> call, Response<BaseResponse<List<ResponseProductos>>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessAccountStatus(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<List<ResponseProductos>>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailure(t, true);
                    }else{
                        listener.onError(null);
                    }
                }
            });
        } catch (Exception e) {
            listener.onError(null);
        }
    }

/*    call.enqueue(new Callback<BaseResponse<List<ResponseMovimientos>>>() {

        @Override
        public void onResponse(Call<BaseResponse<List<ResponseMovimientos>>> call, Response<BaseResponse<List<ResponseMovimientos>>> response) {
            if (response.isSuccessful()) {
                if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                    listener.onExpiredToken(response);
                }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                    Log.i("onErrorJ", "1");
                    listener.onError(response);
                }else{
                    listener.onSuccessSalaryAdvanceMovements(response);
                }
            } else {
                Log.i("onErrorJ", "2");
                listener.onError(null);
            }
        }
        @Override
        public void onFailure(Call<BaseResponse<List<ResponseMovimientos>>> call, Throwable t) {
            if(t instanceof IOException){
                listener.onFailure(t, true);
            }else{
                Log.i("onErrorJ", "3"+ t.getMessage());
                listener.onError(null);
            }
        }
    });*/

}
