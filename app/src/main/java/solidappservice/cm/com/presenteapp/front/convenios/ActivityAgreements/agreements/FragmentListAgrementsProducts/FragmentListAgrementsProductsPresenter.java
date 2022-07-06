package solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.agreements.FragmentListAgrementsProducts;

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
        view.showProgressDialog("Consultando productos...");
        model.getProductsByAgreements(baseRequest, idAgreement, this);
    }


    @Override
    public <T> void onSuccess(Response<BaseResponseConvenios<T>> response) {
        view.hideProgressDialog();
        try{
            List<ResponseProducto> responseProductos = (List<ResponseProducto>) response.body().getRespuesta();
            List<Producto> newProductos = new ArrayList<>();
            if(responseProductos != null){
                for(ResponseProducto p : responseProductos){
                    newProductos.add(p.generateProducto());
                }
                view.showProductsByAgreements(newProductos);
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
