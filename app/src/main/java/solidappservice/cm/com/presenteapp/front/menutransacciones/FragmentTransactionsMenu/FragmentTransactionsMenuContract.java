package solidappservice.cm.com.presenteapp.front.menutransacciones.FragmentTransactionsMenu;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseMovimientos;
import solidappservice.cm.com.presenteapp.entities.banercomercial.response.ResponseBanerComercial;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.mensajes.response.ResponseObtenerMensajes;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 17/09/2021.
 */
public interface    FragmentTransactionsMenuContract {

    interface View{
        void fetchButtonStateAdvanceSalary();
        void showButtonAdvanceSalary();
        void hideButtonAdvanceSalary();
        void fetchButtonActionAdvanceSalary();
        void changeButtonActionAdvanceSalary(List<String> listDependencies);
        void fetchButtonStateResorts();
        void showButtonResorts(String urlLinkResort);
        void hideButtonResorts();
        void fetchAssociatedDependency();
        void showResultAssociatedDependency(String associatedDependency);
        void validateSalaryAdvanceStatus();
        void fetchPendingSalaryAdvance();
        void showResultPendingSalaryAdvance(List<ResponseMovimientos> listPendingMovements);

        void fetchButtonStateTransfers();
        void showButtonTransfers();
        void hideButtonTransfers();
        void fetchButtonStateSavings();
        void showButtonSavings();
        void hideButtonSavings();
        void fetchButtonStatePaymentCredits();
        void showButtonPaymentCredits();
        void hideButtonPaymentCredits();

        void showTransactionMenu();
        void hideTransactionMenu();
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showCircularProgressBar(String message);
        void hideCircularProgressBar();
        void showErrorTimeOut();
        void showDataFetchError(String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchButtonStateAdvanceSalary();
        void fetchButtonActionAdvanceSalary();
        void fetchButtonStateResorts();
        void fetchButtonStateTransfers();
        void fetchButtonStateSavings();
        void fetchButtonStatePaymentCredits();
        void fetchAssociatedDependency(BaseRequest baseRequest);
        void fetchPendingSalaryAdvance(BaseRequest baseRequest);
    }

    interface Model{
        void getButtonStateAdvanceSalary(final APIListener listener);
        void getButtonActionAdvanceSalary(final APIListener listener);
        void getButtonStateResorts(final APIListener listener);
        void getAssociatedDependency(BaseRequest baseRequest, final APIListener listener);
        void getPendingSalaryAdvance(BaseRequest baseRequest, final APIListener listener);
        void getButtonStateTransfers(final APIListener listener);
        void getButtonStateSavings(final APIListener listener);
        void getButtonStatePaymentCredits(final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessStateAdvanceSalary(Response<BaseResponse<T>> response);
        <T> void onErrorStateAdvanceSalary(Response<BaseResponse<T>> response);

        <T> void onSuccessActionAdvanceSalary(Response<BaseResponse<T>> response);
        <T> void onErrorActionAdvanceSalary(Response<BaseResponse<T>> response);

        <T> void onSuccessButtonStateResorts(Response<BaseResponse<T>> response);
        <T> void onErrorButtonStateResorts(Response<BaseResponse<T>> response);

        <T> void onSuccessAssociatedDependency(Response<BaseResponse<T>> response);
        <T> void onErrorAssociatedDependency(Response<BaseResponse<T>> response);

        <T> void onSuccessPendingSalaryAdvance(Response<BaseResponse<T>> response);
        <T> void onErrorPendingSalaryAdvance(Response<BaseResponse<T>> response);

        <T> void onSuccessButtonStateTransfers(Response<BaseResponse<T>> response);
        <T> void onErrorButtonStateTransfers(Response<BaseResponse<T>> response);

        <T> void onSuccessButtonStateSavings(Response<BaseResponse<T>> response);
        <T> void onErrorButtonStateSavings(Response<BaseResponse<T>> response);

        <T> void onSuccessButtonStatePaymentCredits(Response<BaseResponse<T>> response);
        <T> void onErrorButtonStatePaymentCredits(Response<BaseResponse<T>> response);

        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
