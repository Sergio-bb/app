package solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.agreements.ActivityHtmlViewer;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseConvenios;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public interface ActivityHtmlViewerContract {

    interface View{
        void showHtml(String html);
        void showDataFetchError(String message);
    }

    interface Presenter{
    }

    interface Model{
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponseConvenios<T>> response);
        <T> void onExpiredToken(Response<BaseResponseConvenios<T>> response);
        <T> void onError(Response<BaseResponseConvenios<T>> response);
        void onFailure(Throwable t);
    }

}
