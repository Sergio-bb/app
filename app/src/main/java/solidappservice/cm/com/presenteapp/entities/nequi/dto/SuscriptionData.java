package solidappservice.cm.com.presenteapp.entities.nequi.dto;

public class SuscriptionData {

    private String tokenSuscripcion;
    private String documentoSuscripcion;
    private String telefonoSuscripcion;
    private String fechaCreacion;

    public SuscriptionData() {
    }

    public SuscriptionData(String tokenSuscripcion, String documentoSuscripcion, String telefonoSuscripcion, String fechaCreacion) {
        this.tokenSuscripcion = tokenSuscripcion;
        this.documentoSuscripcion = documentoSuscripcion;
        this.telefonoSuscripcion = telefonoSuscripcion;
        this.fechaCreacion = fechaCreacion;
    }

    public String getTokenSuscripcion() {
        return tokenSuscripcion;
    }

    public void setTokenSuscripcion(String tokenSuscripcion) {
        this.tokenSuscripcion = tokenSuscripcion;
    }

    public String getDocumentoSuscripcion() {
        return documentoSuscripcion;
    }

    public void setDocumentoSuscripcion(String documentoSuscripcion) {
        this.documentoSuscripcion = documentoSuscripcion;
    }

    public String getTelefonoSuscripcion() {
        return telefonoSuscripcion;
    }

    public void setTelefonoSuscripcion(String telefonoSuscripcion) {
        this.telefonoSuscripcion = telefonoSuscripcion;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}