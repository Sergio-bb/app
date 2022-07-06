package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityFrequentQuestions;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponsePreguntasFrecuente;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 20/09/2021.
 */
public interface ActivityFrequentQuestionsContract {

    interface View{
        void fetchFrequentQuestions();
        void showFrequentQuestions(List<ResponsePreguntasFrecuente> frequentQuestions);
        void showProgressDialog(String message);
        void hideProgressDialog();
        void showDataFetchError(String message);
        void showErrorTimeOut();
        void showExpiredToken(String message);
    }

    interface Presenter{
        void fetchFrequentQuestions();
    }

    interface Model{
        void getFrequentQuestions(final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t, boolean isErrorTimeOut);
    }

}
