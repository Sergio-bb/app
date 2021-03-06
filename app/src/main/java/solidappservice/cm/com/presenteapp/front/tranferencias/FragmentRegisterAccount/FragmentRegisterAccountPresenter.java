package solidappservice.cm.com.presenteapp.front.tranferencias.FragmentRegisterAccount;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.transferencias.response.ResponseBanco;
import solidappservice.cm.com.presenteapp.entities.transferencias.response.ResponseCuentasInscritas;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.transferencias.request.RequestRegisterAccount;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 11/09/2021.
 */
public class FragmentRegisterAccountPresenter implements FragmentRegisterAccountContract.Presenter,
        FragmentRegisterAccountContract.APIListener{

    FragmentRegisterAccountView view;
    FragmentRegisterAccountModel model;

    public FragmentRegisterAccountPresenter(@NonNull FragmentRegisterAccountView view, @NonNull FragmentRegisterAccountModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchBanks() {
        view.showProgressDialog("Consultando cuentas...");
        model.getBanks(this);
    }

    @Override
    public void fetchRegisteredAccounts(BaseRequest baseRequest) {
        view.showProgressDialog("Validando datos...");
        model.getRegisteredAccounts(baseRequest, this);
    }

    @Override
    public void registerAccount(RequestRegisterAccount request) {
        view.disabledRegisterAccountButton();
        view.showProgressDialog("Inscribiendo cuenta...");
        model.registerAccount(request, this);
    }

    @Override
    public <T> void onSuccessBanks(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            List<ResponseBanco> responseBancos = (List<ResponseBanco>) response.body().getResultado();
            view.showBanks(responseBancos);
        }catch (Exception ex){
            view.showDataFetchError("");
        }
    }

    @Override
    public <T> void onSuccessRegisteredAccounts(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            List<ResponseCuentasInscritas> responseCuentasInscritas = (List<ResponseCuentasInscritas>) response.body().getResultado();
            view.validateRepeatAccounts(responseCuentasInscritas);
        }catch (Exception ex){
            view.showDataFetchError("");
        }
    }

    @Override
    public <T> void onSuccessRegisterAccount(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            String result = (String) response.body().getResultado();
            if(result != null && result.equals("OK")){
                view.showResultRegisterAccount();
            }else{
                view.enabledRegisterAccountButton();
                view.showDataFetchError("");
            }
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
        view.enabledRegisterAccountButton();
        if(response != null){
            view.showDataFetchError(response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("");
        }
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideProgressDialog();
        view.enabledRegisterAccountButton();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("");
        }
    }

}
