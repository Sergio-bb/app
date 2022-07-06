package solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.movilexito.FragmentTransactionSummary;

import java.io.InputStream;
import java.util.List;

import okhttp3.OkHttpClient;
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
import solidappservice.cm.com.presenteapp.entities.movilexito.response.ResponseResumenTransaccion;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestRealizarRecargar;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseParametrosAPP;

public interface FragmentTransactionSummaryContract {

    interface View{
        void fetchDocumentType();
        void resultDocumentType(String tipoDocumento);

        void fetchDataBasicTokenPresenteME();
        void resultDataBasicTokenPresenteME(ResponseParametrosAPP params);

        void fetchSummaryTransaction();
        void showSummaryTransaction(ResponseResumenTransaccion resumen);

        void fetchAccounts();
        void showAccounts(List<ResponseProductosV2> cuentas);

        void validateDataPayment();
        void showDialogConfirmPayment();

//        void fetchAccessTokenOAuthPresenteME();
        void makePayment();
        void resultMakePayment(ResponseRealizarPago resultPayment);

        void showDialogPaymentLoading();
        void hideDialogPaymentLoading();
        boolean isShowingDialogPaymentLoading();

//        void fetchAccessTokenOAuthME();
        void performLineRechargeME();
        void updateResultRechargeME(String resultME);

        void showDialogPaymentSuccess();
        void showDialogPaymentError(String messageError);

        void showContentTransactionSummary();
        void hideContentTransactionSummary();

        void sendEmailFailedTransaction();
        void resultEmailFailedTransaction(String resultadoEmail);

        void showCircularProgressBar(String message);
        void hideCircularProgressBar();
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showDataFetchError(String message);
        void showErrorTimeOut();
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchDocumentType(BaseRequest body);
        void getDataBasicTokenPresenteME();
        void fetchSummaryTransaction(RequestResumenTransaccion body, RequestAccessToken request);
        void fetchAccounts(BaseRequest body);
//        void fetchAccessTokenOAuthPresenteME(String basicToken);
        void makePayment(RequestRealizarPago request, RequestAccessToken accessToken);
//        void fetchAccessTokenOAuthME(RequestAccessToken request, InputStream certificate);
        void performLineRechargeME(RequestRealizarRecargar request, RequestAccessToken accessToken, InputStream certificate, String pass);
        void updateResultRechargeME(RequestActualizarRecarga request, RequestAccessToken accessToken);
        void sendEmailFailedTransaction(RequestEmail request, RequestAccessToken accessToken);
    }

    interface Model{
        void getDocumentType(BaseRequest  body, final APIListener listener);
        void getSummaryTransaction(RequestResumenTransaccion body, RequestAccessToken accessToken, final APIListener listener);
        void getAccounts(BaseRequest  body, final APIListener listener);
        void getDataBasicTokenPresenteME(final APIListener listener);
        void makePayment(RequestRealizarPago  body, RequestAccessToken accessToken, final APIListener listener);
        void performLineRechargeME(RequestRealizarRecargar recargaPaqueteRequest, RequestAccessToken accessToken, InputStream certificate, String pass, final APIListener listener);
        OkHttpClient provideUnsafeOkHttpClient(InputStream certificate, String pass);
        void updateResultRechargeME(RequestActualizarRecarga body, RequestAccessToken accessToken, final APIListener listener);
        void sendEmail(RequestEmail  body, RequestAccessToken accessToken, final APIListener listener);
    }

    interface APIListener{
        void onSuccessDocumentType(Response<BaseResponse<ResponseDatosBasicosAsociado>> response);
        void onErrorDocumentType(Response<BaseResponse<ResponseDatosBasicosAsociado>> response);

        void onSuccessSummaryTransaction(Response<BaseResponse<ResponseResumenTransaccion>> response);
        void onErrorSummaryTransaction(Response<BaseResponse<ResponseResumenTransaccion>> response);

        void onSuccessAccounts(Response<BaseResponse<List<ResponseProductosV2>>> response);
        void onErrorAccounts(Response<BaseResponse<List<ResponseProductosV2>>> response);

        void onSuccessGetDataBasicTokenPresenteME(Response<BaseResponse<ResponseParametrosAPP>> response);
        void onErrorGetDataBasicTokenPresenteME(Response<BaseResponse<ResponseParametrosAPP>> response);

        void onSuccessMakePayment(Response<BaseResponse<ResponseRealizarPago>> response);
        void onErrorMakePayment(Response<BaseResponse<ResponseRealizarPago>> response);

        void onSuccessPerformLineRechargeME(Response<ResponseRealizarRecarga> response);
        void onErrorPerformLineRechargeME(String error);

        void onSuccessUpdateResultRechargeME(Response<BaseResponse<ResponseActualizarRecarga>> response);
        void onErrorUpdateResultRechargeME(Response<BaseResponse<ResponseActualizarRecarga>> response);

        void onSuccessSendEmail(String response);
        void onErrorSendEmail(String error);

        <T> void onExpiredToken(Response<BaseResponse<T>> response);
    }
}
