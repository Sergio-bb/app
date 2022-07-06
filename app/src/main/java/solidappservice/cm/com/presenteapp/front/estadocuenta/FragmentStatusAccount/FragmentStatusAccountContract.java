package solidappservice.cm.com.presenteapp.front.estadocuenta.FragmentStatusAccount;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseConsultaAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestActualizarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestConsultarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProductos;
import solidappservice.cm.com.presenteapp.entities.mensajes.request.RequestEnviarMensaje;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public interface FragmentStatusAccountContract {

    interface View{
        void fetchSalaryAdvanceMovements();
        void processSalaryAdvancePending(Integer idFlujo, String valorSolicitado);
        void updateSalaryAdvanceStatus(ResponseConsultaAdelantoNomina consulta);
        void sendSalaryAdvanceNotification();
        void fetchAccountStatus();
        void showAccountStatus(List<ResponseProductos> cuentas);
        void hideAccountStatus();
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showCircularProgressBar(String message);
        void hideCircularProgressBar();
        void showDataFetchError(String message);
        void showErrorTimeOut();
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchSalaryAdvanceMovements(BaseRequest baseRequest);
        void processSalaryAdvancePending(RequestConsultarAdelantoNomina request);
        void updateSalaryAdvanceStatus(RequestActualizarAdelantoNomina request);
        void sendSalaryAdvanceNotification(RequestEnviarMensaje request);
        void fetchAccountStatus(BaseRequest baseRequest);
    }

    interface Model{
        void getSalaryAdvanceMovements(BaseRequest body, final APIListener listener);
        void processSalaryAdvancePending(RequestConsultarAdelantoNomina body, final APIListener listener);
        void updateSalaryAdvanceStatus(RequestActualizarAdelantoNomina body, final APIListener listener);
        void sendSalaryAdvanceNotification(RequestEnviarMensaje body, final APIListener listener);
        void getAccountStatus(BaseRequest body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessSalaryAdvanceMovements(Response<BaseResponse<T>> response);
        <T> void onSuccessProcessSalaryAdvancePending(Response<BaseResponse<T>> response);
        <T> void onSuccessUpdateSalaryAdvanceStatus(Response<BaseResponse<T>> response);
        <T> void onSuccessSendSalaryAdvanceNotification(Response<BaseResponse<T>> response);
        <T> void onSuccessAccountStatus(Response<BaseResponse<T>> response);
        <T> void onErrorSalaryAdvance(Response<BaseResponse<T>> response);
        void onFailureSalaryAdvance(Throwable t, boolean isErrorTimeOut);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
