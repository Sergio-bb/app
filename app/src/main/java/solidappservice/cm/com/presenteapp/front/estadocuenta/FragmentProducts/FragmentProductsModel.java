package solidappservice.cm.com.presenteapp.front.estadocuenta.FragmentProducts;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.parametrosemail.ResponseParametrosEmail;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public class FragmentProductsModel implements FragmentProductsContract.Model{

    @Override
    public void getStatusMessageMisAportes(final FragmentProductsContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseParametrosEmail>> call = service.getStatusMessageMisAportes();
            call.enqueue(new Callback<BaseResponse<ResponseParametrosEmail>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseParametrosEmail>> call, Response<BaseResponse<ResponseParametrosEmail>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getDescripcionError() != null && !response.body().getDescripcionError().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccess(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseParametrosEmail>> call, Throwable t) {
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
