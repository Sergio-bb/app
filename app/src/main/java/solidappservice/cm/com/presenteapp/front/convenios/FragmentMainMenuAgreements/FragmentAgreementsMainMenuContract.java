package solidappservice.cm.com.presenteapp.front.convenios.FragmentMainMenuAgreements;

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
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchAgreementCategoriesByUserLocation(Resumen resumen);
        void fetchFeaturedAgreements(List<Convenio> convenios);
    }

    interface Model{
    }

    interface APIListener{
    }

}
