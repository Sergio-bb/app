package solidappservice.cm.com.presenteapp.front.menuprincipal.FragmentHome;

import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.banercomercial.response.ResponseBannerComercial;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.mensajes.response.ResponseObtenerMensajes;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultaSuscripcion;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiGeneral;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseSuscriptionStatus;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseParametrosAPP;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 17/09/2021.
 */
public class FragmentHomePresenter implements FragmentHomeContract.Presenter,
        FragmentHomeContract.APIListener{

    FragmentHomeView view;
    FragmentHomeModel model;

    public FragmentHomePresenter(@NonNull FragmentHomeView view, @NonNull FragmentHomeModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchButtonStateAgreements() {
        model.getButtonStateAgreements(this);
    }
    @Override
    public <T> void onSuccessButtonStateAgreements(Response<BaseResponse<T>> response) {
        try{
            ResponseParametrosAPP stateConvenios= (ResponseParametrosAPP) response.body().getResultado();
            if(stateConvenios != null && stateConvenios.getEstado() != null && stateConvenios.getEstado().equals("Y")){
                view.showButtonAgreements();
            }else{
                view.hideButtonAgreements();
            }
            view.fetchButtonStateResorts();
        }catch (Exception ex){
            view.hideButtonAgreements();
            view.fetchButtonStateResorts();
        }
    }
    @Override
    public <T> void onErrorButtonStateAgreements(Response<BaseResponse<T>> response) {
        view.hideButtonAgreements();
        view.fetchButtonStateResorts();
    }

    @Override
    public void fetchButtonStateTransfers() {
        view.hideContentOptions();
        view.showCircularProgressBarOptions();
        model.getButtonStateTransfers(this);
    }
    @Override
    public <T> void onSuccessButtonStateTransfers(Response<BaseResponse<T>> response) {
        try{
            ResponseParametrosAPP stateTransferencias = (ResponseParametrosAPP) response.body().getResultado();
            if(stateTransferencias != null && stateTransferencias.getEstado() != null && stateTransferencias.getEstado().equals("Y")){
                view.showButtonTransfers();
            }else{
                view.hideButtonTransfers();
            }
            view.fetchButtonStateSavings();
        }catch (Exception ex){
            view.hideButtonTransfers();
            view.fetchButtonStateSavings();
        }
    }

    @Override
    public <T> void onErrorButtonStateTransfers(Response<BaseResponse<T>> response) {
        view.hideButtonTransfers();
        view.fetchButtonStateSavings();
    }


    @Override
    public void fetchButtonStateSavings() {
        view.hideContentOptions();
        view.showCircularProgressBarOptions();
        model.getButtonStateSavings(this);
    }
    @Override
    public <T> void onSuccessButtonStateSavings(Response<BaseResponse<T>> response) {
        try{
            ResponseParametrosAPP stateAperturaAhorros = (ResponseParametrosAPP) response.body().getResultado();
            if(stateAperturaAhorros != null && stateAperturaAhorros.getEstado() != null && stateAperturaAhorros.getEstado().equals("Y")){
                view.showButtonSavings();
            }else{
                view.hideButtonSavings();
            }
            view.fetchMessageInbox();
        }catch (Exception ex){
            view.hideButtonSavings();
            view.fetchMessageInbox();
        }
    }
    @Override
    public <T> void onErrorButtonStateSavings(Response<BaseResponse<T>> response) {
        view.hideButtonSavings();
        view.fetchMessageInbox();
    }


    @Override
    public void fetchMessageInbox(BaseRequest baseRequest) {
        view.hideContentOptions();
        view.showCircularProgressBarOptions();
        model.getMessageInbox(baseRequest, this);
    }
    @Override
    public <T> void onSuccessMessageInbox(Response<BaseResponse<T>> response) {
        try{
            List<ResponseObtenerMensajes> messages = (List<ResponseObtenerMensajes>) response.body().getResultado();
            if(messages != null){
                view.showMessageInbox(messages);
            }
            view.showButtonMessageInbox();
            view.hideCircularProgressBarOptions();
            view.showContentOptions();
        }catch (Exception ex){
            view.showButtonMessageInbox();
            view.hideCircularProgressBarOptions();
            view.showContentOptions();
        }
    }
    @Override
    public <T> void onErrorMessageInbox(Response<BaseResponse<T>> response) {
        view.showButtonMessageInbox();
        view.hideCircularProgressBarOptions();
        view.showContentOptions();
    }


    @Override
    public void validateSuscriptionNequi(BaseRequest baseRequest) {
        model.getSuscriptionNequi(baseRequest, this);
    }
    @Override
    public <T> void onSuccessGetSuscriptionNequi(Response<BaseResponseNequi<T>> response) {
        try{
            BaseResponseNequi<ResponseConsultaSuscripcion> consulta = (BaseResponseNequi<ResponseConsultaSuscripcion>)response.body();
            if(consulta.getResult() == null){
                view.saveSuscriptionData(null, null);
            }else{
                ResponseConsultaSuscripcion.GetSuscriptionNequi suscriptionNequi = consulta.getResult().getResultNequi();
                String status = suscriptionNequi.getResponseMessage().getResponseBody().getAny().getGetSubscriptionRS().getSubscription().getStatus();
                if(status.equals("0") || status.equals("2") || status.equals("3") ){
                    view.saveSuscriptionData(null, status);
                }else if(status.equals("1")){
                    view.saveSuscriptionData(consulta.getResult().getDatosSuscripcion(), status);
                }else{
                    view.saveSuscriptionData(null, null);
                }
            }
        }catch (Exception ex){
            view.saveSuscriptionData(null, null);
        }
    }
    @Override
    public <T> void onErrorGetSuscriptionNequi(Response<BaseResponseNequi<T>> response) {
        view.saveSuscriptionData(null, null);
    }
    @Override
    public void onFailureGetSuscriptionNequi(Throwable t) {
        view.saveSuscriptionData(null, null);
    }


    @Override
    public void fetchCommercialBanner(BaseRequest baseRequest) {
        view.showCircularProgressBar();
        model.getCommercialBanner(baseRequest, this);
    }
    @Override
    public <T> void onSuccessCommercialBanner(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        try{
            List<ResponseBannerComercial> commercialBanners = (List<ResponseBannerComercial>) response.body().getResultado();
            if(commercialBanners != null){
                view.showCommercialBanner(commercialBanners);
            }else{
                view.hideCommercialBanner();
            }
        }catch (Exception ex){
            Log.d("Error",ex.getMessage());
            view.hideCommercialBanner();
        }
    }
    @Override
    public <T> void onErrorCommercialBanner(Response<BaseResponse<T>> response) {
        view.hideCommercialBanner();
    }

    @Override
    public void fetchButtonStateResorts() {
        model.getButtonStateResorts(this);
    }
    @Override
    public <T> void onSuccessButtonStateResorts(Response<BaseResponse<T>> response) {
        try{
            ResponseParametrosAPP stateResorts = (ResponseParametrosAPP) response.body().getResultado();
            if(stateResorts != null && stateResorts.getEstado() != null && stateResorts.getEstado().equals("Y")){
                view.showButtonResorts(stateResorts.getValue1());
            }else{
                view.hideButtonResorts();
            }
        }catch (Exception ex){
            view.hideButtonResorts();
        }
    }
    @Override
    public <T> void onErrorButtonStateResorts(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        view.hideButtonResorts();
    }

    @Override
    public void  getTimeExpiration(){
        model.getTimeExpiration(this);
    }
    @Override
    public <T> void onSuccessGetTimeExpiration(Response<ResponseNequiGeneral> response){
        try{
            if(response != null && response.body() != null && response.body().getN_ValorParametro() != null
                    && response.body().getN_ValorParametro() != ""){
                view.resultTimeExpiration(Integer.parseInt(response.body().getN_ValorParametro()));
            }else{
                view.resultTimeExpiration(15);
            }
            view.getTimeOfSuscription();
        }catch(Exception ex){
            view.resultTimeExpiration(15);
            view.getTimeOfSuscription();
        }
    }
    @Override
    public <T> void onErrorGetTimeExpiration(Response<ResponseNequiGeneral> response) {
        view.resultTimeExpiration(15);
        view.getTimeOfSuscription();
    }


    @Override
    public void  getTimeOfsuscription(BaseRequestNequi request){
        if(request != null){
            model.getMinutesOfSuscription(request, this);
        }
    }
    @Override
    public <T> void onSuccessGetTimeOfSuscription(Response<BaseResponseNequi<ResponseSuscriptionStatus>> response) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date fechaSuscription = format.parse(response.body().getResult().getF_FechaCreacion());
        Date fechaActual = new  Date();
        long diff = fechaActual.getTime() - fechaSuscription.getTime();
        long diffDays = diff / 24;
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        view.calculeMinutes((int)diffDays, (int)diffHours, (int)diffMinutes, (int) diffSeconds);
    }
    @Override
    public <T> void onFailureGetTimeOfSuscription(Response<BaseResponseNequi<T>> response) {
        view.calculeMinutes(null, null, null, null);
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        view.showExpiredToken(response.body().getErrorToken());
    }
    @Override
    public <T> void onExpiredTokenNequi(Response<BaseResponseNequi<T>> response) {
        view.hideCircularProgressBar();
        view.showExpiredToken(response.body().getErrorToken());
    }

    @Override
    public <T> void onError(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        if(response != null){
            view.showDataFetchError("Lo sentimos", response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideCircularProgressBar();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }
}
