package solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs;

import androidx.annotation.NonNull;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 19/09/2021.
 */
public class ActivityTabsPresenter implements ActivityTabsContract.Presenter,
        ActivityTabsContract.APIListener{

    ActivityTabsView view;

    public ActivityTabsPresenter(@NonNull ActivityTabsView view) {
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
