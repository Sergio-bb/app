package solidappservice.cm.com.presenteapp.front.convenios.FragmentListAgreements;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Convenio;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Resumen;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Ubicacion;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public class FragmentListAgreementsPresenter implements FragmentListAgreementsContract.Presenter,
        FragmentListAgreementsContract.APIListener{

    FragmentListAgreementsView view;

    public FragmentListAgreementsPresenter(@NonNull FragmentListAgreementsView view) {
        this.view = view;
    }

    @Override
    public void fetchAgreementByCategoryByUserLocation(Resumen resumen){
        try{
            if (resumen != null) {

                String locationAsociated = resumen.getUbicacionAsociado();
                List<Convenio> agreementsByCategory = resumen.getConveniosPorCategoria(resumen.getCategoriaSeleccionada().getId());
                List<Convenio> agreementsByCategoryByLocationAsoicated = new ArrayList<>();

                Collections.sort(agreementsByCategory, new Comparator<Convenio>() {
                    public int compare(Convenio s, Convenio o) {
                        return o.getOrden() - s.getOrden();
                    }
                });

                for (Convenio c: agreementsByCategory){
                    List<Ubicacion> ubicaciones = c.getUbicaciones();
                    for(Ubicacion u: ubicaciones){
                        if(u.getCiudad().toUpperCase().contains(locationAsociated.toUpperCase())){
                            agreementsByCategoryByLocationAsoicated.add(c);
                            break;
                        }
                    }
                }
                view.showAgreements(agreementsByCategoryByLocationAsoicated);
            }else{
                view.showDataFetchError("Lo sentimos", "Se ha producido un error, inténtalo nuevamente en unos minutos.");
            }
        } catch(Exception ex){
            view.showDataFetchError("Lo sentimos", "Se ha producido un error, inténtalo nuevamente en unos minutos.");
        }
    }

    @Override
    public <T> void onSuccess(Response<BaseResponse<T>> response) {
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
    }

    @Override
    public <T> void onError(Response<BaseResponse<T>> response) {
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
    }

}
