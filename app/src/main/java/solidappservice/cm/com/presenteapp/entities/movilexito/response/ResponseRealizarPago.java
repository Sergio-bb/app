package solidappservice.cm.com.presenteapp.entities.movilexito.response;

public class ResponseRealizarPago {

    private String idPagoME;
    private String fechaCompra;

    public ResponseRealizarPago() {
    }

    public ResponseRealizarPago(String idPagoME, String fechaCompra) {
        this.setIdPagoME(idPagoME);
        this.fechaCompra = fechaCompra;
    }

    public String getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getIdPagoME() {
        return idPagoME;
    }

    public void setIdPagoME(String idPagoME) {
        this.idPagoME = idPagoME;
    }
}
