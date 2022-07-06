package solidappservice.cm.com.presenteapp.entities.nequi.response;

public class ResponseCheckStatusPaymentSuscription {

    private long idTransaccionPresente;
    private String estadoPagoPresente;
    private String idTransaccionNequi;
    private String idPagoNequi;
    private String estadoPagoNequi;
    private NequiReceivedStatusPaySusc resultNequi;

    public ResponseCheckStatusPaymentSuscription() {
    }

    public ResponseCheckStatusPaymentSuscription(long idTransaccionPresente, String estadoPagoPresente, String idTransaccionNequi, String idPagoNequi, String estadoPagoNequi, NequiReceivedStatusPaySusc resultNequi) {
        this.idTransaccionPresente = idTransaccionPresente;
        this.estadoPagoPresente = estadoPagoPresente;
        this.idTransaccionNequi = idTransaccionNequi;
        this.idPagoNequi = idPagoNequi;
        this.estadoPagoNequi = estadoPagoNequi;
        this.resultNequi = resultNequi;
    }

    public long getIdTransaccionPresente() {
        return idTransaccionPresente;
    }

    public void setIdTransaccionPresente(long idTransaccionPresente) {
        this.idTransaccionPresente = idTransaccionPresente;
    }

    public String getEstadoPagoPresente() {
        return estadoPagoPresente;
    }

    public void setEstadoPagoPresente(String estadoPagoPresente) {
        this.estadoPagoPresente = estadoPagoPresente;
    }

    public String getIdTransaccionNequi() {
        return idTransaccionNequi;
    }

    public void setIdTransaccionNequi(String idTransaccionNequi) {
        this.idTransaccionNequi = idTransaccionNequi;
    }

    public String getIdPagoNequi() {
        return idPagoNequi;
    }

    public void setIdPagoNequi(String idPagoNequi) {
        this.idPagoNequi = idPagoNequi;
    }

    public String getEstadoPagoNequi() {
        return estadoPagoNequi;
    }

    public void setEstadoPagoNequi(String estadoPagoNequi) {
        this.estadoPagoNequi = estadoPagoNequi;
    }

    public NequiReceivedStatusPaySusc getResultNequi() {
        return resultNequi;
    }

    public void setResultNequi(NequiReceivedStatusPaySusc resultNequi) {
        this.resultNequi = resultNequi;
    }

    public class NequiReceivedStatusPaySusc
    {
        private StatusPaySuscResponseMessage ResponseMessage;

        public NequiReceivedStatusPaySusc() {
        }

        public NequiReceivedStatusPaySusc(StatusPaySuscResponseMessage responseMessage) {
            ResponseMessage = responseMessage;
        }

        public StatusPaySuscResponseMessage getResponseMessage() {
            return ResponseMessage;
        }
        public void setResponseMessage(StatusPaySuscResponseMessage responseMessage) {
            ResponseMessage = responseMessage;
        }
    }

    public class StatusPaySuscResponseMessage
    {
        private StatusPaySuscResponseHeader ResponseHeader;
        private StatusPaySuscResponseBody ResponseBody;

        public StatusPaySuscResponseMessage() {
        }

        public StatusPaySuscResponseMessage(StatusPaySuscResponseHeader responseHeader, StatusPaySuscResponseBody responseBody) {
            ResponseHeader = responseHeader;
            ResponseBody = responseBody;
        }

        public StatusPaySuscResponseHeader getResponseHeader() {
            return ResponseHeader;
        }
        public void setResponseHeader(StatusPaySuscResponseHeader responseHeader) {
            ResponseHeader = responseHeader;
        }
        public StatusPaySuscResponseBody getResponseBody() {
            return ResponseBody;
        }
        public void setResponseBody(StatusPaySuscResponseBody responseBody) {
            ResponseBody = responseBody;
        }
    }

    public class StatusPaySuscResponseHeader
    {
        private String Channel;
        private String ResponseDate;
        private StatusPaySuscStatusResponseHeader Status;
        private String MessageID;
        private String ClientID;
        private StatusPaySuscDestinationResponseHeader Destination;

        public StatusPaySuscResponseHeader() {
        }

        public StatusPaySuscResponseHeader(String channel, String responseDate, StatusPaySuscStatusResponseHeader status, String messageID, String clientID, StatusPaySuscDestinationResponseHeader destination) {
            Channel = channel;
            ResponseDate = responseDate;
            Status = status;
            MessageID = messageID;
            ClientID = clientID;
            Destination = destination;
        }

        public String getChannel() {
            return Channel;
        }
        public void setChannel(String channel) {
            Channel = channel;
        }
        public String getResponseDate() {
            return ResponseDate;
        }
        public void setResponseDate(String responseDate) {
            ResponseDate = responseDate;
        }
        public StatusPaySuscStatusResponseHeader getStatus() {
            return Status;
        }
        public void setStatus(StatusPaySuscStatusResponseHeader status) {
            Status = status;
        }
        public String getMessageID() {
            return MessageID;
        }
        public void setMessageID(String messageID) {
            MessageID = messageID;
        }
        public String getClientID() {
            return ClientID;
        }
        public void setClientID(String clientID) {
            ClientID = clientID;
        }
        public StatusPaySuscDestinationResponseHeader getDestination() {
            return Destination;
        }
        public void setDestination(StatusPaySuscDestinationResponseHeader destination) {
            Destination = destination;
        }
    }

