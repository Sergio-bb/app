package solidappservice.cm.com.presenteapp.front.nequi.transfieredinero.FragmentDispersiones;


import static solidappservice.cm.com.presenteapp.tools.constants.Constans.ERROR_CONTACTA_PRESENTE;

import android.app.Dialog;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.nequi.CuentasNequiSpinnerAdapter;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.nequi.dto.SuscriptionData;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestPaymentDispersion;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestReversePaymentDispersion;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultarTopes;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponsePaymentDispersionError;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.popups.PopUp;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.tools.constants.Constans;
import solidappservice.cm.com.presenteapp.tools.helpers.DialogHelpers;
import solidappservice.cm.com.presenteapp.tools.helpers.NumberTextWatcher;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

public class FragmentDispersionesView extends Fragment implements FragmentDispersionesContract.View{

    private FragmentDispersionesPresenter presenter;
    private ActivityTabsView baseView;
    private ActivityBase context;
    private GlobalState state;
    private FirebaseAnalytics firebaseAnalytics;
    private Dialog dialogConfirmTransfer;
    private Dialog dialogTransferLoading;
    public static Integer costoTransferencia;

    private ResponseConsultarTopes topes;
    private List<ResponseProducto> listCuentasOrigen;
    private ResponseProducto cuentaOrigen;
    private boolean pocketAvailable;
    private boolean pocketPayroll;
    private Integer valorTransferencia;

    @BindView(R.id.contentNequiDispersiones)
    ScrollView contentNequiDispersiones;

    @BindView(R.id.layout_circular_progress_bar)
    LinearLayout layoutCircularProgressBar;
    @BindView(R.id.circular_progress_bar)
    ProgressBar circularProgressBar;
    @BindView(R.id.text_circular_progress_Bar)
    TextView textCircularProgressBar;
    @BindView(R.id.imageReferesh)
    ImageView buttonReferesh;
    @BindView(R.id.sp_dispersion_cuentaOrigen)
    Spinner spinnerCuentaOrigen;
    @BindView(R.id.txtValorTransferencia)
    EditText txtValorTransferencia;
    @BindView(R.id.btnContinuar)
    Button button_continuar;
    @BindView(R.id.txt_minimo)
    TextView txtMinimo;
    @BindView(R.id.txt_maximo)
    TextView txtMaximo;
    public TextView txtCosto;

