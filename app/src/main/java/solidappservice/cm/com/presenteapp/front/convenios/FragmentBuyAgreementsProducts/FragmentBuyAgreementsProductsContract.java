package solidappservice.cm.com.presenteapp.front.convenios.FragmentBuyAgreementsProducts;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseConvenios;
import solidappservice.cm.com.presenteapp.entities.convenios.request.RequestSolicitudProducto;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public interface FragmentBuyAgreementsProductsContract {

    interface View{
        void showProduct();
        void configureLegal(int positionOfFormaPago);
        void enableButtonComprar(boolean enable);
        void startBuyingProduct();
        boolean validateData();
        void buyProduct();
        void showResultBuyProduct(String resultHTML);
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void buyProduct(BaseRequest baseRequest, RequestSolicitudProducto solicitudProducto);
    }

    interface Model{
        void buyProduct(BaseRequest baseRequest, RequestSolicitudProducto solicitudProducto, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponseConvenios<T>> response);
        <T> void onExpiredToken(Response<BaseResponseConvenios<T>> response);
        <T> void onError(Response<BaseResponseConvenios<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
