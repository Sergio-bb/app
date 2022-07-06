package solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.movilexito.FragmentTransactionSummary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EmptyStackException;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.convenios.BolisllosMESpinnerAdapter;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseConsultarDatosAsociado;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.dto.ProductoDebitable;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProductosV2;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestAccessToken;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestActualizarRecarga;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestEmail;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestRealizarPago;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestResumenTransaccion;
import solidappservice.cm.com.presenteapp.entities.movilexito.response.ResponseAccessTokenOAuthME;
import solidappservice.cm.com.presenteapp.entities.movilexito.response.ResponseRealizarPago;
import solidappservice.cm.com.presenteapp.entities.movilexito.response.ResponseResumenTransaccion;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestRealizarRecargar;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseParametrosAPP;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.ActivityAgreementsView;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

public class FragmentTransactionSummaryView extends Fragment implements FragmentTransactionSummaryContract.View {

    private FragmentTransactionSummaryPresenter presenter;
    private ActivityAgreementsView context;
    private ActivityBase activityBase;
    private GlobalState state;
    public ProgressDialog pd;

    private List<ResponseProductosV2> cuentas;
    private List<ProductoDebitable> objetoCuentasDebitables;
    private ProductoDebitable productoSeleccionado;
    private RequestRealizarRecargar requestRecarga;
    private ResponseResumenTransaccion resumenTransaccion;
    private ResponseParametrosAPP paramsBasicToken;
    private String tipoDocumento;
    private String resultRechargeME;

    private ResponseConsultarDatosAsociado datosAsociado;
    private ResponseAccessTokenOAuthME accessTokenOAuthME;
    private ResponseRealizarPago resultadoPago;

    private Dialog dialogTransferLoading;

    //--Parametrizables
    private String tipoSuscripcion = "05"; //05 pruebas - diferente produccion
    private String canalVenta = "02"; //02 pruebas - diferente produccion
    private String idAplicacion = "1807";

    @BindView(R.id.circular_progress_bar)
    ProgressBar circularProgressBar;
    @BindView(R.id.text_circular_progress_Bar)
    TextView textCircularProgressBar;
    @BindView(R.id.layout_circular_progress_bar)
    LinearLayout layoutCircularProgressBar;
    @BindView(R.id.contentTransactionSummary)
    RelativeLayout contentTransactionSummary;

    @BindView(R.id.btnContinuar)
    Button btnRealizarPago;
    @BindView(R.id.btnCancelarCompra)
    Button btnCancelarCompra;
    @BindView(R.id.checkboxAceptarTyC)
    CheckBox checkBoxAceptarTyC;
    @BindView(R.id.lyRecurrente)
    LinearLayout lyRecurrente;

    @BindView(R.id.spnrBolsillos)
    Spinner spinnerBolsillos;

    @BindView(R.id.txtPrecioPaquete)
    TextView txtPrecioPaquete;
    @BindView(R.id.txtPlu)
    TextView txtPlu;
    @BindView(R.id.detallePaquete)
    TextView txtdetallePaquete;
    @BindView(R.id.vigenciaPaquete)
    TextView txtvigenciaPaquete;
    @BindView(R.id.txtCorreo)
    TextView txtCorreo;
    @BindView(R.id.txtLineas)
    TextView txtLineas;
    @BindView(R.id.txtFormaPago)
    TextView txtFormaPago;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agreements_me_transactionsummary, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentTransactionSummaryPresenter(this, new FragmentTransactionSummaryModel());
        context = (ActivityAgreementsView) getActivity();
        activityBase = (ActivityBase) getActivity();
        state = context.getState();
        pd = new ProgressDialog(context);
        datosAsociado = state.getDatosAsociado();
        objetoCuentasDebitables = new ArrayList<>();

