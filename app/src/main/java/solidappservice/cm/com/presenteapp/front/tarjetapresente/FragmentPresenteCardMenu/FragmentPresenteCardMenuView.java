package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu;

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
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

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
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 25/11/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 22/07/2021.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public class FragmentPresenteCardMenuView extends Fragment implements FragmentPresenteCardMenuContract.View {

    private FragmentPresenteCardMenuPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private ProgressDialog pd;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.scrollview_menu_presentecard)
    ScrollView scrollViewMenuPresenteCard;
    @BindView(R.id.layoutActivar)
    LinearLayout layoutActivar;
    @BindView(R.id.layoutMovimientos)
    LinearLayout layoutMovimientos;
    @BindView(R.id.layoutReposicion)
    LinearLayout layoutReposicion;
    @BindView(R.id.txtCantProducts)
    TextView txtCantProducts;

    @BindView(R.id.layout_circular_progress_bar)
    LinearLayout layoutCircularProgressBar;
    @BindView(R.id.circular_progress_bar)
    ProgressBar circularProgressBar;
    @BindView(R.id.text_circular_progress_Bar)
    TextView textCircularProgressBar;

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
        params.putString("Descripción", "Interacción con pantalla de tarjeta pte");
        firebaseAnalytics.logEvent("pantalla_tarjeta_pte", params);
        View view = inflater.inflate(R.layout.fragment_presentecard_menu, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentPresenteCardMenuPresenter(this, new FragmentPresenteCardMenuModel());
        context = (ActivityBase) getActivity();
        state = context.getState();
        pd = new ProgressDialog(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null || state.getUsuario() == null){
            context.salir();
        }else {
//            Usuario usuario = state.getUsuario();
//            new TarjetaTask().execute(usuario.cedula, usuario.token);
            fetchButtonStateReplacementCard();
            fetchPresenteCards();
        }
    }

    @OnClick(R.id.layoutActivar)
    public void onClickActivateCard(){
        layoutActivar.setBackgroundColor(Color.parseColor("#F2F2F2"));
        context.getState().getmTabHost().setCurrentTab(ActivityTabsView.TAB_14_PRESENTE_CARD_SECURITY_MENU_TAG);
    }

    @OnClick(R.id.layoutMovimientos)
    public void onClickMovementsCard(){
        layoutMovimientos.setBackgroundColor(Color.parseColor("#F2F2F2"));
        context.getState().getmTabHost().setCurrentTab(ActivityTabsView.TAB_13_PRESENTE_CARD_PRODUCTS_TAG);
    }

    @OnClick(R.id.layoutReposicion)
    public void onClickReplacementCard(){
        layoutReposicion.setBackgroundColor(Color.parseColor("#F2F2F2"));
        context.getState().getmTabHost().setCurrentTab(ActivityTabsView.TAB_21_PRESENTE_CARD_REPLACEMENT_TAG);
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
    public void showNumberPresenteCards(List<ResponseTarjeta> tarjetas){
        state.setTarjetas(tarjetas);
        scrollViewMenuPresenteCard.setVisibility(View.VISIBLE);
        if(txtCantProducts != null){
            txtCantProducts.setText("Producto(s) " + tarjetas.size());
        }
    }

    @Override
    public void fetchButtonStateReplacementCard() {
        try {
            presenter.fetchButtonStateReplacementCard();
        } catch (Exception ex) {
            showDataFetchError("");
        }
    }

    @Override
    public void showButtonReplacementCard() {
        layoutReposicion.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideButtonReplacementCard() {
        layoutReposicion.setVisibility(View.GONE);
    }

    @Override
    public void hideNumberPresenteCards(){
        scrollViewMenuPresenteCard.setVisibility(View.GONE);
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
    public void showCircularProgressBar(String textProgressBar) {
        layoutCircularProgressBar.setVisibility(View.VISIBLE);
        textCircularProgressBar.setText(textProgressBar);
    }

    @Override
    public void hideCircularProgressBar() {
        layoutCircularProgressBar.setVisibility(View.GONE);
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
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
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
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
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
//            mostrarTarjetasCant(tarjetas.size());
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
//
//    private void mostrarTarjetasCant(int cant){
//        if(txtCantProducts != null){
//            txtCantProducts.setText("Producto(s) " + cant);
//        }
//    }
}