package solidappservice.cm.com.presenteapp.front.pagoobligaciones.FragmentPaymentCredits;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponserSolicitarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.pagoobligaciones.response.ResponsePagosPendientes;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.pagoobligaciones.request.RequestEnviarPago;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

/**
 * CREADO POR MIGUEL DAVID CABEZAS 17/09/2021.
 */
public class FragmentPaymentCreditsModel implements FragmentPaymentCreditsContract.Model{

    @Override
    public void getPendingPayments(BaseRequest body, final FragmentPaymentCreditsContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<List<ResponsePagosPendientes>>> call = service.getPendingPayments(body);
            call.enqueue(new Callback<BaseResponse<List<ResponsePagosPendientes>>>() {

                @Override
                public void onResponse(Call<BaseResponse<List<ResponsePagosPendientes>>> call, Response<BaseResponse<List<ResponsePagosPendientes>>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onErrorPendingPayments(response);
                        }else{
                            listener.onSuccessPendingPayments(response);
                        }
                    } else {
                        listener.onErrorPendingPayments(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<List<ResponsePagosPendientes>>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailurePendingPayments(t, true);
                    }else{
                        listener.onFailurePendingPayments(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onErrorPendingPayments(null);
        }
    }

    @Override
    public void getProducts(BaseRequest body, final FragmentPaymentCreditsContract.APIListener listener) {
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
                            listener.onErrorProducts(response);
                        }else{
                            listener.onSuccessProducts(response);
                        }
                    } else {
                        listener.onErrorProducts(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<List<ResponseProducto>>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailureProducts(t, true);
                    }else{
                        listener.onFailureProducts(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onErrorProducts(null);
        }
    }

    @Override
    public void makePayment(RequestEnviarPago body, final FragmentPaymentCreditsContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<String>> call = service.makePayment(body);
            call.enqueue(new Callback<BaseResponse<String>>() {

                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onErrorMakePayment(response);
                        }else{
                            listener.onSuccessMakePayment(response);
                        }
                    } else {
                        listener.onErrorMakePayment(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    if(t instanceof IOException){
                        BaseResponse<String> body = new BaseResponse<String>(
                                "",
                                "",
                                "",
                                "Tu solicitud ha sido enviada con éxito."
                        );
                        Response<BaseResponse<String>> response = Response.success(body);
                        listener.onSuccessMakePayment(response);
                    }else{
                        if(t instanceof IOException){
                            listener.onFailureMakePayment(t, true);
                        }else{
                            listener.onFailureMakePayment(t, false);
                        }
                    }
                }
            });
        } catch (Exception e) {
            listener.onErrorMakePayment(null);
        }
    }

}
