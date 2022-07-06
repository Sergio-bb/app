package solidappservice.cm.com.presenteapp.entities.estadocuenta.dto;

import java.util.ArrayList;

import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProductos;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 26/11/2015.
 */
public class ProductosPorTipoDR {

    private String n_tipodr;
    private ArrayList<ResponseProductos> productos;

    public ProductosPorTipoDR() {
    }

    public ProductosPorTipoDR(String n_tipodr, ArrayList<ResponseProductos> productos) {
        this.n_tipodr = n_tipodr;
        this.productos = productos;
    }

    public String getN_tipodr() {
        return n_tipodr;
    }

    public void setN_tipodr(String n_tipodr) {
        this.n_tipodr = n_tipodr;
    }

    public ArrayList<ResponseProductos> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<ResponseProductos> productos) {
        this.productos = productos;
    }
}
