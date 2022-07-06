package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.SecurityCard.FragmentSecurityCardMenu;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseTarjeta;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public class FragmentSecurityCardMenuPresenter implements FragmentSecurityCardMenuContract.Presenter,
        FragmentSecurityCardMenuContract.APIListener{

    FragmentSecurityCardMenuView view;
    FragmentSecurityCardMenuModel model;

    public FragmentSecurityCardMenuPresenter(@NonNull FragmentSecurityCardMenuView view, @NonNull FragmentSecurityCardMenuModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchPresenteCards(BaseRequest baseRequest) {
        view.showProgressDialog("Obteniendo tarjetas...");
        model.getPresenteCards(baseRequest, this);
    }

    @Override
    public <T> void onSuccess(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            List<ResponseTarjeta> tarjetas = (List<ResponseTarjeta>) response.body().getResultado();
            if(tarjetas != null){
                Encripcion encripcion = new Encripcion();
                for (ResponseTarjeta tarjeta : tarjetas) {
                    tarjeta.setA_numcta(encripcion.desencriptar(tarjeta.getA_numcta().trim()));
                    tarjeta.setA_obliga(encripcion.desencriptar(tarjeta.getA_obliga().trim()));
                    tarjeta.setA_tipodr(tarjeta.getA_tipodr().trim());
                    tarjeta.setF_movim(tarjeta.getF_movim().trim());
                    tarjeta.setI_estado(tarjeta.getI_estado().trim());
                    tarjeta.setK_numpla(encripcion.desencriptar(tarjeta.getK_numpla().trim()));
                    tarjeta.setK_mnumpl(encripcion.desencriptar(tarjeta.getK_mnumpl().trim()));
                    tarjeta.setN_percol(tarjeta.getN_percol().trim());
                    tarjeta.setV_cupo(tarjeta.getV_cupo());
                }
            }
            view.showPresenteCards(tarjetas);
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
