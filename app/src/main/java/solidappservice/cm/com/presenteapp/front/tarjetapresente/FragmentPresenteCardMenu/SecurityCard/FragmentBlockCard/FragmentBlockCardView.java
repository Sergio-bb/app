package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.SecurityCard.FragmentBlockCard;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.request.RequestBloquearTarjeta;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseTarjeta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 01/12/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public class FragmentBlockCardView extends Fragment implements FragmentBlockCardContract.View {

    private FragmentBlockCardPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
//    private ProgressDialog pd;
    private Dialog pd;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.spinnerProducto)
    Spinner spinnerProducto;
    @BindView(R.id.spinnerMotivoBloqueo)
    Spinner spinnerMotivoBloqueo;
    @BindView(R.id.btnBloquearTarjeta)
    Button btnBloquearTarjeta;

    @BindView(R.id.layout_circular_progress_bar)
    LinearLayout layoutCircularProgressBar;
    @BindView(R.id.circular_progress_bar)
    ProgressBar circularProgressBar;
    @BindView(R.id.text_circular_progress_Bar)
    TextView textCircularProgressBar;
    @BindView(R.id.imageReferesh)
    ImageView buttonReferesh;

    @BindView(R.id.contentBlockCard)
    ScrollView contentBlockCard;

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
        params.putString("Descripción", "Interacción con pantalla de bloquear tarjeta");
        firebaseAnalytics.logEvent("pantalla_bloquear_tarjeta", params);
        View view = inflater.inflate(R.layout.fragment_presentecard_security_block, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentBlockCardPresenter(this, new FragmentBlockCardModel());
        context = (ActivityBase) getActivity();
        state = context.getState();
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null || state.getUsuario() == null){
            context.salir();
            return;
        }
        List<ResponseTarjeta> tarjetas = state.getTarjetas();
        showPresenteCards(tarjetas);
        fetchReasonsBlockCard();
    }

    @OnClick(R.id.btnBloquearTarjeta)
    public void onClickBlockCard(){
        if(spinnerProducto != null && spinnerProducto.getSelectedItem() != null) {
            confirmBlockCard((ResponseTarjeta) spinnerProducto.getSelectedItem());
        }else{
            showDataFetchError("Datos incompletos", "Selecciona la tarjeta que desea bloquear.");
        }
    }

    @OnClick(R.id.imageReferesh)
    public void onClickRefresh(){
        state.setMotivosBloqueoTarjeta(null);
        fetchReasonsBlockCard();
    }

    @Override
    public void fetchReasonsBlockCard(){
        try{
            if (state != null && state.getMotivosBloqueoTarjeta() != null && state.getMotivosBloqueoTarjeta().size() > 0) {
                hideCircularProgressBar();
                showSectionBlockCard();
                showReasonsBlockCard(state.getMotivosBloqueoTarjeta());
            } else {
                Encripcion encripcion = Encripcion.getInstance();
                presenter.fetchReasonsBlockCard(new BaseRequest(
                        encripcion.encriptar(state.getUsuario().getCedula()),
                        state.getUsuario().getToken()
                ));
            }
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "Se ha producido un error, inténtalo nuevamente en unos minutos.");
            showErrorWithRefresh();
        }
    }

    @Override
    public void showPresenteCards(List<ResponseTarjeta> tarjetas) {
        try {
            if(tarjetas != null && tarjetas.size() > 0){
                ArrayList<ResponseTarjeta> seleccionadas = new ArrayList<>();
                ArrayList<String> numeros = new ArrayList<>();
                for (ResponseTarjeta t: tarjetas){
                    if(!numeros.contains(t.getK_mnumpl()) && t.getI_estado().equalsIgnoreCase("A")){
                        seleccionadas.add(t);
                        numeros.add(t.getK_mnumpl());
                    }
                }
                ArrayAdapter<ResponseTarjeta> adapter = new ArrayAdapter<>(context, R.layout.list_item_spinner, seleccionadas);
                spinnerProducto.setAdapter(adapter);
            }else{
                showDataFetchError("No tienes tarjetas", "Actualmente no tienes tarjetas para bloquear");
            }
        } catch (Exception e) {
            showDialogError("Lo sentimos", "Se ha producido un error, inténtalo nuevamente en unos minutos.");
            showErrorWithRefresh();
        }
    }

    @Override
    public void showReasonsBlockCard(List<String> motivosBloqueo) {
        try {
            if(motivosBloqueo == null) return;
            state.setMotivosBloqueoTarjeta(motivosBloqueo);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.list_item_spinner, motivosBloqueo);
            spinnerMotivoBloqueo.setAdapter(adapter);
        } catch (Exception e) {
            showDataFetchError("Lo sentimos", "Se ha producido un error, inténtalo nuevamente en unos minutos.");
        }
    }

    @Override
    public void confirmBlockCard(ResponseTarjeta tarjeta) {
        try {
            if (tarjeta != null) {
                String motivo = null;
                if (spinnerMotivoBloqueo != null && spinnerMotivoBloqueo.getSelectedItem() != null) {
                    motivo = spinnerMotivoBloqueo.getSelectedItem().toString();
                }

                if (motivo == null || TextUtils.isEmpty(motivo)) {
                    showDialogError("Datos incompletos", "Debe seleccionar el motivo por el cual desea bloquear su Tarjeta.");
                    return;
                }

                final String _motivo = motivo;
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(R.layout.pop_up_confirm);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView titleMessage = (TextView) dialog.findViewById(R.id.lbl_title_message);
                titleMessage.setText("¿Confirma tu solicitud?");
                TextView contentMessage = (TextView) dialog.findViewById(R.id.lbl_content_message);
                contentMessage.setText("¿Deseas cancelar tu Tarjeta?");
                ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.buttonClose);
                buttonClose.setOnClickListener(view -> dialog.dismiss());
                Button buttonAceptar = (Button) dialog.findViewById(R.id.btnAceptar);
                buttonAceptar.setOnClickListener(view -> {
                    blockCard(tarjeta, _motivo);
                    dialog.dismiss();
                });
                dialog.show();
            } else {
                showDataFetchError("Lo sentimos","No se ha cargado la Tarjeta, por favor intente de nuevo");
            }
        } catch (Exception e) {
            showDataFetchError("Lo sentimos", "Se ha producido un error, inténtalo nuevamente en unos minutos.");
        }
    }

    @Override
    public void blockCard(ResponseTarjeta tarjeta, String motivoBloqueo) {
        try{
            Encripcion encripcion = Encripcion.getInstance();
            String idDispositivo = context.obtenerIdDispositivo();
            RequestBloquearTarjeta bloquearTarjeta = new RequestBloquearTarjeta(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    tarjeta.getK_mnumpl(),
                    motivoBloqueo,
                    "B",
                    idDispositivo
            );
            presenter.blockCard(bloquearTarjeta);
        }catch (Exception e){
            showDataFetchError("Lo sentimos", "Se ha producido un error, inténtalo nuevamente en unos minutos.");
        }
    }

    @Override
    public void showResultBlockCard(String resultBlockCard) {
        try{
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
            contentMessage.setText(resultBlockCard);
            ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.buttonClose);
            buttonClose.setOnClickListener(view -> {
                updateStatePresenteCard(tarjeta, true);
                context.getState().getmTabHost().setCurrentTab(ActivityTabsView.TAB_14_PRESENTE_CARD_SECURITY_MENU_TAG);
                dialog.dismiss();
            });
            dialog.show();
        }catch (Exception e){
            showDataFetchError("Lo sentimos", "Se ha producido un error, inténtalo nuevamente en unos minutos.");
        }
    }

    @Override
    public void showSectionBlockCard(){
        contentBlockCard.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideSectionBlockCard(){
        contentBlockCard.setVisibility(View.GONE);
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
        contentBlockCard.setVisibility(View.GONE);
        layoutCircularProgressBar.setVisibility(View.VISIBLE);
        circularProgressBar.setVisibility(View.GONE);
        textCircularProgressBar.setText("Ha ocurrido un error, inténtalo de nuevo ");
        buttonReferesh.setVisibility(View.VISIBLE);
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
    public void showExpiredToken(String message) {
        final Dialog dialog = new Dialog(context);
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


//    private void mostrarTarjetas(List<ResponseTarjeta> tarjetas) {
//        try {
//            if(tarjetas == null) return;
//            ArrayList<ResponseTarjeta> seleccionadas = new ArrayList<>();
//            ArrayList<String> numeros = new ArrayList<>();
//            for (ResponseTarjeta t: tarjetas){
//                if(!numeros.contains(t.getK_mnumpl()) && t.getI_estado().equalsIgnoreCase("A")){
//                    seleccionadas.add(t);
//                    numeros.add(t.getK_mnumpl());
//                }
//            }
//            ArrayAdapter<ResponseTarjeta> adapter = new ArrayAdapter<>(context, R.layout.list_item_spinner, seleccionadas);
//            spinnerProducto.setAdapter(adapter);
//        } catch (Exception e) {
//            context.makeErrorDialog(e.getMessage());
//        }
//    }
//
//    private void mostrarMotivos(ArrayList<String> motivos) {
//        try {
//            if(motivos == null) return;
//            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.list_item_spinner, motivos);
//            spinnerMotivoBloqueo.setAdapter(adapter);
//        } catch (Exception e) {
//            context.makeErrorDialog(e.getMessage());
//        }
//    }
//
//    class MotivosTask extends AsyncTask<String, String, String> {
//
//        String cedula = null;
//        String token = null;
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                Encripcion encripcion = Encripcion.getInstance();
//                JSONObject param = new JSONObject();
//                param.put("cedula", cedula = encripcion.encriptar(params[0]));
//                param.put("token", token = params[1]);
//                return networkHelper.writeService(param, SincroHelper.MOTIVOS_BLOQUEO_TARJETA);
//            } catch (Exception e) {
//                return e.getMessage();
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            procesarJSONmotivos(result);
//        }
//    }
//
//    private void procesarJSONmotivos(String result) {
//        try {
//            ArrayList<String> motivos = SincroHelper.procesarJsonMotivosBloqueo(result);
//            context.getState().setMotivosBloqueoTarjeta(motivos);
//            mostrarMotivos(motivos);
//        } catch (ErrorTokenException e) {
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
//        } catch (Exception e) {
//            context.makeErrorDialog(e.getMessage());
//        }
//    }
//
//    class BloquearTask extends AsyncTask<JSONObject, String, String> {
//
//        ResponseTarjeta tarjeta;
//
//        BloquearTask(ResponseTarjeta tarjeta){
//            this.tarjeta = tarjeta;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Cancelando tarjeta..");
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(JSONObject... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                return networkHelper.writeService(params[0], SincroHelper.BLOQUEAR_ACTIVAR_TARJETA);
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
//            procesarResultBloquear(result, this.tarjeta);
//        }
//    }
//
//    private void procesarResultBloquear(String result, final ResponseTarjeta tarjeta) {
//        try {
//            result = SincroHelper.procesarJsonCrearSolicitudAhorro(result);
//            AlertDialog.Builder d = new AlertDialog.Builder(context);
//            d.setTitle(context.getResources().getString(R.string.app_name));
//            d.setIcon(R.mipmap.icon_presente);
//            d.setMessage(result);
//            d.setCancelable(false);
//            d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    context.getState().bloquearActivarTarjetas(tarjeta, true);
//                    context.getState().getmTabHost().setCurrentTab(16);
//                }
//            });
//            d.show();
//        } catch (ErrorTokenException e) {
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
//        } catch (Exception e) {
//            context.makeErrorDialog(e.getMessage());
//        }
//    }
//
//    private void bloquearTarjeta(final ResponseTarjeta tarjeta) {
//        try {
//            if (tarjeta != null) {
//
//                String motivo = null;
//                if (spinnerMotivoBloqueo != null && spinnerMotivoBloqueo.getSelectedItem() != null) {
//                    motivo = spinnerMotivoBloqueo.getSelectedItem().toString();
//                }
//
//                if (motivo == null || TextUtils.isEmpty(motivo)) {
//                    context.makeErrorDialog("Debe seleccionar el motivo por el cual desea bloquear su Tarjeta");
//                    return;
//                }
//                final String _motivo = motivo;
//                AlertDialog.Builder d = new AlertDialog.Builder(context);
//                d.setTitle(context.getResources().getString(R.string.app_name));
//                d.setIcon(R.mipmap.icon_presente);
//                d.setMessage("¿Estás seguro que deseas cancelar tu Tarjeta?");
//                d.setCancelable(false);
//                d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//
//                        try{
//                            Usuario usuario = context.getState().getUsuario();
//                            Encripcion encripcion = Encripcion.getInstance();
//                            String idDispositivo = context.obtenerIdDispositivo();
//                            JSONObject obj = new JSONObject();
//                            obj.put("cedula", encripcion.encriptar(usuario.cedula));
//                            obj.put("token", usuario.token);
//                            obj.put("numeroTarjeta", tarjeta.getK_mnumpl());
//                            obj.put("motivo", _motivo);
//                            obj.put("estado", "B");
//                            obj.put("id_dispositivo", idDispositivo);
//
//                            new BloquearTask(tarjeta).execute(obj);
//                        }catch (Exception e){
//                            context.makeErrorDialog(e.getMessage());
//                        }
//                    }
//                });
//                d.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                    }
//                });
//                d.show();
//            } else {
//                context.makeErrorDialog("No se ha cargado la Tarjeta, por favor intente de nuevo");
//            }
//
//        } catch (Exception e) {
//            context.makeErrorDialog(e.getMessage());
//        }
//    }

}
