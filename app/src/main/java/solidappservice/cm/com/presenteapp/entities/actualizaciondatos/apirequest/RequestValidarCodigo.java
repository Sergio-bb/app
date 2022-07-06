package solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class RequestValidarCodigo extends BaseRequest {

    private String idCodigoEnviado;
    private String codigoIngresado;

    public RequestValidarCodigo() {
    }

    public RequestValidarCodigo(String cedula, String token, String idCodigoEnviado, String codigoIngresado) {
        this.cedula = cedula;
        this.token = token;
        this.idCodigoEnviado = idCodigoEnviado;
        this.codigoIngresado = codigoIngresado;
    }

    public String getIdCodigoEnviado() {
        return this.idCodigoEnviado;
    }

    public void setIdCodigoEnviado(String idCodigoEnviado) {
        this.idCodigoEnviado = idCodigoEnviado;
    }

    public String getCodigoIngresado() {
        return this.codigoIngresado;
    }

    public void setCodigoIngresado(String codigoIngresado) {
        this.codigoIngresado = codigoIngresado;
    }

}
