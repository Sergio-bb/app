package solidappservice.cm.com.presenteapp.entities.nequi.request;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class RequestGetDataCommerceQR extends BaseRequest {

    private String qr;

    public RequestGetDataCommerceQR() {
    }

    public RequestGetDataCommerceQR(String cedula, String token, String qr) {
        super(cedula, token);
        this.qr = qr;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }
}
