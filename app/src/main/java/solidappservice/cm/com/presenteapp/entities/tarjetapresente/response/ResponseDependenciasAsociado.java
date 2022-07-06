package solidappservice.cm.com.presenteapp.entities.tarjetapresente.response;

public class ResponseDependenciasAsociado {

    private String n_NOMBR1;
    private String n_NOMBR2;
    private String n_APELL1;
    private String n_APELL2;
    private String n_CIUDAD;
    private String codigodependencia;
    private String n_DEPENDENCIA;

    public ResponseDependenciasAsociado() {
    }

    public ResponseDependenciasAsociado(String n_NOMBR1, String n_NOMBR2, String n_APELL1, String n_APELL2, String n_CIUDAD, String codigodependencia, String n_DEPENDENCIA) {
        this.n_NOMBR1 = n_NOMBR1;
        this.n_NOMBR2 = n_NOMBR2;
        this.n_APELL1 = n_APELL1;
        this.n_APELL2 = n_APELL2;
        this.n_CIUDAD = n_CIUDAD;
        this.codigodependencia = codigodependencia;
        this.n_DEPENDENCIA = n_DEPENDENCIA;
    }

    public String getN_NOMBR1() {
        return n_NOMBR1;
    }

    public void setN_NOMBR1(String n_NOMBR1) {
        this.n_NOMBR1 = n_NOMBR1;
    }

    public String getN_NOMBR2() {
        return n_NOMBR2;
    }

    public void setN_NOMBR2(String n_NOMBR2) {
        this.n_NOMBR2 = n_NOMBR2;
    }

    public String getN_APELL1() {
        return n_APELL1;
    }

    public void setN_APELL1(String n_APELL1) {
        this.n_APELL1 = n_APELL1;
    }

    public String getN_APELL2() {
        return n_APELL2;
    }

    public void setN_APELL2(String n_APELL2) {
        this.n_APELL2 = n_APELL2;
    }

    public String getN_CIUDAD() {
        return n_CIUDAD;
    }

    public void setN_CIUDAD(String n_CIUDAD) {
        this.n_CIUDAD = n_CIUDAD;
    }

    public String getCodigodependencia() {
        return codigodependencia;
    }

    public void setCodigodependencia(String codigodependencia) {
        this.codigodependencia = codigodependencia;
    }

    public String getN_DEPENDENCIA() {
        return n_DEPENDENCIA;
    }

    public void setN_DEPENDENCIA(String n_DEPENDENCIA) {
        this.n_DEPENDENCIA = n_DEPENDENCIA;
    }
}
