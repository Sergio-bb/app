package solidappservice.cm.com.presenteapp.front.convenios.FragmentListAgrementsProducts;

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
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseConvenios;
import solidappservice.cm.com.presenteapp.entities.convenios.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apiconvenios.ApiConvenios;
import solidappservice.cm.com.presenteapp.rest.retrofit.apiconvenios.ConveniosRestClient;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public class FragmentListAgrementsProductsModel implements FragmentListAgrementsProductsContract.Model{

    @Override
    public void getProductsByAgreements(BaseRequest body, String idAgreement, final FragmentListAgrementsProductsContract.APIListener listener) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.CONVENIOS_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            ApiConvenios service = retrofit.create(ApiConvenios.class);
            Call<BaseResponseConvenios<List<ResponseProducto>>> call = service.listarProductos(body.token, body.cedula, idAgreement);
            call.enqueue(new Callback<BaseResponseConvenios<List<ResponseProducto>>>() {

                @Override
                public void onResponse(Call<BaseResponseConvenios<List<ResponseProducto>>> call, Response<BaseResponseConvenios<List<ResponseProducto>>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().isErrorAutenticacion()){
                            response.body().setDescripcionError(ConveniosRestClient.TOKEN_EXCEPTION);
                            listener.onExpiredToken(response);
                        } else if(response.body().isExitoso()){
                            listener.onSuccess(response);
                        } else{
                            response.body().setDescripcionError(ConveniosRestClient.SERVER_ERROR);
                            listener.onError(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponseConvenios<List<ResponseProducto>>> call, Throwable t) {
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
