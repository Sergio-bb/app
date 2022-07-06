package solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.TabHost;


import java.text.ParseException;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.nequi.dto.SuscriptionData;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiGeneral;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseSuscriptionStatus;
import solidappservice.cm.com.presenteapp.front.base.main.ActivityMainContract;
import solidappservice.cm.com.presenteapp.front.menuprincipal.FragmentHome.FragmentHomeContract;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 19/09/2021.
 */
public interface ActivityTabsContract {

    interface View{
        void configureSideBar();

        void validateSuscriptionNequi();
        void saveSuscriptionData(SuscriptionData datosSuscripcion, String statusSuscription);
        void getTimeExpiration();
        void resultTimeExpiration(Integer timeExpiration);
        void calculeMinutes(Integer days, Integer hour, Integer minute, Integer second);
        void getTimeOfSuscription();
        void activateButtonWarning(boolean activate);

        int validateTabSelected(int tab);
        android.view.View createTabView(TabHost context, final String text, ViewGroup viewGroup);
        Drawable createDrawable(String textTab);
        void showErrorTimeOut();
        void showDataFetchError(String title, String message);
        void showExpiredToken(String message);
    }

    interface Presenter{
        void validateSuscriptionNequi(BaseRequest body);
        void getTimeExpiration();
        void getTimeOfsuscription(BaseRequestNequi request);
    }

    interface Model{
        void getSuscriptionNequi(BaseRequest baseRequest, final APIListener listener);
        void getTimeExpiration(final APIListener listener);
        void getMinutesOfSuscription(BaseRequestNequi body,  final APIListener listener);
    }

    interface APIListener{
        <T> void onSuccessGetSuscriptionNequi(Response<BaseResponseNequi<T>> response);
        <T> void onErrorGetSuscriptionNequi(Response<BaseResponseNequi<T>> response);
        void onFailureGetSuscriptionNequi(Throwable t, boolean isErrorTimeOut);

        <T> void onSuccessGetTimeExpiration(Response<ResponseNequiGeneral> response);
        <T> void onErrorGetTimeExpiration(Response<ResponseNequiGeneral> response);

        <T>void onSuccessGetTimeOfSuscription(Response<BaseResponseNequi<ResponseSuscriptionStatus>> response) throws ParseException;
        <T>void onFailureGetTimeOfSuscription(Response<BaseResponseNequi<T>> response);

        <T> void onExpiredTokenNequi(Response<BaseResponseNequi<T>> response);
    }

}
