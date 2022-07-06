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
        view.hideSectionDeleteAccount();
        view.showCircularProgressBar("Consultando cuentas...");
        model.getRegisteredAccounts(baseRequest, this);
    }
    @Override
    public <T> void onSuccessRegisteredAccounts(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        try{
            List<ResponseCuentasInscritas> responseCuentasInscritas = (List<ResponseCuentasInscritas>) response.body().getResultado();
            view.showSectionDeleteAccount();
            view.showRegisteredAccounts(responseCuentasInscritas);
        }catch (Exception ex){
            view.showDialogError("Lo sentimos", "");
            view.showErrorWithRefresh();
        }
    }
    @Override
    public <T> void onErrorRegisteredAccounts(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        if(response != null){
            view.showDialogError("Lo sentimos", response.body().getMensajeErrorUsuario());
        }else{
            view.showDialogError("Lo sentimos", "");
        }
        view.showErrorWithRefresh();
    }
    @Override
    public void onFailureRegisteredAccounts(Throwable t, boolean isErrorTimeOut) {
        view.hideCircularProgressBar();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
        view.showErrorWithRefresh();
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
        view.hideProgressDialog();
        view.enabledDeleteAccountButton();
        if(response != null){
            view.showDataFetchError("Lo sentimos", response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideProgressDialog();
        view.enabledDeleteAccountButton();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }

    }

}
