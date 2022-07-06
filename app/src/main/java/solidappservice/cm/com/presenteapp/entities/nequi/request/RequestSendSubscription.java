package solidappservice.cm.com.presenteapp.entities.nequi.request;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class RequestSendSubscription extends BaseRequest {

    private String numTelefono;

    public RequestSendSubscription() {
    }

    public RequestSendSubscription(String cedula, String token, String numTelefono) {
        super(cedula, token);
        this.numTelefono = numTelefono;
    }

    public String getNumTelefono() {
        return numTelefono;
    }

    public void setNumTelefono(String numTelefono) {
        this.numTelefono = numTelefono;
    }
}
