package solidappservice.cm.com.presenteapp.entities.adelantonomina.response;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 04/07/2020.
 */

public class ResponseTopes {

    private String f_corte;
    private String n_aanumnit;
    private int v_cupo;
    private int v_minimo;
    private int v_maximo;

    public ResponseTopes() {
    }

    public ResponseTopes(String f_corte, String n_aanumnit, int v_cupo, int v_minimo, int v_maximo) {
        this.f_corte = f_corte;
        this.n_aanumnit = n_aanumnit;
        this.v_cupo = v_cupo;
        this.v_minimo = v_minimo;
        this.v_maximo = v_maximo;
    }

    public String getF_corte() {
        return f_corte;
    }

    public void setF_corte(String f_corte) {
        this.f_corte = f_corte;
    }

    public String getN_aanumnit() {
        return n_aanumnit;
    }

    public void setN_aanumnit(String n_aanumnit) {
        this.n_aanumnit = n_aanumnit;
    }

    public int getV_cupo() {
        return v_cupo;
    }

    public void setV_cupo(int v_cupo) {
        this.v_cupo = v_cupo;
    }

    public int getV_minimo() {
        return v_minimo;
    }

    public void setV_minimo(int v_minimo) {
        this.v_minimo = v_minimo;
    }

    public int getV_maximo() {
        return v_maximo;
    }

    public void setV_maximo(int v_maximo) {
        this.v_maximo = v_maximo;
    }
}