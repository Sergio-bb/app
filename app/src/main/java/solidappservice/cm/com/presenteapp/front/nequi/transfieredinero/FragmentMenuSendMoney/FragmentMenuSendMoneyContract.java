package solidappservice.cm.com.presenteapp.front.nequi.transfieredinero.FragmentMenuSendMoney;


import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.nequi.dto.SuscriptionData;

public interface FragmentMenuSendMoneyContract {

    interface View{
        void fetchSuscriptionNequi();
        void requiredSuscription();
        void notRequiredSuscription();
//        void validateSuscriptionNequi();
//        void redirectSuscriptionRequired();
        void saveSuscriptionData(SuscriptionData datosSuscripcion);
        void showSectionTransferNequiMenu();
        void hideSectionTransferNequiMenu();
        void showContentSendMoneyMenu();
        void hideContentSendMoneyMenu();
        void expandSectionPresenteTransfer();
        void shrinkSectionPresenteTransfer();
        void expandSectionNequiTransfer();
        void shrinkSectionNequiTransfer();
        void showCircularProgressBar(String message);
        void hideCircularProgressBar();
        void showErrorWithRefresh();
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);

    }

    interface Presenter{
        void fetchSuscriptionNequi(BaseRequest body);
//        void validateSuscriptionNequi(BaseRequest body);
    }

    interface Model{
        void getSuscriptionNequi(BaseRequest body, final APIListener listener);
//        void validateSuscriptionNequi(BaseRequest body, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessGetSuscriptionNequi(Response<BaseResponseNequi<T>> response);
        <T> void onErrorGetSuscriptionNequi(Response<BaseResponseNequi<T>> response);
        void onFailureGetSuscriptionNequiNequi(Throwable t, boolean isErrorTimeOut);

//        <T> void onSuccessValidateSuscriptionNequi(Response<BaseResponseNequi<T>> response);
//        <T> void onErrorValidateSuscriptionNequi(Response<BaseResponseNequi<T>> response);
//        void onFailureValidateSuscriptionNequi(Throwable t, boolean isErrorTimeOut);

        <T> void onExpiredToken(Response<BaseResponseNequi<T>> response);
//        <T> void onSuccess(Response<BaseResponse<T>> response);
//        <T> void onExpiredToken(Response<BaseResponse<T>> response);
//        <T> void onError(Response<BaseResponse<T>> response);
//        void onFailure(Throwable t);
    }

}

