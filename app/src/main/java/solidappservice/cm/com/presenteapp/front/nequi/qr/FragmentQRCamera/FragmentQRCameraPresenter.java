package solidappservice.cm.com.presenteapp.front.nequi.qr.FragmentQRCamera;

import androidx.annotation.NonNull;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestGetDataCommerceQR;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseDataCommerceQR;

public class FragmentQRCameraPresenter implements FragmentQRCameraContract.Presenter, FragmentQRCameraContract.APIListener{

    FragmentQRCameraView view;
    FragmentQRCameraModel model;

    public FragmentQRCameraPresenter(@NonNull FragmentQRCameraView view, @NonNull FragmentQRCameraModel model) {
        this.view = view;
        this.model = model;
    }

}
