package solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class RequestEnviarCodigo extends BaseRequest {

    private Integer tipoEnvio;
    private Integer tipoCodigo;
    private String celular;
    private String correo;

    public RequestEnviarCodigo(){}

    public RequestEnviarCodigo(String cedula, String token, Integer tipoEnvio, String celular, String correo) {
        super();
        this.cedula = cedula;
        this.token = token;
        this.tipoEnvio = tipoEnvio;
        this.celular = celular;
        this.correo = correo;
    }

    public RequestEnviarCodigo(String cedula, String token, Integer tipoEnvio, Integer tipoCodigo, String celular, String correo) {
        this.cedula = cedula;
        this.token = token;
        this.tipoEnvio = tipoEnvio;
        this.tipoCodigo = tipoCodigo;
        this.celular = celular;
        this.correo = correo;
    }

    public RequestEnviarCodigo(String cedula, String token, Integer tipoEnvio, Integer tipoCodigo) {
        super(cedula, token);
        this.tipoEnvio = tipoEnvio;
        this.tipoCodigo = tipoCodigo;
    }

    public Integer getTipoEnvio() {
        return tipoEnvio;
    }

    public void setTipoEnvio(Integer tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
    }

    public Integer getTipoCodigo() {
        return tipoCodigo;
    }

    public void setTipoCodigo(Integer tipoCodigo) {
        this.tipoCodigo = tipoCodigo;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
