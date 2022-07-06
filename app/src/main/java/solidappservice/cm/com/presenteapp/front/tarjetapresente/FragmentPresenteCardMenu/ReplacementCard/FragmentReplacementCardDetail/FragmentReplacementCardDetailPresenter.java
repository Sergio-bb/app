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
        view.hideSectionReplacemendCard();
        view.showCircularProgressBar("Un momento...");
        model.getAssociatedDependence(baseRequest, this);
    }
    @Override
    public <T> void onSuccessAssociatedDependence(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        try{
            ResponseDependenciasAsociado dependencias = (ResponseDependenciasAsociado) response.body().getResultado();
            view.showSectionReplacemendCard();
            view.showAssociatedDependence(dependencias);
        }catch (Exception ex){
            view.showDialogError("Lo sentimos", "");
            view.showErrorWithRefresh();
        }
    }
    @Override
    public <T> void onErrorAssociatedDependence(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        if(response != null){
            view.showDialogError("Lo sentimos", response.body().getMensajeErrorUsuario());
        }else{
            view.showDialogError("Lo sentimos", "");
        }
        view.showErrorWithRefresh();
    }
    @Override
    public void onFailureAssociatedDependence(Throwable t, boolean isErrorTimeOut) {
        view.hideCircularProgressBar();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDialogError("Lo sentimos", "");
        }
        view.showErrorWithRefresh();
    }


    @Override
    public void fechReplacementCardValue() {
        view.showCircularProgressBarCardValue();
        model.getReplacementCardValue(this);
    }
    @Override
    public <T> void onSuccessReplacementCardValue(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBarCardValue();
        try{
            ResponseValorReposicionTarjeta valorTarjeta = (ResponseValorReposicionTarjeta) response.body().getResultado();
            if(valorTarjeta != null){
                view.showReplacementCardValue(valorTarjeta.getV_numeri());
            }
        }catch (Exception ex){
            view.showDataFetchError("Lo sentimos", "");
        }
    }
    @Override
    public <T> void onErrorReplacementCardValue(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        if(response != null){
            view.showDataFetchError("Lo sentimos", response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }
    @Override
    public void onFailureReplacementCardValue(Throwable t, boolean isErrorTimeOut) {
        view.hideCircularProgressBar();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }


    @Override
    public void solicityReplacementCard(RequestReposicionTarjeta body) {
        view.showProgressDialog("Enviando solicitud...");
        model.solicityReplacementCard(body, this);
    }
    @Override
    public <T> void onSuccessSolicityReplacementCard(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            String resultReplacementeCard = (String) response.body().getResultado();
            if(resultReplacementeCard != null && resultReplacementeCard.equals("OK")){
                view.showResultReplacementCard();
            }
        }catch (Exception ex){
            view.showDataFetchError("Lo sentimos", "");
        }
    }
    @Override
    public <T> void onErrorSolicityReplacementCard(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        if(response != null){
            view.showDataFetchError("Lo sentimos", response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }
    @Override
    public void onFailureSolicityReplacementCard(Throwable t, boolean isErrorTimeOut) {
        view.hideProgressDialog();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }


    @Override
    public void sendMessageCardReplacementSuccessful(RequestMensajeReposicionTarjeta body) {
        model.sendMessageCardReplacementSuccessful(body, this);
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        view.showExpiredToken(response.body().getErrorToken());
    }

}
