package solidappservice.cm.com.presenteapp.entities.solicitudahorros.request;

import android.view.View;

import org.json.JSONObject;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class RequestEnviarSolicitudAhorro extends BaseRequest {

    private String v_cuota;
    private String k_tipodr;
    private Integer v_plazo;
    private String f_vigenci_fin;
    private String n_destin;
    private String id_dispositivo;

    public RequestEnviarSolicitudAhorro() {
    }

    public RequestEnviarSolicitudAhorro(String cedula, String token, String v_cuota, String k_tipodr, Integer v_plazo,
                                        String f_vigenci_fin, String n_destin, String id_dispositivo) {
        super(cedula, token);
        this.v_cuota = v_cuota;
        this.k_tipodr = k_tipodr;
        this.v_plazo = v_plazo;
        this.f_vigenci_fin = f_vigenci_fin;
        this.n_destin = n_destin;
        this.id_dispositivo = id_dispositivo;
    }

    public String getV_cuota() {
        return v_cuota;
    }

    public void setV_cuota(String v_cuota) {
        this.v_cuota = v_cuota;
    }

    public String getK_tipodr() {
        return k_tipodr;
    }

    public void setK_tipodr(String k_tipodr) {
        this.k_tipodr = k_tipodr;
    }

    public Integer getV_plazo() {
        return v_plazo;
    }

    public void setV_plazo(Integer v_plazo) {
        this.v_plazo = v_plazo;
    }

    public String getF_vigenci_fin() {
        return f_vigenci_fin;
    }

    public void setF_vigenci_fin(String f_vigenci_fin) {
        this.f_vigenci_fin = f_vigenci_fin;
    }

    public String getN_destin() {
        return n_destin;
    }

    public void setN_destin(String n_destin) {
        this.n_destin = n_destin;
    }

    public String getId_dispositivo() {
        return id_dispositivo;
    }

    public void setId_dispositivo(String id_dispositivo) {
        this.id_dispositivo = id_dispositivo;
    }
}
