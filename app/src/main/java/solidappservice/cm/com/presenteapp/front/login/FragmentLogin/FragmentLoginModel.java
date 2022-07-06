package solidappservice.cm.com.presenteapp.front.login.FragmentLogin;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.dispositivo.apirequest.Dispositivo;
import solidappservice.cm.com.presenteapp.entities.dispositivo.apiresponse.ResponseValidarDispositivo;
import solidappservice.cm.com.presenteapp.entities.imagelogin.ResponseImageLogin;
import solidappservice.cm.com.presenteapp.entities.login.Request.RequestLogin;
import solidappservice.cm.com.presenteapp.entities.login.Response.Usuario;
import solidappservice.cm.com.presenteapp.entities.mensajesbanner.ResponseMensajesBanner;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 20/09/2021.
 */
public class FragmentLoginModel implements FragmentLoginContract.Model{

    @Override
    public void validateLogin(RequestLogin body, final FragmentLoginContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<Usuario>> call = service.validateLogin(body);
            call.enqueue(new Callback<BaseResponse<Usuario>>() {

                @Override
                public void onResponse(Call<BaseResponse<Usuario>> call, Response<BaseResponse<Usuario>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onErrorValidateLogin(response);
                        }else{
                            listener.onSuccessValidateLogin(body, response);
                        }
                    } else {
                        listener.onErrorValidateLogin(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<Usuario>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailure(t, true);
                    }else{
                        listener.onFailure(t, false);
                    }

                }
            });
        } catch (Exception e) {
            listener.onErrorValidateLogin(null);
        }
    }

    @Override
    public void getBannerMessages(final FragmentLoginContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<List<ResponseMensajesBanner>>> call = service.getBannerMessages();
            call.enqueue(new Callback<BaseResponse<List<ResponseMensajesBanner>>>() {

                @Override
                public void onResponse(Call<BaseResponse<List<ResponseMensajesBanner>>> call, Response<BaseResponse<List<ResponseMensajesBanner>>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessBannerMessages(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<List<ResponseMensajesBanner>>> call, Throwable t) {
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
    public void getLoginImage(final FragmentLoginContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseImageLogin>> call = service.getLoginImage();
            call.enqueue(new Callback<BaseResponse<ResponseImageLogin>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseImageLogin>> call, Response<BaseResponse<ResponseImageLogin>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessLoginImage(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseImageLogin>> call, Throwable t) {
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
    public void validateRegisterDevice(Dispositivo body, final FragmentLoginContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseValidarDispositivo>> call = service.validateRegisterDevice(body);
            call.enqueue(new Callback<BaseResponse<ResponseValidarDispositivo>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseValidarDispositivo>> call, Response<BaseResponse<ResponseValidarDispositivo>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessValidateRegisterDevice(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseValidarDispositivo>> call, Throwable t) {
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
