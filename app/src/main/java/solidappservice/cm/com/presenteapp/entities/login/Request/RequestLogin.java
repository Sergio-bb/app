package solidappservice.cm.com.presenteapp.entities.login.Request;

public class RequestLogin {

    private String cedula;
    private String clave;
    private String origen;

    public RequestLogin() {
    }

    public RequestLogin(String cedula, String clave, String origen) {
        this.cedula = cedula;
        this.clave = clave;
        this.origen = origen;
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

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }
}
