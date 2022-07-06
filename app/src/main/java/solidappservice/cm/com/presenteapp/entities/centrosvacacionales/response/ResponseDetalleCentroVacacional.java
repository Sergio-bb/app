package solidappservice.cm.com.presenteapp.entities.centrosvacacionales.response;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 02/12/2015.
 */
public class ResponseDetalleCentroVacacional {

    private String codigo;
    private String nombre;

    public ResponseDetalleCentroVacacional() {
    }

    public ResponseDetalleCentroVacacional(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
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

    @Override
    public String toString() {
        return nombre;
    }
}
