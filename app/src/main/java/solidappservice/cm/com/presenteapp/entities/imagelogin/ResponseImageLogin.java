package solidappservice.cm.com.presenteapp.entities.imagelogin;

public class ResponseImageLogin {

    private String imageUrl;

    public ResponseImageLogin() {
    }

    public ResponseImageLogin(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
