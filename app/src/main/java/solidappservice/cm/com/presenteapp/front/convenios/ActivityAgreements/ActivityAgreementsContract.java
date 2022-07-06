package solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseConvenios;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Resumen;
import solidappservice.cm.com.presenteapp.entities.convenios.response.ResponseResumen;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public interface ActivityAgreementsContract {

    interface View{
        void setFragment(IFragmentCoordinator.Pantalla pantalla);
        void fetchAgreements();
        void showResultAgreements(Resumen resumen);
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showDataFetchError(String message);
        void showErrorTimeOut();
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchAgreements(BaseRequest baseRequest);
    }

    interface Model{
        void getAgreements(BaseRequest baseRequest, final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponseConvenios<ResponseResumen>> response);
        <T> void onExpiredToken(Response<BaseResponseConvenios<ResponseResumen>> response);
        <T> void onError(Response<BaseResponseConvenios<ResponseResumen>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
