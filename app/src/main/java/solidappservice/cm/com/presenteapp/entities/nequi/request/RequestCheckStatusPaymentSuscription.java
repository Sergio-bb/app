package solidappservice.cm.com.presenteapp.entities.nequi.request;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.nequi.dto.SuscriptionData;

public class RequestCheckStatusPaymentSuscription extends BaseRequest {

    private int valorTransaccion;
    private long idTransaccionPresente;
    private String estadoPagoPresente;
    private String idTransaccionNequi;
    private String idPagoNequi;
    private String estadoPagoNequi;
    private SuscriptionData datosSuscripcion;

    public RequestCheckStatusPaymentSuscription() {
    }

    public RequestCheckStatusPaymentSuscription(String cedula, String token, int valorTransaccion, long idTransaccionPresente, String estadoPagoPresente, String idTransaccionNequi, String idPagoNequi, String estadoPagoNequi, SuscriptionData datosSuscripcion) {
        super(cedula, token);
        this.valorTransaccion = valorTransaccion;
        this.idTransaccionPresente = idTransaccionPresente;
        this.estadoPagoPresente = estadoPagoPresente;
        this.idTransaccionNequi = idTransaccionNequi;
        this.idPagoNequi = idPagoNequi;
        this.estadoPagoNequi = estadoPagoNequi;
        this.datosSuscripcion = datosSuscripcion;
    }

    public int getValorTransaccion() {
        return valorTransaccion;
    }

    public void setValorTransaccion(int valorTransaccion) {
        this.valorTransaccion = valorTransaccion;
    }

    public long getIdTransaccionPresente() {
        return idTransaccionPresente;
    }

    public void setIdTransaccionPresente(long idTransaccionPresente) {
        this.idTransaccionPresente = idTransaccionPresente;
    }

    public String getEstadoPagoPresente() {
        return estadoPagoPresente;
    }

    public void setEstadoPagoPresente(String estadoPagoPresente) {
        this.estadoPagoPresente = estadoPagoPresente;
    }

    public String getIdTransaccionNequi() {
        return idTransaccionNequi;
    }

    public void setIdTransaccionNequi(String idTransaccionNequi) {
        this.idTransaccionNequi = idTransaccionNequi;
    }

    public String getIdPagoNequi() {
        return idPagoNequi;
    }

    public void setIdPagoNequi(String idPagoNequi) {
        this.idPagoNequi = idPagoNequi;
    }

    public String getEstadoPagoNequi() {
        return estadoPagoNequi;
    }

    public void setEstadoPagoNequi(String estadoPagoNequi) {
        this.estadoPagoNequi = estadoPagoNequi;
    }

    public SuscriptionData getDatosSuscripcion() {
        return datosSuscripcion;
    }

    public void setDatosSuscripcion(SuscriptionData datosSuscripcion) {
        this.datosSuscripcion = datosSuscripcion;
    }
}
