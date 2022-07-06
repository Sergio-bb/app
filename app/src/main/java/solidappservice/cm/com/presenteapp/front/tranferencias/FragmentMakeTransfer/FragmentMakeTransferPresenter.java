package solidappservice.cm.com.presenteapp.front.tranferencias.FragmentMakeTransfer;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.transferencias.response.ResponseTransferenciasPendientes;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.transferencias.request.RequestMakeTransfer;
import solidappservice.cm.com.presenteapp.entities.transferencias.response.ResponseCuentasInscritas;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 11/09/2021.
 */
public class FragmentMakeTransferPresenter implements FragmentMakeTransferContract.Presenter,
        FragmentMakeTransferContract.APIListener{

    FragmentMakeTransferView view;
    FragmentMakeTransferModel model;

    public FragmentMakeTransferPresenter(@NonNull FragmentMakeTransferView view, @NonNull FragmentMakeTransferModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchIncompleteTransfers(BaseRequest baseRequest) {
        view.hideSectionMakeTransfer();
        view.showCircularProgressBar("Validando datos...");
        model.getIncompleteTransfers(baseRequest, this);
    }
    @Override
    public <T> void onSuccessIncompleteTransfers(Response<BaseResponse<T>> response) {
        try{
            List<ResponseTransferenciasPendientes> responseTransferencias = (List<ResponseTransferenciasPendientes>) response.body().getResultado();
            if(responseTransferencias != null && responseTransferencias.size() > 0){
                view.hideCircularProgressBar();
                view.showResultIncompleteTransfers();
            }else{
                view.fetchRegisteredAccounts();
//                view.fetchAccounts();
            }
        } catch(Exception ex){
            view.fetchRegisteredAccounts();
        }
    }
    @Override
    public <T> void onErrorIncompleteTransfers(Response<BaseResponse<T>> response) {
        view.fetchRegisteredAccounts();
    }
    @Override
    public void onFailureIncompleteTransfers(Throwable t, boolean isErrorTimeOut) {
        view.fetchRegisteredAccounts();
    }

    @Override
    public void fetchRegisteredAccounts(BaseRequest baseRequest) {
        view.hideSectionMakeTransfer();
        view.showCircularProgressBar("Obteniendo cuentas inscritas...");
        model.getRegisteredAccounts(baseRequest, this);
    }
    @Override
    public <T> void onSuccessRegisteredAccounts(Response<BaseResponse<T>> response) {
        try{
            List<ResponseCuentasInscritas> responseCuentasInscritas = (List<ResponseCuentasInscritas>) response.body().getResultado();
            view.showRegisteredAccounts(responseCuentasInscritas);
            view.fetchAccounts();
        }catch (Exception ex){
            view.hideCircularProgressBar();
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void fetchAccounts(BaseRequest baseRequest) {
        view.hideSectionMakeTransfer();
        view.showCircularProgressBar("Obteniendo cuentas...");
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
            view.hideCircularProgressBar();
            view.showAccounts(responseCuentas);
            view.showSectionMakeTransfer();
        }catch (Exception ex){
            view.hideCircularProgressBar();
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void makeTransfer(RequestMakeTransfer request) {
        view.disabledMakeTransferButton();
        view.showProgressDialog("Transfiriendo...");
        model.makeTransfer(request, this);
    }
    @Override
    public <T> void onSuccessMakeTransfer(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            String resultTransfer = (String) response.body().getResultado();
            view.showResultTransfer(resultTransfer);
        }catch (Exception ex){
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        view.showExpiredToken(response.body().getErrorToken());
    }

    @Override
    public <T> void onError(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        view.enabledMakeTransferButton();
        if(response != null){
            view.showDataFetchError("Lo sentimos", response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideCircularProgressBar();
        view.enabledMakeTransferButton();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }

}
