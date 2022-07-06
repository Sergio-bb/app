package solidappservice.cm.com.presenteapp.entities.adelantonomina.response;

public class ResponserSolicitarAdelantoNomina {

    private String n_aanumnit;
    private String v_monto;
    private String f_solici;
    private String n_resultado;
    private String n_error;
    private Integer v_k_flujo;

    public ResponserSolicitarAdelantoNomina() {
    }

    public ResponserSolicitarAdelantoNomina(String n_aanumnit, String v_monto, String f_solici, String n_resultado,
                                            String n_error, Integer v_k_flujo) {
        this.n_aanumnit = n_aanumnit;
        this.v_monto = v_monto;
        this.f_solici = f_solici;
        this.n_resultado = n_resultado;
        this.n_error = n_error;
        this.v_k_flujo = v_k_flujo;
    }

    public String getN_aanumnit() {
        return n_aanumnit;
    }

    public void setN_aanumnit(String n_aanumnit) {
        this.n_aanumnit = n_aanumnit;
    }

    public String getV_monto() {
        return v_monto;
    }

    public void setV_monto(String v_monto) {
        this.v_monto = v_monto;
    }

    public String getF_solici() {
        return f_solici;
    }

    public void setF_solici(String f_solici) {
        this.f_solici = f_solici;
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

    public Integer getV_k_flujo() {
        return v_k_flujo;
    }

    public void setV_k_flujo(Integer v_k_flujo) {
        this.v_k_flujo = v_k_flujo;
    }
}

