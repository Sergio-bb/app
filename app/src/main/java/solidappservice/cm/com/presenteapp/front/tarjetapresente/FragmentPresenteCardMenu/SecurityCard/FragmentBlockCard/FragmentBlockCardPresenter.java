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
        view.showProgressDialog("Un momento...");
        model.getReasonsBlockCard(baseRequest, this);
    }

    @Override
    public void blockCard(RequestBloquearTarjeta request) {
        view.showProgressDialog("Cancelando tarjeta...");
        model.blockCard(request, this);
    }

    @Override
    public <T> void onSuccess(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            if(ArrayList.class.equals(response.body().getResultado().getClass())){
                List<Object> listValidate = (List<Object>) response.body().getResultado();
                if(listValidate.size() <= 0){
                    listValidate.add(new Object());
                }
                if(String.class.equals(listValidate.get(0).getClass())){
                    List<String> motivosBloqueo = (List<String>) response.body().getResultado();
                    view.showReasonsBlockCard(motivosBloqueo);
                }
            }

            if(String.class.equals(response.body().getResultado().getClass())){
                String resulBloqueoTarjeta = (String) response.body().getResultado();
                view.showResultBlockCard(resulBloqueoTarjeta);
            }
        }catch(Exception ex){
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
        if(response != null){
            view.showDataFetchError(response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("");
        }
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideProgressDialog();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("");
        }
    }

}
