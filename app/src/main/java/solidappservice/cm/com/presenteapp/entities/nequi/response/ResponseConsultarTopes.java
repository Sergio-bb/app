package solidappservice.cm.com.presenteapp.entities.nequi.response;

public class ResponseConsultarTopes {

    private Integer minimoTransferencia;
    private Integer maximoTransferencia;
    private Integer maximoDia;
    private Integer maximoMes;

    public ResponseConsultarTopes() {
    }

    public ResponseConsultarTopes(Integer minimoTransferencia, Integer maximoTransferencia, Integer maximoDia, Integer maximoMes) {
        this.minimoTransferencia = minimoTransferencia;
        this.maximoTransferencia = maximoTransferencia;
        this.maximoDia = maximoDia;
        this.maximoMes = maximoMes;
    }

    public Integer getMinimoTransferencia() {
        return minimoTransferencia;
    }

    public void setMinimoTransferencia(Integer minimoTransferencia) {
        this.minimoTransferencia = minimoTransferencia;
    }

    public Integer getMaximoTransferencia() {
        return maximoTransferencia;
    }

    public void setMaximoTransferencia(Integer maximoTransferencia) {
        this.maximoTransferencia = maximoTransferencia;
    }

    public Integer getMaximoDia() {
        return maximoDia;
    }

    public void setMaximoDia(Integer maximoDia) {
        this.maximoDia = maximoDia;
    }

    public Integer getMaximoMes() {
        return maximoMes;
    }

    public void setMaximoMes(Integer maximoMes) {
        this.maximoMes = maximoMes;
    }
}
