package solidappservice.cm.com.presenteapp.entities.nequi.response;

public class ResponseDataCommerceQR {

    private String statusCode;
    private String statusDesc;
    private DataReceivedQR data;

    public ResponseDataCommerceQR() {
    }

    public ResponseDataCommerceQR(String statusCode, String statusDesc, DataReceivedQR data) {
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
        private PaymentDataQR paymentData;
        private String commerce;
        private String amount;
        private boolean isEditable;

        public DataReceivedQR() {
        }

        public DataReceivedQR(PaymentDataQR paymentData, String commerce, String amount, boolean isEditable) {
            this.paymentData = paymentData;
            this.commerce = commerce;
            this.amount = amount;
            this.isEditable = isEditable;
        }

        public PaymentDataQR getPaymentData() {
            return paymentData;
        }

        public void setPaymentData(PaymentDataQR paymentData) {
            this.paymentData = paymentData;
        }

        public String getCommerce() {
            return commerce;
        }

        public void setCommerce(String commerce) {
            this.commerce = commerce;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public boolean isEditable() {
            return isEditable;
        }

        public void setEditable(boolean editable) {
            isEditable = editable;
        }
    }

    public class PaymentDataQR {
        private String date;
        private String trnId;
        private Object[] originMoney;
        private String name;
        private String ipAddress;
        private String value;
        private String status;

        public PaymentDataQR() {
        }

        public PaymentDataQR(String date, String trnId, Object[] originMoney, String name, String ipAddress, String value, String status) {
            this.date = date;
            this.trnId = trnId;
            this.originMoney = originMoney;
            this.name = name;
            this.ipAddress = ipAddress;
            this.value = value;
            this.status = status;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTrnId() {
            return trnId;
        }

        public void setTrnId(String trnId) {
            this.trnId = trnId;
        }

        public Object[] getOriginMoney() {
            return originMoney;
        }

        public void setOriginMoney(Object[] originMoney) {
            this.originMoney = originMoney;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
