package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.MovementsCards.FragmentMovementsProductsCard;

import android.util.Log;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseMovimientoProducto;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.request.RequestMovimientosProducto;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public class FragmentMovementsProductsCardPresenter implements FragmentMovementsProductsCardContract.Presenter,
        FragmentMovementsProductsCardContract.APIListener{

    FragmentMovementsProductsCardView view;
    FragmentMovementsProductsCardModel model;

    public FragmentMovementsProductsCardPresenter(@NonNull FragmentMovementsProductsCardView view, @NonNull FragmentMovementsProductsCardModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchMovementsPresenteCards(RequestMovimientosProducto request) {
        view.showProgressDialog("Estamos realizando la consulta de tus saldos y productos...");
        model.getMovementsPresenteCards(request, this);
    }

    @Override
    public <T> void onSuccess(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            List<ResponseMovimientoProducto> movimientos = (List<ResponseMovimientoProducto>) response.body().getResultado();
            if(movimientos != null){
                view.showTitleProductPresenteCards();
                Encripcion encripcion = new Encripcion();
                SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yy");
                for (ResponseMovimientoProducto movimiento : movimientos) {
                    movimiento.setA_numdoc(encripcion.desencriptar(movimiento.getA_numdoc().trim()));
                    movimiento.setA_tipodr(movimiento.getA_tipodr().trim());
                    Date date = new Date(Long.parseLong(movimiento.getF_movimi().trim()));
                    movimiento.setF_movimi(formatFecha.format(date));
                    movimiento.setK_numdoc(encripcion.desencriptar(movimiento.getK_numdoc().trim()));
                    movimiento.setN_tipdoc(movimiento.getN_tipdoc().trim());
                    movimiento.setV_valor(movimiento.getV_valor());
                }
            }
            view.showMovementsPresenteCards(movimientos);
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
