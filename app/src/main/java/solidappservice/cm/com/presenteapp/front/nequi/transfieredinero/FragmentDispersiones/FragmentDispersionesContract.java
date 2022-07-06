package solidappservice.cm.com.presenteapp.front.nequi.transfieredinero.FragmentDispersiones;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.nequi.dto.SuscriptionData;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestPaymentDispersion;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestReversePaymentDispersion;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiGeneral;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultarTopes;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponsePaymentDispersionError;
import solidappservice.cm.com.presenteapp.front.menuprincipal.FragmentHome.FragmentHomeContract;

public interface FragmentDispersionesContract {

    interface View{
        void fetchAccountsAvailable();
        void resultAccountsAvailable(boolean pocketAvailable, boolean pocketPayroll);
        void fetchAccounts();
        void showAccounts(List<ResponseProducto> cuentas);
        void fetchMaximumTranferValues();
        void showMaximumTranferValues(ResponseConsultarTopes topes);
        void validateDataTransfer();
        void showDialogConfirmTransfer(int valorTransferencia, ResponseProducto cuentaOrigen);
        void hideDialogConfirmTransfer();
        void makePaymentDispersion(int valorTransferencia, ResponseProducto cuentaOrigen);
        void showDialogTransferLoading();
        void hideDialogTransferLoading();
        void showDialogTransferSuccess();
        void showDialogTransferError(String message);
        void fetchReverseDispersion(ResponsePaymentDispersionError resultTransfer);
        void showContentNequiDispersiones();
        void hideContentNequiDispersiones();
        void showCircularProgressBar(String message);
        void hideCircularProgressBar();
        void showErrorWithRefresh();
        void showDialogError(String title, String message);
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);
        void showErrorSuscription();
        void showRebaseTopsNequi();
        void saveSuscriptionData(SuscriptionData datosSuscripcion);
        }

    interface Presenter{
        void fetchAccountsAvailable();
        void fetchAccounts(BaseRequest body);
        void fetchMaximumTranferValues(BaseRequest body);
        void makePaymentDispersion(RequestPaymentDispersion body);
        void fetchReversePaymentDispersion(RequestReversePaymentDispersion body);
        void getCostoPorOperacion();
        void validateSuscriptionNequi(BaseRequest baseRequest);
    }

    interface Model{
        void getAccountsAvailable(final APIListener listener);
        void getAccounts(BaseRequest  body, final APIListener listener);
        void getMaximumTranferValues(BaseRequest  body, final APIListener listener);
        void makePaymentDispersion(RequestPaymentDispersion base, final APIListener listener);
        void reverseNequiPaymentDispersion(RequestReversePaymentDispersion base, final APIListener listener);
        void getCobroPorSuscripcion(final APIListener listener);
        void getSuscriptionNequi(BaseRequest baseRequest, APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessAccountsAvailable(Response<ResponseNequiGeneral> response);
        <T> void onErrorAccountsAvailable(Response<ResponseNequiGeneral> response);

        <T> void onSuccessAccounts(Response<BaseResponse<T>> response);
        <T> void onErrorAccounts(Response<BaseResponse<T>> response);

        <T> void onSuccessMaximumTranferValues(Response<BaseResponseNequi<T>> response);
        <T> void onErrorMaximumTranferValues(Response<BaseResponseNequi<T>> response);

        <T> void onSuccessMakePaymentDispersion(Response<BaseResponseNequi<T>> response);
        <T> void onErrorMakePaymentDispersion(Response<BaseResponseNequi<T>> response);

        <T> void onSuccessReversePaymentDispersion(Response<BaseResponseNequi<T>> response);
        <T> void onErrorReversePaymentDispersion(Response<BaseResponseNequi<T>> response);

        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onExpiredTokenNequi(Response<BaseResponseNequi<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);

        void onSuccesGetCoborPorOperacion(Response<Integer> response);

        <T> void onSuccessGetSuscriptionNequi(Response<BaseResponseNequi<T>> response);
    }

}
