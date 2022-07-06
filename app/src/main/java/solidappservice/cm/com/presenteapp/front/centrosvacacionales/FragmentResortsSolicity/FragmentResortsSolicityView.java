package solidappservice.cm.com.presenteapp.front.centrosvacacionales.FragmentResortsSolicity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.centrosvacacionales.request.RequestSolicitarCentroVacacional;
import solidappservice.cm.com.presenteapp.entities.centrosvacacionales.response.ResponseCentroVacacional;
import solidappservice.cm.com.presenteapp.entities.centrosvacacionales.response.ResponseDetalleCentroVacacional;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 25/11/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 16/06/2021.
 */
public class FragmentResortsSolicityView extends Fragment implements FragmentResortsSolicityContract.View {

    private FragmentResortsSolicityPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private ProgressDialog pd;
    private FirebaseAnalytics firebaseAnalytics;
    private List<ResponseCentroVacacional> centros;

    @BindView(R.id.spinner_cv)
    Spinner spinner_cv;
    @BindView(R.id.spinner_cv_perfil)
    Spinner spinner_cv_perfil;
    @BindView(R.id.txtPermanencia)
    TextView txtPermanencia;
    @BindView(R.id.txtObservaciones)
    EditText txtObservaciones;
    @BindView(R.id.txtCantAdultos)
    EditText txtCantAdultos;
    @BindView(R.id.txtCantKids)
    EditText txtCantKids;
    @BindView(R.id.txtEmail)
    EditText txtEmail;
    @BindView(R.id.btnAceptar)
    Button btnAceptar;
    //private CheckBox rbAsociadoViaja;

    //@BindView(R.id.lblFechaDesde)
    static TextView lblFechaDesde;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de solicitud CV");
        firebaseAnalytics.logEvent("pantalla_solicitud_cv", params);
        View view = inflater.inflate(R.layout.fragment_resorts_solicity, container, false);
        lblFechaDesde = view.findViewById(R.id.lblFechaDesde);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentResortsSolicityPresenter(this, new FragmentResortsSolicityModel());
        context = (ActivityBase) getActivity();
        state = context.getState();
        pd = new ProgressDialog(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        List<ResponseCentroVacacional> centros = state.getCentrosVacacionales();
        if (centros != null && centros.size() > 0) {
//            mostrarCentros(centros);
            showResorts(centros);
        } else {
            if(state == null || state.getUsuario() == null){
                context.salir();
            }else {
//                Usuario usuario = state.getUsuario();
//                new CentrosTask().execute(usuario.cedula, usuario.token);
                fetchResorts();
            }
        }
    }

    @OnItemSelected(R.id.spinner_cv)
    public void onItemSelectedResort(AdapterView<?> parent, View view, int position, long id) {
        txtCantKids.setText("");
        txtCantAdultos.setText("");
        txtObservaciones.setText("");
        if (position > 0) {
            ResponseCentroVacacional c = centros.get(position);
//            mostrarPerfiles(c);
            showDetailResort(c);
        } else {
            ArrayAdapter<ResponseDetalleCentroVacacional> adapter =
                    new ArrayAdapter<>(context, R.layout.list_item_spinner,
                            new ArrayList<ResponseDetalleCentroVacacional>());
            spinner_cv_perfil.setAdapter(adapter);
        }
    }

    @OnClick(R.id.btnAceptar)
    public void onClickAceptar(View v) {
//        hacerSolicitud();
        validateData();
    }

    @OnClick(R.id.lblFechaDesde)
    public void onClickFechaDesde(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("fecha", "lblFechaDesde");
        showDatePicker(bundle);
    }

