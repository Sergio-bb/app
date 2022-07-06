package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.MovementsCards.FragmentMovementsProductsCard;

import android.util.Log;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseMovimientoProducto;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.request.RequestMovimientosProducto;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
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
    public void fetchAccounts(BaseRequest baseRequest) {
        view.hideSectionMovementsPresenteCards();
        view.showCircularProgressBar("Consultando Cuentas...");
        model.getAccounts(baseRequest, this);
    }
    @Override
    public <T> void onSuccessAccounts(Response<BaseResponse<T>> response) {
        try{
            List<ResponseProducto> productos = (List<ResponseProducto>) response.body().getResultado();
            Encripcion encripcion = Encripcion.getInstance();
            for (ResponseProducto producto : productos) {
                producto.setA_numdoc(encripcion.desencriptar(producto.getA_numdoc()));
            }
            view.showAccountsPresenteCard(productos);
        }catch (Exception ex){
            view.showDialogError("Lo sentimos", "");
            view.showErrorWithRefresh();
        }
    }
    @Override
    public <T> void onErrorAccounts(Response<BaseResponse<T>> response) {
        view.showDialogError("Lo sentimos", "");
        view.showErrorWithRefresh();
    }
    @Override
    public void onFailureAccounts(Throwable t, boolean isErrorTimeOut) {
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDialogError("Lo sentimos", "");
        }
        view.showErrorWithRefresh();
    }

    @Override
    public void fetchMovementsPresenteCards(RequestMovimientosProducto request) {
        view.hideSectionMovementsPresenteCards();
        view.showCircularProgressBar("Consultando movimientos...");
        model.getMovementsPresenteCards(request, this);
    }

    @Override
    public <T> void onSuccess(Response<BaseResponse<T>> response) {
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
            view.hideCircularProgressBar();
            view.showMovementsPresenteCards(movimientos);
            view.showSectionMovementsPresenteCards();
        }catch (Exception ex){
            view.hideCircularProgressBar();
            view.showDialogError("Lo sentimos", "");
            view.showErrorWithRefresh();
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
            view.showDialogError("Lo sentimos", response.body().getMensajeErrorUsuario());
        }else{
            view.showDialogError("Lo sentimos", "");
        }
        view.showErrorWithRefresh();
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideCircularProgressBar();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDialogError("Lo sentimos", "");
        }

        view.showErrorWithRefresh();
    }

}
