package solidappservice.cm.com.presenteapp.entities.solicitudahorros.response;

import java.util.ArrayList;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 27/11/2015.
 */
public class ResponseTiposAhorro {

    private String k_tipodr;
    private String n_tipodr;
    private double v_cuota_min;
    private double v_cuota_max;
    private int v_plazo_min;
    private int v_plazo_max;
    private String i_fecha;
    private String i_observ;
    private String f_vigenci_fin;
    private ArrayList<String> descripciones;

    public ResponseTiposAhorro() {
    }

    public ResponseTiposAhorro(String k_tipodr, String n_tipodr, double v_cuota_min, double v_cuota_max, int v_plazo_min,
                               int v_plazo_max, String i_fecha, String i_observ, String f_vigenci_fin, ArrayList<String> descripciones) {
        this.k_tipodr = k_tipodr;
        this.n_tipodr = n_tipodr;
        this.v_cuota_min = v_cuota_min;
        this.v_cuota_max = v_cuota_max;
        this.v_plazo_min = v_plazo_min;
        this.v_plazo_max = v_plazo_max;
        this.i_fecha = i_fecha;
        this.i_observ = i_observ;
        this.f_vigenci_fin = f_vigenci_fin;
        this.descripciones = descripciones;
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

    public double getV_cuota_min() {
        return v_cuota_min;
    }

    public void setV_cuota_min(double v_cuota_min) {
        this.v_cuota_min = v_cuota_min;
    }

    public double getV_cuota_max() {
        return v_cuota_max;
    }

    public void setV_cuota_max(double v_cuota_max) {
        this.v_cuota_max = v_cuota_max;
    }

    public int getV_plazo_min() {
        return v_plazo_min;
    }

    public void setV_plazo_min(int v_plazo_min) {
        this.v_plazo_min = v_plazo_min;
    }

    public int getV_plazo_max() {
        return v_plazo_max;
    }

    public void setV_plazo_max(int v_plazo_max) {
        this.v_plazo_max = v_plazo_max;
    }

    public String getI_fecha() {
        return i_fecha;
    }

    public void setI_fecha(String i_fecha) {
        this.i_fecha = i_fecha;
    }

    public String getI_observ() {
        return i_observ;
    }

    public void setI_observ(String i_observ) {
        this.i_observ = i_observ;
    }

    public String getF_vigenci_fin() {
        return f_vigenci_fin;
    }

    public void setF_vigenci_fin(String f_vigenci_fin) {
        this.f_vigenci_fin = f_vigenci_fin;
    }

    public ArrayList<String> getDescripciones() {
        return descripciones;
    }

    public void setDescripciones(ArrayList<String> descripciones) {
        this.descripciones = descripciones;
    }

    @Override
    public String toString() {
        return n_tipodr;
    }
}
