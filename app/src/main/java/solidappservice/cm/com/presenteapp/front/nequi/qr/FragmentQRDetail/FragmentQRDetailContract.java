package solidappservice.cm.com.presenteapp.front.nequi.qr.FragmentQRDetail;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestPaymentQR;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiGeneral;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultarTopes;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseMakePaymentQR;

public interface FragmentQRDetailContract {

    interface View{
        void fetchAccountsAvailable();
        void resultAccountsAvailable(boolean pocketAvailable, boolean pocketPayroll);
        void fetchAccounts();
        void showAccounts(List<ResponseProducto> cuentas);
        void fetchMaximumTranferValues();
        void showMaximumTranferValues(ResponseConsultarTopes topes);
        void showDataCommerceText();
        void validateDataPaymentQR();
        void showDialogConfirmPayment(int valorpago, ResponseProducto cuentaOrigen);
        void hideDialogConfirmPayment();
        void showDialogPaymentError();
        void makePaymentByQR(int valorTransferencia, ResponseProducto cuentaOrigen);
        void showDialogPaymentLoading();
        void editTextDialogPaymentLoading(String text);
        void hideDialogPaymentLoading();
        void showContentQrDetail();
        void hideContentQrDetail();
        void showCircularProgressBar(String message);
        void hideCircularProgressBar();
        void showErrorWithRefresh();
        void showDialogError(String title, String message);
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchAccountsAvailable();
        void fetchAccounts(BaseRequest body);
        void fetchMaximumTranferValues(BaseRequest body);
        void makePaymentByQR(RequestPaymentQR body);
    }

    interface Model{
        void getAccountsAvailable(final APIListener listener);
        void getAccounts(BaseRequest  body, final APIListener listener);
        void getMaximumTranferValues(BaseRequest  body, final APIListener listener);
        void makePaymentByQr(RequestPaymentQR base, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessAccountsAvailable(Response<ResponseNequiGeneral> response);
        <T> void onErrorAccountsAvailable(Response<ResponseNequiGeneral> response);

        <T> void onSuccessAccounts(Response<BaseResponse<T>> response);
        <T> void onErrorAccounts(Response<BaseResponse<T>> response);

        <T> void onSuccessMaximumTranferValues(Response<BaseResponseNequi<T>> response);
        <T> void onErrorMaximumTranferValues(Response<BaseResponseNequi<T>> response);

        void onSuccessMakePaymentByQr(Response<BaseResponseNequi<ResponseMakePaymentQR>> response);
        void onErrorMakePaymentByQr(Response<BaseResponseNequi<ResponseMakePaymentQR>> response);

        <T> void onExpiredTokenNequi(Response<BaseResponseNequi<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);

        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
