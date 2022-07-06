package solidappservice.cm.com.presenteapp.front.adelantonomina.FragmentDetail;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestInsertarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestLogs;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestProcesarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 24/08/2021.
 */
public interface FragmentDetailContract {

    interface View{
        void fetchCommissionValue();
        void showCommissionValue(String valorComision);
        void showCircularProgressBarValue();
        void hideCircularProgressBarValue();
        void fetchRegisterSalaryAdvance();
        void fetchProcessSalaryAdvance(String numeroTransaccion);
        void enterLogs(String accion, String descripcion);
        void showSuccessfulSalaryAdvance();
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchCommissionValue();
        void fetchRegisterSalaryAdvance(RequestInsertarAdelantoNomina request);
        void fetchProcessSalaryAdvance(RequestProcesarAdelantoNomina request);
        void enterLogs(RequestLogs request);
    }

    interface Model{
        void getCommissionValue(final APIListener listener);
        void registerSalaryAdvance(RequestInsertarAdelantoNomina body, final APIListener listener);
        void processSalaryAdvance(RequestProcesarAdelantoNomina body, final APIListener listener);
        void sendLogs(RequestLogs body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessCommissionValue(Response<BaseResponse<T>> response);
        <T> void onSuccessRegisterSalaryAdvance(Response<BaseResponse<T>> response);
        <T> void onSuccessProcessSalaryAdvance(Response<BaseResponse<T>> response);

        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
