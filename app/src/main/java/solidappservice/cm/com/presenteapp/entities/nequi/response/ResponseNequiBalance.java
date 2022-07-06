package solidappservice.cm.com.presenteapp.entities.nequi.response;

public class ResponseNequiBalance {

    private String statusCode;
    private String statusDesc;
    private DataNequiBalance data;

    public ResponseNequiBalance() {
    }

    public ResponseNequiBalance(String statusCode, String statusDesc, DataNequiBalance data) {
        this.statusCode = statusCode;
        this.statusDesc = statusDesc;
        this.data = data;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public DataNequiBalance getData() {
        return data;
    }

    public void setData(DataNequiBalance data) {
        this.data = data;
    }


    public static class DataNequiBalance{

        private String balance;

        public DataNequiBalance() {
        }

        public DataNequiBalance(String balance) {
            this.balance = balance;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }
    }

}
