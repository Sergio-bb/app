package solidappservice.cm.com.presenteapp.entities.convenios.response;

import java.util.ArrayList;
import java.util.List;

import solidappservice.cm.com.presenteapp.entities.convenios.dto.Categoria;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Ciudad;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Convenio;

public class ResponseResumen {

    private String EmailAsociado;
    private String UbicacionAsociado;
    private List<ResponseCategoria> Categorias;
    private List<ResponseConvenio> Convenios;
    private List<String> Ciudades;

    public ResponseResumen() {
    }

    public ResponseResumen(String emailAsociado, String ubicacionAsociado, List<ResponseCategoria> categorias,
                           List<ResponseConvenio> Convenios, List<String> ciudades) {
        EmailAsociado = emailAsociado;
        UbicacionAsociado = ubicacionAsociado;
        Categorias = categorias;
        this.Convenios = Convenios;
        Ciudades = ciudades;
    }

    public String getEmailAsociado() {
        return EmailAsociado;
    }

    public void setEmailAsociado(String emailAsociado) {
        EmailAsociado = emailAsociado;
    }

    public String getUbicacionAsociado() {
        return UbicacionAsociado;
    }

    public void setUbicacionAsociado(String ubicacionAsociado) {
        UbicacionAsociado = ubicacionAsociado;
    }

    public List<ResponseCategoria> getCategorias() {
        return Categorias;
    }

    public void setCategorias(List<ResponseCategoria> categorias) {
        Categorias = categorias;
    }

    public List<ResponseConvenio> getConvenios() {
        return Convenios;
    }

    public void setConvenios(List<ResponseConvenio> Convenios) {
        this.Convenios = Convenios;
    }

    public List<String> getCiudades() {
        return Ciudades;
    }

    public void setCiudades(List<String> ciudades) {
        Ciudades = ciudades;
    }

    public List<Categoria> generateCategorias() {
        List<Categoria> result = new ArrayList<>();
        for(ResponseCategoria c: Categorias){
            Categoria ca = new Categoria();
            ca.setId(c.getId());
            ca.setImagen(c.getImagen());
            ca.setNombre(c.getNombre());
            ca.setCiudades(c.getCiudades());
            result.add(ca);
        }
        return result;
    }

    public ArrayList<Convenio> generateConvenios() {
        ArrayList<Convenio> result = new ArrayList<>();
        for(ResponseConvenio c: Convenios){
            Convenio ca = new Convenio();
            ca.setId(c.getId());
            ca.setNombre(c.getNombre());
            ca.setImagen(c.getImagen());
            ca.setBeneficio(c.getBeneficio());
            ca.setDescripcionCorta(c.getDescripcionCorta());
            ca.setDetalle(c.getDetalle());
            ca.setDestacado(c.isDestacado());
            ca.setIdCategoria(c.getIdCategoria());
            ca.setOrden(c.getOrden());
            ca.setUbicaciones(c.generateUbicaciones());
            result.add(ca);
        }
        return result;
    }

    public ArrayList<Ciudad> generateCiudades() {
        ArrayList<Ciudad> result = new ArrayList<>();
        for(String c: Ciudades){
            Ciudad ca = new Ciudad();
            ca.setNombre(c);
            result.add(ca);
        }
        return result;
    }
}
