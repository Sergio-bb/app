package solidappservice.cm.com.presenteapp.front.base.main;

import java.text.ParseException;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.nequi.dto.SuscriptionData;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseSuscriptionStatus;
import solidappservice.cm.com.presenteapp.front.menuprincipal.FragmentHome.FragmentHomeContract;

public interface ActivityMainContract {

    interface View{
        void configureSideBar();
        void activateButtonWarning(boolean activate);
        void saveSuscriptionData(SuscriptionData datosSuscripcion, String statusSuscription);
        void showDialogValidateCode();
        void showScreenHome();
        void showScreenForgotPassword();
        void showScreenFinanceMenu();
        void showScreenStatusAccount();
        void showScreenTransactionsMenu();
        void showScreenPresenteCard();
        void showScreenInbox();
        void showDataFetchError(String title, String message);
    }

    interface Presenter{
    }

    interface Model{
    }

    interface APIListener{
    }

}

