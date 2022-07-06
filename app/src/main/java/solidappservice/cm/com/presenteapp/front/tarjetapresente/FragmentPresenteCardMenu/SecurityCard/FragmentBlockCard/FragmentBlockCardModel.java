package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.SecurityCard.FragmentBlockCard;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.request.RequestBloquearTarjeta;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public class FragmentBlockCardModel implements FragmentBlockCardContract.Model{

    @Override
    public void getReasonsBlockCard(BaseRequest body, final FragmentBlockCardContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<List<String>>> call = service.getReasonsBlockCard(body);
            call.enqueue(new Callback<BaseResponse<List<String>>>() {

                @Override
                public void onResponse(Call<BaseResponse<List<String>>> call, Response<BaseResponse<List<String>>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onErrorReasonsBlockCard(response);
                        }else{
                            listener.onSuccessReasonsBlockCard(response);
                        }
                    } else {
                        listener.onErrorReasonsBlockCard(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<List<String>>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailureReasonsBlockCard(t, true);
                    }else{
                        listener.onFailureReasonsBlockCard(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onErrorReasonsBlockCard(null);
        }
    }

    @Override
    public void blockCard(RequestBloquearTarjeta body, final FragmentBlockCardContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<String>> call = service.blockCard(body);
            call.enqueue(new Callback<BaseResponse<String>>() {

                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onErrorBlockCard(response);
                        }else{
                            listener.onSuccessBlockCard(response);
                        }
                    } else {
                        listener.onErrorBlockCard(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    listener.onFailureBlockCard(t, t instanceof IOException);
                }
            });
        } catch (Exception e) {
            listener.onErrorBlockCard(null);
        }
    }

}
