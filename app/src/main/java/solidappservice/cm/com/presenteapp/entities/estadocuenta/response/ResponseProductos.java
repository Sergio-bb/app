package solidappservice.cm.com.presenteapp.entities.estadocuenta.response;


import java.util.ArrayList;

public class ResponseProductos {

    private String aanumnit;
    private String n_tipodr;
    private String a_tipodr;
    private String k_tipodr;
    private String a_numdoc;
    private double v_saldo;
    private double v_vencid;
    private String f_vencim;
    private String n_produc;
    private String f_actual;
    private int q_diaven;
    private double v_cuota;
    private double v_transf;
    private String i_pagsup;
    private String i_debito;
    private Integer k_tipcuent;
    private ArrayList<ResponseMovimientoProducto> movimientos;
    //Para el manejo del acordeon
    private boolean expanded;

    public ResponseProductos() {
    }

    public ResponseProductos(String aanumnit, String n_tipodr, String a_tipodr, String k_tipodr, String a_numdoc,
                             double v_saldo, double v_vencid, String f_vencim, String n_produc, String f_actual,
                             int q_diaven, double v_cuota, double v_transf, String i_pagsup, String i_debito,
                             Integer k_tipcuent, ArrayList<ResponseMovimientoProducto> movimientos, boolean expanded) {
        this.aanumnit = aanumnit;
        this.n_tipodr = n_tipodr;
        this.a_tipodr = a_tipodr;
        this.k_tipodr = k_tipodr;
        this.a_numdoc = a_numdoc;
        this.v_saldo = v_saldo;
        this.v_vencid = v_vencid;
        this.f_vencim = f_vencim;
        this.n_produc = n_produc;
        this.f_actual = f_actual;
        this.q_diaven = q_diaven;
        this.v_cuota = v_cuota;
        this.v_transf = v_transf;
        this.i_pagsup = i_pagsup;
        this.i_debito = i_debito;
        this.k_tipcuent = k_tipcuent;
        this.movimientos = movimientos;
        this.expanded = expanded;
    }

    public String getAanumnit() {
        return aanumnit;
    }

    public void setAanumnit(String aanumnit) {
        this.aanumnit = aanumnit;
    }

    public String getN_tipodr() {
        return n_tipodr;
    }

    public void setN_tipodr(String n_tipodr) {
        this.n_tipodr = n_tipodr;
    }

    public String getA_tipodr() {
        return a_tipodr;
    }

    public void setA_tipodr(String a_tipodr) {
        this.a_tipodr = a_tipodr;
    }

    public String getK_tipodr() {
        return k_tipodr;
    }

    public void setK_tipodr(String k_tipodr) {
        this.k_tipodr = k_tipodr;
    }

    public String getA_numdoc() {
        return a_numdoc;
    }

    public void setA_numdoc(String a_numdoc) {
        this.a_numdoc = a_numdoc;
    }

    public double getV_saldo() {
        return v_saldo;
    }

    public void setV_saldo(double v_saldo) {
        this.v_saldo = v_saldo;
    }

    public double getV_vencid() {
        return v_vencid;
    }

    public void setV_vencid(double v_vencid) {
        this.v_vencid = v_vencid;
    }

    public String getF_vencim() {
        return f_vencim;
    }

    public void setF_vencim(String f_vencim) {
        this.f_vencim = f_vencim;
    }

    public String getN_produc() {
        return n_produc;
    }

    public void setN_produc(String n_produc) {
        this.n_produc = n_produc;
    }

    public String getF_actual() {
        return f_actual;
    }

    public void setF_actual(String f_actual) {
        this.f_actual = f_actual;
    }

    public int getQ_diaven() {
        return q_diaven;
    }

    public void setQ_diaven(int q_diaven) {
        this.q_diaven = q_diaven;
    }

    public double getV_cuota() {
        return v_cuota;
    }

    public void setV_cuota(double v_cuota) {
        this.v_cuota = v_cuota;
    }

    public double getV_transf() {
        return v_transf;
    }

    public void setV_transf(double v_transf) {
        this.v_transf = v_transf;
    }

    public String getI_pagsup() {
        return i_pagsup;
    }

    public void setI_pagsup(String i_pagsup) {
        this.i_pagsup = i_pagsup;
    }

    public String getI_debito() {
        return i_debito;
    }

    public void setI_debito(String i_debito) {
        this.i_debito = i_debito;
    }

    public Integer getK_tipcuent() {
        return k_tipcuent;
    }

    public void setK_tipcuent(Integer k_tipcuent) {
        this.k_tipcuent = k_tipcuent;
    }

    public ArrayList<ResponseMovimientoProducto> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(ArrayList<ResponseMovimientoProducto> movimientos) {
        this.movimientos = movimientos;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String toString() {
        return n_tipodr + " (" + a_numdoc + ")";
    }
}


