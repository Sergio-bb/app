package solidappservice.cm.com.presenteapp.front.adelantonomina.FragmentDetail;

import android.util.Log;

import androidx.annotation.NonNull;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestInsertarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestLogs;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestProcesarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseValorComision;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponserSolicitarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 24/08/2021.
 */
public class FragmentDetailPresenter implements FragmentDetailContract.Presenter, FragmentDetailContract.APIListener {

    FragmentDetailView view;
    FragmentDetailModel model;

    public FragmentDetailPresenter(@NonNull FragmentDetailView view, @NonNull FragmentDetailModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchCommissionValue() {
        view.showCircularProgressBarValue();
        model.getCommissionValue(this);
    }
    @Override
    public <T> void onSuccessCommissionValue(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBarValue();
        try{
            ResponseValorComision valorComision = (ResponseValorComision) response.body().getResultado();
            view.showCommissionValue(valorComision.getValor());
        }catch (Exception ex){
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void fetchRegisterSalaryAdvance(RequestInsertarAdelantoNomina request) {
        view.showProgressDialog("Validando datos...");
        model.registerSalaryAdvance(request, this);
    }
    @Override
    public <T> void onSuccessRegisterSalaryAdvance(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            String numeroTransaccion = (String) response.body().getResultado();
            if(numeroTransaccion != null) {
                view.fetchProcessSalaryAdvance(numeroTransaccion);
            } else {
                view.enterLogs("ADELANTO DE NOMINA FALLIDO", "Ha ocurrido un error al insertar la solicitud del adelanto de nómina");
                view.showDataFetchError("Solicitud Fallida", "Tu solicitud no se ha enviado, por favor inténtalo de nuevo más tarde");
            }
        }catch (Exception ex){
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void fetchProcessSalaryAdvance(RequestProcesarAdelantoNomina request) {
        view.showProgressDialog("Enviando solicitud...");
        model.processSalaryAdvance(request, this);
    }
    @Override
    public <T> void onSuccessProcessSalaryAdvance(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            ResponserSolicitarAdelantoNomina adelanto = (ResponserSolicitarAdelantoNomina) response.body().getResultado();
            if(adelanto != null){
                if(adelanto.getN_resultado() != null && adelanto.getN_resultado().equals("OK")){
                    view.enterLogs("ADELANTO DE NOMINA EXITOSA", "Numero de flujo "+(adelanto.getV_k_flujo()==null || adelanto.getV_k_flujo()==0 ? "" : adelanto.getV_k_flujo()));
                }else{
                    view.enterLogs("ADELANTO DE NOMINA ENVIADO PERO SIN RESPUESTA", "El adelanto de nomina se envio, pero no se obtuve respuesta de la base de datos");
                }
            }else {
                view.enterLogs("ADELANTO DE NOMINA ENVIADO PERO SIN RESPUESTA", "El adelanto de nomina se envio, pero no se obtuve respuesta de la base de datos");
            }
            view.showSuccessfulSalaryAdvance();
        }catch (Exception ex){
            view.hideProgressDialog();
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void enterLogs(RequestLogs request){
        model.sendLogs(request, this);
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        view.showExpiredToken(response.body().getErrorToken());
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
            view.showDataFetchError("Lo sentimos", "Se ha producido un error, inténtalo nuevamente en unos minutos.");
        }
    }

}
