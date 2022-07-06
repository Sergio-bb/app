package solidappservice.cm.com.presenteapp.entities.nequi.response;

public class ResponsePaymentSuscription {

    private long idTransaccionPresente;
    private String estadoPagoPresente;
    private String idTransaccionNequi;
    private String idPagoNequi;
    private String estadoPagoNequi;
    private NequiReceivedSubscriptionPayment resultNequi;

    public ResponsePaymentSuscription() {
    }

    public ResponsePaymentSuscription(long idTransaccionPresente, String estadoPagoPresente, String idTransaccionNequi, String idPagoNequi, String estadoPagoNequi, NequiReceivedSubscriptionPayment resultNequi) {
        this.idTransaccionPresente = idTransaccionPresente;
        this.estadoPagoPresente = estadoPagoPresente;
        this.idTransaccionNequi = idTransaccionNequi;
        this.estadoPagoNequi = estadoPagoNequi;
        this.estadoPagoNequi = idPagoNequi;
        this.resultNequi = resultNequi;
    }

    public ResponsePaymentSuscription(long idTransaccionPresente, String estadoPagoPresente, String idTransaccionNequi, String idPagoNequi, String estadoPagoNequi) {
        this.idTransaccionPresente = idTransaccionPresente;
        this.estadoPagoPresente = estadoPagoPresente;
        this.idTransaccionNequi = idTransaccionNequi;
        this.idPagoNequi = idPagoNequi;
        this.estadoPagoNequi = estadoPagoNequi;
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

    public NequiReceivedSubscriptionPayment getResultNequi() {
        return resultNequi;
    }
    public void setResultNequi(NequiReceivedSubscriptionPayment resultNequi) {
        this.resultNequi = resultNequi;
    }

    public static class NequiReceivedSubscriptionPayment
    {
        private ReceivedSubscriptionPaymentResponseMessage ResponseMessage;

        public NequiReceivedSubscriptionPayment() {
        }

        public NequiReceivedSubscriptionPayment(ReceivedSubscriptionPaymentResponseMessage responseMessage) {
            ResponseMessage = responseMessage;
        }
        public ReceivedSubscriptionPaymentResponseMessage getResponseMessage() {
            return ResponseMessage;
        }
        public void setResponseMessage(ReceivedSubscriptionPaymentResponseMessage responseMessage) {
            ResponseMessage = responseMessage;
        }
    }

    public static class ReceivedSubscriptionPaymentResponseMessage
    {
        private ReceivedSubscriptionPaymentResponseHeader ResponseHeader;
        private ReceivedSubscriptionPaymentResponseBody ResponseBody;

        public ReceivedSubscriptionPaymentResponseMessage() {
        }

        public ReceivedSubscriptionPaymentResponseMessage(ReceivedSubscriptionPaymentResponseHeader responseHeader, ReceivedSubscriptionPaymentResponseBody responseBody) {
            ResponseHeader = responseHeader;
            ResponseBody = responseBody;
        }
        public ReceivedSubscriptionPaymentResponseHeader getResponseHeader() {
            return ResponseHeader;
        }
        public void setResponseHeader(ReceivedSubscriptionPaymentResponseHeader responseHeader) {
            ResponseHeader = responseHeader;
        }
        public ReceivedSubscriptionPaymentResponseBody getResponseBody() {
            return ResponseBody;
        }
        public void setResponseBody(ReceivedSubscriptionPaymentResponseBody responseBody) {
            ResponseBody = responseBody;
        }
    }

    public static class ReceivedSubscriptionPaymentResponseHeader
    {
        private String Channel;
        private String ResponseDate;
        private ReceivedSubscriptionPaymentStatusResponseHeader Status;
        private String MessageID;
        private String ClientID;
        private ReceivedSubscriptionPaymentDestinationResponseHeader Destination;

        public ReceivedSubscriptionPaymentResponseHeader() {
        }

        public ReceivedSubscriptionPaymentResponseHeader(String channel, String responseDate, String messageID, String clientID, ReceivedSubscriptionPaymentDestinationResponseHeader destination) {
            Channel = channel;
            ResponseDate = responseDate;
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
        public ReceivedSubscriptionPaymentDestinationResponseHeader getDestination() {
            return Destination;
        }
        public void setDestination(ReceivedSubscriptionPaymentDestinationResponseHeader destination) {
            Destination = destination;
        }
    }

    public static class ReceivedSubscriptionPaymentStatusResponseHeader
    {
        private String StatusCode;
        private String StatusDesc;

        public ReceivedSubscriptionPaymentStatusResponseHeader() {
        }

        public ReceivedSubscriptionPaymentStatusResponseHeader(String statusCode, String statusDesc) {
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

    public static class ReceivedSubscriptionPaymentDestinationResponseHeader
    {
        private String ServiceName;
        private String ServiceOperation;
        private String ServiceRegion;
        private String ServiceVersion;

        public ReceivedSubscriptionPaymentDestinationResponseHeader() {
        }

        public ReceivedSubscriptionPaymentDestinationResponseHeader(String serviceName, String serviceOperation, String serviceRegion, String serviceVersion) {
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

    public static class ReceivedSubscriptionPaymentResponseBody
    {
        private ReceivedSubscriptionPaymentAnyResponseBody any;

        public ReceivedSubscriptionPaymentResponseBody() {
        }

        public ReceivedSubscriptionPaymentResponseBody(ReceivedSubscriptionPaymentAnyResponseBody any) {
            this.any = any;
        }

        public ReceivedSubscriptionPaymentAnyResponseBody getAny() {
            return any;
        }
        public void setAny(ReceivedSubscriptionPaymentAnyResponseBody any) {
            this.any = any;
        }
    }

    public static class ReceivedSubscriptionPaymentAnyResponseBody
    {
        private AutomaticPaymentrsResponseBody automaticPaymentRS;

        public ReceivedSubscriptionPaymentAnyResponseBody() {
        }

        public ReceivedSubscriptionPaymentAnyResponseBody(AutomaticPaymentrsResponseBody automaticPaymentRS) {
            this.automaticPaymentRS = automaticPaymentRS;
        }

        public AutomaticPaymentrsResponseBody getAutomaticPaymentRS() {
            return automaticPaymentRS;
        }
        public void setAutomaticPaymentRS(AutomaticPaymentrsResponseBody automaticPaymentRS) {
            this.automaticPaymentRS = automaticPaymentRS;
        }
    }

    public static class AutomaticPaymentrsResponseBody
    {
        private String transactionId;
        private String token;

        public AutomaticPaymentrsResponseBody() {
        }

        public AutomaticPaymentrsResponseBody(String transactionId, String token) {
            this.transactionId = transactionId;
            this.token = token;
        }

        public String getTransactionId() {
            return transactionId;
        }
        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }
        public String getToken() {
            return token;
        }
        public void setToken(String token) {
            this.token = token;
        }
    }

}
