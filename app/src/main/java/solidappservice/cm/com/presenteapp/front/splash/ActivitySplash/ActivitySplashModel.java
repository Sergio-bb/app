package solidappservice.cm.com.presenteapp.front.splash.ActivitySplash;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.mensajesbanner.ResponseMensajesBanner;
import solidappservice.cm.com.presenteapp.entities.imagelogin.ResponseImageLogin;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.entities.tyc.response.ReponseTyC;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 19/09/2021.
 */
public class ActivitySplashModel implements ActivitySplashContract.Model{

    @Override
    public void getBannerMessages(final ActivitySplashContract.APIListener listener){
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<List<ResponseMensajesBanner>>> call = service.getBannerMessages();
            call.enqueue(new Callback<BaseResponse<List<ResponseMensajesBanner>>>() {

                @Override
                public void onResponse(Call<BaseResponse<List<ResponseMensajesBanner>>> call, Response<BaseResponse<List<ResponseMensajesBanner>>> response) {
                    if (response.isSuccessful()) {
                        listener.onSuccessBannerMessages(response);
                    } else {
                        listener.onSuccessBannerMessages(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<List<ResponseMensajesBanner>>> call, Throwable t){
                    listener.onSuccessBannerMessages(null);
                }
            });
        } catch (Exception e) {
            listener.onSuccessBannerMessages(null);
        }
    }

    @Override
    public void getLoginImage(final ActivitySplashContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseImageLogin>> call = service.getLoginImage();
            call.enqueue(new Callback<BaseResponse<ResponseImageLogin>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseImageLogin>> call, Response<BaseResponse<ResponseImageLogin>> response) {
                    if (response.isSuccessful()) {
                        listener.onSuccessLoginImage(response);
                    } else {
                        listener.onSuccessLoginImage(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseImageLogin>> call, Throwable t) {
                    listener.onSuccessLoginImage(null);
                }
            });
        } catch (Exception e) {
            listener.onSuccessLoginImage(null);
        }
    }

    @Override
    public void getAppVersion(final ActivitySplashContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<String>> call = service.getAppVersion();
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    if (response.isSuccessful()) {
                        listener.onSuccessAppVersion(response);
                    } else {
                        listener.onSuccessAppVersion(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    listener.onSuccessAppVersion(null);
                }
            });
        } catch (Exception e) {
            listener.onSuccessAppVersion(null);
        }
    }

    @Override
    public void getTermsAndConditions(final ActivitySplashContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<List<ReponseTyC>>> call = service.getTermsAndConditions();
            call.enqueue(new Callback<BaseResponse<List<ReponseTyC>>>() {

                @Override
                public void onResponse(Call<BaseResponse<List<ReponseTyC>>> call, Response<BaseResponse<List<ReponseTyC>>> response) {
                    if (response.isSuccessful()) {
                        listener.onSuccessTermsAndConditions(response);
                    } else {
                        listener.onSuccessTermsAndConditions(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<List<ReponseTyC>>> call, Throwable t) {
                    listener.onSuccessTermsAndConditions(null);
                }
            });
        } catch (Exception e) {
            listener.onSuccessTermsAndConditions(null);
        }
    }

    @Override
    public void getResponseMessages(final ActivitySplashContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.URL_APIPRESENTEAPP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<List<ResponseMensajesRespuesta>>> call = service.getResponseMessages();
            call.enqueue(new Callback<BaseResponse<List<ResponseMensajesRespuesta>>>() {

                @Override
                public void onResponse(Call<BaseResponse<List<ResponseMensajesRespuesta>>> call, Response<BaseResponse<List<ResponseMensajesRespuesta>>> response) {
                    if (response.isSuccessful()) {
                        listener.onSuccessResponseMessages(response);
                    } else {
                        listener.onSuccessResponseMessages(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<List<ResponseMensajesRespuesta>>> call, Throwable t) {
                    listener.onSuccessResponseMessages(null);
                }
            });
        } catch (Exception e) {
            listener.onSuccessResponseMessages(null);
        }
    }


}
