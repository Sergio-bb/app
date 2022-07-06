package solidappservice.cm.com.presenteapp.entities.convenios.request;

public class RequestSolicitudProducto {

    private String IdUso;
    private String IdProducto;
    private String Valor;
    private String IdFormaPago;
    private String Email;
    private String EtiquetaCelular;
    private String Celular;

    private String Beneficio;
    private String NombreFormaPago;
    private String NombreProducto;

    public String getIdUso() {
        return IdUso;
    }

    public void setIdUso(String idUso) {
        IdUso = idUso;
    }

    public String getIdProducto() {
        return IdProducto;
    }

    public void setIdProducto(String idProducto) {
        IdProducto = idProducto;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String valor) {
        Valor = valor;
    }

    public String getIdFormaPago() {
        return IdFormaPago;
    }

    public void setIdFormaPago(String idFormaPago) {
        IdFormaPago = idFormaPago;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCelular() {
        return Celular;
    }

    public void setCelular(String celular) {
        Celular = celular;
    }

    public void setEqtiquetaCelular(String etiquetaCelular) {
        EtiquetaCelular = etiquetaCelular;
    }

    public String getEtiquetaCelular() {
        return EtiquetaCelular;
    }

    public String getBeneficio() {
        return Beneficio;
    }

    public void setBeneficio(String beneficio) {
        Beneficio = beneficio;
    }

    public String getNombreFormaPago() {
        return NombreFormaPago;
    }

    public void setNombreFormaPago(String nombreFormaPago) {
        NombreFormaPago = nombreFormaPago;
    }

    public String getNombreProducto() {
        return NombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        NombreProducto = nombreProducto;
    }
}
