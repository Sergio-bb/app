package solidappservice.cm.com.presenteapp.front.login.ActivityValidateCode;

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
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseValidarCodigo;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.dispositivo.apirequest.Dispositivo;
import solidappservice.cm.com.presenteapp.entities.dispositivo.apiresponse.ResponseRegistrarDispositivo;
import solidappservice.cm.com.presenteapp.entities.tyc.request.RequestAceptaTyC;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 21/09/2021.
 */
public class ActivityValidateCodePresenter implements ActivityValidateCodeContract.Presenter,
        ActivityValidateCodeContract.APIListener{

    ActivityValidateCodeView view;
    ActivityValidateCodeModel model;

    public ActivityValidateCodePresenter(@NonNull ActivityValidateCodeView view, @NonNull ActivityValidateCodeModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void sendVerificationCode(RequestEnviarCodigo codigo) {
        view.hideBoxTypesCodeSend();
        view.showCircularProgressBar("Enviando código");
        model.sendVerificationCode(codigo,this);
    }

    @Override
    public void validateVerificationCode(RequestValidarCodigo codigo) {
        view.hideBoxTypesCodeSend();
        view.hideBoxCodeFields();
        view.showCircularProgressBar("Validando código");
        model.validateVerificationCode(codigo, this);
    }

    @Override
    public void registerDevice(Dispositivo dispositivo) {
        view.hideBoxTypesCodeSend();
        view.showCircularProgressBar("Registrando dispositivo...");
        model.registerDevice(dispositivo,this);
    }

    @Override
    public <T> void onSuccessSendVerificationCode(Response<BaseResponse<T>> response) {
        try {
            ResponseEnviarCodigo codigo = (ResponseEnviarCodigo) response.body().getResultado();
            if(codigo != null){
                view.hideCircularProgressBar();
                view.showBoxCodeFields();
                view.initializeCounter();
                view.resultSendVerificationCode(new CodigoVerificacion(
                        codigo.getIdCodigo(),
                        codigo.getCodigo(),
                        new Date(codigo.getFechaGeneracion()),
                        new Date(codigo.getFechaExpiracion())
                ));
            }
        }catch(Exception ex){
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public <T> void onSuccessValidateVerificationCode(Response<BaseResponse<T>> response) {
        try {
            ResponseValidarCodigo codigo = (ResponseValidarCodigo) response.body().getResultado();
            view.hideCircularProgressBar();
            if (codigo != null && codigo.getCodigoValido().equals("Y") && codigo.getCodigoExpirado().equals("N")) {
                view.registerDevice();
            }
            if (codigo != null && codigo.getCodigoValido().equals("Y") && codigo.getCodigoExpirado().equals("Y")) {
                view.showBoxCodeFields();
                view.showTextError("El código ha caducado");
            }
            if (codigo != null && codigo.getCodigoValido().equals("N")) {
                view.showBoxCodeFields();
                view.showTextError("Codigo erróneo");
            }
        }catch(Exception ex){
            view.showBoxCodeFields();
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public <T> void onSuccessRegisterDevice(Response<BaseResponse<T>> response) {
        try {
            ResponseRegistrarDispositivo registrarDispositivo = (ResponseRegistrarDispositivo) response.body().getResultado();
            view.resultRegisterDevice(registrarDispositivo.getIdRegistroDispositivo());
        }catch(Exception ex){
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.showExpiredToken(response.body().getErrorToken());
    }

    @Override
    public <T> void onError(Response<BaseResponse<T>> response) {
        if(response != null){
            view.showDataFetchError("Lo sentimos", response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
    }

}