    public class StatusPaySuscStatusResponseHeader
    {
        private String StatusCode;
        private String StatusDesc;

        public StatusPaySuscStatusResponseHeader() {
        }

        public StatusPaySuscStatusResponseHeader(String statusCode, String statusDesc) {
            StatusCode = statusCode;
            StatusDesc = statusDesc;
        }

        public String getStatusCode() {
            return StatusCode;
        }
        public void setStatusCode(String statusCode) {
            StatusCode = statusCode;
        }
        public String getStatusDesc() {
            return StatusDesc;
        }
        public void setStatusDesc(String statusDesc) {
            StatusDesc = statusDesc;
        }
    }

    public class StatusPaySuscDestinationResponseHeader
    {
        private String ServiceName;
        private String ServiceOperation;
        private String ServiceRegion;
        private String ServiceVersion;

        public StatusPaySuscDestinationResponseHeader() {
        }

        public StatusPaySuscDestinationResponseHeader(String serviceName, String serviceOperation, String serviceRegion, String serviceVersion) {
            ServiceName = serviceName;
            ServiceOperation = serviceOperation;
            ServiceRegion = serviceRegion;
            ServiceVersion = serviceVersion;
        }

        public String getServiceName() {
            return ServiceName;
        }
        public void setServiceName(String serviceName) {
            ServiceName = serviceName;
        }
        public String getServiceOperation() {
            return ServiceOperation;
        }
        public void setServiceOperation(String serviceOperation) {
            ServiceOperation = serviceOperation;
        }
        public String getServiceRegion() {
            return ServiceRegion;
        }
        public void setServiceRegion(String serviceRegion) {
            ServiceRegion = serviceRegion;
        }
        public String getServiceVersion() {
            return ServiceVersion;
        }
        public void setServiceVersion(String serviceVersion) {
            ServiceVersion = serviceVersion;
        }
    }


    public class StatusPaySuscResponseBody
    {
        private StatusPaySuscAnyResponseBody any;

        public StatusPaySuscResponseBody() {
        }

        public StatusPaySuscResponseBody(StatusPaySuscAnyResponseBody any) {
            this.any = any;
        }

        public StatusPaySuscAnyResponseBody getAny() {
            return any;
        }
        public void setAny(StatusPaySuscAnyResponseBody any) {
            this.any = any;
        }
    }

    public class StatusPaySuscAnyResponseBody
    {
        private StatusPaySuscGetStatusPaymentRS getStatusPaymentRS;

        public StatusPaySuscAnyResponseBody() {
        }

        public StatusPaySuscAnyResponseBody(StatusPaySuscGetStatusPaymentRS getStatusPaymentRS) {
            this.getStatusPaymentRS = getStatusPaymentRS;
        }

        public StatusPaySuscGetStatusPaymentRS getGetStatusPaymentRS() {
            return getStatusPaymentRS;
        }
        public void setGetStatusPaymentRS(StatusPaySuscGetStatusPaymentRS getStatusPaymentRS) {
            this.getStatusPaymentRS = getStatusPaymentRS;
        }
    }

    public class StatusPaySuscGetStatusPaymentRS
    {
        private String status;
        private String name;
        private String value;
        private String date;
        private String trnId;
        private StatusPaySuscOriginMoneyResponseBody[] originMoney;
        private String ipAddress;

        public StatusPaySuscGetStatusPaymentRS() {
        }

        public StatusPaySuscGetStatusPaymentRS(String status, String name, String value, String date, String trnId, StatusPaySuscOriginMoneyResponseBody[] originMoney, String ipAddress) {
            this.status = status;
            this.name = name;
            this.value = value;
            this.date = date;
            this.trnId = trnId;
            this.originMoney = originMoney;
            this.ipAddress = ipAddress;
        }

        public String getStatus() {
            return status;
        }
        public void setStatus(String status) {
            this.status = status;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
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
        public StatusPaySuscOriginMoneyResponseBody[] getOriginMoney() {
            return originMoney;
        }
        public void setOriginMoney(StatusPaySuscOriginMoneyResponseBody[] originMoney) {
            this.originMoney = originMoney;
        }
        public String getIpAddress() {
            return ipAddress;
        }
        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }
    }

    public class StatusPaySuscOriginMoneyResponseBody
    {
        private String name;
        private String pocketType;
        private String value;

        public StatusPaySuscOriginMoneyResponseBody() {
        }

        public StatusPaySuscOriginMoneyResponseBody(String name, String pocketType, String value) {
            this.name = name;
            this.pocketType = pocketType;
            this.value = value;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getPocketType() {
            return pocketType;
        }
        public void setPocketType(String pocketType) {
            this.pocketType = pocketType;
        }
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
    }

}
