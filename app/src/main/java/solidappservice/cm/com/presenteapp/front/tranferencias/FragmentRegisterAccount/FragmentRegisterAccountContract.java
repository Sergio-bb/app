package solidappservice.cm.com.presenteapp.front.tranferencias.FragmentRegisterAccount;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.transferencias.response.ResponseBanco;
import solidappservice.cm.com.presenteapp.entities.transferencias.response.ResponseCuentasInscritas;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.transferencias.request.RequestRegisterAccount;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 11/09/2021.
 */
public interface FragmentRegisterAccountContract {

    interface View{
        void fetchBanks();
        void showBanks(List<ResponseBanco> responseBancos);
        void validateRepeatAccounts(List<ResponseCuentasInscritas> responseCuentasInscritas);
        void fetchRegisteredAccounts();
        void registerAccount();
        void showResultRegisterAccount();
        void disabledRegisterAccountButton();
        void enabledRegisterAccountButton();
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showDataFetchError(String message);
        void showErrorTimeOut();
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchBanks();
        void fetchRegisteredAccounts(BaseRequest baseRequest);
        void registerAccount(RequestRegisterAccount request);
    }

    interface Model{
        void getBanks(final APIListener listener);
        void getRegisteredAccounts(BaseRequest body, final APIListener listener);
        void registerAccount(RequestRegisterAccount body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessBanks(Response<BaseResponse<T>> response);
        <T> void onSuccessRegisteredAccounts(Response<BaseResponse<T>> response);
        <T> void onSuccessRegisterAccount(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
