package solidappservice.cm.com.presenteapp.entities.base;


public class BaseResponseConvenios<T> {

    private boolean Exitoso;
    private boolean ErrorAutenticacion;
    private String DescripcionError;
    private T Respuesta;

    public BaseResponseConvenios() {
    }

    public BaseResponseConvenios(boolean exitoso, boolean errorAutenticacion, String descripcionError, T respuesta) {
        Exitoso = exitoso;
        ErrorAutenticacion = errorAutenticacion;
        DescripcionError = descripcionError;
        Respuesta = respuesta;
    }

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

    public T getRespuesta() {
        return Respuesta;
    }

    public void setRespuesta(T respuesta) {
        Respuesta = respuesta;
    }

}
