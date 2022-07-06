package solidappservice.cm.com.presenteapp.front.login.ActivityValidateCode;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.RequestActualizarDatos;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.RequestEnviarCodigo;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.RequestValidarCodigo;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseEnviarCodigo;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseValidarCodigo;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.dispositivo.apirequest.Dispositivo;
import solidappservice.cm.com.presenteapp.entities.dispositivo.apiresponse.ResponseRegistrarDispositivo;
import solidappservice.cm.com.presenteapp.entities.tyc.request.RequestAceptaTyC;
import solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentVerifyCode.FragmentVerifyCodeContract;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 21/09/2021.
 */
public class ActivityValidateCodeModel implements ActivityValidateCodeContract.Model{

    @Override
    public void sendVerificationCode(RequestEnviarCodigo body, ActivityValidateCodeContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseEnviarCodigo>> call = service.sendVerificationCodeEmail(body);
            call.enqueue(new Callback<BaseResponse<ResponseEnviarCodigo>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseEnviarCodigo>> call, Response<BaseResponse<ResponseEnviarCodigo>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessSendVerificationCode(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseEnviarCodigo>> call, Throwable t) {
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
    public void validateVerificationCode(RequestValidarCodigo body, ActivityValidateCodeContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseValidarCodigo>> call = service.validateVerificationCode(body);
            call.enqueue(new Callback<BaseResponse<ResponseValidarCodigo>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseValidarCodigo>> call, Response<BaseResponse<ResponseValidarCodigo>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessValidateVerificationCode(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseValidarCodigo>> call, Throwable t) {
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
    public void registerDevice(Dispositivo body, final ActivityValidateCodeContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseRegistrarDispositivo>> call = service.registerDevice(body);
            call.enqueue(new Callback<BaseResponse<ResponseRegistrarDispositivo>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseRegistrarDispositivo>> call, Response<BaseResponse<ResponseRegistrarDispositivo>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessRegisterDevice(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseRegistrarDispositivo>> call, Throwable t) {
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

}
