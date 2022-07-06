package solidappservice.cm.com.presenteapp.entities.nequi.response;

import solidappservice.cm.com.presenteapp.entities.nequi.dto.SuscriptionData;

public class ResponsePrueba {
    public ResponsePrueba(){}
    public ResponsePrueba(SuscriptionData datosSuscripcion, String resultNequi) {
        this.datosSuscripcion = datosSuscripcion;
        this.resultNequi = resultNequi;
    }

    public SuscriptionData getDatosSuscripcion() {
        return datosSuscripcion;
    }

    public void setDatosSuscripcion(SuscriptionData datosSuscripcion) {
        this.datosSuscripcion = datosSuscripcion;
    }

    public String getResultNequi() {
        return resultNequi;
    }

    public void setResultNequi(String resultNequi) {
        this.resultNequi = resultNequi;
    }

    private SuscriptionData datosSuscripcion;
    private String resultNequi;

}
