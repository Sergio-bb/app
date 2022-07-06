package solidappservice.cm.com.presenteapp.entities.mensajesbanner;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 28/11/2015.
 */
public class ResponseMensajesBanner {

    private int k_idmens;
    private String n_mensaje;

    public ResponseMensajesBanner() {
    }

    public ResponseMensajesBanner(int k_idmens, String n_mensaje) {
        this.k_idmens = k_idmens;
        this.n_mensaje = n_mensaje;
    }

    public int getK_idmens() {
        return k_idmens;
    }

    public void setK_idmens(int k_idmens) {
        this.k_idmens = k_idmens;
    }

    public String getN_mensaje() {
        return n_mensaje;
    }

    public void setN_mensaje(String n_mensaje) {
        this.n_mensaje = n_mensaje;
    }
}
