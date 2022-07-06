package solidappservice.cm.com.presenteapp.entities.nequi.response;

public class ResponsePaymentByQR {

    private String statusCode;
    private String statusDesc;
    private DataReceivedQR data;

    public ResponsePaymentByQR() {
    }

    public ResponsePaymentByQR(String statusCode, String statusDesc, DataReceivedQR data) {
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

    public DataReceivedQR getData() {
        return data;
    }

    public void setData(DataReceivedQR data) {
        this.data = data;
    }

    public class DataReceivedQR {
        private String a;
        private String paymentMessageId;

        public DataReceivedQR() {
        }

        public DataReceivedQR(String a, String paymentMessageId) {
            this.a = a;
            this.paymentMessageId = paymentMessageId;
        }

        public String getA() {

            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getPaymentMessageId() {
            return paymentMessageId;
        }

        public void setPaymentMessageId(String paymentMessageId) {
            this.paymentMessageId = paymentMessageId;
        }
    }

}
