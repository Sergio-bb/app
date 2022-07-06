package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityServices;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponseServicios;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 28/08/2021
 */
public class ActivityServicesPresenter implements ActivityServicesContract.Presenter,
        ActivityServicesContract.APIListener{

    ActivityServicesView view;
    ActivityServicesModel model;

    public ActivityServicesPresenter(@NonNull ActivityServicesView view, @NonNull ActivityServicesModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchServices() {
        view.showProgressDialog("Consultando servicios...");
        model.getServices(this);
    }

    @Override
    public <T> void onSuccess(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try {
            List<ResponseServicios> servicios = (List<ResponseServicios>) response.body().getResultado();
            if (servicios != null && servicios.size() > 0) {

                for (Iterator<ResponseServicios> it = servicios.iterator(); it.hasNext();) {
                    ResponseServicios s = it.next();
                    if (!s.getI_estado().equals("S")) {
                        it.remove();
                    }
                }

                Collections.sort(servicios, new Comparator<ResponseServicios>() {
                    public int compare(ResponseServicios s, ResponseServicios o) {
                        return s.getQ_orden().compareTo(o.getQ_orden());
                    }
                });
                view.showServices(servicios);
            } else {
                view.showDataFetchError("Error al consultar los productos");
            }
        }catch (Exception ex){
            view.showDataFetchError("");
        }
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        view.showExpiredToken(response.body().getErrorToken());
    }

    @Override
    public <T> void onError(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        if(response != null){
            view.showDataFetchError(response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("");
        }
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideProgressDialog();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("");
        }
    }

}
