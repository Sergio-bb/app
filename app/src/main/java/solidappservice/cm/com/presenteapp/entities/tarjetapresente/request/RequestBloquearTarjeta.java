package solidappservice.cm.com.presenteapp.entities.tarjetapresente.request;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class RequestBloquearTarjeta extends BaseRequest {

    private String numeroTarjeta;
    private String motivo;
    private String estado;
    private String id_dispositivo;

    public RequestBloquearTarjeta() {
    }

    public RequestBloquearTarjeta(String cedula, String token, String numeroTarjeta, String motivo, String estado, String id_dispositivo) {
        super(cedula, token);
        this.numeroTarjeta = numeroTarjeta;
        this.motivo = motivo;
        this.estado = estado;
        this.id_dispositivo = id_dispositivo;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getId_dispositivo() {
        return id_dispositivo;
    }

    public void setId_dispositivo(String id_dispositivo) {
        this.id_dispositivo = id_dispositivo;
    }
}
