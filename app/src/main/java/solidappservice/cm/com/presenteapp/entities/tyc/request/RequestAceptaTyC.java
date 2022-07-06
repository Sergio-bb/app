package solidappservice.cm.com.presenteapp.entities.tyc.request;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class RequestAceptaTyC extends BaseRequest {

    private Integer k_termycond;
    private String f_aceptacion;
    private String i_aceptacion;
    private String ip;

    public RequestAceptaTyC() {
    }

    public RequestAceptaTyC(String cedula, String token, Integer k_termycond, String f_aceptacion, String i_aceptacion, String ip) {
        super(cedula, token);
        this.k_termycond = k_termycond;
        this.f_aceptacion = f_aceptacion;
        this.i_aceptacion = i_aceptacion;
        this.ip = ip;
    }

    public Integer getK_termycond() {
        return k_termycond;
    }

    public void setK_termycond(Integer k_termycond) {
        this.k_termycond = k_termycond;
    }

    public String getF_aceptacion() {
        return f_aceptacion;
    }

    public void setF_aceptacion(String f_aceptacion) {
        this.f_aceptacion = f_aceptacion;
    }

    public String getI_aceptacion() {
        return i_aceptacion;
    }

    public void setI_aceptacion(String i_aceptacion) {
        this.i_aceptacion = i_aceptacion;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
