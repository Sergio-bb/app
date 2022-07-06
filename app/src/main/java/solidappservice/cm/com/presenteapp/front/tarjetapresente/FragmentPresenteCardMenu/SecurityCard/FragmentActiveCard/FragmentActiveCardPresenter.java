package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.SecurityCard.FragmentActiveCard;

import android.util.Log;

import androidx.annotation.NonNull;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.request.RequestActivarTarjeta;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public class FragmentActiveCardPresenter implements FragmentActiveCardContract.Presenter,
        FragmentActiveCardContract.APIListener{

    FragmentActiveCardView view;
    FragmentActiveCardModel model;

    public FragmentActiveCardPresenter(@NonNull FragmentActiveCardView view, @NonNull FragmentActiveCardModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void activateCard(RequestActivarTarjeta request) {
        view.showProgressDialog("Activando tarjeta...");
        model.activateCard(request, this);
    }

    @Override
    public <T> void onSuccess(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            String resultActivarTarjeta = (String) response.body().getResultado();
            view.showResultActivateCard(resultActivarTarjeta);
        }catch(Exception ex) {
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
        if(response != null){
            view.showDataFetchError("Lo sentimos", response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideProgressDialog();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }

}
