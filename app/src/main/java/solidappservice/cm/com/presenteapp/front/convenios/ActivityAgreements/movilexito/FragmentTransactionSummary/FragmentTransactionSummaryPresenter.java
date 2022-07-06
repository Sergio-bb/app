package solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.movilexito.FragmentTransactionSummary;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.InputStream;
import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseConsultarDatosAsociado;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.datosasociado.response.ResponseDatosBasicosAsociado;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProductosV2;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestAccessToken;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestActualizarRecarga;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestEmail;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestRealizarPago;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestResumenTransaccion;
import solidappservice.cm.com.presenteapp.entities.movilexito.response.ResponseActualizarRecarga;
import solidappservice.cm.com.presenteapp.entities.movilexito.response.ResponseRealizarPago;
import solidappservice.cm.com.presenteapp.entities.movilexito.response.ResponseRealizarRecarga;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestRealizarRecargar;
import solidappservice.cm.com.presenteapp.entities.movilexito.response.ResponseResumenTransaccion;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseParametrosAPP;

public class FragmentTransactionSummaryPresenter implements FragmentTransactionSummaryContract.Presenter, FragmentTransactionSummaryContract.APIListener {

    FragmentTransactionSummaryView view;
    FragmentTransactionSummaryModel model;

    public FragmentTransactionSummaryPresenter(@NonNull FragmentTransactionSummaryView view, @NonNull FragmentTransactionSummaryModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchDocumentType(BaseRequest baseRequest) {
        view.hideContentTransactionSummary();
        view.showCircularProgressBar("Obteniendo datos...");
        model.getDocumentType(baseRequest, this);
    }
    @Override
    public void onSuccessDocumentType(Response<BaseResponse<ResponseDatosBasicosAsociado>> response) {
        try{
            ResponseDatosBasicosAsociado asociado = (ResponseDatosBasicosAsociado) response.body().getResultado();
            String tipoDocumento = "CC";
            if(asociado != null && asociado.getTipoDocumento() != null && asociado.getTipoDocumento() != ""){
                tipoDocumento = asociado.getTipoDocumento();
            }
            view.resultDocumentType(tipoDocumento);
            view.hideCircularProgressBar();
            view.showContentTransactionSummary();
        }catch (Exception ex){
            view.hideCircularProgressBar();
            view.showDialogPaymentError("");
        }
    }
    @Override
    public void onErrorDocumentType(Response<BaseResponse<ResponseDatosBasicosAsociado>> response) {
        view.hideCircularProgressBar();
        view.showDialogPaymentError("");
    }

    @Override
    public void getDataBasicTokenPresenteME() {
        view.hideContentTransactionSummary();
        view.showCircularProgressBar("Un momento...");
        model.getDataBasicTokenPresenteME(this);
    }
    @Override
    public void onSuccessGetDataBasicTokenPresenteME(Response<BaseResponse<ResponseParametrosAPP>> response) {
        try {
            ResponseParametrosAPP basicToken = (ResponseParametrosAPP) response.body().getResultado();
            view.resultDataBasicTokenPresenteME(basicToken);
            view.fetchSummaryTransaction();
//            fetchAccessTokenOAuthPresenteME(basicToken.getValue1());
        }catch(Exception ex){
            view.showDialogPaymentError("");
        }
    }
    @Override
    public void onErrorGetDataBasicTokenPresenteME(Response<BaseResponse<ResponseParametrosAPP>> response) {
        view.showDialogPaymentError("");
    }

    @Override
    public void fetchSummaryTransaction(RequestResumenTransaccion body, RequestAccessToken accessToken) {
        view.hideContentTransactionSummary();
        view.showCircularProgressBar("Estamos confirmando tus datos...");
        model.getSummaryTransaction(body, accessToken,this);
    }
    @Override
    public void onSuccessSummaryTransaction(Response<BaseResponse<ResponseResumenTransaccion>> response) {
        try{
            ResponseResumenTransaccion resumenTransaccion = (ResponseResumenTransaccion) response.body().getResultado();
            if(resumenTransaccion != null){
                view.showSummaryTransaction(resumenTransaccion);
                view.fetchAccounts();
            }else{
                view.hideCircularProgressBar();
                view.showDialogPaymentError("");
            }
        }catch (Exception ex){
            view.hideCircularProgressBar();
            view.showDialogPaymentError("");
        }
    }
    @Override
    public void onErrorSummaryTransaction(Response<BaseResponse<ResponseResumenTransaccion>> response) {
        view.hideCircularProgressBar();
        if(response != null && response.body() != null
                && response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().equals("")){
            view.showDialogPaymentError(response.body().getMensajeErrorUsuario());
        }else{
            view.showDialogPaymentError("");
        }
    }

    @Override
    public void fetchAccounts(BaseRequest baseRequest) {
        view.hideContentTransactionSummary();
        view.showCircularProgressBar("Obteniendo tus bolsillos...");
        model.getAccounts(baseRequest, this);
    }
    @Override
    public void onSuccessAccounts(Response<BaseResponse<List<ResponseProductosV2>>> response) {
        try{
            List<ResponseProductosV2> responseCuentas = (List<ResponseProductosV2>) response.body().getResultado();
            if(responseCuentas != null && responseCuentas.size() > 0){
                view.showAccounts(responseCuentas);
                view.fetchDocumentType();
            }else{
                view.hideCircularProgressBar();
                view.showDialogPaymentError("");
            }
        }catch (Exception ex){
            view.hideCircularProgressBar();
            view.showDialogPaymentError("");
        }
    }
    @Override
    public void onErrorAccounts(Response<BaseResponse<List<ResponseProductosV2>>> response) {
        view.hideCircularProgressBar();
        view.showDialogPaymentError("");
    }


//    @Override
//    public void fetchAccessTokenOAuthPresenteME(String basicToken) {
//        model.getAccessTokenOAuthPresenteME(new RequestAccessToken(
//            basicToken.split(";")[0],
//            basicToken.split(";")[1],
//            "client_credentials",
//            ""
//        ), this);
//    }
//    @Override
//    public void onSuccessAccessTokenOAuthPresenteME(Response<ResponseAccessTokenOAuthPresenteME> response) {
//        try {
//            ResponseAccessTokenOAuthPresenteME accessToken = (ResponseAccessTokenOAuthPresenteME) response.body();
//            view.makePayment(accessToken);
//        }catch(Exception ex){
//            view.showDialogPaymentError();
//        }
//    }
//    @Override
//    public void onErrorAccessTokenOAuthPresenteME(Response<ResponseAccessTokenOAuthPresenteME> response) {
//        view.showDialogPaymentError();
//    }


    @Override
    public void makePayment(RequestRealizarPago body, RequestAccessToken accessToken) {
        view.showDialogPaymentLoading();
        model.makePayment(body, accessToken, this);
    }
    @Override
    public void onSuccessMakePayment(Response<BaseResponse<ResponseRealizarPago>> response) {
        try {
            ResponseRealizarPago resultPayment = (ResponseRealizarPago) response.body().getResultado();
            if(resultPayment != null && resultPayment.getIdPagoME() != null && !resultPayment.getIdPagoME().isEmpty()){
                view.resultMakePayment(resultPayment);
                view.performLineRechargeME();
            }
        }catch(Exception ex){
            view.hideDialogPaymentLoading();
            view.showDialogPaymentError("");
        }
    }
    @Override
    public void onErrorMakePayment(Response<BaseResponse<ResponseRealizarPago>> response) {
        view.hideDialogPaymentLoading();
        if(response != null && response.body() != null
                && response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().equals("")){
            view.showDialogPaymentError(response.body().getMensajeErrorUsuario());
        }else{
            view.showDialogPaymentError("");
        }
    }


//    @Override
//    public void fetchAccessTokenOAuthME(RequestAccessToken request, InputStream certificate) {
//        model.getAccessTokenOAuthME(request, certificate, this);
//    }
//    @Override
//    public void onSuccessAccessTokenOAuthME(Response<ResponseAccessTokenOAuthME> response) {
//        try {
//            ResponseAccessTokenOAuthME accessToken = (ResponseAccessTokenOAuthME) response.body();
//            view.performLineRechargeME(accessToken);
//        }catch(Exception ex){
//            //Enviar correo de fallo recarga
//        }
//    }
//    @Override
//    public void onErrorAccessTokenOAuthME(Response<ResponseAccessTokenOAuthME> response) {
//        //Enviar correo de fallo recarga
//    }


    @Override
    public void performLineRechargeME(RequestRealizarRecargar request, RequestAccessToken accessToken,  InputStream certificate, String pass) {
        model.performLineRechargeME(request, accessToken, certificate, pass, this);
    }
    @Override
    public void onSuccessPerformLineRechargeME(Response<ResponseRealizarRecarga> response) {
        try {
            ResponseRealizarRecarga resultRecarga = (ResponseRealizarRecarga) response.body();
            view.hideDialogPaymentLoading();
            view.showDialogPaymentSuccess();
        }catch(Exception ex){
            view.hideDialogPaymentLoading();
            view.showDialogPaymentSuccess();
            view.updateResultRechargeME(ex.getMessage());
            view.sendEmailFailedTransaction();
        }
    }
    @Override
    public void onErrorPerformLineRechargeME(String error) {
        view.hideDialogPaymentLoading();
        view.showDialogPaymentSuccess();
        view.updateResultRechargeME(error);
        view.sendEmailFailedTransaction();
    }

    @Override
    public void updateResultRechargeME(RequestActualizarRecarga request, RequestAccessToken accessToken) {
        model.updateResultRechargeME(request, accessToken, this);
    }

    @Override
    public void sendEmailFailedTransaction(RequestEmail request, RequestAccessToken accessToken) {
        model.sendEmail(request, accessToken, this);
    }

    @Override
    public void onSuccessUpdateResultRechargeME(Response<BaseResponse<ResponseActualizarRecarga>> response) {
        try {
            BaseResponse<ResponseActualizarRecarga> resultRecarga = (BaseResponse<ResponseActualizarRecarga>) response.body();
        }catch(Exception ex){
        }
    }
    @Override
    public void onErrorUpdateResultRechargeME(Response<BaseResponse<ResponseActualizarRecarga>> response) {
    }

    @Override
    public void onSuccessSendEmail(String response) {
        view.resultEmailFailedTransaction(response);
    }

    @Override
    public void onErrorSendEmail(String error) {
        view.resultEmailFailedTransaction(error);
    }


    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        if (view.isShowingDialogPaymentLoading()){
            view.hideDialogPaymentLoading();
        }
        view.hideCircularProgressBar();
        view.showExpiredToken(response.body().getErrorToken());
    }

}
