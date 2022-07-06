package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityPortfolio.ActivityPortfolioProducts;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponsePortafolio;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 28/08/2021.
 */
public class ActivityPortfolioProductsModel implements ActivityPortfolioProductsContract.Model{

    @Override
    public void getPortfolioProducts(final ActivityPortfolioProductsContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<List<ResponsePortafolio>>> call = service.getPortfolioProducts();
            call.enqueue(new Callback<BaseResponse<List<ResponsePortafolio>>>() {

                @Override
                public void onResponse(Call<BaseResponse<List<ResponsePortafolio>>> call, Response<BaseResponse<List<ResponsePortafolio>>> response) {
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
                public void onFailure(Call<BaseResponse<List<ResponsePortafolio>>> call, Throwable t) {
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
