package solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.agreements.FragmentMainMenuAgreements;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Categoria;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Convenio;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Resumen;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public interface FragmentAgreementsMainMenuContract {

    interface View{
        void showCategories(List<Categoria> categorias, Resumen resumen);
        void showAgreementByCategories(int positionCategory);
        void showFeaturedAgreements(List<Convenio> featuredAgreements, boolean genericUsed);
        void showProductsByAgreements(int positionAgreement);
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showDataFetchError(String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchAgreementCategoriesByUserLocation(Resumen resumen);
        void fetchFeaturedAgreements(List<Convenio> convenios);
    }

    interface Model{
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t);
    }

}
