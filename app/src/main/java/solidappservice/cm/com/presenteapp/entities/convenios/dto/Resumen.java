package solidappservice.cm.com.presenteapp.entities.convenios.dto;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import solidappservice.cm.com.presenteapp.entities.convenios.request.RequestSolicitudProducto;

public class Resumen {

    private String EmailAsociado;
    private String UbicacionAsociado;
    private List<Categoria> Categorias;
    private List<Convenio> Convenios;
    private List<Ciudad> Ciudades;

    private List<Convenio> conveniosDestacados;
    private List<Producto> productosPorConvenio;

    //Para manejar flujo entre pantallas
    private Convenio convenioSeleccionado;
    private Producto productoSeleccionado;
    private Categoria categoriaSeleccionada;
    private RequestSolicitudProducto solicitudProducto;

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

    public List<Categoria> getCategorias() {
        return Categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.Categorias = categorias;
    }

    public List<Convenio> getConvenios() {
        return Convenios;
    }

    public List<Convenio> getConveniosPorCategoria(String idCategoria){
        ArrayList<Convenio> convenios = new ArrayList<>();
        for(Convenio c: Convenios){
            if(c.getIdCategoria().equals(idCategoria)){
                    convenios.add(c);
            }
        }
        return convenios;
    }

    public void setConvenios(List<Convenio> convenios) {
        this.Convenios = convenios;
        loadConveniosDestcados();
    }

    public List<Ciudad> getCiudades() {
        return Ciudades;
    }

    public void setCiudades(List<Ciudad> ciudades) {
        this.Ciudades = ciudades;
    }

    public void loadConveniosDestcados(){
        conveniosDestacados = new ArrayList<>();
        for (Convenio c: Convenios) {
            if(c.isDestacado()){
                ArrayList<Ubicacion> ubicaciones = c.getUbicaciones();
                if(!TextUtils.isEmpty(UbicacionAsociado)
                        && ubicaciones != null
                        && ubicaciones.size() > 0){
                    boolean convenioEstaEnUbicacionUsuario = false;
                    for(Ubicacion u: ubicaciones){
                        if(u.getCiudad().equalsIgnoreCase(UbicacionAsociado)){
                            convenioEstaEnUbicacionUsuario = true;
                            break;
                        }
                    }
                    if(convenioEstaEnUbicacionUsuario){
                        conveniosDestacados.add(c);
                    }
                }else{
                    conveniosDestacados.add(c);
                }
            }
        }
    }

    public List<Convenio> getConveniosDestacados() {
        return conveniosDestacados;
    }

    public Convenio getConvenioSeleccionado() {
        return convenioSeleccionado;
    }

    public void setConvenioSeleccionado(Convenio convenioSeleccionado) {
        this.convenioSeleccionado = convenioSeleccionado;
    }

    public List<Producto> getProductosPorConvenio() {
        if(productosPorConvenio == null) productosPorConvenio = new ArrayList<>();
        return productosPorConvenio;
    }

    public void setProductosPorConvenio(List<Producto> productosPorConvenio) {
        this.productosPorConvenio = productosPorConvenio;
    }

    public void setProductoSeleccionado(Producto productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
    }

    public Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public RequestSolicitudProducto getSolicitudProducto() {
        return solicitudProducto;
    }

    public void setSolicitudProducto(RequestSolicitudProducto solicitudProducto) {
        this.solicitudProducto = solicitudProducto;
    }

    public void setCategoriaSeleccionada(Categoria categoriaSeleccionada) {
        this.categoriaSeleccionada = categoriaSeleccionada;
    }

    public Categoria getCategoriaSeleccionada() {
        return categoriaSeleccionada;
    }
}
