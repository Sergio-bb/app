package solidappservice.cm.com.presenteapp.front.menufinanzas.FragmentFinanceMenu;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.solicitudahorros.request.RequestEnviarSolicitudAhorro;
import solidappservice.cm.com.presenteapp.entities.solicitudahorros.response.ResponseTiposAhorro;
import solidappservice.cm.com.presenteapp.front.solicitudahorros.FragmentSavingsSolicity.FragmentSavingsSolicityView;

/**
 * CREADO POR MIGUEL DAVID CABEZAS 17/09/2021.
 **/
public class FragmentFinanceMenuPresenter implements FragmentFinanceMenuContract.Presenter,
        FragmentFinanceMenuContract.APIListener{

    FragmentFinanceMenuView view;

    public FragmentFinanceMenuPresenter(@NonNull FragmentFinanceMenuView view) {
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
