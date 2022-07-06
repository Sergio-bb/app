package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.ReplacementCard.FragmentReplacementCard;

import androidx.annotation.NonNull;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public class FragmentReplacementCardPresenter implements FragmentReplacementCardContract.Presenter,
        FragmentReplacementCardContract.APIListener{

    FragmentReplacementCardView view;

    public FragmentReplacementCardPresenter(@NonNull FragmentReplacementCardView view) {
        this.view = view;
    }

    @Override
    public void validateDataReplacementCard(){
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
    public void onFailure(Throwable t) {
    }

}
