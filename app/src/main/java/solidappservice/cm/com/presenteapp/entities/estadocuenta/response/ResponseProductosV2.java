package solidappservice.cm.com.presenteapp.entities.estadocuenta.response;

import java.util.List;

public class ResponseProductosV2 {

    private String nombreCuenta;
    private List<ResponseDetalleProductos> productos;

    public ResponseProductosV2() {
    }

    public ResponseProductosV2(String nombreCuenta, List<ResponseDetalleProductos> productos) {
        this.nombreCuenta = nombreCuenta;
        this.productos = productos;
    }

    public String getNombreCuenta() {
        return nombreCuenta;
    }

    public void setNombreCuenta(String nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
    }

    public List<ResponseDetalleProductos> getProductos() {
        return productos;
    }

    public void setProductos(List<ResponseDetalleProductos> productos) {
        this.productos = productos;
    }

    public static class ResponseDetalleProductos {

        private String nombreProducto;
        private String clasificacionProducto;
        private int tipoCuenta;
        private String codigoProducto;
        private String numeroProducto;
        private int saldoProducto;
        private int saldoVencido;
        private String fechaVencimiento;
        private Long fechaActual;
        private int diasVencidos;
        private int valorTransferible;
        private int valorCuota;
        private String aceptaPagoSuperior;
        private String productoDebitable;

        public ResponseDetalleProductos() {
        }

        public ResponseDetalleProductos(String nombreProducto, String clasificacionProducto, int tipoCuenta, String codigoProducto, String numeroProducto, int saldoProducto, int saldoVencido, String fechaVencimiento, Long fechaActual, int diasVencidos, int valorTransferible, int valorCuota, String aceptaPagoSuperior, String productoDebitable) {
            this.nombreProducto = nombreProducto;
            this.clasificacionProducto = clasificacionProducto;
            this.tipoCuenta = tipoCuenta;
            this.codigoProducto = codigoProducto;
            this.numeroProducto = numeroProducto;
            this.saldoProducto = saldoProducto;
            this.saldoVencido = saldoVencido;
            this.fechaVencimiento = fechaVencimiento;
            this.fechaActual = fechaActual;
            this.diasVencidos = diasVencidos;
            this.valorTransferible = valorTransferible;
            this.valorCuota = valorCuota;
            this.aceptaPagoSuperior = aceptaPagoSuperior;
            this.productoDebitable = productoDebitable;
        }

        public String getNombreProducto() {
            return nombreProducto;
        }

        public void setNombreProducto(String nombreProducto) {
            this.nombreProducto = nombreProducto;
        }

        public String getClasificacionProducto() {
            return clasificacionProducto;
        }

        public void setClasificacionProducto(String clasificacionProducto) {
            this.clasificacionProducto = clasificacionProducto;
        }

        public int getTipoCuenta() {
            return tipoCuenta;
        }

        public void setTipoCuenta(int tipoCuenta) {
            this.tipoCuenta = tipoCuenta;
        }

        public String getCodigoProducto() {
            return codigoProducto;
        }

        public void setCodigoProducto(String codigoProducto) {
            this.codigoProducto = codigoProducto;
        }

        public String getNumeroProducto() {
            return numeroProducto;
        }

        public void setNumeroProducto(String numeroProducto) {
            this.numeroProducto = numeroProducto;
        }

        public int getSaldoProducto() {
            return saldoProducto;
        }

        public void setSaldoProducto(int saldoProducto) {
            this.saldoProducto = saldoProducto;
        }

        public int getSaldoVencido() {
            return saldoVencido;
        }

        public void setSaldoVencido(int saldoVencido) {
            this.saldoVencido = saldoVencido;
        }

        public String getFechaVencimiento() {
            return fechaVencimiento;
        }

        public void setFechaVencimiento(String fechaVencimiento) {
            this.fechaVencimiento = fechaVencimiento;
        }

        public Long getFechaActual() {
            return fechaActual;
        }

        public void setFechaActual(Long fechaActual) {
            this.fechaActual = fechaActual;
        }

        public int getDiasVencidos() {
            return diasVencidos;
        }

        public void setDiasVencidos(int diasVencidos) {
            this.diasVencidos = diasVencidos;
        }

        public int getValorTransferible() {
            return valorTransferible;
        }

        public void setValorTransferible(int valorTransferible) {
            this.valorTransferible = valorTransferible;
        }

        public int getValorCuota() {
            return valorCuota;
        }

        public void setValorCuota(int valorCuota) {
            this.valorCuota = valorCuota;
        }

        public String getAceptaPagoSuperior() {
            return aceptaPagoSuperior;
        }

        public void setAceptaPagoSuperior(String aceptaPagoSuperior) {
            this.aceptaPagoSuperior = aceptaPagoSuperior;
        }

        public String getProductoDebitable() {
            return productoDebitable;
        }

        public void setProductoDebitable(String productoDebitable) {
            this.productoDebitable = productoDebitable;
        }
    }


}
