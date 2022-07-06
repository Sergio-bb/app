package solidappservice.cm.com.presenteapp.entities.adelantonomina.response;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 07/07/2020.
 */

public class ResponseNoCumple {

    private String k_aannumnit;
    private String n_requisito;
    private String n_cumple;
    private String n_observacion;
    private String k_identificador;
    private String f_evaluacion;

    public ResponseNoCumple() {
    }

    public ResponseNoCumple(String k_aannumnit, String n_requisito, String n_cumple, String n_observacion, String k_identificador, String f_evaluacion) {
        this.k_aannumnit = k_aannumnit;
        this.n_requisito = n_requisito;
        this.n_cumple = n_cumple;
        this.n_observacion = n_observacion;
        this.k_identificador = k_identificador;
        this.f_evaluacion = f_evaluacion;
    }

    public String getK_aannumnit() {
        return k_aannumnit;
    }

    public void setK_aannumnit(String k_aannumnit) {
        this.k_aannumnit = k_aannumnit;
    }

    public String getN_requisito() {
        return n_requisito;
    }

    public void setN_requisito(String n_requisito) {
        this.n_requisito = n_requisito;
    }

    public String getN_cumple() {
        return n_cumple;
    }

    public void setN_cumple(String n_cumple) {
        this.n_cumple = n_cumple;
    }

    public String getN_observacion() {
        return n_observacion;
    }

    public void setN_observacion(String n_observacion) {
        this.n_observacion = n_observacion;
    }

    public String getK_identificador() {
        return k_identificador;
    }

    public void setK_identificador(String k_identificador) {
        this.k_identificador = k_identificador;
    }

    public String getF_evaluacion() {
        return f_evaluacion;
    }

    public void setF_evaluacion(String f_evaluacion) {
        this.f_evaluacion = f_evaluacion;
    }
}
