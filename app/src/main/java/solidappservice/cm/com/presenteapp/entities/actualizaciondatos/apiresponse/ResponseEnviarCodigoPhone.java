package solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse;

public class ResponseEnviarCodigoPhone {

    private String MessageText;
    private String Status;
    private long TransactionNumber;

    public ResponseEnviarCodigoPhone() {
    }

    public ResponseEnviarCodigoPhone(String messageText, String status, long transactionNumber) {
        MessageText = messageText;
        Status = status;
        TransactionNumber = transactionNumber;
    }

    public String getMessageText() {
        return MessageText;
    }

    public void setMessageText(String messageText) {
        MessageText = messageText;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public long getTransactionNumber() {
        return TransactionNumber;
    }

    public void setTransactionNumber(long transactionNumber) {
        TransactionNumber = transactionNumber;
    }

}
