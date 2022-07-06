package solidappservice.cm.com.presenteapp.front.estadocuenta.FragmentStatusAccount;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestActualizarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestConsultarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseConsultaAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseMovimientos;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProductos;
import solidappservice.cm.com.presenteapp.entities.mensajes.request.RequestEnviarMensaje;
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
        view.showCircularProgressBar("Validando datos...");
        model.getSalaryAdvanceMovements(baseRequest, this);
    }

    @Override
    public void processSalaryAdvancePending(RequestConsultarAdelantoNomina request) {
        model.processSalaryAdvancePending(request, this);
    }

    @Override
    public void updateSalaryAdvanceStatus(RequestActualizarAdelantoNomina request) {
        model.updateSalaryAdvanceStatus(request, this);
    }

    @Override
    public void sendSalaryAdvanceNotification(RequestEnviarMensaje request) {
        model.sendSalaryAdvanceNotification(request, this);
    }

    @Override
    public void fetchAccountStatus(BaseRequest baseRequest) {
        view.showCircularProgressBar("Actualizando estado de cuenta...");
        model.getAccountStatus(baseRequest, this);
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
                    view.hideCircularProgressBar();
                    view.fetchAccountStatus();
                }
            }else{
                view.hideCircularProgressBar();
                view.fetchAccountStatus();
            }
        }catch (Exception ex){
            view.showDataFetchError("");
        }
    }

    @Override
    public <T> void onSuccessProcessSalaryAdvancePending(Response<BaseResponse<T>> response) {
        try{
            ResponseConsultaAdelantoNomina consulta = (ResponseConsultaAdelantoNomina) response.body().getResultado();
            if(consulta != null){
                if(!consulta.getN_resultado().equals("ERROR")){
                    view.updateSalaryAdvanceStatus(consulta);
                }else{
                    view.hideCircularProgressBar();
                    view.fetchAccountStatus();
                }
            }else{
                view.hideCircularProgressBar();
                view.fetchAccountStatus();
            }
        }catch (Exception ex){
            view.showDataFetchError("");
        }
    }

    @Override
    public <T> void onSuccessUpdateSalaryAdvanceStatus(Response<BaseResponse<T>> response) {
        try{
            String result = (String) response.body().getResultado();
            if(result != null && result.equals("OK")){
                view.sendSalaryAdvanceNotification();
            }else{
                view.hideCircularProgressBar();
                view.fetchAccountStatus();
            }
        }catch (Exception ex){
            view.showDataFetchError("");
        }
    }

    @Override
    public <T> void onSuccessSendSalaryAdvanceNotification(Response<BaseResponse<T>> response) {
        try{
            String result = (String) response.body().getResultado();
            view.hideCircularProgressBar();
            view.fetchAccountStatus();
        }catch (Exception ex){
            view.showDataFetchError("");
        }
    }

    @Override
    public <T> void onSuccessAccountStatus(Response<BaseResponse<T>> response) {
        try{
            List<ResponseProductos> accounts = (List<ResponseProductos>) response.body().getResultado();
            for(ResponseProductos product : accounts){
                Encripcion encripcion = Encripcion.getInstance();
                product.setA_numdoc(encripcion.desencriptar(product.getA_numdoc().trim()));
            }
            view.showAccountStatus(accounts);
            view.hideCircularProgressBar();
        }catch (Exception ex){
            view.showDataFetchError("");
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
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        view.showExpiredToken(response.body().getErrorToken());
    }

    @Override
    public <T> void onError(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        if(response != null){
            view.showDataFetchError(response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("");
        }
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideProgressDialog();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("");
        }
    }

}
