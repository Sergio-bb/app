package solidappservice.cm.com.presenteapp.entities.movilexito.response;

public class ResponseActualizarRecarga {

    private String fechaRespuesta;
    private String idTransaccionPresente;
    private String resultado;

    public ResponseActualizarRecarga() {
    }

    public ResponseActualizarRecarga(String fechaRespuesta, String idTransaccionPresente, String resultado) {
        this.fechaRespuesta = fechaRespuesta;
        this.idTransaccionPresente = idTransaccionPresente;
        this.resultado = resultado;
    }

    public String getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(String fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }

    public String getIdTransaccionPresente() {
        return idTransaccionPresente;
    }

    public void setIdTransaccionPresente(String idTransaccionPresente) {
        this.idTransaccionPresente = idTransaccionPresente;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
