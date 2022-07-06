package solidappservice.cm.com.presenteapp.front.estadocuenta.FragmentStatusAccount;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseConsultaAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestActualizarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestConsultarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.mensajes.request.RequestEnviarMensaje;
import solidappservice.cm.com.presenteapp.front.nequi.transfieredinero.FragmentSuscriptionsPayment.FragmentSuscriptionsPaymentContract;

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
        void showAccountStatus(List<ResponseProducto> cuentas);
        void showSectionAcccountStatus();
        void hideSectionAcccountStatus();
        void fetchNequiBalance();
        void fetchAuthorizationNequiBalance();
        void resultGetAuthorizationNequiBalance(String status);
        void showNequiBalance(String saldoNequi);
        void showDialogGetBalanceNequi();
        void showCircularProgressBar(String message);
        void hideCircularProgressBar();
        void showErrorWithRefresh();
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchSalaryAdvanceMovements(BaseRequest baseRequest);
        void processSalaryAdvancePending(RequestConsultarAdelantoNomina request);
        void updateSalaryAdvanceStatus(RequestActualizarAdelantoNomina request);
        void sendSalaryAdvanceNotification(RequestEnviarMensaje request);
        void fetchAccountStatus(BaseRequest baseRequest);
        void fetchNequiBalance(BaseRequestNequi baseRequest);
        void fetchAuthorizationNequiBalance(BaseRequestNequi body);
    }

    interface Model{
        void getSalaryAdvanceMovements(BaseRequest body, final APIListener listener);
        void processSalaryAdvancePending(RequestConsultarAdelantoNomina body, final APIListener listener);
        void updateSalaryAdvanceStatus(RequestActualizarAdelantoNomina body, final APIListener listener);
        void sendSalaryAdvanceNotification(RequestEnviarMensaje body, final APIListener listener);
        void getAccountStatus(BaseRequest body, final APIListener listener);
        void getNequiBalance(BaseRequestNequi body, final APIListener listener);
        void getAuthorizationNequiBalance(BaseRequestNequi  body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessSalaryAdvanceMovements(Response<BaseResponse<T>> response);
        <T> void onSuccessProcessSalaryAdvancePending(Response<BaseResponse<T>> response);
        <T> void onSuccessUpdateSalaryAdvanceStatus(Response<BaseResponse<T>> response);
        <T> void onSuccessSendSalaryAdvanceNotification(Response<BaseResponse<T>> response);
        <T> void onErrorSalaryAdvance(Response<BaseResponse<T>> response);
        void onFailureSalaryAdvance(Throwable t, boolean isErrorTimeOut);

        <T> void onSuccessAccountStatus(Response<BaseResponse<T>> response);
        <T> void onErrorAccountStatus(Response<BaseResponse<T>> response);
        void onFailureAccountStatus(Throwable t, boolean isErrorTimeOut);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);

        <T> void onSuccessNequiBalance(Response<BaseResponseNequi<T>> response);
        <T> void onErrorNequiBalance(Response<BaseResponseNequi<T>> response);

        <T> void onSuccessAuthorizationNequiBalance(Response<BaseResponseNequi<T>> response);
        <T> void onErrorAuthorizationNequiBalance(Response<BaseResponseNequi<T>> response);

        <T> void onExpiredTokenNequi(Response<BaseResponseNequi<T>> response);
    }

}
