package solidappservice.cm.com.presenteapp.entities.movilexito.request;

public class RequestEmail {

    private String idAplicacion;
    private String asunto;
    private Boolean esHtml;
    private String contenido;

    public RequestEmail(String idAplicacion, String asunto, Boolean esHtml, String contenido) {
        this.idAplicacion = idAplicacion;
        this.asunto = asunto;
        this.esHtml = esHtml;
        this.contenido = contenido;
    }

    public String getIdAplicacion() {
        return idAplicacion;
    }

    public void setIdAplicacion(String idAplicacion) {
        this.idAplicacion = idAplicacion;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public Boolean getEsHtml() {
        return esHtml;
    }

    public void setEsHtml(Boolean esHtml) {
        this.esHtml = esHtml;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}
