package solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse;

public class ResponseValidarCodigo {

    private String codigoValido;
    private String codigoExpirado;

    public ResponseValidarCodigo() {
    }

    public ResponseValidarCodigo(String codigoValido, String codigoExpirado) {
        this.codigoValido = codigoValido;
        this.codigoExpirado = codigoExpirado;
    }

    public String getCodigoValido() {
        return this.codigoValido;
    }

    public void setCodigoValido(String codigoValido) {
        this.codigoValido = codigoValido;
    }

    public String getCodigoExpirado() {
        return this.codigoExpirado;
    }

    public void setCodigoExpirado(String codigoExpirado) {
        this.codigoExpirado = codigoExpirado;
    }

}
