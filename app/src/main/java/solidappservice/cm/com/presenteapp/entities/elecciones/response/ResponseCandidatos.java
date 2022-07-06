package solidappservice.cm.com.presenteapp.entities.elecciones.response;

import java.util.List;

/**
 * JORGE ANDRÉS DAVID CARDONA EL 28/12/16.
 */
public class ResponseCandidatos {

    private String estado;
    private String menssaje;
    private List<Candidato> candidatos;

    public ResponseCandidatos() {
    }

    public ResponseCandidatos(String estado, String menssaje, List<Candidato> candidatos) {
        this.estado = estado;
        this.menssaje = menssaje;
        this.candidatos = candidatos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMenssaje() {
        return menssaje;
    }

    public void setMenssaje(String menssaje) {
        this.menssaje = menssaje;
    }

    public List<Candidato> getCandidatos() {
        return candidatos;
    }

    public void setCandidatos(List<Candidato> candidatos) {
        this.candidatos = candidatos;
    }
}


