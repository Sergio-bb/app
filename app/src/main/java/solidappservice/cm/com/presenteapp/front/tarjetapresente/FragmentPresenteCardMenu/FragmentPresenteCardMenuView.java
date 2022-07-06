package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private ActivityTabsView tabsContext;
    private GlobalState state;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.contentMenuPresenteCard)
    ScrollView contentMenuPresenteCard;
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
    @BindView(R.id.imageReferesh)
    ImageView buttonReferesh;

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
        tabsContext = (ActivityTabsView) getActivity();
        state = context.getState();
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null || state.getUsuario() == null){
            context.salir();
        }else {
            fetchButtonStateReplacementCard();
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

    @OnClick(R.id.imageReferesh)
    public void onClickRefresh(){
        state.setTarjetas(null);
        fetchButtonStateReplacementCard();
    }

    @Override
    public void fetchButtonStateReplacementCard() {
        try {
            presenter.fetchButtonStateReplacementCard();
        } catch (Exception ex) {
            hideButtonReplacementCard();
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
    public void fetchPresenteCards(){
        try{
            if(state != null && state.getTarjetas()!= null){
                hideCircularProgressBar();
                showSectionMenuPresenteCards();
                showNumberPresenteCards(state.getTarjetas());
            }else{
                Encripcion encripcion = Encripcion.getInstance();
                presenter.fetchPresenteCards(new BaseRequest(
                        encripcion.encriptar(state.getUsuario().getCedula()),
                        state.getUsuario().getToken()
                ));
            }
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
            showErrorWithRefresh();
        }
    }

    @Override
    public void showNumberPresenteCards(List<ResponseTarjeta> tarjetas){
        state.setTarjetas(tarjetas);
        contentMenuPresenteCard.setVisibility(View.VISIBLE);
        if(txtCantProducts != null){
            txtCantProducts.setText("Producto(s) " + tarjetas.size());
        }
    }

    @Override
    public void showSectionMenuPresenteCards() {
        contentMenuPresenteCard.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideSectionMenuPresenteCards(){
        contentMenuPresenteCard.setVisibility(View.GONE);
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
    public void showErrorWithRefresh(){
        contentMenuPresenteCard.setVisibility(View.GONE);
        layoutCircularProgressBar.setVisibility(View.VISIBLE);
        circularProgressBar.setVisibility(View.GONE);
        textCircularProgressBar.setText("Ha ocurrido un error, inténtalo de nuevo ");
        buttonReferesh.setVisibility(View.VISIBLE);
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
        final Dialog dialog = new Dialog(tabsContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_error);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView titleMessage = (TextView) dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText("Lo sentimos");
        TextView contentMessage = (TextView) dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void showDataFetchError(String title, String message){
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
        final Dialog dialog = new Dialog(tabsContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_error);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView titleMessage = (TextView) dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText(title);
        TextView contentMessage = (TextView) dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void showExpiredToken(String message) {
        final Dialog dialog = new Dialog(tabsContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_closedsession);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button buttonClosedSession = (Button) dialog.findViewById(R.id.btnVolverAIngresar);
        buttonClosedSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                context.salir();
            }
        });
        dialog.show();
    }
}