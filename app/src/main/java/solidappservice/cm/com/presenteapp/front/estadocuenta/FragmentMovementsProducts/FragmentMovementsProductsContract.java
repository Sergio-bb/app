package solidappservice.cm.com.presenteapp.front.estadocuenta.FragmentMovementsProducts;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.request.RequestMovimientosProducto;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseMovimientoProducto;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProductos;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public interface FragmentMovementsProductsContract {

    interface View{
        void fetchMovementsProducts(ResponseProductos producto);
        void showMovementsProducts(List<ResponseMovimientoProducto> movimientos);
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showDataFetchError(String message);
        void showErrorTimeOut();
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchMovementsProducts(RequestMovimientosProducto request);
    }

    interface Model{
        void getMovementsProducts(RequestMovimientosProducto body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
