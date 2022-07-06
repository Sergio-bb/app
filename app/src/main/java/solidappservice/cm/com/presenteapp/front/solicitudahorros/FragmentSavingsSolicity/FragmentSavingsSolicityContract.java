package solidappservice.cm.com.presenteapp.front.solicitudahorros.FragmentSavingsSolicity;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.solicitudahorros.response.ResponseTiposAhorro;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.solicitudahorros.request.RequestEnviarSolicitudAhorro;

/**
 * CREADO POR MIGUEL DAVID CABEZAS 16/09/2021.
 **/
public interface FragmentSavingsSolicityContract {

    interface View{
        void fetchTypesOfSavings();
        void showTypesOfSavings(List<ResponseTiposAhorro> tiposAhorro);
        void showOptionsTypesofSavings(int positionTypeofSavings);
        void cleanFields();
        void confirmSolicitySaving();
        boolean validateData();
        void solicitySaving();
        void showResultSolicitySaving(String resultSolicitySaving);
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showDataFetchError(String message);
        void showErrorTimeOut();
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchTypesOfSavings(BaseRequest baseRequest);
        void solicitySaving(RequestEnviarSolicitudAhorro request);
    }

    interface Model{
        void getTypesOfSavings(BaseRequest body, final APIListener listener);
        void solicitySaving(RequestEnviarSolicitudAhorro body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessTypesOfSavings(Response<BaseResponse<T>> response);
        <T> void onSuccessSolicitySaving(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
