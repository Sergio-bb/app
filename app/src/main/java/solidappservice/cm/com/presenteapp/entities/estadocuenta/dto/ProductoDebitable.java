package solidappservice.cm.com.presenteapp.entities.estadocuenta.dto;

public class ProductoDebitable {

    private String nombreCuenta;
    private String numeroProducto;
    private String nombreProducto;
    private double saldo;

    public ProductoDebitable() {
    }

    public ProductoDebitable(String nombreCuenta, String numeroProducto, String nombreProducto, double saldo) {
        this.nombreCuenta = nombreCuenta;
        this.numeroProducto = numeroProducto;
        this.nombreProducto = nombreProducto;
        this.saldo = saldo;
    }

    public String getNombreCuenta() {
        return nombreCuenta;
    }

    public void setNombreCuenta(String nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
    }

    public String getNumeroProducto() {
        return numeroProducto;
    }

    public void setNumeroProducto(String numeroProducto) {
        this.numeroProducto = numeroProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
