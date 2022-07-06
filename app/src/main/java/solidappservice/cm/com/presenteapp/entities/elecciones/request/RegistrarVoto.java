package solidappservice.cm.com.presenteapp.entities.elecciones.request;

import retrofit2.http.Query;

public class RegistrarVoto {

    private String idZonaElectoral;
    private String idCandidato;
    private String idVotante;

    public RegistrarVoto() {
    }

    public RegistrarVoto(String idZonaElectoral, String idCandidato, String idVotante) {
        this.idZonaElectoral = idZonaElectoral;
        this.idCandidato = idCandidato;
        this.idVotante = idVotante;
    }

    public String getIdZonaElectoral() {
        return idZonaElectoral;
    }

    public void setIdZonaElectoral(String idZonaElectoral) {
        this.idZonaElectoral = idZonaElectoral;
    }

    public String getIdCandidato() {
        return idCandidato;
    }

    public void setIdCandidato(String idCandidato) {
        this.idCandidato = idCandidato;
    }

    public String getIdVotante() {
        return idVotante;
    }

    public void setIdVotante(String idVotante) {
        this.idVotante = idVotante;
    }
}
