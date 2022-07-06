package solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.dto;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 09/12/2015.
 */
public class FiltroMapa {

    private String i_tipage;
    private String descripcion;

    public FiltroMapa() {
    }

    public FiltroMapa(String i_tipage, String descripcion) {
        this.i_tipage = i_tipage;
        this.descripcion = descripcion;
    }

    public String getI_tipage() {
        return i_tipage;
    }

    public void setI_tipage(String i_tipage) {
        this.i_tipage = i_tipage;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
