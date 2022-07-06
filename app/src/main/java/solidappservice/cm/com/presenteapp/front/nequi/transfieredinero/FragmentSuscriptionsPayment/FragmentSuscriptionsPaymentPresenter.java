package solidappservice.cm.com.presenteapp.front.nequi.transfieredinero.FragmentSuscriptionsPayment;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestCheckStatusPaymentSuscription;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestPaymentSuscritpion;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestReversePaymentSuscription;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseCheckStatusPaymentSuscription;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultarTopes;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiGeneral;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseGetAuthorizacionBalance;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiBalance;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponsePaymentSuscription;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseReversePaymentSuscriptions;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

public class FragmentSuscriptionsPaymentPresenter implements FragmentSuscriptionsPaymentContract.Presenter, FragmentSuscriptionsPaymentContract.APIListener{

    FragmentSuscriptionsPaymentView view;
    FragmentSuscriptionsPaymentModel model;

    public FragmentSuscriptionsPaymentPresenter(@NonNull FragmentSuscriptionsPaymentView view, @NonNull FragmentSuscriptionsPaymentModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchAccountsAvailable(){
        view.hideContentNequiPaySuscriptions();
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
        view.hideContentNequiPaySuscriptions();
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
            view.showDataFetchError("Lo sentimos", "No hemos logrado obtener tu cuentas activas en Presente, inténtalo de nuevo en unos minutos");
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
        view.hideContentNequiPaySuscriptions();
        view.showCircularProgressBar("Obteniendo valores de transferencia");
        model.getMaximumTranferValues(baseRequest, this);
    }
    @Override
    public <T> void onSuccessMaximumTranferValues(Response<BaseResponseNequi<T>> response) {
        try{
            ResponseConsultarTopes topes = (ResponseConsultarTopes) response.body().getResult();
            if(topes != null){
                view.showMaximumTranferValues(topes);
                view.fetchIncompleteSubscriptionPayments();
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
    public void fetchIncompleteSubscriptionPayments(BaseRequest baseRequest){
        view.hideContentNequiPaySuscriptions();
        view.showCircularProgressBar("Un momento");
        model.getIncompleteSubscriptionPayments(baseRequest, this);
    }
    @Override
    public <T> void onSuccessIncompleteSubscriptionPayments(Response<BaseResponseNequi<T>> response) {
        try{
            boolean isResultValid = response.body().isResponse();
            view.hideCircularProgressBar();
            view.showContentNequiPaySuscriptions();
            view.fetchNequiBalance();
        }catch(Exception ex){
            Log.d("Error", ex.getMessage());
            view.hideCircularProgressBar();
            view.showContentNequiPaySuscriptions();
            view.fetchNequiBalance();
        }
    }
    @Override
    public <T> void onErrorIncompleteSubscriptionPayments(Response<BaseResponseNequi<T>> response) {
        view.hideCircularProgressBar();
        view.showContentNequiPaySuscriptions();
        view.fetchNequiBalance();
    }


    @Override
    public void fetchNequiBalance(BaseRequestNequi baseRequest){
        view.showCircularProgressBarNequiBalance();
        model.getNequiBalance(baseRequest, this);
    }
    @Override
    public <T> void onSuccessNequiBalance(Response<BaseResponseNequi<T>> response) {
        try{
            ResponseNequiBalance saldo = (ResponseNequiBalance) response.body().getResult();
            if(saldo != null && saldo.getData() != null & saldo.getData().getBalance() != null){
                view.showNequiBalance(saldo.getData().getBalance());
                view.hideCircularProgressBarNequiBalance();
            }else{
                view.fetchAuthorizationNequiBalance();
            }
        }catch(Exception ex){
            Log.d("Error", ex.getMessage());
            view.hideCircularProgressBarNequiBalance();
            view.showNequiBalance("");
        }
    }
    @Override
    public <T> void onErrorNequiBalance(Response<BaseResponseNequi<T>> response) {
        view.fetchAuthorizationNequiBalance();
    }


    @Override
    public void fetchAuthorizationNequiBalance(BaseRequestNequi baseRequest){
        view.showCircularProgressBarNequiBalance();
        model.getAuthorizationNequiBalance(baseRequest, this);
    }
    @Override
    public <T> void onSuccessAuthorizationNequiBalance(Response<BaseResponseNequi<T>> response) {
        view.hideCircularProgressBarNequiBalance();
        try{
            BaseResponseNequi<ResponseGetAuthorizacionBalance> autorizacion = (BaseResponseNequi<ResponseGetAuthorizacionBalance>)response.body();
            if(autorizacion != null){
                if(autorizacion.getResult() != null && autorizacion.getResult().getData() != null
                        && autorizacion.getResult().getData().getStatus() != null){
                    view.resultGetAuthorizationNequiBalance(autorizacion.getResult().getData().getStatus());
                }else{
                    view.resultGetAuthorizationNequiBalance("ERROR");
                }
            }else{
                view.resultGetAuthorizationNequiBalance("ERROR");
            }
        }catch(Exception ex){
            view.resultGetAuthorizationNequiBalance("ERROR");
        }
    }
    @Override
    public <T> void onErrorAuthorizationNequiBalance(Response<BaseResponseNequi<T>> response) {
        view.hideCircularProgressBarNequiBalance();
        view.resultGetAuthorizationNequiBalance("ERROR");
    }


    @Override
    public void makePaymentSuscription(RequestPaymentSuscritpion request) {
        view.showDialogTransferLoading();
        model.makePaymentSuscription(request, this);
    }
    @Override
    public <T> void onSuccessPaymentSuscription(Response<BaseResponseNequi<T>> response, boolean isSuccessfulTransfer) {
        view.hideDialogConfirmTransfer();
        try{
            ResponsePaymentSuscription resultTransfer = (ResponsePaymentSuscription) response.body().getResult();
            if(resultTransfer != null){
                if(isSuccessfulTransfer){
//                    view.fetchReversePaymentSubscription(resultTransfer);
                    view.fetchCheckStatusPaymentSubscription(resultTransfer);
                }else{
                    if(resultTransfer.getEstadoPagoPresente() == null || resultTransfer.getEstadoPagoPresente().equals(("F"))){
                        view.showDialogTransferError("");
                    }else if(resultTransfer.getEstadoPagoPresente().equals("C") &&
                                (resultTransfer.getEstadoPagoNequi() == null || resultTransfer.getEstadoPagoNequi().equals("F"))){
                        view.editTextDialogTransferLoading("Estamos teniendo problemas para procesar tu pago con Nequi");
                        view.fetchReversePaymentSubscription(resultTransfer);
                    }
                }
            }else{
                view.hideDialogConfirmTransfer();
                view.hideDialogTransferLoading();
                view.showDialogTransferError("");
            }
        }catch(Exception ex){
            view.hideDialogConfirmTransfer();
            view.hideDialogTransferLoading();
            view.showDialogTransferError("");
        }
    }
    @Override
    public <T> void onErrorPaymentSuscription(Response<BaseResponseNequi<T>> response) {
        view.hideDialogConfirmTransfer();
        view.hideDialogTransferLoading();
        view.showDialogTransferError("");
    }


    @Override
    public void fetchReversePaymentSubscription(RequestReversePaymentSuscription request) {
        model.reverseNequiSubscriptions(request, this);
    }
    @Override
    public <T> void onSuccessReversePaymentSuscription(Response<BaseResponseNequi<T>> response) {
        view.hideDialogConfirmTransfer();
        try{
            ResponseReversePaymentSuscriptions resultTransfer = (ResponseReversePaymentSuscriptions) response.body().getResult();
            view.hideDialogTransferLoading();
            view.showDialogTransferError("");
        }catch(Exception ex){
            view.hideDialogTransferLoading();
            view.showDialogTransferError("");
        }
    }
    @Override
    public <T> void onErrorReversePaymentSuscription(Response<BaseResponseNequi<T>> response) {
        view.hideDialogConfirmTransfer();
        view.hideDialogTransferLoading();
        view.showDialogTransferError("");
    }


    @Override
    public void fetchCheckStatusPaymentSubscription(RequestCheckStatusPaymentSuscription request) {
        model.checkStatusPaymentSubscription(request, this);
    }
    @Override
    public <T> void onSuccessCheckStatusPaymentSubscription(Response<BaseResponseNequi<T>> response, boolean isSuccessfulTransfer) {
        view.hideDialogConfirmTransfer();
        try{
            ResponseCheckStatusPaymentSuscription resultTransfer = (ResponseCheckStatusPaymentSuscription) response.body().getResult();
            if(resultTransfer != null){
                if(isSuccessfulTransfer){
                    view.hideDialogTransferLoading();
                    view.showDialogTransferSuccess();
                }else{
                    if(resultTransfer.getEstadoPagoNequi() != null && resultTransfer.getEstadoPagoNequi().equals("F")){
                        view.editTextDialogTransferLoading("Estamos teniendo problemas para procesar tu pago con Nequi");
                        view.fetchReversePaymentSubscription(new ResponsePaymentSuscription(
                            resultTransfer.getIdTransaccionPresente(),
                            resultTransfer.getEstadoPagoPresente(),
                            resultTransfer.getIdTransaccionNequi(),
                            resultTransfer.getIdPagoNequi(),
                            resultTransfer.getEstadoPagoNequi()
                        ));
                    }else{
                        view.hideDialogTransferLoading();
                        view.showDialogTransferPending();
                    }
                }
            }

        }catch(Exception ex){
            view.hideDialogTransferLoading();
            view.showDialogTransferError("");
        }
    }
    @Override
    public <T> void onErrorCheckStatusPaymentSubscription(Response<BaseResponseNequi<T>> response) {
        view.hideDialogConfirmTransfer();
        view.hideDialogTransferLoading();
        view.showDialogTransferError("");
    }


    @Override
    public <T> void onExpiredTokenNequi(Response<BaseResponseNequi<T>> response) {
        view.hideCircularProgressBar();
        view.showExpiredToken(response.body().getErrorToken());
    }
    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        view.showExpiredToken(response.body().getErrorToken());
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideCircularProgressBar();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }
}
