package solidappservice.cm.com.presenteapp.entities.convenios.response;

import java.util.ArrayList;

import solidappservice.cm.com.presenteapp.entities.convenios.dto.Ubicacion;

public class ResponseConvenio {

    private String Id;
    private String Nombre;
    private String Imagen;
    private String Beneficio;
    private String DescripcionCorta;
    private String Detalle;
    private boolean Destacado;
    private String IdCategoria;
    private ArrayList<ResponseUbicacion> Ubicaciones;
    private int Orden;

    public ResponseConvenio() {
    }

    public ResponseConvenio(String id, String nombre, String imagen, String beneficio, String descripcionCorta, String detalle, boolean destacado, String idCategoria, ArrayList<ResponseUbicacion> ubicaciones, int orden) {
        Id = id;
        Nombre = nombre;
        Imagen = imagen;
        Beneficio = beneficio;
        DescripcionCorta = descripcionCorta;
        Detalle = detalle;
        Destacado = destacado;
        IdCategoria = idCategoria;
        Ubicaciones = ubicaciones;
        Orden = orden;
    }

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

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public String getBeneficio() {
        return Beneficio;
    }

    public void setBeneficio(String beneficio) {
        Beneficio = beneficio;
    }

    public String getDescripcionCorta() {
        return DescripcionCorta;
    }

    public void setDescripcionCorta(String descripcionCorta) {
        DescripcionCorta = descripcionCorta;
    }

    public String getDetalle() {
        return Detalle;
    }

    public void setDetalle(String detalle) {
        Detalle = detalle;
    }

    public boolean isDestacado() {
        return Destacado;
    }

    public void setDestacado(boolean destacado) {
        Destacado = destacado;
    }

    public String getIdCategoria() {
        return IdCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        IdCategoria = idCategoria;
    }

    public ArrayList<ResponseUbicacion> getUbicaciones() {
        return Ubicaciones;
    }

    public void setUbicaciones(ArrayList<ResponseUbicacion> ubicaciones) {
        Ubicaciones = ubicaciones;
    }

    public int getOrden() {
        return Orden;
    }

    public void setOrden(int orden) {
        Orden = orden;
    }

    public ArrayList<Ubicacion> generateUbicaciones(){
        ArrayList<Ubicacion> ubicaciones = new ArrayList<>();

        for(ResponseUbicacion u: Ubicaciones){
            Ubicacion ub = new Ubicacion();
            ub.setId(u.getId());
            ub.setCiudad(u.getCiudad());
            ub.setDireccion(u.getDireccion());
            ub.setTelefonoContacto(u.getTelefonoContacto());
            ub.setDireccionWeb(u.getDireccionWeb());
            ub.setEmail(u.getEmail());
            ub.setLatitud(u.getLatitud());
            ub.setLongitud(u.getLongitud());
            ubicaciones.add(ub);
        }

        return ubicaciones;
    }
}
