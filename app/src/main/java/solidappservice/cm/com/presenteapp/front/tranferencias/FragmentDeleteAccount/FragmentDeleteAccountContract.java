package solidappservice.cm.com.presenteapp.front.tranferencias.FragmentDeleteAccount;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.transferencias.response.ResponseCuentasInscritas;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.transferencias.request.RequestDeleteAccount;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 11/09/2021.
 */
public interface FragmentDeleteAccountContract {

    interface View{
        void fetchRegisteredAccounts();
        void showRegisteredAccounts(List<ResponseCuentasInscritas> cuentas);
        void confirmDeleteSelectedAccounts(String cuentas);
        void deleteSelectedAccounts(List<ResponseCuentasInscritas> accountsSelected);
        void showResultDeleteAccounts(String resultDelete);
        void disabledDeleteAccountButton();
        void enabledDeleteAccountButton();
        void showSectionDeleteAccount();
        void hideSectionDeleteAccount();
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
        void fetchRegisteredAccounts(BaseRequest baseRequest);
        void deleteSelectedAccounts(List<RequestDeleteAccount> body);
    }

    interface Model{
        void getRegisteredAccounts(BaseRequest body, final APIListener listener);
        void deleteSelectedAccounts(RequestDeleteAccount body, boolean isLastSelected, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessRegisteredAccounts(Response<BaseResponse<T>> response);
        <T> void onErrorRegisteredAccounts(Response<BaseResponse<T>> response);
        void onFailureRegisteredAccounts(Throwable t, boolean isErrorTimeOut);

        <T> void onSuccessDeleteSelectedAccounts(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
