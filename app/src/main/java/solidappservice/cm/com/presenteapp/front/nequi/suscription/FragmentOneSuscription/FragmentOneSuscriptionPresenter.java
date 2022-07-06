package solidappservice.cm.com.presenteapp.front.nequi.suscription.FragmentOneSuscription;

import androidx.annotation.NonNull;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.DatosAsociado;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseConsultarDatosAsociado;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

public class FragmentOneSuscriptionPresenter implements FragmentOneSuscriptionContract.Presenter, FragmentOneSuscriptionContract.APIListener{

    FragmentOneSuscriptionView view;
    FragmentOneSuscriptionModel model;

    public FragmentOneSuscriptionPresenter(@NonNull FragmentOneSuscriptionView view, @NonNull FragmentOneSuscriptionModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchPersonalData(BaseRequest base) {
        view.hideSuscriptionPhone();
        view.disabledButtonNext();
        view.showCircularProgressBar();
        model.getPersonalData(base, this);
    }

    @Override
    public <T> void onSuccess(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        try{
            ResponseConsultarDatosAsociado datos = (ResponseConsultarDatosAsociado) response.body().getResultado();
            DatosAsociado datosAsociado = new DatosAsociado(datos.getNombreCompleto(),
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
            );
            view.showSuscriptionPhone();
            view.enabledButtonNext();
            view.showPersonalData(datosAsociado);
        }catch(Exception ex){
            view.showCircularProgressBar();
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
