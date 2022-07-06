package solidappservice.cm.com.presenteapp.entities.nequi.response;

public class ResponseSuscriptionStatus {

    private int k_Registro;
    private String t_Telefono;
    private String a_AnumNit;
    private String n_TokenSuscripcion;
    private  String f_FechaCreacion;
    private String n_ClientID;
    private String n_MessageID;
    private String n_Activo;

    public ResponseSuscriptionStatus(int k_Registro, String t_Telefono, String a_AnumNit, String n_TokenSuscripcion, String f_FechaCreacion, String n_ClientID, String n_MessageID, String n_Activo) {
        this.k_Registro = k_Registro;
        this.t_Telefono = t_Telefono;
        this.a_AnumNit = a_AnumNit;
        this.n_TokenSuscripcion = n_TokenSuscripcion;
        this.f_FechaCreacion = f_FechaCreacion;
        this.n_ClientID = n_ClientID;
        this.n_MessageID = n_MessageID;
        this.n_Activo = n_Activo;
    }

    public int getK_Registro() {
        return k_Registro;
    }

    public void setK_Registro(int k_Registro) {
        this.k_Registro = k_Registro;
    }

    public String getT_Telefono() {
        return t_Telefono;
    }

    public void setT_Telefono(String t_Telefono) {
        this.t_Telefono = t_Telefono;
    }

    public String getA_AnumNit() {
        return a_AnumNit;
    }

    public void setA_AnumNit(String a_AnumNit) {
        this.a_AnumNit = a_AnumNit;
    }

    public String getN_TokenSuscripcion() {
        return n_TokenSuscripcion;
    }

    public void setN_TokenSuscripcion(String n_TokenSuscripcion) {
        this.n_TokenSuscripcion = n_TokenSuscripcion;
    }

    public String getF_FechaCreacion() {
        return f_FechaCreacion;
    }

    public void setF_FechaCreacion(String f_FechaCreacion) {
        this.f_FechaCreacion = f_FechaCreacion;
    }

    public String getN_ClientID() {
        return n_ClientID;
    }

    public void setN_ClientID(String n_ClientID) {
        this.n_ClientID = n_ClientID;
    }

    public String getN_MessageID() {
        return n_MessageID;
    }

    public void setN_MessageID(String n_MessageID) {
        this.n_MessageID = n_MessageID;
    }

    public String getN_Activo() {
        return n_Activo;
    }

    public void setN_Activo(String n_Activo) {
        this.n_Activo = n_Activo;
    }
}
