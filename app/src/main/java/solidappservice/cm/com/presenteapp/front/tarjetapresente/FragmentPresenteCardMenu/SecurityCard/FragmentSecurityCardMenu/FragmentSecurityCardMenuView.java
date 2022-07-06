package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.SecurityCard.FragmentSecurityCardMenu;

import static solidappservice.cm.com.presenteapp.tools.constants.Constans.ERROR_CONTACTA_PRESENTE;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 07/12/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public class FragmentSecurityCardMenuView extends Fragment implements FragmentSecurityCardMenuContract.View {

    private FragmentSecurityCardMenuPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private FirebaseAnalytics firebaseAnalytics;
    private List<ResponseTarjeta> tarjetas;

    @BindView(R.id.layoutActivar)
    LinearLayout layoutActivar;
    @BindView(R.id.layoutBloquear)
    LinearLayout layoutBloquear;

    @BindView(R.id.layout_circular_progress_bar)
    LinearLayout layoutCircularProgressBar;
    @BindView(R.id.circular_progress_bar)
    ProgressBar circularProgressBar;
    @BindView(R.id.text_circular_progress_Bar)
    TextView textCircularProgressBar;
    @BindView(R.id.imageReferesh)
    ImageView buttonReferesh;

    @BindView(R.id.contentMenuTarjetas)
    LinearLayout contentMenuTarjetas;

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
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if (state == null || state.getUsuario() == null) {
            context.salir();
        } else {
            fetchPresenteCards();
        }
    }

    @OnClick(R.id.imageReferesh)
    public void onClickRefresh(){
        state.setTarjetas(null);
        fetchPresenteCards();
    }

    @OnClick(R.id.layoutActivar)
    public void onClickActivateCard(){
        layoutActivar.setBackgroundColor(Color.parseColor("#F2F2F2"));
        if (validatePresenteCardsStatus(false)) {
            context.getState().getmTabHost().setCurrentTab(ActivityTabsView.TAB_10_PRESENTE_CARD_ACTIVE_TAG);
        } else {
            layoutActivar.setBackgroundColor(Color.parseColor("#FFFFFF"));
            showDataFetchError("No hay tarjetas inactivas", "Actualmente no tienes tarjetas inactivas");
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
            if(state != null && state.getTarjetas() != null && state.getTarjetas().size() > 0){
                hideCircularProgressBar();
                showSectionSecurityCardMenu();
                showPresenteCards(state.getTarjetas());
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
    public void showSectionSecurityCardMenu(){
        contentMenuTarjetas.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideSectionSecurityCardMenu(){
        contentMenuTarjetas.setVisibility(View.GONE);
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
        contentMenuTarjetas.setVisibility(View.GONE);
        layoutCircularProgressBar.setVisibility(View.VISIBLE);
        circularProgressBar.setVisibility(View.GONE);
        textCircularProgressBar.setText("Ha ocurrido un error, inténtalo de nuevo ");
        buttonReferesh.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorTimeOut() {
        String message =  validateMessageError("", 7);
        final Dialog dialog = getDialog(R.layout.pop_up_error);
        TextView titleMessage =  dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText("Lo sentimos");
        TextView contentMessage =  dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        ImageButton buttonClose =  dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_2_PRESENTE_CARD_MENU_TAG);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void showDataFetchError(String title, String message){
        String errorMessage = validateMessageError(message, 7);
        final Dialog dialog = getDialog(R.layout.pop_up_error);
        TextView titleMessage =  dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText(title);
        TextView contentMessage =  dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(errorMessage);
        ImageButton buttonClose =  dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(view -> {
            state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_2_PRESENTE_CARD_MENU_TAG);
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void showExpiredToken(String message) {
        final Dialog dialog = getDialog(R.layout.pop_up_closedsession);
        Button buttonClosedSession = (Button) dialog.findViewById(R.id.btnVolverAIngresar);
        buttonClosedSession.setOnClickListener(view -> {
            dialog.dismiss();
            context.salir();
        });
        dialog.show();
    }
    private Dialog getDialog(Integer idPopUp){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(idPopUp);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        return dialog;
    }
    private String  validateMessageError(String message, int idMessage){
        String newMessage = message;
        if(TextUtils.isEmpty(message)){
            newMessage  = ERROR_CONTACTA_PRESENTE;
            if(state.getMensajesRespuesta() != null && state.getMensajesRespuesta().size() > 0){
                for(ResponseMensajesRespuesta rm : state.getMensajesRespuesta()){
                    if(rm.getIdMensaje() == idMessage){
                        newMessage = rm.getMensaje();
                    }
                }
            }
        }
        return newMessage;
    }
}