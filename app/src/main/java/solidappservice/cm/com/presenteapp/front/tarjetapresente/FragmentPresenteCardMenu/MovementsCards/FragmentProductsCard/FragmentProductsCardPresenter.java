package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.MovementsCards.FragmentProductsCard;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProductos;
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
        view.showProgressDialog("Obteniendo tarjetas...");
        model.getPresenteCards(baseRequest, this);
    }

    @Override
    public void fetchAccounts(BaseRequest baseRequest) {
        view.showProgressDialog("Consultando Cuentas...");
        model.getAccounts(baseRequest, this);
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

                if(ResponseTarjeta.class.equals(listValidate.get(0).getClass())){
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
                }

                if(ResponseProductos.class.equals(listValidate.get(0).getClass())){
                    List<ResponseProductos> productos = (List<ResponseProductos>) response.body().getResultado();

                    Encripcion encripcion = Encripcion.getInstance();
                    for (ResponseProductos producto : productos) {
                        producto.setA_numdoc(encripcion.desencriptar(producto.getA_numdoc()));
                    }
                    view.showAccountsPresenteCard(productos);
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
