package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.MovementsCards.FragmentMovementsProductsCard;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseMovimientoProducto;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProductos;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.request.RequestMovimientosProducto;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public interface FragmentMovementsProductsCardContract {

    interface View{
        void fetchMovementsPresenteCards(ResponseProductos producto);
        void showMovementsPresenteCards(List<ResponseMovimientoProducto> movimientos);
        void showTitleProductPresenteCards();
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showDataFetchError(String message);
        void showErrorTimeOut();
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchMovementsPresenteCards(RequestMovimientosProducto request);
    }

    interface Model{
        void getMovementsPresenteCards(RequestMovimientosProducto body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
