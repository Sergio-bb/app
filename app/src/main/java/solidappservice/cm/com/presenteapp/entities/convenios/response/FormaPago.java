package solidappservice.cm.com.presenteapp.entities.convenios.response;

public class FormaPago {

    private String Id;
    private String Nombre;
    private String TextoLegal;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getTextoLegal() {
        return TextoLegal;
    }

    public void setTextoLegal(String textoLegal) {
        TextoLegal = textoLegal;
    }
}
