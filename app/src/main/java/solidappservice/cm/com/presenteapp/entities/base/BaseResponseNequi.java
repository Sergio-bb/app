package solidappservice.cm.com.presenteapp.entities.base;

public class BaseResponseNequi<T> {

    private T result;
    private boolean response;
    private String errorToken;
    private String message;
    private String href;

    public BaseResponseNequi() {
    }

    public BaseResponseNequi(T result, boolean response, String errorToken, String message, String href) {
        this.result = result;
        this.response = response;
        this.errorToken = errorToken;
        this.message = message;
        this.href = href;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public String getErrorToken() {
        return errorToken;
    }

    public void setErrorToken(String errorToken) {
        this.errorToken = errorToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
