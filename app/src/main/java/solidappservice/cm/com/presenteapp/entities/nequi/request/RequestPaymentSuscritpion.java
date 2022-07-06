package solidappservice.cm.com.presenteapp.entities.nequi.request;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.nequi.dto.SuscriptionData;

public class RequestPaymentSuscritpion extends BaseRequest {

    private Integer valorTransaccion;
    private String numeroProductoDebitable;
    private SuscriptionData datosSuscripcion;

    public RequestPaymentSuscritpion() {
    }

    public RequestPaymentSuscritpion(String cedula, String token, Integer valorTransaccion, String numeroProductoDebitable, SuscriptionData datosSuscripcion) {
        super(cedula, token);
        this.valorTransaccion = valorTransaccion;
        this.numeroProductoDebitable = numeroProductoDebitable;
        this.datosSuscripcion = datosSuscripcion;
    }

    public Integer getValorTransaccion() {
        return valorTransaccion;
    }

    public void setValorTransaccion(Integer valorTransaccion) {
        this.valorTransaccion = valorTransaccion;
    }

    public String getNumeroProductoDebitable() {
        return numeroProductoDebitable;
    }

    public void setNumeroProductoDebitable(String numeroProductoDebitable) {
        this.numeroProductoDebitable = numeroProductoDebitable;
    }

    public SuscriptionData getDatosSuscripcion() {
        return datosSuscripcion;
    }

    public void setDatosSuscripcion(SuscriptionData datosSuscripcion) {
        this.datosSuscripcion = datosSuscripcion;
    }

}