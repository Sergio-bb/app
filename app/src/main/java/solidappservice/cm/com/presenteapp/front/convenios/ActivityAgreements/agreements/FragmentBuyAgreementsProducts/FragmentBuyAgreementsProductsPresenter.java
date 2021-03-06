package solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.agreements.FragmentBuyAgreementsProducts;

import androidx.annotation.NonNull;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseConvenios;
import solidappservice.cm.com.presenteapp.entities.convenios.request.RequestSolicitudProducto;
import solidappservice.cm.com.presenteapp.entities.convenios.response.ResponseSolicitudProducto;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public class FragmentBuyAgreementsProductsPresenter implements FragmentBuyAgreementsProductsContract.Presenter,
        FragmentBuyAgreementsProductsContract.APIListener{

    FragmentBuyAgreementsProductsView view;
    FragmentBuyAgreementsProductsModel model;

    public FragmentBuyAgreementsProductsPresenter(@NonNull FragmentBuyAgreementsProductsView view, @NonNull FragmentBuyAgreementsProductsModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void buyProduct(BaseRequest baseRequest, RequestSolicitudProducto solicitudProducto) {
        view.showProgressDialog("Solicitando producto...");
        model.buyProduct(baseRequest, solicitudProducto, this);
    }

    @Override
    public <T> void onSuccess(Response<BaseResponseConvenios<T>> response) {
        view.hideProgressDialog();
        try{
            ResponseSolicitudProducto responseSolicitud = (ResponseSolicitudProducto) response.body().getRespuesta();
            if(responseSolicitud != null){
                view.showResultBuyProduct(responseSolicitud.getResultadoHTML());
            }else{
                view.showDataFetchError("");
            }
        }catch (Exception ex){
            view.showDataFetchError("");
        }
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponseConvenios<T>> response) {
        view.hideProgressDialog();
        view.showExpiredToken("");
    }

    @Override
    public <T> void onError(Response<BaseResponseConvenios<T>> response) {
        view.hideProgressDialog();
        if(response != null &&
                response.body() != null &&
                response.body().getDescripcionError() != null &&
                response.body().getDescripcionError() != ""){
            view.showDataFetchError(response.body().getDescripcionError());
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
