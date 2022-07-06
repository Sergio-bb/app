package solidappservice.cm.com.presenteapp.entities.nequi.request;


import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class RequestPaymentDispersion extends BaseRequest {

    private int v_Valor_Transa;
    private String v_a_Numcta;

    public RequestPaymentDispersion() {
    }

    public RequestPaymentDispersion(String cedula, String token, int v_Valor_Transa, String v_a_Numcta) {
        super(cedula, token);
        this.v_Valor_Transa = v_Valor_Transa;
        this.v_a_Numcta = v_a_Numcta;
    }

    public int getV_Valor_Transa() {
        return v_Valor_Transa;
    }

    public void setV_Valor_Transa(int v_Valor_Transa) {
        this.v_Valor_Transa = v_Valor_Transa;
    }

    public String getV_a_Numcta() {
        return v_a_Numcta;
    }

    public void setV_a_Numcta(String v_a_Numcta) {
        this.v_a_Numcta = v_a_Numcta;
    }
}
