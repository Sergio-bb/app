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
        void showCircularProgressBarCardValue();
        void hideCircularProgressBarCardValue();
        void solicityReplacementCard();
        void showResultReplacementCard();
        void sendMessageCardReplacementSuccessful();
        void showSectionReplacemendCard();
        void hideSectionReplacemendCard();
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
        <T> void onSuccessAssociatedDependence(Response<BaseResponse<T>> response);
        <T> void onErrorAssociatedDependence(Response<BaseResponse<T>> response);
        void onFailureAssociatedDependence(Throwable t, boolean isErrorTimeOut);

        <T> void onSuccessReplacementCardValue(Response<BaseResponse<T>> response);
        <T> void onErrorReplacementCardValue(Response<BaseResponse<T>> response);
        void onFailureReplacementCardValue(Throwable t, boolean isErrorTimeOut);

        <T> void onSuccessSolicityReplacementCard(Response<BaseResponse<T>> response);
        <T> void onErrorSolicityReplacementCard(Response<BaseResponse<T>> response);
        void onFailureSolicityReplacementCard(Throwable t, boolean isErrorTimeOut);

        <T> void onExpiredToken(Response<BaseResponse<T>> response);
    }

}
