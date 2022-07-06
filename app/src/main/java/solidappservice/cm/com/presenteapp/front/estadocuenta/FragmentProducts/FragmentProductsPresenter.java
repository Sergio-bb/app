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
        model.getStatusMessageMisAportes(this);
    }


    @Override
    public <T> void onSuccess(Response<BaseResponse<T>> response) {
        try{
            ResponseParametrosEmail estado = (ResponseParametrosEmail) response.body().getResultado();
            if (estado != null && estado.getV_alfabe() != null && estado.getV_alfabe().equals("Y")) {
                view.showMessageMisAportes();
            }
        }catch (Exception ex){
        }
    }
    @Override
    public <T> void onError(Response<BaseResponse<T>> response) {
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.showExpiredToken(response.body().getErrorToken());
    }

}
