package solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentValidateData;

import android.util.Log;

import androidx.annotation.NonNull;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.DatosAsociado;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.RequestActualizarDatos;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseConsultarDatosAsociado;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 11/07/2021.
 */
public class FragmentValidateDataPresenter implements FragmentValidateDataContract.Presenter, FragmentValidateDataContract.APIListener{

    FragmentValidateDataView view;
    FragmentValidateDataModel model;

    public FragmentValidateDataPresenter(@NonNull FragmentValidateDataView view, @NonNull FragmentValidateDataModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchPersonalData(BaseRequest base) {
        view.showProgressDialog("Un momento...");
        model.getPersonalData(base, this);
    }

    @Override
    public void updatePersonalData(RequestActualizarDatos datosAsociado) {
        view.showProgressDialog("Actualizando datos...");
        model.updatePersonalData(datosAsociado,this);
    }

    @Override
    public <T> void onSuccessPersonalData(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try {
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
            datosAsociado.setDetalleDireccion(new DatosAsociado.Direccion());
            String[] detalleDireccion = datos.getDireccion().replace("# ", "").split(" ");
            if (detalleDireccion.length > 0) {
                datosAsociado.getDetalleDireccion().setTipoVia(detalleDireccion[0]);
                datosAsociado.getDetalleDireccion().setNumeroVia1(detalleDireccion[1]);
                if (tryParseInt(detalleDireccion[2])) {
                    datosAsociado.getDetalleDireccion().setNumeroVia2(detalleDireccion[2]);
                } else {
                    if (detalleDireccion[2].length() <= 2) {
                        datosAsociado.getDetalleDireccion().setLetraVia1(detalleDireccion[2]);
                        if (tryParseInt(detalleDireccion[3])) {
                            datosAsociado.getDetalleDireccion().setNumeroVia2(detalleDireccion[3]);
                        } else {
                            datosAsociado.getDetalleDireccion().setComplementoVia1(detalleDireccion[3]);
                            datosAsociado.getDetalleDireccion().setNumeroVia2(detalleDireccion[4]);
                            if (tryParseInt(detalleDireccion[5])) {
                                datosAsociado.getDetalleDireccion().setNumeroComplemento(detalleDireccion[5]);
                            } else {
                                if (detalleDireccion[5].length() <= 2) {
                                    datosAsociado.getDetalleDireccion().setLetraVia2(detalleDireccion[5]);
                                    if (tryParseInt(detalleDireccion[6])) {
                                        datosAsociado.getDetalleDireccion().setNumeroComplemento(detalleDireccion[6]);
                                    } else {
                                        datosAsociado.getDetalleDireccion().setComplementoVia2(detalleDireccion[6]);
                                        datosAsociado.getDetalleDireccion().setNumeroComplemento(detalleDireccion[7]);
                                    }
                                } else {
                                    datosAsociado.getDetalleDireccion().setComplementoVia2(detalleDireccion[5]);
                                    datosAsociado.getDetalleDireccion().setNumeroComplemento(detalleDireccion[6]);
                                }
                            }
                        }
                    } else {
                        datosAsociado.getDetalleDireccion().setComplementoVia1(detalleDireccion[2]);
                        datosAsociado.getDetalleDireccion().setNumeroVia2(detalleDireccion[3]);
                        if (tryParseInt(detalleDireccion[4])) {
                            datosAsociado.getDetalleDireccion().setNumeroComplemento(detalleDireccion[4]);
                        } else {
                            if (detalleDireccion[4].length() <= 2) {
                                datosAsociado.getDetalleDireccion().setLetraVia2(detalleDireccion[4]);
                                if (tryParseInt(detalleDireccion[5])) {
                                    datosAsociado.getDetalleDireccion().setNumeroComplemento(detalleDireccion[5]);
                                } else {
                                    datosAsociado.getDetalleDireccion().setComplementoVia2(detalleDireccion[5]);
                                    datosAsociado.getDetalleDireccion().setNumeroComplemento(detalleDireccion[6]);
                                }
                            } else {
                                datosAsociado.getDetalleDireccion().setComplementoVia2(detalleDireccion[4]);
                                datosAsociado.getDetalleDireccion().setNumeroComplemento(detalleDireccion[5]);
                            }
                        }
                    }
                }
            }
            view.showPersonalData(datosAsociado);
        }catch(Exception ex){
            Log.d("Error",ex.getMessage());
            view.showDataFetchError("");
        }
    }

    @Override
    public <T> void onSuccessUpdatePersonalData(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try {
            view.resultUpdatePersonalData();
        }catch(Exception ex){
            Log.d("Error",ex.getMessage());
            view.showDataFetchError("");
        }
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        view.showExpiredToken(response.body().getErrorToken());
    }

    @Override
    public <T> void onError(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        if(response != null){
            view.showDataFetchError(response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("");
        }
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideProgressDialog();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("");
        }
    }

    public boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
