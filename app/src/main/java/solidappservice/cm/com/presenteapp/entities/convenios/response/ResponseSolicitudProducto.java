package solidappservice.cm.com.presenteapp.entities.convenios.response;

public class ResponseSolicitudProducto {

    private String IdTransaccion;
    private String ResultadoHTML;

    public ResponseSolicitudProducto() {
    }

    public ResponseSolicitudProducto(String idTransaccion, String resultadoHTML) {
        IdTransaccion = idTransaccion;
        ResultadoHTML = resultadoHTML;
    }

    public String getIdTransaccion() {
        return IdTransaccion;
    }

    public String getResultadoHTML() {
        return ResultadoHTML;
    }
}
