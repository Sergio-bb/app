package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.MovementsCards.FragmentProductsCard;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProductos;
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
        void fetchAccounts();
        void showAccountsPresenteCard(List<ResponseProductos> productos);
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showDataFetchError(String message);
        void showErrorTimeOut();
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchPresenteCards(BaseRequest baseRequest);
        void fetchAccounts(BaseRequest baseRequest);
    }

    interface Model{
        void getPresenteCards(BaseRequest body, final APIListener listener);
        void getAccounts(BaseRequest body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
