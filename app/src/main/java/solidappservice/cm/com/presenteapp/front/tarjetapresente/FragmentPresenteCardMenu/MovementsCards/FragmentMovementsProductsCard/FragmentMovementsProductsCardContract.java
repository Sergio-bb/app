package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.MovementsCards.FragmentMovementsProductsCard;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseMovimientoProducto;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.request.RequestMovimientosProducto;
import solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.MovementsCards.FragmentProductsCard.FragmentProductsCardContract;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public interface FragmentMovementsProductsCardContract {

    interface View{
        void fetchAccounts();
        void showAccountsPresenteCard(List<ResponseProducto> productos);
        void fetchMovementsPresenteCards(ResponseProducto producto);
        void showMovementsPresenteCards(List<ResponseMovimientoProducto> movimientos);
        void showTitleProductPresenteCards();
        void showSectionMovementsPresenteCards();
        void hideSectionMovementsPresenteCards();
        void showCircularProgressBar(String message);
        void hideCircularProgressBar();
        void showErrorWithRefresh();
        void showDialogError(String title, String message);
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchMovementsPresenteCards(RequestMovimientosProducto request);
        void fetchAccounts(BaseRequest baseRequest);
    }

    interface Model{
        void getMovementsPresenteCards(RequestMovimientosProducto body, final APIListener listener);
        void getAccounts(BaseRequest body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponse<T>> response);

        <T> void onSuccessAccounts(Response<BaseResponse<T>> response);
        <T> void onErrorAccounts(Response<BaseResponse<T>> response);
        void onFailureAccounts(Throwable t, boolean isErrorTimeOut);

        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
