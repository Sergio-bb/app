package solidappservice.cm.com.presenteapp.entities.datosasociado.response;

public class ResponseDatosBasicosAsociado {

    private String tipoDocumento;
    private String documentoIdentidad;
    private String pimerApellido;
    private String segundoApellido;
    private String primerNombre;
    private String segundoNombre;

    public ResponseDatosBasicosAsociado() {
    }

    public ResponseDatosBasicosAsociado(String tipoDocumento, String documentoIdentidad, String pimerApellido, String segundoApellido, String primerNombre, String segundoNombre) {
        this.tipoDocumento = tipoDocumento;
        this.documentoIdentidad = documentoIdentidad;
        this.pimerApellido = pimerApellido;
        this.segundoApellido = segundoApellido;
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public void setDocumentoIdentidad(String documentoIdentidad) {
        this.documentoIdentidad = documentoIdentidad;
    }

    public String getPimerApellido() {
        return pimerApellido;
    }

    public void setPimerApellido(String pimerApellido) {
        this.pimerApellido = pimerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }
}
