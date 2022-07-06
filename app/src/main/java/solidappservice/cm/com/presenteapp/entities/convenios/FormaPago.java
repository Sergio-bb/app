package solidappservice.cm.com.presenteapp.entities.convenios;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 15/08/18.
 */

public class FormaPago {

    private String id;
    private String nombre;
    private String legal;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public String getLegal() {
        return legal;
    }

    public void setLegal(String legal) {
        this.legal = legal;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
