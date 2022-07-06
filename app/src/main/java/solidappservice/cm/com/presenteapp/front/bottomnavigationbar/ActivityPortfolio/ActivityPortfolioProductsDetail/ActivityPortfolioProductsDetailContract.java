package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityPortfolio.ActivityPortfolioProductsDetail;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponsePortafolio;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 28/08/2021.
 */
public interface ActivityPortfolioProductsDetailContract {

    interface View{
        void showPortfolioProductsDetails(ResponsePortafolio producto);
    }

    interface Presenter{
    }

    interface Model{
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t);
    }

}
