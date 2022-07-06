package solidappservice.cm.com.presenteapp.entities.convenios.response;

public class ResponseUbicacion {

    private String Id;
    private String Ciudad;
    private String Direccion;
    private String TelefonoContacto;
    private String DireccionWeb;
    private String Email;
    private String Latitud;
    private String Longitud;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCiudad() {
        return Ciudad;
    }

    public void setCiudad(String ciudad) {
        Ciudad = ciudad;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getTelefonoContacto() {
        return TelefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        TelefonoContacto = telefonoContacto;
    }

    public String getDireccionWeb() {
        return DireccionWeb;
    }

    public void setDireccionWeb(String direccionWeb) {
        DireccionWeb = direccionWeb;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getLatitud() {
        return Latitud;
    }

    public void setLatitud(String latitud) {
        Latitud = latitud;
    }

    public String getLongitud() {
        return Longitud;
    }

    public void setLongitud(String longitud) {
        Longitud = longitud;
    }
}

