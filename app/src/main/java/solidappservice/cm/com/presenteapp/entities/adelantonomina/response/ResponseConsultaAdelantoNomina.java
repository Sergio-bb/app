package solidappservice.cm.com.presenteapp.entities.adelantonomina.response;

public class ResponseConsultaAdelantoNomina {

    private String v_k_flujo;
    private String a_numnit;
    private String f_solici;
    private String v_solici;
    private String k_ano;
    private String k_sucurs;
    private String k_tipdoc;
    private String f_primera;
    private String k_numdoc;
    private String a_tipodr;
    private String a_obliga;
    private String n_resultado;
    private String n_error;

    public ResponseConsultaAdelantoNomina() {
    }

    public ResponseConsultaAdelantoNomina(String v_k_flujo, String a_numnit, String f_solici, String v_solici, String k_ano, String k_sucurs, String k_tipdoc, String f_primera, String k_numdoc, String a_tipodr, String a_obliga, String n_resultado, String n_error) {
        this.v_k_flujo = v_k_flujo;
        this.a_numnit = a_numnit;
        this.f_solici = f_solici;
        this.v_solici = v_solici;
        this.k_ano = k_ano;
        this.k_sucurs = k_sucurs;
        this.k_tipdoc = k_tipdoc;
        this.f_primera = f_primera;
        this.k_numdoc = k_numdoc;
        this.a_tipodr = a_tipodr;
        this.a_obliga = a_obliga;
        this.n_resultado = n_resultado;
        this.n_error = n_error;
    }

    public String getV_k_flujo() {
        return v_k_flujo;
    }

    public void setV_k_flujo(String v_k_flujo) {
        this.v_k_flujo = v_k_flujo;
    }

    public String getA_numnit() {
        return a_numnit;
    }

    public void setA_numnit(String a_numnit) {
        this.a_numnit = a_numnit;
    }

    public String getF_solici() {
        return f_solici;
    }

    public void setF_solici(String f_solici) {
        this.f_solici = f_solici;
    }

    public String getV_solici() {
        return v_solici;
    }

    public void setV_solici(String v_solici) {
        this.v_solici = v_solici;
    }

    public String getK_ano() {
        return k_ano;
    }

    public void setK_ano(String k_ano) {
        this.k_ano = k_ano;
    }

    public String getK_sucurs() {
        return k_sucurs;
    }

    public void setK_sucurs(String k_sucurs) {
        this.k_sucurs = k_sucurs;
    }

    public String getK_tipdoc() {
        return k_tipdoc;
    }

    public void setK_tipdoc(String k_tipdoc) {
        this.k_tipdoc = k_tipdoc;
    }

    public String getF_primera() {
        return f_primera;
    }

    public void setF_primera(String f_primera) {
        this.f_primera = f_primera;
    }

    public String getK_numdoc() {
        return k_numdoc;
    }

    public void setK_numdoc(String k_numdoc) {
        this.k_numdoc = k_numdoc;
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

    public String getN_resultado() {
        return n_resultado;
    }

    public void setN_resultado(String n_resultado) {
        this.n_resultado = n_resultado;
    }

    public String getN_error() {
        return n_error;
    }

    public void setN_error(String n_error) {
        this.n_error = n_error;
    }
}

