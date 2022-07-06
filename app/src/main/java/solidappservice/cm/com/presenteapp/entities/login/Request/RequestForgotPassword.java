package solidappservice.cm.com.presenteapp.entities.login.Request;

public class RequestForgotPassword {

    private String cedula;

    public RequestForgotPassword() {
    }

    public RequestForgotPassword(String cedula) {
        this.cedula = cedula;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
}
