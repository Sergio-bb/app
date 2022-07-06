package solidappservice.cm.com.presenteapp.entities.transferencias.response;


/**
 * CREADO POR JORGE ANDRÈS DAVID CARDONA EL 31/10/16.
 */
public class ResponseBanco {

    private String codigo;
    private String nombre;

    public ResponseBanco() {
    }

    public ResponseBanco(String codigo, String nombre) {
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
