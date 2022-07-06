package solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.movilexito.FragmentTransactionSummary;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.datosasociado.response.ResponseDatosBasicosAsociado;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProductosV2;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestAccessToken;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestActualizarRecarga;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestEmail;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestRealizarPago;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestResumenTransaccion;
import solidappservice.cm.com.presenteapp.entities.movilexito.response.ResponseAccessTokenOAuthME;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestRealizarRecargar;
import solidappservice.cm.com.presenteapp.entities.movilexito.response.ResponseAccessTokenOAuthPresenteME;
import solidappservice.cm.com.presenteapp.entities.movilexito.response.ResponseActualizarRecarga;
import solidappservice.cm.com.presenteapp.entities.movilexito.response.ResponseRealizarPago;
import solidappservice.cm.com.presenteapp.entities.movilexito.response.ResponseRealizarRecarga;
import solidappservice.cm.com.presenteapp.entities.movilexito.response.ResponseResumenTransaccion;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseParametrosAPP;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apimovilexito.ApiMovilExito;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

public class FragmentTransactionSummaryModel implements FragmentTransactionSummaryContract.Model {

    private Retrofit retrofitApiPresente = new Retrofit.Builder()
            .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient client = new OkHttpClient().newBuilder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();

    private Retrofit retrofitApiPresenteME = new Retrofit.Builder()
            .baseUrl(NetworkHelper.URL_APIPRESENTEMOVILEXITO)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();

