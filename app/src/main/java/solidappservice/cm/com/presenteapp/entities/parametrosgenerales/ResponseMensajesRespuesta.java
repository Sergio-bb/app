package solidappservice.cm.com.presenteapp.entities.parametrosgenerales;

public class ResponseMensajesRespuesta {

    private Integer idMensaje;
    private String tipoMensaje;
    private String mensaje;

    public ResponseMensajesRespuesta() {
    }

    public ResponseMensajesRespuesta(Integer idMensaje, String tipoMensaje, String mensaje) {
        this.idMensaje = idMensaje;
        this.tipoMensaje = tipoMensaje;
        this.mensaje = mensaje;
    }

    public Integer getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(Integer idMensaje) {
        this.idMensaje = idMensaje;
    }

    public String getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(String tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
