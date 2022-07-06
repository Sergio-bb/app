package solidappservice.cm.com.presenteapp.entities.convenios.response;

import java.util.ArrayList;

public class ResponseCategoria {

    private String Id;
    private String Imagen;
    private String Nombre;
    private ArrayList<String> Ciudades;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public ArrayList<String> getCiudades() {
        return Ciudades;
    }

    public void setCiudades(ArrayList<String> ciudades) {
        Ciudades = ciudades;
    }
}
