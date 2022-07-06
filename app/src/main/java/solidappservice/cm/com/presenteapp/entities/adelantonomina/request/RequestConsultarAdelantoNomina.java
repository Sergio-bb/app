package solidappservice.cm.com.presenteapp.entities.adelantonomina.request;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class RequestConsultarAdelantoNomina extends BaseRequest {

    private Integer v_k_flujo;

    public RequestConsultarAdelantoNomina() {
    }

    public RequestConsultarAdelantoNomina(String cedula, String token, Integer v_k_flujo) {
        super(cedula, token);
        this.v_k_flujo = v_k_flujo;
    }

    public Integer getV_k_flujo() {
        return v_k_flujo;
    }

    public void setV_k_flujo(Integer v_k_flujo) {
        this.v_k_flujo = v_k_flujo;
    }
}
