package solidappservice.cm.com.presenteapp.front.estadocuenta.FragmentStatusAccount;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseConsultaAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestActualizarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestConsultarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseMovimientos;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.mensajes.request.RequestEnviarMensaje;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseGetAuthorizacionBalance;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiBalance;
import solidappservice.cm.com.presenteapp.front.nequi.transfieredinero.FragmentSuscriptionsPayment.FragmentSuscriptionsPaymentContract;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apinequi.ApiNequi;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public class FragmentStatusAccountModel implements FragmentStatusAccountContract.Model{

    @Override
    public void getSalaryAdvanceMovements(BaseRequest body, final FragmentStatusAccountContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<List<ResponseMovimientos>>> call = service.getMoves(body);
            call.enqueue(new Callback<BaseResponse<List<ResponseMovimientos>>>() {

                @Override
                public void onResponse(Call<BaseResponse<List<ResponseMovimientos>>> call, Response<BaseResponse<List<ResponseMovimientos>>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onErrorSalaryAdvance(response);
                        }else{
                            listener.onSuccessSalaryAdvanceMovements(response);
                        }
                    } else {
                        listener.onErrorSalaryAdvance(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<List<ResponseMovimientos>>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailureSalaryAdvance(t, true);
                    }else{
                        listener.onFailureSalaryAdvance(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onErrorSalaryAdvance(null);
        }
    }

    @Override
    public void processSalaryAdvancePending(RequestConsultarAdelantoNomina body, final FragmentStatusAccountContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseConsultaAdelantoNomina>> call = service.checkSalaryAdvance(body);
            call.enqueue(new Callback<BaseResponse<ResponseConsultaAdelantoNomina>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseConsultaAdelantoNomina>> call, Response<BaseResponse<ResponseConsultaAdelantoNomina>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onErrorSalaryAdvance(response);
                        }else{
                            listener.onSuccessProcessSalaryAdvancePending(response);
                        }
                    } else {
                        listener.onErrorSalaryAdvance(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseConsultaAdelantoNomina>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailureSalaryAdvance(t, true);
                    }else{
                        listener.onFailureSalaryAdvance(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onErrorSalaryAdvance(null);
        }
    }

    @Override
    public void updateSalaryAdvanceStatus(RequestActualizarAdelantoNomina body, final FragmentStatusAccountContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<String>> call = service.updateSalaryAdvance(body);
            call.enqueue(new Callback<BaseResponse<String>>() {

                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onErrorSalaryAdvance(response);
                        }else{
                            listener.onSuccessUpdateSalaryAdvanceStatus(response);
                        }
                    } else {
                        listener.onErrorSalaryAdvance(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailureSalaryAdvance(t, true);
                    }else{
                        listener.onFailureSalaryAdvance(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onErrorSalaryAdvance(null);
        }
    }

    @Override
    public void sendSalaryAdvanceNotification(RequestEnviarMensaje body, final FragmentStatusAccountContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<String>> call = service.sendNotification(body);
            call.enqueue(new Callback<BaseResponse<String>>() {

                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onErrorSalaryAdvance(response);
                        }else{
                            response.body().setResultado("MENSAJE");
                            listener.onSuccessSendSalaryAdvanceNotification(response);
                        }
                    } else {
                        listener.onErrorSalaryAdvance(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailureSalaryAdvance(t, true);
                    }else{
                        listener.onFailureSalaryAdvance(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onErrorSalaryAdvance(null);
        }
    }

    @Override
    public void getAccountStatus(BaseRequest body, final FragmentStatusAccountContract.APIListener listener) {
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
                            listener.onErrorAccountStatus(response);
                        }else{
                            listener.onSuccessAccountStatus(response);
                        }
                    } else {
                        listener.onErrorAccountStatus(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<List<ResponseProducto>>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailureAccountStatus(t, true);
                    }else{
                        listener.onFailureAccountStatus(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onErrorAccountStatus(null);
        }
    }

    @Override
    public void getNequiBalance(BaseRequestNequi body, final FragmentStatusAccountContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.NEQUI_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiNequi service = retrofit.create(ApiNequi.class);
            Call<BaseResponseNequi<ResponseNequiBalance>> call = service.getNequiBalance(body);
            call.enqueue(new Callback<BaseResponseNequi<ResponseNequiBalance>>() {

                @Override
                public void onResponse(Call<BaseResponseNequi<ResponseNequiBalance>> call, Response<BaseResponseNequi<ResponseNequiBalance>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().isResponse()){
                            listener.onSuccessNequiBalance(response);
                        }else if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredTokenNequi(response);
                        }else{
                            listener.onErrorNequiBalance(response);
                        }
                    } else {
                        listener.onErrorNequiBalance(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponseNequi<ResponseNequiBalance>> call, Throwable t) {
                    listener.onErrorNequiBalance(null);
                }
            });
        } catch (Exception e) {
            listener.onErrorNequiBalance(null);
        }
    }

    @Override
    public void getAuthorizationNequiBalance(BaseRequestNequi body, final FragmentStatusAccountContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.NEQUI_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiNequi service = retrofit.create(ApiNequi.class);
            Call<BaseResponseNequi<ResponseGetAuthorizacionBalance>> call = service.getAuthorizationNequiBalance(body);
            call.enqueue(new Callback<BaseResponseNequi<ResponseGetAuthorizacionBalance>>() {

                @Override
                public void onResponse(Call<BaseResponseNequi<ResponseGetAuthorizacionBalance>> call, Response<BaseResponseNequi<ResponseGetAuthorizacionBalance>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().isResponse()){
                            listener.onSuccessAuthorizationNequiBalance(response);
                        }else if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredTokenNequi(response);
                        }else{
                            listener.onErrorAuthorizationNequiBalance(response);
                        }
                    } else {
                        listener.onErrorAuthorizationNequiBalance(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponseNequi<ResponseGetAuthorizacionBalance>> call, Throwable t) {
                    listener.onErrorAuthorizationNequiBalance(null);
                }
            });
        } catch (Exception e) {
            listener.onErrorAuthorizationNequiBalance(null);
        }
    }

}
