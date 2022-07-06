package solidappservice.cm.com.presenteapp.front.convenios.FragmentListAgreements;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Convenio;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Resumen;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public interface FragmentListAgreementsContract {

    interface View{
        void showAgreements(List<Convenio> convenios);
        void showProductsByAgreements(int positionAgreement);
        void showDataFetchError(String title, String message);
    }

    interface Presenter{
        void fetchAgreementByCategoryByUserLocation(Resumen resumen);
    }

    interface Model{
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
