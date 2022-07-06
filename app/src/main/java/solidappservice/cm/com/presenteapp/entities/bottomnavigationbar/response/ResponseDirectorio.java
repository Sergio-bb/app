package solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response;

public class ResponseDirectorio {

    private String n_nombre;
    private String n_telefono;
    private String n_teleusu;
    private int i_orden;

    public ResponseDirectorio() {
    }

    public ResponseDirectorio(String n_nombre, String n_telefono, String n_teleusu, int i_orden) {
        this.n_nombre = n_nombre;
        this.n_telefono = n_telefono;
        this.n_teleusu = n_teleusu;
        this.i_orden = i_orden;
    }

    public String getN_nombre() {
        return n_nombre;
    }

    public void setN_nombre(String n_nombre) {
        this.n_nombre = n_nombre;
    }

    public String getN_telefono() {
        return n_telefono;
    }

    public void setN_telefono(String n_telefono) {
        this.n_telefono = n_telefono;
    }

    public String getN_teleusu() {
        return n_teleusu;
    }

    public void setN_teleusu(String n_teleusu) {
        this.n_teleusu = n_teleusu;
    }

    public int getI_orden() {
        return i_orden;
    }

    public void setI_orden(int i_orden) {
        this.i_orden = i_orden;
    }

}
