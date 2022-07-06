package solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.movilexito.FragmentLandingME;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseConsultarDatosAsociado;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestEmail;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseParametrosAPP;

public interface FragmentLandingMEContract {

    interface View{
        void fetchUrlLandingMovilExito();
        void resultUrlLandingMovilExito(ResponseParametrosAPP paramMovilExito);
        void fetchAssociatedData();
        void openLandingMovilExito(ResponseConsultarDatosAsociado asociado);
        void showContentWebView();
        void showCircularProgressBar(String message);
        void hideCircularProgressBar();
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showDataFetchError(String message);
        void showErrorTimeOut();
        void showExpiredToken(String message);
    }

    interface Presenter{
        void getUrlLandingMovilExito();
        void getAssociatedData(BaseRequest request);
//        void postEmailFallo(String contenidoEmail);
    }

    interface Model{
        void getAssociatedData(final APIListener listener, BaseRequest baseRequest);
        void getUrlLandingMovilExito(final APIListener listener);
//        void postEmail(final APIListener listener, RequestEmail email);
    }

    interface APIListener{
        <T> void onSuccessGetUrlLandingMovilExito(Response<BaseResponse<T>> response);
        <T> void onErrorGetUrlLandingMovilExito(Response<BaseResponse<T>> response);

        <T> void onSuccessGetAssociatedData(Response<BaseResponse<T>> response);
        <T> void onErrorGetAssociatedData(Response<BaseResponse<T>> response);

        <T> void onExpiredToken(Response<BaseResponse<T>> response);
    }
}
