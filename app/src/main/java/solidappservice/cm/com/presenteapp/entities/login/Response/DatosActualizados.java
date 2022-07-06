package solidappservice.cm.com.presenteapp.entities.login.Response;

import java.util.Date;

public class DatosActualizados {

    private String actualizaPrimeraVez;
    private String tieneDatosActualizados;
    private Long fechaUltimaActualizacion;
    private String canalActualizacion;

    public DatosActualizados() {
    }

    public DatosActualizados(String actualizaPrimeraVez, String tieneDatosActualizados, Long fechaUltimaActualizacion, String canalActualizacion) {
        this.actualizaPrimeraVez = actualizaPrimeraVez;
        this.tieneDatosActualizados = tieneDatosActualizados;
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
        this.canalActualizacion = canalActualizacion;
    }

    public String getActualizaPrimeraVez() {
        return actualizaPrimeraVez;
    }

    public void setActualizaPrimeraVez(String actualizaPrimeraVez) {
        this.actualizaPrimeraVez = actualizaPrimeraVez;
    }

    public String getTieneDatosActualizados() {
        return tieneDatosActualizados;
    }

    public void setTieneDatosActualizados(String tieneDatosActualizados) {
        this.tieneDatosActualizados = tieneDatosActualizados;
    }

    public Long getFechaUltimaActualizacion() {
        return fechaUltimaActualizacion;
    }

    public void setFechaUltimaActualizacion(Long fechaUltimaActualizacion) {
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
    }

    public String getCanalActualizacion() {
        return this.canalActualizacion;
    }

    public void setCanalActualizacion(String canalActualizacion) {
        this.canalActualizacion = canalActualizacion;
    }

}
