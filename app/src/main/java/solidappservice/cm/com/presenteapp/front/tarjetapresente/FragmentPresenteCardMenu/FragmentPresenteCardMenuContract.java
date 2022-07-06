package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseTarjeta;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public interface FragmentPresenteCardMenuContract {

    interface View{
        void fetchPresenteCards();
        void showNumberPresenteCards(List<ResponseTarjeta> tarjetas);

        void fetchButtonStateReplacementCard();
        void showButtonReplacementCard();
        void hideButtonReplacementCard();

        void showSectionMenuPresenteCards();
        void hideSectionMenuPresenteCards();
        void showCircularProgressBar(String message);
        void hideCircularProgressBar();
        void showErrorWithRefresh();
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchPresenteCards(BaseRequest baseRequest);
        void fetchButtonStateReplacementCard();
    }

    interface Model{
        void getPresenteCards(BaseRequest body, final APIListener listener);
        void getButtonStateReplacementCard(final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponse<T>> response);

        <T> void onSuccessButtonStateReplacementCard(Response<BaseResponse<T>> response);
        <T> void onErrorButtonStateReplacementCard(Response<BaseResponse<T>> response);

        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
