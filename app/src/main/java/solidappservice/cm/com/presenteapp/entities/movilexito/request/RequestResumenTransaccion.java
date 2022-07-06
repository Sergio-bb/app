package solidappservice.cm.com.presenteapp.entities.movilexito.request;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class RequestResumenTransaccion extends BaseRequest {

    private String idTransaccionPresente;

    public RequestResumenTransaccion() {
    }

    public RequestResumenTransaccion(String cedula, String token, String idTransaccionPresente) {
        super(cedula, token);
        this.idTransaccionPresente = idTransaccionPresente;
    }

    public String getIdTransaccionPresente() {
        return idTransaccionPresente;
    }

    public void setIdTransaccionPresente(String idTransaccionPresente) {
        this.idTransaccionPresente = idTransaccionPresente;
    }
}
