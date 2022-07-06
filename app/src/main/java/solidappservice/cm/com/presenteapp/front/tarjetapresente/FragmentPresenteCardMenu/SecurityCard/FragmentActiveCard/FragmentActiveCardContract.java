package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.SecurityCard.FragmentActiveCard;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.request.RequestActivarTarjeta;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseTarjeta;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public interface FragmentActiveCardContract {

    interface View{
        void showPresenteCards(List<ResponseTarjeta> tarjetas);
        void confirmActivateCard(ResponseTarjeta tarjeta);
        void activateCard(ResponseTarjeta tarjeta);
        void showResultActivateCard(String result);
        void updateStatePresenteCard(ResponseTarjeta card, boolean isBlock);
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showDialogError(String title, String message);
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void activateCard(RequestActivarTarjeta request);
    }

    interface Model{
        void activateCard(RequestActivarTarjeta request, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
