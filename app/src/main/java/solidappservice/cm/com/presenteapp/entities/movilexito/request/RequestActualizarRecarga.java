    package solidappservice.cm.com.presenteapp.entities.movilexito.request;

public class RequestActualizarRecarga {

    private String idTransaccionPresente;
    private String resultadoMovilExito;

    public RequestActualizarRecarga() {
    }

    public RequestActualizarRecarga(String idTransaccionPresente, String resultadoMovilExito) {
        this.idTransaccionPresente = idTransaccionPresente;
        this.resultadoMovilExito = resultadoMovilExito;
    }

    public String getIdTransaccionPresente() {
        return idTransaccionPresente;
    }

    public void setIdTransaccionPresente(String idTransaccionPresente) {
        this.idTransaccionPresente = idTransaccionPresente;
    }

    public String getResultadoMovilExito() {
        return resultadoMovilExito;
    }

    public void setResultadoMovilExito(String resultadoMovilExito) {
        this.resultadoMovilExito = resultadoMovilExito;
    }
}
