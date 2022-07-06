package solidappservice.cm.com.presenteapp.entities.tyc.response;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 16/07/2020.
 */
public class ReponseTyC {

    private Integer k_termycond;
    private String n_termycond;
    private String f_modificacion;
    private String i_estado;

    public ReponseTyC() {
    }

    public ReponseTyC(Integer k_termycond, String n_termycond, String f_modificacion, String i_estado) {
        this.k_termycond = k_termycond;
        this.n_termycond = n_termycond;
        this.f_modificacion = f_modificacion;
        this.i_estado = i_estado;
    }

    public Integer getK_termycond() {
        return k_termycond;
    }

    public void setK_termycond(Integer k_termycond) {
        this.k_termycond = k_termycond;
    }

    public String getN_termycond() {
        return n_termycond;
    }

    public void setN_termycond(String n_termycond) {
        this.n_termycond = n_termycond;
    }

    public String getF_modificacion() {
        return f_modificacion;
    }

    public void setF_modificacion(String f_modificacion) {
        this.f_modificacion = f_modificacion;
    }

    public String getI_estado() {
        return i_estado;
    }

    public void setI_estado(String i_estado) {
        this.i_estado = i_estado;
    }
}
