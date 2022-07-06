package solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentVerifyCode;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Date;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.CodigoVerificacion;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.RequestActualizarDatos;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.RequestEnviarCodigo;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.RequestEnviarCodigoPhone;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.RequestValidarCodigo;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseEnviarCodigo;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseEnviarCodigoPhone;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseValidarCodigo;
import solidappservice.cm.com.presenteapp.entities.dispositivo.apirequest.Dispositivo;
import solidappservice.cm.com.presenteapp.entities.dispositivo.apiresponse.ResponseRegistrarDispositivo;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 11/07/2021.
 */
public class FragmentVerifyCodePresenter implements FragmentVerifyCodeContract.Presenter, FragmentVerifyCodeContract.APIListener{

    FragmentVerifyCodeView view;
    FragmentVerifyCodeModel model;

    public FragmentVerifyCodePresenter(@NonNull FragmentVerifyCodeView view, @NonNull FragmentVerifyCodeModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void sendVerificationCodeEmail(RequestEnviarCodigo codigo) {
        view.hideBoxTypesCodeSend();
        view.showCircularProgressBar("Enviando código");
        model.sendVerificationCodeEmail(codigo,this);
    }

    @Override
    public void sendVerificationCodePhone(RequestEnviarCodigo codigo) {
        view.hideBoxTypesCodeSend();
        view.showCircularProgressBar("Enviando código");
        model.sendVerificationCodePhone(codigo,this);
    }

    @Override
    public void sendSms(RequestEnviarCodigoPhone codigo) {
        view.hideBoxTypesCodeSend();
        view.showCircularProgressBar("Enviando código");
        model.sendSms(codigo,this);
    }

    @Override
    public void updatePersonalData(RequestActualizarDatos datos) {
        view.showProgressDialog("Actualizando datos...");
        model.updatePersonalData(datos,this);
    }

    @Override
    public void registerDevice(Dispositivo dispositivo) {
        view.showProgressDialog("Actualizando datos...");
        model.registerDevice(dispositivo,this);
    }

    @Override
    public void validateVerificationCode(RequestValidarCodigo codigo) {
        view.hideBoxTypesCodeSend();
        view.showCircularProgressBar("Validando código");
        model.validateVerificationCode(codigo, this);
    }

    @Override
    public void resendVerificationCode() {
        view.showBoxTypesCodeSend();
        view.disabledSectionVerificationCode();
        view.sendVerificationCodeEmail();
    }

    @Override
    public <T> void onSuccessSendVerificationCodeEmail(Response<BaseResponse<T>> response) {
        try {
            ResponseEnviarCodigo codigo = (ResponseEnviarCodigo) response.body().getResultado();
            view.hideCircularProgressBar();
            view.showBoxTypesCodeSend();
            view.showDialogCodeSent();
            view.enableSectionVerificationCode();
            view.initializeCounter();
            view.resultSendVerificationCode(new CodigoVerificacion(
                    codigo.getIdCodigo(),
                    codigo.getCodigo(),
                    new Date(codigo.getFechaGeneracion()),
                    new Date(codigo.getFechaExpiracion())
            ));
        }catch(Exception ex){
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public <T> void onSuccessSendVerificationCodePhone(Response<BaseResponse<T>> response) {
        try {
            ResponseEnviarCodigo codigo = (ResponseEnviarCodigo) response.body().getResultado();
            view.hideCircularProgressBar();
            view.showBoxTypesCodeSend();
            view.showDialogCodeSent();
            view.enableSectionVerificationCode();
            view.initializeCounter();
            view.resultSendVerificationCode(new CodigoVerificacion(
                    codigo.getIdCodigo(),
                    codigo.getCodigo(),
                    new Date(codigo.getFechaGeneracion()),
                    new Date(codigo.getFechaExpiracion())
            ));
        }catch(Exception ex){
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public <T> void onSuccessValidateVerificationCode(Response<BaseResponse<T>> response) {
        try {
            view.hideCircularProgressBar();
            ResponseValidarCodigo codigo = (ResponseValidarCodigo) response.body().getResultado();
            if (codigo != null && codigo.getCodigoValido().equals("Y") && codigo.getCodigoExpirado().equals("N")) {
                view.showBoxTypesCodeSend();
                view.updateInformation();
            }
            if (codigo != null && codigo.getCodigoValido().equals("Y") && codigo.getCodigoExpirado().equals("Y")) {
                view.showDialogError("Lo sentimos", "El código ha caducado");
                view.showBoxTypesCodeSend();
                view.disabledSectionVerificationCode();
            }
            if (codigo != null && codigo.getCodigoValido().equals("N")) {
                view.showBoxTypesCodeSend();
                view.showDialogError("Lo sentimos", "Verifica si ingresaste el codigo correctamente");
            }
        }catch(Exception ex){
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public <T> void onSuccessUpdatePersonalData(Response<BaseResponse<T>> response) {
        try {
            if (response.body().getResultado().equals("OK")) {
                view.hideProgressDialog();
                view.resultUpdatePersonalData();
            } else {
                view.hideProgressDialog();
                view.showDataFetchError("Lo sentimos", "");
            }
        }catch(Exception ex){
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public <T> void onSuccessRegisterDevice(Response<BaseResponse<T>> response) {
        try {
            view.hideProgressDialog();
            ResponseRegistrarDispositivo registrarDispositivo = (ResponseRegistrarDispositivo) response.body().getResultado();
            view.resultRegisterDevice(registrarDispositivo.getIdRegistroDispositivo());
        }catch(Exception ex){
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public <T> void onSuccessSendSms(Response<BaseResponse<T>> response) {
        try {
            ResponseEnviarCodigoPhone result = (ResponseEnviarCodigoPhone) response.body().getResultado();
            if (result.getMessageText().equals("OK")) {
                view.hideProgressDialog();
                view.resultUpdatePersonalData();
            } else {
                view.hideProgressDialog();
                view.showDataFetchError("Lo sentimos", (String) response.body().getResultado());
            }
        }catch(Exception ex){
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        view.hideCircularProgressBar();
        view.showBoxTypesCodeSend();
        view.disabledSectionVerificationCode();
        view.showExpiredToken(response.body().getErrorToken());
    }

    @Override
    public <T> void onError(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        view.hideCircularProgressBar();
        view.showBoxTypesCodeSend();
        view.disabledSectionVerificationCode();
        view.showDataFetchError("Lo sentimos", "");
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideProgressDialog();
        view.hideCircularProgressBar();
        view.showBoxTypesCodeSend();
        view.disabledSectionVerificationCode();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("Lo sentimos","");
        }
    }

    public boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
