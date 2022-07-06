package solidappservice.cm.com.presenteapp.front.splash.ActivitySplash;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.mensajesbanner.ResponseMensajesBanner;
import solidappservice.cm.com.presenteapp.entities.imagelogin.ResponseImageLogin;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiGeneral;
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
    public void fetchResponseMessages() {
        view.showCircularProgressBar();
        model.getResponseMessages(this);
    }
    @Override
    public <T> void onSuccessResponseMessages(Response<BaseResponse<T>> response) {
        try{
            List<ResponseMensajesRespuesta> responseMessages = null;
            view.loadResponseMessages(responseMessages);
            view.fetchBannerMessages();
        }catch (Exception ex){
            view.fetchBannerMessages();
        }
    }
    @Override
    public <T> void onErrorResponseMessages(Response<BaseResponse<T>> response) {
        view.fetchBannerMessages();
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
            view.fetchLoginImage();
        }
    }
    @Override
    public <T> void onErrorBannerMessages(Response<BaseResponse<T>> response) {
        view.fetchLoginImage();
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
    public <T> void onErrorLoginImage(Response<BaseResponse<T>> response) {
        view.fetchAppVersion();
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
    public <T> void onErrorAppVersion(Response<BaseResponse<T>> response) {
        view.fetchTermsAndConditions();
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
            view.fetchButtonStatePaymentQR();
//            view.startApp();
        }catch (Exception ex){
            Log.d("Error",ex.getMessage());
            view.fetchButtonStatePaymentQR();
        }
    }
    @Override
    public <T> void onErrorTermsAndConditions(Response<BaseResponse<T>> response) {
        view.fetchButtonStatePaymentQR();
    }


    @Override
    public void fetchButtonStatePaymentQR(){
        model.getButtonStatePaymentQR(this);
    }
    @Override
    public <T> void onSuccessButtonStatePaymentQR(Response<ResponseNequiGeneral> response){
        try{
            ResponseNequiGeneral stateQR = (ResponseNequiGeneral)response.body();
            if(stateQR != null && stateQR.getN_Valor1().equals("Y")){
                view.isActiveButtonPaymentQR(true);
            }else{
                view.isActiveButtonPaymentQR(false);
            }
            view.fetchButtonStatePaymentDispersiones();
        }catch (Exception ex){
            Log.d("Error",ex.getMessage());
            view.isActiveButtonPaymentQR(false);
            view.fetchButtonStatePaymentDispersiones();
        }
    }
    @Override
    public <T> void onErrorButtonStatePaymentQR(Response<ResponseNequiGeneral> response){
        view.isActiveButtonPaymentQR(false);
        view.fetchButtonStatePaymentDispersiones();
    }

    @Override
    public void fetchButtonStatePaymentDispersiones(){
        model.getButtonStatePaymentDispersiones(this);
    }
    @Override
    public <T> void onSuccessButtonStatePaymentDispersiones(Response<ResponseNequiGeneral> response){
        try{
            ResponseNequiGeneral statePaymentDispersiones = (ResponseNequiGeneral)response.body();
            if(statePaymentDispersiones != null && statePaymentDispersiones.getN_Valor1().equals("Y")){
                view.isActiveButtonPaymentDispersiones(true);
            }else{
                view.isActiveButtonPaymentDispersiones(false);
            }
            view.fetchStateButtonPaymentSuscriptions();
        }catch (Exception ex){
            view.isActiveButtonPaymentDispersiones(false);
            view.fetchStateButtonPaymentSuscriptions();
        }
    }
    @Override
    public <T> void onErrorButtonStatePaymentDispersiones(Response<ResponseNequiGeneral> response){
        view.isActiveButtonPaymentDispersiones(false);
        view.fetchStateButtonPaymentSuscriptions();
    }


    @Override
    public void fetchStateButtonPaymentSuscriptions(){
        model.getStateButtonPaymentSuscriptions(this);
    }
    @Override
    public <T> void onSuccessButtonStatePaymentSuscriptions(Response<ResponseNequiGeneral> response){
        try{
            ResponseNequiGeneral statePaymentSuscriptions = (ResponseNequiGeneral)response.body();
            if(statePaymentSuscriptions != null && statePaymentSuscriptions.getN_Valor1().equals("Y")){
                view.isActiveButtonPaymentSuscriptions(true);
            }else{
                view.isActiveButtonPaymentSuscriptions(false);
            }
            view.fetchStateNequiBalance();
        }catch (Exception ex){
            view.isActiveButtonPaymentSuscriptions(false);
            view.fetchStateNequiBalance();
        }
    }
    @Override
    public <T> void onErrorButtonStatePaymentSuscriptions(Response<ResponseNequiGeneral> response){
        view.isActiveButtonPaymentSuscriptions(false);
        view.fetchStateNequiBalance();
    }


    @Override
    public void fetchStateNequiBalance(){
        model.getStateNequiBalance(this);
    }
    @Override
    public <T> void onSuccessStateNequiBalance(Response<ResponseNequiGeneral> response){
        try{
            ResponseNequiGeneral stateNequiBalance = (ResponseNequiGeneral)response.body();
            if(stateNequiBalance != null && stateNequiBalance.getN_Valor1().equals("Y")){
                view.isActiveStateNequiBalance(true);
            }else{
                view.isActiveStateNequiBalance(false);
            }
            view.fetchStateSuscriptions();
        }catch (Exception ex){
            view.isActiveStateNequiBalance(false);
            view.fetchStateSuscriptions();
        }
    }
    @Override
    public <T> void onErrorStateNequiBalance(Response<ResponseNequiGeneral> response){
        view.isActiveStateNequiBalance(false);
        view.fetchStateSuscriptions();
    }


    @Override
    public void fetchStateSuscriptions(){
        model.getStateSuscriptions(this);
    }
    @Override
    public <T> void onSuccessStateSuscriptions(Response<ResponseNequiGeneral> response){
        try{
            ResponseNequiGeneral stateNequiBalance = (ResponseNequiGeneral)response.body();
            if(stateNequiBalance != null && stateNequiBalance.getN_Valor1().equals("Y")){
                view.isActiveStateSuscriptions(true);
            }else{
                view.isActiveStateSuscriptions(false);
            }
            view.startApp();
        }catch (Exception ex){
            view.isActiveStateSuscriptions(false);
            view.startApp();
        }
    }
    @Override
    public <T> void onErrorStateSuscriptions(Response<ResponseNequiGeneral> response){
        view.isActiveStateSuscriptions(false);
        view.startApp();
    }


}
