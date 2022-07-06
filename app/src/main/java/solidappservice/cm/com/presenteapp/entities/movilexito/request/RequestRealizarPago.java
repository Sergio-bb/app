package solidappservice.cm.com.presenteapp.entities.movilexito.request;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class RequestRealizarPago extends BaseRequest {

    private String idPaquete;
    private String idTransaccionPresente;
    private String idTransaccionME;
    private String lineaMovil;
    private String numeroCuenta;
    private double valorCompra;
    private String origen;

    public RequestRealizarPago() {
    }

    public RequestRealizarPago(String cedula, String token, String idPaquete, String idTransaccionPresente, String idTransaccionME, String lineaMovil, String numeroCuenta, double valorCompra, String origen) {
        super(cedula, token);
        this.idPaquete = idPaquete;
        this.idTransaccionPresente = idTransaccionPresente;
        this.idTransaccionME = idTransaccionME;
        this.lineaMovil = lineaMovil;
        this.numeroCuenta = numeroCuenta;
        this.valorCompra = valorCompra;
        this.origen = origen;
    }

    public String getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(String idPaquete) {
        this.idPaquete = idPaquete;
    }

    public String getIdTransaccionPresente() {
        return idTransaccionPresente;
    }

    public void setIdTransaccionPresente(String idTransaccionPresente) {
        this.idTransaccionPresente = idTransaccionPresente;
    }

    public String getIdTransaccionME() {
        return idTransaccionME;
    }

    public void setIdTransaccionME(String idTransaccionME) {
        this.idTransaccionME = idTransaccionME;
    }

    public String getLineaMovil() {
        return lineaMovil;
    }

    public void setLineaMovil(String lineaMovil) {
        this.lineaMovil = lineaMovil;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public double getValorCompra() {
        return valorCompra;
    }

    public void setValorCompra(double valorCompra) {
        this.valorCompra = valorCompra;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }
}
