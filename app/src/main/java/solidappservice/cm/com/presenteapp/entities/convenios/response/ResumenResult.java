package solidappservice.cm.com.presenteapp.entities.convenios.response;


public class ResumenResult {

    private boolean Exitoso;
    private boolean ErrorAutenticacion;
    private String DescripcionError;
    private ResponseResumen Respuesta;

    public boolean isExitoso() {
        return Exitoso;
    }

    public void setExitoso(boolean exitoso) {
        Exitoso = exitoso;
    }

    public boolean isErrorAutenticacion() {
        return ErrorAutenticacion;
    }

    public void setErrorAutenticacion(boolean errorAutenticacion) {
        ErrorAutenticacion = errorAutenticacion;
    }

    public String getDescripcionError() {
        return DescripcionError;
    }

    public void setDescripcionError(String descripcionError) {
        DescripcionError = descripcionError;
    }

    public ResponseResumen getRespuesta() {
        return Respuesta;
    }

    public void setRespuesta(ResponseResumen respuesta) {
        Respuesta = respuesta;
    }

}
