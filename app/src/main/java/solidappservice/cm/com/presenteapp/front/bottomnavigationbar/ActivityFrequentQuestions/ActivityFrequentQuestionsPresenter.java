package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityFrequentQuestions;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponsePreguntasFrecuente;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 20/09/2021.
 */
public class ActivityFrequentQuestionsPresenter implements ActivityFrequentQuestionsContract.Presenter,
        ActivityFrequentQuestionsContract.APIListener{

    ActivityFrequentQuestionsView view;
    ActivityFrequentQuestionsModel model;

    public ActivityFrequentQuestionsPresenter(@NonNull ActivityFrequentQuestionsView view, @NonNull ActivityFrequentQuestionsModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void fetchFrequentQuestions() {
        view.hideSectionFrequentQuestions();
        view.showCircularProgressBar("Consultando preguntas frecuentes...");
        model.getFrequentQuestions(this);
    }

    @Override
    public <T> void onSuccess(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        try {
            List<ResponsePreguntasFrecuente> frequentsQuestions = (List<ResponsePreguntasFrecuente>) response.body().getResultado();
            view.showSectionFrequentQuestions();
            view.showFrequentQuestions(frequentsQuestions);
        }catch(Exception ex){
            view.showDataFetchError("Lo sentimos", "");
            view.showErrorWithRefresh();
        }
    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        view.showExpiredToken(response.body().getErrorToken());
    }

    @Override
    public <T> void onError(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        if(response != null){
            view.showDataFetchError("Lo sentimos", response.body().getMensajeErrorUsuario());
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
        view.showErrorWithRefresh();
    }

    @Override
    public void onFailure(Throwable t, boolean isErrorTimeOut) {
        view.hideCircularProgressBar();
        if(isErrorTimeOut){
            view.showErrorTimeOut();
        }else{
            view.showDataFetchError("Lo sentimos", "");
        }
        view.showErrorWithRefresh();
    }

}
