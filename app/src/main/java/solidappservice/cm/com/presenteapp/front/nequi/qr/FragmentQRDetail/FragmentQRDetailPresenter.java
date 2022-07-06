package solidappservice.cm.com.presenteapp.front.nequi.qr.FragmentQRDetail;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestPaymentQR;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiGeneral;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultarTopes;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseMakePaymentQR;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

public class FragmentQRDetailPresenter implements FragmentQRDetailContract.Presenter, FragmentQRDetailContract.APIListener{

    FragmentQRDetailView view;
    FragmentQRDetailModel model;

    public FragmentQRDetailPresenter(@NonNull FragmentQRDetailView view, @NonNull FragmentQRDetailModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchAccountsAvailable(){
        view.hideContentQrDetail();
        view.showCircularProgressBar("Obteniendo cuentas disponibles...");
        model.getAccountsAvailable(this);
    }
    @Override
    public <T> void onSuccessAccountsAvailable(Response<ResponseNequiGeneral> response) {
        try{
            ResponseNequiGeneral bolsillosDisponibles = (ResponseNequiGeneral) response.body();
            if(bolsillosDisponibles != null){
                boolean pocketAvailable = bolsillosDisponibles.getN_Valor1() != null && bolsillosDisponibles.getN_Valor1().equals("Y");
                boolean pocketPayroll = bolsillosDisponibles.getN_Valor2() != null && bolsillosDisponibles.getN_Valor2().equals("Y");
                view.resultAccountsAvailable(pocketAvailable, pocketPayroll);
            }else{
                view.resultAccountsAvailable(true, false);
            }
            view.fetchAccounts();
        }catch (Exception ex){
            Log.d("Error",ex.getMessage());
            view.resultAccountsAvailable(true, false);
            view.fetchAccounts();
        }
    }
    @Override
    public <T> void onErrorAccountsAvailable(Response<ResponseNequiGeneral> response) {
        view.resultAccountsAvailable(true, false);
        view.fetchAccounts();
    }

    @Override
    public void fetchAccounts(BaseRequest baseRequest) {
        view.hideContentQrDetail();
        view.showCircularProgressBar("Obteniendo cuentas disponibles...");
        model.getAccounts(baseRequest, this);
    }
    @Override
    public <T> void onSuccessAccounts(Response<BaseResponse<T>> response) {
        try{
            List<ResponseProducto> responseCuentas = (List<ResponseProducto>) response.body().getResultado();
            Encripcion encripcion = Encripcion.getInstance();
            for (ResponseProducto producto : responseCuentas) {
                producto.setA_numdoc(encripcion.desencriptar(producto.getA_numdoc()));
            }
            view.showAccounts(responseCuentas);
            view.fetchMaximumTranferValues();
        }catch (Exception ex){
            view.hideCircularProgressBar();
            view.showDataFetchError("Lo sentimos", "");
        }
    }
    @Override
    public <T> void onErrorAccounts(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        if(response != null){
            view.showDataFetchError("Lo sentimos", response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void fetchMaximumTranferValues(BaseRequest baseRequest){
        view.hideContentQrDetail();
        view.showCircularProgressBar("Obteniendo valores de transferencia");
        model.getMaximumTranferValues(baseRequest, this);
    }
    @Override
    public <T> void onSuccessMaximumTranferValues(Response<BaseResponseNequi<T>> response) {
        try{
            ResponseConsultarTopes topes = (ResponseConsultarTopes) response.body().getResult();
            if(topes != null){
                view.hideCircularProgressBar();
                view.showMaximumTranferValues(topes);
                view.showDataCommerceText();
                view.showContentQrDetail();
            }else{
                view.hideCircularProgressBar();
                view.showDataFetchError("Lo sentimos", "Tenemos un error para cargar el estado de tus transacciones en Nequi, inténtalo de nuevo en unos minutos");
            }
        }catch(Exception ex){
            view.hideCircularProgressBar();
            view.showDataFetchError("Lo sentimos", "Tenemos un error para cargar el estado de tus transacciones en Nequi, inténtalo de nuevo en unos minutos");
        }
    }
    @Override
    public <T> void onErrorMaximumTranferValues(Response<BaseResponseNequi<T>> response) {
        view.showDataFetchError("Lo sentimos", "Tenemos un error para cargar el estado de tus transacciones en Nequi, inténtalo de nuevo en unos minutos");
    }

    @Override
    public void makePaymentByQR(RequestPaymentQR request) {
        view.showDialogPaymentLoading();
        model.makePaymentByQr(request, this);
    }
    @Override
    public void onSuccessMakePaymentByQr(Response<BaseResponseNequi<ResponseMakePaymentQR>> response) {
        view.hideDialogConfirmPayment();
        try{

        }catch (Exception ex){
            view.hideDialogConfirmPayment();
            view.hideDialogPaymentLoading();
            view.showDialogPaymentError();
        }
    }
    @Override
    public void onErrorMakePaymentByQr(Response<BaseResponseNequi<ResponseMakePaymentQR>> response) {
        view.hideDialogConfirmPayment();
        view.hideDialogPaymentLoading();
        view.showDialogPaymentError();
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.hideDialogConfirmPayment();
        view.hideDialogPaymentLoading();
        view.showExpiredToken(response.body().getErrorToken());
    }@Override
    public <T> void onExpiredTokenNequi(Response<BaseResponseNequi<T>> response) {
        view.hideDialogConfirmPayment();
        view.hideDialogPaymentLoading();
        view.showExpiredToken(response.body().getErrorToken());
    }

    @Override
    public <T> void onError(Response<BaseResponse<T>> response) {
        view.hideDialogConfirmPayment();
        view.hideDialogPaymentLoading();
        if(response != null){
            view.showDataFetchError("Lo sentimos", response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideDialogConfirmPayment();
        view.hideDialogPaymentLoading();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }
}
