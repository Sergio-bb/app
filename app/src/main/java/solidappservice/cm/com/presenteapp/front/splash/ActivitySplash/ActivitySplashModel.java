package solidappservice.cm.com.presenteapp.front.splash.ActivitySplash;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.mensajesbanner.ResponseMensajesBanner;
import solidappservice.cm.com.presenteapp.entities.imagelogin.ResponseImageLogin;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiGeneral;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.entities.tyc.response.ReponseTyC;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.front.menuprincipal.FragmentHome.FragmentHomeContract;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apinequi.ApiNequi;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 19/09/2021.
 */
public class ActivitySplashModel implements ActivitySplashContract.Model{

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(NetworkHelper.DIRECCION_WS)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private ApiPresente service = retrofit.create(ApiPresente.class);

    private Retrofit retrofitNequi = new Retrofit.Builder()
            .baseUrl(NetworkHelper.NEQUI_WS)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private ApiNequi serviceNequi = retrofitNequi.create(ApiNequi.class);

    @Override
    public void getResponseMessages(final ActivitySplashContract.APIListener listener) {
        try {
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
                    listener.onErrorResponseMessages(null);
                }
            });
        } catch (Exception e) {
            listener.onErrorResponseMessages(null);
        }
    }

    @Override
    public void getBannerMessages(final ActivitySplashContract.APIListener listener){
        try {
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
                    listener.onErrorBannerMessages(null);
                }
            });
        } catch (Exception e) {
            listener.onErrorBannerMessages(null);
        }
    }

    @Override
    public void getLoginImage(final ActivitySplashContract.APIListener listener) {
        try {
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
                    listener.onErrorLoginImage(null);
                }
            });
        } catch (Exception e) {
            listener.onErrorLoginImage(null);
        }
    }

    @Override
    public void getAppVersion(final ActivitySplashContract.APIListener listener) {
        try {
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
                    listener.onErrorAppVersion(null);
                }
            });
        } catch (Exception e) {
            listener.onErrorAppVersion(null);
        }
    }

    @Override
    public void getTermsAndConditions(final ActivitySplashContract.APIListener listener) {
        try {
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
                    listener.onErrorTermsAndConditions(null);
                }
            });
        } catch (Exception e) {
            listener.onErrorTermsAndConditions(null);
        }
    }

    @Override
    public void getButtonStatePaymentQR(final ActivitySplashContract.APIListener listener) {
        try {
            Call<ResponseNequiGeneral> call = serviceNequi.getStatusSectionQR();
            call.enqueue(new Callback<ResponseNequiGeneral>() {

                @Override
                public void onResponse(Call<ResponseNequiGeneral> call, Response<ResponseNequiGeneral> response) {
                    if (response.isSuccessful()) {
                        if(response.body() != null){
                            listener.onSuccessButtonStatePaymentQR(response);
                        }else{
                            listener.onErrorButtonStatePaymentQR(response);
                        }
                    } else {
                        listener.onErrorButtonStatePaymentQR(response);
                    }
                }
                @Override
                public void onFailure(Call<ResponseNequiGeneral> call, Throwable t) {
                    listener.onErrorButtonStatePaymentQR(null);
                }
            });
        } catch (Exception e) {
            listener.onErrorButtonStatePaymentQR(null);
        }
    }

    @Override
    public void getButtonStatePaymentDispersiones(final ActivitySplashContract.APIListener listener) {
        try {
            Call<ResponseNequiGeneral> call = serviceNequi.getStatusSectionPaymentDispersiones();
            call.enqueue(new Callback<ResponseNequiGeneral>() {

                @Override
                public void onResponse(Call<ResponseNequiGeneral> call, Response<ResponseNequiGeneral> response) {
                    if (response.isSuccessful()) {
                        if(response.body() != null){
                            listener.onSuccessButtonStatePaymentDispersiones(response);
                        }else{
                            listener.onErrorButtonStatePaymentDispersiones(response);
                        }
                    } else {
                        listener.onErrorButtonStatePaymentDispersiones(response);
                    }
                }
                @Override
                public void onFailure(Call<ResponseNequiGeneral> call, Throwable t) {
                    listener.onErrorButtonStatePaymentDispersiones(null);
                }
            });
        } catch (Exception e) {
            listener.onErrorButtonStatePaymentDispersiones(null);
        }
    }

    @Override
    public void getStateButtonPaymentSuscriptions(final ActivitySplashContract.APIListener listener) {
        try {
            Call<ResponseNequiGeneral> call = serviceNequi.getStatusSectionPaymentSuscriptions();
            call.enqueue(new Callback<ResponseNequiGeneral>() {

                @Override
                public void onResponse(Call<ResponseNequiGeneral> call, Response<ResponseNequiGeneral> response) {
                    if (response.isSuccessful()) {
                        if(response.body() != null){
                            listener.onSuccessButtonStatePaymentSuscriptions(response);
                        }else{
                            listener.onErrorButtonStatePaymentSuscriptions(response);
                        }
                    } else {
                        listener.onErrorButtonStatePaymentSuscriptions(response);
                    }
                }
                @Override
                public void onFailure(Call<ResponseNequiGeneral> call, Throwable t) {
                    listener.onErrorButtonStatePaymentSuscriptions(null);
                }
            });
        } catch (Exception e) {
            listener.onErrorButtonStatePaymentSuscriptions(null);
        }
    }

    @Override
    public void getStateNequiBalance(final ActivitySplashContract.APIListener listener) {
        try {
            Call<ResponseNequiGeneral> call = serviceNequi.getStatusSectionNequiBalance();
            call.enqueue(new Callback<ResponseNequiGeneral>() {

                @Override
                public void onResponse(Call<ResponseNequiGeneral> call, Response<ResponseNequiGeneral> response) {
                    if (response.isSuccessful()) {
                        if(response.body() != null){
                            listener.onSuccessStateNequiBalance(response);
                        }else{
                            listener.onErrorStateNequiBalance(response);
                        }
                    } else {
                        listener.onErrorStateNequiBalance(response);
                    }
                }
                @Override
                public void onFailure(Call<ResponseNequiGeneral> call, Throwable t) {
                    listener.onErrorStateNequiBalance(null);
                }
            });
        } catch (Exception e) {
            listener.onErrorStateNequiBalance(null);
        }
    }

    @Override
    public void getStateSuscriptions(final ActivitySplashContract.APIListener listener) {
        try {
            Call<ResponseNequiGeneral> call = serviceNequi.getStatusSectionSuscription();
            call.enqueue(new Callback<ResponseNequiGeneral>() {

                @Override
                public void onResponse(Call<ResponseNequiGeneral> call, Response<ResponseNequiGeneral> response) {
                    if (response.isSuccessful()) {
                        if(response.body() != null){
                            listener.onSuccessStateSuscriptions(response);
                        }else{
                            listener.onErrorStateSuscriptions(response);
                        }
                    } else {
                        listener.onErrorStateSuscriptions(response);
                    }
                }
                @Override
                public void onFailure(Call<ResponseNequiGeneral> call, Throwable t) {
                    listener.onErrorStateSuscriptions(null);
                }
            });
        } catch (Exception e) {
            listener.onErrorStateSuscriptions(null);
        }
    }
}
