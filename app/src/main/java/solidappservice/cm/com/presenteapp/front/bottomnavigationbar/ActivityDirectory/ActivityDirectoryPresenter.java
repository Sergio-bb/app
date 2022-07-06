package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityDirectory;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponseDirectorio;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 24/08/2021.
 */
public class ActivityDirectoryPresenter implements ActivityDirectoryContract.Presenter,
        ActivityDirectoryContract.APIListener{

    ActivityDirectoryView view;
    ActivityDirectoryModel model;

    public ActivityDirectoryPresenter(@NonNull ActivityDirectoryView view, @NonNull ActivityDirectoryModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchDirectory() {
        view.showProgressDialog("Consultando directorio...");
        model.getDirectory(this);
    }

    @Override
    public <T> void onSuccess(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try {
            List<ResponseDirectorio> directorios = (List<ResponseDirectorio>) response.body().getResultado();
            view.showDirectory(directorios);
        }catch(Exception ex){
            Log.d("Error",ex.getMessage());
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
