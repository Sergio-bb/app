package solidappservice.cm.com.presenteapp.entities.tarjetapresente.request;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class RequestReposicionTarjeta extends BaseRequest {

    private String n_nombr1;
    private String n_nombr2;
    private String n_apell1;
    private String n_apell2;
    private String n_numplastico;
    private String n_ciudad;
    private String k_coddependencia;
    private String n_dependencia;
    private String k_ccostos;
    private String i_estpla;
    private String k_tipotransa;
    private String d_email;
    private String t_telcel;
    private String f_solicitud;
    private String i_depmodificada;

    public RequestReposicionTarjeta() {
    }

    public RequestReposicionTarjeta(String cedula, String token, String n_nombr1, String n_nombr2,
            String n_apell1, String n_apell2, String n_numplastico, String n_ciudad, String k_coddependencia,
            String n_dependencia, String k_ccostos, String i_estpla, String k_tipotransa, String d_email, String t_telcel,
            String f_solicitud, String i_depmodificada) {
        super(cedula, token);
        this.n_nombr1 = n_nombr1;
        this.n_nombr2 = n_nombr2;
        this.n_apell1 = n_apell1;
        this.n_apell2 = n_apell2;
        this.n_numplastico = n_numplastico;
        this.n_ciudad = n_ciudad;
        this.k_coddependencia = k_coddependencia;
        this.n_dependencia = n_dependencia;
        this.k_ccostos = k_ccostos;
        this.i_estpla = i_estpla;
        this.k_tipotransa = k_tipotransa;
        this.d_email = d_email;
        this.t_telcel = t_telcel;
        this.f_solicitud = f_solicitud;
        this.i_depmodificada = i_depmodificada;
    }

    public String getN_nombr1() {
        return n_nombr1;
    }

    public void setN_nombr1(String n_nombr1) {
        this.n_nombr1 = n_nombr1;
    }

    public String getN_nombr2() {
        return n_nombr2;
    }

    public void setN_nombr2(String n_nombr2) {
        this.n_nombr2 = n_nombr2;
    }

    public String getN_apell1() {
        return n_apell1;
    }

    public void setN_apell1(String n_apell1) {
        this.n_apell1 = n_apell1;
    }

    public String getN_apell2() {
        return n_apell2;
    }

    public void setN_apell2(String n_apell2) {
        this.n_apell2 = n_apell2;
    }

    public String getN_numplastico() {
        return n_numplastico;
    }

    public void setN_numplastico(String n_numplastico) {
        this.n_numplastico = n_numplastico;
    }

    public String getN_ciudad() {
        return n_ciudad;
    }

    public void setN_ciudad(String n_ciudad) {
        this.n_ciudad = n_ciudad;
    }

    public String getK_coddependencia() {
        return k_coddependencia;
    }

    public void setK_coddependencia(String k_coddependencia) {
        this.k_coddependencia = k_coddependencia;
    }

    public String getN_dependencia() {
        return n_dependencia;
    }

    public void setN_dependencia(String n_dependencia) {
        this.n_dependencia = n_dependencia;
    }

    public String getK_ccostos() {
        return k_ccostos;
    }

    public void setK_ccostos(String k_ccostos) {
        this.k_ccostos = k_ccostos;
    }

    public String getI_estpla() {
        return i_estpla;
    }

    public void setI_estpla(String i_estpla) {
        this.i_estpla = i_estpla;
    }

    public String getK_tipotransa() {
        return k_tipotransa;
    }

    public void setK_tipotransa(String k_tipotransa) {
        this.k_tipotransa = k_tipotransa;
    }

    public String getD_email() {
        return d_email;
    }

    public void setD_email(String d_email) {
        this.d_email = d_email;
    }

    public String getT_telcel() {
        return t_telcel;
    }

    public void setT_telcel(String t_telcel) {
        this.t_telcel = t_telcel;
    }

    public String getF_solicitud() {
        return f_solicitud;
    }

    public void setF_solicitud(String f_solicitud) {
        this.f_solicitud = f_solicitud;
    }

    public String getI_depmodificada() {
        return i_depmodificada;
    }

    public void setI_depmodificada(String i_depmodificada) {
        this.i_depmodificada = i_depmodificada;
    }
}
