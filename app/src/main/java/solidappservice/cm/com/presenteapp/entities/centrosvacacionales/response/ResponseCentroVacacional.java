package solidappservice.cm.com.presenteapp.entities.centrosvacacionales.response;

import java.util.ArrayList;
import java.util.List;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 01/12/2015.
 */
public class ResponseCentroVacacional {

    private String codigo;
    private String nombre;
    private String direccion;
    private List<ResponseDetalleCentroVacacional> perfiles;

    public ResponseCentroVacacional() {
    }

    public ResponseCentroVacacional(String codigo, String nombre, String direccion, ArrayList<ResponseDetalleCentroVacacional> perfiles) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.direccion = direccion;
        this.perfiles = perfiles;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<ResponseDetalleCentroVacacional> getPerfiles() {
        return perfiles;
    }

    public void setPerfiles(List<ResponseDetalleCentroVacacional> perfiles) {
        this.perfiles = perfiles;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
