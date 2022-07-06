package solidappservice.cm.com.presenteapp.front.nequi.qr.FragmentQRDetail;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import androidx.annotation.Nullable;
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
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestPaymentQR;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultarTopes;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.nequi.qr.ActivityQRDetail.ActivityQRDetailView;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.helpers.NumberTextWatcher;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

public class FragmentQRDetailView extends Fragment implements FragmentQRDetailContract.View{

    private FragmentQRDetailPresenter presenter;
    private ActivityQRDetailView baseView;
    private ActivityBase context;
    private GlobalState state;
    private FirebaseAnalytics firebaseAnalytics;
    private List<ResponseProducto> cuentasPropias;
    private Dialog dialogPaymentTransfer;
    private Dialog dialogPaymentLoading;

    private ResponseConsultarTopes topes;
    private List<ResponseProducto> listCuentasOrigen;
    private ResponseProducto cuentaOrigen;
    private boolean pocketAvailable;
    private boolean pocketPayroll;
    private Integer valorTransferencia;

    @BindView(R.id.spinnercuentaOrigen)
    Spinner spinnerCuentaOrigen;
    @BindView(R.id.etx_valor_pagoQr)
    EditText txtValorPagoQR;
    @BindView(R.id.nombreComercio)
    TextView nameComercio;
    @BindView(R.id.btnContinuar)
    Button btn_continuar;

    @BindView(R.id.contentNequiQR)
    ScrollView contentNequiQR;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla detalles del pago QR nequi");
        firebaseAnalytics.logEvent("pantalla_qr_detail", params);
        View view = inflater.inflate(R.layout.fragment_nequi_qr_detail, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentQRDetailPresenter(this, new FragmentQRDetailModel());
        context = (ActivityBase)getActivity();
        baseView = (ActivityQRDetailView) getActivity();
        state = context.getState();
        txtValorPagoQR.addTextChangedListener(new NumberTextWatcher(txtValorPagoQR));
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if (state == null || state.getUsuario() == null) {
            context.salir();
        } else {
            fetchAccountsAvailable();
        }
    }

