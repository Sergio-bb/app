package solidappservice.cm.com.presenteapp.front.menuprincipal.FragmentHome;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.banercomercial.response.ResponseBanerComercial;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.mensajes.response.ResponseObtenerMensajes;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseParametrosAPP;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 17/09/2021.
 */
public class FragmentHomePresenter implements FragmentHomeContract.Presenter,
        FragmentHomeContract.APIListener{

    FragmentHomeView view;
    FragmentHomeModel model;

    public FragmentHomePresenter(@NonNull FragmentHomeView view, @NonNull FragmentHomeModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchButtonStateAgreements() {
        model.getButtonStateAgreements(this);
    }
    @Override
    public <T> void onSuccessButtonStateAgreements(Response<BaseResponse<T>> response) {
        try{
            ResponseParametrosAPP stateConvenios= (ResponseParametrosAPP) response.body().getResultado();
            if(stateConvenios != null && stateConvenios.getEstado() != null && stateConvenios.getEstado().equals("Y")){
                view.showButtonAgreements();
            }else{
                view.hideButtonAgreements();
            }
            view.hideCircularProgressBar();

        }catch (Exception ex){
            view.showDataFetchError("");
        }
    }


    @Override
    public void fetchCommercialBanner(BaseRequest baseRequest) {
        view.showCircularProgressBar();
        model.getCommercialBanner(baseRequest, this);
    }
    @Override
    public <T> void onSuccessCommercialBanner(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        try{
            List<ResponseBanerComercial> commercialBanner = (List<ResponseBanerComercial>) response.body().getResultado();
            if(commercialBanner != null){
                ResponseBanerComercial commercialBannerActive = null;
                for(ResponseBanerComercial bp : commercialBanner){
                    if(bp.getI_estado() != null && bp.getI_estado().equals("S")){
                        commercialBannerActive = bp;
                    }
                }

                if(commercialBannerActive != null){
                    view.showCommercialBanner(commercialBannerActive);
                }
            }else{
                view.hideCommercialBanner();
            }
        }catch (Exception ex){
            view.showDataFetchError("");
        }
    }


    @Override
    public void fetchMessageInbox(BaseRequest baseRequest) {
        model.getMessageInbox(baseRequest, this);
    }
    @Override
    public <T> void onSuccessMessageInbox(Response<BaseResponse<T>> response) {
        try{
            List<ResponseObtenerMensajes> messages = (List<ResponseObtenerMensajes>) response.body().getResultado();
            if(messages != null){
                view.showMessageInbox(messages);
            }else{
                //view.showDataFetchError("Upps, se ha producido un error, inténtalo nuevamente en unos minutos.");
            }
        }catch (Exception ex){
            view.showDataFetchError("");
        }
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
            view.showDataFetchError(response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("");
        }
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideCircularProgressBar();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("");
        }
    }

}