    public Boolean isVinculated = true;

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
        params.putString("Descripción", "Interacción con pantalla de transferencias presente-nequi");
        firebaseAnalytics.logEvent("pantalla_pago_dispersiones", params);
        View view = inflater.inflate(R.layout.fragment_nequi_sendmoney_dispersiones, container, false);
        ButterKnife.bind(this, view);
        txtCosto = view.findViewById(R.id.txt_costo);

        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentDispersionesPresenter(this, new FragmentDispersionesModel());
        context = (ActivityBase) getActivity();
        baseView = (ActivityTabsView) getActivity();
        state = context.getState();
        txtValorTransferencia.addTextChangedListener(new NumberTextWatcher(txtValorTransferencia));
        pocketAvailable = true;
        if(state.getDatosSuscripcion() == null){
            showDataFetchError(Constans.LO_SENTIMOS,"No hemos logrado obtener los datos de tu vinculación a Nequi, inténtalo nuevamente en unos minutos.");
        }else{
            fetchAccountsAvailable();
        }
        presenter.getCostoPorOperacion();
        validateSubscriptionNequi();
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if (state == null || state.getUsuario() == null) {
            context.salir();
        }
    }

    @OnClick(R.id.btnContinuar)
    public void onClickContinuar() {
        validateDataTransfer();
    }

    @OnClick(R.id.buttonCancelar)
    public void onClickCancelar(){
        if(txtValorTransferencia != null){
            txtValorTransferencia.setText("");
        }
        state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_23_NEQUI_MENU_SEND_MONEY_TAG);
    }

    @OnClick(R.id.imageReferesh)
    public void onClickRefresh(){
        if(state.getDatosSuscripcion() == null){
            showDataFetchError(Constans.LO_SENTIMOS,"No hemos logrado obtener los datos de tu suscripción, inténtalo nuevamente en unos minutos.");
        }else{
            fetchAccountsAvailable();
        }
    }

    @Override
    public void fetchAccountsAvailable() {
        presenter.fetchAccountsAvailable();
    }

    @Override
    public void resultAccountsAvailable(boolean pocketAvailable, boolean pocketPayroll) {
        this.pocketAvailable = pocketAvailable;
        this.pocketPayroll = pocketPayroll;
    }
    @Override
    public void saveSuscriptionData(SuscriptionData datosSuscripcion){
        state.setDatosSuscripcion(datosSuscripcion);
    }

    @Override
    public void fetchAccounts() {
        try {
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchAccounts(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        } catch (Exception ex) {
            showDataFetchError(Constans.LO_SENTIMOS, "");
        }
    }
    @Override
    public void showAccounts(List<ResponseProducto> cuentasPropias) {
        try {
            if (cuentasPropias != null && cuentasPropias.size()>0 && spinnerCuentaOrigen != null) {
                List<ResponseProducto> cuentasPropiasFinal = new ArrayList<>();
                if (pocketAvailable && pocketPayroll) {
                    for (ResponseProducto ec : cuentasPropias ) {
                        if ( ec.getI_debito() != null
                                && ec.getI_debito().equals("Y")) {
                            cuentasPropiasFinal.add(ec);
                        }
                    }
                }else if(pocketAvailable) {
                    for(ResponseProducto ec : cuentasPropias){
                        if(ec.getA_tipodr() != null && ec.getA_tipodr().equals("10") && ec.getI_debito().equals("Y")){
                            cuentasPropiasFinal.add(ec);
                        }
                    }
                }else if(pocketPayroll) {
                    for(ResponseProducto ec : cuentasPropias){
                        if(ec.getA_tipodr() != null && ec.getA_tipodr().equals("11") && ec.getI_debito().equals("Y")){
                            cuentasPropiasFinal.add(ec);
                        }
                    }
                }
                CuentasNequiSpinnerAdapter adapter = new CuentasNequiSpinnerAdapter(context, cuentasPropiasFinal, true);
                spinnerCuentaOrigen.setAdapter(adapter);
                this.listCuentasOrigen = cuentasPropiasFinal;
            }else{
                showDataFetchError(Constans.LO_SENTIMOS,"No hemos logrado obtener tus cuentas activas en Presente, inténtalo de nuevo en unos minutos");
            }
        } catch (Exception ex) {
            showDataFetchError(Constans.LO_SENTIMOS,"No hemos logrado obtener tus cuentas activas en Presente, inténtalo de nuevo en unos minutos");
        }
    }

    @Override
    public void fetchMaximumTranferValues() {
        try {
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchMaximumTranferValues(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        } catch (Exception ex) {
            showDataFetchError(Constans.LO_SENTIMOS,"Tenemos un error para cargar el estado de tus transacciones en Nequi, inténtalo de nuevo en unos minutos");
        }
    }
    @Override
    public void showMaximumTranferValues(ResponseConsultarTopes topes) {
        this.topes = topes;
        txtMinimo.setText("Mínimo: "+context.getMonedaWithOutDecimals(topes.getMinimoTransferencia()));
        txtMaximo.setText("Máximo: "+context.getMonedaWithOutDecimals(topes.getMaximoTransferencia()));
        txtCosto.setText("Costo: $" + costoTransferencia);
    }
    private void validateSubscriptionNequi(){
        presenter.validateSuscriptionNequi(new BaseRequest(
                Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
                state.getUsuario().getToken()));
    }

    @Override
    public void validateDataTransfer() {
        try {
            if(!isVinculated){
               showErrorSuscription();
            }else{
                if(topes == null){
                    showDataFetchError(Constans.LO_SENTIMOS,Constans.TENEMOS_PROBLEMAS_CONSULTANDO_ESTADO_CUENTA);
                    return;
                }else{
                    if(topes.getMinimoTransferencia() <= 0 || topes.getMaximoTransferencia()<=0){
                        showDataFetchError(Constans.LO_SENTIMOS, Constans.TENEMOS_PROBLEMAS_CONSULTANDO_ESTADO_CUENTA);
                        return;
                    }
                }

                if (spinnerCuentaOrigen != null) {
                    cuentaOrigen = (ResponseProducto) spinnerCuentaOrigen.getSelectedItem();
                }

                if (cuentaOrigen == null || cuentaOrigen.getN_tipodr() == null || cuentaOrigen.getN_tipodr().equals("")) {
                    CuentasNequiSpinnerAdapter adapter = (CuentasNequiSpinnerAdapter)spinnerCuentaOrigen.getAdapter();
                    View view = spinnerCuentaOrigen.getSelectedView();
                    adapter.setError(view, "Selecciona tu cuenta destino");
                    return;
                }

                double valorTransferencia;
                if (txtValorTransferencia.getText() == null || TextUtils.isEmpty(txtValorTransferencia.getText().toString())) {
                    txtValorTransferencia.setError(context.getResources().getString(R.string.error_valor_transfer));
                    return;
                } else {
                    try {
                        String cleanString = txtValorTransferencia.getText().toString().replaceAll("[$,.]", "");
                        valorTransferencia = Double.parseDouble(cleanString);
                    } catch (Exception e) {
                        txtValorTransferencia.setError(context.getResources().getString(R.string.error_valor_transfer));
                        return;
                    }
                }

                if (valorTransferencia <= 0) {
                    txtValorTransferencia.setError(context.getResources().getString(R.string.error_valor_transfer));
                    return;
                }

                if (valorTransferencia < topes.getMinimoTransferencia()) {
                    txtValorTransferencia.setError("El mínimo de transferencia es: "+context.getMonedaWithOutDecimals(topes.getMinimoTransferencia()));
                    return;
                }

                if (valorTransferencia > topes.getMaximoTransferencia()) {
                    txtValorTransferencia.setError("El máximo de transferencia es: "+context.getMonedaWithOutDecimals(topes.getMaximoTransferencia()));
                    return;
                }

                if((valorTransferencia + costoTransferencia ) > cuentaOrigen.getV_saldo()){
                    showInsuficcientBalance();
                    return;
                }

                if(topes.getMaximoDia() <= 0){
                    showDataFetchError(Constans.LO_SENTIMOS,"Has superado el límite de transferencias diario");
                    return;
                }

                if(topes.getMaximoMes() <= 0){
                    showDataFetchError(Constans.LO_SENTIMOS,"Has superado el límite de transferencias en el mes");
                    return;
                }
                final int _valorTransferencia = (int) valorTransferencia;
                this.valorTransferencia = _valorTransferencia;
                txtValorTransferencia.clearFocus();
                showDialogConfirmTransfer(_valorTransferencia, cuentaOrigen);
            }
        } catch (Exception ex) {
            showDataFetchError(Constans.LO_SENTIMOS, "");
        }
    }

    @Override
    public void showDialogConfirmTransfer(int valorTransferencia, ResponseProducto cuentaOrigen){
        dialogConfirmTransfer = getDialog(R.layout.pop_up_nequi_transfer_validatedata);
        TextView txtValortranfer = dialogConfirmTransfer.findViewById(R.id.tV_TransferirValor);
        txtValortranfer.setText(context.getMoneda(valorTransferencia));
        TextView texCostoTransaccion = dialogConfirmTransfer.findViewById(R.id.tV_CostoTransaccion);
        texCostoTransaccion.setText("$"+costoTransferencia.toString());

        TextView tV_CuentaOrigen = dialogConfirmTransfer.findViewById(R.id.tV_CuentaOrigen);
        tV_CuentaOrigen.setText(cuentaOrigen.getN_produc());

        Button buttonContinuar = dialogConfirmTransfer.findViewById(R.id.buttonContinuar);
        buttonContinuar.setOnClickListener(v -> makePaymentDispersion(valorTransferencia, cuentaOrigen));
        Button buttonCancelar = dialogConfirmTransfer.findViewById(R.id.buttonCancelar);
        buttonCancelar.setOnClickListener(v -> dialogConfirmTransfer.dismiss());
        dialogConfirmTransfer.show();
    }

    @Override
    public void hideDialogConfirmTransfer(){
        dialogConfirmTransfer.dismiss();
    }

    @Override
    public void makePaymentDispersion(int valorTransferencia, ResponseProducto cuentaOrigen) {
        try{
            presenter.makePaymentDispersion(new RequestPaymentDispersion(
                    Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    valorTransferencia,
                    cuentaOrigen.getA_numdoc()
            ));
        }catch (Exception ex){
            showDataFetchError(Constans.LO_SENTIMOS, "");
        }
    }

    @Override
    public void showDialogTransferLoading(){
        dialogTransferLoading = getDialog(R.layout.pop_up_nequi_transfer_loading);
        dialogTransferLoading.show();
    }

    @Override
    public void hideDialogTransferLoading(){
        dialogTransferLoading.dismiss();
    }

    @Override
    public void showDialogTransferSuccess(){
        final Dialog dialog = getDialog(R.layout.pop_up_nequi_transfer_success);
        ImageButton buttonClose = dialog.findViewById(R.id.button_closeTranferNequi);
        buttonClose .setOnClickListener(v -> {
            txtValorTransferencia.setText("");
            state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_23_NEQUI_MENU_SEND_MONEY_TAG);
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void showDialogTransferError(String message) {
        final Dialog dialog = getDialog(R.layout.pop_up_nequi_transfer_error);
        ImageButton buttonClose = dialog.findViewById(R.id.button_closeTranferNequi);
        buttonClose.setOnClickListener(v -> {
            txtValorTransferencia.setText("");
            state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_23_NEQUI_MENU_SEND_MONEY_TAG);
            dialog.dismiss();
        });
        ImageView consultaSaldos = dialog.findViewById(R.id.imageViewConsultaTusSaldos);
        consultaSaldos.setOnClickListener(v -> {
            txtValorTransferencia.setText("");
            state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_0_STATUS_ACCOUNT_TAG);
            dialog.dismiss();
        });
        ImageView irAlInicio = dialog.findViewById(R.id.imageViewIrAlInicio);
        irAlInicio.setOnClickListener(v -> {
            txtValorTransferencia.setText("");
            dialog.dismiss();
            baseView.finish();
        });
        TextView descriptionError = dialog.findViewById(R.id.textViewSalioMalDescrip);
        if(!TextUtils.isEmpty(message)){
            descriptionError.setText(message);
        }
        dialog.show();
    }

    @Override
    public void fetchReverseDispersion(ResponsePaymentDispersionError resultTransfer) {
        try{
            presenter.fetchReversePaymentDispersion(new RequestReversePaymentDispersion(
                    Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    String.valueOf(resultTransfer.getV_RegID()),
                    resultTransfer.getV_a_Numcta(),
                    resultTransfer.getV_ValorOperacion(),
                    resultTransfer.getTrackingID(),
                    resultTransfer.getV_ValorTotal(),
                    resultTransfer.getV_CobroTransa(),
                    resultTransfer.getV_UseCobro(),
                    resultTransfer.getV_UseCobroIva()
            ));
        }catch (Exception ex){
            showDataFetchError(Constans.LO_SENTIMOS, "");
        }
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
        contentNequiDispersiones.setVisibility(View.GONE);
        layoutCircularProgressBar.setVisibility(View.VISIBLE);
        circularProgressBar.setVisibility(View.GONE);
        textCircularProgressBar.setText("Ha ocurrido un error, inténtalo de nuevo ");
        buttonReferesh.setVisibility(View.VISIBLE);
    }

    @Override
    public void showContentNequiDispersiones(){
        contentNequiDispersiones.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideContentNequiDispersiones(){
        contentNequiDispersiones.setVisibility(View.GONE);
    }

    @Override
    public void showDialogError(String title, String message){
        try(PopUp popUp = new PopUp()){
            popUp.showError(title, message, context, state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showErrorTimeOut() {
        String errorMessage =validateMessageError("", 6);
        final Dialog dialog = getDialog(R.layout.pop_up_error);
        TextView titleMessage = dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText(Constans.LO_SENTIMOS);
        TextView contentMessage =  dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(errorMessage);
        ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(view -> {
            txtValorTransferencia.setText("");
            state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_23_NEQUI_MENU_SEND_MONEY_TAG);
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void showDataFetchError(String title, String message){
        String errorMessage = validateMessageError(message, 7);
        final Dialog dialog = getDialog(R.layout.pop_up_error);
        TextView titleMessage = dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText(title);
        TextView contentMessage = dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(errorMessage);
        ImageButton buttonClose = dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(view -> {
            txtValorTransferencia.setText("");
            state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_23_NEQUI_MENU_SEND_MONEY_TAG);
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void showExpiredToken(String message) {
       try (PopUp popUp = new PopUp()) {
           popUp.showExpiredToken(message, context);
       } catch (Exception e) {
           e.printStackTrace();
       }
    }
    public void showErrorSuscription(){
        final Dialog dialog = getDialog(R.layout.pop_up_error);
        TextView titleMessage = dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText("Lo sentimos");
        TextView contentMessage = dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText("Actualmente no tienes una cuenta nequi vinculada.");
        ImageButton buttonClose = dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(view -> {
            txtValorTransferencia.setText("");
            state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
            dialog.dismiss();
        });
        dialog.show();
    }

    public void showInsuficcientBalance(){
        final Dialog dialog = getDialog(R.layout.pop_up_saldo_insuficiente);
        Button buttonClose =  dialog.findViewById(R.id.btncerrarSaldoInsuficiente);
        buttonClose.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }
    public void showRebaseTopsNequi(){
        final Dialog dialog = getDialog(R.layout.pop_up_saldo_insuficiente);
        TextView titleMessage = dialog.findViewById(R.id.textViewValoraTransferirTextTitle);
        titleMessage.setText("Lo sentimos...");
        TextView contentMessage = dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText("No se ha completado tu solitud debido  a que superas el monto máximo permitido en tu cuenta Nequi");
        ImageButton buttonClose = dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(view -> {
            txtValorTransferencia.setText("");
            state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_23_NEQUI_MENU_SEND_MONEY_TAG);
            dialog.dismiss();
        });
        dialog.show();
    }

    private Dialog getDialog(Integer id){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(id);
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
