package solidappservice.cm.com.presenteapp.entities.estadocuenta.response;


public class ResponseMovimientoProducto {

    private String a_tipodr;
    private String a_numdoc;
    private String f_movimi;
    private String k_numdoc;
    private String n_tipdoc;
    private double v_valor;

    public ResponseMovimientoProducto() {
    }

    public ResponseMovimientoProducto(String a_tipodr, String a_numdoc, String f_movimi, String k_numdoc, String n_tipdoc, double v_valor) {
        this.a_tipodr = a_tipodr;
        this.a_numdoc = a_numdoc;
        this.f_movimi = f_movimi;
        this.k_numdoc = k_numdoc;
        this.n_tipdoc = n_tipdoc;
        this.v_valor = v_valor;
    }

    public String getA_tipodr() {
        return a_tipodr;
    }

    public void setA_tipodr(String a_tipodr) {
        this.a_tipodr = a_tipodr;
    }

    public String getA_numdoc() {
        return a_numdoc;
    }

    public void setA_numdoc(String a_numdoc) {
        this.a_numdoc = a_numdoc;
    }

    public String getF_movimi() {
        return f_movimi;
    }

    public void setF_movimi(String f_movimi) {
        this.f_movimi = f_movimi;
    }

    public String getK_numdoc() {
        return k_numdoc;
    }

    public void setK_numdoc(String k_numdoc) {
        this.k_numdoc = k_numdoc;
    }

    public String getN_tipdoc() {
        return n_tipdoc;
    }

    public void setN_tipdoc(String n_tipdoc) {
        this.n_tipdoc = n_tipdoc;
    }

    public double getV_valor() {
        return v_valor;
    }

    public void setV_valor(double v_valor) {
        this.v_valor = v_valor;
    }
}
