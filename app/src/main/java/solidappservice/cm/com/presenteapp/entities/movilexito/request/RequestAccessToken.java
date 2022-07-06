package solidappservice.cm.com.presenteapp.entities.movilexito.request;

public class RequestAccessToken {

    private String clientId;
    private String clientSecret;
    private String grantType;
    private String scope;

    public RequestAccessToken() {
    }

    public RequestAccessToken(String clientId, String clientSecret, String grantType, String scope) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.grantType = grantType;
        this.scope = scope;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
