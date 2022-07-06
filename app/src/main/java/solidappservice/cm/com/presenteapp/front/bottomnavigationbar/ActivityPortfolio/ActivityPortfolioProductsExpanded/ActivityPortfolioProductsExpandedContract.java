package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityPortfolio.ActivityPortfolioProductsExpanded;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.dto.PortafolioPadre;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 28/08/2021.
 */
public interface ActivityPortfolioProductsExpandedContract {

    interface View{
        void showPortfolioProductsExpanded(PortafolioPadre portafolioPadre);
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
