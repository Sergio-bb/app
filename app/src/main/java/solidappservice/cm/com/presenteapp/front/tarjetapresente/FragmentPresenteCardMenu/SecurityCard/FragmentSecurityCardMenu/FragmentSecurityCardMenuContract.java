package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.SecurityCard.FragmentSecurityCardMenu;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseTarjeta;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public interface FragmentSecurityCardMenuContract {

    interface View{
        void fetchPresenteCards();
        void showPresenteCards(List<ResponseTarjeta> tarjetas);
        boolean validatePresenteCardsStatus(boolean isStatusBlock);
        void showSectionSecurityCardMenu();
        void hideSectionSecurityCardMenu();
        void showCircularProgressBar(String message);
        void hideCircularProgressBar();
        void showErrorWithRefresh();
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchPresenteCards(BaseRequest baseRequest);
    }

    interface Model{
        void getPresenteCards(BaseRequest body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
