package solidappservice.cm.com.presenteapp.entities.elecciones.response;

public class ResponseRegistraVoto {

    private String estado;

    public ResponseRegistraVoto() {
    }

    public ResponseRegistraVoto(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
