package solidappservice.cm.com.presenteapp.front.adelantonomina.FragmentDetail;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestInsertarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestLogs;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestProcesarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseValorComision;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponserSolicitarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 24/08/2021.
 */
public class FragmentDetailModel implements FragmentDetailContract.Model {


    @Override
    public void getCommissionValue(FragmentDetailContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseValorComision>> call = service.getCommissionValue();
            call.enqueue(new Callback<BaseResponse<ResponseValorComision>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseValorComision>> call, Response<BaseResponse<ResponseValorComision>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getDescripcionError() != null && !response.body().getDescripcionError().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessCommissionValue(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseValorComision>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailure(t, true);
                    }else{
                        listener.onFailure(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onError(null);
        }
    }

    @Override
    public void registerSalaryAdvance(RequestInsertarAdelantoNomina body, FragmentDetailContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<String>> call = service.registerSalaryAdvance(body);
            call.enqueue(new Callback<BaseResponse<String>>() {

                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getDescripcionError() != null && !response.body().getDescripcionError().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessRegisterSalaryAdvance(response);
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
                        listener.onFailure(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onError(null);
        }
    }

    @Override
    public void processSalaryAdvance(RequestProcesarAdelantoNomina body, FragmentDetailContract.APIListener listener) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponserSolicitarAdelantoNomina>> call = service.processSalaryAdvance(body);
            call.enqueue(new Callback<BaseResponse<ResponserSolicitarAdelantoNomina>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponserSolicitarAdelantoNomina>> call, Response<BaseResponse<ResponserSolicitarAdelantoNomina>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getDescripcionError() != null && !response.body().getDescripcionError().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessProcessSalaryAdvance(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponserSolicitarAdelantoNomina>> call, Throwable t) {

                    if(t instanceof IOException){
                        BaseResponse<ResponserSolicitarAdelantoNomina> body = new BaseResponse<ResponserSolicitarAdelantoNomina>(
                                "",
                                "",
                                "",
                                new ResponserSolicitarAdelantoNomina()
                        );
                        Response<BaseResponse<ResponserSolicitarAdelantoNomina>> response = Response.success(body);
                        listener.onSuccessProcessSalaryAdvance(response);
                    }else{
                        listener.onFailure(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onError(null);
        }
    }

    @Override
    public void sendLogs(RequestLogs body, FragmentDetailContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<String>> call = service.sendLogs(body);
            call.enqueue(new Callback<BaseResponse<String>>() {

                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
//                            listener.onExpiredToken(response);
                        }else if(response.body().getDescripcionError() != null && !response.body().getDescripcionError().isEmpty()){
//                            listener.onError(response);
                        }else{
//                            listener.onSuccess(response);
                        }
                    } else {
                        response.body().setDescripcionError("Error: "+response.code());
//                        listener.onError(response);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
//                    listener.onFailure(t);
                }
            });
        } catch (Exception e) {
            listener.onError(null);
        }
    }


    public static <T> Response<T> success(T body, Response rawResponse) {
        if(rawResponse != null && body != null){
            Response<T> response = rawResponse;
        }
        return rawResponse;
    }
}
