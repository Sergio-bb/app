package solidappservice.cm.com.presenteapp.entities.adelantonomina.request;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class RequestInsertarAdelantoNomina extends BaseRequest {

    private String f_solictud;
    private Integer v_solicitado;
    private Integer v_valorcre;
    private Integer v_cupo;
    private String i_estado;
    private String n_error;
    private Integer k_flujo;
    private String i_aceptacion;
    private String f_aceptacion;
    private String ip;

    public RequestInsertarAdelantoNomina() {
    }

    public RequestInsertarAdelantoNomina(String cedula, String token, String f_solicitud, Integer v_solicitado,
                 Integer v_valorcre, Integer v_cupo, String i_estado, String n_error, Integer k_flujo, String i_aceptacion,
                 String f_aceptacion, String ip) {
        super(cedula, token);
        this.f_solictud = f_solicitud;
        this.v_solicitado = v_solicitado;
        this.v_valorcre = v_valorcre;
        this.v_cupo = v_cupo;
        this.i_estado = i_estado;
        this.n_error = n_error;
        this.k_flujo = k_flujo;
        this.i_aceptacion = i_aceptacion;
        this.f_aceptacion = f_aceptacion;
        this.ip = ip;
    }

    public String getF_solictud() {
        return f_solictud;
    }

    public void setF_solictud(String f_solictud) {
        this.f_solictud = f_solictud;
    }

    public Integer getV_solicitado() {
        return v_solicitado;
    }

    public void setV_solicitado(Integer v_solicitado) {
        this.v_solicitado = v_solicitado;
    }

    public Integer getV_valorcre() {
        return v_valorcre;
    }

    public void setV_valorcre(Integer v_valorcre) {
        this.v_valorcre = v_valorcre;
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
