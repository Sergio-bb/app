package solidappservice.cm.com.presenteapp.front.tranferencias.FragmentTransfersMenu;

import androidx.annotation.NonNull;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 11/09/2021.
 */
public class FragmentTransfersMenuPresenter implements FragmentTransfersMenuContract.Presenter,
        FragmentTransfersMenuContract.APIListener{

    FragmentTransfersMenuView view;

    public FragmentTransfersMenuPresenter(@NonNull FragmentTransfersMenuView view) {
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
