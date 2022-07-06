package solidappservice.cm.com.presenteapp.entities.banercomercial.response;

public class ResponseBanerComercial {

    private Integer k_banner;
    private String n_banner;
    private String n_url_imagen;
    private String n_url_enlace;
    private String i_estado;
    private String i_autenticportal;

    public ResponseBanerComercial() {
    }

    public ResponseBanerComercial(Integer k_banner, String n_banner, String n_url_imagen, String n_url_enlace,
                                  String i_estado, String i_autenticportal) {
        this.k_banner = k_banner;
        this.n_banner = n_banner;
        this.n_url_imagen = n_url_imagen;
        this.n_url_enlace = n_url_enlace;
        this.i_estado = i_estado;
        this.i_autenticportal = i_autenticportal;
    }

    public Integer getK_banner() {
        return k_banner;
    }

    public void setK_banner(Integer k_banner) {
        this.k_banner = k_banner;
    }

    public String getN_banner() {
        return n_banner;
    }

    public void setN_banner(String n_banner) {
        this.n_banner = n_banner;
    }

    public String getN_url_imagen() {
        return n_url_imagen;
    }

    public void setN_url_imagen(String n_url_imagen) {
        this.n_url_imagen = n_url_imagen;
    }

    public String getN_url_enlace() {
        return n_url_enlace;
    }

    public void setN_url_enlace(String n_url_enlace) {
        this.n_url_enlace = n_url_enlace;
    }

    public String getI_estado() {
        return i_estado;
    }

    public void setI_estado(String i_estado) {
        this.i_estado = i_estado;
    }

    public String getI_autenticportal() {
        return i_autenticportal;
    }

    public void setI_autenticportal(String i_autenticportal) {
        this.i_autenticportal = i_autenticportal;
    }
}
