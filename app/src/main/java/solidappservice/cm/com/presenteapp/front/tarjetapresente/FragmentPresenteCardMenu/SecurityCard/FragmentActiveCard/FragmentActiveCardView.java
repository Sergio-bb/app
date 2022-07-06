package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.SecurityCard.FragmentActiveCard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.request.RequestActivarTarjeta;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseTarjeta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 01/12/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public class FragmentActiveCardView extends Fragment implements FragmentActiveCardContract.View{

    private FragmentActiveCardPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
//    private ProgressDialog pd;
    private Dialog pd;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.spinnerProducto)
    Spinner spinnerProducto;
    @BindView(R.id.btnActivarTarjeta)
    Button btnActivarTarjeta;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de activar tarjeta");
        firebaseAnalytics.logEvent("pantalla_activar_tarjeta", params);
        View view = inflater.inflate(R.layout.fragment_presentecard_security_activate, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentActiveCardPresenter(this, new FragmentActiveCardModel());
        context = (ActivityBase) getActivity();
        state = context.getState();
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if (state == null || state.getUsuario() == null) {
            context.salir();
            return;
        }
        List<ResponseTarjeta> tarjetas = state.getTarjetas();
        showPresenteCards(tarjetas);
    }

    @OnClick(R.id.btnActivarTarjeta)
    public void onClickActivateCard(){
        if(spinnerProducto != null && spinnerProducto.getSelectedItem() != null) {
            confirmActivateCard((ResponseTarjeta) spinnerProducto.getSelectedItem());
        }else{
            showDialogError("Datos incompletos","Selecciona la tarjeta que desea activar");
        }
    }

    @Override
    public void showPresenteCards(List<ResponseTarjeta> tarjetas) {
        try {
            if (tarjetas == null) return;
            ArrayList<ResponseTarjeta> seleccionadas = new ArrayList<>();
            ArrayList<String> numeros = new ArrayList<>();
            for (ResponseTarjeta t: tarjetas){
                if(t.getI_estado().equalsIgnoreCase("I") && !numeros.contains(t.getK_mnumpl())){
                    seleccionadas.add(t);
                    numeros.add(t.getK_mnumpl());
                }
            }
            ArrayAdapter<ResponseTarjeta> adapter = new ArrayAdapter<>(context, R.layout.list_item_spinner, seleccionadas);
            spinnerProducto.setAdapter(adapter);
        } catch (Exception e) {
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void confirmActivateCard(ResponseTarjeta tarjeta){
        try {
            if (tarjeta != null) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(R.layout.pop_up_confirm);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView titleMessage =  dialog.findViewById(R.id.lbl_title_message);
                titleMessage.setText("¿Confirma tu solicitud?");
                TextView contentMessage = (TextView) dialog.findViewById(R.id.lbl_content_message);
                contentMessage.setText("¿Deseas activar tu Tarjeta?");
                ImageButton buttonClose =  dialog.findViewById(R.id.buttonClose);
                buttonClose.setOnClickListener(view -> dialog.dismiss());
                Button buttonAceptar =  dialog.findViewById(R.id.btnAceptar);
                buttonAceptar.setOnClickListener(view -> {
                    dialog.dismiss();
                    activateCard(tarjeta);
                });
                dialog.show();
            } else {
                showDataFetchError("Lo sentimos", "");
            }
        } catch (Exception e) {
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void activateCard(ResponseTarjeta tarjeta) {
        try {
            Encripcion encripcion = Encripcion.getInstance();
            String idDispositivo = context.obtenerIdDispositivo();
            presenter.activateCard(new RequestActivarTarjeta(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    tarjeta.getK_mnumpl(),
                    "",
                    "A",
                    idDispositivo
            ));
        } catch (Exception e) {
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void showResultActivateCard(String result) {
        try {
            ResponseTarjeta tarjeta = (ResponseTarjeta) spinnerProducto.getSelectedItem();
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.pop_up_success);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView titleMessage = (TextView) dialog.findViewById(R.id.titleSuccess);
            titleMessage.setText("Solicitud Enviada");
            TextView contentMessage = (TextView) dialog.findViewById(R.id.contentSuccess);
            contentMessage.setText(result);
            ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.buttonClose);
            buttonClose.setOnClickListener(view -> {
                updateStatePresenteCard(tarjeta, false);
                context.getState().getmTabHost().setCurrentTab(ActivityTabsView.TAB_14_PRESENTE_CARD_SECURITY_MENU_TAG);
                dialog.dismiss();
            });
            dialog.show();
        } catch (Exception e) {
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void updateStatePresenteCard(ResponseTarjeta card, boolean isBlock){
        if(state != null && state.getTarjetas() != null && state.getTarjetas().size() > 0){
            for(ResponseTarjeta t : state.getTarjetas()){
                if(t.getK_mnumpl().equals(card.getK_mnumpl())){
                    t.setI_estado((isBlock?"B":"A"));;
                }
            }
        }
    }

    @Override
    public void showProgressDialog(String message) {
        pd = new Dialog(context);
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.setCanceledOnTouchOutside(false);
        pd.setContentView(R.layout.pop_up_loading);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView contentMessage = (TextView) pd.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        pd.show();
    }

    @Override
    public void hideProgressDialog() {
        pd.dismiss();
    }

    @Override
    public void showDialogError(String title, String message){
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
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_error);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView titleMessage =  dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText(title);
        TextView contentMessage =  dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        ImageButton buttonClose = dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
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
        final Dialog dialog = new Dialog(context);
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
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_2_PRESENTE_CARD_MENU_TAG);
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
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_error);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView titleMessage = (TextView) dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText(title);
        TextView contentMessage = (TextView) dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(view -> {
            state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_2_PRESENTE_CARD_MENU_TAG);
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void showExpiredToken(String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_closedsession);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button buttonClosedSession = (Button) dialog.findViewById(R.id.btnVolverAIngresar);
        buttonClosedSession.setOnClickListener(view -> {
            dialog.dismiss();
            context.salir();
        });
        dialog.show();
    }
}
