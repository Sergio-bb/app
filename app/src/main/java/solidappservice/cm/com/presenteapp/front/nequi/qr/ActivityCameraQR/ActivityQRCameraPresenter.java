package solidappservice.cm.com.presenteapp.front.nequi.qr.ActivityCameraQR;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestGetDataCommerceQR;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseDataCommerceQR;

public class ActivityQRCameraPresenter implements ActivityQRCameraContract.Presenter, ActivityQRCameraContract.APIListener{

    ActivityQRCameraView view;
    ActivityQRCameraModel model;

    public ActivityQRCameraPresenter(ActivityQRCameraView view, ActivityQRCameraModel model ) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchDataCommerceQR(RequestGetDataCommerceQR request ) {
        view.showProgressDialog("Validando código QR");
        model.getDataCommerceQR(request, this);
    }

    @Override
    public <T> void onSuccessDataCommerceQR(Response<BaseResponseNequi<T>> response) {
        try{
            ResponseDataCommerceQR dataCommerceQR = (ResponseDataCommerceQR)response.body().getResult();
            if(dataCommerceQR != null && dataCommerceQR.getData() != null){
                view.hideProgressDialog();
                view.resultScannerCodeQR(dataCommerceQR.getData());
            }else{
                view.hideProgressDialog();
                view.showDialogErrorScannerQR();
            }
        }catch(Exception ex){
            view.hideProgressDialog();
            view.showDialogErrorScannerQR();
        }
    }

    @Override
    public <T> void onErrorDataCommerceQR(Response<BaseResponseNequi<T>> response) {
        view.hideProgressDialog();
        view.showDialogErrorQR();
    }

    @Override
    public void onFailureDataCommerceQR(Throwable t, boolean isErrorTimeOut) {
        view.hideProgressDialog();
        view.showDialogErrorQR();
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponseNequi<T>> response) throws Exception {
        view.hideProgressDialog();
        view.showExpiredToken(response.body().getErrorToken());
    }

}
