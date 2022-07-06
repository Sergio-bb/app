package solidappservice.cm.com.presenteapp.entities.convenios.dto;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 14/08/18.
 */

public class Categoria {

    private String id;
    private String imagen;
    private String nombre;
    private int imagenId;
    private ArrayList<String> Ciudades;

    private Bitmap img;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setImagenId(int imagenId) {
        this.imagenId = imagenId;
    }

    public int getImagenId() {
        return imagenId;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public Bitmap getImg() {
        return img;
    }

    public ArrayList<String> getCiudades() {
        return Ciudades;
    }

    public void setCiudades(ArrayList<String> ciudades) {
        Ciudades = ciudades;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
