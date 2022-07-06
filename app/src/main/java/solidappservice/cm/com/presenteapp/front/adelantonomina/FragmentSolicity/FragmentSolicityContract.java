package solidappservice.cm.com.presenteapp.front.adelantonomina.FragmentSolicity;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestLogs;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestNoCumple;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseMovimientos;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseTips;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseTopes;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseValidarRequisitos;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.front.menutransacciones.FragmentTransactionsMenu.FragmentTransactionsMenuContract;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 24/08/2021.
 */
public interface FragmentSolicityContract {

    interface View{
        void showContentSalaryAdvance();
        void hideContentSalaryAdvance();

        void fetchPendingSalaryAdvance();
        void showResultPendingSalaryAdvance(List<ResponseMovimientos> listPendingMovements);
        void validateRequirements();
        void fetchReasonsNotMeetsRequirements(ResponseValidarRequisitos requisitos);

        void fetchDebtCapacity(ResponseValidarRequisitos requisitos);
        void showCircularProgressBarDebtCapacity();
        void hideCircularProgressBarDebtCapacity();

        void fetchMoves();
        void showMoves(List<ResponseMovimientos> movimientos);
        void showCircularProgressBarMovements(String message);
        void hideCircularProgressBarMovements();
        void showErrorWithRefreshMovements();

        boolean validateRequestedAmount();
        void showErrorRequestedAmount(String messageError);
        void showDialogTips(List<ResponseTips> tips);
        void enterLogs(String accion, String descripcion);
        void showDebtCapacity(ResponseTopes topes);

        void showCircularProgressBar(String message);
        void hideCircularProgressBar();
        void showErrorWithRefresh();
        void showDialogError(String title, String message);
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);

    }

    interface Presenter{
        void fetchPendingSalaryAdvance(BaseRequest baseRequest);
        void fetchMoves(BaseRequest base);
        void validateRequirements(BaseRequest base);
        boolean validateRequestedAmount(int valorSolicitado, String valorMaximo, int valorCupo, int valorMax, int valorMin);
        void fetchTips();
        void fetchDebtCapacity(BaseRequest base);
        void fetchReasonsNotMeetsRequirements(RequestNoCumple request);
        void enterLogs(RequestLogs request);
    }

    interface Model{
        void getPendingSalaryAdvance(BaseRequest baseRequest, final APIListener listener);
        void getValidateRequirements(BaseRequest body, final APIListener listener);
        void getReasonsNotMeetsRequirements(RequestNoCumple body, final APIListener listener);

        void getDebtCapacity(BaseRequest body, final APIListener listener);
        void getTips(final APIListener listener);
        void sendLogs(RequestLogs body, final APIListener listener);
        void getMoves(BaseRequest body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessPendingSalaryAdvance(Response<BaseResponse<T>> response);
        <T> void onErrorPendingSalaryAdvance(Response<BaseResponse<T>> response);
        void onFailurePendingSalaryAdvance(Throwable t, boolean isErrorTimeOut);

        <T> void onSuccessValidateRequirements(Response<BaseResponse<T>> response);
        <T> void onErrorValidateRequirements(Response<BaseResponse<T>> response);

        <T> void onSuccessReasonsNotMeetsRequirements(Response<BaseResponse<T>> response);
        <T> void onErrorReasonsNotMeetsRequirements(Response<BaseResponse<T>> response);
        void onFailureReasonsNotMeetsRequirements(Throwable t, boolean isErrorTimeOut);

        <T> void onSuccessDebtCapacity(Response<BaseResponse<T>> response);

        <T> void onSuccessMoves(Response<BaseResponse<T>> response);
        <T> void onErrorMoves(Response<BaseResponse<T>> response);
        void onFailureMoves(Throwable t, boolean isErrorTimeOut);

        <T> void onSuccessTips(Response<BaseResponse<T>> response);
        <T> void onErrorTips(Response<BaseResponse<T>> response);
        void onFailureTips(Throwable t, boolean isErrorTimeOut);

        void onFailure(Throwable t, boolean isErrorTimeOut);
        <T> void onError(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
    }

}
