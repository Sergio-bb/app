package solidappservice.cm.com.presenteapp.entities.convenios.dto;

import android.text.TextUtils;

import java.util.ArrayList;

import solidappservice.cm.com.presenteapp.entities.convenios.FormaPago;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 15/08/18.
 */

public class Producto {

    private int id;
    private String nombre;
    private String idConvenio;
    private String imagen;
    private int resourceImage;
    private String resumen;
    private String htmlDescripcion;
    private String htmlRestriccion;
    private String valor;
    private String etiquetaCampoCelular;
    private boolean opcionesVisible;
    private ArrayList<FormaPago> formasPago;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdConvenio() {
        return idConvenio;
    }

    public void setIdConvenio(String idConvenio) {
        this.idConvenio = idConvenio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getHtmlDescripcion() {
        return htmlDescripcion;
    }

    public void setHtmlDescripcion(String htmlDescripcion) {
        this.htmlDescripcion = htmlDescripcion;
    }

    public String getHtmlRestriccion() {
        return htmlRestriccion;
    }

    public void setHtmlRestriccion(String htmlRestriccion) {
        this.htmlRestriccion = htmlRestriccion;
    }

    public String getValor() {
        if(TextUtils.isEmpty(valor)) return "$0";
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getEtiquetaCampoCelular() {
        return etiquetaCampoCelular;
    }

    public void setEtiquetaCampoCelular(String etiquetaCampoCelular) {
        this.etiquetaCampoCelular = etiquetaCampoCelular;
    }

    public ArrayList<FormaPago> getFormasPago() {
        return formasPago;
    }

    public void setFormasPago(ArrayList<FormaPago> formasPago) {
        this.formasPago = formasPago;
    }

    public boolean isOpcionesVisible() {
        return opcionesVisible;
    }

    public void setOpcionesVisible(boolean opcionesVisible) {
        this.opcionesVisible = opcionesVisible;
    }

    public void setResourceImage(int resourceImage) {
        this.resourceImage = resourceImage;
    }

    public int getResourceImage() {
        return resourceImage;
    }
}
