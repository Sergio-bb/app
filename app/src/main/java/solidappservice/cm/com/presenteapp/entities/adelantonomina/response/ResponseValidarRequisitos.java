package solidappservice.cm.com.presenteapp.entities.adelantonomina.response;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 04/07/2020.
 */

public class ResponseValidarRequisitos {


    private String f_corte;
    private String k_identificador;
    private String cumple;

    public ResponseValidarRequisitos() {
    }

    public ResponseValidarRequisitos(String f_corte, String k_identificador, String cumple) {
        this.f_corte = f_corte;
        this.k_identificador = k_identificador;
        this.cumple = cumple;
    }

    public String getF_corte() {
        return f_corte;
    }

    public void setF_corte(String f_corte) {
        this.f_corte = f_corte;
    }

    public String getK_identificador() {
        return k_identificador;
    }

    public void setK_identificador(String k_identificador) {
        this.k_identificador = k_identificador;
    }

    public String getCumple() {
        return cumple;
    }

    public void setCumple(String cumple) {
        this.cumple = cumple;
    }
}
