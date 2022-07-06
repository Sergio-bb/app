package solidappservice.cm.com.presenteapp.front.nequi.transfieredinero.FragmentSuscriptionsPayment;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestCheckStatusPaymentSuscription;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestPaymentSuscritpion;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestReversePaymentSuscription;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultarTopes;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiGeneral;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponsePaymentSuscription;

public interface FragmentSuscriptionsPaymentContract {

    interface View{
        void fetchAccountsAvailable();
        void resultAccountsAvailable(boolean pocketAvailable, boolean pocketPayroll);
        void fetchAccounts();
        void showAccounts(List<ResponseProducto> cuentas);
        void fetchMaximumTranferValues();
        void showMaximumTranferValues(ResponseConsultarTopes topes);
        void showDialogGetBalanceNequi();
        void fetchIncompleteSubscriptionPayments();
        void fetchNequiBalance();
        void showNequiBalance(String saldoNequi);
        void fetchAuthorizationNequiBalance();
        void resultGetAuthorizationNequiBalance(String status);
        void showCircularProgressBarNequiBalance();
        void hideCircularProgressBarNequiBalance();
        void validateDataTransfer();
        void showDialogConfirmTransfer(int valorTransferencia, ResponseProducto cuentaOrigen);
        void hideDialogConfirmTransfer();
        void makePaymentSuscription(int valorTransferencia, ResponseProducto cuentaOrigen);
        void showDialogTransferLoading();
        void editTextDialogTransferLoading(String text);
        void hideDialogTransferLoading();
        void showDialogTransferSuccess();
        void showDialogTransferError(String message);
        void showDialogTransferPending();
        void fetchReversePaymentSubscription(ResponsePaymentSuscription resultTransfer);
        void fetchCheckStatusPaymentSubscription(ResponsePaymentSuscription resultTransfer);
        void showContentNequiPaySuscriptions();
        void hideContentNequiPaySuscriptions();
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
        void fetchIncompleteSubscriptionPayments(BaseRequest baseRequest);
        void fetchNequiBalance(BaseRequestNequi body);
        void fetchAuthorizationNequiBalance(BaseRequestNequi body);
        void makePaymentSuscription(RequestPaymentSuscritpion body);
        void fetchReversePaymentSubscription(RequestReversePaymentSuscription body);
        void fetchCheckStatusPaymentSubscription(RequestCheckStatusPaymentSuscription body);
    }

    interface Model{
        void getAccountsAvailable(final APIListener listener);
        void getAccounts(BaseRequest  body, final APIListener listener);
        void getMaximumTranferValues(BaseRequest  body, final APIListener listener);
        void getIncompleteSubscriptionPayments(BaseRequest  body, final APIListener listener);
        void getAuthorizationNequiBalance(BaseRequestNequi  body, final APIListener listener);
        void getNequiBalance(BaseRequestNequi  body, final APIListener listener);
        void makePaymentSuscription(RequestPaymentSuscritpion base, final APIListener listener);
        void reverseNequiSubscriptions(RequestReversePaymentSuscription base, final APIListener listener);
        void checkStatusPaymentSubscription(RequestCheckStatusPaymentSuscription base, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessAccountsAvailable(Response<ResponseNequiGeneral> response);
        <T> void onErrorAccountsAvailable(Response<ResponseNequiGeneral> response);

        <T> void onSuccessAccounts(Response<BaseResponse<T>> response);
        <T> void onErrorAccounts(Response<BaseResponse<T>> response);

        <T> void onSuccessMaximumTranferValues(Response<BaseResponseNequi<T>> response);
        <T> void onErrorMaximumTranferValues(Response<BaseResponseNequi<T>> response);

        <T> void onSuccessIncompleteSubscriptionPayments(Response<BaseResponseNequi<T>> response);
        <T> void onErrorIncompleteSubscriptionPayments(Response<BaseResponseNequi<T>> response);

        <T> void onSuccessAuthorizationNequiBalance(Response<BaseResponseNequi<T>> response);
        <T> void onErrorAuthorizationNequiBalance(Response<BaseResponseNequi<T>> response);

        <T> void onSuccessNequiBalance(Response<BaseResponseNequi<T>> response);
        <T> void onErrorNequiBalance(Response<BaseResponseNequi<T>> response);

        <T> void onSuccessPaymentSuscription(Response<BaseResponseNequi<T>> response, boolean isSuccessfulTransfer);
        <T> void onErrorPaymentSuscription(Response<BaseResponseNequi<T>> response);

        <T> void onSuccessReversePaymentSuscription(Response<BaseResponseNequi<T>> response);
        <T> void onErrorReversePaymentSuscription(Response<BaseResponseNequi<T>> response);

        <T> void onSuccessCheckStatusPaymentSubscription(Response<BaseResponseNequi<T>> response, boolean isSuccessfulTransfer);
        <T> void onErrorCheckStatusPaymentSubscription(Response<BaseResponseNequi<T>> response);

        <T> void onExpiredTokenNequi(Response<BaseResponseNequi<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);

        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
