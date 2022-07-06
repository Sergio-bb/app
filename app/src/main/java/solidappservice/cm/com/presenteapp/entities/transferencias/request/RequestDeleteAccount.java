package solidappservice.cm.com.presenteapp.entities.transferencias.request;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class RequestDeleteAccount extends BaseRequest {

    private String cedulaInscrita;
    private String numeroCuenta;

    public RequestDeleteAccount() {
    }

    public RequestDeleteAccount(String cedula, String token, String cedulaInscrita, String numeroCuenta) {
        super(cedula, token);
        this.cedulaInscrita = cedulaInscrita;
        this.numeroCuenta = numeroCuenta;
    }

    public String getCedulaInscrita() {
        return cedulaInscrita;
    }

    public void setCedulaInscrita(String cedulaInscrita) {
        this.cedulaInscrita = cedulaInscrita;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }
}
