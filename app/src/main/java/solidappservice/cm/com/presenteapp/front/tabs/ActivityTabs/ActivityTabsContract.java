package solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.TabHost;


import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 19/09/2021.
 */
public interface ActivityTabsContract {

    interface View{
        int validateTabSelected(int tab);
        android.view.View createTabView(TabHost context, final String text, ViewGroup viewGroup);
        Drawable createDrawable(String textTab);
    }

    interface Presenter{
    }

    interface Model{

    }

    interface APIListener{
        <T> void onSuccess(Response<BaseResponse<T>> response);
        <T> void onExpiredToken(Response<BaseResponse<T>> response);
        <T> void onError(Response<BaseResponse<T>> response);
        void onFailure(Throwable t);
    }

}
