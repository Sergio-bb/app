package solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentEditData;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.*;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 11/07/2021.
 */
public class FragmentEditDataModel implements FragmentEditDataContract.Model{

    @Override
    public void getPersonalData(BaseRequest body, final FragmentEditDataContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseConsultarDatosAsociado>> call = service.getPersonalData(body);
            call.enqueue(new Callback<BaseResponse<ResponseConsultarDatosAsociado>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseConsultarDatosAsociado>> call, Response<BaseResponse<ResponseConsultarDatosAsociado>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessPersonalData(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseConsultarDatosAsociado>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailure(t, true);
                    }else{
                        listener.onFailure(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onFailure(null, false);
        }
    }

    @Override
    public void getLocations(final FragmentEditDataContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseUbicaciones>> call = service.getLocations();
            call.enqueue(new Callback<BaseResponse<ResponseUbicaciones>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseUbicaciones>> call, Response<BaseResponse<ResponseUbicaciones>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessLocations(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseUbicaciones>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailure(t, true);
                    }else{
                        listener.onFailure(t, false);
                    }
                }
            });

        } catch (Exception e) {
            listener.onFailure(null, false);
        }
    }

    @Override
    public void getAddressFormat(final FragmentEditDataContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseFormatoDirecciones>> call = service.getAddressFormat();
            call.enqueue(new Callback<BaseResponse<ResponseFormatoDirecciones>>() {
                @Override
                public void onResponse(Call<BaseResponse<ResponseFormatoDirecciones>> call, Response<BaseResponse<ResponseFormatoDirecciones>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessAddressFormat(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseFormatoDirecciones>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailure(t, true);
                    }else{
                        listener.onFailure(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onFailure(null, false);
        }
    }

}
