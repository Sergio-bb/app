package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.ReplacementCard.FragmentReplacementCardDetail;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.request.RequestMensajeReposicionTarjeta;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.request.RequestReposicionTarjeta;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseDependenciasAsociado;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public interface FragmentReplacementCardDetailContract {

    interface View{
        void fetchAssociatedDependence();
        void showAssociatedDependence(ResponseDependenciasAsociado dependencia);
        void showDialogConfirmReplacementCard();
        void showReplacementCardValue(int valueReplacementeCard);
        void solicityReplacementCard();
        void showResultReplacementCard();
        void sendMessageCardReplacementSuccessful();
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showDataFetchError(String message);
        void showErrorTimeOut();
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchAssociatedDependence(BaseRequest baseRequest);
        void fechReplacementCardValue();
        void solicityReplacementCard(RequestReposicionTarjeta request);
        void sendMessageCardReplacementSuccessful(RequestMensajeReposicionTarjeta body);
    }

    interface Model{
        void getAssociatedDependence(BaseRequest body, final APIListener listener);
        void getReplacementCardValue(final APIListener listener);
        void solicityReplacementCard(RequestReposicionTarjeta body, final APIListener listener);
        void sendMessageCardReplacementSuccessful(RequestMensajeReposicionTarjeta body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
