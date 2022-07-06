package solidappservice.cm.com.presenteapp.entities.nequi.response;

public class ResponseSendAuthorizacionBalance {

    private String statusCode;
    private String statusDesc;
    private DataAuthorizacionBalance data;

    public ResponseSendAuthorizacionBalance() {
    }

    public ResponseSendAuthorizacionBalance(String statusCode, String statusDesc, DataAuthorizacionBalance data) {
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

    public DataAuthorizacionBalance getData() {
        return data;
    }

    public void setData(DataAuthorizacionBalance data) {
        this.data = data;
    }

    public static class DataAuthorizacionBalance{

        private String authId;

        public DataAuthorizacionBalance() {
        }

        public DataAuthorizacionBalance(String authId) {
            this.authId = authId;
        }

        public String getAuthId() {
            return authId;
        }

        public void setAuthId(String authId) {
            this.authId = authId;
        }
    }

}
