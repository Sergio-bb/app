package solidappservice.cm.com.presenteapp.front.splash.ActivitySplash;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.mensajesbanner.ResponseMensajesBanner;
import solidappservice.cm.com.presenteapp.entities.imagelogin.ResponseImageLogin;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.entities.tyc.response.ReponseTyC;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 19/09/2021.
 */
public class ActivitySplashPresenter implements ActivitySplashContract.Presenter,
        ActivitySplashContract.APIListener{

    ActivitySplashView view;
    ActivitySplashModel model;

    public ActivitySplashPresenter(@NonNull ActivitySplashView view, @NonNull ActivitySplashModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchBannerMessages() {
        view.showCircularProgressBar();
        model.getBannerMessages(this);
    }
    @Override
    public <T> void onSuccessBannerMessages(Response<BaseResponse<T>> response) {
        try {
            List<ResponseMensajesBanner> bannerMessages = null;
            if (response != null){
                bannerMessages = (List<ResponseMensajesBanner>) response.body().getResultado();
            }
            view.loadBannerMessages(bannerMessages);
            view.fetchLoginImage();
        }catch (Exception ex){
            Log.d("Error",ex.getMessage());
            view.fetchLoginImage();
        }
    }

    @Override
    public void fetchLoginImage() {
        view.showCircularProgressBar();
        model.getLoginImage(this);
    }
    @Override
    public <T> void onSuccessLoginImage(Response<BaseResponse<T>> response) {
        try{
            ResponseImageLogin imageLogin = null;
            if(response != null){
                imageLogin = (ResponseImageLogin) response.body().getResultado();
            }
            if(imageLogin != null){
                view.loadLoginImage(imageLogin.getImageUrl());
            }
            view.fetchAppVersion();
        }catch (Exception ex){
            Log.d("Error",ex.getMessage());
            view.fetchAppVersion();
        }
    }

    @Override
    public void fetchAppVersion() {
        view.showCircularProgressBar();
        model.getAppVersion(this);
    }
    @Override
    public <T> void onSuccessAppVersion(Response<BaseResponse<T>> response) {
        try{
            String appVersion = null;
            if(response != null){
                appVersion = (String) response.body().getResultado();
            }
            view.loadAppVersion(appVersion);
            view.fetchTermsAndConditions();
        }catch (Exception ex){
            Log.d("Error",ex.getMessage());
            view.fetchTermsAndConditions();
        }
    }

    @Override
    public void fetchTermsAndConditions() {
        view.showCircularProgressBar();
        model.getTermsAndConditions(this);
    }
    @Override
    public <T> void onSuccessTermsAndConditions(Response<BaseResponse<T>> response) {
        try{
            List<ReponseTyC> termsAndConditions = null;
            ReponseTyC termsAndConditionsActive = new ReponseTyC();
            if(response != null){
                termsAndConditions = (List<ReponseTyC>) response.body().getResultado();
            }

            if(termsAndConditions != null) {
                for (ReponseTyC ta : termsAndConditions){
                    if(ta.getI_estado().equals("A")){
                        termsAndConditionsActive = ta;
                    }
                }
            }
            view.loadTermsAndConditions(termsAndConditionsActive);
            view.fetchResponseMessages();
        }catch (Exception ex){
            Log.d("Error",ex.getMessage());
            view.fetchResponseMessages();
        }
    }

    @Override
    public void fetchResponseMessages() {
        view.showCircularProgressBar();
        model.getResponseMessages(this);
    }
    @Override
    public <T> void onSuccessResponseMessages(Response<BaseResponse<T>> response) {
        try{
            List<ResponseMensajesRespuesta> responseMessages = null;
            view.loadResponseMessages(responseMessages);
            view.startApp();
        }catch (Exception ex){
            Log.d("Error",ex.getMessage());
            view.startApp();
        }
    }

    @Override
    public <T> void onError(Response<BaseResponse<T>> response) {
    }

    @Override
    public void onFailure(Throwable t) {
    }

}
