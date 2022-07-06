package solidappservice.cm.com.presenteapp.front.tranferencias.FragmentDeleteAccount;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.transferencias.response.ResponseCuentasInscritas;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.transferencias.request.RequestDeleteAccount;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 11/09/2021.
 */
public class FragmentDeleteAccountPresenter implements FragmentDeleteAccountContract.Presenter,
        FragmentDeleteAccountContract.APIListener{

    FragmentDeleteAccountView view;
    FragmentDeleteAccountModel model;

    public FragmentDeleteAccountPresenter(@NonNull FragmentDeleteAccountView view, @NonNull FragmentDeleteAccountModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchRegisteredAccounts(BaseRequest baseRequest) {
        view.showProgressDialog("Consultando cuentas...");
        model.getRegisteredAccounts(baseRequest, this);
    }

    @Override
    public void deleteSelectedAccounts(List<RequestDeleteAccount> request) {
        if(request != null && request.size() > 0){
            view.disabledDeleteAccountButton();
            int counter = 1;
            for(RequestDeleteAccount rd : request){
                view.showProgressDialog("Borrando cuenta(s)...");
                model.deleteSelectedAccounts(rd, counter == request.size(), this);
                counter ++;
            }
        }
    }

    @Override
    public <T> void onSuccessRegisteredAccounts(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            List<ResponseCuentasInscritas> responseCuentasInscritas = (List<ResponseCuentasInscritas>) response.body().getResultado();
            if(responseCuentasInscritas != null && responseCuentasInscritas.size()>0){
                view.showRegisteredAccounts(responseCuentasInscritas);
            }else{
                view.showDataFetchError("No se encontraron cuentas inscritas");
            }
        }catch (Exception ex){
            view.showDataFetchError("");
        }
    }

    @Override
    public <T> void onSuccessDeleteSelectedAccounts(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            String result = (String) response.body().getResultado();
            if(!result.equals("OK")){
                view.showResultDeleteAccounts(result);
            }else{
                view.enabledDeleteAccountButton();
            }
        }catch (Exception ex){
            view.enabledDeleteAccountButton();
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
        view.enabledDeleteAccountButton();
        if(response != null){
            view.showDataFetchError(response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("");
        }
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideProgressDialog();
        view.enabledDeleteAccountButton();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("");
        }
    }

}
