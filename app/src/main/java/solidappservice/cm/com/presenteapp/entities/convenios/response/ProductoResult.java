package solidappservice.cm.com.presenteapp.entities.convenios.response;

import java.util.ArrayList;

public class ProductoResult {

    private boolean Exitoso;
    private boolean ErrorAutenticacion;
    private String DescripcionError;
    private ArrayList<ResponseProducto> Respuesta;

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

    public ArrayList<ResponseProducto> getRespuesta() {
        return Respuesta;
    }

    public void setRespuesta(ArrayList<ResponseProducto> respuesta) {
        Respuesta = respuesta;
    }
}
