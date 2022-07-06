package solidappservice.cm.com.presenteapp.front.pagoobligaciones.FragmentPaymentCredits;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.pagoobligaciones.response.ResponsePagosPendientes;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProductos;
import solidappservice.cm.com.presenteapp.entities.pagoobligaciones.request.RequestEnviarPago;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

/**
 * CREADO POR MIGUEL DAVID CABEZAS 17/09/2021.
 */
public class FragmentPaymentCreditsPresenter implements FragmentPaymentCreditsContract.Presenter,
        FragmentPaymentCreditsContract.APIListener{

    FragmentPaymentCreditsView view;
    FragmentPaymentCreditsModel model;

    public FragmentPaymentCreditsPresenter(@NonNull FragmentPaymentCreditsView view, @NonNull FragmentPaymentCreditsModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchPendingPayments(BaseRequest baseRequest) {
        view.showProgressDialog("Validando datos...");
        model.getPendingPayments(baseRequest, this);
    }

    @Override
    public void fetchProducts(BaseRequest baseRequest) {
        view.showProgressDialog("Actualizando estado de cuenta...");
        model.getProducts(baseRequest,this);
    }

    @Override
    public void makePayment(RequestEnviarPago request) {
        view.disabledAcceptButton();
        view.showProgressDialog("Realizando el pago...");
        model.makePayment(request, this);
    }


    @Override
    public <T> void onSuccessPendingPayments(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            List<ResponsePagosPendientes> pendingPayments = (List<ResponsePagosPendientes>) response.body().getResultado();
            if(pendingPayments != null && pendingPayments.size() > 0){
                view.showResultPendingPayments();
            }else{
                view.fetchProducts();
            }
        }catch (Exception ex){
            view.showDataFetchError("");
        }
    }

    @Override
    public <T> void onSuccessProducts(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            List<ResponseProductos> products = (List<ResponseProductos>) response.body().getResultado();
            Encripcion encripcion = Encripcion.getInstance();
            for (ResponseProductos producto : products) {
                producto.setA_numdoc(encripcion.desencriptar(producto.getA_numdoc()));
            }
            view.showProductstoDebit(products);
            view.showProductstoPay(products);
        }catch (Exception ex){
            view.showDataFetchError("");
        }
    }

    @Override
    public <T> void onSuccessMakePayment(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            String resultPayment = (String) response.body().getResultado();
            view.showResultPayment(resultPayment);
        }catch (Exception ex){
            view.enabledAcceptButton();
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
        view.enabledAcceptButton();
        if(response != null){
            view.showDataFetchError(response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("");
        }
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideProgressDialog();
        view.enabledAcceptButton();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("");
        }
    }

}
