package solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentEditData;

import android.util.Log;

import androidx.annotation.NonNull;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.DatosAsociado;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseConsultarDatosAsociado;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseFormatoDirecciones;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseUbicaciones;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 11/07/2021.
 */
public class FragmentEditDataPresenter implements FragmentEditDataContract.Presenter, FragmentEditDataContract.APIListener{

    FragmentEditDataView view;
    FragmentEditDataModel model;

    public FragmentEditDataPresenter(@NonNull FragmentEditDataView view, @NonNull FragmentEditDataModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchPersonalData(BaseRequest base) {
        view.hideContentEditData();
        view.showCircularProgressBar("Validando datos...");
        model.getPersonalData(base, this);
    }
    @Override
    public <T> void onSuccessPersonalData(Response<BaseResponse<T>> response) {
        try{
            ResponseConsultarDatosAsociado datos = (ResponseConsultarDatosAsociado) response.body().getResultado();
            view.showPersonalData(new DatosAsociado(
                    datos.getNombreCompleto(),
                    datos.getDireccion(),
                    datos.getCelular(),
                    datos.getEmail(),
                    datos.getBarrio(),
                    datos.getIdCiudad(),
                    datos.getNombreCiudad(),
                    datos.getIdDepartamento(),
                    datos.getNombreDepartamento(),
                    datos.getIdPais(),
                    datos.getNombrePais()
            ));
        } catch(Exception ex){
            view.hideCircularProgressBar();
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void fetchLocations() {
        view.hideContentEditData();
        view.showCircularProgressBar("Un momento...");
        model.getLocations(this);
    }
    @Override
    public <T> void onSuccessLocations(Response<BaseResponse<T>> response) {
        try{
            view.showLocations((ResponseUbicaciones) response.body().getResultado());
        } catch(Exception ex){
            view.hideCircularProgressBar();
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void fetchAddressFormat() {
        view.hideContentEditData();
        view.showCircularProgressBar("Un momento...");
        model.getAddressFormat(this);
    }
    @Override
    public <T> void onSuccessAddressFormat(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        try{
            view.showAddressFormat((ResponseFormatoDirecciones) response.body().getResultado());
            view.showContentEditData();
        } catch(Exception ex){
            view.showDataFetchError("Lo sentimos", "");
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
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideCircularProgressBar();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }
}

