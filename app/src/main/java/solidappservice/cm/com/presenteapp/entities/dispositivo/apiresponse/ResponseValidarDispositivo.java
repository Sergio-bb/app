package solidappservice.cm.com.presenteapp.entities.dispositivo.apiresponse;

public class ResponseValidarDispositivo {

    private String dispositivoRegistrado;
    private String idRegistroDispositivo;

    public ResponseValidarDispositivo() {
    }

    public ResponseValidarDispositivo(String dispositivoRegistrado, String idRegistroDispositivo) {
        this.dispositivoRegistrado = dispositivoRegistrado;
        this.idRegistroDispositivo = idRegistroDispositivo;
    }

    public String getDispositivoRegistrado() {
        return dispositivoRegistrado;
    }

    public void setDispositivoRegistrado(String dispositivoRegistrado) {
        this.dispositivoRegistrado = dispositivoRegistrado;
    }

    public String getIdRegistroDispositivo() {
        return idRegistroDispositivo;
    }

    public void setIdRegistroDispositivo(String idRegistroDispositivo) {
        this.idRegistroDispositivo = idRegistroDispositivo;
    }
}
