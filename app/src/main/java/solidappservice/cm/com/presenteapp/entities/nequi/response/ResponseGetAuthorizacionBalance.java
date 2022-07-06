package solidappservice.cm.com.presenteapp.entities.nequi.response;

public class ResponseGetAuthorizacionBalance {

    private String statusCode;
    private String statusDesc;
    private DataGetAuthorizacionBalance data;

    public ResponseGetAuthorizacionBalance() {
    }

    public ResponseGetAuthorizacionBalance(String statusCode, String statusDesc, DataGetAuthorizacionBalance data) {
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

    public DataGetAuthorizacionBalance getData() {
        return data;
    }

    public void setData(DataGetAuthorizacionBalance data) {
        this.data = data;
    }


    public static class DataGetAuthorizacionBalance{

        private String status;

        public DataGetAuthorizacionBalance() {
        }

        public DataGetAuthorizacionBalance(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

}
