package solidappservice.cm.com.presenteapp.entities.mensajes.response;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA 30/11/2015.
 */
public class ResponseObtenerMensajes {

    private String idMensaje;
    private String titulo;
    private String contenido;
    private String leido;

    public ResponseObtenerMensajes() {
    }

    public ResponseObtenerMensajes(String idMensaje, String titulo, String contenido, String leido) {
        this.idMensaje = idMensaje;
        this.titulo = titulo;
        this.contenido = contenido;
        this.leido = leido;
    }

    public String getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(String idMensaje) {
        this.idMensaje = idMensaje;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getLeido() {
        return leido;
    }

    public void setLeido(String leido) {
        this.leido = leido;
    }
}
