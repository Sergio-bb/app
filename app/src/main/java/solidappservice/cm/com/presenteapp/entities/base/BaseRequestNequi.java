package solidappservice.cm.com.presenteapp.entities.base;

public class BaseRequestNequi extends BaseRequest{

    private String documento;
    private String numTelefono;

    public BaseRequestNequi() {
    }

    public BaseRequestNequi(String cedula, String token, String numTelefono) {
        super(cedula, token);
        this.numTelefono = numTelefono;
    }

    public BaseRequestNequi(String documento, String token) {
        super(documento, token);
        this.documento = documento;
    }

    public String getDocumento() {
        return documento;
    }
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNumTelefono() {
        return numTelefono;
    }

    public void setNumTelefono(String numTelefono) {
        this.numTelefono = numTelefono;
    }
}
