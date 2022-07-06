package solidappservice.cm.com.presenteapp.front.solicitudahorros.FragmentSavingsSolicity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.entities.solicitudahorros.response.ResponseTiposAhorro;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.login.Response.Usuario;
import solidappservice.cm.com.presenteapp.entities.solicitudahorros.request.RequestEnviarSolicitudAhorro;
import solidappservice.cm.com.presenteapp.front.solicitudahorros.ActivityProductRequestDate.ActivityProductRequestDateView;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 25/11/2015
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS 16/09/2021.
 **/
public class FragmentSavingsSolicityView extends Fragment implements FragmentSavingsSolicityContract.View {

    private FragmentSavingsSolicityPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private ProgressDialog pd;
    private FirebaseAnalytics firebaseAnalytics;
    private ResponseTiposAhorro tipo;
    private List<ResponseTiposAhorro> tipos;
    int acutal=0;
    int anterior=0;

    private final static int SELCCIONAR_FECHA_PAGO = 1000;

    @BindView(R.id.spinnerTipoProd)
    Spinner spinnerTipoProd;
    @BindView(R.id.spinnerPlazo)
    Spinner spinnerPlazo;
    @BindView(R.id.txtValorCuota)
    EditText txtValorCuota;
    @BindView(R.id.txtFecha)
    TextView txtFecha;
    @BindView(R.id.spinnerDescripcion)
    Spinner spinnerDescripcion;
    @BindView(R.id.lblDescripcion)
    TextView lblDescripcion;
    @BindView(R.id.lblPlazo)
    TextView lblPlazo;
    @BindView(R.id.lblFecha)
    TextView lblFecha;
    @BindView(R.id.btnEnviarSolicitud)
    Button btnEnviarSolicitud;
    @BindView(R.id.FragmentAhorros)
    LinearLayout frmFragmentAhorros;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de solicitud ahorros");
        firebaseAnalytics.logEvent("pantalla_solicitud_ahorros", params);
        View view = inflater.inflate(R.layout.fragment_savingssolicity, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentSavingsSolicityPresenter(this, new FragmentSavingsSolicityModel());
        context = (ActivityBase) getActivity();
        state = context.getState();
        pd = new ProgressDialog(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (state.getFechaSeleccionada() == null || TextUtils.isEmpty(state.getFechaSeleccionada())) {
            List<ResponseTiposAhorro> tiposAhorros = state.getTiposAhorros();
            if (tiposAhorros != null && tiposAhorros.size() > 0) {
//                cargarDatosAhorro(tiposAhorros);
                showTypesOfSavings(tiposAhorros);
            } else {
                if (state == null || state.getUsuario() == null) {
                    context.salir();
                } else {
                    Usuario usuario = state.getUsuario();
//                    new DatosTask().execute(usuario.cedula, usuario.token);
                    fetchTypesOfSavings();
                }
            }
        } else {
            txtFecha.setText(state.getFechaSeleccionada());
        }
    }

    @OnClick(R.id.btnEnviarSolicitud)
    public void onClickEnviarSolicitud(View v) {
        //enviarSolicitud();
        confirmSolicitySaving();
    }

    @OnClick(R.id.txtFecha)
    public void onClickFecha(View v) {
        Intent intent = new Intent(getContext(), ActivityProductRequestDateView.class);
        startActivityForResult(intent, SELCCIONAR_FECHA_PAGO);
    }

    @OnTextChanged(R.id.txtValorCuota)
    public void onTextChangedValorCuota(CharSequence s, int start, int before, int count) {
        String textToEdit = txtValorCuota.getText().toString();
        textToEdit = textToEdit.replace(",", "");
        textToEdit = textToEdit.replace(".", "");
        acutal = textToEdit.length();
        if(acutal!=anterior){
            anterior = acutal;
            DecimalFormat formato = new DecimalFormat("#,###");
            textToEdit = !TextUtils.isEmpty(textToEdit) ? formato.format(Double.parseDouble(textToEdit)) : "";
            txtValorCuota.setText(textToEdit);
            txtValorCuota.setSelection(textToEdit.length());
        }
    }

    @OnFocusChange(R.id.txtFecha)
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            //Intent intent = new Intent(context, ActivityFechaSolicitudProducto.class);
            //startActivityForResult(intent, SELCCIONAR_FECHA_PAGO);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater infalter = getActivity().getLayoutInflater();
            builder.setView(infalter.inflate(R.layout.activity_savingssolicity_dateproductssolicity, null));
            builder.create();
        }
    }

    @OnItemSelected(R.id.spinnerTipoProd)
    public void onItemSelectedTipoAhorro(AdapterView<?> parent, View view, int position, long id) {
        showOptionsTypesofSavings(position);
    }

    @Override
    public void fetchTypesOfSavings(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchTypesOfSavings(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void showTypesOfSavings(List<ResponseTiposAhorro> tiposAhorro) {
        this.tipos = tiposAhorro;
        state.setTiposAhorros(tiposAhorro);
        ArrayAdapter<ResponseTiposAhorro> adapter = new ArrayAdapter<>(context, R.layout.list_item_spinner, tipos);
        spinnerTipoProd.setAdapter(adapter);
    }

    @Override
    public void showOptionsTypesofSavings(int positionTypeofSavings){
        try {
            if (positionTypeofSavings > 0) {
                this.tipo = this.tipos.get(positionTypeofSavings);
                if (tipo.getF_vigenci_fin() == null || TextUtils.isEmpty(tipo.getF_vigenci_fin())) {
                    if (tipo.getI_fecha().equals("Y")) {
                        //PEDIR FECHA
                        txtFecha.setText("");
                        txtFecha.setEnabled(true);
                        txtFecha.setVisibility(View.VISIBLE);
                        lblFecha.setVisibility(View.VISIBLE);
                        spinnerPlazo.setVisibility(View.GONE);
                        lblPlazo.setVisibility(View.GONE);
                    } else {
                        //PLAZO
                        txtFecha.setVisibility(View.GONE);
                        lblFecha.setVisibility(View.GONE);
                        ArrayList<String> plazos = new ArrayList<>();
                        if (tipo.getV_plazo_min() == tipo.getV_plazo_max()) {
                            plazos.add((tipo.getV_plazo_min() > 1 ? tipo.getV_plazo_min() + " meses" : 1 + " mes"));
                        } else {
                            for (int i = tipo.getV_plazo_min(); i <= tipo.getV_plazo_max(); i++) {
                                plazos.add((i > 1 ? i + " meses" : 1 + " mes"));
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.list_item_spinner, plazos);
                        spinnerPlazo.setVisibility(View.VISIBLE);
                        lblPlazo.setVisibility(View.VISIBLE);
                        spinnerPlazo.setAdapter(adapter);
                        spinnerPlazo.setEnabled(!(tipo.getV_plazo_min() == tipo.getV_plazo_max()));
                    }
                } else {
                    txtFecha.setVisibility(View.VISIBLE);
                    lblFecha.setVisibility(View.VISIBLE);
                    spinnerPlazo.setVisibility(View.GONE);
                    lblPlazo.setVisibility(View.GONE);
                    txtFecha.setText(tipo.getF_vigenci_fin());
                    txtFecha.setEnabled(false);
                }

                //VALOR CUOTA
                if (tipo.getV_cuota_min() == tipo.getV_cuota_max()) {
                    txtValorCuota.setText(String.valueOf(tipo.getV_cuota_min()));
                } else {
                    txtValorCuota.setText("");
                }

                txtValorCuota.setEnabled(!(tipo.getV_cuota_min() == tipo.getV_plazo_max()));

                //DESCRIPCIONES
                if (tipo.getDescripciones() != null && tipo.getDescripciones().size() > 0) {
                    spinnerDescripcion.setVisibility(View.VISIBLE);
                    lblDescripcion.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> adapter_desc = new ArrayAdapter<>(context, R.layout.list_item_spinner, tipo.getDescripciones());
                    spinnerDescripcion.setAdapter(adapter_desc);
                } else {
                    spinnerDescripcion.setVisibility(View.GONE);
                    lblDescripcion.setVisibility(View.GONE);
                }
                txtValorCuota.setError(null);
                txtFecha.setError(null);
                frmFragmentAhorros.setBackgroundColor(00000);
            } else {
                cleanFields();
            }
        } catch (Exception e) {
            context.makeErrorDialog(e.getMessage());
        }
    }

    @Override
    public void cleanFields() {
        if (txtValorCuota.isEnabled()) {
            txtValorCuota.setText("");
        }
        if (txtFecha.isEnabled()) {
            txtFecha.setText("");
        }
    }

    @Override
    public void confirmSolicitySaving(){
        if (validateData()) {
            String _valorCuota = (txtValorCuota.getText().toString());
            AlertDialog.Builder d = new AlertDialog.Builder(context);
            d.setTitle(context.getResources().getString(R.string.app_name));
            d.setIcon(R.mipmap.icon_presente);
            d.setMessage("Tu ahorro será de $" +_valorCuota + " mensuales");
            d.setCancelable(false);
            _valorCuota = _valorCuota.replace(",", "");
            _valorCuota = _valorCuota.replace(".", "");
            final String final_valorCuota = _valorCuota;
            d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    solicitySaving();
                }
            });
            d.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            d.show();
        }
    }

    @Override
    public void solicitySaving() {
        try {
            if (state == null || state.getUsuario() == null) {
                context.salir();
                return;
            }
            String _valorCuota = txtValorCuota.getText().toString();
            _valorCuota = _valorCuota.replace(",", "");
            _valorCuota = _valorCuota.replace(".", "");
            Encripcion encripcion = Encripcion.getInstance();
            presenter.solicitySaving(new RequestEnviarSolicitudAhorro(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    _valorCuota,
                    tipo.getK_tipodr(),
                    spinnerPlazo.getVisibility() == View.VISIBLE && spinnerPlazo.getSelectedItem() != null ?
                            Integer.parseInt(spinnerPlazo.getSelectedItem().toString().split(" ")[0]) : 0,
                    txtFecha.getVisibility() == View.VISIBLE && txtFecha.getText() != null ?
                            txtFecha.getText().toString() : null,
                    spinnerDescripcion.getVisibility() == View.VISIBLE && spinnerDescripcion.getSelectedItem() != null ?
                            spinnerDescripcion.getSelectedItem().toString() : null,
                    context.obtenerIdDispositivo()
            ));
        } catch (Exception ex) {
            showDataFetchError("");
        }
    }

    @Override
    public boolean validateData() {
        Object tipoAhorro = spinnerTipoProd.getSelectedItem();
        if (tipoAhorro == null || TextUtils.isEmpty(tipoAhorro.toString())) {
            context.makeErrorDialog("Selecciona un tipo de ahorro");
            return false;
        }
        Object valorCuota = txtValorCuota.getText();
        if (valorCuota == null || TextUtils.isEmpty(valorCuota.toString())) {
            context.makeErrorDialog("Ingresa el valor de la cuota");
            txtValorCuota.setError("Ingrese un valor valido");
            return false;
        }
        double _valorCuota;
        valorCuota = valorCuota.toString().replace(",", "");
        valorCuota = valorCuota.toString().replace(".", "");
        try {
            _valorCuota = Double.parseDouble(valorCuota.toString());
        } catch (Exception ex) {
            context.makeErrorDialog("Ingresa el valor de la cuota");
            txtValorCuota.setError("Ingrese un valor valido");
            return false;
        }
        if (_valorCuota <= 0) {
            context.makeErrorDialog("Ingresa el valor de la cuota");
            txtValorCuota.setError("Ingrese un valor valido");
            return false;
        }
        ResponseTiposAhorro t = (ResponseTiposAhorro) tipoAhorro;
        if (_valorCuota > t.getV_cuota_max() && t.getV_cuota_max() != 0) {
            context.makeErrorDialog("Recuerda que la cuota máxima mensual de este ahorro es " + context.getMoneda(t.getV_cuota_max()));
            txtValorCuota.setError("Ingrese un valor valido");
            return false;
        } else if (_valorCuota < t.getV_cuota_min()) {
            context.makeErrorDialog("Recuerda que la cuota mínima mensual de este ahorro es " + context.getMoneda(t.getV_cuota_min()));
            txtValorCuota.setError("Ingrese un valor valido");
            return false;
        }
        if (spinnerPlazo != null && spinnerPlazo.getVisibility() == View.VISIBLE) {
            Object plazo = spinnerPlazo.getSelectedItem();
            if (plazo == null || TextUtils.isEmpty(plazo.toString())) {
                context.makeErrorDialog("Debe seleccionar un plazo de la lista");
                return false;
            }
        }
        if (txtFecha != null && txtFecha.getVisibility() == View.VISIBLE) {
            Object fechaPago = txtFecha.getText();
            if (fechaPago == null || TextUtils.isEmpty(fechaPago.toString())) {
                context.makeErrorDialog("Debe ingresar la fecha de pago");
                txtFecha.setError(null);
                return false;
            }
        }
        return true;
    }

    @Override
    public void showResultSolicitySaving(String resultSolicitySaving){
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle(context.getResources().getString(R.string.app_name));
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(resultSolicitySaving);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
                state.setFechaSeleccionada(null);
            }
        });
        d.show();
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
        if(state != null && state.getMensajesRespuesta() != null && state.getMensajesRespuesta().size()>0){
            for(ResponseMensajesRespuesta rm : state.getMensajesRespuesta()){
                if(rm.getIdMensaje() == 6){
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
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
                state.setFechaSeleccionada(null);
                dialog.dismiss();
            }
        });
        d.show();
    }

    @Override
    public void showDataFetchError(String message) {
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
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle(context.getResources().getString(R.string.app_name));
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(message);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
                state.setFechaSeleccionada(null);
                dialog.dismiss();
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

//    public void _formter(int len, String textToEdit, EditText txtEdit){
//        DecimalFormat fr;
//        DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
//        separadoresPersonalizados.setDecimalSeparator(',');
//
//        switch (len){
//
//            case 1:
//                fr = new DecimalFormat("#", separadoresPersonalizados);
//                String fa = fr.format(Long.parseLong(textToEdit));
//                txtEdit.setText(fa);
//                break;
//
//            case 2:
//                fr = new DecimalFormat("##", separadoresPersonalizados);
//                txtEdit.setText( fr.format(Long.parseLong(textToEdit)));
//                break;
//
//            case 3:
//                fr = new DecimalFormat("###", separadoresPersonalizados);
//                txtEdit.setText( fr.format(Long.parseLong(textToEdit)));
//                break;
//
//            case 4:
//                fr = new DecimalFormat("#,###", separadoresPersonalizados);
//                txtEdit.setText( fr.format(Long.parseLong(textToEdit)));
//                break;
//
//            case 5:
//                fr = new DecimalFormat("##,###", separadoresPersonalizados);
//                txtEdit.setText( fr.format(Long.parseLong(textToEdit)));
//                break;
//
//            case 6:
//                fr = new DecimalFormat("###,###", separadoresPersonalizados);
//                txtEdit.setText( fr.format(Long.parseLong(textToEdit)));
//                break;
//
//            case 7:
//                fr = new DecimalFormat("#,###,###", separadoresPersonalizados);
//                txtEdit.setText( fr.format(Long.parseLong(textToEdit)));
//                break;
//
//            case 8:
//                fr = new DecimalFormat("##,###,###", separadoresPersonalizados);
//                txtEdit.setText( fr.format(Long.parseLong(textToEdit)));
//                break;
//
//            case 9:
//                fr = new DecimalFormat("###,###,###", separadoresPersonalizados);
//                txtEdit.setText( fr.format(Long.parseLong(textToEdit)));
//                break;
//
//            case 10:
//                fr = new DecimalFormat("#,###,###,###", separadoresPersonalizados);
//                txtEdit.setText( fr.format(Long.parseLong(textToEdit)));
//                break;
//        }
//
//        txtEdit.setSelection(txtEdit.length());
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        if (context.getState().getFechaSeleccionada() == null
//                || TextUtils.isEmpty(context.getState().getFechaSeleccionada())) {
//            List<TiposAhorro> tiposAhorros = context.getState().getTiposAhorros();
//            if (tiposAhorros != null && tiposAhorros.size() > 0) {
//                cargarDatosAhorro(tiposAhorros);
//            } else {
//                GlobalState state = context.getState();
//                if (state == null || state.getUsuario() == null) {
//                    context.salir();
//                } else {
//                    Usuario usuario = state.getUsuario();
//                    new DatosTask().execute(usuario.cedula, usuario.token);
//                }
//            }
//        } else {
//            txtFecha.setText(context.getState().getFechaSeleccionada());
//        }
//    }
//
//
//    private void enviarSolicitud() {
//        try {
//            //validarDatos()
//            if (validarDatos()) {
//                String _valorCuota = (txtValorCuota.getText().toString());
//
//                AlertDialog.Builder d = new AlertDialog.Builder(context);
//                d.setTitle(context.getResources().getString(R.string.app_name));
//                d.setIcon(R.mipmap.icon_presente);
//                d.setMessage("Tu ahorro será de $" +_valorCuota + " mensuales");
//                d.setCancelable(false);
//                _valorCuota = _valorCuota.replace(",", "");
//                _valorCuota = _valorCuota.replace(".", "");
//                final String final_valorCuota = _valorCuota;
//                d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//
//                        try {
//                            GlobalState state = context.getState();
//
//                            if (state == null || state.getUsuario() == null) {
//                                context.salir();
//                                return;
//                            }
//
//                            final String vlCuota = final_valorCuota;
//                            Encripcion encripcion = Encripcion.getInstance();
//                            Usuario usuario = state.getUsuario();
//                            JSONObject solicitud = new JSONObject();
//                            solicitud.put("cedula", encripcion.encriptar(usuario.cedula));
//                            solicitud.put("token", usuario.token);
//                            solicitud.put("v_cuota", vlCuota);
//                            solicitud.put("k_tipodr", tipo.getK_tipodr());
//                            if (spinnerPlazo.getVisibility() == View.VISIBLE && spinnerPlazo.getSelectedItem() != null) {
//                                solicitud.put("v_plazo", seleccionarPlazo(spinnerPlazo.getSelectedItem().toString()));
//                            } else {
//                                solicitud.put("v_plazo", 0);
//                            }
//                            if (txtFecha.getVisibility() == View.VISIBLE && txtFecha.getText() != null) {
//                                solicitud.put("f_vigenci_fin", txtFecha.getText());
//                            } else {
//                                solicitud.put("f_vigenci_fin", JSONObject.NULL);
//                            }
//                            if (spinnerDescripcion.getVisibility() == View.VISIBLE && spinnerDescripcion.getSelectedItem() != null) {
//                                solicitud.put("n_destin", spinnerDescripcion.getSelectedItem().toString());
//                            } else {
//                                solicitud.put("n_destin", JSONObject.NULL);
//                            }
//
//                            solicitud.put("id_dispositivo", context.obtenerIdDispositivo());
//                            new EnviarSolicitudTask().execute(solicitud);
//                        } catch (Exception e) {
//                            context.makeErrorDialog(e.getMessage());
//                        }
//                    }
//                });
//                d.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                    }
//                });
//                d.show();
//
//            }
//        } catch (Exception ex) {
//            context.makeErrorDialog(ex.getMessage());
//        }
//    }
//
//    private int seleccionarPlazo(String plazo) {
//        return Integer.parseInt(plazo.split(" ")[0]);
//    }
//
//    private boolean validarDatos() {
//
//        Object tipoAhorro = spinnerTipoProd.getSelectedItem();
//        if (tipoAhorro == null || TextUtils.isEmpty(tipoAhorro.toString())) {
//            context.makeErrorDialog("Selecciona un tipo de ahorro");
//            return false;
//        }
//
//        Object valorCuota = txtValorCuota.getText();
//        if (valorCuota == null || TextUtils.isEmpty(valorCuota.toString())) {
//            context.makeErrorDialog("Ingresa el valor de la cuota");
//            txtValorCuota.setError("Ingrese un valor valido");
//            return false;
//        }
//
//        double _valorCuota;
//        valorCuota = valorCuota.toString().replace(",", "");
//        valorCuota = valorCuota.toString().replace(".", "");
//
//        try {
//            _valorCuota = Double.parseDouble(valorCuota.toString());
//        } catch (Exception ex) {
//            context.makeErrorDialog("Ingresa el valor de la cuota");
//            txtValorCuota.setError("Ingrese un valor valido");
//            return false;
//        }
//
//        if (_valorCuota <= 0) {
//            context.makeErrorDialog("Ingresa el valor de la cuota");
//            txtValorCuota.setError("Ingrese un valor valido");
//            return false;
//        }
//
//        TiposAhorro t = (TiposAhorro) tipoAhorro;
//        //if (t.v_plazo_minimo != t.v_plazo_maximo) {
//        if (_valorCuota > t.getV_cuota_maxima()) {
//            context.makeErrorDialog("Recuerda que la cuota máxima mensual de este ahorro es " + context.getMoneda(t.getV_cuota_maxima()));
//            txtValorCuota.setError("Ingrese un valor valido");
//            return false;
//        } else if (_valorCuota < t.getV_cuota_minima()) {
//            context.makeErrorDialog("Recuerda que la cuota mínima mensual de este ahorro es " + context.getMoneda(t.getV_cuota_minima()));
//            txtValorCuota.setError("Ingrese un valor valido");
//            return false;
//        }
//        //}
//
//        if (spinnerPlazo != null && spinnerPlazo.getVisibility() == View.VISIBLE) {
//            Object plazo = spinnerPlazo.getSelectedItem();
//            if (plazo == null || TextUtils.isEmpty(plazo.toString())) {
//                context.makeErrorDialog("Debe seleccionar un plazo de la lista");
//                return false;
//            }
//        }
//
//        if (txtFecha != null && txtFecha.getVisibility() == View.VISIBLE) {
//            Object fechaPago = txtFecha.getText();
//            if (fechaPago == null || TextUtils.isEmpty(fechaPago.toString())) {
//                context.makeErrorDialog("Debe ingresar la fecha de pago");
//                txtFecha.setError(null);
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    class DatosTask extends AsyncTask<String, String, String> {
//
//        String cedula = null;
//        String token = null;
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Actualizando información...");
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                Encripcion encripcion = Encripcion.getInstance();
//                JSONObject param = new JSONObject();
//                param.put("cedula", cedula = encripcion.encriptar(params[0]));
//                param.put("token", token = params[1]);
//                return networkHelper.writeService(param, SincroHelper.TIPO_AHORROS_DISPONIBLES);
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
//            procesarResultadoTiposAhorro(result);
//        }
//    }
//
//    private void procesarResultadoTiposAhorro(String result) {
//        try {
//            ArrayList<TiposAhorro> tipos = SincroHelper.procesarJsonTiposAhorro(result);
//            cargarDatosAhorro(tipos);
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
//        } catch (Exception ex) {
//            context.makeErrorDialog(ex.getMessage());
//        }
//    }
//
//    private void cargarDatosAhorro(List<TiposAhorro> tipos) {
//
//        //Tipos de ahorro
//        this.tipos = tipos;
//
//        context.getState().setTiposAhorros(tipos);
//        if(tipos == null) return;
//        TiposAhorro t = new TiposAhorro();
//        t.setN_tipodr("Selecciona tu ahorro");
//        if (tipos.size() > 0 && !tipos.get(0).getN_tipodr().equals(t.getN_tipodr())) {
//            tipos.add(0, t);
//        }
//
//        ArrayAdapter<TiposAhorro> adapter = new ArrayAdapter<>(context, R.layout.list_item_spinner, tipos);
//        spinnerTipoProd.setAdapter(adapter);
//        spinnerTipoProd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                opcionesTipoAhorro(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }
//
//    private void opcionesTipoAhorro(int tipoIndex) {
//        try {
//            if (tipoIndex > 0) {
//
//                this.tipo = this.tipos.get(tipoIndex);
//
//                if (tipo.getF_vigenci_fin() == null || TextUtils.isEmpty(tipo.getF_vigenci_fin())) {
//                    if (tipo.getI_fecha().equals("Y")) {
//                        //PEDIR FECHA
//                        txtFecha.setText("");
//                        txtFecha.setEnabled(true);
//                        txtFecha.setVisibility(View.VISIBLE);
//                        lblFecha.setVisibility(View.VISIBLE);
//                        spinnerPlazo.setVisibility(View.GONE);
//                        lblPlazo.setVisibility(View.GONE);
//                    } else {
//                        //PLAZO
//                        txtFecha.setVisibility(View.GONE);
//                        lblFecha.setVisibility(View.GONE);
//                        ArrayList<String> plazos = new ArrayList<>();
//                        if (tipo.getV_plazo_minimo() == tipo.getV_plazo_maximo()) {
//                            plazos.add((tipo.getV_plazo_minimo() > 1 ? tipo.getV_plazo_maximo() + " meses" : 1 + " mes"));
//                        } else {
//                            for (int i = tipo.getV_plazo_minimo(); i <= tipo.getV_plazo_maximo(); i++) {
//                                plazos.add((i > 1 ? i + " meses" : 1 + " mes"));
//                            }
//                        }
//                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.list_item_spinner, plazos);
//                        spinnerPlazo.setVisibility(View.VISIBLE);
//                        lblPlazo.setVisibility(View.VISIBLE);
//                        spinnerPlazo.setAdapter(adapter);
//                        spinnerPlazo.setEnabled(!(tipo.getV_plazo_minimo() == tipo.getV_plazo_maximo()));
//                    }
//                } else {
//                    txtFecha.setVisibility(View.VISIBLE);
//                    lblFecha.setVisibility(View.VISIBLE);
//                    spinnerPlazo.setVisibility(View.GONE);
//                    lblPlazo.setVisibility(View.GONE);
//                    txtFecha.setText(tipo.getF_vigenci_fin());
//                    txtFecha.setEnabled(false);
//                }
//
//
//                //VALOR CUOTA
//                if (tipo.getV_cuota_minima() == tipo.getV_cuota_maxima()) {
//                    txtValorCuota.setText(String.valueOf(tipo.getV_cuota_minima()));
//                } else {
//                    txtValorCuota.setText("");
//                }
//
//                txtValorCuota.setEnabled(!(tipo.getV_cuota_minima() == tipo.getV_plazo_maximo()));
//
//                //DESCRIPCIONES
//                if (tipo.getDescripciones() != null && tipo.getDescripciones().size() > 0) {
//                    spinnerDescripcion.setVisibility(View.VISIBLE);
//                    lblDescripcion.setVisibility(View.VISIBLE);
//                    ArrayAdapter<String> adapter_desc = new ArrayAdapter<>(context, R.layout.list_item_spinner, tipo.getDescripciones());
//                    spinnerDescripcion.setAdapter(adapter_desc);
//                } else {
//                    spinnerDescripcion.setVisibility(View.GONE);
//                    lblDescripcion.setVisibility(View.GONE);
//                }
//                txtValorCuota.setError(null);
//                txtFecha.setError(null);
//                frmFragmentAhorros.setBackgroundColor(00000);
//            } else {
//                limpiarCampos();
//            }
//        } catch (Exception e) {
//            context.makeErrorDialog(e.getMessage());
//        }
//    }
//
//    class EnviarSolicitudTask extends AsyncTask<JSONObject, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Enviando solicitud...");
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(JSONObject... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                return networkHelper.writeService(params[0], SincroHelper.CREAR_SOLICITUD_AHORRO);
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
//            procesarResultCrearSolicitudAhorro(result);
//        }
//    }
//
//    private void procesarResultCrearSolicitudAhorro(String result) {
//        try {
//            result = SincroHelper.procesarJsonCrearSolicitudAhorro(result);
//            AlertDialog.Builder d = new AlertDialog.Builder(context);
//            d.setTitle(context.getResources().getString(R.string.app_name));
//            d.setIcon(R.mipmap.icon_presente);
//            d.setMessage(result);
//            d.setCancelable(false);
//            d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    context.getState().getmTabHost().setCurrentTab(1);
//                    context.getState().setFechaSeleccionada(null);
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
//    private void limpiarCampos() {
//        if (txtValorCuota.isEnabled()) {
//            txtValorCuota.setText("");
//        }
//
//        if (txtFecha.isEnabled()) {
//            txtFecha.setText("");
//        }
//    }


}
