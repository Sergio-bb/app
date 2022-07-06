package solidappservice.cm.com.presenteapp.front.tutorial.ActivityTutorial;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.imagelogin.ResponseImageLogin;
import solidappservice.cm.com.presenteapp.entities.mensajesbanner.ResponseMensajesBanner;
import solidappservice.cm.com.presenteapp.entities.tyc.response.ReponseTyC;
import solidappservice.cm.com.presenteapp.front.splash.ActivitySplash.ActivitySplashView;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 20/09/2021.
 */
public class ActivityTutorialPresenter implements ActivityTutorialContract.Presenter,
        ActivityTutorialContract.APIListener{

    ActivityTutorialView view;

    public ActivityTutorialPresenter(@NonNull ActivityTutorialView view) {
        this.view = view;
    }

    @Override
    public <T> void onSuccess(Response<BaseResponse<T>> response) {
    }

    @Override
    public <T> void onError(Response<BaseResponse<T>> response) {
    }

    @Override
    public void onFailure(Throwable t) {
    }

}
