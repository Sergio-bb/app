package solidappservice.cm.com.presenteapp.front.tranferencias.FragmentMakeTransfer;

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
import android.widget.AdapterView;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.pagoobligaciones.ProductoSpinnerAdapter;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.entities.transferencias.request.RequestMakeTransfer;
import solidappservice.cm.com.presenteapp.entities.transferencias.response.ResponseCuentasInscritas;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.tools.helpers.DialogHelpers;
import solidappservice.cm.com.presenteapp.tools.helpers.NumberTextWatcher;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 03/11/2016.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 11/09/2021.
 */
public class FragmentMakeTransferView extends Fragment implements FragmentMakeTransferContract.View {

    private FragmentMakeTransferPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
//    private ProgressDialog pd;
    private Dialog pd;
    private FirebaseAnalytics firebaseAnalytics;
    private List<ResponseProducto> cuentasPropias;

    @BindView(R.id.spCuentaOrigen)
    Spinner spCuentaOrigen;
    @BindView(R.id.spCuentaDestino)
    Spinner spCuentaDestino;
    @BindView(R.id.txtValorTransferencia)
    EditText txtValorTransferencia;
    @BindView(R.id.button_transferir)
    Button buttonMakeTransfer;
    @BindView(R.id.txtAvaliable)
    TextView txtAvaliable;
    @BindView(R.id.txtAvaliableSaldo)
    TextView txtAvaliableSaldo;
    @BindView(R.id.tv_cuenta_destino)
    TextView tv_cuenta_destino;
    @BindView(R.id.tv_valor_transferencia)
    TextView tv_valor_transferencia;
    @BindView(R.id.lbl_inscribir_cuenta)
    TextView lblInscribirCuenta;
    @BindView(R.id.lbl_borrar_cuenta_inscrita)
    TextView lblborrarCuenta;
    @BindView(R.id.txt3)
    TextView txt3;

