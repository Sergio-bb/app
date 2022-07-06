package solidappservice.cm.com.presenteapp.entities.login.Response;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 26/11/2015.
 */
public class Usuario {

    private String cedula;
    private String clave;
    private String token;
    private String nombreAsociado;
    private String topeTransacciones;
    private String aceptoUltimosTyC;
    private String fechaUltimoIngreso;
    private DatosActualizados datosActualizados;

    public Usuario() {
    }

    public Usuario(String cedula, String clave, String token, String nombreAsociado, String topeTransacciones,
                   String aceptoUltimosTyC,String fechaUltimoIngreso, DatosActualizados datosActualizados) {
        this.cedula = cedula;
        this.clave = clave;
        this.token = token;
        this.nombreAsociado = nombreAsociado;
        this.topeTransacciones = topeTransacciones;
        this.aceptoUltimosTyC = aceptoUltimosTyC;
        this.fechaUltimoIngreso = fechaUltimoIngreso;
        this.datosActualizados = datosActualizados;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNombreAsociado() {
        return nombreAsociado;
    }

    public void setNombreAsociado(String nombreAsociado) {
        this.nombreAsociado = nombreAsociado;
    }

    public String getTopeTransacciones() {
        return topeTransacciones;
    }

    public void setTopeTransacciones(String topeTransacciones) {
        this.topeTransacciones = topeTransacciones;
    }

    public String getAceptoUltimosTyC() {
        return aceptoUltimosTyC;
    }

    public void setAceptoUltimosTyC(String aceptoUltimosTyC) {
        this.aceptoUltimosTyC = aceptoUltimosTyC;
    }

    public String getFechaUltimoIngreso() {
        return fechaUltimoIngreso;
    }
    public void setFechaUltimoIngreso(String fechaUltimoIngreso) {
        this.fechaUltimoIngreso = fechaUltimoIngreso;
    }

    public DatosActualizados getDatosActualizados() {
        return datosActualizados;
    }

    public void setDatosActualizados(DatosActualizados datosActualizados) {
        this.datosActualizados = datosActualizados;
    }
}
