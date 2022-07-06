package solidappservice.cm.com.presenteapp.entities.elecciones.response;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 28/12/16.
 */
public class ResponseEstadoVotante {

    private String estado;
    private Votante votante;

    public ResponseEstadoVotante() {
    }

    public ResponseEstadoVotante(String estado, Votante votante) {
        this.estado = estado;
        this.votante = votante;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Votante getVotante() {
        return votante;
    }

    public void setVotante(Votante votante) {
        this.votante = votante;
    }
}
