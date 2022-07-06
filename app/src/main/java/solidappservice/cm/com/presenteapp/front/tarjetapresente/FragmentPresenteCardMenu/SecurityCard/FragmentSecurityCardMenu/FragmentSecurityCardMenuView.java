package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.SecurityCard.FragmentSecurityCardMenu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseTarjeta;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 07/12/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public class FragmentSecurityCardMenuView extends Fragment implements FragmentSecurityCardMenuContract.View {

    private FragmentSecurityCardMenuPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private ProgressDialog pd;
    private FirebaseAnalytics firebaseAnalytics;
    private List<ResponseTarjeta> tarjetas;

    @BindView(R.id.layoutActivar)
    LinearLayout layoutActivar;
    @BindView(R.id.layoutBloquear)
    LinearLayout layoutBloquear;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de tarjeta seguridad");
        firebaseAnalytics.logEvent("pantalla_tarjeta_seguridad", params);
        View view = inflater.inflate(R.layout.fragment_presentecard_security, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentSecurityCardMenuPresenter(this, new FragmentSecurityCardMenuModel());
        context = (ActivityBase) getActivity();
        state = context.getState();
        pd = new ProgressDialog(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if (state == null || state.getUsuario() == null) {
            context.salir();
        } else {
            tarjetas = state.getTarjetas();
            if(tarjetas == null || tarjetas.size() <= 0){
                fetchPresenteCards();
//                Usuario usuario = state.getUsuario();
//                new TarjetaTask().execute(usuario.cedula, usuario.token);
            }
        }
    }

    @OnClick(R.id.layoutActivar)
    public void onClickActivateCard(){
        layoutActivar.setBackgroundColor(Color.parseColor("#F2F2F2"));
        if (validatePresenteCardsStatus(false)) {
            context.getState().getmTabHost().setCurrentTab(ActivityTabsView.TAB_10_PRESENTE_CARD_ACTIVE_TAG);
        } else {
            layoutActivar.setBackgroundColor(Color.parseColor("#FFFFFF"));
            context.makeDialog("Actualmente no cuentas con tarjetas inactivas que puedan ser activadas");
        }
    }

    @OnClick(R.id.layoutBloquear)
    public void onClickBlockCard(){
        layoutBloquear.setBackgroundColor(Color.parseColor("#F2F2F2"));
        context.getState().getmTabHost().setCurrentTab(ActivityTabsView.TAB_11_PRESENTE_CARD_BLOCK_TAG);
    }

    @Override
    public void fetchPresenteCards(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchPresenteCards(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void showPresenteCards(List<ResponseTarjeta> tarjetas){
        state.setTarjetas(tarjetas);
        this.tarjetas = tarjetas;
    }

    @Override
    public boolean validatePresenteCardsStatus(final boolean isStatusBlock) {
        boolean sw = false;
        if (tarjetas != null && tarjetas.size() > 0) {
            int count = 0;
            for (ResponseTarjeta t : tarjetas) {
                if (isStatusBlock) {//VAMOS A BLOQUEAR TARJETAS
                    if (t.getI_estado().equalsIgnoreCase("A")) {
                        count++;
                    }
                } else {//VAMOS A ACTIVAR TARJETAS
                    if (t.getI_estado().equalsIgnoreCase("I")) {
                        count++;
                    }
                }
            }
            sw = count > 0;
        }
        return sw;
    }

    @Override
    public void showProgressDialog(String message) {
        pd.setTitle(context.getResources().getString(R.string.app_name));
        pd.setMessage(message);
        pd.setIcon(R.mipmap.icon_presente);
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    public void hideProgressDialog() {
        pd.dismiss();
    }

    @Override
    public void showErrorTimeOut() {
        String message = "Ha ocurrido un error. Intenta de nuevo y si el error persiste, contacta a PRESENTE.";
        if(state != null && state.getMensajesRespuesta() != null && state.getMensajesRespuesta().size()>0){
            for(ResponseMensajesRespuesta rm : state.getMensajesRespuesta()){
                if(rm.getIdMensaje() == 6){
                    message = rm.getMensaje();
                }
            }
        }
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle(context.getResources().getString(R.string.app_name));
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(message);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_2_PRESENTE_CARD_MENU_TAG);
                dialog.dismiss();
            }
        });
        d.show();
    }

    @Override
    public void showDataFetchError(String message) {
        if(TextUtils.isEmpty(message)){
            message = "Ha ocurrido un error. Intenta de nuevo y si el error persiste, contacta a PRESENTE.";
            if(state != null && state.getMensajesRespuesta() != null && state.getMensajesRespuesta().size()>0){
                for(ResponseMensajesRespuesta rm : state.getMensajesRespuesta()){
                    if(rm.getIdMensaje() == 7){
                        message = rm.getMensaje();
                    }
                }
            }
        }
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle(context.getResources().getString(R.string.app_name));
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(message);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_2_PRESENTE_CARD_MENU_TAG);
                dialog.dismiss();
            }
        });
        d.show();
    }

    @Override
    public void showExpiredToken(String message) {
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle("Sesión finalizada");
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(message);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                context.salir();
            }
        });
        d.show();
    }

//    private boolean validarBloqueadasActivas(final boolean bloquear) {
//        boolean sw = false;
//        if (tarjetas != null && tarjetas.size() > 0) {
//
//            int count = 0;
//            for (Tarjeta t : tarjetas) {
//                if (bloquear) {//VAMOS A BLOQUEAR TARJETAS
//                    if (t.i_estado.equalsIgnoreCase("A")) {
//                        count++;
//                    }
//                } else {//VAMOS A ACTIVAR TARJETAS
//                    if (t.i_estado.equalsIgnoreCase("I")) {
//                        count++;
//                    }
//                }
//            }
//
//            sw = count > 0;
//        }
//        return sw;
//    }
//
//
//
//    class TarjetaTask extends AsyncTask<String, String, String> {
//
//        String cedula = null;
//        String token = null;
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Obteniendo tarjetas...");
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                Encripcion encripcion = Encripcion.getInstance();
//                JSONObject param = new JSONObject();
//                param.put("cedula", cedula = encripcion.encriptar(params[0]));
//                param.put("token", token = params[1]);
//                return networkHelper.writeService(param, SincroHelper.TARJETAS);
//            } catch (Exception e) {
//                return e.getMessage();
//            }
//        }
//
//        @Override
//        protected void onProgressUpdate(String... values) {
//            pd.setMessage(values[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            pd.dismiss();
//            procesarJsonTarjetas(result);
//        }
//    }
//
//    private void procesarJsonTarjetas(String jsonRespuesta){
//        try{
//            ArrayList<Tarjeta> tarjetas = SincroHelper.procesarJsonTarjetas(jsonRespuesta);
//            this.tarjetas = tarjetas;
//            context.getState().setTarjetas(tarjetas);
//        }catch (ErrorTokenException e){
//            AlertDialog.Builder d = new AlertDialog.Builder(context);
//            d.setTitle("Sesión finalizada");
//            d.setIcon(R.mipmap.icon_presente);
//            d.setMessage(e.getMessage());
//            d.setCancelable(false);
//            d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    context.salir();
//                }
//            });
//            d.show();
//        }catch (Exception e){
//            context.makeErrorDialog(e.getMessage());
//        }
//    }


}