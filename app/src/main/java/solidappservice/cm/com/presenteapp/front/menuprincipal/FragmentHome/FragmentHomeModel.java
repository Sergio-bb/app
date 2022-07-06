package solidappservice.cm.com.presenteapp.front.menuprincipal.FragmentHome;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.banercomercial.response.ResponseBanerComercial;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.mensajes.response.ResponseObtenerMensajes;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseParametrosAPP;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 17/09/2021.
 */
public class FragmentHomeModel implements FragmentHomeContract.Model{

    @Override
    public void getButtonStateAgreements(FragmentHomeContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseParametrosAPP>> call = service.getButtonStateAgreements();
            call.enqueue(new Callback<BaseResponse<ResponseParametrosAPP>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseParametrosAPP>> call, Response<BaseResponse<ResponseParametrosAPP>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessButtonStateAgreements(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseParametrosAPP>> call, Throwable t) {
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
    public void getCommercialBanner(BaseRequest body, final FragmentHomeContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<List<ResponseBanerComercial>>> call = service.getCommercialBanner(body);
            call.enqueue(new Callback<BaseResponse<List<ResponseBanerComercial>>>() {

                @Override
                public void onResponse(Call<BaseResponse<List<ResponseBanerComercial>>> call, Response<BaseResponse<List<ResponseBanerComercial>>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessCommercialBanner(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<List<ResponseBanerComercial>>> call, Throwable t) {
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
    public void getMessageInbox(BaseRequest body, final FragmentHomeContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<List<ResponseObtenerMensajes>>> call = service.getMessages(body);
            call.enqueue(new Callback<BaseResponse<List<ResponseObtenerMensajes>>>() {

                @Override
                public void onResponse(Call<BaseResponse<List<ResponseObtenerMensajes>>> call, Response<BaseResponse<List<ResponseObtenerMensajes>>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessMessageInbox(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<List<ResponseObtenerMensajes>>> call, Throwable t) {
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
