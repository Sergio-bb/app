package solidappservice.cm.com.presenteapp.entities.nequi.request;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class RequestPaymentQR extends BaseRequest {

    private String date;
    private String trnId;
    private Object[] originMoney;
    private String name;
    private String ipAddress;
    private Integer value;
    private String status;
    private String documento;
    private String v_a_Numcta;

    public RequestPaymentQR() {
    }

    public RequestPaymentQR(String cedula, String token, String date, String trnId, Object[] originMoney, String name, String ipAddress, Integer value, String status, String documento, String v_a_Numcta) {
        super(cedula, token);
        this.date = date;
        this.trnId = trnId;
        this.originMoney = originMoney;
        this.name = name;
        this.ipAddress = ipAddress;
        this.value = value;
        this.status = status;
        this.documento = documento;
        this.v_a_Numcta = v_a_Numcta;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTrnId() {
        return trnId;
    }

    public void setTrnId(String trnId) {
        this.trnId = trnId;
    }

    public Object[] getOriginMoney() {
        return originMoney;
    }

    public void setOriginMoney(Object[] originMoney) {
        this.originMoney = originMoney;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getV_a_Numcta() {
        return v_a_Numcta;
    }

    public void setV_a_Numcta(String v_a_Numcta) {
        this.v_a_Numcta = v_a_Numcta;
    }
}