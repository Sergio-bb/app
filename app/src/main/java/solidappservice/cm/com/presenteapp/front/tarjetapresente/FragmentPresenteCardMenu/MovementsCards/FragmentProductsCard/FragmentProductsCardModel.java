package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.MovementsCards.FragmentProductsCard;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseTarjeta;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public class FragmentProductsCardModel implements FragmentProductsCardContract.Model{

    @Override
    public void getPresenteCards(BaseRequest body, final FragmentProductsCardContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<List<ResponseTarjeta>>> call = service.getPresenteCards(body);
            call.enqueue(new Callback<BaseResponse<List<ResponseTarjeta>>>() {

                @Override
                public void onResponse(Call<BaseResponse<List<ResponseTarjeta>>> call, Response<BaseResponse<List<ResponseTarjeta>>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onErrorPresenteCards(response);
                        }else{
                            listener.onSuccessPresenteCards(response);
                        }
                    } else {
                        listener.onErrorPresenteCards(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<List<ResponseTarjeta>>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailurePresenteCards(t, true);
                    }else{
                        listener.onFailurePresenteCards(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onErrorPresenteCards(null);
        }
    }

}
