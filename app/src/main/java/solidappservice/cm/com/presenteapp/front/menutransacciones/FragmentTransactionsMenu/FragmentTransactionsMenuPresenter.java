package solidappservice.cm.com.presenteapp.front.menutransacciones.FragmentTransactionsMenu;

import android.util.Log;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseMovimientos;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.parametrosemail.ResponseParametrosEmail;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseParametrosAPP;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseDependenciasAsociado;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 17/09/2021.
 */
public class FragmentTransactionsMenuPresenter implements FragmentTransactionsMenuContract.Presenter,
        FragmentTransactionsMenuContract.APIListener{

    FragmentTransactionsMenuView view;
    FragmentTransactionsMenuModel model;

    public FragmentTransactionsMenuPresenter(@NonNull FragmentTransactionsMenuView view, @NonNull FragmentTransactionsMenuModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchButtonStateAdvanceSalary() {
        view.hideTransactionMenu();
        view.showCircularProgressBar("Un momento");
        model.getButtonStateAdvanceSalary(this);
    }
    @Override
    public <T> void onSuccessStateAdvanceSalary(Response<BaseResponse<T>> response) {
        try{
            ResponseParametrosEmail stateAdvanceSalary = (ResponseParametrosEmail) response.body().getResultado();
            if (stateAdvanceSalary != null && stateAdvanceSalary.getV_alfabe().equals("Y")) {
                view.showButtonAdvanceSalary();
                view.fetchButtonActionAdvanceSalary();
            } else {
                view.hideButtonAdvanceSalary();
                view.fetchButtonStateResorts();
            }
        }catch (Exception ex){
            view.hideButtonAdvanceSalary();
            view.fetchButtonStateResorts();
        }
    }
    @Override
    public <T> void onErrorStateAdvanceSalary(Response<BaseResponse<T>> response) {
        view.hideButtonAdvanceSalary();
        view.fetchButtonStateResorts();
    }

    @Override
    public void fetchButtonActionAdvanceSalary() {
        model.getButtonActionAdvanceSalary(this);
    }
    @Override
    public <T> void onSuccessActionAdvanceSalary(Response<BaseResponse<T>> response) {
        try{
            ResponseParametrosEmail actionAdvanceSalary = (ResponseParametrosEmail) response.body().getResultado();
            if(actionAdvanceSalary != null && actionAdvanceSalary.getV_alfabe() != null){
                List<String> listDependencies = new ArrayList<>();
                String[] dependencias =  actionAdvanceSalary.getV_alfabe().split(";");
                for(int i = 0; i < dependencias.length ; i++){
                    listDependencies.add(dependencias[i].trim());
                }
                view.changeButtonActionAdvanceSalary(listDependencies);
                view.fetchAssociatedDependency();
            }else{
                view.fetchAssociatedDependency();
            }
        }catch (Exception ex){
            view.hideButtonAdvanceSalary();
            view.fetchButtonStateResorts();
        }
    }
    @Override
    public <T> void onErrorActionAdvanceSalary(Response<BaseResponse<T>> response) {
        view.hideButtonAdvanceSalary();
        view.fetchButtonStateResorts();
    }


    @Override
    public void fetchAssociatedDependency(BaseRequest baseRequest) {
        model.getAssociatedDependency(baseRequest,this);
    }
    @Override
    public <T> void onSuccessAssociatedDependency(Response<BaseResponse<T>> response) {
        try{
            ResponseDependenciasAsociado associatedDependency = (ResponseDependenciasAsociado) response.body().getResultado();
            if(associatedDependency != null && associatedDependency.getCodigodependencia() != null){
                view.showResultAssociatedDependency(associatedDependency.getCodigodependencia());
            }
            view.fetchButtonStateResorts();
        }catch (Exception ex){
            view.hideButtonAdvanceSalary();
            view.fetchButtonStateResorts();
        }
    }
    @Override
    public <T> void onErrorAssociatedDependency(Response<BaseResponse<T>> response) {
        view.hideButtonAdvanceSalary();
        view.fetchButtonStateResorts();
    }


    @Override
    public void fetchButtonStateResorts() {
        model.getButtonStateResorts(this);
    }
    @Override
    public <T> void onSuccessButtonStateResorts(Response<BaseResponse<T>> response) {
        try{
            ResponseParametrosAPP stateResorts = (ResponseParametrosAPP) response.body().getResultado();
            if(stateResorts != null && stateResorts.getEstado() != null && stateResorts.getEstado().equals("Y")){
                view.showButtonResorts(stateResorts.getValue1());
            }else{
                view.hideButtonResorts();
            }
            view.fetchButtonStateTransfers();
        }catch (Exception ex){
            view.fetchButtonStateTransfers();
        }
    }
    @Override
    public <T> void onErrorButtonStateResorts(Response<BaseResponse<T>> response) {
        view.hideButtonAdvanceSalary();
        view.fetchButtonStateResorts();
    }


    @Override
    public void fetchPendingSalaryAdvance(BaseRequest baseRequest) {
        view.showProgressDialog("Un momento...");
        model.getPendingSalaryAdvance(baseRequest,this);
    }
    @Override
    public <T> void onSuccessPendingSalaryAdvance(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try{
            List<ResponseMovimientos> listMovements = (List<ResponseMovimientos>) response.body().getResultado();
            List<ResponseMovimientos> listPendingMovements = new ArrayList<>();
            for (ResponseMovimientos m : listMovements) {
                SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
                String fecha = dt1.format(new Date());
                Date date = dt1.parse(fecha);
                if (m.getI_estado() != null && (m.getI_estado().equals("E") || m.getI_estado().equals("A")) && (m.getF_solictud().after(date) || m.getF_solictud().equals(date) )) {
                    listPendingMovements.add(m);
                }
            }
            view.showResultPendingSalaryAdvance(listPendingMovements);
        }catch (Exception ex){
            view.showDataFetchError("");
        }
    }
    @Override
    public <T> void onErrorPendingSalaryAdvance(Response<BaseResponse<T>> response) {
        if(response != null){
            view.showDataFetchError(response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("");
        }
    }



    @Override
    public void fetchButtonStateTransfers() {
        model.getButtonStateTransfers(this);
    }
    @Override
    public <T> void onSuccessButtonStateTransfers(Response<BaseResponse<T>> response) {
        try{
            ResponseParametrosAPP stateTransferencias = (ResponseParametrosAPP) response.body().getResultado();
            if(stateTransferencias != null && stateTransferencias.getEstado() != null && stateTransferencias.getEstado().equals("Y")){
                view.showButtonTransfers();
            }else{
                view.hideButtonTransfers();
            }
            view.fetchButtonStateSavings();
        }catch (Exception ex){
            view.hideButtonTransfers();
            view.fetchButtonStateSavings();
        }
    }
    @Override
    public <T> void onErrorButtonStateTransfers(Response<BaseResponse<T>> response) {
        view.hideButtonTransfers();
        view.fetchButtonStateSavings();
    }


    @Override
    public void fetchButtonStateSavings() {
        model.getButtonStateSavings(this);
    }
    @Override
    public <T> void onSuccessButtonStateSavings(Response<BaseResponse<T>> response) {
        try{
            ResponseParametrosAPP stateAperturaAhorros = (ResponseParametrosAPP) response.body().getResultado();
            if(stateAperturaAhorros != null && stateAperturaAhorros.getEstado() != null && stateAperturaAhorros.getEstado().equals("Y")){
                view.showButtonSavings();
            }else{
                view.hideButtonSavings();
            }
            view.fetchButtonStatePaymentCredits();
        }catch (Exception ex){
            view.hideButtonSavings();
            view.fetchButtonStatePaymentCredits();
        }
    }
    @Override
    public <T> void onErrorButtonStateSavings(Response<BaseResponse<T>> response) {
        view.hideButtonSavings();
        view.fetchButtonStatePaymentCredits();
    }



    @Override
    public void fetchButtonStatePaymentCredits() {
        model.getButtonStatePaymentCredits(this);
    }
    @Override
    public <T> void onSuccessButtonStatePaymentCredits(Response<BaseResponse<T>> response) {
        try{
            ResponseParametrosAPP stateAbonoObligaciones = (ResponseParametrosAPP) response.body().getResultado();
            if(stateAbonoObligaciones != null && stateAbonoObligaciones.getEstado() != null && stateAbonoObligaciones.getEstado().equals("Y")){
                view.showButtonPaymentCredits();
            }else{
                view.hideButtonPaymentCredits();
            }
            view.hideCircularProgressBar();
            view.showTransactionMenu();
        }catch (Exception ex){
            view.hideButtonPaymentCredits();
            view.hideCircularProgressBar();
            view.showTransactionMenu();
        }
    }
    @Override
    public <T> void onErrorButtonStatePaymentCredits(Response<BaseResponse<T>> response) {
        view.hideButtonPaymentCredits();
        view.hideCircularProgressBar();
        view.showTransactionMenu();
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        view.showExpiredToken(response.body().getErrorToken());
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
