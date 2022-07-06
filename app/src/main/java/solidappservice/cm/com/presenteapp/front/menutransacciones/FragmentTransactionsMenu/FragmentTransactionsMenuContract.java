package solidappservice.cm.com.presenteapp.front.menutransacciones.FragmentTransactionsMenu;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 17/09/2021.
 */
public interface FragmentTransactionsMenuContract {

    interface View{
        void fetchButtonStateAdvanceSalary();
        void showButtonAdvanceSalary();
        void hideButtonAdvanceSalary();
        void fetchButtonActionAdvanceSalary();
        void changeButtonActionAdvanceSalary(List<String> listDependencies);
        void fetchAssociatedDependency();
        void showResultAssociatedDependency(String associatedDependency);
        void validateSalaryAdvanceStatus();

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
        void showCircularProgressBar(String message);
        void hideCircularProgressBar();
        void showErrorWithRefresh();
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchButtonStateAdvanceSalary();
        void fetchButtonActionAdvanceSalary();
        void fetchAssociatedDependency(BaseRequest baseRequest);

        void fetchButtonStateTransfers();
        void fetchButtonStateSavings();
        void fetchButtonStatePaymentCredits();
    }

    interface Model{
        void getButtonStateAdvanceSalary(final APIListener listener);
        void getButtonActionAdvanceSalary(final APIListener listener);
        void getAssociatedDependency(BaseRequest baseRequest, final APIListener listener);

        void getButtonStateTransfers(final APIListener listener);
        void getButtonStateSavings(final APIListener listener);
        void getButtonStatePaymentCredits(final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessStateAdvanceSalary(Response<BaseResponse<T>> response);
        <T> void onErrorStateAdvanceSalary(Response<BaseResponse<T>> response);
        void onFailureStateAdvanceSalary(Throwable t, boolean isErrorTimeOut);

        <T> void onSuccessActionAdvanceSalary(Response<BaseResponse<T>> response);
        <T> void onErrorActionAdvanceSalary(Response<BaseResponse<T>> response);
        void onFailureActionAdvanceSalary(Throwable t, boolean isErrorTimeOut);

        <T> void onSuccessAssociatedDependency(Response<BaseResponse<T>> response);
        <T> void onErrorAssociatedDependency(Response<BaseResponse<T>> response);
        void onFailureAssociatedDependency(Throwable t, boolean isErrorTimeOut);

        <T> void onSuccessButtonStateTransfers(Response<BaseResponse<T>> response);
        <T> void onErrorButtonStateTransfers(Response<BaseResponse<T>> response);

        <T> void onSuccessButtonStateSavings(Response<BaseResponse<T>> response);
        <T> void onErrorButtonStateSavings(Response<BaseResponse<T>> response);

        <T> void onSuccessButtonStatePaymentCredits(Response<BaseResponse<T>> response);
        <T> void onErrorButtonStatePaymentCredits(Response<BaseResponse<T>> response);

        <T> void onExpiredToken(Response<BaseResponse<T>> response);
    }

}
