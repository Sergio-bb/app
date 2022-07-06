package solidappservice.cm.com.presenteapp.entities.dispositivo.apiresponse;

public class ResponseRegistrarDispositivo {

    private String idRegistroDispositivo;

    public ResponseRegistrarDispositivo() {
    }

    public ResponseRegistrarDispositivo(String idRegistroDispositivo) {
        this.idRegistroDispositivo = idRegistroDispositivo;
    }

    public String getIdRegistroDispositivo() {
        return this.idRegistroDispositivo;
    }

    public void setIdRegistroDispositivo(String idRegistroDispositivo) {
        this.idRegistroDispositivo = idRegistroDispositivo;
    }

}