        fetchDataBasicTokenPresenteME();
    }

    @OnClick(R.id.btnCancelarCompra)
    public void onClickCancelar() {
        context.setFragment(IFragmentCoordinator.Pantalla.ConveniosMenuPrincipal);
    }

    @OnClick(R.id.btnContinuar)
    public void onClickContinuar() {
        validateDataPayment();
    }

    @OnCheckedChanged(R.id.checkboxAceptarTyC)
    public void onCheckedChangedTerminos(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            btnRealizarPago.setEnabled(true);
            btnRealizarPago.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1BC44B")));
        } else {
            btnRealizarPago.setEnabled(false);
            btnRealizarPago.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D7D7D7")));
        }
    }

    @Override
    public void fetchDocumentType() {
        try {
            presenter.fetchDocumentType(new BaseRequest(Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()));
        } catch (Exception ex) {
            hideCircularProgressBar();
            showDialogPaymentError("");
        }
    }

    @Override
    public void resultDocumentType(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @Override
    public void fetchDataBasicTokenPresenteME() {
        try {
            presenter.getDataBasicTokenPresenteME();
        } catch (Exception ex) {
            hideCircularProgressBar();
            showDialogPaymentError("");
        }
    }

    @Override
    public void resultDataBasicTokenPresenteME(ResponseParametrosAPP params) {
        this.paramsBasicToken = params;
    }

    @Override
    public void fetchSummaryTransaction() {
        try {
            presenter.fetchSummaryTransaction(
                    new RequestResumenTransaccion(
                            Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
                            state.getUsuario().getToken(),
                            state.getIdTransaccionPresente()
                    ),
                    new RequestAccessToken(
                            paramsBasicToken.getValue1().split(";")[0],
                            paramsBasicToken.getValue1().split(";")[1],
                            "client_credentials",
                            ""
                    )
            );
        } catch (Exception ex) {
            hideCircularProgressBar();
            showDialogPaymentError("");
        }
    }

    @Override
    public void showSummaryTransaction(ResponseResumenTransaccion resumen) {
        this.resumenTransaccion = resumen;
        txtPrecioPaquete.setText(String.format("Paquete: %s", context.getMonedaWithOutDecimals(resumen.getValorPaquete())));
        txtPlu.setText(String.format("PLU: %s", resumen.getIdPaquete()));
        txtdetallePaquete.setText(resumen.getDetallePaquete());
        txtvigenciaPaquete.setText(resumen.getVigenciaPaquete());
        txtFormaPago.setText(resumen.getTipoRecarga());
        txtLineas.setText(resumen.getLineasARecargar());
        txtCorreo.setText(datosAsociado.getEmail());
    }

    @Override
    public void fetchAccounts() {
        try {
            presenter.fetchAccounts(new BaseRequest(
                    Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        } catch (Exception ex) {
            hideCircularProgressBar();
            showDialogPaymentError("");
        }
    }

    @Override
    public void showAccounts(List<ResponseProductosV2> cuentas) {
        try {
            if (cuentas != null && cuentas.size() > 0 && spinnerBolsillos != null) {
                List<ProductoDebitable> cuentasDebitables = new ArrayList<>();
                for (ResponseProductosV2 rp : cuentas) {
                    for (ResponseProductosV2.ResponseDetalleProductos dp : rp.getProductos()) {
                        if (dp.getProductoDebitable() != null
                                && dp.getProductoDebitable().equals("Y")) {
                            cuentasDebitables.add(new ProductoDebitable(
                                    rp.getNombreCuenta(),
                                    Encripcion.getInstance().desencriptar(dp.getNumeroProducto()),
                                    dp.getNombreProducto(),
                                    dp.getSaldoProducto()
                            ));
                        }
                    }
                }
                objetoCuentasDebitables = cuentasDebitables;
                BolisllosMESpinnerAdapter adapter = new BolisllosMESpinnerAdapter(context, cuentasDebitables, true);
                spinnerBolsillos.setAdapter(adapter);
                this.cuentas = cuentas;
            } else {
                hideCircularProgressBar();
                showDialogPaymentError("");
            }
        } catch (Exception ex) {
            hideCircularProgressBar();
            showDialogPaymentError("");
        }
    }

    @Override
    public void validateDataPayment() {
        try {
            if (spinnerBolsillos != null) {
                productoSeleccionado = (ProductoDebitable) spinnerBolsillos.getSelectedItem();
            }

            if (!checkBoxAceptarTyC.isChecked()) {
                showDataFetchError("Debes aceptar los términos y condiciones");
            }

            if (productoSeleccionado == null || productoSeleccionado.getNombreCuenta() == null || productoSeleccionado.getNombreCuenta().equals("")) {
                BolisllosMESpinnerAdapter adapter = (BolisllosMESpinnerAdapter) spinnerBolsillos.getAdapter();
                View view = spinnerBolsillos.getSelectedView();
                adapter.setError(view, "Selecciona tu bolsillo");
                TextView errorText = (TextView) spinnerBolsillos.getSelectedView();
                return;
            }

            if (resumenTransaccion != null && resumenTransaccion.getValorFinal() > productoSeleccionado.getSaldo()) {
                BolisllosMESpinnerAdapter adapter = (BolisllosMESpinnerAdapter) spinnerBolsillos.getAdapter();
                View view = spinnerBolsillos.getSelectedView();
                adapter.setError(view, "No tienes suficiente saldo en tu cuenta");
                TextView errorText = (TextView) spinnerBolsillos.getSelectedView();
                return;
            }
            showDialogConfirmPayment();
        } catch (Exception ex) {
            hideDialogPaymentLoading();
            showDialogPaymentError("");
        }
    }

    @Override
    public void showDialogConfirmPayment() {
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle(context.getResources().getString(R.string.app_name));
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage("Tu pago será por un valor de " + context.getMonedaWithOutDecimals(resumenTransaccion.getValorFinal()) + " ¿Deseas continuar?");
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    makePayment();
                } catch (Exception e) {
                    hideDialogPaymentLoading();
                    showDialogPaymentError("");
                }
            }
        });
        d.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        d.show();
    }

    @Override
    public void makePayment() {
        try {
            presenter.makePayment(
                    new RequestRealizarPago(
                            Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
                            state.getUsuario().getToken(),
                            resumenTransaccion.getIdPaquete(),
                            state.getIdTransaccionPresente(),
                            String.valueOf(resumenTransaccion.getIdTransaccioME()),
                            resumenTransaccion.getLineasARecargar(),
                            productoSeleccionado.getNumeroProducto(),
                            resumenTransaccion.getValorFinal(),
                            "APP"
                    ),
                    new RequestAccessToken(
                            paramsBasicToken.getValue1().split(";")[0],
                            paramsBasicToken.getValue1().split(";")[1],
                            "client_credentials",
                            ""
                    )
            );
        } catch (Exception ex) {
            hideDialogPaymentLoading();
            showDialogPaymentError("");
        }
    }

    @Override
    public void resultMakePayment(ResponseRealizarPago resultPayment) {
        this.resultadoPago = resultPayment;
    }


    @Override
    public void performLineRechargeME() {
        try {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(Calendar.getInstance().getTime());
            String contentTotalBolsillos = "";
            int contador = 1;
            for (ProductoDebitable rp : objetoCuentasDebitables) {
                String bolsilloSeleccionado = "N";
                if (productoSeleccionado.getNombreProducto().equals(rp.getNombreProducto())) {
                    bolsilloSeleccionado = "Y";
                }
                contentTotalBolsillos = contentTotalBolsillos + "{\"nombreBolsillo\":\"" + rp.getNombreProducto() + "\",\"numeroCuenta\":\"" + rp.getNumeroProducto() + "\",\"bolsilloSeleccionado\":\"" + bolsilloSeleccionado + "\"}";
                if (objetoCuentasDebitables.size() != contador) {
                    contentTotalBolsillos = contentTotalBolsillos + ",";
                }
                contador++;
            }
            String cadenaBolsillos = "\",\"bolsillos\":" + "[" + contentTotalBolsillos + "]";
            presenter.performLineRechargeME(requestRecarga =
                            new RequestRealizarRecargar(
                                    UUID.randomUUID().toString(),
                                    state.getIdTransaccionPresente(),
                                    state.getParamsMovilExito().getValue3().split(";")[0],
                                    "{\"idTransaccionPresente\":\"" + state.getIdTransaccionPresente() + "\",\"ValorPaquete\":\"" + resumenTransaccion.getValorPaquete() + "\",\"ValorFinalCompra\":\"" + resumenTransaccion.getValorFinal() + cadenaBolsillos + "}",
                                    resumenTransaccion.getIdPaquete(),
                                    state.getParamsMovilExito().getValue3().split(";")[1],
                                    "",
                                    resumenTransaccion.getLineasARecargar(),
                                    resumenTransaccion.getTipoRecarga() != null && resumenTransaccion.getTipoRecarga().equals("Y"),
                                    tipoDocumento,
                                    state.getUsuario().getCedula(),
                                    timeStamp
                            ),
                    new RequestAccessToken(
                            state.getParamsMovilExito().getValue2().split(";")[0],
                            state.getParamsMovilExito().getValue2().split(";")[1],
                            "client_credentials",
                            state.getParamsMovilExito().getValue2().split(";")[2]
                    ),
                    NetworkHelper.CERTIFICATE_BAN == 1 ? context.getResources().openRawResource(R.raw.movilexitopruebas2021) : context.getResources().openRawResource(R.raw.certificado_prd),// parametrizacion del certificado - mejorar esta accion
                    state.getParamsMovilExito().getValue3().split(";")[2]
            );
        } catch (Exception ex) {
            hideDialogPaymentLoading();
            showDialogPaymentError("");
        }
    }

    @Override
    public void updateResultRechargeME(String resultME) {
        try {
            resultRechargeME = resultME;
            presenter.updateResultRechargeME(
                    new RequestActualizarRecarga(
                            state.getIdTransaccionPresente(),
                            resultME
                    ),
                    new RequestAccessToken(
                            paramsBasicToken.getValue1().split(";")[0],
                            paramsBasicToken.getValue1().split(";")[1],
                            "client_credentials",
                            ""
                    )
            );
        } catch (Exception ex) {
            hideDialogPaymentLoading();
            showDialogPaymentError("");
        }
    }

    @Override
    public void showDialogPaymentLoading() {
        dialogTransferLoading = new Dialog(context);
        dialogTransferLoading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogTransferLoading.setContentView(R.layout.pop_up_loading);
        dialogTransferLoading.setCancelable(false);
        dialogTransferLoading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogTransferLoading.getWindow().setGravity(Gravity.CENTER);
        TextView title = dialogTransferLoading.findViewById(R.id.titleLoading);
        title.setText("Estamos procesando tu pago");
        TextView body = dialogTransferLoading.findViewById(R.id.textLoading);
        body.setText("En unos instantes podrás disfrutar de tu paquete");
        dialogTransferLoading.show();
    }

    @Override
    public void hideDialogPaymentLoading() {
        dialogTransferLoading.dismiss();
    }

    @Override
    public boolean isShowingDialogPaymentLoading() {
        return dialogTransferLoading.isShowing();
    }

    @Override
    public void showDialogPaymentSuccess() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_success);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        TextView titleSuccess = dialog.findViewById(R.id.titleSuccess);
        titleSuccess.setText("Compra exitosa");
        TextView textSuccess = dialog.findViewById(R.id.textSuccess);
        textSuccess.setText("Tu transacción ha sido exitosa, en los próximos minutos recibirás un mensaje de texto confirmando la recarga.");
        ImageButton buttonClose = dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.setFragment(IFragmentCoordinator.Pantalla.ConveniosMenuPrincipal);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void showDialogPaymentError(String messageError) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_error);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        ImageButton buttonClose = dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.setFragment(IFragmentCoordinator.Pantalla.ConveniosMenuPrincipal);
                dialog.dismiss();
            }
        });
        TextView textError = dialog.findViewById(R.id.textError);
        textError.setText(!TextUtils.isEmpty(messageError) ? messageError : "No hemos logrado procesar tu pago, intenta de nuevo en unos instantes");
        dialog.show();
    }

    @Override
    public void showContentTransactionSummary() {
        contentTransactionSummary.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideContentTransactionSummary() {
        contentTransactionSummary.setVisibility(View.GONE);
    }

    @Override
    public void sendEmailFailedTransaction() {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(Calendar.getInstance().getTime());
        String operacion = getResources().getString(R.string.txt_mensaje_recargar);
        if (resumenTransaccion.getTipoRecarga() != null && resumenTransaccion.getTipoRecarga().equals("Y")) {
            operacion = getResources().getString(R.string.txt_mensaje_suscribir);
        }

        String body = "Datos de la Solicitud : {\n" +
                "    \"transaccionId\": \"" + requestRecarga.getTransaccionId() + "\",\n" +
                "    \"id\": \"" + requestRecarga.getId() + "\",   \n" +
                "     \"tipoSuscripcion\": \"" + requestRecarga.getTipoSuscripcion() + "\",     \n" +
                "     \"adicionalesSuscripcion\": " + requestRecarga.getAdicionalesSuscripcion() + ",\n" +
                "     \"plu\" : \"" + requestRecarga.getPlu() + "\",\n" +
                "     \"canalVenta\": \"" + requestRecarga.getCanalVenta() + "\",\n" +
                "     \"idSuscriptionOptiva\": \"\",\n" +
                "     \"msisdn\": \"" + requestRecarga.getMsisdn() + "\",\n" +
                "     \"esRecurrente\": " + requestRecarga.getEsRecurrente() + ",\n" +
                "     \"tipoDocumento\": \"" + requestRecarga.getTipoDocumento() + "\",\n" +
                "     \"documento\":\"" + requestRecarga.getDocumento() + "\",\n" +
                "     \"FechaCompra\": \"" + requestRecarga.getFechaCompra() + "\" }";


        String contenidoEmail = getResources().getString(R.string.txt_intro_email) + "\n\n" + getResources().getString(R.string.txt_contenido_email) + "\n\n" + getResources().getString(R.string.txt_contenidoFecha_email) + " " + timeStamp + "\n" + getResources().getString(R.string.txt_contenidoOperacion_email) + " " + operacion + "\n" +
                body + "\n" + getResources().getString(R.string.txt_contenidoError_email) + " " + resultRechargeME + ". " + "\n\n" + getResources().getString(R.string.txt_cadenafin_email);

        presenter.sendEmailFailedTransaction(
                new RequestEmail(
                        idAplicacion,
                        getResources().getString(R.string.txt_asunto_email),
                        false,
                        contenidoEmail
                ),
                new RequestAccessToken(
                        paramsBasicToken.getValue1().split(";")[0],
                        paramsBasicToken.getValue1().split(";")[1],
                        "client_credentials",
                        ""
                )
        );
    }

    @Override
    public void resultEmailFailedTransaction(String resultadoEmail) {
        String resutEmail = resultadoEmail;
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
    public void showErrorTimeOut() {
        String message = "Ha ocurrido un error. Intenta de nuevo y si el error persiste, contacta a PRESENTE.";
        if (state != null && state.getMensajesRespuesta() != null && state.getMensajesRespuesta().size() > 0) {
            for (ResponseMensajesRespuesta rm : state.getMensajesRespuesta()) {
                if (rm.getIdMensaje() == 6) {
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
                dialog.dismiss();
                context.finish();
            }
        });
        d.show();
    }

    @Override
    public void showDataFetchError(String message) {
        if (TextUtils.isEmpty(message)) {
            message = "Ha ocurrido un error. Intenta de nuevo y si el error persiste, contacta a PRESENTE.";
            if (state != null && state.getMensajesRespuesta() != null && state.getMensajesRespuesta().size() > 0) {
                for (ResponseMensajesRespuesta rm : state.getMensajesRespuesta()) {
                    if (rm.getIdMensaje() == 7) {
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
                dialog.dismiss();
//                state.getmTabHost().setCurrentTab(2);
                context.finish();
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

    //            presenter.performLineRechargeME(
//                    new RequestRealizarRecargar(
//                            UUID.randomUUID().toString(),
//                            state.getIdTransaccionPresente(),
//                            state.getParamsMovilExito().getValue3().split(";")[0],
//                            "{\"NumeroCuenta\":\"" + productoSeleccionado.getNumeroProducto() + "\",\"ValorPaquete\":\"" + resumenTransaccion.getValorPaquete() + "\",\"ValorFinalCompra\":\"" + resumenTransaccion.getValorFinal() + "\"}",
//                            resumenTransaccion.getIdPaquete(),
//                            state.getParamsMovilExito().getValue3().split(";")[1],
//                            "",
//                            resumenTransaccion.getLineasARecargar(),
//                            resumenTransaccion.getTipoRecarga() != null && resumenTransaccion.getTipoRecarga().equals("Y"),
//                            tipoDocumento,
//                            state.getUsuario().getCedula(),
//                            timeStamp
//                    ),
//                    new RequestAccessToken(
//                            state.getParamsMovilExito().getValue2().split(";")[0],
//                            state.getParamsMovilExito().getValue2().split(";")[1],
//                            "client_credentials",
//                            state.getParamsMovilExito().getValue2().split(";")[2]
//                    ),
//                    context.getResources().openRawResource(R.raw.movilexitopruebas2021)
//            );

}