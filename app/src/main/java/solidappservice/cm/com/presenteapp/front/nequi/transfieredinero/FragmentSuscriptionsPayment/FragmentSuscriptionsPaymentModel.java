package solidappservice.cm.com.presenteapp.front.nequi.transfieredinero.FragmentSuscriptionsPayment;

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
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestCheckStatusPaymentSuscription;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestPaymentSuscritpion;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestReversePaymentSuscription;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiGeneral;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseCheckStatusPaymentSuscription;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultarTopes;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseGetAuthorizacionBalance;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiBalance;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponsePaymentSuscription;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseReversePaymentSuscriptions;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apinequi.ApiNequi;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

public class FragmentSuscriptionsPaymentModel implements FragmentSuscriptionsPaymentContract.Model{

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
    public void getAccountsAvailable(final FragmentSuscriptionsPaymentContract.APIListener listener) {
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
    public void getAccounts(BaseRequest body, final FragmentSuscriptionsPaymentContract.APIListener listener) {
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
    public void getIncompleteSubscriptionPayments(BaseRequest body, final FragmentSuscriptionsPaymentContract.APIListener listener) {
        try {
            Call<BaseResponseNequi<String>> call = serviceNequi.getIncompleteSubscriptionPayments(body);
            call.enqueue(new Callback<BaseResponseNequi<String>>() {
                @Override
                public void onResponse(Call<BaseResponseNequi<String>> call, Response<BaseResponseNequi<String>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().isResponse()){
                            listener.onSuccessIncompleteSubscriptionPayments(response);
                        }else if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredTokenNequi(response);
                        }else{
                            listener.onErrorIncompleteSubscriptionPayments(response);
                        }
                    } else {
                        listener.onErrorIncompleteSubscriptionPayments(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponseNequi<String>> call, Throwable t) {
                    listener.onErrorIncompleteSubscriptionPayments(null);
                }
            });
        } catch (Exception e) {
            listener.onErrorIncompleteSubscriptionPayments(null);
        }
    }

    @Override
    public void getMaximumTranferValues(BaseRequest body, final FragmentSuscriptionsPaymentContract.APIListener listener) {
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
    public void getNequiBalance(BaseRequestNequi body, final FragmentSuscriptionsPaymentContract.APIListener listener) {
        try {
            Call<BaseResponseNequi<ResponseNequiBalance>> call = serviceNequi.getNequiBalance(body);
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
    public void getAuthorizationNequiBalance(BaseRequestNequi body, final FragmentSuscriptionsPaymentContract.APIListener listener) {
        try {
            Call<BaseResponseNequi<ResponseGetAuthorizacionBalance>> call = serviceNequi.getAuthorizationNequiBalance(body);
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

    @Override
    public void makePaymentSuscription(RequestPaymentSuscritpion body, FragmentSuscriptionsPaymentContract.APIListener listener) {
        try {
            Call<BaseResponseNequi<ResponsePaymentSuscription>> call = serviceNequi.makePaymentSuscription(body);
            call.enqueue(new Callback<BaseResponseNequi<ResponsePaymentSuscription>>() {

                @Override
                public void onResponse(Call<BaseResponseNequi<ResponsePaymentSuscription>> call, Response<BaseResponseNequi<ResponsePaymentSuscription>> response) {
                    if (response.isSuccessful()){
                        if(response.body() != null && response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredTokenNequi(response);
                        }else if(response.body() != null && response.body().getResult() != null){
                            listener.onSuccessPaymentSuscription(response, response.body().isResponse());
                        }else{
                            listener.onErrorPaymentSuscription(response);
                        }
                    }else {
                        listener.onErrorPaymentSuscription(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponseNequi<ResponsePaymentSuscription>> call, Throwable t) {
                    listener.onErrorPaymentSuscription(null);
                }

            });
        } catch (Exception e) {
            listener.onErrorPaymentSuscription(null);
        }
    }

    @Override
    public void reverseNequiSubscriptions(RequestReversePaymentSuscription body, FragmentSuscriptionsPaymentContract.APIListener listener) {
        try {
            ApiNequi service = retrofit.create(ApiNequi.class);
            Call<BaseResponseNequi<ResponseReversePaymentSuscriptions>> call = service.reverseNequiSubscriptions(body);
            call.enqueue(new Callback<BaseResponseNequi<ResponseReversePaymentSuscriptions>>() {

                @Override
                public void onResponse(Call<BaseResponseNequi<ResponseReversePaymentSuscriptions>> call, Response<BaseResponseNequi<ResponseReversePaymentSuscriptions>> response) {
                    Log.i("response", String.valueOf(response));
                    if (response.isSuccessful()){
                        if(response.body() != null && response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredTokenNequi(response);
                        }else if(response.body() != null && response.body().getResult() != null){
                            listener.onSuccessReversePaymentSuscription(response);
                        }else{
                            listener.onErrorReversePaymentSuscription(response);
                        }
                    }else {
                        listener.onErrorReversePaymentSuscription(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponseNequi<ResponseReversePaymentSuscriptions>> call, Throwable t) {
                    listener.onErrorReversePaymentSuscription(null);
                }

            });
        } catch (Exception e) {
            listener.onErrorReversePaymentSuscription(null);
        }
    }

    @Override
    public void checkStatusPaymentSubscription(RequestCheckStatusPaymentSuscription body, FragmentSuscriptionsPaymentContract.APIListener listener) {
        try {
            Call<BaseResponseNequi<ResponseCheckStatusPaymentSuscription>> call = serviceNequi.checkStatusPaymentSubscription(body);
            call.enqueue(new Callback<BaseResponseNequi<ResponseCheckStatusPaymentSuscription>>() {

                @Override
                public void onResponse(Call<BaseResponseNequi<ResponseCheckStatusPaymentSuscription>> call, Response<BaseResponseNequi<ResponseCheckStatusPaymentSuscription>> response) {
                    Log.i("response", String.valueOf(response));
                    if (response.isSuccessful()){
                        if(response.body() != null && response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredTokenNequi(response);
                        }else if(response.body() != null && response.body().getResult() != null){
                            listener.onSuccessCheckStatusPaymentSubscription(response, response.body().isResponse());
                        }else{
                            listener.onErrorCheckStatusPaymentSubscription(response);
                        }
                    }else {
                        listener.onErrorCheckStatusPaymentSubscription(null);
                    }
                }

                @Override
                public void onFailure(Call<BaseResponseNequi<ResponseCheckStatusPaymentSuscription>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailure(t, true);
                    }else{
                        listener.onFailure(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onErrorCheckStatusPaymentSubscription(null);
        }
    }
}
