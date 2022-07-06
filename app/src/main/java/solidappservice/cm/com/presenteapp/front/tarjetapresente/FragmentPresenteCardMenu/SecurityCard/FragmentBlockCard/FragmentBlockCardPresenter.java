package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.SecurityCard.FragmentBlockCard;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.request.RequestBloquearTarjeta;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public class FragmentBlockCardPresenter implements FragmentBlockCardContract.Presenter,
        FragmentBlockCardContract.APIListener{

    FragmentBlockCardView view;
    FragmentBlockCardModel model;

    public FragmentBlockCardPresenter(@NonNull FragmentBlockCardView view, @NonNull FragmentBlockCardModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchReasonsBlockCard(BaseRequest baseRequest) {
        view.hideSectionBlockCard();
        view.showCircularProgressBar("Un momento...");
        model.getReasonsBlockCard(baseRequest, this);
    }
    @Override
    public <T> void onSuccessReasonsBlockCard(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        try{
            List<String> motivosBloqueo = (List<String>) response.body().getResultado();
            view.showSectionBlockCard();
            view.showReasonsBlockCard(motivosBloqueo);
        }catch(Exception ex){
            view.showDialogError("Lo sentimos", "");
            view.showErrorWithRefresh();
        }
    }
    @Override
    public <T> void onErrorReasonsBlockCard(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        if(response != null){
            view.showDialogError("Lo sentimos", response.body().getMensajeErrorUsuario());
        }else{
            view.showDialogError("Lo sentimos", "");
        }
        view.showErrorWithRefresh();
    }
    @Override
    public void onFailureReasonsBlockCard(Throwable t, boolean isErrorTimeOut) {
        view.hideCircularProgressBar();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDialogError("Lo sentimos", "");
        }
        view.showErrorWithRefresh();
    }

    @Override
    public void blockCard(RequestBloquearTarjeta request) {
        view.showProgressDialog("Cancelando tarjeta...");
        model.blockCard(request, this);
    }
    @Override
    public <T> void onSuccessBlockCard(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            String resulBloqueoTarjeta = (String) response.body().getResultado();
            view.showResultBlockCard(resulBloqueoTarjeta);
        }catch(Exception ex){
            view.showDataFetchError("Lo sentimos", "");
        }
    }
    @Override
    public <T> void onErrorBlockCard(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        if(response != null){
            view.showDataFetchError("Lo sentimos", response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }
    @Override
    public void onFailureBlockCard(Throwable t, boolean isErrorTimeOut) {
        view.hideProgressDialog();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        view.showExpiredToken(response.body().getErrorToken());
    }

}
