package solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class RequestActualizarDatos extends BaseRequest {

    private String idRegistroDispositivo;
    private String nombreCompleto;
    private String direccion;
    private String celular;
    private String email;
    private String barrio;
    private String idCiudad;
    private String idDepartamento;
    private String idPais;
    private String ip;
    private String canal;

    public RequestActualizarDatos() {}

    public RequestActualizarDatos(String cedula , String token, String idRegistroDispositivo, String nombreCompleto,
                                  String direccion, String celular, String email, String barrio, String idCiudad,
                                  String idDepartamento, String idPais, String ip, String canal) {
        this.cedula = cedula;
        this.token = token;
        this.idRegistroDispositivo = idRegistroDispositivo;
        this.nombreCompleto = nombreCompleto;
        this.direccion = direccion;
        this.celular = celular;
        this.email = email;
        this.barrio = barrio;
        this.idCiudad = idCiudad;
        this.idDepartamento = idDepartamento;
        this.idPais = idPais;
        this.ip  = ip;
        this.canal = canal;
    }

    public String getIdRegistroDispositivo() {
        return idRegistroDispositivo;
    }

    public void setIdRegistroDispositivo(String idRegistroDispositivo) {
        this.idRegistroDispositivo = idRegistroDispositivo;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(String idCiudad) {
        this.idCiudad = idCiudad;
    }

    public String getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(String idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getIdPais() {
        return idPais;
    }

    public void setIdPais(String idPais) {
        this.idPais = idPais;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCanal() {
        return this.canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

}
