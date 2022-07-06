package solidappservice.cm.com.presenteapp.front.convenios.FragmentListAgrementsProducts;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseConvenios;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Producto;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public interface FragmentListAgrementsProductsContract {

    interface View{;
        void showAgreement();
        void fetchProductsByAgreements(String idAgreement);
        void showProductsByAgreements(List<Producto> productos);
        void showContentProductsByAgreements();
        void hideContentProductsByAgreements();
        void showCircularProgressBar(String message);
        void hideCircularProgressBar();
        void showErrorWithRefresh();
        void showDialogError(String title, String message);
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchProductsByAgreements(BaseRequest baseRequest, String idAgreement);
    }

    interface Model{
        void getProductsByAgreements(BaseRequest body, String idAgreement, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponseConvenios<T>> response);
        <T> void onExpiredToken(Response<BaseResponseConvenios<T>> response);
        <T> void onError(Response<BaseResponseConvenios<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
