package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.ReplacementCard.FragmentReplacementCardDetail;

import android.util.Log;

import androidx.annotation.NonNull;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.request.RequestMensajeReposicionTarjeta;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.request.RequestReposicionTarjeta;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseDependenciasAsociado;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseValorReposicionTarjeta;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public class FragmentReplacementCardDetailPresenter implements FragmentReplacementCardDetailContract.Presenter,
        FragmentReplacementCardDetailContract.APIListener{

    FragmentReplacementCardDetailView view;
    FragmentReplacementCardDetailModel model;

    public FragmentReplacementCardDetailPresenter(@NonNull FragmentReplacementCardDetailView view, @NonNull FragmentReplacementCardDetailModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchAssociatedDependence(BaseRequest baseRequest) {
        view.showProgressDialog("Un momento...");
        model.getAssociatedDependence(baseRequest, this);
    }

    @Override
    public void fechReplacementCardValue() {
        view.showProgressDialog("Obteniendo valor comisión...");
        model.getReplacementCardValue(this);
    }

    @Override
    public void solicityReplacementCard(RequestReposicionTarjeta body) {
        view.showProgressDialog("Enviando solicitud...");
        model.solicityReplacementCard(body, this);
    }

    @Override
    public void sendMessageCardReplacementSuccessful(RequestMensajeReposicionTarjeta body) {
        model.sendMessageCardReplacementSuccessful(body, this);
    }

    @Override
    public <T> void onSuccess(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            if(ResponseDependenciasAsociado.class.equals(response.body().getResultado().getClass())){
                ResponseDependenciasAsociado dependencias = (ResponseDependenciasAsociado) response.body().getResultado();
                view.showAssociatedDependence(dependencias);
            }
            if(ResponseValorReposicionTarjeta.class.equals(response.body().getResultado().getClass())){
                ResponseValorReposicionTarjeta valorTarjeta = (ResponseValorReposicionTarjeta) response.body().getResultado();
                if(valorTarjeta != null){
                    view.showReplacementCardValue(valorTarjeta.getV_numeri());
                }
            }
            if(String.class.equals(response.body().getResultado().getClass())){
                String resultReplacementeCard = (String) response.body().getResultado();
                if(resultReplacementeCard != null && resultReplacementeCard.equals("OK")){
                    view.showResultReplacementCard();
                }
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
