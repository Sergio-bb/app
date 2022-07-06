package solidappservice.cm.com.presenteapp.front.adelantonomina.FragmentSolicity;

import android.util.Log;

import androidx.annotation.NonNull;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestLogs;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestNoCumple;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseMovimientos;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseNoCumple;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseTips;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseTopes;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseValidarRequisitos;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 24/08/2021.
 */
public class FragmentSolicityPresenter implements FragmentSolicityContract.Presenter, FragmentSolicityContract.APIListener {

    FragmentSolicityView view;
    FragmentSolicityModel model;

    public FragmentSolicityPresenter(@NonNull FragmentSolicityView view, @NonNull FragmentSolicityModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchMoves(BaseRequest base) {
        view.showProgressDialog("Obteniendo movimientos...");
        model.getMoves(base, this);
    }

    @Override
    public boolean validateRequestedAmount(int valorSolicitado, String valorMaximo, int valorCupo, int valorMax, int valorMin) {

        DecimalFormat formato = new DecimalFormat("#,###");
        String formatValorMax = formato.format(valorMax);
        String formatValorMin = formato.format(valorMin);
        if(valorCupo <= 0 || valorSolicitado > valorCupo){
            view.showErrorRequestedAmount("No tienes cupo para realizar esta solicitud");
            return false;
        }

        if(valorMaximo == null || valorMaximo.isEmpty()){
            view.showErrorRequestedAmount("Tu cupo no se ha cargado correctamente");
            return false;
        }

        if(valorSolicitado == 0) {
            view.showErrorRequestedAmount("Debes ingresar un monto");
            return false;
        }

        if(valorSolicitado > valorMax || valorSolicitado < valorMin){
            view.showErrorRequestedAmount("Debes ingresar un monto entre $"+formatValorMin+" y $"+formatValorMax);
            return false;
        }

        if(valorCupo > 0 && (valorSolicitado > 0) && valorSolicitado <= valorMax &&
                valorSolicitado >= valorMin &&  valorSolicitado <= valorCupo) {
            return true;
        }
        return false;
    }

    @Override
    public void fetchTips(){
        model.getTips(this);
    }

    @Override
    public void validateRequirements(BaseRequest base){
        view.showProgressDialog("Validando requisitos...");
        model.getValidateRequirements(base, this);
    }

    @Override
    public void fetchDebtCapacity(BaseRequest base){
        view.showProgressDialog("Obteniendo topes...");
        model.getDebtCapacity(base, this);
    }

    @Override
    public void fetchReasonsNotMeetsRequirements(RequestNoCumple request){
        view.showProgressDialog("Un momento...");
        model.getReasonsNotMeetsRequirements(request, this);
    }

    @Override
    public void enterLogs(RequestLogs request){
        model.sendLogs(request, this);
    }

    @Override
    public <T> void onSuccess(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            if(ResponseValidarRequisitos.class.equals(response.body().getResultado().getClass())){
                ResponseValidarRequisitos requisitos = (ResponseValidarRequisitos) response.body().getResultado();
                if(requisitos != null){
//                    requisitos.setCumple("CUMPLE");
                    if(requisitos.getCumple().equals("CUMPLE")){
                        view.fetchDebtCapacity(requisitos);
                    } else {
                        view.fetchReasonsNotMeetsRequirements(requisitos);
                    }
                }else{
                    view.enterLogs("ASOCIADO NO CUMPLE REQUISITOS", "Error obteniendo los requisitos");
                    view.showDataFetchError("No es posible solicitar este producto:", "Ha ocurrido un error cargando la información inténtalo de nuevo en unos minutos");
                }
            }

            if(ResponseTopes.class.equals(response.body().getResultado().getClass())){
                ResponseTopes topes = (ResponseTopes) response.body().getResultado();
                if (topes != null){
//                    topes.setV_cupo(150000);
//                    topes.setV_maximo(300000);
//                    topes.setV_minimo(50000);
                    if(topes.getV_cupo() <= 0){
                        view.showDataFetchError("No es posible solicitar este producto:", "No tienes cupo para solicitar este producto.");
                    }else{
                        view.showDebtCapacity(topes);
                    }
                } else {
                    view.enterLogs("ASOCIADO NO CUMPLE REQUISITOS", "Error obteniendo los topes");
                    view.showDataFetchError("No es posible solicitar este producto:", "Ha ocurrido un error cargando la información inténtalo de nuevo en unos minutos");
                }
            }

            if(ArrayList.class.equals(response.body().getResultado().getClass())){
                List<Object> listValidate = (List<Object>) response.body().getResultado();
                if(listValidate.size() <= 0){
                    listValidate.add(new Object());
                }

                if(ResponseMovimientos.class.equals(listValidate.get(0).getClass())){
                    List<ResponseMovimientos> movimientos = (List<ResponseMovimientos>) response.body().getResultado();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    if(movimientos!=null && movimientos.size()>0){
                        for (Iterator<ResponseMovimientos> it = movimientos.iterator(); it.hasNext();) {
                            ResponseMovimientos m = it.next();
                            if (m.getI_estado() != null && !m.getI_estado().equals("C")) {
                                it.remove();
                            }
                        }
                    }
                    view.showMoves(movimientos);
                }

                if(ResponseTips.class.equals(listValidate.get(0).getClass())){
                    List<ResponseTips> tips = (List<ResponseTips>) response.body().getResultado();
                    if(tips!=null){
                        view.showDialogTips(tips);
                    }
                }

                if(ResponseNoCumple.class.equals(listValidate.get(0).getClass())){
                    List<ResponseNoCumple> listaNoCumple = (List<ResponseNoCumple>) response.body().getResultado();
                    if(listaNoCumple.size() > 0){
                        String nc = "";
                        for (ResponseNoCumple item: listaNoCumple) {
                            nc += "• "+item.getN_observacion()+"\n";
                        }
                        nc += "\n Si tienes alguna duda comunicate con tu gestor";
                        view.enterLogs("ASOCIADO NO CUMPLE REQUISITOS", nc);
                        view.showDataFetchError("No es posible solicitar este producto:", nc);
                    }else{
                        view.enterLogs("ASOCIADO NO CUMPLE REQUISITOS", "No se obtuvieron los motivos por las que el usuario no cumple los requisitos.");
                        view.showDataFetchError("No es posible solicitar este producto:", "Upps, en este momento no es posible solicitar este producto, por favor inténtalo más tarde. ");
                    }
                }
            }
        }catch(Exception ex){
            view.showDataFetchError("PRESENTE","");
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
            view.showDataFetchError("PRESENTE", response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("PRESENTE","");
        }
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideProgressDialog();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("PRESENTE","");
        }
    }

}
