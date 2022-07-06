package solidappservice.cm.com.presenteapp.entities.transferencias.request;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class RequestMakeTransfer extends BaseRequest {

    private String id_dispositivo;
    private String aanumnit_o;
    private String a_numdoc;
    private Double v_valor;
    private String k_idterc;
    private String k_idterc_tit;
    private String n_numcta;
    private Long f_solici;

    public RequestMakeTransfer() {
    }

    public RequestMakeTransfer(String cedula, String token, String id_dispositivo, String aanumnit_o,
                               String a_numdoc, Double v_valor, String k_idterc, String k_idterc_tit,
                               String n_numcta, Long f_solici) {
        super(cedula, token);
        this.id_dispositivo = id_dispositivo;
        this.aanumnit_o = aanumnit_o;
        this.a_numdoc = a_numdoc;
        this.v_valor = v_valor;
        this.k_idterc = k_idterc;
        this.k_idterc_tit = k_idterc_tit;
        this.n_numcta = n_numcta;
        this.f_solici = f_solici;
    }

    public String getId_dispositivo() {
        return id_dispositivo;
    }

    public void setId_dispositivo(String id_dispositivo) {
        this.id_dispositivo = id_dispositivo;
    }

    public String getAanumnit_o() {
        return aanumnit_o;
    }

    public void setAanumnit_o(String aanumnit_o) {
        this.aanumnit_o = aanumnit_o;
    }

    public String getA_numdoc() {
        return a_numdoc;
    }

    public void setA_numdoc(String a_numdoc) {
        this.a_numdoc = a_numdoc;
    }

    public Double getV_valor() {
        return v_valor;
    }

    public void setV_valor(Double v_valor) {
        this.v_valor = v_valor;
    }

    public String getK_idterc() {
        return k_idterc;
    }

    public void setK_idterc(String k_idterc) {
        this.k_idterc = k_idterc;
    }

    public String getK_idterc_tit() {
        return k_idterc_tit;
    }

    public void setK_idterc_tit(String k_idterc_tit) {
        this.k_idterc_tit = k_idterc_tit;
    }

    public String getN_numcta() {
        return n_numcta;
    }

    public void setN_numcta(String n_numcta) {
        this.n_numcta = n_numcta;
    }

    public Long getF_solici() {
        return f_solici;
    }

    public void setF_solici(Long f_solici) {
        this.f_solici = f_solici;
    }
}
