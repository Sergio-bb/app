package solidappservice.cm.com.presenteapp.front.convenios.FragmentListAgrementsProducts;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseConvenios;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Producto;
import solidappservice.cm.com.presenteapp.entities.convenios.response.ResponseProducto;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public class FragmentListAgrementsProductsPresenter implements FragmentListAgrementsProductsContract.Presenter,
        FragmentListAgrementsProductsContract.APIListener{

    FragmentListAgrementsProductsView view;
    FragmentListAgrementsProductsModel model;

    public FragmentListAgrementsProductsPresenter(@NonNull FragmentListAgrementsProductsView view, @NonNull FragmentListAgrementsProductsModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchProductsByAgreements(BaseRequest baseRequest, String idAgreement) {
        view.hideContentProductsByAgreements();
        view.showCircularProgressBar("Consultando productos...");
        model.getProductsByAgreements(baseRequest, idAgreement, this);
    }


    @Override
    public <T> void onSuccess(Response<BaseResponseConvenios<T>> response) {
        view.hideCircularProgressBar();
        try{
            List<ResponseProducto> responseProductos = (List<ResponseProducto>) response.body().getRespuesta();
            List<Producto> newProductos = new ArrayList<>();
            if(responseProductos != null){
                for(ResponseProducto p : responseProductos){
                    newProductos.add(p.generateProducto());
                }
                view.showProductsByAgreements(newProductos);
                view.showContentProductsByAgreements();
            }else{
                view.showDialogError("Lo sentimos", "");
                view.showErrorWithRefresh();
            }

        }catch (Exception ex){
            view.showDialogError("Lo sentimos", "");
            view.showErrorWithRefresh();
        }
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponseConvenios<T>> response) {
        view.hideCircularProgressBar();
        view.showExpiredToken("Has estado inactivo un tiempo, por tu seguridad hemos finalizado tu sesión, ingresa nuevamente.");
    }

    @Override
    public <T> void onError(Response<BaseResponseConvenios<T>> response) {
        view.hideCircularProgressBar();
        if(response != null){
            view.showDialogError("Lo sentimos", response.body().getDescripcionError());
        }else{
            view.showDialogError("Lo sentimos", "");
        }
        view.showErrorWithRefresh();
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideCircularProgressBar();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDialogError("Lo sentimos", "");
        }
        view.showErrorWithRefresh();
    }

}
