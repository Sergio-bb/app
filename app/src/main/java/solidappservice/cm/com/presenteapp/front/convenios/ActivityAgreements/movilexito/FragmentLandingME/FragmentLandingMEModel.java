package solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.movilexito.FragmentLandingME;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseConsultarDatosAsociado;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestEmail;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseParametrosAPP;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apiconvenios.ApiConvenios;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

public class FragmentLandingMEModel implements FragmentLandingMEContract.Model {

    private ApiPresente apiPresente;
    private ApiConvenios apiConvenios;
    private FragmentLandingMEPresenter presenter;
    public static final String BASE_URL_ME = "https://apps.presente.com.co/PresenteME2/";

    @Override
    public void getUrlLandingMovilExito(FragmentLandingMEContract.APIListener listener) {
        try{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiPresente = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseParametrosAPP>> call = apiPresente.getUrlLandingMovilExito();
            call.enqueue(new Callback<BaseResponse<ResponseParametrosAPP>>() {
                @Override
                public void onResponse(Call<BaseResponse<ResponseParametrosAPP>> call, Response<BaseResponse<ResponseParametrosAPP>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getDescripcionError() != null && !response.body().getDescripcionError().isEmpty()) {
                            listener.onErrorGetUrlLandingMovilExito(response);
                        } else {
                            listener.onSuccessGetUrlLandingMovilExito(response);
                        }
                    } else {
                        listener.onErrorGetUrlLandingMovilExito(null);
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<ResponseParametrosAPP>> call, Throwable t) {
                    listener.onErrorGetUrlLandingMovilExito(null);
                }
            });
        }catch (Exception ex){
            listener.onErrorGetUrlLandingMovilExito(null);
        }
    }

    @Override
    public void getAssociatedData(FragmentLandingMEContract.APIListener listener, BaseRequest baseRequest) {

        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiPresente = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseConsultarDatosAsociado>> call = apiPresente.getPersonalData(baseRequest);
            call.enqueue(new Callback<BaseResponse<ResponseConsultarDatosAsociado>>() {
                @Override
                public void onResponse(Call<BaseResponse<ResponseConsultarDatosAsociado>> call, Response<BaseResponse<ResponseConsultarDatosAsociado>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()) {
                            listener.onExpiredToken(response);
                        } else if (response.body().getDescripcionError() != null && !response.body().getDescripcionError().isEmpty()) {
                            listener.onErrorGetAssociatedData(response);
                        } else {
                            listener.onSuccessGetAssociatedData(response);
                        }
                    } else {
                        listener.onErrorGetAssociatedData(response);
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<ResponseConsultarDatosAsociado>> call, Throwable t) {
                    listener.onErrorGetAssociatedData(null);
                }
            });
        }catch(Exception ex){
            listener.onErrorGetAssociatedData(null);
        }
    }

//    @Override
//    public void postEmail(FragmentLandingMEContract.APIListener listener, RequestEmail email) {
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().
//                setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .addInterceptor(interceptor)
//                .readTimeout(60, TimeUnit.SECONDS)
//                .connectTimeout(60, TimeUnit.SECONDS)
//                .writeTimeout(60, TimeUnit.SECONDS)
//                .build();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL_ME)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
//                .build();
//
//        apiConvenios = retrofit.create(ApiConvenios.class);
//        Call<String> call = apiConvenios.correoFalloTransaccion(email);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (response.isSuccessful()) {
//                    if (response.body().equals("Enviado correctamente")) {
//                        Log.i("Email", "el correo de fallo en la transaccion fue enviado correctamente");
//                    } else {
//                        Log.i("errorEmail", "no se envio correctamente");
//                    }
//                } else {
//                    Log.i("errorEmail", "no fue satisfactorio");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Log.i("errorEmail", "no se pudo consumir el servicio");
//            }
//        });
//    }

    //    public void recolectarDatosAsociadoWV(FragmentWebViewView view, BaseRequest baseRequest) {
//        presenter = new FragmentWebViewPresenter(view, this);
//
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
//                .setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .addInterceptor(loggingInterceptor)
//                .readTimeout(60, TimeUnit.SECONDS)
//                .connectTimeout(60, TimeUnit.SECONDS)
//                .writeTimeout(60, TimeUnit.SECONDS)
//                .build();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
//                .build();
//
//        serviceApiPresente = retrofit.create(ApiPresente.class);
//        Call<BaseResponse<ResponseConsultarDatosAsociado>> call = serviceApiPresente.getPersonalData(baseRequest);
//        call.enqueue(new Callback<BaseResponse<ResponseConsultarDatosAsociado>>() {
//            @Override
//            public void onResponse(Call<BaseResponse<ResponseConsultarDatosAsociado>> call, Response<BaseResponse<ResponseConsultarDatosAsociado>> response) {
//                if (!response.isSuccessful()) {
//                    presenter.mostrarMensaje();
//                    Log.i("fallo", "1");
//                    return;
//                }
//                datosAsociado = (ResponseConsultarDatosAsociado) response.body().getResultado();
//                if (datosAsociado != null) {
//                    presenter.datosAsociado(datosAsociado);
//                } else {
//                    Log.i("fallo", "2");
//                    presenter.mostrarMensaje();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BaseResponse<ResponseConsultarDatosAsociado>> call, Throwable t) {
//                Log.i("fallo", t.getMessage());
//                presenter.mostrarMensaje();
//            }
//        });
//    }
//
//    public void consultarUrlWebView(FragmentWebViewView view){
//        presenter = new FragmentWebViewPresenter(view, this);
//
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
//                .setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .addInterceptor(interceptor)
//                .readTimeout(60, TimeUnit.SECONDS)
//                .connectTimeout(60, TimeUnit.SECONDS)
//                .writeTimeout(60, TimeUnit.SECONDS)
//                .build();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
//                .build();
//
//        serviceApiConvenios = retrofit.create(ApiConvenios.class);
//        Call<BaseResponseUrl<RequestBodyUrl>> call = serviceApiConvenios.ObtenerUrlWEbView("20");
//        call.enqueue(new Callback<BaseResponseUrl<RequestBodyUrl>>() {
//            @Override
//            public void onResponse(Call<BaseResponseUrl<RequestBodyUrl>> call, Response<BaseResponseUrl<RequestBodyUrl>> response) {
//                if (!response.isSuccessful()){
//                    presenter.mostrarMensaje();
//                }
//                requestBodyUrl = response.body().getResultado();
//                if (requestBodyUrl != null) {
//                    presenter.setRequestBodyUrl(requestBodyUrl);
//                } else {
//                    Log.i("fallo", "2");
//                    presenter.mostrarMensaje();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BaseResponseUrl<RequestBodyUrl>> call, Throwable t) {
//                presenter.mostrarMensaje();
//            }
//        });
//    }
}
