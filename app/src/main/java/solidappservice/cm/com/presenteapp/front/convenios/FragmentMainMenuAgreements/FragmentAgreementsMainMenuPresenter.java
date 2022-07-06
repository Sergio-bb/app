package solidappservice.cm.com.presenteapp.front.convenios.FragmentMainMenuAgreements;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Categoria;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Convenio;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Resumen;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public class FragmentAgreementsMainMenuPresenter implements FragmentAgreementsMainMenuContract.Presenter,
        FragmentAgreementsMainMenuContract.APIListener{

    FragmentAgreementsMainMenuView view;

    public FragmentAgreementsMainMenuPresenter(@NonNull FragmentAgreementsMainMenuView view) {
        this.view = view;
    }

    @Override
    public void fetchAgreementCategoriesByUserLocation(Resumen resumen){
        try{
            if (resumen != null) {
                List<Categoria> allCategories = resumen.getCategorias();
                String locationAsociated = resumen.getUbicacionAsociado();
                List<Categoria> categoriesByLocation = new ArrayList<>();

                for (Categoria cat: allCategories) {
                    if(cat.getCiudades() != null &&
                            cat.getCiudades().size() > 0
                            && cat.getCiudades().contains(locationAsociated)){
                        categoriesByLocation.add(cat);
                    }
                }
                view.showCategories(categoriesByLocation, resumen);
            }else{
                view.showDataFetchError("Lo sentimos", "Se ha producido un error, inténtalo nuevamente en unos minutos.");
            }
        } catch(Exception ex){
            view.showDataFetchError("Lo sentimos", "Se ha producido un error, inténtalo nuevamente en unos minutos.");
        }
    }

    @Override
    public void fetchFeaturedAgreements(List<Convenio> convenios){
        try{
            boolean genericUsed = false;
            if(convenios != null && convenios.size() == 0){
                Convenio generic = new Convenio();
                generic.setBeneficio("GENERIC");
                generic.setDescripcionCorta("Conoce todos los convenios de PRESENTE para ti");
                generic.setDestacado(true);
                generic.setId("0");
                generic.setNombre("GENERIC");
                generic.setOrden(0);
                generic.setImagen(null);
                convenios.add(generic);
                genericUsed = true;
            }
            view.showFeaturedAgreements(convenios, genericUsed);
        } catch(Exception ex){
            view.showDataFetchError("Lo sentimos", "Se ha producido un error, inténtalo nuevamente en unos minutos.");
        }
    }


}
