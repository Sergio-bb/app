package solidappservice.cm.com.presenteapp.front.estadocuenta.FragmentProducts;

import android.util.Log;

import androidx.annotation.NonNull;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.parametrosemail.ResponseParametrosEmail;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public class FragmentProductsPresenter implements FragmentProductsContract.Presenter,
        FragmentProductsContract.APIListener{

    FragmentProductsView view;
    FragmentProductsModel model;

    public FragmentProductsPresenter(@NonNull FragmentProductsView view, @NonNull FragmentProductsModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchStatusMessageMisAportes() {
        view.showProgressDialog("Un momento...");
        model.getStatusMessageMisAportes(this);
    }


    @Override
    public <T> void onSuccess(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            ResponseParametrosEmail estado = (ResponseParametrosEmail) response.body().getResultado();
            if (estado != null && estado.getV_alfabe() != null && estado.getV_alfabe().equals("Y")) {
                view.showMessageMisAportes();
            }
        }catch (Exception ex){
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
