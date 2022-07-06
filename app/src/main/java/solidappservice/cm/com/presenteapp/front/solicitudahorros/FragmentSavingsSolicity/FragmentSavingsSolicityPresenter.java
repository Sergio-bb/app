package solidappservice.cm.com.presenteapp.front.solicitudahorros.FragmentSavingsSolicity;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.solicitudahorros.response.ResponseTiposAhorro;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.solicitudahorros.request.RequestEnviarSolicitudAhorro;

/**
 * CREADO POR MIGUEL DAVID CABEZAS 16/09/2021.
 **/
public class FragmentSavingsSolicityPresenter implements FragmentSavingsSolicityContract.Presenter,
        FragmentSavingsSolicityContract.APIListener{

    FragmentSavingsSolicityView view;
    FragmentSavingsSolicityModel model;

    public FragmentSavingsSolicityPresenter(@NonNull FragmentSavingsSolicityView view, @NonNull FragmentSavingsSolicityModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchTypesOfSavings(BaseRequest baseRequest) {
        view.showProgressDialog("Actualizando información...");
        model.getTypesOfSavings(baseRequest, this);
    }

    @Override
    public void solicitySaving(RequestEnviarSolicitudAhorro request) {
        view.showProgressDialog("Enviando solicitud...");
        model.solicitySaving(request, this);
    }


    @Override
    public <T> void onSuccessTypesOfSavings(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            List<ResponseTiposAhorro>  tiposAhorro = (List<ResponseTiposAhorro>) response.body().getResultado();
                if(tiposAhorro != null && tiposAhorro.size() > 0){
                ResponseTiposAhorro t = new ResponseTiposAhorro();
                t.setN_tipodr("Selecciona tu ahorro");
                if (tiposAhorro.size() > 0 && !tiposAhorro.get(0).getN_tipodr().equals(t.getN_tipodr())) {
                    tiposAhorro.add(0, t);
                }
                view.showTypesOfSavings(tiposAhorro);
            }else{
                view.showDataFetchError("");
            }
        }catch(Exception ex){
            view.showDataFetchError("");
        }
    }

    @Override
    public <T> void onSuccessSolicitySaving(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            String resultSolicitySaving = (String) response.body().getResultado();
            view.showResultSolicitySaving(resultSolicitySaving);
        }catch(Exception ex){
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

}