    @BindView(R.id.contentMakeTransfer)
    ScrollView contentMakeTransfer;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de transferencia");
        firebaseAnalytics.logEvent("pantalla_transferencia", params);
        View view = inflater.inflate(R.layout.fragment_transfers_maketransfer, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentMakeTransferPresenter(this, new FragmentMakeTransferModel());
        context = (ActivityTabsView) getActivity();
        assert context != null;
        state = context.getState();

        spCuentaDestino.setEnabled(false);
        String vlrMinimo = "Valor mínimo a transferir: $5.000 ";
        String vlrDisponible = "Valor disponible: ";
        String vlrCero = "$0.00";
        if (state.getUsuario() != null){
            txt3.setText(vlrMinimo);
        }else {
            context.onBackPressed();
        }
        txtAvaliable.setText(vlrDisponible);
        txtAvaliableSaldo.setText(vlrCero);
        txtValorTransferencia.addTextChangedListener(new NumberTextWatcher(txtValorTransferencia));
        fetchIncompleteTransfers();
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null || state.getUsuario() == null){
            context.salir();
        }
    }

    @OnItemSelected(R.id.spCuentaOrigen)
    public void onItemSelectedOriginAccount(AdapterView<?> parent, View view, int position, long id){
        String vlrDisponible = "Valor disponible: ";
        String vlrCero = "$0.00";
        if (position != 0){
            double tope = this.cuentasPropias.get(position).getV_transf();
            tv_cuenta_destino.setTextColor(Color.parseColor("#444444"));
            txtAvaliable.setText(vlrDisponible);
            txtAvaliableSaldo.setText(context.getMoneda(tope));
            spCuentaDestino.setEnabled(true);
        }else{
            tv_cuenta_destino.setTextColor(Color.parseColor("#cccccc"));
            spCuentaDestino.setSelection(0);
            spCuentaDestino.setEnabled(false);
            txtAvaliable.setText(vlrDisponible);
            txtAvaliableSaldo.setText(vlrCero);
        }
    }

    @OnItemSelected(R.id.spCuentaDestino)
    public void onItemSelectedDestinationAccount(AdapterView<?> parent, View view, int position, long id){
        if(position > 0){
            txtValorTransferencia.setEnabled(true);
            tv_valor_transferencia.setTextColor(Color.parseColor("#000000"));
        } else {
            tv_valor_transferencia.setTextColor(Color.parseColor("#cccccc"));
            txtValorTransferencia.setText("");
            txtValorTransferencia.setEnabled(false);
        }
    }

    @OnTextChanged(R.id.txtValorTransferencia)
    public void afterTextChanged(Editable arg0) {
        enabledMakeTransferButton();
    }

    @OnClick(R.id.button_transferir)
    public void onClickMakeTransfer(){
        validateDataTransfer();
    }

    @OnClick(R.id.lbl_inscribir_cuenta)
    public void onClicRegisterAccount(){
        state.getmTabHost().setCurrentTab(18);
    }

    @OnClick(R.id.lbl_borrar_cuenta_inscrita)
    public void onClickDeleteAccount(){
        state.getmTabHost().setCurrentTab(19);
    }

    @OnClick(R.id.imageReferesh)
    public void onClickRefresh(){
        fetchIncompleteTransfers();
    }

    @Override
    public void fetchIncompleteTransfers(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchIncompleteTransfers(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void showResultIncompleteTransfers(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_error);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView titleMessage = (TextView) dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText("No es posible realizar la transferencia");
        TextView contentMessage = (TextView) dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText("Tienes una transferencia en proceso, inténtalo de nuevo más tarde.");
        ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_23_NEQUI_MENU_SEND_MONEY_TAG);
            }
        });
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
            showDialogError("Lo sentimos", "");
            showErrorWithRefresh();
        }
    }

    @Override
    public void showRegisteredAccounts(List<ResponseCuentasInscritas> cuentasInscritas){
        try {
            if(cuentasInscritas != null && cuentasInscritas.size()>0){
                ResponseCuentasInscritas first = new ResponseCuentasInscritas();
                first.setNnasocia("");
                first.setN_numcta("Seleccionar un producto");
                cuentasInscritas.add(0, first);
                if (spCuentaDestino != null) {
                    ArrayAdapter<ResponseCuentasInscritas> adapter = new ArrayAdapter<ResponseCuentasInscritas>(context, R.layout.list_item_spinner, cuentasInscritas);
                    spCuentaDestino.setAdapter(adapter);
                }
            }else{
                showDataFetchError("Lo sentimos", "No tienes cuentas inscritas.");
            }
        } catch (Exception ex) {
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void fetchAccounts(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchAccounts(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void showAccounts(List<ResponseProducto> cuentasPropias){
        try {
            if (spCuentaOrigen != null) {
                List<ResponseProducto> cuentasPropiasFinal = new ArrayList<>();
                ResponseProducto first = new ResponseProducto();
                first.setN_tipodr("Seleccionar un producto");
                first.setN_produc("Seleccionar un producto");
                first.setA_numdoc("");
                cuentasPropiasFinal.add(first);
                for (ResponseProducto ec : cuentasPropias) {
                    if (ec.getI_debito() != null
                            && ec.getI_debito().equals("Y")) {
                        cuentasPropiasFinal.add(ec);
                    }
                }
                ProductoSpinnerAdapter adapter = new ProductoSpinnerAdapter(context, cuentasPropiasFinal, true);
                spCuentaOrigen.setAdapter(adapter);
                this.cuentasPropias = cuentasPropiasFinal;
            }
        } catch (Exception ex) {
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void validateDataTransfer(){
        try {
            Object _cuentaOrigen = spCuentaOrigen.getSelectedItem();
            Object _cuentaDestino = spCuentaDestino.getSelectedItem();

            ResponseProducto cuentaOrigen = null;
            ResponseCuentasInscritas cuentaDestino = null;

            if (_cuentaDestino != null && _cuentaOrigen != null) {
                cuentaOrigen = (ResponseProducto) _cuentaOrigen;
                cuentaDestino = (ResponseCuentasInscritas) _cuentaDestino;
            }

            if (cuentaOrigen == null || cuentaDestino == null
                    || cuentaOrigen.getN_tipodr().equals("")
                    || cuentaDestino.getNnasocia().equals("")) {
                showDialogError("Datos incompletos", context.getResources().getString(R.string.error_cuenta_origen));
                return;
            }

            double valorTransferencia;
            if (txtValorTransferencia.getText() == null
                    || TextUtils.isEmpty(txtValorTransferencia.getText().toString())) {
                showDialogError("Datos incompletos", context.getResources().getString(R.string.error_valor_transfer));
                return;
            } else {
                try {
                    String cleanString = txtValorTransferencia.getText().toString().replaceAll("[$,.]", "");
                    valorTransferencia = Double.parseDouble(cleanString);
                } catch (Exception e) {
                    showDialogError("Datos incompletos", context.getResources().getString(R.string.error_valor_transfer));
                    return;
                }
            }

            if (valorTransferencia <= 0) {
                showDialogError("Datos incompletos", context.getResources().getString(R.string.error_valor_transfer));
                return;
            }

            if (valorTransferencia < 5000) {
                showDialogError("Error de transferencia", context.getResources().getString(R.string.error_valor_min_transfer));
                return;
            }

            double v_transf = cuentaOrigen.getV_transf();
            if (valorTransferencia > v_transf){
                showDialogError("Error de transferencia", context.getResources().getString(R.string.alerta_valor_disponible) + ": " + context.getMoneda(cuentaOrigen.getV_transf()));
                return;
            }

            final double _valorTransferencia = valorTransferencia;
            final ResponseProducto auxcuentaOrigen = cuentaOrigen;
            final ResponseCuentasInscritas auxcuentaDestino = cuentaDestino;
            showDialogConfirmTransfer(_valorTransferencia, auxcuentaOrigen, auxcuentaDestino);

        } catch (Exception ex) {
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void showDialogConfirmTransfer(Double valorTransferencia, ResponseProducto cuentaOrigen, ResponseCuentasInscritas cuentaDestino){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_confirm);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView titleMessage = (TextView) dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText("¿Confirma tu solicitud?");
        TextView contentMessage = (TextView) dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText("Tu transferencia será por un valor de " + context.getMoneda(valorTransferencia));
        ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.buttonClose);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button buttonAceptar = (Button) dialog.findViewById(R.id.btnAceptar);
        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                makeTransfer(valorTransferencia, cuentaOrigen, cuentaDestino);
            }
        });
        dialog.show();
    }

    @Override
    public void makeTransfer(Double valorTransferencia, ResponseProducto cuentaOrigen, ResponseCuentasInscritas cuentaDestino){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.makeTransfer(new RequestMakeTransfer(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    context.obtenerIdDispositivo(),
                    cuentaDestino.getAanumnit_o(),
                    cuentaOrigen.getA_numdoc(),
                    valorTransferencia,
                    cuentaDestino.getK_idterc(),
                    cuentaDestino.getK_idterc_tit(),
                    cuentaDestino.getN_numcta(),
                    new Date().getTime()
            ));
        }catch (Exception ex){
            enabledMakeTransferButton();
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void showResultTransfer(String resultMessage){
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.pop_up_success);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView titleMessage = (TextView) dialog.findViewById(R.id.titleSuccess);
            titleMessage.setText("Solicitud Enviada");
            TextView contentMessage = (TextView) dialog.findViewById(R.id.contentSuccess);
            contentMessage.setText(resultMessage);
            ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.buttonClose);
            buttonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_23_NEQUI_MENU_SEND_MONEY_TAG);
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void disabledMakeTransferButton() {
        buttonMakeTransfer.setEnabled(false);
    }

    @Override
    public void enabledMakeTransferButton(){
        buttonMakeTransfer.setEnabled(true);
    }

    @Override
    public void showSectionMakeTransfer() {
        contentMakeTransfer.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideSectionMakeTransfer(){
        contentMakeTransfer.setVisibility(View.GONE);
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
        contentMakeTransfer.setVisibility(View.GONE);
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
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_23_NEQUI_MENU_SEND_MONEY_TAG);
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