    @OnClick(R.id.btnContinuar)
    public void onClickContinuar() {
//        validateDataPaymentQR();
        showDialogError("Lo sentimos","Esta función aún esta en desarrollo.");
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
            showDialogError("Lo sentimos", "");
            showErrorWithRefresh();
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
                showDialogError("Lo sentimos","No hemos logrado obtener tu cuentas activas en Presente, inténtalo de nuevo en unos minutos");
                showErrorWithRefresh();
            }
        } catch (Exception ex) {
            showDialogError("Lo sentimos","No hemos logrado obtener tu cuentas activas en Presente, inténtalo de nuevo en unos minutos");
            showErrorWithRefresh();
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
            showDialogError("Lo sentimos","Tenemos un error para cargar el estado de tus transacciones, inténtalo de nuevo en unos minutos");
            showErrorWithRefresh();
        }
    }
    @Override
    public void showMaximumTranferValues(ResponseConsultarTopes topes) {
        this.topes = topes;
    }

    @Override
    public void showDataCommerceText() {
        if(state != null && state.getDataCommerceQR() != null && state.getDataCommerceQR().getPaymentData() != null){
            nameComercio.setText(state.getDataCommerceQR().getPaymentData().getName());
        }else{
            showDataFetchError("Lo sentimos", "Tenemos problemas para obtener los datos de la cuenta de destino, inténtalo de nuevo en unos minutos");
        }
    }

    @Override
    public void validateDataPaymentQR() {
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

            if (spinnerCuentaOrigen != null) {
                cuentaOrigen = (ResponseProducto) spinnerCuentaOrigen.getSelectedItem();
            }

            if (cuentaOrigen == null || cuentaOrigen.getN_tipodr() == null || cuentaOrigen.getN_tipodr().equals("")) {
                CuentasNequiSpinnerAdapter adapter = (CuentasNequiSpinnerAdapter)spinnerCuentaOrigen.getAdapter();
                View view = spinnerCuentaOrigen.getSelectedView();
                adapter.setError(view, "Selecciona tu cuenta destino");
                TextView errorText = (TextView)spinnerCuentaOrigen.getSelectedView();
                return;
            }

            double valorTransferencia;
            if (txtValorPagoQR.getText() == null || TextUtils.isEmpty(txtValorPagoQR.getText().toString())) {
                txtValorPagoQR.setError(context.getResources().getString(R.string.error_valor_transfer));
                return;
            } else {
                try {
                    String cleanString = txtValorPagoQR.getText().toString().replaceAll("[$,.]", "");
                    valorTransferencia = Double.parseDouble(cleanString);
                } catch (Exception e) {
                    txtValorPagoQR.setError(context.getResources().getString(R.string.error_valor_transfer));
                    return;
                }
            }

            if (valorTransferencia <= 0) {
                txtValorPagoQR.setError(context.getResources().getString(R.string.error_valor_transfer));
                return;
            }

            if (valorTransferencia < topes.getMinimoTransferencia()) {
                txtValorPagoQR.setError("El mínimo de transferencia es: "+context.getMonedaWithOutDecimals(topes.getMinimoTransferencia()));
                return;
            }

            if (valorTransferencia > topes.getMaximoTransferencia()) {
                txtValorPagoQR.setError("El máximo de transferencia es: "+context.getMonedaWithOutDecimals(topes.getMaximoTransferencia()));
                return;
            }

            if(valorTransferencia > cuentaOrigen.getV_saldo()){
                txtValorPagoQR.setError("No tienes saldo para realizar esta transacción");
                return;
            }

            if(topes.getMaximoDia() <= 0){
                showDataFetchError("Lo sentimos","Haz superado el limite de transferencias diario");
                return;
            }

            if(topes.getMaximoMes() <= 0){
                showDataFetchError("Lo sentimos","Haz superado el limite de transferencias en el mes");
                return;
            }

            final int _valorTransferencia = (int) valorTransferencia;
            this.valorTransferencia = _valorTransferencia;
            showDialogConfirmPayment( _valorTransferencia, cuentaOrigen);
        } catch (Exception ex) {
            showDataFetchError("Lo sentimos","Tenemos un error para cargar el estado de tus transacciones, inténtalo de nuevo en unos minutos");
        }
    }

    @Override
    public void showDialogConfirmPayment(int valorpago, ResponseProducto cuentaOrigen) {
        dialogPaymentTransfer = new Dialog(context);
        dialogPaymentTransfer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPaymentTransfer.setContentView(R.layout.pop_up_qr_transfer_validatedata);
        dialogPaymentTransfer.setCancelable(true);
        dialogPaymentTransfer.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogPaymentTransfer.getWindow().setGravity(Gravity.CENTER);
        TextView txtCuentaOrigen = dialogPaymentTransfer.findViewById(R.id.tv_cuenta_origen);
        txtCuentaOrigen.setText(cuentaOrigen.getN_produc());
        TextView txtValortranfer = dialogPaymentTransfer.findViewById(R.id.tv_ValorPago);
        txtValortranfer.setText(context.getMonedaWithOutDecimals(valorpago));
        Button buttonContinuar = dialogPaymentTransfer.findViewById(R.id.buttonRealizarPago);
        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    makePaymentByQR(valorpago, cuentaOrigen);
                }catch (Exception e) {
                    showDialogPaymentError();
                }
            }
        });
        Button buttonCancelar = dialogPaymentTransfer.findViewById(R.id.buttonCancelar);
        buttonCancelar.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                dialogPaymentTransfer.dismiss();
            }
        });
        dialogPaymentTransfer.show();
    }

    @Override
    public void hideDialogConfirmPayment(){
        dialogPaymentTransfer.dismiss();
    }

    @Override
    public void showDialogPaymentError(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_nequi_transfer_error);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        ImageButton buttonClose = dialog.findViewById(R.id.button_closeTranferNequi);
        buttonClose.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                txtValorPagoQR.setText("");
                baseView.finish();
                dialog.dismiss();
            }
        });
        ImageView consultaSaldos = dialog.findViewById(R.id.imageViewConsultaTusSaldos);
        consultaSaldos.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                txtValorPagoQR.setText("");
                baseView.finish();
                dialog.dismiss();
            }
        });
        ImageView irAlInicio = dialog.findViewById(R.id.imageViewIrAlInicio);
        irAlInicio.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                txtValorPagoQR.setText("");
                baseView.finish();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void makePaymentByQR(int valorTransferencia, ResponseProducto cuentaOrigen) {
        try{
            presenter.makePaymentByQR(new RequestPaymentQR(
                    Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    state.getDataCommerceQR().getPaymentData().getDate(),
                    state.getDataCommerceQR().getPaymentData().getTrnId(),
                    state.getDataCommerceQR().getPaymentData().getOriginMoney(),
                    state.getDataCommerceQR().getPaymentData().getName(),
                    state.getDataCommerceQR().getPaymentData().getIpAddress(),
                    valorTransferencia,
                    state.getDataCommerceQR().getPaymentData().getStatus(),
                    state.getUsuario().getCedula(),
                    cuentaOrigen.getA_numdoc()
            ));
        }catch (Exception ex){
            showDataFetchError("Lo sentimos","");
        }
    }

    @Override
    public void showDialogPaymentLoading(){
        dialogPaymentLoading = new Dialog(context);
        dialogPaymentLoading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPaymentLoading.setContentView(R.layout.pop_up_nequi_transfer_loading);
        dialogPaymentLoading.setCancelable(true);
        dialogPaymentLoading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogPaymentLoading.getWindow().setGravity(Gravity.CENTER);
        TextView titleLoading = dialogPaymentLoading.findViewById(R.id.titleLoading);
        titleLoading.setText("Estamos trasladando tu dinero de Nequi a PRESENTE");
        dialogPaymentLoading.show();
    }

    @Override
    public void editTextDialogPaymentLoading(String text){
        TextView textLoading = dialogPaymentLoading.findViewById(R.id.textLoading);
        textLoading.setText("Estamos trasladando tu dinero de Nequi a PRESENTE");
    }

    @Override
    public void hideDialogPaymentLoading(){
        dialogPaymentLoading.dismiss();
    }

    @Override
    public void showContentQrDetail(){
        contentNequiQR.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideContentQrDetail(){
        contentNequiQR.setVisibility(View.GONE);
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
        contentNequiQR.setVisibility(View.GONE);
        layoutCircularProgressBar.setVisibility(View.VISIBLE);
        circularProgressBar.setVisibility(View.GONE);
        textCircularProgressBar.setText("Ha ocurrido un error, inténtalo de nuevo ");
        buttonReferesh.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDialogError(String title, String message){
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
                baseView.finish();
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
                baseView.finish();
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
