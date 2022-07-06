package solidappservice.cm.com.presenteapp.entities.dispositivo.apirequest;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class Dispositivo extends BaseRequest {

    private String fabricante;
    private String modelo;
    private String idDispositivo;
    private String imei;
    private String celPrincipal;
    private String sistemaOperativo;
    private String versionSistemaOperativo;

    public Dispositivo() {
    }

    public Dispositivo(String cedula, String token, String fabricante, String modelo, String idDispositivo,
                       String imei, String celPrincipal, String sistemaOperativo, String versionSistemaOperativo) {
        this.cedula = cedula;
        this.token = token;
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.idDispositivo = idDispositivo;
        this.imei = imei;
        this.celPrincipal = celPrincipal;
        this.sistemaOperativo = sistemaOperativo;
        this.versionSistemaOperativo = versionSistemaOperativo;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getIdDispositivo() {
        return idDispositivo;
    }

    public void setIdDispositivo(String idDispositivo) {
        this.idDispositivo = idDispositivo;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getCelPrincipal() {
        return celPrincipal;
    }

    public void setCelPrincipal(String celPrincipal) {
        this.celPrincipal = celPrincipal;
    }

    public String getSistemaOperativo() {
        return sistemaOperativo;
    }

    public void setSistemaOperativo(String sistemaOperativo) {
        this.sistemaOperativo = sistemaOperativo;
    }

    public String getVersionSistemaOperativo() {
        return versionSistemaOperativo;
    }

    public void setVersionSistemaOperativo(String versionSistemaOperativo) {
        this.versionSistemaOperativo = versionSistemaOperativo;
    }
}
