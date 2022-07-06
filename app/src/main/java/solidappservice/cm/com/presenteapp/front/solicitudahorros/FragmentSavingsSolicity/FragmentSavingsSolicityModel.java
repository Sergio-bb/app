package solidappservice.cm.com.presenteapp.front.solicitudahorros.FragmentSavingsSolicity;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.solicitudahorros.response.ResponseTiposAhorro;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.solicitudahorros.request.RequestEnviarSolicitudAhorro;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

/**
 * CREADO POR MIGUEL DAVID CABEZAS 16/09/2021.
 **/
public class FragmentSavingsSolicityModel implements FragmentSavingsSolicityContract.Model{

    @Override
    public void getTypesOfSavings(BaseRequest body, final FragmentSavingsSolicityContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<List<ResponseTiposAhorro>>> call = service.getTypesOfSavings(body);
            call.enqueue(new Callback<BaseResponse<List<ResponseTiposAhorro>>>() {

                @Override
                public void onResponse(Call<BaseResponse<List<ResponseTiposAhorro>>> call, Response<BaseResponse<List<ResponseTiposAhorro>>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessTypesOfSavings(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<List<ResponseTiposAhorro>>> call, Throwable t) {
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
    public void solicitySaving(RequestEnviarSolicitudAhorro body, final FragmentSavingsSolicityContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<String>> call = service.solicitySavings(body);
            call.enqueue(new Callback<BaseResponse<String>>() {

                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessSolicitySaving(response);
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

}
