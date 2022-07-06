package solidappservice.cm.com.presenteapp.entities.nequi.response;

public class ResponsePaymentDispersionSuccess {

    private ResponseMessage responseMessage;

    public ResponsePaymentDispersionSuccess(ResponseMessage responseMessage) {
        this.responseMessage = responseMessage;
    }

    public ResponseMessage getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(ResponseMessage responseMessage) {
        this.responseMessage = responseMessage;
    }


    public static class ResponseMessage{

        private ResponseHeader responseHeader;
        private ResponseBody responseBody;

        public ResponseMessage() {
        }

        public ResponseMessage(ResponseHeader responseHeader, ResponseBody responseBody) {
            this.responseHeader = responseHeader;
            this.responseBody = responseBody;
        }

        public ResponseHeader getResponseHeader() {
            return responseHeader;
        }

        public void setResponseHeader(ResponseHeader responseHeader) {
            this.responseHeader = responseHeader;
        }

        public ResponseBody getResponseBody() {
            return responseBody;
        }

        public void setResponseBody(ResponseBody responseBody) {
            this.responseBody = responseBody;
        }
    }


    public static class ResponseHeader{
        private String channel;
        private String requestDate;
        private Status status;
        private String messageID;
        private String clientID;
        private Destination destination;

        public ResponseHeader() {
        }

        public ResponseHeader(String channel, String requestDate,Status status, String messageID, String clientID, Destination destination) {
            this.channel = channel;
            this.requestDate = requestDate;
            this.status = status;
            this.messageID = messageID;
            this.clientID = clientID;
            this.destination = destination;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getRequestDate() {
            return requestDate;
        }

        public void setRequestDate(String requestDate) {
            this.requestDate = requestDate;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public String getMessageID() {
            return messageID;
        }

        public void setMessageID(String messageID) {
            this.messageID = messageID;
        }

        public String getClientID() {
            return clientID;
        }

        public void setClientID(String clientID) {
            this.clientID = clientID;
        }

        public Destination getDestination() {
            return destination;
        }

        public void setDestination(Destination destination) {
            this.destination = destination;
        }
    }


    public static class Status{
        private String statusCode;
        private String statusDesc;

        public Status() { }

        public Status(String statusCode,String statusDesc) {
            this.statusCode = statusCode;
            this.statusDesc = statusDesc;
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
    }

    public static class Destination{
        private String serviceName;
        private String serviceOperation;
        private String serviceRegion;
        private String serviceVersion;

        public Destination() {
        }

        public Destination(String serviceName, String serviceOperation, String serviceRegion, String serviceVersion) {
            this.serviceName = serviceName;
            this.serviceOperation = serviceOperation;
            this.serviceRegion = serviceRegion;
            this.serviceVersion = serviceVersion;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getServiceOperation() {
            return serviceOperation;
        }

        public void setServiceOperation(String serviceOperation) {
            this.serviceOperation = serviceOperation;
        }

        public String getServiceRegion() {
            return serviceRegion;
        }

        public void setServiceRegion(String serviceRegion) {
            this.serviceRegion = serviceRegion;
        }

        public String getServiceVersion() {
            return serviceVersion;
        }

        public void setServiceVersion(String serviceVersion) {
            this.serviceVersion = serviceVersion;
        }
    }


    //Body
    public static class ResponseBody{

        private Any any;

        public ResponseBody() {
        }

        public ResponseBody(Any any) {
            this.any = any;
        }

        public Any getAny() {
            return any;
        }

        public void setAny(Any any) {
            this.any = any;
        }
    }

    public static class Any{

        public Any() {
        }

    }
    }

