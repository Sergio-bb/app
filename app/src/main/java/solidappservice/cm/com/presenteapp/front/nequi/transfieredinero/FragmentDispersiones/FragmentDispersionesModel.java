package solidappservice.cm.com.presenteapp.front.nequi.transfieredinero.FragmentDispersiones;

import com.google.gson.Gson;

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
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestPaymentDispersion;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestReversePaymentDispersion;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultaSuscripcion;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultarTopes;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiGeneral;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponsePrueba;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseReversePaymentDispersion;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apinequi.ApiNequi;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

public class FragmentDispersionesModel implements FragmentDispersionesContract.Model{

    OkHttpClient client = new OkHttpClient().newBuilder()
            .readTimeout(100, TimeUnit.SECONDS)
            .connectTimeout(100, TimeUnit.SECONDS)
            .build();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(NetworkHelper.DIRECCION_WS)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    ApiPresente service = retrofit.create(ApiPresente.class);


    Retrofit retrofitNequi = new Retrofit.Builder()
            .baseUrl(NetworkHelper.NEQUI_WS)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();
    ApiNequi serviceNequi = retrofitNequi.create(ApiNequi.class);


    @Override
    public void getAccountsAvailable(final FragmentDispersionesContract.APIListener listener) {
        try {
            Call<ResponseNequiGeneral> call = serviceNequi.getAccountsAvailable();
            call.enqueue(new Callback<ResponseNequiGeneral>() {
                @Override
                public void onResponse(Call<ResponseNequiGeneral> call, Response<ResponseNequiGeneral> response) {
                    if (response.isSuccessful()) {
                        listener.onSuccessAccountsAvailable(response);
                    } else {
                        listener.onErrorAccountsAvailable(null);
                    }
                }
                @Override
                public void onFailure(Call<ResponseNequiGeneral> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailure(t, true);
                    }else{
                        listener.onFailure(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onErrorAccountsAvailable(null);
        }
    }

    @Override
    public void getAccounts(BaseRequest body, final FragmentDispersionesContract.APIListener listener) {
        try {
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
                        listener.onFailure(t, true);
                    }else{
                        listener.onFailure(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onErrorAccounts(null);
        }
    }

    @Override
    public void getMaximumTranferValues(BaseRequest body, final FragmentDispersionesContract.APIListener listener) {
        try {
            Call<BaseResponseNequi<ResponseConsultarTopes>> call = serviceNequi.getMaximumTranferValues(body);
            call.enqueue(new Callback<BaseResponseNequi<ResponseConsultarTopes>>() {
                @Override
                public void onResponse(Call<BaseResponseNequi<ResponseConsultarTopes>> call, Response<BaseResponseNequi<ResponseConsultarTopes>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().isResponse()){
                            listener.onSuccessMaximumTranferValues(response);
                        }else if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredTokenNequi(response);
                        }else{
                            listener.onErrorMaximumTranferValues(response);
                        }
                    } else {
                        listener.onErrorMaximumTranferValues(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponseNequi<ResponseConsultarTopes>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailure(t, true);
                    }else{
                        listener.onFailure(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onErrorMaximumTranferValues(null);
        }
    }

    @Override
    public void makePaymentDispersion(RequestPaymentDispersion body, FragmentDispersionesContract.APIListener listener) {
        try {
            Call<BaseResponseNequi<Object>> call = serviceNequi.tranferCuentanequi(body);
            call.enqueue(new Callback<BaseResponseNequi<Object>>() {
                @Override
                public void onResponse(Call<BaseResponseNequi<Object>> call, Response<BaseResponseNequi<Object>> response) {
                    if(response.isSuccessful() ){
                        if (response.body() != null && response.body().getResult() != null && response.body().isResponse()){
                            listener.onSuccessMakePaymentDispersion(response);
                        }else if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredTokenNequi(response);
                        }else {
                            listener.onErrorMakePaymentDispersion(response);
                        }
                    }else{
                        listener.onErrorMakePaymentDispersion(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponseNequi<Object>> call, Throwable t) {
                    listener.onErrorMakePaymentDispersion(null);
                }
            });
        } catch (Exception e) {
            listener.onErrorMakePaymentDispersion(null);
        }
    }

    @Override
    public void reverseNequiPaymentDispersion(RequestReversePaymentDispersion body, FragmentDispersionesContract.APIListener listener) {
        try {
            Call<BaseResponseNequi<ResponseReversePaymentDispersion>> call = serviceNequi.reverseNequiDispersion(body);
            call.enqueue(new Callback<BaseResponseNequi<ResponseReversePaymentDispersion>>() {
                @Override
                public void onResponse(Call<BaseResponseNequi<ResponseReversePaymentDispersion>> call, Response<BaseResponseNequi<ResponseReversePaymentDispersion>> response) {
                    if (response.isSuccessful()){
                        if(response.body() != null && response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredTokenNequi(response);
                        }else if(response.body() != null && response.body().getResult() != null){
                            listener.onSuccessReversePaymentDispersion(response);
                        }else{
                            listener.onErrorReversePaymentDispersion(response);
                        }
                    }else {
                        listener.onErrorReversePaymentDispersion(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponseNequi<ResponseReversePaymentDispersion>> call, Throwable t) {
                    listener.onErrorReversePaymentDispersion(null);
                }
            });
        } catch (Exception e) {
            listener.onErrorReversePaymentDispersion(null);
        }
    }

    @Override
    public void getCobroPorSuscripcion(FragmentDispersionesContract.APIListener listener) {
        Call <Integer> call = serviceNequi.getCostoPorOperacion();
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null ) {
                        listener.onSuccesGetCoborPorOperacion(response);
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }
    @Override
    public void getSuscriptionNequi(BaseRequest body, final FragmentDispersionesContract.APIListener  listener) {
        try {
            Retrofit retrofitNequi2 = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.NEQUI_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            ApiNequi serviceNequi2 = retrofitNequi2.create(ApiNequi.class);
            Call<BaseResponseNequi<ResponseConsultaSuscripcion>> call = serviceNequi2.getSuscription(body);
            call.enqueue(new Callback<BaseResponseNequi<ResponseConsultaSuscripcion>>() {
                @Override
                public void onResponse(Call<BaseResponseNequi<ResponseConsultaSuscripcion>> call, Response<BaseResponseNequi<ResponseConsultaSuscripcion>> response) {
                    if(response.isSuccessful()){
                        assert response.body() != null;
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredTokenNequi(response);
                        }else if(response.isSuccessful()){
                            listener.onSuccessGetSuscriptionNequi(response);
                        }
                    }
                }
                @Override
                public void onFailure(Call<BaseResponseNequi<ResponseConsultaSuscripcion>> call, Throwable t) {
                    String error = t.getMessage();
                }
            });
        } catch (Exception e) {
            String error  = e.getMessage();
            e.printStackTrace();
        }
    }

}
