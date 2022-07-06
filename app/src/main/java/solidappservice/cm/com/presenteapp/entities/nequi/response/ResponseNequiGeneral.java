package solidappservice.cm.com.presenteapp.entities.nequi.response;

public class ResponseNequiGeneral {

    private String k_Parametro;
    private String n_NombreParametro;
    private String n_ValorParametro;
    private String n_Valor1;
    private String n_Valor2;
    private String n_Valor3;
    private String n_Valor4;

    public ResponseNequiGeneral() {
    }

    public ResponseNequiGeneral(String k_Parametro, String n_NombreParametro, String n_ValorParametro, String n_Valor1, String n_Valor2, String n_Valor3, String n_Valor4) {
        this.k_Parametro = k_Parametro;
        this.n_NombreParametro = n_NombreParametro;
        this.n_ValorParametro = n_ValorParametro;
        this.n_Valor1 = n_Valor1;
        this.n_Valor2 = n_Valor2;
        this.n_Valor3 = n_Valor3;
        this.n_Valor4 = n_Valor4;
    }

    public String getK_Parametro() {
        return k_Parametro;
    }

    public void setK_Parametro(String k_Parametro) {
        this.k_Parametro = k_Parametro;
    }

    public String getN_NombreParametro() {
        return n_NombreParametro;
    }

    public void setN_NombreParametro(String n_NombreParametro) {
        this.n_NombreParametro = n_NombreParametro;
    }

    public String getN_ValorParametro() {
        return n_ValorParametro;
    }

    public void setN_ValorParametro(String n_ValorParametro) {
        this.n_ValorParametro = n_ValorParametro;
    }

    public String getN_Valor1() {
        return n_Valor1;
    }

    public void setN_Valor1(String n_Valor1) {
        this.n_Valor1 = n_Valor1;
    }

    public String getN_Valor2() {
        return n_Valor2;
    }

    public void setN_Valor2(String n_Valor2) {
        this.n_Valor2 = n_Valor2;
    }

    public String getN_Valor3() {
        return n_Valor3;
    }

    public void setN_Valor3(String n_Valor3) {
        this.n_Valor3 = n_Valor3;
    }

    public String getN_Valor4() {
        return n_Valor4;
    }

    public void setN_Valor4(String n_Valor4) {
        this.n_Valor4 = n_Valor4;
    }
}