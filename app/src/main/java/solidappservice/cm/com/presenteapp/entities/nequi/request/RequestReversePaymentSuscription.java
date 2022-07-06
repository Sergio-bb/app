package solidappservice.cm.com.presenteapp.entities.nequi.request;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.nequi.dto.SuscriptionData;

public class RequestReversePaymentSuscription extends BaseRequest {

    private int valorTransaccion;
    private String numeroProductoDebitable;
    private ReversePresente reversePresente;
    private ReverseNequi reverseNequi;
    private SuscriptionData datosSuscripcion;

    public RequestReversePaymentSuscription() {
    }

    public RequestReversePaymentSuscription(String cedula, String token, int valorTransaccion, String numeroProductoDebitable,
                                            ReversePresente reversePresente, ReverseNequi reverseNequi, SuscriptionData datosSuscripcion) {
        super(cedula, token);
        this.valorTransaccion = valorTransaccion;
        this.numeroProductoDebitable = numeroProductoDebitable;
        this.reversePresente = reversePresente;
        this.reverseNequi = reverseNequi;
        this.datosSuscripcion = datosSuscripcion;
    }

    public int getValorTransaccion() {
        return valorTransaccion;
    }

    public void setValorTransaccion(int valorTransaccion) {
        this.valorTransaccion = valorTransaccion;
    }

    public String getNumeroProductoDebitable() {
        return numeroProductoDebitable;
    }

    public void setNumeroProductoDebitable(String numeroProductoDebitable) {
        this.numeroProductoDebitable = numeroProductoDebitable;
    }

    public ReversePresente getReversePresente() {
        return reversePresente;
    }

    public void setReversePresente(ReversePresente reversePresente) {
        this.reversePresente = reversePresente;
    }

    public ReverseNequi getReverseNequi() {
        return reverseNequi;
    }

    public void setReverseNequi(ReverseNequi reverseNequi) {
        this.reverseNequi = reverseNequi;
    }

    public SuscriptionData getDatosSuscripcion() {
        return datosSuscripcion;
    }

    public void setDatosSuscripcion(SuscriptionData datosSuscripcion) {
        this.datosSuscripcion = datosSuscripcion;
    }

    public static class ReversePresente
    {
        private boolean requiredReversePresente;
        private long idTransaccionPresente;
        private String estadoPagoPresente;

        public ReversePresente() {
        }

        public ReversePresente(boolean requiredReversePresente, long idTransaccionPresente, String estadoPagoPresente) {
            this.requiredReversePresente = requiredReversePresente;
            this.idTransaccionPresente = idTransaccionPresente;
            this.estadoPagoPresente = estadoPagoPresente;
        }

        public boolean isRequiredReversePresente() {
            return requiredReversePresente;
        }

        public void setRequiredReversePresente(boolean requiredReversePresente) {
            this.requiredReversePresente = requiredReversePresente;
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
    }

    public static class ReverseNequi
    {
        private boolean requiredReverseNequi;
        private String idTransaccionNequi;
        private String estadoPagoNequi;

        public ReverseNequi() {
        }

        public ReverseNequi(boolean requiredReverseNequi, String idTransaccionNequi, String estadoPagoNequi) {
            this.requiredReverseNequi = requiredReverseNequi;
            this.idTransaccionNequi = idTransaccionNequi;
            this.estadoPagoNequi = estadoPagoNequi;
        }

        public boolean isRequiredReverseNequi() {
            return requiredReverseNequi;
        }

        public void setRequiredReverseNequi(boolean requiredReverseNequi) {
            this.requiredReverseNequi = requiredReverseNequi;
        }

        public String getIdTransaccionNequi() {
            return idTransaccionNequi;
        }

        public void setIdTransaccionNequi(String idTransaccionNequi) {
            this.idTransaccionNequi = idTransaccionNequi;
        }

        public String getEstadoPagoNequi() {
            return estadoPagoNequi;
        }

        public void setEstadoPagoNequi(String estadoPagoNequi) {
            this.estadoPagoNequi = estadoPagoNequi;
        }
    }

}
