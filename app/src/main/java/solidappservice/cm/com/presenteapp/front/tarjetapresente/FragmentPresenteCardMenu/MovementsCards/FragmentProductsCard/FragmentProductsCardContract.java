package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.MovementsCards.FragmentProductsCard;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseTarjeta;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public interface FragmentProductsCardContract {

    interface View{
        void fetchPresenteCards();
        void showPresenteCards(List<ResponseTarjeta> tarjetas);
        void showSectionProductsCards();
        void hideSectionProductsCards();
        void showCircularProgressBar(String message);
        void hideCircularProgressBar();
        void showErrorWithRefresh();
        void showDialogError(String title, String message);
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
        <T> void onSuccessPresenteCards(Response<BaseResponse<T>> response);
        <T> void onErrorPresenteCards(Response<BaseResponse<T>> response);
        void onFailurePresenteCards(Throwable t, boolean isErrorTimeOut);

        <T> void onExpiredToken(Response<BaseResponse<T>> response);
    }

}
