package solidappservice.cm.com.presenteapp.entities.elecciones.response;

public class Votante {

    private String voto;
    private String ZonaElectoral;
    private String fechaVoto;
    private String Nombre;

    public Votante() {
    }

    public Votante(String voto, String zonaElectoral, String fechaVoto, String nombre) {
        this.voto = voto;
        ZonaElectoral = zonaElectoral;
        this.fechaVoto = fechaVoto;
        Nombre = nombre;
    }

    public String getVoto() {
        return voto;
    }

    public void setVoto(String voto) {
        this.voto = voto;
    }

    public String getZonaElectoral() {
        return ZonaElectoral;
    }

    public void setZonaElectoral(String zonaElectoral) {
        ZonaElectoral = zonaElectoral;
    }

    public String getFechaVoto() {
        return fechaVoto;
    }

    public void setFechaVoto(String fechaVoto) {
        this.fechaVoto = fechaVoto;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}
