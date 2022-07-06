package solidappservice.cm.com.presenteapp.entities.estadocuenta.request;

import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;

public class RequestMovimientosProducto extends BaseRequest {

    private int k_tipcuent;
    private String v_tipodr;
    private String v_numdoc;

    public RequestMovimientosProducto() {
    }

    public RequestMovimientosProducto(String cedula, String token, int k_tipcuent, String v_tipodr, String v_numdoc) {
        super(cedula, token);
        this.k_tipcuent = k_tipcuent;
        this.v_tipodr = v_tipodr;
        this.v_numdoc = v_numdoc;
    }

    public int getK_tipcuent() {
        return k_tipcuent;
    }

    public void setK_tipcuent(int k_tipcuent) {
        this.k_tipcuent = k_tipcuent;
    }

    public String getV_tipodr() {
        return v_tipodr;
    }

    public void setV_tipodr(String v_tipodr) {
        this.v_tipodr = v_tipodr;
    }

    public String getV_numdoc() {
        return v_numdoc;
    }

    public void setV_numdoc(String v_numdoc) {
        this.v_numdoc = v_numdoc;
    }
}
