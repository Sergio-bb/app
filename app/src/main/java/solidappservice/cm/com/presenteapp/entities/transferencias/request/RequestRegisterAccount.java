package solidappservice.cm.com.presenteapp.entities.transferencias.request;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class RequestRegisterAccount extends BaseRequest {

    private String idDispositivo;
    private String tipoCuenta;
    private String nombreCuenta;
    private String codigoBanco;
    private String numeroCuenta;
    private String cedulaInscripcion;

    public RequestRegisterAccount() {
    }

    public RequestRegisterAccount(String cedula, String token, String idDispositivo,
                                  String tipoCuenta, String nombreCuenta, String codigoBanco,
                                  String numeroCuenta, String cedulaInscripcion) {
        super(cedula, token);
        this.idDispositivo = idDispositivo;
        this.tipoCuenta = tipoCuenta;
        this.nombreCuenta = nombreCuenta;
        this.codigoBanco = codigoBanco;
        this.numeroCuenta = numeroCuenta;
        this.cedulaInscripcion = cedulaInscripcion;
    }

    public String getIdDispositivo() {
        return idDispositivo;
    }

    public void setIdDispositivo(String idDispositivo) {
        this.idDispositivo = idDispositivo;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public String getNombreCuenta() {
        return nombreCuenta;
    }

    public void setNombreCuenta(String nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
    }

    public String getCodigoBanco() {
        return codigoBanco;
    }

    public void setCodigoBanco(String codigoBanco) {
        this.codigoBanco = codigoBanco;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getCedulaInscripcion() {
        return cedulaInscripcion;
    }

    public void setCedulaInscripcion(String cedulaInscripcion) {
        this.cedulaInscripcion = cedulaInscripcion;
    }
}
