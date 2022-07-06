package solidappservice.cm.com.presenteapp.entities.tarjetapresente.dto;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 23/07/2020.
 */

public class ReposicionTarjeta {

    private String d_email;
    private String t_telcel;

    public ReposicionTarjeta() {
    }

    public ReposicionTarjeta(String d_email, String t_telcel) {
        this.d_email = d_email;
        this.t_telcel = t_telcel;
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
}
