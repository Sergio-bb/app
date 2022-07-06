package solidappservice.cm.com.presenteapp.entities.pagoobligaciones.request;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class RequestEnviarPago extends BaseRequest {

    private String id_dispositivo;
    private Double v_valor;
    private String k_tipodr;
    private String n_tipodr;
    private String a_tipodr;
    private String a_numdoc;
    private String n_produc;
    private String a_numcta;

    public RequestEnviarPago() {
    }

    public RequestEnviarPago(String cedula, String token, String id_dispositivo, Double v_valor, String k_tipodr,
                             String n_tipodr, String a_tipodr, String a_numdoc, String n_produc, String a_numcta) {
        super(cedula, token);
        this.id_dispositivo = id_dispositivo;
        this.v_valor = v_valor;
        this.k_tipodr = k_tipodr;
        this.n_tipodr = n_tipodr;
        this.a_tipodr = a_tipodr;
        this.a_numdoc = a_numdoc;
        this.n_produc = n_produc;
        this.a_numcta = a_numcta;
    }

    public String getId_dispositivo() {
        return id_dispositivo;
    }

    public void setId_dispositivo(String id_dispositivo) {
        this.id_dispositivo = id_dispositivo;
    }

    public Double getV_valor() {
        return v_valor;
    }

    public void setV_valor(Double v_valor) {
        this.v_valor = v_valor;
    }

    public String getK_tipodr() {
        return k_tipodr;
    }

    public void setK_tipodr(String k_tipodr) {
        this.k_tipodr = k_tipodr;
    }

    public String getN_tipodr() {
        return n_tipodr;
    }

    public void setN_tipodr(String n_tipodr) {
        this.n_tipodr = n_tipodr;
    }

    public String getA_tipodr() {
        return a_tipodr;
    }

    public void setA_tipodr(String a_tipodr) {
        this.a_tipodr = a_tipodr;
    }

    public String getA_numdoc() {
        return a_numdoc;
    }

    public void setA_numdoc(String a_numdoc) {
        this.a_numdoc = a_numdoc;
    }

    public String getN_produc() {
        return n_produc;
    }

    public void setN_produc(String n_produc) {
        this.n_produc = n_produc;
    }

    public String getA_numcta() {
        return a_numcta;
    }

    public void setA_numcta(String a_numcta) {
        this.a_numcta = a_numcta;
    }
}
