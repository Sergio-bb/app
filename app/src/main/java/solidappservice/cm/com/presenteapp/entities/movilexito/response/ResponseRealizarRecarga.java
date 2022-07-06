package solidappservice.cm.com.presenteapp.entities.movilexito.response;

public class ResponseRealizarRecarga {

    private int subscripcionId;
    private String transaccionId;
    private String estado;
    private String descripcionError;
    private String timeStamp;

    public ResponseRealizarRecarga() {
    }

    public ResponseRealizarRecarga(int subscripcionId, String transaccionId, String estado, String descripcionError, String timeStamp) {
        this.subscripcionId = subscripcionId;
        this.transaccionId = transaccionId;
        this.estado = estado;
        this.descripcionError = descripcionError;
        this.timeStamp = timeStamp;
    }

    public ResponseRealizarRecarga(String descripcionError) {
        this.descripcionError = descripcionError;
    }

    public int getSubscripcionId() {
        return subscripcionId;
    }

    public void setSubscripcionId(int subscripcionId) {
        this.subscripcionId = subscripcionId;
    }

    public String getTransaccionId() {
        return transaccionId;
    }

    public void setTransaccionId(String transaccionId) {
        this.transaccionId = transaccionId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDescripcionError() {
        return descripcionError;
    }

    public void setDescripcionError(String descripcionError) {
        this.descripcionError = descripcionError;
    }
}
