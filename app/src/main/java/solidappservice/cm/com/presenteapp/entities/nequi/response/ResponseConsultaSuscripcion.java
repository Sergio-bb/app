package solidappservice.cm.com.presenteapp.entities.nequi.response;

import solidappservice.cm.com.presenteapp.entities.nequi.dto.SuscriptionData;

public class ResponseConsultaSuscripcion {

    private SuscriptionData datosSuscripcion;
    private GetSuscriptionNequi resultNequi;

    public ResponseConsultaSuscripcion() {
    }

    public ResponseConsultaSuscripcion(SuscriptionData datosSuscripcion, GetSuscriptionNequi resultNequi) {
        this.datosSuscripcion = datosSuscripcion;
        this.resultNequi = resultNequi;
    }

    public SuscriptionData getDatosSuscripcion() {
        return datosSuscripcion;
    }

    public void setDatosSuscripcion(SuscriptionData datosSuscripcion) {
        this.datosSuscripcion = datosSuscripcion;
    }

    public GetSuscriptionNequi getResultNequi() {
        return resultNequi;
    }

    public void setResultNequi(GetSuscriptionNequi resultNequi) {
        this.resultNequi = resultNequi;
    }


    public static class GetSuscriptionNequi {

        private ResponseMessage responseMessage;

        public GetSuscriptionNequi() {
        }

        public GetSuscriptionNequi(ResponseMessage responseMessage) {
            this.responseMessage = responseMessage;
        }

        public ResponseMessage getResponseMessage() {
            return responseMessage;
        }

        public void setResponseMessage(ResponseMessage responseMessage) {
            this.responseMessage = responseMessage;
        }
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
        private String responseDate;
        private Status status;
        private String messageID;
        private String clientID;
        private Destination destination;

        public ResponseHeader() {
        }

        public ResponseHeader(String channel, String responseDate, Status status, String messageID, String clientID, Destination destination) {
            this.channel = channel;
            this.responseDate = responseDate;
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

        public String getResponseDate() {
            return responseDate;
        }

        public void setResponseDate(String responseDate) {
            this.responseDate = responseDate;
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
        private GetSubscriptionRS getSubscriptionRS;

        public Any() {
        }

        public Any(GetSubscriptionRS getSubscriptionRS) {
            this.getSubscriptionRS = getSubscriptionRS;
        }

        public GetSubscriptionRS getGetSubscriptionRS() {
            return getSubscriptionRS;
        }

        public void setGetSubscriptionRS(GetSubscriptionRS getSubscriptionRS) {
            this.getSubscriptionRS = getSubscriptionRS;
        }
    }


    //CLASE GETSUBSCRIPTIONRS
    public static class GetSubscriptionRS{
        private Subscription subscription;

        public GetSubscriptionRS() {
        }

        public GetSubscriptionRS(Subscription subscription) {
            this.subscription = subscription;
        }

        public Subscription getSubscription() {
            return subscription;
        }

        public void setSubscription(Subscription subscription) {
            this.subscription = subscription;
        }
    }


    //CLASE SUBCRIPTION
    public static class Subscription{
        private String dateCreated;
        private String status;
        private String region;
        private String name;
        private String merchantId;
        private String namePocket;

        public Subscription() {
        }

        public Subscription(String dateCreated, String status, String region, String name, String merchantId, String namePocket) {
            this.dateCreated = dateCreated;
            this.status = status;
            this.region = region;
            this.name = name;
            this.merchantId = merchantId;
            this.namePocket = namePocket;
        }

        public String getDateCreated() {
            return dateCreated;
        }

        public void setDateCreated(String dateCreated) {
            this.dateCreated = dateCreated;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        public String getNamePocket() {
            return namePocket;
        }

        public void setNamePocket(String namePocket) {
            this.namePocket = namePocket;
        }
    }
}
