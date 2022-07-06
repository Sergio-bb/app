package solidappservice.cm.com.presenteapp.entities.estadocuenta.dto;

import java.util.ArrayList;

import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 26/11/2015.
 */
public class ProductosPorTipoDR {

    private String n_tipodr;
    private ArrayList<ResponseProducto> productos;

    public ProductosPorTipoDR() {
    }

    public ProductosPorTipoDR(String n_tipodr, ArrayList<ResponseProducto> productos) {
        this.n_tipodr = n_tipodr;
        this.productos = productos;
    }

    public String getN_tipodr() {
        return n_tipodr;
    }

    public void setN_tipodr(String n_tipodr) {
        this.n_tipodr = n_tipodr;
    }

    public ArrayList<ResponseProducto> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<ResponseProducto> productos) {
        this.productos = productos;
    }
}
