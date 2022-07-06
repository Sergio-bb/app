package solidappservice.cm.com.presenteapp.front.nequi.qr.FragmentQRDetail;

import android.util.Log;

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
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestPaymentQR;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiGeneral;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultarTopes;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseMakePaymentQR;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apinequi.ApiNequi;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

public class FragmentQRDetailModel implements FragmentQRDetailContract.Model{

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
    public void getAccountsAvailable(final FragmentQRDetailContract.APIListener listener) {
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
    public void getAccounts(BaseRequest body, final FragmentQRDetailContract.APIListener listener) {
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
    public void getMaximumTranferValues(BaseRequest body, final FragmentQRDetailContract.APIListener listener) {
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
    public void makePaymentByQr(RequestPaymentQR body, final FragmentQRDetailContract.APIListener listener) {
        try {
            Call<BaseResponseNequi<ResponseMakePaymentQR>> call = serviceNequi.pagoQR(body);
            call.enqueue(new Callback<BaseResponseNequi<ResponseMakePaymentQR>>() {
                @Override
                public void onResponse(Call<BaseResponseNequi<ResponseMakePaymentQR>> call, Response<BaseResponseNequi<ResponseMakePaymentQR>> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().getResult() != null){
                            listener.onSuccessMakePaymentByQr(response);
                        }else if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredTokenNequi(response);
                        }else {
                            listener.onErrorMakePaymentByQr(response);
                        }
                    }else{
                        listener.onErrorMakePaymentByQr(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponseNequi<ResponseMakePaymentQR>> call, Throwable t) {
                    listener.onErrorMakePaymentByQr(null);
                }
            });
        } catch (Exception e) {
            listener.onErrorMakePaymentByQr(null);
        }
    }
}
