package solidappservice.cm.com.presenteapp.entities.movilexito.request;

public class RequestRealizarRecargar {

    private String transaccionId;
    private String id;
    private String tipoSuscripcion;
    private String adicionalesSuscripcion;
    private String plu;
    private String canalVenta;
    private String idSuscriptionOptiva;
    private String msisdn;
    private Boolean esRecurrente;
    private String tipoDocumento;
    private String documento;
    private String FechaCompra;

    public RequestRealizarRecargar() {
    }

    public RequestRealizarRecargar(String transaccionId, String id, String tipoSuscripcion, String adicionalesSuscripcion, String plu, String canalVenta, String idSuscriptionOptiva, String msisdn, Boolean esRecurrente, String tipoDocumento, String documento, String fechaCompra) {
        this.transaccionId = transaccionId;
        this.id = id;
        this.tipoSuscripcion = tipoSuscripcion;
        this.adicionalesSuscripcion = adicionalesSuscripcion;
        this.plu = plu;
        this.canalVenta = canalVenta;
        this.idSuscriptionOptiva = idSuscriptionOptiva;
        this.msisdn = msisdn;
        this.esRecurrente = esRecurrente;
        this.tipoDocumento = tipoDocumento;
        this.documento = documento;
        FechaCompra = fechaCompra;
    }

    public String getTransaccionId() {
        return transaccionId;
    }

    public void setTransaccionId(String transaccionId) {
        this.transaccionId = transaccionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipoSuscripcion() {
        return tipoSuscripcion;
    }

    public void setTipoSuscripcion(String tipoSuscripcion) {
        this.tipoSuscripcion = tipoSuscripcion;
    }

    public String getAdicionalesSuscripcion() {
        return adicionalesSuscripcion;
    }

    public void setAdicionalesSuscripcion(String adicionalesSuscripcion) {
        this.adicionalesSuscripcion = adicionalesSuscripcion;
    }

    public String getPlu() {
        return plu;
    }

    public void setPlu(String plu) {
        this.plu = plu;
    }

    public String getCanalVenta() {
        return canalVenta;
    }

    public void setCanalVenta(String canalVenta) {
        this.canalVenta = canalVenta;
    }

    public String getIdSuscriptionOptiva() {
        return idSuscriptionOptiva;
    }

    public void setIdSuscriptionOptiva(String idSuscriptionOptiva) {
        this.idSuscriptionOptiva = idSuscriptionOptiva;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Boolean getEsRecurrente() {
        return esRecurrente;
    }

    public void setEsRecurrente(Boolean esRecurrente) {
        this.esRecurrente = esRecurrente;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getFechaCompra() {
        return FechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
        FechaCompra = fechaCompra;
    }
}
