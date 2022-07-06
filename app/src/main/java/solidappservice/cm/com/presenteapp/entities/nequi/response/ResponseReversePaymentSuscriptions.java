package solidappservice.cm.com.presenteapp.entities.nequi.response;

public class ResponseReversePaymentSuscriptions {

    private long  idTransaccion;
    private boolean reversePresente;
    private String estadoPagoPresente;
    private boolean reverseNequi;
    private String estadoPagoNequi;
    private NequiReceivedReversePaySuscription resultNequi;

    public ResponseReversePaymentSuscriptions() {
    }

    public ResponseReversePaymentSuscriptions(long idTransaccion, boolean reversePresente, String estadoPagoPresente, boolean reverseNequi, String estadoPagoNequi, NequiReceivedReversePaySuscription resultNequi) {
        this.idTransaccion = idTransaccion;
        this.reversePresente = reversePresente;
        this.estadoPagoPresente = estadoPagoPresente;
        this.reverseNequi = reverseNequi;
        this.estadoPagoNequi = estadoPagoNequi;
        this.resultNequi = resultNequi;
    }

    public long getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public boolean isReversePresente() {
        return reversePresente;
    }

    public void setReversePresente(boolean reversePresente) {
        this.reversePresente = reversePresente;
    }

    public String getEstadoPagoPresente() {
        return estadoPagoPresente;
    }

    public void setEstadoPagoPresente(String estadoPagoPresente) {
        this.estadoPagoPresente = estadoPagoPresente;
    }

    public boolean isReverseNequi() {
        return reverseNequi;
    }

    public void setReverseNequi(boolean reverseNequi) {
        this.reverseNequi = reverseNequi;
    }

    public String getEstadoPagoNequi() {
        return estadoPagoNequi;
    }

    public void setEstadoPagoNequi(String estadoPagoNequi) {
        this.estadoPagoNequi = estadoPagoNequi;
    }

    public NequiReceivedReversePaySuscription getResultNequi() {
        return resultNequi;
    }

    public void setResultNequi(NequiReceivedReversePaySuscription resultNequi) {
        this.resultNequi = resultNequi;
    }

    public static class NequiReceivedReversePaySuscription
    {
        private ReceivedReversePaySuscResponseMessage ResponseMessage;

        public NequiReceivedReversePaySuscription() {
        }

        public NequiReceivedReversePaySuscription(ReceivedReversePaySuscResponseMessage responseMessage) {
            ResponseMessage = responseMessage;
        }

        public ReceivedReversePaySuscResponseMessage getResponseMessage() {
            return ResponseMessage;
        }

        public void setResponseMessage(ReceivedReversePaySuscResponseMessage responseMessage) {
            ResponseMessage = responseMessage;
        }
    }

    public static class ReceivedReversePaySuscResponseMessage
    {
        private ReceivedReversePaySuscResponseHeader ResponseHeader;
        private ReceivedReversePaySuscResponseBody ResponseBody;

        public ReceivedReversePaySuscResponseMessage() {
        }

        public ReceivedReversePaySuscResponseMessage(ReceivedReversePaySuscResponseHeader responseHeader, ReceivedReversePaySuscResponseBody responseBody) {
            ResponseHeader = responseHeader;
            ResponseBody = responseBody;
        }

        public ReceivedReversePaySuscResponseHeader getResponseHeader() {
            return ResponseHeader;
        }

        public void setResponseHeader(ReceivedReversePaySuscResponseHeader responseHeader) {
            ResponseHeader = responseHeader;
        }

        public ReceivedReversePaySuscResponseBody getResponseBody() {
            return ResponseBody;
        }

        public void setResponseBody(ReceivedReversePaySuscResponseBody responseBody) {
            ResponseBody = responseBody;
        }
    }

    public static class ReceivedReversePaySuscResponseHeader
    {
        private String Channel;
        private String ResponseDate;
        private ReceivedReversePaySuscStatusResponseHeader Status;
        private String MessageID;
        private String ClientID;
        private ReceivedReversePaySuscDestinationResponseHeader Destination;

        public ReceivedReversePaySuscResponseHeader() {
        }

        public ReceivedReversePaySuscResponseHeader(String channel, String responseDate, ReceivedReversePaySuscStatusResponseHeader status, String messageID, String clientID, ReceivedReversePaySuscDestinationResponseHeader destination) {
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
        public ReceivedReversePaySuscStatusResponseHeader getStatus() {
            return Status;
        }
        public void setStatus(ReceivedReversePaySuscStatusResponseHeader status) {
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
        public ReceivedReversePaySuscDestinationResponseHeader getDestination() {
            return Destination;
        }
        public void setDestination(ReceivedReversePaySuscDestinationResponseHeader destination) {
            Destination = destination;
        }
    }

    public static class ReceivedReversePaySuscStatusResponseHeader
    {
        private String StatusCode;
        private String StatusDesc;

        public ReceivedReversePaySuscStatusResponseHeader() {
        }

        public ReceivedReversePaySuscStatusResponseHeader(String statusCode, String statusDesc) {
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

    public static class ReceivedReversePaySuscDestinationResponseHeader
    {
        private String ServiceName;
        private String ServiceOperation;
        private String ServiceRegion;
        private String ServiceVersion;

        public ReceivedReversePaySuscDestinationResponseHeader() {
        }

        public ReceivedReversePaySuscDestinationResponseHeader(String serviceName, String serviceOperation, String serviceRegion, String serviceVersion) {
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

    public static class ReceivedReversePaySuscResponseBody
    {
        private ReceivedReversePaySuscAnyResponseBody any;

        public ReceivedReversePaySuscResponseBody() {
        }

        public ReceivedReversePaySuscResponseBody(ReceivedReversePaySuscAnyResponseBody any) {
            this.any = any;
        }

        public ReceivedReversePaySuscAnyResponseBody getAny() {
            return any;
        }

        public void setAny(ReceivedReversePaySuscAnyResponseBody any) {
            this.any = any;
        }
    }

    public static class ReceivedReversePaySuscAnyResponseBody
    {
        private ReceivedReversePaySuscReversionrsResponseBody reversionRS;

        public ReceivedReversePaySuscAnyResponseBody() {
        }

        public ReceivedReversePaySuscAnyResponseBody(ReceivedReversePaySuscReversionrsResponseBody reversionRS) {
            this.reversionRS = reversionRS;
        }

        public ReceivedReversePaySuscReversionrsResponseBody getReversionRS() {
            return reversionRS;
        }

        public void setReversionRS(ReceivedReversePaySuscReversionrsResponseBody reversionRS) {
            this.reversionRS = reversionRS;
        }
    }

    public static class ReceivedReversePaySuscReversionrsResponseBody
    {
    }

}
