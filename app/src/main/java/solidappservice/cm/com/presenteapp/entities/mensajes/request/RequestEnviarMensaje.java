package solidappservice.cm.com.presenteapp.entities.mensajes.request;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class RequestEnviarMensaje extends BaseRequest {

    private String f_inicio;
    private String f_final;
    private String n_tipo;
    private String n_mensaj;

    public RequestEnviarMensaje() {
    }

    public RequestEnviarMensaje(String cedula, String token, String f_inicio, String f_final, String n_tipo, String n_mensaj) {
        super(cedula, token);
        this.f_inicio = f_inicio;
        this.f_final = f_final;
        this.n_tipo = n_tipo;
        this.n_mensaj = n_mensaj;
    }

    public String getF_inicio() {
        return f_inicio;
    }

    public void setF_inicio(String f_inicio) {
        this.f_inicio = f_inicio;
    }

    public String getF_final() {
        return f_final;
    }

    public void setF_final(String f_final) {
        this.f_final = f_final;
    }

    public String getN_tipo() {
        return n_tipo;
    }

    public void setN_tipo(String n_tipo) {
        this.n_tipo = n_tipo;
    }

    public String getN_mensaj() {
        return n_mensaj;
    }

    public void setN_mensaj(String n_mensaj) {
        this.n_mensaj = n_mensaj;
    }
}
