package solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response;

import java.util.ArrayList;
import java.util.List;

public class ResponsePortafolio {

    private String categoria;
    private String categoriaPadre;
    public List<DetallePortafolio> detalle;

    public ResponsePortafolio() {
    }

    public ResponsePortafolio(String categoria, String categoriaPadre, List<DetallePortafolio> detalle) {
        this.categoria = categoria;
        this.categoriaPadre = categoriaPadre;
        this.detalle = detalle;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCategoriaPadre() {
        return categoriaPadre;
    }

    public void setCategoriaPadre(String categoriaPadre) {
        this.categoriaPadre = categoriaPadre;
    }

    public List<DetallePortafolio> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetallePortafolio> detalle) {
        this.detalle = detalle;
    }

    public static class DetallePortafolio{
        String nombre;
        String descripcionCorta;
        String descripcionLarga;

        public DetallePortafolio() {
        }

        public DetallePortafolio(String nombre, String descripcionCorta, String descripcionLarga) {
            this.nombre = nombre;
            this.descripcionCorta = descripcionCorta;
            this.descripcionLarga = descripcionLarga;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getDescripcionCorta() {
            return descripcionCorta;
        }

        public void setDescripcionCorta(String descripcionCorta) {
            this.descripcionCorta = descripcionCorta;
        }

        public String getDescripcionLarga() {
            return descripcionLarga;
        }

        public void setDescripcionLarga(String descripcionLarga) {
            this.descripcionLarga = descripcionLarga;
        }
    }
}
