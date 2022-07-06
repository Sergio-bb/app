package solidappservice.cm.com.presenteapp.entities.base;

public class BaseResponse<T>{

    private String descripcionError;
    private String mensajeErrorUsuario;
    private String errorToken;
    private T resultado;

    public BaseResponse() {
    }

    public BaseResponse(String descripcionError, String mensajeErrorUsuario, String errorToken, T resultado) {
        this.descripcionError = descripcionError;
        this.mensajeErrorUsuario = mensajeErrorUsuario;
        this.errorToken = errorToken;
        this.resultado = resultado;
    }

    public String getDescripcionError() {
        return descripcionError;
    }

    public void setDescripcionError(String descripcionError) {
        this.descripcionError = descripcionError;
    }

    public String getMensajeErrorUsuario() {
        return mensajeErrorUsuario;
    }

    public void setMensajeErrorUsuario(String mensajeErrorUsuario) {
        this.mensajeErrorUsuario = mensajeErrorUsuario;
    }

    public String getErrorToken() {
        return errorToken;
    }

    public void setErrorToken(String errorToken) {
        this.errorToken = errorToken;
    }

    public T getResultado() {
        return resultado;
    }

    public void setResultado(T resultado) {
        this.resultado = resultado;
    }

}
