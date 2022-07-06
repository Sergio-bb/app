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
        view.hideSectionSavings();
        view.showCircularProgressBar("Actualizando información...");
        model.getTypesOfSavings(baseRequest, this);
    }
    @Override
    public <T> void onSuccessTypesOfSavings(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        try{
            List<ResponseTiposAhorro>  tiposAhorro = (List<ResponseTiposAhorro>) response.body().getResultado();
            if(tiposAhorro != null && tiposAhorro.size() > 0){
                ResponseTiposAhorro t = new ResponseTiposAhorro();
                t.setN_tipodr("Selecciona tu ahorro");
                if (tiposAhorro.size() > 0 && !tiposAhorro.get(0).getN_tipodr().equals(t.getN_tipodr())) {
                    tiposAhorro.add(0, t);
                }
                view.showSectionSavings();
                view.showTypesOfSavings(tiposAhorro);
            }else{
                view.showDialogError("Lo sentimos", "");
                view.showErrorWithRefresh();
            }
        }catch(Exception ex){
            view.showDialogError("Lo sentimos", "");
            view.showErrorWithRefresh();
        }
    }
    @Override
    public <T> void onErrorTypesOfSaving(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        view.showDialogError("Lo sentimos","");
        view.showErrorWithRefresh();
    }
    @Override
    public void onFailureTypesOfSaving(Throwable t, boolean isErrorTimeOut) {
        view.hideCircularProgressBar();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDialogError("Lo sentimos", "");
        }
        view.showErrorWithRefresh();
    }


    @Override
    public void solicitySaving(RequestEnviarSolicitudAhorro request) {
        view.showProgressDialog("Enviando solicitud...");
        model.solicitySaving(request, this);
    }
    @Override
    public <T> void onSuccessSolicitySaving(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            String resultSolicitySaving = (String) response.body().getResultado();
            view.showResultSolicitySaving(resultSolicitySaving);
        }catch(Exception ex){
            view.showResultSolicitySaving(null);
        }
    }
    @Override
    public <T> void onErrorSolicitySaving(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        view.showResultSolicitySaving(null);
    }

    @Override
    public void onFailureSolicitySaving(Throwable t, boolean isErrorTimeOut) {
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showResultSolicitySaving(null);
        }
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        view.showExpiredToken(response.body().getErrorToken());
    }

}
