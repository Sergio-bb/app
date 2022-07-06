package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.MovementsCards.FragmentMovementsProductsCard;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseMovimientoProducto;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.request.RequestMovimientosProducto;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.MovementsCards.FragmentProductsCard.FragmentProductsCardContract;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public class FragmentMovementsProductsCardModel implements FragmentMovementsProductsCardContract.Model{

    @Override
    public void getAccounts(BaseRequest body, final FragmentMovementsProductsCardContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<List<ResponseProducto>>> call = service.getAccounts(body);
            call.enqueue(new Callback<BaseResponse<List<ResponseProducto>>>() {

                @Override
                public void onResponse(Call<BaseResponse<List<ResponseProducto>>> call, Response<BaseResponse<List<ResponseProducto>>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onErrorAccounts(response);
                        }else{
                            listener.onSuccessAccounts(response);
                        }
                    } else {
                        listener.onErrorAccounts(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<List<ResponseProducto>>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailureAccounts(t, true);
                    }else{
                        listener.onFailureAccounts(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onErrorAccounts(null);
        }
    }

    @Override
    public void getMovementsPresenteCards(RequestMovimientosProducto body, final FragmentMovementsProductsCardContract.APIListener listener) {
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
            Call<BaseResponse<List<ResponseMovimientoProducto>>> call = service.getMovementsAccounts(body);
            call.enqueue(new Callback<BaseResponse<List<ResponseMovimientoProducto>>>() {

                @Override
                public void onResponse(Call<BaseResponse<List<ResponseMovimientoProducto>>> call, Response<BaseResponse<List<ResponseMovimientoProducto>>> response) {
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
                public void onFailure(Call<BaseResponse<List<ResponseMovimientoProducto>>> call, Throwable t) {
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

