package solidappservice.cm.com.presenteapp.entities.adelantonomina.request;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class RequestNoCumple extends BaseRequest {

    private String k_identificador;
    private String n_requisito;

    public RequestNoCumple() {
    }

    public RequestNoCumple(String cedula, String token, String k_identificador, String n_requisito) {
        super(cedula, token);
        this.k_identificador = k_identificador;
        this.n_requisito = n_requisito;
    }

    public String getK_identificador() {
        return k_identificador;
    }

    public void setK_identificador(String k_identificador) {
        this.k_identificador = k_identificador;
    }

    public String getN_requisito() {
        return n_requisito;
    }

    public void setN_requisito(String n_requisito) {
        this.n_requisito = n_requisito;
    }
}
