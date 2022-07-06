package solidappservice.cm.com.presenteapp.entities.nequi.response;

public class ResponsePaymentDispersionError {

    private String v_RegID;
    private String a_AnumNit;
    private String v_a_Numcta;
    private String v_ValorOperacion;
    private String trackingID;
    private String v_ValorTotal;
    private String v_CobroTransa ;
    private String v_UseCobro ;
    private String v_UseCobroIva ;



    public ResponsePaymentDispersionError() {
    }

    public String getV_ValorTotal() {
        return v_ValorTotal;
    }

    public void setV_ValorTotal(String v_ValorTotal) {
        this.v_ValorTotal = v_ValorTotal;
    }

    public String getV_CobroTransa() {
        return v_CobroTransa;
    }

    public void setV_CobroTransa(String v_CobroTransa) {
        this.v_CobroTransa = v_CobroTransa;
    }

    public String getV_UseCobro() {
        return v_UseCobro;
    }

    public void setV_UseCobro(String v_UseCobro) {
        this.v_UseCobro = v_UseCobro;
    }

    public String getV_UseCobroIva() {
        return v_UseCobroIva;
    }

    public void setV_UseCobroIva(String v_UseCobroIva) {
        this.v_UseCobroIva = v_UseCobroIva;
    }

    public ResponsePaymentDispersionError(String v_RegID, String a_AnumNit, String v_a_Numcta, String v_ValorOperacion, String trackingID, String v_ValorTotal, String v_CobroTransa, String v_UseCobro, String v_UseCobroIva) {
        this.v_RegID = v_RegID;
        this.a_AnumNit = a_AnumNit;
        this.v_a_Numcta = v_a_Numcta;
        this.v_ValorOperacion = v_ValorOperacion;
        this.trackingID = trackingID;
        this.v_ValorTotal = v_ValorTotal;
        this.v_CobroTransa = v_CobroTransa;
        this.v_UseCobro = v_UseCobro;
        this.v_UseCobroIva = v_UseCobroIva;
    }

    public String getV_RegID() {
        return v_RegID;
    }

    public void setV_RegID(String v_RegID) {
        this.v_RegID = v_RegID;
    }

    public String getA_AnumNit() {
        return a_AnumNit;
    }

    public void setA_AnumNit(String a_AnumNit) {
        this.a_AnumNit = a_AnumNit;
    }

    public String getV_a_Numcta() {
        return v_a_Numcta;
    }

    public void setV_a_Numcta(String v_a_Numcta) {
        this.v_a_Numcta = v_a_Numcta;
    }

    public String getV_ValorOperacion() {
        return v_ValorOperacion;
    }

    public void setV_ValorOperacion(String v_ValorOperacion) {
        this.v_ValorOperacion = v_ValorOperacion;
    }

    public String getTrackingID() {
        return trackingID;
    }

    public void setTrackingID(String trackingID) {
        this.trackingID = trackingID;
    }
}

