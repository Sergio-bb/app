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

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 24/08/2021.
 */
public interface FragmentSolicityContract {

    interface View{
        void fetchMoves();
        void showMoves(List<ResponseMovimientos> movimientos);
        boolean validateRequestedAmount();
        void showErrorRequestedAmount(String messageError);
        void showDialogTips(List<ResponseTips> tips);
        void validateRequirements();
        void fetchDebtCapacity(ResponseValidarRequisitos requisitos);
        void fetchReasonsNotMeetsRequirements(ResponseValidarRequisitos requisitos);
        void enterLogs(String accion, String descripcion);
        void showDebtCapacity(ResponseTopes topes);
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showDataFetchError(String title, String message);
        void showErrorTimeOut();
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchMoves(BaseRequest base);
        boolean validateRequestedAmount(int valorSolicitado, String valorMaximo, int valorCupo, int valorMax, int valorMin);
        void fetchTips();
        void validateRequirements(BaseRequest base);
        void fetchDebtCapacity(BaseRequest base);
        void fetchReasonsNotMeetsRequirements(RequestNoCumple request);
        void enterLogs(RequestLogs request);
    }

    interface Model{
        void getValidateRequirements(BaseRequest body, final APIListener listener);
        void getDebtCapacity(BaseRequest body, final APIListener listener);
        void getReasonsNotMeetsRequirements(RequestNoCumple body, final APIListener listener);
        void getTips(final APIListener listener);
        void sendLogs(RequestLogs body, final APIListener listener);
        void getMoves(BaseRequest body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
