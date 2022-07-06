package solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentVerifyCode;

import android.util.Base64;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.*;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.*;
import solidappservice.cm.com.presenteapp.entities.dispositivo.apirequest.*;
import solidappservice.cm.com.presenteapp.entities.dispositivo.apiresponse.*;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 11/07/2021.
 */
public class FragmentVerifyCodeModel implements FragmentVerifyCodeContract.Model{

    @Override
    public void sendVerificationCodeEmail(RequestEnviarCodigo body, FragmentVerifyCodeContract.APIListener listener) {
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
            Call<BaseResponse<ResponseEnviarCodigo>> call = service.sendVerificationCodeEmail(body);
            call.enqueue(new Callback<BaseResponse<ResponseEnviarCodigo>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseEnviarCodigo>> call, Response<BaseResponse<ResponseEnviarCodigo>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessSendVerificationCodeEmail(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseEnviarCodigo>> call, Throwable t) {
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

    @Override
    public void sendVerificationCodePhone(RequestEnviarCodigo body, FragmentVerifyCodeContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseEnviarCodigo>> call = service.sendVerificationCodePhone(body);
            call.enqueue(new Callback<BaseResponse<ResponseEnviarCodigo>>() {
                @Override
                public void onResponse(Call<BaseResponse<ResponseEnviarCodigo>> call, Response<BaseResponse<ResponseEnviarCodigo>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessSendVerificationCodePhone(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseEnviarCodigo>> call, Throwable t) {
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

    @Override
    public void validateVerificationCode(RequestValidarCodigo body, FragmentVerifyCodeContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseValidarCodigo>> call = service.validateVerificationCode(body);
            call.enqueue(new Callback<BaseResponse<ResponseValidarCodigo>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseValidarCodigo>> call, Response<BaseResponse<ResponseValidarCodigo>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessValidateVerificationCode(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseValidarCodigo>> call, Throwable t) {
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

    @Override
    public void updatePersonalData(RequestActualizarDatos body, final FragmentVerifyCodeContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<String>> call = service.updatePersonalData(body);
            call.enqueue(new Callback<BaseResponse<String>>() {

                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessUpdatePersonalData(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
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


    @Override
    public void registerDevice(Dispositivo body, final FragmentVerifyCodeContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseRegistrarDispositivo>> call = service.registerDevice(body);
            call.enqueue(new Callback<BaseResponse<ResponseRegistrarDispositivo>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseRegistrarDispositivo>> call, Response<BaseResponse<ResponseRegistrarDispositivo>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessRegisterDevice(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseRegistrarDispositivo>> call, Throwable t) {
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


    @Override
    public void sendSms(RequestEnviarCodigoPhone body, final FragmentVerifyCodeContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://rest.inalambria.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getUnsafeOkHttpClient().build())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseEnviarCodigoPhone>> call = service.sendSms(body, "Besic "+ Arrays.toString(Base64.encode("trPRUEBASjcl15:V287fm".getBytes(), Base64.DEFAULT)));
            call.enqueue(new Callback<BaseResponse<ResponseEnviarCodigoPhone>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseEnviarCodigoPhone>> call, Response<BaseResponse<ResponseEnviarCodigoPhone>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onError(response);
                        }else{
                            listener.onSuccessSendSms(response);
                        }
                    } else {
                        listener.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseEnviarCodigoPhone>> call, Throwable t) {
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


    public static OkHttpClient.Builder getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