    @Override
    public void fetchResorts(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchResorts(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void showResorts(List<ResponseCentroVacacional> centros) {
        state.setCentrosVacacionales(centros);
        ResponseCentroVacacional c = new ResponseCentroVacacional();
        c.setNombre("Selecciona el destino");
        if (!centros.get(0).getNombre().equals(c.getNombre())) {
            centros.add(0, c);
        }
        ArrayAdapter<ResponseCentroVacacional> adapter = new ArrayAdapter<>(context, R.layout.list_item_spinner, centros);
        spinner_cv.setAdapter(adapter);
        this.centros = centros;
    }

    @Override
    public void showDetailResort(ResponseCentroVacacional centroVacacional) {
        try {
            List<ResponseDetalleCentroVacacional> detallesCV = centroVacacional.getPerfiles();
            if (detallesCV != null && detallesCV.size() > 0) {
                ArrayAdapter<ResponseDetalleCentroVacacional> adapter =
                        new ArrayAdapter<>(context, R.layout.list_item_spinner, detallesCV);
                spinner_cv_perfil.setAdapter(adapter);
            } else {
                ArrayAdapter<ResponseDetalleCentroVacacional> adapter =
                        new ArrayAdapter<>(context, R.layout.list_item_spinner,
                                new ArrayList<ResponseDetalleCentroVacacional>());
                spinner_cv_perfil.setAdapter(adapter);
            }
        } catch (Exception e) {
            context.makeErrorDialog("Error cargando los perfiles del centro vacacional");
        }
    }

    @Override
    public void showDatePicker(Bundle bundle) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(context.getFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private String lblFechaActual;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            Bundle bundle = getArguments();
            if (bundle != null) {
                lblFechaActual = bundle.getString("fecha", "lblFechaDesde");
            }
            //Create a new instance of DatePickerDialog and return it
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMinDate(c.getTimeInMillis());

            final Calendar c2 = Calendar.getInstance();
            c2.add(Calendar.YEAR, 1);
            dialog.getDatePicker().setMaxDate(c2.getTimeInMillis());

            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            StringBuilder fecha = new StringBuilder();
            int mes = (month + 1);
            fecha.append((day < 10 ? ("0" + day) : String.valueOf(day)))
                    .append("/").append(mes < 10 ? ("0" + mes) : String.valueOf(mes))
                    .append("/").append(String.valueOf(year));

            if (lblFechaActual.equals("lblFechaDesde")) {
                lblFechaDesde.setText(fecha.toString());
            }
        }
    }

    @Override
    public void validateData() {
        try {
            if (spinner_cv == null || spinner_cv.getSelectedItem() == null
                    || spinner_cv.getSelectedItemPosition() == 0) {
                context.makeErrorDialog("Selecciona el destino");
                return;
            }

            ResponseCentroVacacional c = centros.get(spinner_cv.getSelectedItemPosition());
            if (spinner_cv_perfil == null || spinner_cv_perfil.getSelectedItem() == null) {
                context.makeErrorDialog("Selecciona el Tipo de unidad vacacional");
                return;
            }

            ResponseDetalleCentroVacacional p = (ResponseDetalleCentroVacacional) spinner_cv_perfil.getSelectedItem();
            if (lblFechaDesde.getText() == null || TextUtils.isEmpty(lblFechaDesde.getText())) {
                context.makeErrorDialog("Ingresa la fecha de llegada en el campo \"Fecha de llegada\"");
                return;
            }

            if (txtPermanencia.getText() == null || TextUtils.isEmpty(txtPermanencia.getText())) {
                context.makeErrorDialog("Ingresa el número de días de estadía en el campo \"Días de permanencia\"");
                return;
            }

            int dias;
            try {
                dias = Integer.parseInt(txtPermanencia.getText().toString());
            } catch (Exception e) {
                context.makeErrorDialog("Ingrese el número de días de estadía en el campo \"Días de permanencia\"");
                return;
            }

            if (dias <= 0) {
                context.makeErrorDialog("Ingrese el número de días de estadía en el campo \"Días de permanencia\"");
                return;
            }

            if (dias > ActivityBase.MAX_DIAS_ESTADIA) {
                context.makeErrorDialog("Ingrese un número de días de estadía no mayor a " + ActivityBase.MAX_DIAS_ESTADIA);
                return;
            }

            if ((txtCantKids.getText() == null || TextUtils.isEmpty(txtCantKids.getText()))
                    && (txtCantAdultos.getText() == null || TextUtils.isEmpty(txtCantAdultos.getText()))) {
                context.makeErrorDialog("Ingrese el número total de personas que visitarán el centro vacacional");
                return;
            }

            int cantA;
            try {
                cantA = Integer.parseInt(txtCantAdultos.getText().toString());
            } catch (Exception e) {
                cantA = 0;
            }

            int cantK;
            try {
                cantK = Integer.parseInt(txtCantKids.getText().toString());
            } catch (Exception e) {
                cantK = 0;
            }

            if (cantA == 0 && cantK == 0) {
                context.makeErrorDialog("Ingrese el número total de personas que visitarán el centro vacacional");
                return;
            }

            if (txtEmail.getText() == null || TextUtils.isEmpty(txtEmail.getText())) {
                context.makeErrorDialog("Ingresa tu correo electrónico en el campo \"Email de contacto\"");
                return;
            }

            if(!ActivityBase.validateEmail(txtEmail.getText().toString())){
                context.makeErrorDialog("Ingresa un correo electrónico válido en el campo \"Email de contacto\"");
                return;
            }

            GlobalState state = context.getState();
            if(state == null || state.getUsuario() == null){
                context.salir();
            }else {
                solicityResort(c, p, dias, cantA, cantK);
            }

        } catch (Exception e) {
            context.makeErrorDialog(e.getMessage());
        }
    }

    @Override
    public void solicityResort(ResponseCentroVacacional resort, ResponseDetalleCentroVacacional detail, Integer days,
                               Integer numberOfAdults, Integer numberOfKids){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.solicityResort(new RequestSolicitarCentroVacacional(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    String.valueOf(numberOfAdults),
                    String.valueOf(numberOfKids),
                    resort.getCodigo(),
                    detail.getCodigo(),
                    lblFechaDesde.getText().toString(),
                    String.valueOf(days),
                    txtObservaciones.getText().toString(),
                    context.obtenerIdDispositivo(),
                    txtEmail.getText().toString()
            ));
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void showResultSolicityResort(String result){
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle(context.getResources().getString(R.string.app_name));
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(result);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                context.getState().getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
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
                context.getState().getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
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
                context.getState().getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
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


//    private void mostrarPerfiles(CentroVacacional centroVacacional) {
//        try {
//            ArrayList<PerfilCentroVacacional> perfiles = centroVacacional.perfiles;
//            if (perfiles != null && perfiles.size() > 0) {
//                ArrayAdapter<PerfilCentroVacacional> adapter =
//                        new ArrayAdapter<>(context, R.layout.list_item_spinner, perfiles);
//                spinner_cv_perfil.setAdapter(adapter);
//            } else {
//                ArrayAdapter<PerfilCentroVacacional> adapter =
//                        new ArrayAdapter<>(context, R.layout.list_item_spinner,
//                                new ArrayList<PerfilCentroVacacional>());
//                spinner_cv_perfil.setAdapter(adapter);
//            }
//        } catch (Exception e) {
//            context.makeErrorDialog("Error cargando los perfiles del centro vacacional");
//        }
//    }
//
//    private void hacerSolicitud() {
//        try {
//
//            if (spinner_cv == null || spinner_cv.getSelectedItem() == null
//                    || spinner_cv.getSelectedItemPosition() == 0) {
//                context.makeErrorDialog("Selecciona el destino");
//                return;
//            }
//
//            CentroVacacional c = centros.get(spinner_cv.getSelectedItemPosition());
//
//            if (spinner_cv_perfil == null || spinner_cv_perfil.getSelectedItem() == null) {
//                context.makeErrorDialog("Selecciona el Tipo de unidad vacacional");
//                return;
//            }
//
//            PerfilCentroVacacional p = (PerfilCentroVacacional) spinner_cv_perfil.getSelectedItem();
//
//            if (lblFechaDesde.getText() == null || TextUtils.isEmpty(lblFechaDesde.getText())) {
//                context.makeErrorDialog("Ingresa la fecha de llegada en el campo \"Fecha de llegada\"");
//                return;
//            }
//
//            if (txtPermanencia.getText() == null || TextUtils.isEmpty(txtPermanencia.getText())) {
//                context.makeErrorDialog("Ingresa el número de días de estadía en el campo \"Días de permanencia\"");
//                return;
//            }
//
//            int dias;
//            try {
//                dias = Integer.parseInt(txtPermanencia.getText().toString());
//            } catch (Exception e) {
//                context.makeErrorDialog("Ingrese el número de días de estadía en el campo \"Días de permanencia\"");
//                return;
//            }
//
//            if (dias <= 0) {
//                context.makeErrorDialog("Ingrese el número de días de estadía en el campo \"Días de permanencia\"");
//                return;
//            }
//
//            if (dias > ActivityBase.MAX_DIAS_ESTADIA) {
//                context.makeErrorDialog("Ingrese un número de días de estadía no mayor a " + ActivityBase.MAX_DIAS_ESTADIA);
//                return;
//            }
//
//            if ((txtCantKids.getText() == null || TextUtils.isEmpty(txtCantKids.getText()))
//                    && (txtCantAdultos.getText() == null || TextUtils.isEmpty(txtCantAdultos.getText()))) {
//                context.makeErrorDialog("Ingrese el número total de personas que visitarán el centro vacacional");
//                return;
//            }
//
//            int cantA;
//            try {
//                cantA = Integer.parseInt(txtCantAdultos.getText().toString());
//            } catch (Exception e) {
//                cantA = 0;
//            }
//
//            int cantK;
//            try {
//                cantK = Integer.parseInt(txtCantKids.getText().toString());
//            } catch (Exception e) {
//                cantK = 0;
//            }
//
//            if (cantA == 0 && cantK == 0) {
//                context.makeErrorDialog("Ingrese el número total de personas que visitarán el centro vacacional");
//                return;
//            }
//
//            if (txtEmail.getText() == null || TextUtils.isEmpty(txtEmail.getText())) {
//                context.makeErrorDialog("Ingresa tu correo electrónico en el campo \"Email de contacto\"");
//                return;
//            }
//
//            if(!ActivityBase.validateEmail(txtEmail.getText().toString())){
//                context.makeErrorDialog("Ingresa un correo electrónico válido en el campo \"Email de contacto\"");
//                return;
//            }
//
//            GlobalState state = context.getState();
//            if(state == null || state.getUsuario() == null){
//                context.salir();
//            }else {
//
//                Encripcion encripcion = Encripcion.getInstance();
//                Usuario usuario = state.getUsuario();
//                String idDispositivo = context.obtenerIdDispositivo();
//                JSONObject obj = new JSONObject();
//                obj.put("cedula", encripcion.encriptar(usuario.cedula));
//                obj.put("token", usuario.token);
//                //obj.put("asociadoViaja", rbAsociadoViaja.isChecked() ? "Y" : "N");
//                obj.put("numeroAdultos", String.valueOf(cantA));
//                obj.put("numeroNinos", String.valueOf(cantK));
//                obj.put("codigo", c.codigo);
//                obj.put("codigoPerfil", p.codigo);
//                obj.put("fecha", lblFechaDesde.getText());
//                obj.put("numeroDias", String.valueOf(dias));
//                obj.put("observaciones", txtObservaciones.getText());
//                obj.put("idDispositivo", idDispositivo);
//                obj.put("emailContacto", txtEmail.getText());
//
//                new EnviarSolicitudTask().execute(obj);
//            }
//
//        } catch (Exception e) {
//            context.makeErrorDialog(e.getMessage());
//        }
//    }
//
//    private void mostrarDatePicker(Bundle bundle) {
//        DialogFragment newFragment = new DatePickerFragment();
//        newFragment.setArguments(bundle);
//        newFragment.show(context.getFragmentManager(), "datePicker");
//    }
//

//    private void mostrarCentros(ArrayList<CentroVacacional> centros) {
//        CentroVacacional c = new CentroVacacional();
//        c.nombre = "Selecciona el destino";
//
//
//        if (!centros.get(0).nombre.equals(c.nombre)) {
//            centros.add(0, c);
//        }
//
//        ArrayAdapter<CentroVacacional> adapter = new ArrayAdapter<>(context, R.layout.list_item_spinner, centros);
//        spinner_cv.setAdapter(adapter);
//        this.centros = centros;
//    }
//
//    class CentrosTask extends AsyncTask<String, String, String> {
//
//        String cedula = null;
//        String token = null;
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Obteniendo centros vacacionales...");
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
//                return networkHelper.writeService(param, SincroHelper.CENTROS_VACACIONALES);
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
//            procesarJsonCentros(result);
//        }
//    }
//
//    private void procesarJsonCentros(String jsonRespuesta) {
//        try {
//            ArrayList<CentroVacacional> centros = SincroHelper.procesarJsonCentros(jsonRespuesta);
//            context.getState().setCentrosVacacionales(centros);
//            mostrarCentros(centros);
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
//    public static class DatePickerFragment extends DialogFragment
//            implements DatePickerDialog.OnDateSetListener {
//
//        private String lblFechaActual;
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // Use the current date as the default date in the picker
//            final Calendar c = Calendar.getInstance();
//            int year = c.get(Calendar.YEAR);
//            int month = c.get(Calendar.MONTH);
//            int day = c.get(Calendar.DAY_OF_MONTH);
//
//            Bundle bundle = getArguments();
//            if (bundle != null) {
//                lblFechaActual = bundle.getString("fecha", "lblFechaDesde");
//            }
//            //Create a new instance of DatePickerDialog and return it
//            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
//            dialog.getDatePicker().setMinDate(c.getTimeInMillis());
//
//            final Calendar c2 = Calendar.getInstance();
//            c2.add(Calendar.YEAR, 1);
//            dialog.getDatePicker().setMaxDate(c2.getTimeInMillis());
//
//            return dialog;
//        }
//
//        public void onDateSet(DatePicker view, int year, int month, int day) {
//            StringBuilder fecha = new StringBuilder();
//            int mes = (month + 1);
//            fecha.append((day < 10 ? ("0" + day) : String.valueOf(day)))
//                    .append("/").append(mes < 10 ? ("0" + mes) : String.valueOf(mes))
//                    .append("/").append(String.valueOf(year));
//
//            if (lblFechaActual.equals("lblFechaDesde")) {
//                lblFechaDesde.setText(fecha.toString());
//            }
//        }
//    }
//
//    class EnviarSolicitudTask extends AsyncTask<JSONObject, String, String> {
//
//        String cedula = null;
//        String token = null;
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
//                return networkHelper.writeService(params[0], SincroHelper.SOLICITUD_CV);
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
//            procesarJsonSolicitudCV(result);
//        }
//    }
//
//    private void procesarJsonSolicitudCV(String result) {
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

}
