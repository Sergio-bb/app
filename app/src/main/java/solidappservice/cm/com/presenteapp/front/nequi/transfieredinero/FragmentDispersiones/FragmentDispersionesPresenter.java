package solidappservice.cm.com.presenteapp.front.nequi.transfieredinero.FragmentDispersiones;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestPaymentDispersion;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestReversePaymentDispersion;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultaSuscripcion;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponsePaymentDispersionError;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponsePaymentDispersionSuccess;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiGeneral;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultarTopes;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseReversePaymentDispersion;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

public class FragmentDispersionesPresenter implements FragmentDispersionesContract.Presenter, FragmentDispersionesContract.APIListener{

    FragmentDispersionesView view;
    FragmentDispersionesModel model;

    public FragmentDispersionesPresenter(@NonNull FragmentDispersionesView view, @NonNull FragmentDispersionesModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchAccountsAvailable(){
        view.hideContentNequiDispersiones();
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
        view.hideContentNequiDispersiones();
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
        view.hideContentNequiDispersiones();
        view.showCircularProgressBar("Obteniendo valores de transferencia");
        model.getMaximumTranferValues(baseRequest, this);
    }
    @Override
    public <T> void onSuccessMaximumTranferValues(Response<BaseResponseNequi<T>> response) {
        try{
            ResponseConsultarTopes topes = (ResponseConsultarTopes) response.body().getResult();
            if(topes != null){
                view.showMaximumTranferValues(topes);
                view.hideCircularProgressBar();
                view.showContentNequiDispersiones();
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
    public void makePaymentDispersion(RequestPaymentDispersion request) {
        view.showDialogTransferLoading();
        model.makePaymentDispersion(request, this);
    }
    @Override
    public <T> void onSuccessMakePaymentDispersion(Response<BaseResponseNequi<T>> response) {
        view.hideDialogConfirmTransfer();
        try{
            BaseResponseNequi<Object> resultTransfer = (BaseResponseNequi<Object>)response.body();
            Gson gson = new Gson();
            String jsonObject = gson.toJson(resultTransfer.getResult());
            if(resultTransfer.isResponse()){
                view.hideDialogTransferLoading();
                view.showDialogTransferSuccess();
            }else{
                ResponsePaymentDispersionError resultPay = gson.fromJson(jsonObject, ResponsePaymentDispersionError.class);
                view.fetchReverseDispersion(resultPay);
            }
        }catch(Exception ex){
            Log.d("Error", ex.getMessage());
            view.hideDialogTransferLoading();
            view.showDialogTransferError("");
        }
    }
    @Override
    public <T> void onErrorMakePaymentDispersion(Response<BaseResponseNequi<T>> response) {
        try{
            view.hideDialogConfirmTransfer();
            view.hideDialogTransferLoading();
            if(response.message().equals("2-CCSB000005")){
                view.showRebaseTopsNequi();
            }else {
                view.showDialogTransferError("");
            }
            BaseResponseNequi<Object> resultTransfer = (BaseResponseNequi<Object>)response.body();
            Gson gson = new Gson();
            assert resultTransfer != null;
            String jsonObject = gson.toJson(resultTransfer.getResult());
            ResponsePaymentDispersionError resultPay = gson.fromJson(jsonObject, ResponsePaymentDispersionError.class);
            view.fetchReverseDispersion(resultPay);
        }catch (Exception e){
            view.hideDialogTransferLoading();
            view.showDialogTransferError("");
        }

    }

    @Override
    public void fetchReversePaymentDispersion(RequestReversePaymentDispersion request) {
        model.reverseNequiPaymentDispersion(request, this);
    }

    @Override
    public void getCostoPorOperacion() {
        model.getCobroPorSuscripcion(this);
    }

    @Override
    public void validateSuscriptionNequi(BaseRequest baseRequest) {
        model.getSuscriptionNequi(baseRequest, this);
    }

    @Override
    public <T> void onSuccessReversePaymentDispersion(Response<BaseResponseNequi<T>> response) {
        view.hideDialogConfirmTransfer();
        try{
            ResponseReversePaymentDispersion resultTransfer = (ResponseReversePaymentDispersion) response.body().getResult();
            view.hideDialogTransferLoading();
            view.showDialogTransferError("");
        }catch(Exception ex){
            view.hideDialogTransferLoading();
            view.showDialogTransferError("");
        }
    }
    @Override
    public <T> void onErrorReversePaymentDispersion(Response<BaseResponseNequi<T>> response) {
        view.hideDialogConfirmTransfer();
        view.hideDialogTransferLoading();
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
    public <T> void onError(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        if(response != null){
            view.showDataFetchError("Lo sentimos", response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
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

    @Override
    public void onSuccesGetCoborPorOperacion(Response<Integer> response) {
        view.costoTransferencia = response.body();
    }

    @Override
    public <T> void onSuccessGetSuscriptionNequi(Response<BaseResponseNequi<T>> response) {
        BaseResponseNequi<ResponseConsultaSuscripcion> consulta = (BaseResponseNequi<ResponseConsultaSuscripcion>)response.body();
        if(consulta != null && consulta.getMessage() != null){
            view.isVinculated = consulta.getMessage().equals("El usuario se encuentra registrado y activo.");
            if ( consulta.getResult().getDatosSuscripcion() != null){
                view.saveSuscriptionData(consulta.getResult().getDatosSuscripcion());
            }else{
                view.showDataFetchError("Lo sentimos", "");
            }
        }
    }
}
