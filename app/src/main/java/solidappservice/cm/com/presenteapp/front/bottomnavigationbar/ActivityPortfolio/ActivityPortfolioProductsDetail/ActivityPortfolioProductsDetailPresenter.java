package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityPortfolio.ActivityPortfolioProductsDetail;

import androidx.annotation.NonNull;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 28/08/2021.
 */
public class ActivityPortfolioProductsDetailPresenter implements ActivityPortfolioProductsDetailContract.Presenter,
        ActivityPortfolioProductsDetailContract.APIListener{

    ActivityPortfolioProductsDetailView view;

    public ActivityPortfolioProductsDetailPresenter(@NonNull ActivityPortfolioProductsDetailView view) {
        this.view = view;
    }


    @Override
    public <T> void onSuccess(Response<BaseResponse<T>> response) {
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
    }

    @Override
    public <T> void onError(Response<BaseResponse<T>> response) {
    }

    @Override
    public void onFailure(Throwable t) {
    }

}
