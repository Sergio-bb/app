package solidappservice.cm.com.presenteapp.entities.convenios.response;

import java.util.ArrayList;

import solidappservice.cm.com.presenteapp.entities.convenios.dto.Producto;

public class ResponseProducto {

    private int Id;
    private String Nombre;
    private String IdConvenio;
    private String Imagen;
    private String Resumen;
    private String HTMLDescripcion;
    private String HTMLRestriccion;
    private String Valor;
    private String EtiquetaCampoCelular;
    private ArrayList<FormaPago> FormasPago;

    public ResponseProducto() {
    }

    public ResponseProducto(int id, String nombre, String idConvenio, String imagen, String resumen,
                            String HTMLDescripcion, String HTMLRestriccion, String valor, String etiquetaCampoCelular,
                            ArrayList<FormaPago> formasPago) {
        Id = id;
        Nombre = nombre;
        IdConvenio = idConvenio;
        Imagen = imagen;
        Resumen = resumen;
        this.HTMLDescripcion = HTMLDescripcion;
        this.HTMLRestriccion = HTMLRestriccion;
        Valor = valor;
        EtiquetaCampoCelular = etiquetaCampoCelular;
        FormasPago = formasPago;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getIdConvenio() {
        return IdConvenio;
    }

    public void setIdConvenio(String idConvenio) {
        IdConvenio = idConvenio;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public String getResumen() {
        return Resumen;
    }

    public void setResumen(String resumen) {
        Resumen = resumen;
    }

    public String getHTMLDescripcion() {
        return HTMLDescripcion;
    }

    public void setHTMLDescripcion(String HTMLDescripcion) {
        this.HTMLDescripcion = HTMLDescripcion;
    }

    public String getHTMLRestriccion() {
        return HTMLRestriccion;
    }

    public void setHTMLRestriccion(String HTMLRestriccion) {
        this.HTMLRestriccion = HTMLRestriccion;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String valor) {
        Valor = valor;
    }

    public String getEtiquetaCampoCelular() {
        return EtiquetaCampoCelular;
    }

    public void setEtiquetaCampoCelular(String etiquetaCampoCelular) {
        EtiquetaCampoCelular = etiquetaCampoCelular;
    }

    public ArrayList<FormaPago> getFormasPago() {
        return FormasPago;
    }

    public void setFormasPago(ArrayList<FormaPago> formasPago) {
        FormasPago = formasPago;
    }

    public Producto generateProducto(){
        Producto producto = new Producto();
        producto.setId(Id);
        producto.setNombre(Nombre);
        producto.setIdConvenio(IdConvenio);
        producto.setImagen(Imagen);
        producto.setResumen(Resumen);
        producto.setHtmlDescripcion(HTMLDescripcion);
        producto.setHtmlRestriccion(HTMLRestriccion);
        producto.setValor(Valor);
        producto.setEtiquetaCampoCelular(EtiquetaCampoCelular);
        producto.setFormasPago(generateFormasPago());
        return producto;
    }

    private ArrayList<solidappservice.cm.com.presenteapp.entities.convenios.FormaPago> generateFormasPago(){
        ArrayList<solidappservice.cm.com.presenteapp.entities.convenios.FormaPago> formasPago
                = new ArrayList<>();

        for(FormaPago fp: FormasPago){
            solidappservice.cm.com.presenteapp.entities.convenios.FormaPago formaPago = new
                    solidappservice.cm.com.presenteapp.entities.convenios.FormaPago();

            formaPago.setId(fp.getId());
            formaPago.setNombre(fp.getNombre());
            formaPago.setLegal(fp.getTextoLegal());

            formasPago.add(formaPago);
        }

        return formasPago;
    }
}
