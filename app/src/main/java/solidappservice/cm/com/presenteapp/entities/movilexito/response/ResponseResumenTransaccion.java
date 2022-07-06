package solidappservice.cm.com.presenteapp.entities.movilexito.response;

public class ResponseResumenTransaccion {

    private String idTransaccionPresente;
    private String idPaquete;
    private String nombrePaquete;
    private String lineasARecargar;
    private String origen;
    private int valorPaquete;
    private String vigenciaPaquete;
    private String detallePaquete;
    private int valorFinal;
    private String fechaSolicitud;
    private int idTransaccioME;
    private String tipoRecarga;

    public ResponseResumenTransaccion() {
    }

    public ResponseResumenTransaccion(String idTransaccionPresente, String idPaquete, String nombrePaquete, String lineasARecargar, String origen, int valorPaquete, String vigenciaPaquete, String detallePaquete, int valorFinal, String fechaSolicitud, int idTransaccioME, String tipoRecarga) {
        this.idTransaccionPresente = idTransaccionPresente;
        this.idPaquete = idPaquete;
        this.nombrePaquete = nombrePaquete;
        this.lineasARecargar = lineasARecargar;
        this.origen = origen;
        this.valorPaquete = valorPaquete;
        this.vigenciaPaquete = vigenciaPaquete;
        this.detallePaquete = detallePaquete;
        this.valorFinal = valorFinal;
        this.fechaSolicitud = fechaSolicitud;
        this.idTransaccioME = idTransaccioME;
        this.tipoRecarga = tipoRecarga;
    }

    public String getIdTransaccionPresente() {
        return idTransaccionPresente;
    }

    public void setIdTransaccionPresente(String idTransaccionPresente) {
        this.idTransaccionPresente = idTransaccionPresente;
    }

    public String getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(String idPaquete) {
        this.idPaquete = idPaquete;
    }

    public String getNombrePaquete() {
        return nombrePaquete;
    }

    public void setNombrePaquete(String nombrePaquete) {
        this.nombrePaquete = nombrePaquete;
    }

    public String getLineasARecargar() {
        return lineasARecargar;
    }

    public void setLineasARecargar(String lineasARecargar) {
        this.lineasARecargar = lineasARecargar;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public int getValorPaquete() {
        return valorPaquete;
    }

    public void setValorPaquete(int valorPaquete) {
        this.valorPaquete = valorPaquete;
    }

    public String getVigenciaPaquete() {
        return vigenciaPaquete;
    }

    public void setVigenciaPaquete(String vigenciaPaquete) {
        this.vigenciaPaquete = vigenciaPaquete;
    }

    public String getDetallePaquete() {
        return detallePaquete;
    }

    public void setDetallePaquete(String detallePaquete) {
        this.detallePaquete = detallePaquete;
    }

    public int getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(int valorFinal) {
        this.valorFinal = valorFinal;
    }

    public String getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(String fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public int getIdTransaccioME() {
        return idTransaccioME;
    }

    public void setIdTransaccioME(int idTransaccioME) {
        this.idTransaccioME = idTransaccioME;
    }

    public String getTipoRecarga() {
        return tipoRecarga;
    }

    public void setTipoRecarga(String tipoRecarga) {
        this.tipoRecarga = tipoRecarga;
    }
}
