package solidappservice.cm.com.presenteapp.front.pagoobligaciones.FragmentPaymentCredits;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.pagoobligaciones.response.ResponsePagosPendientes;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
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
        view.hideSectionPaymentsCredits();
        view.showCircularProgressBar("Validando datos...");
        model.getPendingPayments(baseRequest, this);
    }
    @Override
    public <T> void onSuccessPendingPayments(Response<BaseResponse<T>> response) {
        try{
            List<ResponsePagosPendientes> pendingPayments = (List<ResponsePagosPendientes>) response.body().getResultado();
            if(pendingPayments != null && pendingPayments.size() > 0){
                view.hideCircularProgressBar();
                view.showResultPendingPayments();
            }else{
                view.fetchProducts();
            }
        }catch (Exception ex){
            view.fetchProducts();
        }
    }
    @Override
    public <T> void onErrorPendingPayments(Response<BaseResponse<T>> response) {
        view.fetchProducts();
    }
    @Override
    public void onFailurePendingPayments(Throwable t, boolean isErrorTimeOut) {
        view.fetchProducts();
    }


    @Override
    public void fetchProducts(BaseRequest baseRequest) {
        view.hideSectionPaymentsCredits();
        view.showCircularProgressBar("Actualizando estado de cuenta...");
        model.getProducts(baseRequest,this);
    }
    @Override
    public <T> void onSuccessProducts(Response<BaseResponse<T>> response) {
        try{
            List<ResponseProducto> products = (List<ResponseProducto>) response.body().getResultado();
            Encripcion encripcion = Encripcion.getInstance();
            for (ResponseProducto producto : products) {
                producto.setA_numdoc(encripcion.desencriptar(producto.getA_numdoc()));
            }
            view.hideCircularProgressBar();
            view.showSectionPaymentsCredits();
            view.showProductstoDebit(products);
            view.showProductstoPay(products);
        }catch (Exception ex){
            Log.d("Error",ex.getMessage());
            view.hideCircularProgressBar();
            view.showDialogError("Lo sentimos", "Se ha producido un error, inténtalo nuevamente en unos minutos.");
            view.showErrorWithRefresh();
        }
    }
    @Override
    public <T> void onErrorProducts(Response<BaseResponse<T>> response) {
        view.showDialogError("Lo sentimos", "Se ha producido un error, inténtalo nuevamente en unos minutos.");
        view.showErrorWithRefresh();
    }
    @Override
    public void onFailureProducts(Throwable t, boolean isErrorTimeOut) {
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDialogError("Lo sentimos", "");
        }
        view.showErrorWithRefresh();
    }


    @Override
    public void makePayment(RequestEnviarPago request) {
        view.disabledAcceptButton();
        view.showProgressDialog("Realizando el pago...");
        model.makePayment(request, this);
    }
    @Override
    public <T> void onSuccessMakePayment(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            String resultPayment = (String) response.body().getResultado();
            view.showResultPayment(resultPayment);
        }catch (Exception ex){
            view.enabledAcceptButton();
            view.showResultPayment(null);
        }
    }
    @Override
    public <T> void onErrorMakePayment(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        view.enabledAcceptButton();
        view.showResultPayment(null);
    }
    @Override
    public void onFailureMakePayment(Throwable t, boolean isErrorTimeOut) {
        view.hideProgressDialog();
        view.enabledAcceptButton();
        view.showResultPayment(null);
    }


    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        view.showExpiredToken(response.body().getErrorToken());
    }


}
