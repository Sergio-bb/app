package solidappservice.cm.com.presenteapp.entities.adelantonomina.response;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 04/07/2020.
 */

public class ResponseTips {

    private int k_TIP;
    private String n_TIP;

    public ResponseTips() {
    }

    public ResponseTips(int k_TIP, String n_TIP) {
        this.k_TIP = k_TIP;
        this.n_TIP = n_TIP;
    }

    public int getK_TIP() {
        return k_TIP;
    }

    public void setK_TIP(int k_TIP) {
        this.k_TIP = k_TIP;
    }

    public String getN_TIP() {
        return n_TIP;
    }

    public void setN_TIP(String n_TIP) {
        this.n_TIP = n_TIP;
    }
}