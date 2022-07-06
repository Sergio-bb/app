package solidappservice.cm.com.presenteapp.front.tranferencias.FragmentMakeTransfer;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.transferencias.request.RequestMakeTransfer;
import solidappservice.cm.com.presenteapp.entities.transferencias.response.ResponseCuentasInscritas;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 11/09/2021.
 */
public interface FragmentMakeTransferContract {

    interface View{
        void fetchIncompleteTransfers();
        void showResultIncompleteTransfers();
        void fetchRegisteredAccounts();
        void showRegisteredAccounts(List<ResponseCuentasInscritas> cuentasInscritas);
        void fetchAccounts();
        void showAccounts(List<ResponseProducto> cuentas);
        void validateDataTransfer();
        void showDialogConfirmTransfer(Double valorTransferencia, ResponseProducto cuentaOrigen, ResponseCuentasInscritas cuentaDestino);
        void makeTransfer(Double valorTransferencia, ResponseProducto cuentaOrigen, ResponseCuentasInscritas cuentaDestino);
        void showResultTransfer(String resultMessage);
        void disabledMakeTransferButton();
        void enabledMakeTransferButton();
        void showSectionMakeTransfer();
        void hideSectionMakeTransfer();
        void showCircularProgressBar(String message);
        void hideCircularProgressBar();
        void showErrorWithRefresh();
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showDialogError(String title, String message);
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchIncompleteTransfers(BaseRequest baseRequest);
        void fetchRegisteredAccounts(BaseRequest baseRequest);
        void fetchAccounts(BaseRequest baseRequest);
        void makeTransfer(RequestMakeTransfer request);
    }

    interface Model{
        void getIncompleteTransfers(BaseRequest body, final APIListener listener);
        void getRegisteredAccounts(BaseRequest body, final APIListener listener);
        void getAccounts(BaseRequest body, final APIListener listener);
        void makeTransfer(RequestMakeTransfer body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessIncompleteTransfers(Response<BaseResponse<T>> response);
        <T> void onErrorIncompleteTransfers(Response<BaseResponse<T>> response);
        void onFailureIncompleteTransfers(Throwable t, boolean isErrorTimeOut);

        <T> void onSuccessRegisteredAccounts(Response<BaseResponse<T>> response);

        <T> void onSuccessAccounts(Response<BaseResponse<T>> response);
        <T> void onSuccessMakeTransfer(Response<BaseResponse<T>> response);

        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
    }

}