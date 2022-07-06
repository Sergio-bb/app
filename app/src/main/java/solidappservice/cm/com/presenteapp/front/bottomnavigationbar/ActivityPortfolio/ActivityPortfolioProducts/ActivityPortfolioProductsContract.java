package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityPortfolio.ActivityPortfolioProducts;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.dto.PortafolioPadre;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponsePortafolio;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 28/08/2021.
 */
public interface ActivityPortfolioProductsContract {

    interface View{
        void showPortfolioProducts(List<ResponsePortafolio> productosPortafolio, List<PortafolioPadre> portafolioPadres);
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showDataFetchError(String message);
        void showErrorTimeOut();
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchPortfolioProducts();
    }

    interface Model{
        void getPortfolioProducts(final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
