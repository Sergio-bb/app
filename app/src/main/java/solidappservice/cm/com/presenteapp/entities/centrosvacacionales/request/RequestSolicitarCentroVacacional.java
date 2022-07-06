package solidappservice.cm.com.presenteapp.entities.centrosvacacionales.request;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class RequestSolicitarCentroVacacional extends BaseRequest {

    private String numeroAdultos;
    private String numeroNinos;
    private String codigo;
    private String codigoPerfil;
    private String fecha;
    private String numeroDias;
    private String observaciones;
    private String idDispositivo;
    private String emailContacto;

    public RequestSolicitarCentroVacacional() {
    }

    public RequestSolicitarCentroVacacional(String cedula, String token, String numeroAdultos, String numeroNinos, String codigo,
                                            String codigoPerfil, String fecha, String numeroDias, String observaciones, String idDispositivo, String emailContacto) {
        super(cedula, token);
        this.numeroAdultos = numeroAdultos;
        this.numeroNinos = numeroNinos;
        this.codigo = codigo;
        this.codigoPerfil = codigoPerfil;
        this.fecha = fecha;
        this.numeroDias = numeroDias;
        this.observaciones = observaciones;
        this.idDispositivo = idDispositivo;
        this.emailContacto = emailContacto;
    }

    public String getNumeroAdultos() {
        return numeroAdultos;
    }

    public void setNumeroAdultos(String numeroAdultos) {
        this.numeroAdultos = numeroAdultos;
    }

    public String getNumeroNinos() {
        return numeroNinos;
    }

    public void setNumeroNinos(String numeroNinos) {
        this.numeroNinos = numeroNinos;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoPerfil() {
        return codigoPerfil;
    }

    public void setCodigoPerfil(String codigoPerfil) {
        this.codigoPerfil = codigoPerfil;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNumeroDias() {
        return numeroDias;
    }

    public void setNumeroDias(String numeroDias) {
        this.numeroDias = numeroDias;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getIdDispositivo() {
        return idDispositivo;
    }

    public void setIdDispositivo(String idDispositivo) {
        this.idDispositivo = idDispositivo;
    }

    public String getEmailContacto() {
        return emailContacto;
    }

    public void setEmailContacto(String emailContacto) {
        this.emailContacto = emailContacto;
    }
}
