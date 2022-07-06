package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.SecurityCard.FragmentBlockCard;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.request.RequestBloquearTarjeta;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseTarjeta;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public interface FragmentBlockCardContract {

    interface View{
        void showPresenteCards(List<ResponseTarjeta> tarjetas);
        void fetchReasonsBlockCard();
        void showReasonsBlockCard(List<String> motivos);
        void confirmBlockCard(ResponseTarjeta tarjeta);
        void blockCard(ResponseTarjeta tarjeta, String motivoBloqueo);
        void showResultBlockCard(String result);
        void updateStatePresenteCard(ResponseTarjeta card, boolean isBlock);
        void showSectionBlockCard();
        void hideSectionBlockCard();
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
        void fetchReasonsBlockCard(BaseRequest baseRequest);
        void blockCard(RequestBloquearTarjeta request);
    }

    interface Model{
        void getReasonsBlockCard(BaseRequest body, final APIListener listener);
        void blockCard(RequestBloquearTarjeta body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessReasonsBlockCard(Response<BaseResponse<T>> response);
        <T> void onErrorReasonsBlockCard(Response<BaseResponse<T>> response);
        void onFailureReasonsBlockCard(Throwable t, boolean isErrorTimeOut);

        <T> void onSuccessBlockCard(Response<BaseResponse<T>> response);
        <T> void onErrorBlockCard(Response<BaseResponse<T>> response);
        void onFailureBlockCard(Throwable t, boolean isErrorTimeOut);

        <T> void onExpiredToken(Response<BaseResponse<T>> response);
    }

}
