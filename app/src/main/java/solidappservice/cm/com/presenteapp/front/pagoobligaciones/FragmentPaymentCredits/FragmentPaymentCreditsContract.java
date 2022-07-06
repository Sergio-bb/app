package solidappservice.cm.com.presenteapp.front.pagoobligaciones.FragmentPaymentCredits;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
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
        void showProductstoDebit(List<ResponseProducto> products);
        void showProductstoPay(List<ResponseProducto> products);
        void validateDataPayment();
        void showDialogConfirmPayment(Double paymentValue, ResponseProducto productToPay, ResponseProducto productoToDebit);
        void makePayment(Double paymentValue, ResponseProducto productToPay, ResponseProducto productoToDebit);
        void showResultPayment(String resultPayment);
        void enabledCheckboxSaldo(boolean enabled);
        void enabledCheckboxOtroValor(boolean enabled);
        void disabledAcceptButton();
        void enabledAcceptButton();
        void showSectionPaymentsCredits();
        void hideSectionPaymentsCredits();
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
        <T> void onErrorPendingPayments(Response<BaseResponse<T>> response);
        void onFailurePendingPayments(Throwable t, boolean isErrorTimeOut);

        <T> void onSuccessProducts(Response<BaseResponse<T>> response);
        <T> void onErrorProducts(Response<BaseResponse<T>> response);
        void onFailureProducts(Throwable t, boolean isErrorTimeOut);

        <T> void onSuccessMakePayment(Response<BaseResponse<T>> response);
        <T> void onErrorMakePayment(Response<BaseResponse<T>> response);
        void onFailureMakePayment(Throwable t, boolean isErrorTimeOut);

        <T> void onExpiredToken(Response<BaseResponse<T>> response);
    }

}
