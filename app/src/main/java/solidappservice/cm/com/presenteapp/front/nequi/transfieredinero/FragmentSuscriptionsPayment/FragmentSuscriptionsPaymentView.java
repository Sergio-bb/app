package solidappservice.cm.com.presenteapp.front.nequi.transfieredinero.FragmentSuscriptionsPayment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestCheckStatusPaymentSuscription;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestPaymentSuscritpion;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestReversePaymentSuscription;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultarTopes;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponsePaymentSuscription;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.nequi.transfieredinero.ActivityDialogNequiBalance.ActivityDialogNequiBalanceView;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.helpers.NumberTextWatcher;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class FragmentSuscriptionsPaymentView extends Fragment implements FragmentSuscriptionsPaymentContract.View{

    private FragmentSuscriptionsPaymentPresenter presenter;
    private ActivityTabsView baseView;
    private ActivityBase context;
    private GlobalState state;
    private FirebaseAnalytics firebaseAnalytics;
    private Integer saldoNequi;
    private Dialog dialogConfirmTransfer;
    private Dialog dialogTransferLoading;

    private ResponseConsultarTopes topes;
    private List<ResponseProducto> listCuentasDestino;
    private ResponseProducto cuentaDestino;
    private boolean pocketAvailable;
    private boolean pocketPayroll;
    private Integer valorTransferencia;

    @BindView(R.id.contentNequiSuscripciones)
    ScrollView contentNequiPaySuscriptions;

    @BindView(R.id.layout_circular_progress_bar)
    LinearLayout layoutCircularProgressBar;
    @BindView(R.id.circular_progress_bar)
    ProgressBar circularProgressBar;
    @BindView(R.id.text_circular_progress_Bar)
    TextView textCircularProgressBar;
    @BindView(R.id.imageReferesh)
    ImageView buttonReferesh;

    @BindView(R.id.tv_cuenta_origen)
    TextView cuentaOrigen;
    @BindView(R.id.refreshSaldo)
    ImageView refreshSaldo;
    @BindView(R.id.tvConsultarSaldo)
    TextView buttonConsultaSaldo;
    @BindView(R.id.circular_progress_bar_nequi_balance)
    ProgressBar circularProgressBarNequiBalance;

    @BindView(R.id.etx_valor_transferencia)
    EditText txtValorTransferencia;
    @BindView(R.id.spinnerCuentaDestino)
    Spinner spinnerCuentaDestino;
    @BindView(R.id.btnSiguiente)
    Button buttonSiguiente;
    @BindView(R.id.buttonCancelar)
    Button buttonCancelar;
    @BindView(R.id.txt_minimo)
    TextView txtMinimo;
    @BindView(R.id.txt_maximo)
    TextView txtMaximo;
    @BindView(R.id.txt_costo)
    TextView txtCosto;

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
        params.putString("Descripción", "Interacción con pantalla de transferencias nequi");
        firebaseAnalytics.logEvent("pantalla_transferencias_nequi", params);
        View View = inflater.inflate(R.layout.fragment_nequi_sendmoney_suscriptions, container, false);
        ButterKnife.bind(this, View);
        setControls();
        return View;
    }

    protected void setControls() {
        presenter = new FragmentSuscriptionsPaymentPresenter(this, new FragmentSuscriptionsPaymentModel());
        context = (ActivityBase) getActivity();
        baseView = (ActivityTabsView) getActivity();
        state = context.getState();
        txtValorTransferencia.addTextChangedListener(new NumberTextWatcher(txtValorTransferencia));
        pocketAvailable = true;
        if(state.getDatosSuscripcion() == null){
            showDataFetchError("Lo sentimos","No hemos logrado obtener los datos de tu vinculación a Nequi, inténtalo nuevamente en unos minutos.");
        }else{
            fetchAccountsAvailable();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if (state == null || state.getUsuario() == null) {
            context.salir();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GlobalState.DIALOG_GET_NEQUI_BALANCE){
            showNequiBalance("");
        }
    }

    @OnClick(R.id.btnSiguiente)
    public void onClickSiguiente() {
        validateDataTransfer();
    }

    @OnClick(R.id.buttonCancelar)
    public void onClickCancelar(){
        state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_23_NEQUI_MENU_SEND_MONEY_TAG);
    }

    @OnClick(R.id.imageReferesh)
    public void onClickRefresh(){
        if(state.getDatosSuscripcion() == null){
            showDataFetchError("Lo sentimos","No hemos logrado obtener los datos de tu suscripción, inténtalo nuevamente en unos minutos.");
        }else{
            fetchAccountsAvailable();
        }
    }

    @OnClick(R.id.tvConsultarSaldo)
    public void onClickConsultarSaldo(){
        showDialogGetBalanceNequi();
    }

    @OnClick(R.id.refreshSaldo)
    public void onClickRefreshSaldo(){
        if(state != null && !state.isRefusedAuthorizationNequiBalance()){
            refreshSaldo.setVisibility(View.GONE);
            buttonConsultaSaldo.setVisibility(View.GONE);
            fetchNequiBalance();
        }
    }

    @Override
    public void showDialogGetBalanceNequi(){
        Intent i = new Intent(context, ActivityDialogNequiBalanceView.class);
        startActivityForResult(i, GlobalState.DIALOG_GET_NEQUI_BALANCE);
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
    public void fetchAccounts() {
        try {
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchAccounts(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        } catch (Exception ex) {
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void showAccounts(List<ResponseProducto> cuentasPropias) {
        try {
            if (cuentasPropias != null && cuentasPropias.size()>0 && spinnerCuentaDestino != null) {
                List<ResponseProducto> cuentasPropiasFinal = new ArrayList<>();
               if(pocketAvailable) {
                    for(ResponseProducto ec : cuentasPropias){
                        if(ec.getA_tipodr() != null && ec.getA_tipodr().equals("10") && ec.getI_debito().equals("Y")){
                            cuentasPropiasFinal.add(ec);
                        }
                    }
                }
                CuentasNequiSpinnerAdapter adapter = new CuentasNequiSpinnerAdapter(context, cuentasPropiasFinal, true);
                spinnerCuentaDestino.setAdapter(adapter);
                this.listCuentasDestino = cuentasPropiasFinal;
            }else{
                showDataFetchError("Lo sentimos","No hemos logrado obtener tu cuentas activas en Presente, inténtalo de nuevo en unos minutos");
            }
        } catch (Exception ex) {
            showDataFetchError("Lo sentimos","No hemos logrado obtener tu cuentas activas en Presente, inténtalo de nuevo en unos minutos");
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
            showDataFetchError("Lo sentimos","Tenemos un error para cargar el estado de tus transacciones en Nequi, inténtalo de nuevo en unos minutos");
        }
    }

    @Override
    public void showMaximumTranferValues(ResponseConsultarTopes topes) {
        this.topes = topes;
        txtMinimo.setText("Mínimo: "+context.getMonedaWithOutDecimals(topes.getMinimoTransferencia()));
        txtMaximo.setText("Máximo: "+context.getMonedaWithOutDecimals(topes.getMaximoTransferencia()));
        txtCosto.setText("Costo: $0");
    }

    @Override
    public void fetchIncompleteSubscriptionPayments() {
        Encripcion encripcion = Encripcion.getInstance();
        presenter.fetchIncompleteSubscriptionPayments(new BaseRequest(
                encripcion.encriptar(state.getUsuario().getCedula()),
                state.getUsuario().getToken()
        ));
    }

    @Override
    public void fetchNequiBalance() {
        try {
            if(state != null && state.isActiveStateNequiBalance()){
                presenter.fetchNequiBalance(new BaseRequestNequi(
                        Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
                        state.getUsuario().getToken(),
                        ""
                ));
            }else{
                hideCircularProgressBarNequiBalance();
                showNequiBalance("");
            }
        } catch (Exception ex) {
            if(state != null && state.isActiveStateNequiBalance()){
                fetchAuthorizationNequiBalance();
            }else{
                hideCircularProgressBarNequiBalance();
                showNequiBalance("");
            }
        }
    }

    @Override
    public void fetchAuthorizationNequiBalance(){
        try {
            presenter.fetchAuthorizationNequiBalance(new BaseRequestNequi(
                    Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    ""
            ));
        } catch (Exception ex) {
            if(state != null){
                state.setRefusedAuthorizationNequiBalance(false);
            }
            showNequiBalance("");
        }
    }

    @Override
    public void resultGetAuthorizationNequiBalance(String status){
        switch(status){
            case "0":
                showDialogError("Autorización pendiente", "Para consultar el saldo de tu cuenta Nequi, es importante que te dirijas a Nequi y aceptes la autorización.");
                showNequiBalance("");
                break;
            case "1":
                if(state != null){
                    state.setRefusedAuthorizationNequiBalance(false);
                }
                showNequiBalance("");
                break;
            case "2":
            case "3":
                if(state != null){
                    state.setRefusedAuthorizationNequiBalance(true);
                }
                showNequiBalance("");
                break;
            default:
                if(state != null && !state.isRefusedAuthorizationNequiBalance() && !state.isAlreadyOpenDialogNequiBalance()){
                    state.setAlreadyOpenDialogNequiBalance(true);
                    showDialogGetBalanceNequi();
                }else{
                    showNequiBalance("");
                }
                break;
        }
    }

    @Override
    public void showNequiBalance(String saldoNequi) {
        try {
            if (cuentaOrigen != null ) {
                if(context.tryParseDouble(saldoNequi)){
                    this.saldoNequi = Double.valueOf(saldoNequi).intValue();;//saldo
                    cuentaOrigen.setText(context.getMonedaWithOutDecimals(this.saldoNequi));
                    cuentaOrigen.setTypeface(Typeface.DEFAULT_BOLD);
                    cuentaOrigen.setTextColor(Color.BLACK);
                    cuentaOrigen.setTextSize(16);
                }else{
                    if(state != null && state.isRefusedAuthorizationNequiBalance()){
                        cuentaOrigen.setText("");
                        refreshSaldo.setVisibility(View.VISIBLE);;
                        buttonConsultaSaldo.setVisibility(View.VISIBLE);
                    }else{
                        cuentaOrigen.setText("");
                        refreshSaldo.setVisibility(View.VISIBLE);
                    }
                }
            }
        } catch (Exception ex) {
            this.saldoNequi = 0;//saldo
            cuentaOrigen.setText("");
            refreshSaldo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showCircularProgressBarNequiBalance() {
        circularProgressBarNequiBalance.setVisibility(View.VISIBLE);
        if(cuentaOrigen != null){
            cuentaOrigen.setText("");
        }
    }

    @Override
    public void hideCircularProgressBarNequiBalance() {
        circularProgressBarNequiBalance.setVisibility(View.GONE);
    }

    @Override
    public void validateDataTransfer() {
        try {
            if(topes == null){
                showDataFetchError("Lo sentimos","Tenemos problemas para consultar el estado de tu cuenta, inténtalo de nuevo en unos minutos");
                return;
            }else{
                if(topes.getMinimoTransferencia() <= 0 || topes.getMaximoTransferencia()<=0){
                    showDataFetchError("Lo sentimos","Tenemos problemas para consultar el estado de tu cuenta, inténtalo de nuevo en unos minutos");
                    return;
                }
            }

            if (spinnerCuentaDestino != null) {
                cuentaDestino = (ResponseProducto) spinnerCuentaDestino.getSelectedItem();
            }

            if (cuentaDestino == null || cuentaDestino.getN_tipodr() == null || cuentaDestino.getN_tipodr().equals("")) {
                CuentasNequiSpinnerAdapter adapter = (CuentasNequiSpinnerAdapter)spinnerCuentaDestino.getAdapter();
                View view = spinnerCuentaDestino.getSelectedView();
                adapter.setError(view, "Selecciona tu cuenta destino");
                TextView errorText = (TextView)spinnerCuentaDestino.getSelectedView();
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

            if(valorTransferencia > cuentaDestino.getV_saldo()){
//                txtValorTransferencia.setError("No tienes saldo suficiente para realizar esta transacción");
                showDialogTransferError("No tienes saldo suficiente para realizar esta transacción");
                return;
            }

            if(topes.getMaximoDia() <= 0){
                showDataFetchError("Lo sentimos","Has superado el límite de transferencias diario");
                return;
            }

            if(topes.getMaximoMes() <= 0){
                showDataFetchError("Lo sentimos","Has superado el límite de transferencias en el mes");
                return;
            }

            if (saldoNequi != null && saldoNequi > 0 && saldoNequi < valorTransferencia) {
                txtValorTransferencia.setError("No tienes suficiente saldo en tu cuenta de Nequi");
                return;
            }

            final int _valorTransferencia = (int) valorTransferencia;
            this.valorTransferencia = _valorTransferencia;
            showDialogConfirmTransfer(_valorTransferencia, cuentaDestino);
        } catch (Exception ex) {
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void showDialogConfirmTransfer(int valorTransferencia, ResponseProducto cuentaOrigen){
        dialogConfirmTransfer = new Dialog(context);
        dialogConfirmTransfer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogConfirmTransfer.setContentView(R.layout.pop_up_nequi_transfer_validatedata);
        dialogConfirmTransfer.setCancelable(true);
        dialogConfirmTransfer.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogConfirmTransfer.getWindow().setGravity(Gravity.CENTER);
            TextView txtValortranfer = dialogConfirmTransfer.findViewById(R.id.tV_TransferirValor);
            txtValortranfer.setText(context.getMonedaWithOutDecimals(valorTransferencia));

            TextView tV_CuentaOrigen = dialogConfirmTransfer.findViewById(R.id.tV_CuentaOrigen);
            tV_CuentaOrigen.setText(cuentaOrigen.getN_produc());

            Button buttonContinuar = dialogConfirmTransfer.findViewById(R.id.buttonContinuar);
            buttonContinuar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makePaymentSuscription(valorTransferencia, cuentaOrigen);
                }
            });
            Button buttonCancelar = dialogConfirmTransfer.findViewById(R.id.buttonCancelar);
            buttonCancelar.setOnClickListener(new View.OnClickListener()  {
                @Override
                public void onClick(View v) {
                    dialogConfirmTransfer.dismiss();
                }
            });
        dialogConfirmTransfer.show();
    }

    @Override
    public void hideDialogConfirmTransfer(){
        dialogConfirmTransfer.dismiss();
    }

    @Override
    public void makePaymentSuscription(int valorTransferencia, ResponseProducto cuentaOrigen) {
        try{
            presenter.makePaymentSuscription(new RequestPaymentSuscritpion(
                Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
                state.getUsuario().getToken(),
                valorTransferencia,
                cuentaDestino.getA_numdoc(),
                state.getDatosSuscripcion()
            ));
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void showDialogTransferLoading(){
        dialogTransferLoading = new Dialog(context);
        dialogTransferLoading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogTransferLoading.setContentView(R.layout.pop_up_nequi_transfer_loading);
        dialogTransferLoading.setCancelable(true);
        dialogTransferLoading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogTransferLoading.getWindow().setGravity(Gravity.CENTER);
            TextView titleLoading = dialogTransferLoading.findViewById(R.id.titleLoading);
            titleLoading.setText("Estamos trasladando tu dinero de Nequi a PRESENTE");
        dialogTransferLoading.show();
    }

    @Override
    public void editTextDialogTransferLoading(String text){
        TextView textLoading = dialogTransferLoading.findViewById(R.id.textLoading);
        textLoading.setText("Estamos trasladando tu dinero de Nequi a PRESENTE");
    }

    @Override
    public void hideDialogTransferLoading(){
        dialogTransferLoading.dismiss();
    }

    @Override
    public void showDialogTransferSuccess(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_nequi_transfer_success);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
            ImageButton buttonClose = dialog.findViewById(R.id.button_closeTranferNequi);
            buttonClose .setOnClickListener(new View.OnClickListener()  {
                @Override
                public void onClick(View v) {
                    txtValorTransferencia.setText("");
                    state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_23_NEQUI_MENU_SEND_MONEY_TAG);
                    dialog.dismiss();
                }
            });
        dialog.show();
    }

    @Override
    public void showDialogTransferError(String message){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_nequi_transfer_error);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        ImageButton buttonClose = dialog.findViewById(R.id.button_closeTranferNequi);
        buttonClose.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                txtValorTransferencia.setText("");
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_23_NEQUI_MENU_SEND_MONEY_TAG);
                dialog.dismiss();
            }
        });
        ImageView consultaSaldos = dialog.findViewById(R.id.imageViewConsultaTusSaldos);
        consultaSaldos.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                txtValorTransferencia.setText("");
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_0_STATUS_ACCOUNT_TAG);
                dialog.dismiss();
            }
        });
        ImageView irAlInicio = dialog.findViewById(R.id.imageViewIrAlInicio);
        irAlInicio.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                txtValorTransferencia.setText("");
                dialog.dismiss();
                baseView.finish();
            }
        });
        TextView descriptionError = dialog.findViewById(R.id.textViewSalioMalDescrip);
        if(!TextUtils.isEmpty(message)){
            descriptionError.setText(message);
        }
        dialog.show();
    }

    @Override
    public void showDialogTransferPending(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_nequi_transfer_pending);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        ImageButton buttonClose = dialog.findViewById(R.id.button_close);
        buttonClose .setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                txtValorTransferencia.setText("");
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_23_NEQUI_MENU_SEND_MONEY_TAG);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void fetchReversePaymentSubscription(ResponsePaymentSuscription resultTransfer) {
        try{
            presenter.fetchReversePaymentSubscription(new RequestReversePaymentSuscription(
                    Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    this.valorTransferencia,
                    cuentaDestino.getA_numdoc(),
                    new RequestReversePaymentSuscription.ReversePresente(
                        true,
                        resultTransfer.getIdTransaccionPresente(),
                        resultTransfer.getEstadoPagoPresente()
                    ),
                    new RequestReversePaymentSuscription.ReverseNequi(
                        resultTransfer.getEstadoPagoNequi() != null
                                && !resultTransfer.getEstadoPagoNequi().equals("S")
                                && resultTransfer.getEstadoPagoNequi().equals("F"),
                        resultTransfer.getIdTransaccionNequi(),
                        resultTransfer.getEstadoPagoNequi()
                    ),
                    state.getDatosSuscripcion()
            ));
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void fetchCheckStatusPaymentSubscription(ResponsePaymentSuscription resultTransfer) {
        try{
            presenter.fetchCheckStatusPaymentSubscription(new RequestCheckStatusPaymentSuscription(
                    Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    this.valorTransferencia,
                    resultTransfer.getIdTransaccionPresente(),
                    resultTransfer.getEstadoPagoPresente(),
                    resultTransfer.getIdTransaccionNequi(),
                    resultTransfer.getIdPagoNequi(),
                    resultTransfer.getEstadoPagoNequi(),
                    state.getDatosSuscripcion()
            ));
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void showContentNequiPaySuscriptions(){
        contentNequiPaySuscriptions.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideContentNequiPaySuscriptions(){
        contentNequiPaySuscriptions.setVisibility(View.GONE);
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
        contentNequiPaySuscriptions.setVisibility(View.GONE);
        layoutCircularProgressBar.setVisibility(View.VISIBLE);
        circularProgressBar.setVisibility(View.GONE);
        textCircularProgressBar.setText("Ha ocurrido un error, inténtalo de nuevo ");
        buttonReferesh.setVisibility(View.VISIBLE);
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
                txtValorTransferencia.setText("");
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_23_NEQUI_MENU_SEND_MONEY_TAG);
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
                txtValorTransferencia.setText("");
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_23_NEQUI_MENU_SEND_MONEY_TAG);
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

}
