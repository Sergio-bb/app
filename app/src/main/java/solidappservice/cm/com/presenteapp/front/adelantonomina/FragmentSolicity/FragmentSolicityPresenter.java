package solidappservice.cm.com.presenteapp.front.adelantonomina.FragmentSolicity;

import android.util.Log;

import androidx.annotation.NonNull;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    public void fetchPendingSalaryAdvance(BaseRequest baseRequest) {
        view.hideContentSalaryAdvance();
        view.showCircularProgressBar("Consultando estado de cuenta...");
        model.getPendingSalaryAdvance(baseRequest,this);
    }
    @Override
    public <T> void onSuccessPendingSalaryAdvance(Response<BaseResponse<T>> response) {
        try{
            List<ResponseMovimientos> listMovements = (List<ResponseMovimientos>) response.body().getResultado();
            List<ResponseMovimientos> listPendingMovements = new ArrayList<>();
            for (ResponseMovimientos m : listMovements) {
                SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
                String fecha = dt1.format(new Date());
                Date date = dt1.parse(fecha);
                if ((m.getI_estado().equals("E") || m.getI_estado().equals("A")) && (m.getF_solictud().after(date) || m.getF_solictud().equals(date) )) {
                    listPendingMovements.add(m);
                }
            }
            if(listPendingMovements != null && listPendingMovements.size() > 0){
                view.hideCircularProgressBar();
                view.showResultPendingSalaryAdvance(listPendingMovements);
            }else{
                view.validateRequirements();
            }
        }catch (Exception ex){
            view.validateRequirements();
        }
    }
    @Override
    public <T> void onErrorPendingSalaryAdvance(Response<BaseResponse<T>> response) {
        view.validateRequirements();
    }
    @Override
    public void onFailurePendingSalaryAdvance(Throwable t, boolean isErrorTimeOut) {
        view.validateRequirements();
    }


    @Override
    public void validateRequirements(BaseRequest base){
        view.hideContentSalaryAdvance();
        view.showCircularProgressBar("Validando requisitos...");
        model.getValidateRequirements(base, this);
    }
    @Override
    public <T> void onSuccessValidateRequirements(Response<BaseResponse<T>> response) {
        try{
            ResponseValidarRequisitos requisitos = (ResponseValidarRequisitos) response.body().getResultado();
            if(requisitos != null){
//                requisitos.setCumple("CUMPLE");
                if(requisitos.getCumple().equals("CUMPLE")){
                    view.hideCircularProgressBar();
                    view.showContentSalaryAdvance();
                    view.fetchDebtCapacity(requisitos);
                } else {
                    view.fetchReasonsNotMeetsRequirements(requisitos);
                }
            }else{
                view.enterLogs("ASOCIADO NO CUMPLE REQUISITOS", "Error obteniendo los requisitos");
                view.hideCircularProgressBar();
                view.showDialogError("Lo sentimos", "");
                view.showErrorWithRefresh();
            }
        }catch(Exception ex){
            view.hideCircularProgressBar();
            view.showDialogError("Lo sentimos", "");
            view.showErrorWithRefresh();
        }
    }
    @Override
    public <T> void onErrorValidateRequirements(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        view.showDialogError("Lo sentimos", "");
        view.showErrorWithRefresh();
    }


    @Override
    public void fetchReasonsNotMeetsRequirements(RequestNoCumple request){
        view.hideContentSalaryAdvance();
        view.showCircularProgressBar("Validando requisitos...");
        model.getReasonsNotMeetsRequirements(request, this);
    }
    @Override
    public <T> void onSuccessReasonsNotMeetsRequirements(Response<BaseResponse<T>> response) {
        try{
            List<ResponseNoCumple> listaNoCumple = (List<ResponseNoCumple>) response.body().getResultado();
            if(listaNoCumple.size() > 0){
                String nc = "";
                for (ResponseNoCumple item: listaNoCumple) {
                    nc += "• "+item.getN_observacion()+"\n";
                }
                nc += "\n Si tienes alguna duda comunicate con tu gestor";
                view.hideCircularProgressBar();
                view.enterLogs("ASOCIADO NO CUMPLE REQUISITOS", nc);
                view.showDataFetchError("No es posible solicitar este producto:", nc);
            }else{
                view.hideCircularProgressBar();
                view.enterLogs("ASOCIADO NO CUMPLE REQUISITOS", "No se obtuvieron los motivos por las que el usuario no cumple los requisitos.");
                view.showDataFetchError("No es posible solicitar este producto:", "No cumples con los requisitos establecidos para solicitar este producto \n\n Si tienes alguna duda comunicate con tu gestor");
            }
        }catch(Exception ex){
            view.hideCircularProgressBar();
            view.showDataFetchError("No es posible solicitar este producto:", "No cumples con los requisitos establecidos para solicitar este producto \n\n Si tienes alguna duda comunicate con tu gestor");
        }
    }
    @Override
    public <T> void onErrorReasonsNotMeetsRequirements(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        view.showDataFetchError("No es posible solicitar este producto:", "No cumples con los requisitos establecidos para solicitar este producto \n\n Si tienes alguna duda comunicate con tu gestor");
    }
    @Override
    public void onFailureReasonsNotMeetsRequirements(Throwable t, boolean isErrorTimeOut) {
        view.hideCircularProgressBar();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("No es posible solicitar este producto:", "No cumples con los requisitos establecidos para solicitar este producto \n\n Si tienes alguna duda comunicate con tu gestor");
        }
    }


    @Override
    public void fetchDebtCapacity(BaseRequest base){
        view.showContentSalaryAdvance();
        view.showCircularProgressBarDebtCapacity();
        model.getDebtCapacity(base, this);
    }
    @Override
    public <T> void onSuccessDebtCapacity(Response<BaseResponse<T>> response) {
        try{
            ResponseTopes topes = (ResponseTopes) response.body().getResultado();
            if (topes != null){
//                topes.setV_cupo(150000);
//                topes.setV_maximo(300000);
//                topes.setV_minimo(50000);
                if(topes.getV_cupo() <= 0){
                    view.showDataFetchError("No es posible solicitar este producto:", "No tienes cupo para solicitar este producto.");
                }else{
                    view.hideCircularProgressBarDebtCapacity();
                    view.showDebtCapacity(topes);
                }
            } else {
                view.enterLogs("ASOCIADO NO CUMPLE REQUISITOS", "Error obteniendo los topes");
                view.showDataFetchError("No es posible solicitar este producto:", "Ha ocurrido un error cargando la información inténtalo de nuevo en unos minutos");
            }
        }catch(Exception ex){
            view.showDataFetchError("Lo sentimos", "");
        }
    }


    @Override
    public void fetchMoves(BaseRequest base) {
        view.showCircularProgressBarMovements("Obteniendo movimientos...");
        model.getMoves(base, this);
    }
    @Override
    public <T> void onSuccessMoves(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBarMovements();
        try{
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
        }catch(Exception ex){
            view.showErrorWithRefreshMovements();
        }
    }
    @Override
    public <T> void onErrorMoves(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBarMovements();
        view.showErrorWithRefreshMovements();
    }
    @Override
    public void onFailureMoves(Throwable t, boolean isErrorTimeOut) {
        view.hideCircularProgressBarMovements();
        view.showErrorWithRefreshMovements();
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
    public <T> void onSuccessTips(Response<BaseResponse<T>> response) {
        try{
            List<ResponseTips> tips = (List<ResponseTips>) response.body().getResultado();
            if(tips!=null){
                view.showDialogTips(tips);
            }
        }catch(Exception ex){
            view.showDataFetchError("Lo sentimos", "");
        }
    }
    @Override
    public <T> void onErrorTips(Response<BaseResponse<T>> response) {
    }
    @Override
    public void onFailureTips(Throwable t, boolean isErrorTimeOut) {
    }

    @Override
    public void enterLogs(RequestLogs request){
        model.sendLogs(request, this);
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        view.showExpiredToken(response.body().getErrorToken());
    }

    @Override
    public <T> void onError(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        view.hideCircularProgressBar();
        if(response != null){
            view.showDataFetchError("Lo sentimos", response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideCircularProgressBar();
        view.hideCircularProgressBar();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }
}
