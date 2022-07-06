package solidappservice.cm.com.presenteapp.entities.elecciones.response;

import android.text.Html;

/**
 * JORGE ANDRÉS DAVID CARDONA EL 28/12/16.
 */
public class Candidato {

    private String consecutivo;
    private String nombre;
    private String cedula;
    private String votante;
    private String zonaElectoral;

    public Candidato() {
    }

    public Candidato(String consecutivo, String nombre, String cedula) {
        this.consecutivo = consecutivo;
        this.nombre = nombre;
        this.cedula = cedula;
    }

    public Candidato(String votante, String zonaElectoral) {
        this.votante = votante;
        this.zonaElectoral = zonaElectoral;
    }

    public String getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(String consecutivo) {
        this.consecutivo = consecutivo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getVotante() {
        return votante;
    }

    public void setVotante(String votante) {
        this.votante = votante;
    }

    public String getZonaElectoral() {
        return zonaElectoral;
    }

    public void setZonaElectoral(String zonaElectoral) {
        this.zonaElectoral = zonaElectoral;
    }

    @Override
    public String toString() {
        return Html.fromHtml(nombre).toString();
    }
}
