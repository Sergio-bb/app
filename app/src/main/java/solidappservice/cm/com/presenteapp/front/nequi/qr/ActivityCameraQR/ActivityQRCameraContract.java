package solidappservice.cm.com.presenteapp.front.nequi.qr.ActivityCameraQR;

import android.graphics.Bitmap;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestGetDataCommerceQR;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseDataCommerceQR;
import solidappservice.cm.com.presenteapp.front.nequi.qr.FragmentQRCamera.FragmentQRCameraContract;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;

public interface ActivityQRCameraContract {

    interface View{
        void validatePermissionsCamera();
        void validatePermissionsReadGallery();
        void showDialogPermissions(String typeOfPermission, int requestCode);
        void requestPermits();
        void scanQRImage(Bitmap bitmap);
        void showFragmentNequi(IFragmentCoordinator.Pantalla pantalla);
        void scannerCodeQR(String text);
        void resultScannerCodeQR(ResponseDataCommerceQR.DataReceivedQR dataCommerceQR);
        void showDialogErrorScannerQR();
        void showDialogErrorQR();
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showDialogError(String title, String message);
        void showErrorTimeOut();
        void showDataFetchError(String title, String message) throws Exception;
        void showExpiredToken(String message) throws Exception;
    }

    interface Presenter{
        void fetchDataCommerceQR(RequestGetDataCommerceQR text);
    }

    interface Model{
        void getDataCommerceQR(RequestGetDataCommerceQR body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessDataCommerceQR(Response<BaseResponseNequi<T>> response);
        <T> void onErrorDataCommerceQR(Response<BaseResponseNequi<T>> response);
        <T> void onExpiredToken(Response<BaseResponseNequi<T>> response) throws Exception;
        void onFailureDataCommerceQR(Throwable t, boolean isErrorTimeOut);
    }

}
