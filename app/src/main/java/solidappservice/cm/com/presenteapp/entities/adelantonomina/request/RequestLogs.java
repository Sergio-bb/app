package solidappservice.cm.com.presenteapp.entities.adelantonomina.request;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class RequestLogs extends BaseRequest {

    private String n_accion;
    private String n_descr;
    private String f_registro;

    public RequestLogs() {
    }

    public RequestLogs(String cedula, String token, String n_accion, String n_descr, String f_registro) {
        super(cedula, token);
        this.n_accion = n_accion;
        this.n_descr = n_descr;
        this.f_registro = f_registro;
    }

    public String getN_accion() {
        return n_accion;
    }

    public void setN_accion(String n_accion) {
        this.n_accion = n_accion;
    }

    public String getN_descr() {
        return n_descr;
    }

    public void setN_descr(String n_descr) {
        this.n_descr = n_descr;
    }

    public String getF_registro() {
        return f_registro;
    }

    public void setF_registro(String f_registro) {
        this.f_registro = f_registro;
    }
}
