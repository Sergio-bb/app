package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityPortfolio.ActivityPortfolioProducts;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.dto.PortafolioPadre;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponsePortafolio;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 28/08/2021.
 */
public class ActivityPortfolioProductsPresenter implements ActivityPortfolioProductsContract.Presenter,
        ActivityPortfolioProductsContract.APIListener{

    ActivityPortfolioProductsView view;
    ActivityPortfolioProductsModel model;

    public ActivityPortfolioProductsPresenter(@NonNull ActivityPortfolioProductsView view, @NonNull ActivityPortfolioProductsModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchPortfolioProducts() {
        view.showProgressDialog("Consultando portafolio...");
        model.getPortfolioProducts(this);
    }

    @Override
    public <T> void onSuccess(Response<BaseResponse<T>> response) {
        view.hideProgressDialog();
        try {
            List<ResponsePortafolio> productosPortafolio = (List<ResponsePortafolio>) response.body().getResultado();
            List<PortafolioPadre> portafolioPadres = new ArrayList<>();
            List<String> categorias = new ArrayList<>();
            for (ResponsePortafolio p : productosPortafolio) {
                if (!categorias.contains(p.getCategoriaPadre())) {
                    categorias.add(p.getCategoriaPadre());
                }
            }
            for (String c : categorias) {
                PortafolioPadre portafolioPadre = new PortafolioPadre();
                portafolioPadre.setCategoriaPadre(c);

                ArrayList<ResponsePortafolio> portafolios = new ArrayList<>();
                for (ResponsePortafolio p : productosPortafolio) {
                    if (p.getCategoriaPadre().equals(c)) {
                        portafolios.add(p);
                    }
                }
                portafolioPadre.setPortafolios(portafolios);
                portafolioPadres.add(portafolioPadre);
            }
            view.showPortfolioProducts(productosPortafolio, portafolioPadres);
        }catch(Exception ex){
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
