package solidappservice.cm.com.presenteapp.front.login.FragmentLogin;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.dispositivo.apirequest.Dispositivo;
import solidappservice.cm.com.presenteapp.entities.dispositivo.apiresponse.ResponseValidarDispositivo;
import solidappservice.cm.com.presenteapp.entities.imagelogin.ResponseImageLogin;
import solidappservice.cm.com.presenteapp.entities.login.Request.RequestLogin;
import solidappservice.cm.com.presenteapp.entities.login.Response.Usuario;
import solidappservice.cm.com.presenteapp.entities.mensajesbanner.ResponseMensajesBanner;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 20/09/2021.
 */
public class FragmentLoginPresenter implements FragmentLoginContract.Presenter,
        FragmentLoginContract.APIListener{

    FragmentLoginView view;
    FragmentLoginModel model;

    public FragmentLoginPresenter(@NonNull FragmentLoginView view, @NonNull FragmentLoginModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void validateLogin(RequestLogin request) {
        view.showProgressDialog("Validando usuario...");
        model.validateLogin(request,this);
    }
    @Override
    public <T> void onSuccessValidateLogin(RequestLogin login, Response<BaseResponse<T>> response) {
        try{
            if (response != null) {
                Usuario usuario = (Usuario) response.body().getResultado();
                usuario.setCedula(login.getCedula());
                usuario.setClave(login.getClave());
                view.resultValidateLogin(usuario);
            }else{
                view.hideProgressDialog();
                view.showDataFetchError("Lo sentimos", "");
            }
        }catch (Exception ex){
            view.hideProgressDialog();
            view.showDataFetchError("Lo sentimos", "");
        }
    }
    @Override
    public <T> void onErrorValidateLogin(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        if(response != null && response.body() != null
                && response.body().getDescripcionError() != null && response.body().getDescripcionError() != ""){
            view.showDataFetchError("Lo sentimos", response.body().getDescripcionError());
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void fetchBannerMessages() {
        model.getBannerMessages(this);
    }
    @Override
    public <T> void onSuccessBannerMessages(Response<BaseResponse<T>> response) {
        try{
            List<ResponseMensajesBanner> bannerMessages = null;
            if (response != null){
                bannerMessages = (List<ResponseMensajesBanner>) response.body().getResultado();
            }
            view.loadBannerMessages(bannerMessages);
        }catch (Exception ex){
        }
    }



    @Override
    public void fetchLoginImage() {
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
        }catch (Exception ex){
        }
    }
    @Override
    public void validateRegisterDevice(Dispositivo request) {
        model.validateRegisterDevice(request,this);
    }
    @Override
    public <T> void onSuccessValidateRegisterDevice(Response<BaseResponse<T>> response) {
        try{
            if(response != null){
                ResponseValidarDispositivo validateDevice = (ResponseValidarDispositivo) response.body().getResultado();
                //validateDevice.setDispositivoRegistrado("Y");
                view.resultValidateRegisterDevice(validateDevice);
            } else{
                view.hideProgressDialog();
                view.showDataFetchError("Lo sentimos", "");
            }
        }catch (Exception ex){
            view.hideProgressDialog();
            view.showDataFetchError("Lo sentimos", "");
        }
    }


    @Override
    public <T> void onError(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        if(response != null){
            view.showDataFetchError("Lo sentimos", response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideProgressDialog();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }

}
