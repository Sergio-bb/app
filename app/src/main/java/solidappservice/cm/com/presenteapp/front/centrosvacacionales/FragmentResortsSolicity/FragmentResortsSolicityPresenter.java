package solidappservice.cm.com.presenteapp.front.centrosvacacionales.FragmentResortsSolicity;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.centrosvacacionales.request.RequestSolicitarCentroVacacional;
import solidappservice.cm.com.presenteapp.entities.centrosvacacionales.response.ResponseCentroVacacional;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 16/06/2021.
 */
public class FragmentResortsSolicityPresenter implements FragmentResortsSolicityContract.Presenter,
        FragmentResortsSolicityContract.APIListener{

    FragmentResortsSolicityView view;
    FragmentResortsSolicityModel model;

    public FragmentResortsSolicityPresenter(@NonNull FragmentResortsSolicityView view, @NonNull FragmentResortsSolicityModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchResorts(BaseRequest baseRequest) {
        view.showProgressDialog("Obteniendo centros vacacionales...");
        model.getResorts(baseRequest, this);
    }

    @Override
    public void solicityResort(RequestSolicitarCentroVacacional baseRequest) {
        view.showProgressDialog("Enviando solicitud..");
        model.solicityResort(baseRequest, this);
    }

    @Override
    public <T> void onSuccessResorts(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            List<ResponseCentroVacacional> resorts = (List<ResponseCentroVacacional>) response.body().getResultado();
            if(resorts != null && resorts.size()>0){
                view.showResorts(resorts);
            }else{
                view.showDataFetchError("Lo sentimos", "");
            }
        }catch (Exception ex){
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public <T> void onSuccessSolicityResorts(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            String result = (String) response.body().getResultado();
            view.showResultSolicityResort(result);
        }catch (Exception ex){
            view.showDataFetchError("Lo sentimos", "");
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
            view.showDataFetchError("Lo sentimos", response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }

    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideProgressDialog();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }

}
