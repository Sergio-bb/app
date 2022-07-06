package solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse;

import java.util.Date;

public class ResponseEnviarCodigo {

    private String idCodigo; // k_codigo
    private String codigo; // n_codigo
    private Long fechaGeneracion; // f_generacion
    private Long fechaExpiracion; // f_expiracion

    public ResponseEnviarCodigo() {
    }

    public ResponseEnviarCodigo(String idCodigo, String codigo, Long fechaGeneracion, Long fechaExpiracion) {
        this.idCodigo = idCodigo;
        this.codigo = codigo;
        this.fechaGeneracion = fechaGeneracion;
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getIdCodigo() {
        return this.idCodigo;
    }

    public void setIdCodigo(String idCodigo) {
        this.idCodigo = idCodigo;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Long getFechaGeneracion() {
        return this.fechaGeneracion;
    }

    public void setFechaGeneracion(Long fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public Long getFechaExpiracion() {
        return this.fechaExpiracion;
    }

    public void setFechaExpiracion(Long fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

}
