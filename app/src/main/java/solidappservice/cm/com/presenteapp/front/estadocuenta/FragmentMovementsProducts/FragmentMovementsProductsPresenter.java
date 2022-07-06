package solidappservice.cm.com.presenteapp.front.estadocuenta.FragmentMovementsProducts;

import android.util.Log;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.request.RequestMovimientosProducto;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseMovimientoProducto;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public class FragmentMovementsProductsPresenter implements FragmentMovementsProductsContract.Presenter,
        FragmentMovementsProductsContract.APIListener{

    FragmentMovementsProductsView view;
    FragmentMovementsProductsModel model;

    public FragmentMovementsProductsPresenter(@NonNull FragmentMovementsProductsView view, @NonNull FragmentMovementsProductsModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchMovementsProducts(RequestMovimientosProducto request) {
        view.showProgressDialog("Estamos realizando la consulta de tus saldos y productos...");
        model.getMovementsProducts(request, this);
    }

    @Override
    public <T> void onSuccess(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            List<ResponseMovimientoProducto> productos = (List<ResponseMovimientoProducto>) response.body().getResultado();
            Encripcion encripcion = Encripcion.getInstance();
            SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yy");
            for (ResponseMovimientoProducto producto : productos) {
                producto.setA_numdoc(encripcion.desencriptar(producto.getA_numdoc()));
                Date date = new Date(Long.parseLong(producto.getF_movimi()));
                producto.setF_movimi(formatFecha.format(date));
            }
            view.showMovementsProducts(productos);
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
