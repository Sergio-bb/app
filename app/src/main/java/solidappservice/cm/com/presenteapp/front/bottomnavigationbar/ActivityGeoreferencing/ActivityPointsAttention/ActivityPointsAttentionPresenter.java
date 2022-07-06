package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityGeoreferencing.ActivityPointsAttention;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponseLocationsAgencies;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 28/08/2021.
 */
public class ActivityPointsAttentionPresenter implements ActivityPointsAttentionContract.Presenter,
        ActivityPointsAttentionContract.APIListener{

    ActivityPointsAttentionView view;

    public ActivityPointsAttentionPresenter(@NonNull ActivityPointsAttentionView view) {
        this.view = view;
    }

    @Override
    public void getPointAttention(String nombreAgencia, List<ResponseLocationsAgencies> agencias) {
        String[] datos = nombreAgencia.split("\\|");
        if (agencias != null && agencias.size() > 0) {
            for (ResponseLocationsAgencies a : agencias) {
                if (a.getN_sucurs().equals(datos[0]) && a.getN_latitu().equals(datos[1]) && a.getN_longit().equals(datos[2])) {
                    view.showPointAttention(datos[0], a);
                    break;
                }
            }
        }
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
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
    }

}
