package solidappservice.cm.com.presenteapp.entities.adelantonomina.response;

import java.util.Date;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 10/07/2020.
 */

public class ResponseMovimientos {

    private String k_transa;
    private String aanumnit;
    private Date f_solictud;
    private Integer v_solicitado;
    private Integer v_cupo;
    private String i_estado;
    private String n_error;
    private String k_tipodr;
    private String k_numdoc;
    private String v_valorcre;
    private String f_primcuota;
    private Integer k_flujo;
    private String i_aceptacion;
    private String f_aceptacion;
    private String ip;

    public ResponseMovimientos() {
    }

    public ResponseMovimientos(String k_transa, String aanumnit, Date f_solictud, Integer v_solicitado, Integer v_cupo,
               String i_estado, String n_error, String k_tipodr, String k_numdoc, String v_valorcre,
               String f_primcuota, Integer k_flujo, String i_aceptacion, String f_aceptacion, String ip) {
        this.k_transa = k_transa;
        this.aanumnit = aanumnit;
        this.f_solictud = f_solictud;
        this.v_solicitado = v_solicitado;
        this.v_cupo = v_cupo;
        this.i_estado = i_estado;
        this.n_error = n_error;
        this.k_tipodr = k_tipodr;
        this.k_numdoc = k_numdoc;
        this.v_valorcre = v_valorcre;
        this.f_primcuota = f_primcuota;
        this.k_flujo = k_flujo;
        this.i_aceptacion = i_aceptacion;
        this.f_aceptacion = f_aceptacion;
        this.ip = ip;
    }

    public String getK_transa() {
        return k_transa;
    }

    public void setK_transa(String k_transa) {
        this.k_transa = k_transa;
    }

    public String getAanumnit() {
        return aanumnit;
    }

    public void setAanumnit(String aanumnit) {
        this.aanumnit = aanumnit;
    }

    public Date getF_solictud() {
        return f_solictud;
    }

    public void setF_solictud(Date f_solictud) {
        this.f_solictud = f_solictud;
    }

    public Integer getV_solicitado() {
        return v_solicitado;
    }

    public void setV_solicitado(Integer v_solicitado) {
        this.v_solicitado = v_solicitado;
    }

    public Integer getV_cupo() {
        return v_cupo;
    }

    public void setV_cupo(Integer v_cupo) {
        this.v_cupo = v_cupo;
    }

    public String getI_estado() {
        return i_estado;
    }

    public void setI_estado(String i_estado) {
        this.i_estado = i_estado;
    }

    public String getN_error() {
        return n_error;
    }

    public void setN_error(String n_error) {
        this.n_error = n_error;
    }

    public String getK_tipodr() {
        return k_tipodr;
    }

    public void setK_tipodr(String k_tipodr) {
        this.k_tipodr = k_tipodr;
    }

    public String getK_numdoc() {
        return k_numdoc;
    }

    public void setK_numdoc(String k_numdoc) {
        this.k_numdoc = k_numdoc;
    }

    public String getV_valorcre() {
        return v_valorcre;
    }

    public void setV_valorcre(String v_valorcre) {
        this.v_valorcre = v_valorcre;
    }

    public String getF_primcuota() {
        return f_primcuota;
    }

    public void setF_primcuota(String f_primcuota) {
        this.f_primcuota = f_primcuota;
    }

    public Integer getK_flujo() {
        return k_flujo;
    }

    public void setK_flujo(Integer k_flujo) {
        this.k_flujo = k_flujo;
    }

    public String getI_aceptacion() {
        return i_aceptacion;
    }

    public void setI_aceptacion(String i_aceptacion) {
        this.i_aceptacion = i_aceptacion;
    }

    public String getF_aceptacion() {
        return f_aceptacion;
    }

    public void setF_aceptacion(String f_aceptacion) {
        this.f_aceptacion = f_aceptacion;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
