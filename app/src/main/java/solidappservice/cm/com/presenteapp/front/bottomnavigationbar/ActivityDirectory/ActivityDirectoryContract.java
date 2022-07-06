package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityDirectory;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponseDirectorio;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 24/08/2021.
 */
public interface ActivityDirectoryContract {

    interface View{
        void fetchDirectory();
        void showDirectory(List<ResponseDirectorio> directorio);
        void callDirectoryContact(ResponseDirectorio directorio);
        void showSectionDirectory();
        void hideSectionDirectory();
        void showCircularProgressBar(String message);
        void hideCircularProgressBar();
        void showErrorWithRefresh();
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchDirectory();
    }

    interface Model{
        void getDirectory(final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