    @Override
    public void getDocumentType(BaseRequest body, final FragmentTransactionSummaryContract.APIListener listener) {
        try {
            ApiPresente apiPresente = retrofitApiPresente.create(ApiPresente.class);
            Call<BaseResponse<ResponseDatosBasicosAsociado>> call = apiPresente.getBasicPersonalData(body);
            call.enqueue(new Callback<BaseResponse<ResponseDatosBasicosAsociado>>() {
                @Override
                public void onResponse(Call<BaseResponse<ResponseDatosBasicosAsociado>> call, Response<BaseResponse<ResponseDatosBasicosAsociado>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()) {
                            listener.onExpiredToken(response);
                        } else if (response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()) {
                            listener.onErrorDocumentType(response);
                        } else {
                            listener.onSuccessDocumentType(response);
                        }
                    } else {
                        listener.onErrorDocumentType(response);
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<ResponseDatosBasicosAsociado>> call, Throwable t) {
                    listener.onErrorDocumentType(null);
                }
            });
        } catch (Exception e) {
            listener.onErrorDocumentType(null);
        }
    }

    @Override
    public void getSummaryTransaction(RequestResumenTransaccion body, RequestAccessToken accessToken, final FragmentTransactionSummaryContract.APIListener listener) {
        try {
            ApiMovilExito apiMovilExito = retrofitApiPresenteME.create(ApiMovilExito.class);
            String basicKeys = accessToken.getClientId() + ":" + accessToken.getClientSecret();
            Call<ResponseAccessTokenOAuthPresenteME> callAccessToken = apiMovilExito.getAccessTokenOAuthPresenteME(
                    "Basic " + Base64.encodeToString(basicKeys.getBytes(), Base64.NO_WRAP),
                    accessToken.getGrantType()
            );
            callAccessToken.enqueue(new Callback<ResponseAccessTokenOAuthPresenteME>() {
                @Override
                public void onResponse(Call<ResponseAccessTokenOAuthPresenteME> callAccessToken, Response<ResponseAccessTokenOAuthPresenteME> responseAcessToken) {
                    if (responseAcessToken.isSuccessful()) {
                        if (responseAcessToken.body().getAccess_token() != null && !responseAcessToken.body().getAccess_token().isEmpty()) {
                            Call<BaseResponse<ResponseResumenTransaccion>> callSummaryTransaction = apiMovilExito.getSummaryTransaction("Bearer " + responseAcessToken.body().getAccess_token(), body);
                            callSummaryTransaction.enqueue(new Callback<BaseResponse<ResponseResumenTransaccion>>() {
                                @Override
                                public void onResponse(Call<BaseResponse<ResponseResumenTransaccion>> call, Response<BaseResponse<ResponseResumenTransaccion>> responseSummaryTransaction) {
                                    if (responseSummaryTransaction.isSuccessful()) {
                                        if (responseSummaryTransaction.body().getErrorToken() != null && !responseSummaryTransaction.body().getErrorToken().isEmpty()) {
                                            listener.onExpiredToken(responseSummaryTransaction);
                                        } else if (responseSummaryTransaction.body().getMensajeErrorUsuario() != null && !responseSummaryTransaction.body().getMensajeErrorUsuario().isEmpty()) {
                                            listener.onErrorSummaryTransaction(responseSummaryTransaction);
                                        } else {
                                            listener.onSuccessSummaryTransaction(responseSummaryTransaction);
                                        }
                                    } else {
                                        listener.onErrorSummaryTransaction(responseSummaryTransaction);
                                    }
                                }

                                @Override
                                public void onFailure(Call<BaseResponse<ResponseResumenTransaccion>> callSummaryTransaction, Throwable t) {
                                    listener.onErrorSummaryTransaction(null);
                                }
                            });
                        } else {
                            listener.onErrorSummaryTransaction(null);
                        }
                    } else {
                        listener.onErrorSummaryTransaction(null);
                    }
                }

                @Override
                public void onFailure(Call<ResponseAccessTokenOAuthPresenteME> callAccessToken, Throwable t) {
                    listener.onErrorSummaryTransaction(null);
                }
            });
        } catch (Exception ex) {
            listener.onErrorSummaryTransaction(null);
        }
    }

    @Override
    public void getAccounts(BaseRequest body, final FragmentTransactionSummaryContract.APIListener listener) {
        try {
            ApiPresente apiPresente = retrofitApiPresente.create(ApiPresente.class);
            Call<BaseResponse<List<ResponseProductosV2>>> call = apiPresente.getAccountsV2(body);
            call.enqueue(new Callback<BaseResponse<List<ResponseProductosV2>>>() {
                @Override
                public void onResponse(Call<BaseResponse<List<ResponseProductosV2>>> call, Response<BaseResponse<List<ResponseProductosV2>>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()) {
                            listener.onExpiredToken(response);
                        } else if (response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()) {
                            listener.onErrorAccounts(response);
                        } else {
                            listener.onSuccessAccounts(response);
                        }
                    } else {
                        listener.onErrorAccounts(response);
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<List<ResponseProductosV2>>> call, Throwable t) {
                    listener.onErrorAccounts(null);
                }
            });
        } catch (Exception e) {
            listener.onErrorAccounts(null);
        }
    }

    @Override
    public void getDataBasicTokenPresenteME(final FragmentTransactionSummaryContract.APIListener listener) {
        try {
            ApiPresente apiPresente = retrofitApiPresente.create(ApiPresente.class);
            Call<BaseResponse<ResponseParametrosAPP>> call = apiPresente.getParamsAccessTokenPresenteME();
            call.enqueue(new Callback<BaseResponse<ResponseParametrosAPP>>() {
                @Override
                public void onResponse(Call<BaseResponse<ResponseParametrosAPP>> call, Response<BaseResponse<ResponseParametrosAPP>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getDescripcionError() != null && !response.body().getDescripcionError().isEmpty()) {
                            listener.onErrorGetDataBasicTokenPresenteME(response);
                        } else {
                            listener.onSuccessGetDataBasicTokenPresenteME(response);
                        }
                    } else {
                        listener.onErrorGetDataBasicTokenPresenteME(response);
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<ResponseParametrosAPP>> call, Throwable t) {
                    listener.onErrorGetDataBasicTokenPresenteME(null);
                }
            });
        } catch (Exception ex) {
            listener.onErrorGetDataBasicTokenPresenteME(null);
        }
    }

    @Override
    public void makePayment(RequestRealizarPago body, RequestAccessToken accessToken, final FragmentTransactionSummaryContract.APIListener listener) {
        try {
            ApiMovilExito apiMovilExito = retrofitApiPresenteME.create(ApiMovilExito.class);
            String basicKeys = accessToken.getClientId() + ":" + accessToken.getClientSecret();
            Call<ResponseAccessTokenOAuthPresenteME> callAccessToken = apiMovilExito.getAccessTokenOAuthPresenteME(
                    "Basic " + Base64.encodeToString(basicKeys.getBytes(), Base64.NO_WRAP),
                    accessToken.getGrantType()
            );
            callAccessToken.enqueue(new Callback<ResponseAccessTokenOAuthPresenteME>() {
                @Override
                public void onResponse(Call<ResponseAccessTokenOAuthPresenteME> callAccessToken, Response<ResponseAccessTokenOAuthPresenteME> responseAcessToken) {
                    if (responseAcessToken.isSuccessful()) {
                        if (responseAcessToken.body().getAccess_token() != null && !responseAcessToken.body().getAccess_token().isEmpty()) {
                            Call<BaseResponse<ResponseRealizarPago>> call = apiMovilExito.makePayment("Bearer " + responseAcessToken.body().getAccess_token(), body);
                            call.enqueue(new Callback<BaseResponse<ResponseRealizarPago>>() {
                                @Override
                                public void onResponse(Call<BaseResponse<ResponseRealizarPago>> call, Response<BaseResponse<ResponseRealizarPago>> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()) {
                                            listener.onExpiredToken(response);
                                        } else if (response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()) {
                                            listener.onErrorMakePayment(response);
                                        } else {
                                            listener.onSuccessMakePayment(response);
                                        }
                                    } else {
                                        listener.onErrorMakePayment(null);
                                    }
                                }

                                @Override
                                public void onFailure(Call<BaseResponse<ResponseRealizarPago>> call, Throwable t) {
                                    listener.onErrorMakePayment(null);
                                }
                            });
                        } else {
                            listener.onErrorMakePayment(null);
                        }
                    } else {
                        listener.onErrorMakePayment(null);
                    }
                }

                @Override
                public void onFailure(Call<ResponseAccessTokenOAuthPresenteME> callAccessToken, Throwable t) {
                    listener.onErrorMakePayment(null);
                }
            });
        } catch (Exception ex) {
            listener.onErrorMakePayment(null);
        }
    }

    @Override
    public void performLineRechargeME(RequestRealizarRecargar body, RequestAccessToken accessToken, InputStream certificate, String pass, final FragmentTransactionSummaryContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.URL_APIMOVILEXITO)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(provideUnsafeOkHttpClient(certificate, pass))
                    .build();
            ApiMovilExito apiMovilExito = retrofit.create(ApiMovilExito.class);

            Call<ResponseAccessTokenOAuthME> callAccessToken = apiMovilExito.getAccessTokenOAuthME(
                    accessToken.getClientId(),
                    accessToken.getClientSecret(),
                    accessToken.getGrantType(),
                    accessToken.getScope()
            );
            callAccessToken.enqueue(new Callback<ResponseAccessTokenOAuthME>() {
                @Override
                public void onResponse(Call<ResponseAccessTokenOAuthME> callAccessToken, Response<ResponseAccessTokenOAuthME> responseAcessToken) {
                    if (responseAcessToken.isSuccessful()) {
                        if (responseAcessToken.body() != null && responseAcessToken.body().getAccess_token() != null && !responseAcessToken.body().getAccess_token().isEmpty()) {

                            Call<ResponseRealizarRecarga> call = apiMovilExito.performLineRecharge("Bearer " + responseAcessToken.body().getAccess_token(), body);
                            call.enqueue(new Callback<ResponseRealizarRecarga>() {
                                @Override
                                public void onResponse(Call<ResponseRealizarRecarga> call, Response<ResponseRealizarRecarga> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body() != null && response.body().getEstado() != null && response.body().getEstado().equals("Exitoso")) {
                                            listener.onSuccessPerformLineRechargeME(response);
                                        } else {
                                            if (response.body() != null && response.body().getDescripcionError() != null && !response.body().getDescripcionError().equals("")) {
                                                listener.onErrorPerformLineRechargeME(response.body().getDescripcionError());
                                            } else {
                                                listener.onErrorPerformLineRechargeME(response != null ? "ERROR: " + response.message() : "Error realizando recarga");
                                            }
                                        }
                                    } else {
                                        listener.onErrorPerformLineRechargeME(response != null ? "ERROR: " + response.message() : "Error realizando recarga");
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseRealizarRecarga> call, Throwable t) {
                                    listener.onErrorPerformLineRechargeME("ERROR: " + t.getMessage());
                                }
                            });

                        } else {
                            listener.onErrorPerformLineRechargeME(responseAcessToken != null && responseAcessToken.message() != null && responseAcessToken.message() != "" ? "ERROR: " + responseAcessToken.message() : "Error obteniendo token");
                        }
                    } else {
                        listener.onErrorPerformLineRechargeME(responseAcessToken != null && responseAcessToken.message() != null && responseAcessToken.message() != "" ? "ERROR: " + responseAcessToken.message() : "Error obteniendo token");
                    }
                }

                @Override
                public void onFailure(Call<ResponseAccessTokenOAuthME> callAccessToken, Throwable t) {
                    listener.onErrorPerformLineRechargeME("ERROR: " + t.getMessage());
                }
            });
        } catch (Exception ex) {
            listener.onErrorPerformLineRechargeME("ERROR: " + ex.getMessage());
        }
    }

    @Override
    public void updateResultRechargeME(RequestActualizarRecarga body, RequestAccessToken accessToken, final FragmentTransactionSummaryContract.APIListener listener) {
        try {
            ApiMovilExito apiMovilExito = retrofitApiPresenteME.create(ApiMovilExito.class);
            String basicKeys = accessToken.getClientId() + ":" + accessToken.getClientSecret();
            Call<ResponseAccessTokenOAuthPresenteME> callAccessToken = apiMovilExito.getAccessTokenOAuthPresenteME(
                    "Basic " + Base64.encodeToString(basicKeys.getBytes(), Base64.NO_WRAP),
                    accessToken.getGrantType()
            );
            callAccessToken.enqueue(new Callback<ResponseAccessTokenOAuthPresenteME>() {
                @Override
                public void onResponse(Call<ResponseAccessTokenOAuthPresenteME> callAccessToken, Response<ResponseAccessTokenOAuthPresenteME> responseAcessToken) {
                    if (responseAcessToken.isSuccessful()) {
                        if (responseAcessToken.body().getAccess_token() != null && !responseAcessToken.body().getAccess_token().isEmpty()) {
                            Call<BaseResponse<ResponseActualizarRecarga>> call = apiMovilExito.updatePayment("Bearer " + responseAcessToken.body().getAccess_token(), body);
                            call.enqueue(new Callback<BaseResponse<ResponseActualizarRecarga>>() {
                                @Override
                                public void onResponse(Call<BaseResponse<ResponseActualizarRecarga>> call, Response<BaseResponse<ResponseActualizarRecarga>> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()) {
                                            listener.onExpiredToken(response);
                                        } else if (response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()) {
                                            listener.onErrorUpdateResultRechargeME(response);
                                        } else {
                                            listener.onSuccessUpdateResultRechargeME(response);
                                        }
                                    } else {
                                        listener.onErrorUpdateResultRechargeME(null);
                                    }
                                }

                                @Override
                                public void onFailure(Call<BaseResponse<ResponseActualizarRecarga>> call, Throwable t) {
                                    listener.onErrorUpdateResultRechargeME(null);
                                }
                            });
                        } else {
                            listener.onErrorUpdateResultRechargeME(null);
                        }
                    } else {
                        listener.onErrorUpdateResultRechargeME(null);
                    }
                }

                @Override
                public void onFailure(Call<ResponseAccessTokenOAuthPresenteME> callAccessToken, Throwable t) {
                    listener.onErrorUpdateResultRechargeME(null);
                }
            });
        } catch (Exception ex) {
            listener.onErrorUpdateResultRechargeME(null);
        }
    }

    @Override
    public void sendEmail(RequestEmail body, RequestAccessToken accessToken, FragmentTransactionSummaryContract.APIListener listener) {
        try {
            ApiMovilExito apiMovilExito = retrofitApiPresenteME.create(ApiMovilExito.class);
            String basicKeys = accessToken.getClientId() + ":" + accessToken.getClientSecret();
            Call<ResponseAccessTokenOAuthPresenteME> callAccessToken = apiMovilExito.getAccessTokenOAuthPresenteME(
                    "Basic " + Base64.encodeToString(basicKeys.getBytes(), Base64.NO_WRAP),
                    accessToken.getGrantType()
            );
            callAccessToken.enqueue(new Callback<ResponseAccessTokenOAuthPresenteME>() {
                @Override
                public void onResponse(Call<ResponseAccessTokenOAuthPresenteME> callAccessToken, Response<ResponseAccessTokenOAuthPresenteME> responseAcessToken) {
                    if (responseAcessToken.isSuccessful()) {
                        if (responseAcessToken.body().getAccess_token() != null && !responseAcessToken.body().getAccess_token().isEmpty()) {
                            Call<String> callSendEmail = apiMovilExito.emailFailedTransaction("Bearer " + responseAcessToken.body().getAccess_token(), body);
                            callSendEmail.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> responseSendEmail) {
                                    if (responseSendEmail.isSuccessful()) {
                                        if (responseSendEmail.body().equals("Enviado correctamente")) {
                                            listener.onSuccessSendEmail(responseSendEmail.body());
                                        } else {
                                            listener.onErrorSendEmail(responseSendEmail.body());
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    listener.onErrorSendEmail(t.getMessage());
                                }
                            });
                        } else {
                            listener.onErrorSendEmail(null);
                        }
                    } else {
                        listener.onErrorSendEmail(null);
                    }
                }

                @Override
                public void onFailure(Call<ResponseAccessTokenOAuthPresenteME> callAccessToken, Throwable t) {
                    listener.onErrorSendEmail(null);
                }
            });
        } catch (Exception ex) {
            listener.onErrorSendEmail(null);
        }
    }

    @Override
    @Provides
    @Singleton
    public OkHttpClient provideUnsafeOkHttpClient(InputStream certificate, String pass) {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
                                throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
                                throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            InputStream stream = certificate;
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(stream, pass.toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
            keyManagerFactory.init(keyStore, pass.toCharArray());

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, new java.security.SecureRandom());//prueba null km

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(logging);
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return builder
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}


