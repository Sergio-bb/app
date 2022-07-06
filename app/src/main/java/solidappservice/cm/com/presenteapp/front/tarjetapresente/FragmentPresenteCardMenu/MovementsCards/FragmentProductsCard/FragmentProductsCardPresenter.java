package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.MovementsCards.FragmentProductsCard;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseTarjeta;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public class FragmentProductsCardPresenter implements FragmentProductsCardContract.Presenter,
        FragmentProductsCardContract.APIListener{

    FragmentProductsCardView view;
    FragmentProductsCardModel model;

    public FragmentProductsCardPresenter(@NonNull FragmentProductsCardView view, @NonNull FragmentProductsCardModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchPresenteCards(BaseRequest baseRequest) {
        view.hideSectionProductsCards();
        view.showCircularProgressBar("Obteniendo tarjetas...");
        model.getPresenteCards(baseRequest, this);
    }
    @Override
    public <T> void onSuccessPresenteCards(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
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
            view.showSectionProductsCards();
        }catch (Exception ex){
            view.showDialogError("Lo sentimos", "");
            view.showErrorWithRefresh();
        }
    }
    @Override
    public <T> void onErrorPresenteCards(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        if(response != null){
            view.showDialogError("Lo sentimos", response.body().getMensajeErrorUsuario());
        }else{
            view.showDialogError("Lo sentimos", "");
        }
        view.showErrorWithRefresh();
    }
    @Override
    public void onFailurePresenteCards(Throwable t, boolean isErrorTimeOut) {
        view.hideCircularProgressBar();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDialogError("Lo sentimos", "");
        }
        view.showErrorWithRefresh();
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        view.showExpiredToken(response.body().getErrorToken());
    }

}
