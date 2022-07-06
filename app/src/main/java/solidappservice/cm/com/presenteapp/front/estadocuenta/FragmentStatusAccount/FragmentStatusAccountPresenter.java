package solidappservice.cm.com.presenteapp.front.estadocuenta.FragmentStatusAccount;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestActualizarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestConsultarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseConsultaAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseMovimientos;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.mensajes.request.RequestEnviarMensaje;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseGetAuthorizacionBalance;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiBalance;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public class FragmentStatusAccountPresenter implements FragmentStatusAccountContract.Presenter,
        FragmentStatusAccountContract.APIListener{

    FragmentStatusAccountView view;
    FragmentStatusAccountModel model;

    public FragmentStatusAccountPresenter(@NonNull FragmentStatusAccountView view, @NonNull FragmentStatusAccountModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchSalaryAdvanceMovements(BaseRequest baseRequest) {
        view.hideSectionAcccountStatus();
        view.showCircularProgressBar("Validando datos...");
        model.getSalaryAdvanceMovements(baseRequest, this);
    }
    @Override
    public <T> void onSuccessSalaryAdvanceMovements(Response<BaseResponse<T>> response) {
        try{
            List<ResponseMovimientos> movements = (List<ResponseMovimientos>) response.body().getResultado();
            if(movements != null){
                boolean isMovementPending  = false;
                String numeroflujo = "";
                String valorSolicitado = "";
                for (ResponseMovimientos m : movements) {
                    if (m.getI_estado() != null && m.getI_estado().equals("A")) {
                        isMovementPending = true;
                        valorSolicitado = m.getV_solicitado().toString();
                        numeroflujo = m.getK_flujo().toString();
                    }
                }
                if(isMovementPending){
                    view.processSalaryAdvancePending(Integer.parseInt(numeroflujo), valorSolicitado);
                } else {
                    view.fetchAccountStatus();
                }
            }else{
                view.fetchAccountStatus();
            }
        }catch (Exception ex){
            view.fetchAccountStatus();
        }
    }


    @Override
    public void processSalaryAdvancePending(RequestConsultarAdelantoNomina request) {
        model.processSalaryAdvancePending(request, this);
    }
    @Override
    public <T> void onSuccessProcessSalaryAdvancePending(Response<BaseResponse<T>> response) {
        try{
            ResponseConsultaAdelantoNomina consulta = (ResponseConsultaAdelantoNomina) response.body().getResultado();
            if(consulta != null){
                if(!consulta.n_resultado.equals("ERROR")){
                    view.updateSalaryAdvanceStatus(consulta);
                }else{
                    view.fetchAccountStatus();
                }
            }else{
                view.fetchAccountStatus();
            }
        }catch (Exception ex){
            view.fetchAccountStatus();
        }
    }


    @Override
    public void updateSalaryAdvanceStatus(RequestActualizarAdelantoNomina request) {
        model.updateSalaryAdvanceStatus(request, this);
    }
    @Override
    public <T> void onSuccessUpdateSalaryAdvanceStatus(Response<BaseResponse<T>> response) {
        try{
            String result = (String) response.body().getResultado();
            if(result != null && result.equals("OK")){
                view.sendSalaryAdvanceNotification();
            }else{
                view.fetchAccountStatus();
            }
        }catch (Exception ex){
            view.fetchAccountStatus();
        }
    }


    @Override
    public void sendSalaryAdvanceNotification(RequestEnviarMensaje request) {
        model.sendSalaryAdvanceNotification(request, this);
    }
    @Override
    public <T> void onSuccessSendSalaryAdvanceNotification(Response<BaseResponse<T>> response) {
        try{
            String result = (String) response.body().getResultado();
            view.fetchAccountStatus();
        }catch (Exception ex){
            view.fetchAccountStatus();
        }
    }

    @Override
    public <T> void onErrorSalaryAdvance(Response<BaseResponse<T>> response) {
        view.fetchAccountStatus();
    }
    @Override
    public void onFailureSalaryAdvance(Throwable t, boolean isErrorTimeOut) {
        view.fetchAccountStatus();
    }


    @Override
    public void fetchAccountStatus(BaseRequest baseRequest) {
        view.hideSectionAcccountStatus();
        view.showCircularProgressBar("Actualizando estado de cuenta...");
        model.getAccountStatus(baseRequest, this);
    }
    @Override
    public <T> void onSuccessAccountStatus(Response<BaseResponse<T>> response) {
        try{
            List<ResponseProducto> accounts = (List<ResponseProducto>) response.body().getResultado();
            for(ResponseProducto product : accounts){
                Encripcion encripcion = Encripcion.getInstance();
                product.setA_numdoc(encripcion.desencriptar(product.getA_numdoc().trim()));
            }
            view.showAccountStatus(accounts);
            view.fetchNequiBalance();
        }catch (Exception ex){
            view.showDataFetchError("Lo sentimos", "");
            view.showErrorWithRefresh();
        }
    }
    @Override
    public <T> void onErrorAccountStatus(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        view.showDataFetchError("Lo sentimos", "");
        view.showErrorWithRefresh();
    }
    @Override
    public void onFailureAccountStatus(Throwable t, boolean isErrorTimeOut) {
        view.hideCircularProgressBar();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
        view.showErrorWithRefresh();
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        view.showExpiredToken(response.body().getErrorToken());
    }


    @Override
    public void fetchNequiBalance(BaseRequestNequi baseRequest){
        model.getNequiBalance(baseRequest, this);
    }
    @Override
    public <T> void onSuccessNequiBalance(Response<BaseResponseNequi<T>> response) {
        try{
            ResponseNequiBalance saldo = (ResponseNequiBalance) response.body().getResult();
            if(saldo != null && saldo.getData() != null & saldo.getData().getBalance() != null){
                view.showNequiBalance(saldo.getData().getBalance());
                view.hideCircularProgressBar();
                view.showSectionAcccountStatus();
            }else{
                view.fetchAuthorizationNequiBalance();
            }
        }catch(Exception ex){
            view.hideCircularProgressBar();
            view.showSectionAcccountStatus();
        }
    }
    @Override
    public <T> void onErrorNequiBalance(Response<BaseResponseNequi<T>> response) {
        view.fetchAuthorizationNequiBalance();
    }


    @Override
    public void fetchAuthorizationNequiBalance(BaseRequestNequi baseRequest){
        model.getAuthorizationNequiBalance(baseRequest, this);
    }
    @Override
    public <T> void onSuccessAuthorizationNequiBalance(Response<BaseResponseNequi<T>> response) {
        view.hideCircularProgressBar();
        view.showSectionAcccountStatus();
        try{
            BaseResponseNequi<ResponseGetAuthorizacionBalance> autorizacion = (BaseResponseNequi<ResponseGetAuthorizacionBalance>)response.body();
            if(autorizacion != null){
                if(autorizacion.getResult() != null && autorizacion.getResult().getData() != null
                        && autorizacion.getResult().getData().getStatus() != null){
                    view.resultGetAuthorizationNequiBalance(autorizacion.getResult().getData().getStatus());
                }else{
                    view.resultGetAuthorizationNequiBalance("ERROR");
                }
            }else{
                view.resultGetAuthorizationNequiBalance("ERROR");
            }
        }catch(Exception ex){
            view.resultGetAuthorizationNequiBalance("ERROR");
        }
    }
    @Override
    public <T> void onErrorAuthorizationNequiBalance(Response<BaseResponseNequi<T>> response) {
        view.hideCircularProgressBar();
        view.showSectionAcccountStatus();
        view.resultGetAuthorizationNequiBalance("ERROR");
    }

    @Override
    public <T> void onExpiredTokenNequi(Response<BaseResponseNequi<T>> response) {
        view.hideCircularProgressBar();
        view.showExpiredToken(response.body().getErrorToken());
    }

}
