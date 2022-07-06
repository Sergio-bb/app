package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityGeoreferencing.ActivityLocationsGms;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponseLocationsAgencies;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 28/08/2021.
 */
public interface ActivityLocationsGmsContract {

    interface View{
        void fetchLocationsAgencies();
        void showFilters(List<ResponseLocationsAgencies> agencias);
        void validatePermissionsLocation();
        void showDialogPermissions(int requestCode);
        void requestLocationPermits();
        void showMap();
        void showAgencies(List<ResponseLocationsAgencies> listado, float zoomCamera);
        void showMarker(ResponseLocationsAgencies agencia);
        void zoomCameraGoogle(float zoomCamera, LatLng actual);
        void toggleNetworkUpdates();
        void toggleGPSUpdates();
        void toggleBestUpdates();

        void drawPoints();
        int getIdDrawableByAgency(ResponseLocationsAgencies agencia);
        String getNameAgencyType(String idAgency);
        float getZoomDesiredByKilometros(int desiredKm);

        void showSectionLocations();
        void hideSectionLocations();
        void showCircularProgressBar(String message);
        void hideCircularProgressBar();
        void showErrorWithRefresh();
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchLocationsAgencies();
    }

    interface Model{
        void getLocationsAgencies(final ActivityLocationsGmsContract.APIListener listener);
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
