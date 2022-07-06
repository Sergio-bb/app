package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityGeoreferencing.ActivityLocationsHms;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponseLocationsAgencies;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 28/08/2021.
 */
public class ActivityLocationsHmsPresenter implements ActivityLocationsHmsContract.Presenter,
        ActivityLocationsHmsContract.APIListener{

    ActivityLocationsHmsView view;
    ActivityLocationsHmsModel model;

    public ActivityLocationsHmsPresenter(@NonNull ActivityLocationsHmsView view, @NonNull ActivityLocationsHmsModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchLocationsAgencies() {
        view.hideSectionLocations();
        view.showCircularProgressBar("Obteniendo sucursales...");
        model.getLocationsAgencies(this);
    }

    @Override
    public <T> void onSuccess(Response<BaseResponse<T>> response) {
        try{
            List<ResponseLocationsAgencies> agencias = (List<ResponseLocationsAgencies>) response.body().getResultado();
            view.showFilters(agencias);
            view.showAgencies(agencias, view.getZoomDesiredByKilometros(7));
            view.showSectionLocations();
            view.hideCircularProgressBar();
        }catch (Exception ex){
            view.hideCircularProgressBar();
            view.showDataFetchError("Lo sentimos", "");
            view.showErrorWithRefresh();
        }
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        view.showExpiredToken(response.body().getErrorToken());
    }

    @Override
    public <T> void onError(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        if(response != null){
            view.showDataFetchError("Lo sentimos", response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
        view.showErrorWithRefresh();
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideCircularProgressBar();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
        view.showErrorWithRefresh();
    }

}
