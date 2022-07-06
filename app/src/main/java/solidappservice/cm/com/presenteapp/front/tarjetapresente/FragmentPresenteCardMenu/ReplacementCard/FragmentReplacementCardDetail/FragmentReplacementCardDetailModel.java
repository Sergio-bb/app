package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.ReplacementCard.FragmentReplacementCardDetail;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.request.RequestMensajeReposicionTarjeta;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.request.RequestReposicionTarjeta;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseDependenciasAsociado;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseValorReposicionTarjeta;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public class FragmentReplacementCardDetailModel implements FragmentReplacementCardDetailContract.Model{

    @Override
    public void getAssociatedDependence(BaseRequest body, final FragmentReplacementCardDetailContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseDependenciasAsociado>> call = service.getAssociatedDependence(body);
            call.enqueue(new Callback<BaseResponse<ResponseDependenciasAsociado>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseDependenciasAsociado>> call, Response<BaseResponse<ResponseDependenciasAsociado>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onErrorAssociatedDependence(response);
                        }else{
                            listener.onSuccessAssociatedDependence(response);
                        }
                    } else {
                        listener.onErrorAssociatedDependence(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseDependenciasAsociado>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailureAssociatedDependence(t, true);
                    }else{
                        listener.onFailureAssociatedDependence(t, false);
                    }

                }
            });
        } catch (Exception e) {
            listener.onErrorAssociatedDependence(null);
        }
    }

    @Override
    public void getReplacementCardValue(final FragmentReplacementCardDetailContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseValorReposicionTarjeta>> call = service.getReplacementCardValue();
            call.enqueue(new Callback<BaseResponse<ResponseValorReposicionTarjeta>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseValorReposicionTarjeta>> call, Response<BaseResponse<ResponseValorReposicionTarjeta>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onErrorReplacementCardValue(response);
                        }else{
                            listener.onSuccessReplacementCardValue(response);
                        }
                    } else {
                        listener.onErrorReplacementCardValue(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseValorReposicionTarjeta>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailureReplacementCardValue(t, true);
                    }else{
                        listener.onFailureReplacementCardValue(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onErrorReplacementCardValue(null);
        }
    }


    @Override
    public void solicityReplacementCard(RequestReposicionTarjeta body, final FragmentReplacementCardDetailContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<String>> call = service.solicityReplacementCard(body);
            call.enqueue(new Callback<BaseResponse<String>>() {

                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onErrorSolicityReplacementCard(response);
                        }else{
                            listener.onSuccessSolicityReplacementCard(response);
                        }
                    } else {
                        listener.onErrorSolicityReplacementCard(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailureSolicityReplacementCard(t, true);
                    }else{
                        listener.onFailureSolicityReplacementCard(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onErrorSolicityReplacementCard(null);
        }
    }

    @Override
    public void sendMessageCardReplacementSuccessful(RequestMensajeReposicionTarjeta body, final FragmentReplacementCardDetailContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<String>> call = service.sendMessageCardReplacementSuccessful(body);
            call.enqueue(new Callback<BaseResponse<String>>() {

                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
//                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
//                            listener.onError(response);
                        }else{
//                            listener.onSuccess(response);
                        }
                    } else {
//                        response.body().setDescripcionError("Error: "+response.code());
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

}
