package solidappservice.cm.com.presenteapp.front.nequi.qr.FragmentQRCamera;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestGetDataCommerceQR;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseDataCommerceQR;

public interface FragmentQRCameraContract {

    interface View{
        void configureScannerQR();
    }

    interface Presenter{
    }

    interface Model{
    }

    interface APIListener{
    }

}
