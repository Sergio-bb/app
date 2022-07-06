package solidappservice.cm.com.presenteapp.front.pagoobligaciones.FragmentPaymentCredits;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProductos;
import solidappservice.cm.com.presenteapp.entities.pagoobligaciones.request.RequestEnviarPago;

/**
 * CREADO POR MIGUEL DAVID CABEZAS 17/09/2021.
 */
public interface FragmentPaymentCreditsContract {

    interface View{
        void showPaymentDetails(int positionPayment);
        void fetchPendingPayments();
        void showResultPendingPayments();
        void fetchProducts();
        void showProductstoDebit(List<ResponseProductos> products);
        void showProductstoPay(List<ResponseProductos> products);
        void validateDataPayment();
        void showDialogConfirmPayment(Double paymentValue, ResponseProductos productToPay, ResponseProductos productoToDebit);
        void makePayment(Double paymentValue, ResponseProductos productToPay, ResponseProductos productoToDebit);
        void showResultPayment(String resultPayment);
        void enabledCheckboxSaldo(boolean enabled);
        void enabledCheckboxOtroValor(boolean enabled);
        void disabledAcceptButton();
        void enabledAcceptButton();
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showDataFetchError(String message);
        void showErrorTimeOut();
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchPendingPayments(BaseRequest baseRequest);
        void fetchProducts(BaseRequest baseRequest);
        void makePayment(RequestEnviarPago request);
    }

    interface Model{
        void getPendingPayments(BaseRequest baseRequest, final APIListener listener);
        void getProducts(BaseRequest baseRequest, final APIListener listener);
        void makePayment(RequestEnviarPago baseRequest, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessPendingPayments(Response<BaseResponse<T>> response);
        <T> void onSuccessProducts(Response<BaseResponse<T>> response);
        <T> void onSuccessMakePayment(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
