package solidappservice.cm.com.presenteapp.entities.mensajes.request;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class RequestActualizarMensaje extends BaseRequest {

    private String idMensaje;

    public RequestActualizarMensaje() {
    }

    public RequestActualizarMensaje(String cedula, String token, String idMensaje) {
        super(cedula, token);
        this.idMensaje = idMensaje;
    }

    public String getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(String idMensaje) {
        this.idMensaje = idMensaje;
    }
}
