package solidappservice.cm.com.presenteapp.entities.base;

public class BaseRequest{

    public String cedula;
    public String token;

    public BaseRequest() {}

    public BaseRequest(String cedula, String token) {
        this.cedula = cedula;
        this.token = token;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
