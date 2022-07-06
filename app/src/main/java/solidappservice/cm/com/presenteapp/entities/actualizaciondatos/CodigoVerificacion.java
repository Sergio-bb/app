package solidappservice.cm.com.presenteapp.entities.actualizaciondatos;

import java.util.Date;

public class CodigoVerificacion {

    private String idCodigo;
    private String codigo;
    private Date fechaGeneracion;
    private Date fechaExpiracion;
    private String codigoValido;
    private String codigoExpirado;

    public CodigoVerificacion() {
    }

    public CodigoVerificacion(String idCodigo, String codigo, Date fechaGeneracion, Date fechaExpiracion) {
        this.idCodigo = idCodigo;
        this.codigo = codigo;
        this.fechaGeneracion = fechaGeneracion;
        this.fechaExpiracion = fechaExpiracion;
    }

    public CodigoVerificacion(String codigoValido, String codigoExpirado) {
        this.codigoValido = codigoValido;
        this.codigoExpirado = codigoExpirado;
    }

    public String getIdCodigo() {
        return idCodigo;
    }

    public void setIdCodigo(String idCodigo) {
        this.idCodigo = idCodigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public Date getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Date fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getCodigoValido() {
        return codigoValido;
    }

    public void setCodigoValido(String codigoValido) {
        this.codigoValido = codigoValido;
    }

    public String getCodigoExpirado() {
        return codigoExpirado;
    }

    public void setCodigoExpirado(String codigoExpirado) {
        this.codigoExpirado = codigoExpirado;
    }
}
