package solidappservice.cm.com.presenteapp.entities.tarjetapresente.response;


import android.text.TextUtils;

public class ResponseTarjeta {

    private String i_estado;
    private String k_numpla; //"636527031362722102",
    private String k_mnumpl;//"6365270313627221",
    private String n_percol;
    private String a_numcta;
    private String a_tipodr;
    private String a_obliga;
    private String f_movim;
    private double v_cupo;

    public ResponseTarjeta() {
    }

    public ResponseTarjeta(String i_estado, String k_numpla, String k_mnumpl, String n_percol, String a_numcta,
                           String a_tipodr, String a_obliga, String f_movim, double v_cupo) {
        this.i_estado = i_estado;
        this.k_numpla = k_numpla;
        this.k_mnumpl = k_mnumpl;
        this.n_percol = n_percol;
        this.a_numcta = a_numcta;
        this.a_tipodr = a_tipodr;
        this.a_obliga = a_obliga;
        this.f_movim = f_movim;
        this.v_cupo = v_cupo;
    }

    public String getI_estado() {
        return i_estado;
    }

    public void setI_estado(String i_estado) {
        this.i_estado = i_estado;
    }

    public String getK_numpla() {
        return k_numpla;
    }

    public void setK_numpla(String k_numpla) {
        this.k_numpla = k_numpla;
    }

    public String getK_mnumpl() {
        return k_mnumpl;
    }

    public void setK_mnumpl(String k_mnumpl) {
        this.k_mnumpl = k_mnumpl;
    }

    public String getN_percol() {
        return n_percol;
    }

    public void setN_percol(String n_percol) {
        this.n_percol = n_percol;
    }

    public String getA_numcta() {
        return a_numcta;
    }

    public void setA_numcta(String a_numcta) {
        this.a_numcta = a_numcta;
    }

    public String getA_tipodr() {
        return a_tipodr;
    }

    public void setA_tipodr(String a_tipodr) {
        this.a_tipodr = a_tipodr;
    }

    public String getA_obliga() {
        return a_obliga;
    }

    public void setA_obliga(String a_obliga) {
        this.a_obliga = a_obliga;
    }

    public String getF_movim() {
        return f_movim;
    }

    public void setF_movim(String f_movim) {
        this.f_movim = f_movim;
    }

    public double getV_cupo() {
        return v_cupo;
    }

    public void setV_cupo(double v_cupo) {
        this.v_cupo = v_cupo;
    }

    @Override
    public String toString() {
        return ocultarNumeroTarjeta();
    }

    public String ocultarNumeroTarjeta(){
        String numeroFinal = "";
        String numero = k_mnumpl;
        if(numero != null && !TextUtils.isEmpty(numero)){
            String ultimosCInco = numero.substring(numero.length() - 5, numero.length());
            char[] chars = numero.toCharArray();
            for(int i=0; i<chars.length; i++){
                if(i < numero.length() - 6) {
                    numeroFinal += "X";
                }else{
                    numeroFinal += ultimosCInco;
                    break;
                }
            }
        }
        return numeroFinal;
    }
}
