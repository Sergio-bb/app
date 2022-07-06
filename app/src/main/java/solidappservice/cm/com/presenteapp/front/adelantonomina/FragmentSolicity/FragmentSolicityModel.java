package solidappservice.cm.com.presenteapp.front.adelantonomina.FragmentSolicity;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestLogs;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestNoCumple;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseMovimientos;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseNoCumple;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseTips;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseTopes;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseValidarRequisitos;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 24/08/2021.
 */
public class FragmentSolicityModel implements FragmentSolicityContract.Model {


    @Override
    public void getValidateRequirements(BaseRequest body, FragmentSolicityContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseValidarRequisitos>> call = service.getValidateRequirements(body);
            call.enqueue(new Callback<BaseResponse<ResponseValidarRequisitos>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseValidarRequisitos>> call, Response<BaseResponse<ResponseValidarRequisitos>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccess(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseValidarRequisitos>> call, Throwable t) {
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
    public void getDebtCapacity(BaseRequest body, FragmentSolicityContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseTopes>> call = service.getDebtCapacity(body);
            call.enqueue(new Callback<BaseResponse<ResponseTopes>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseTopes>> call, Response<BaseResponse<ResponseTopes>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccess(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseTopes>> call, Throwable t) {
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
    public void getReasonsNotMeetsRequirements(RequestNoCumple body, FragmentSolicityContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<List<ResponseNoCumple>>> call = service.validateReasonsNotMeetsRequirements(body);
            call.enqueue(new Callback<BaseResponse<List<ResponseNoCumple>>>() {

                @Override
                public void onResponse(Call<BaseResponse<List<ResponseNoCumple>>> call, Response<BaseResponse<List<ResponseNoCumple>>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccess(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<List<ResponseNoCumple>>> call, Throwable t) {
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
    public void getTips(FragmentSolicityContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<List<ResponseTips>>> call = service.getTips();
            call.enqueue(new Callback<BaseResponse<List<ResponseTips>>>() {

                @Override
                public void onResponse(Call<BaseResponse<List<ResponseTips>>> call, Response<BaseResponse<List<ResponseTips>>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccess(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<List<ResponseTips>>> call, Throwable t) {
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
    public void sendLogs(RequestLogs body, FragmentSolicityContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
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
            e.printStackTrace();
        }
    }

    @Override
    public void getMoves(BaseRequest body, FragmentSolicityContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
                    .addConverterFactory(GsonConverterFactory.create())
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
                            listener.onSuccess(response);
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
}
