package solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements;

import androidx.annotation.NonNull;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseConvenios;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Resumen;
import solidappservice.cm.com.presenteapp.entities.convenios.response.ResponseResumen;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public class ActivityAgreementsPresenter implements ActivityAgreementsContract.Presenter,
        ActivityAgreementsContract.APIListener{

    ActivityAgreementsView view;
    ActivityAgreementsModel model;

    public ActivityAgreementsPresenter(@NonNull ActivityAgreementsView view, @NonNull ActivityAgreementsModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchAgreements(BaseRequest baseRequest) {
        view.showProgressDialog("Consultando portafolio...");
        model.getAgreements(baseRequest, this);
    }

    @Override
    public <T> void onSuccess(Response<BaseResponseConvenios<ResponseResumen>> response) {
        view.hideProgressDialog();
        try{
            ResponseResumen responseResumen = (ResponseResumen) response.body().getRespuesta();
            Resumen resumen = new Resumen();
            resumen.setEmailAsociado(responseResumen.getEmailAsociado());
            resumen.setUbicacionAsociado(responseResumen.getUbicacionAsociado());
            resumen.setCategorias(responseResumen.generateCategorias());
            resumen.setConvenios(responseResumen.generateConvenios());
            resumen.setCiudades(responseResumen.generateCiudades());
            view.showResultAgreements(resumen);
        }catch (Exception ex){
            view.showDataFetchError("");
        }
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponseConvenios<ResponseResumen>> response) {
        view.hideProgressDialog();
        view.showExpiredToken("");
    }

    @Override
    public <T> void onError(Response<BaseResponseConvenios<ResponseResumen>> response) {
        view.hideProgressDialog();
        view.showDataFetchError("");
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
