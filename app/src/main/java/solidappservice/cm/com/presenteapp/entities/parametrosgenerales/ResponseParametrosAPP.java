package solidappservice.cm.com.presenteapp.entities.parametrosgenerales;

public class ResponseParametrosAPP {

    private Integer idParametro;
    private String nombreParametro;
    private String estado;
    private String value1;
    private String value2;
    private String value3;
    private String value4;

    public ResponseParametrosAPP() {
    }

    public ResponseParametrosAPP(Integer idParametro, String nombreParametro, String estado, String value1,
                                       String value2, String value3, String value4) {
        this.idParametro = idParametro;
        this.nombreParametro = nombreParametro;
        this.estado = estado;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
    }

    public Integer getIdParametro() {
        return idParametro;
    }

    public void setIdParametro(Integer idParametro) {
        this.idParametro = idParametro;
    }

    public String getNombreParametro() {
        return nombreParametro;
    }

    public void setNombreParametro(String nombreParametro) {
        this.nombreParametro = nombreParametro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }

    public String getValue4() {
        return value4;
    }

    public void setValue4(String value4) {
        this.value4 = value4;
    }

}
