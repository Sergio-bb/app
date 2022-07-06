package solidappservice.cm.com.presenteapp.front.tranferencias.FragmentRegisterAccount;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.entities.transferencias.request.RequestRegisterAccount;
import solidappservice.cm.com.presenteapp.entities.transferencias.response.ResponseBanco;
import solidappservice.cm.com.presenteapp.entities.transferencias.response.ResponseCuentasInscritas;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 20/05/2016.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 11/09/2021.
 */
public class FragmentRegisterAccountView extends Fragment implements FragmentRegisterAccountContract.View{

    private FragmentRegisterAccountPresenter presenter;
    private ActivityTabsView context;
    private GlobalState state;
    private Dialog pd;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.spBancoDestino)
    Spinner spBancoDestino;
    @BindView(R.id.txtCedulaDestinatario)
    EditText txtCedulaDestinatario;
    @BindView(R.id.txtCuentaDestinatario)
    EditText txtCuentaDestinatario;
    @BindView(R.id.txtNombreCuentaDestinatario)
    EditText txtNombreCuentaDestinatario;
    @BindView(R.id.btnInscribirCuenta)
    Button btnInscribirCuenta;

    @BindView(R.id.contentRegistrarCuenta)
    ScrollView contentRegistrarCuenta;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de inscripcion cuentas");
        firebaseAnalytics.logEvent("pantalla_inscripcion_cuentas", params);
        context = (ActivityTabsView) getActivity();
        View view = inflater.inflate(R.layout.fragment_transfers_registeraccount,container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentRegisterAccountPresenter(this, new FragmentRegisterAccountModel());
        context = (ActivityTabsView) getActivity();
        state = context.getState();
        fetchBanks();
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null || state.getUsuario() == null){
            context.salir();
        }
    }

    @OnTextChanged(R.id.txtCedulaDestinatario)
    public void afterTextChanged(Editable arg0) {
        enabledRegisterAccountButton();
    }

    @OnClick(R.id.btnInscribirCuenta)
    public void onClickRegisterAccount(){
        if(!TextUtils.isEmpty(txtCedulaDestinatario.getText()) &&
                txtCedulaDestinatario.getText().toString().equals(state.getUsuario().getCedula())){
            txtCedulaDestinatario.setError("No es posible inscribir tu propia cuenta");
        }else{
            fetchRegisteredAccounts();
        }
    }

    @OnClick(R.id.imageReferesh)
    public void onClickRefresh(){
        state.setBancos(null);
        fetchBanks();
    }


    @Override
    public void fetchBanks(){
        try{
            if(state.getBancos() != null && state.getBancos().size() > 0){
                hideCircularProgressBar();
                showSectionRegisterAccount();
                showBanks(state.getBancos());
            }else{
                presenter.fetchBanks();
            }
        }catch (Exception ex){
            showDialogError("Lo sentimos", "");
            showErrorWithRefresh();
        }
    }

    @Override
    public void showBanks(List<ResponseBanco> listaResponseBancos){
        try{
            if(listaResponseBancos != null && listaResponseBancos.size() > 0){
                state.setBancos(listaResponseBancos);
                ArrayAdapter<ResponseBanco> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, android.R.id.text1, listaResponseBancos);
                spBancoDestino.setAdapter(adapter);
            }
        }catch (Exception ex){
            showDialogError("Lo sentimos", "");
            showErrorWithRefresh();
        }
    }

    @Override
    public void fetchRegisteredAccounts(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchRegisteredAccounts(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void validateRepeatAccounts(List<ResponseCuentasInscritas> responseCuentasInscritas){
        boolean isCuentaRepetida = false;
        if(responseCuentasInscritas != null){
            for (ResponseCuentasInscritas ci: responseCuentasInscritas) {
                if (ci.getAanumnit().equals(txtCedulaDestinatario.getText().toString())) {
                    isCuentaRepetida = true;
                }
            }
            if(!isCuentaRepetida){
                registerAccount();
            } else{
                showDialogError("Lo sentimos", "Esta cuenta ya se encuentra inscrita.");
            }
        }
    }

    @Override
    public void registerAccount(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            String idDispositivo = context.obtenerIdDispositivo();
            String cedulaInscripcion = txtCedulaDestinatario.getText().toString();
            String nombreCuenta = txtNombreCuentaDestinatario.getText().toString();

            if(TextUtils.isEmpty(idDispositivo)
                    || TextUtils.isEmpty(nombreCuenta)
                    || TextUtils.isEmpty(cedulaInscripcion)){
                showDialogError("Datos incompletos", "Todos los campos son obligatorios, por favor verifica los datos ingresados");
                return;
            }
            presenter.registerAccount(new RequestRegisterAccount(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    idDispositivo,
                    "A",
                    nombreCuenta,
                    encripcion.encriptar(((ResponseBanco)spBancoDestino.getSelectedItem()).getCodigo()),
                    "0",
                    encripcion.encriptar(cedulaInscripcion)
            ));
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
            enabledRegisterAccountButton();
        }
    }

    @Override
    public void showResultRegisterAccount(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_success);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView titleMessage = (TextView) dialog.findViewById(R.id.titleSuccess);
        titleMessage.setText("Solicitud Enviada");
        TextView contentMessage = (TextView) dialog.findViewById(R.id.contentSuccess);
        contentMessage.setText("La solicitud para registrar la cuenta se ha realizado correctamente.");
        ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.buttonClose);
        buttonClose.setOnClickListener(view -> {
            txtCedulaDestinatario.setText("");
            txtCuentaDestinatario.setText("");
            txtNombreCuentaDestinatario.setText("");
            state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_23_NEQUI_MENU_SEND_MONEY_TAG);
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void disabledRegisterAccountButton() {
        btnInscribirCuenta.setEnabled(false);
    }

    @Override
    public void enabledRegisterAccountButton(){
        if (btnInscribirCuenta != null) {
            btnInscribirCuenta.setEnabled(true);
        }
    }

    @Override
    public void showSectionRegisterAccount(){
        contentRegistrarCuenta.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideSectionRegisterAccount(){
        if (contentRegistrarCuenta.getVisibility() == View.VISIBLE && contentRegistrarCuenta != null)  {
            contentRegistrarCuenta.setVisibility(View.GONE);
        }
    }

    @Override
    public void showCircularProgressBar(String textProgressBar) {
        if (layoutCircularProgressBar.getVisibility() == View.GONE && layoutCircularProgressBar != null) {
            layoutCircularProgressBar.setVisibility(View.VISIBLE);
            textCircularProgressBar.setText(textProgressBar);
        }
    }

    @Override
    public void hideCircularProgressBar() {
        if (layoutCircularProgressBar.getVisibility() == View.VISIBLE && layoutCircularProgressBar != null) {
            layoutCircularProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showErrorWithRefresh(){
        try {
            contentRegistrarCuenta.setVisibility(View.GONE);
            layoutCircularProgressBar.setVisibility(View.VISIBLE);
            circularProgressBar.setVisibility(View.GONE);
            textCircularProgressBar.setText("Ha ocurrido un error, inténtalo de nuevo ");
            buttonReferesh.setVisibility(View.VISIBLE);
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
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
        if(pd != null){
            pd.dismiss();
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
        TextView titleMessage = dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText("Lo sentimos");
        TextView contentMessage = dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        ImageButton buttonClose = dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(view -> {
            txtCedulaDestinatario.setText("");
            txtNombreCuentaDestinatario.setText("");
            dialog.dismiss();
            state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_17_TRANSFERS_MENU_TAG);
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
        TextView titleMessage = dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText(title);
        TextView contentMessage = dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        ImageButton buttonClose = dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(view -> {
            txtCedulaDestinatario.setText("");
            txtNombreCuentaDestinatario.setText("");
            dialog.dismiss();
            state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_17_TRANSFERS_MENU_TAG);
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
        Button buttonClosedSession = dialog.findViewById(R.id.btnVolverAIngresar);
        buttonClosedSession.setOnClickListener(view -> {
            dialog.dismiss();
            context.salir();
        });
        dialog.show();
    }
}