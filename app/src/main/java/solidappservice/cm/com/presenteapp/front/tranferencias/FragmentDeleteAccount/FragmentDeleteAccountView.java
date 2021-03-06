package solidappservice.cm.com.presenteapp.front.tranferencias.FragmentDeleteAccount;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.transferencias.AccountsAdapter;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.entities.transferencias.response.ResponseCuentasInscritas;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.login.Response.Usuario;
import solidappservice.cm.com.presenteapp.entities.transferencias.request.RequestDeleteAccount;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 20/05/2016.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 11/09/2021.
 */
public class FragmentDeleteAccountView extends Fragment implements FragmentDeleteAccountContract.View {

    private FragmentDeleteAccountPresenter presenter;
    private ActivityTabsView context;
    private GlobalState state;
    private ProgressDialog pd;
    private FirebaseAnalytics firebaseAnalytics;
    private List<ResponseCuentasInscritas> cuentasDisponibles;
    private List<ResponseCuentasInscritas> cuentas_selected;

    @BindView(R.id.llCuentasDestinatario)
    LinearLayout llCuentasDestinatario;
    @BindView(R.id.btnBorrarCuenta)
    Button btnBorrarCuenta;

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
        params.putString("Descripción", "Interacción con pantalla de eliminar cuenta");
        firebaseAnalytics.logEvent("pantalla_eliminar_cuenta", params);
        View view = inflater.inflate(R.layout.fragment_transfers_deleteaccount, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentDeleteAccountPresenter(this, new FragmentDeleteAccountModel());
        context = (ActivityTabsView) getActivity();
        state = context.getState();
        pd = new ProgressDialog(context);
        cuentas_selected = new ArrayList<>();
    }

    @Override
    public void onResume() {
        GlobalState state = context.getState();
        Usuario usuario = state.getUsuario();
        if(usuario != null)
            fetchRegisteredAccounts();
//            new ConsultarCuentasTask().execute(usuario.cedula, usuario.token);
        super.onResume();
    }

    @OnClick(R.id.btnBorrarCuenta)
    public void onClickDeleteAccount(){
        Resources resources = getResources();
        if (cuentas_selected == null || cuentas_selected.size() <= 0) {
            context.makeErrorDialog(resources.getString(R.string.error_cuenta_borrar));
        } else {
            if (state.getUsuario() == null) {
                return;
            }
            String sms = "¿Desea eliminar estas cuentas?";
            for(int i=0; i<cuentas_selected.size(); i++){
                sms = sms+"\n"+cuentas_selected.get(i).getNnasocia();
            }
            confirmDeleteSelectedAccounts(sms);
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
            showDataFetchError("");
        }
    }

    @Override
    public void showRegisteredAccounts(List<ResponseCuentasInscritas> cuentas){
        try{
            this.cuentasDisponibles = cuentas;
            llCuentasDestinatario.removeAllViews();
            AccountsAdapter adapter = new AccountsAdapter(context, cuentas);
            for (int pos = 0; pos < adapter.getCount(); pos++) {
                final int index = pos;
                View item = adapter.getView(pos, null, null);
                item.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox chk = item.findViewById(R.id.item_checkbox);
                        ResponseCuentasInscritas cuenta = (ResponseCuentasInscritas)adapter.getItem(index);
                        if(chk.isChecked()){
                            chk.setChecked(false);
                            if(cuentas_selected.contains(cuenta)){
                                cuentas_selected.remove(cuenta);
                            }
                        }else{
                            chk.setChecked(true);
                            if(!cuentas_selected.contains(cuenta)){
                                cuentas_selected.add(cuenta);
                            }
                        }

                        if(cuentas_selected != null && cuentas_selected.size()>0){
                            enabledDeleteAccountButton();
                        }
                    }
                });
                llCuentasDestinatario.addView(item);
            }
        } catch(Exception ex){
            showDataFetchError("Upps, se ha producido un error consultando las cuentas disponibles, inténtalo nuevamente en unos minutos.");
        }
    }

    @Override
    public void confirmDeleteSelectedAccounts(String cuentas){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(cuentas)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        new BorrarCuentaTask(cuentas_selected).execute(usuario.cedula, usuario.token);
                        deleteSelectedAccounts(cuentas_selected);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.show();
    }

    @Override
    public void deleteSelectedAccounts(List<ResponseCuentasInscritas> accountsSelected){
        try{
            if(accountsSelected != null && accountsSelected.size()>0) {
                Encripcion encripcion = Encripcion.getInstance();
                List<RequestDeleteAccount> listAccountsSelected = new ArrayList<>();
                for (ResponseCuentasInscritas cuenta : accountsSelected){
                    listAccountsSelected.add(new RequestDeleteAccount(
                        encripcion.encriptar(state.getUsuario().getCedula()),
                        state.getUsuario().getToken(),
                        cuenta.getAanumnit(),
                        cuenta.getN_numcta()
                    ));
                }
                presenter.deleteSelectedAccounts(listAccountsSelected);
            }
        }catch (Exception ex){
            enabledDeleteAccountButton();
            showDataFetchError("");
        }
    }

    @Override
    public void showResultDeleteAccounts(String resultDelete){
        try {
            AlertDialog.Builder d = new AlertDialog.Builder(context);
            d.setTitle(getResources().getString(R.string.app_name));
            d.setIcon(R.mipmap.icon_presente);
            d.setMessage(resultDelete);
            d.setCancelable(true);
            d.setPositiveButton("Aceptar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            fetchRegisteredAccounts();
                        }
                    });
            d.show();
        } catch (Exception e) {
            context.makeDialog(resultDelete);
        }
    }

    @Override
    public void disabledDeleteAccountButton() {
        btnBorrarCuenta.setEnabled(false);
    }

    @Override
    public void enabledDeleteAccountButton(){
        btnBorrarCuenta.setEnabled(true);
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
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_17_TRANSFERS_MENU_TAG);
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
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_17_TRANSFERS_MENU_TAG);
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

//    public void cargarCuentasDestinatario(final ArrayList<CuentasInscritas> cuentas) {
//        Resources resources = getResources();
//        if (cuentas == null || cuentas.size() <= 0) {
//
//            context.makeErrorDialog(resources.getString(R.string.error_cuentas_no_encontradas));
//            return;
//        }
//
//        cuentasDisponibles = cuentas;
//        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
//        llCuentasDestinatario.removeAllViews();
//        int id = 0;
//        CheckBox cb;
//        for (CuentasInscritas c : cuentas) {
//            cb = new CheckBox(this.context);
//            cb.setText(c.getNnasocia());
//            cb.setId(id);
//            cb.setBackgroundColor(getResources().getColor(R.color.gris));
//            cb.setPadding(0, 30, 0, 30);
//            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    int id = buttonView.getId();
//                    if (cuentasDisponibles != null && cuentasDisponibles.size() > 0) {
//                        if(cuentas_selected == null) cuentas_selected = new ArrayList<>();
//                        CuentasInscritas c = cuentasDisponibles.get(id);
//                        boolean addCuenta = isChecked && !cuentas_selected.contains(c);
//                        boolean removeCuenta = !isChecked && cuentas_selected.contains(c);
//                        if(addCuenta){
//                            cuentas_selected.add(c);
//                        }else if(removeCuenta){
//                            cuentas_selected.remove(c);
//                        }
//
//                    } else {
//                        cuentas_selected = null;
//                    }
//                }
//            });
//            llCuentasDestinatario.addView(cb, layoutParams);
//            id++;
//        }
//    }
//
//    private class ConsultarCuentasTask extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setMessage("Consultando cuentas...");
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper(context.obtenerConfiguracionSecureUrl());
//
//                JSONObject param = new JSONObject();
//                param.put("cedula", Encripcion.getInstance().encriptar(params[0]));
//                param.put("token", params[1]);
//                return networkHelper.writeService(param,
//                        SincroHelper.CONSULTA_CUENTAS_INSCRITAS);
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
//            procesarJsonRespuesta(result);
//        }
//    }
//
//    private void procesarJsonRespuesta(String jsonString) {
//        try {
//            ArrayList<CuentasInscritas> listado_cuentas_inscritas
//                    = SincroHelper.procesarJsonCuentasInscritas(jsonString);
//            cargarCuentasInscritas(listado_cuentas_inscritas);
//        }  catch (Exception e) {
//            context.makeErrorDialog(e.getMessage());
//        }
//    }
//
//    private void cargarCuentasInscritas(ArrayList<CuentasInscritas> listado_cuentas_inscritas) {
//        try {
//            cargarCuentasDestinatario(listado_cuentas_inscritas);
//        } catch (Exception ex) {
//            context.makeErrorDialog("Error cargando las cuentas inscritas");
//        }
//    }
//
//    private class BorrarCuentaTask extends AsyncTask<String, String, String> {
//
//        List<CuentasInscritas> cuentasInscritas;
//
//        BorrarCuentaTask(List<CuentasInscritas> cuentasInscritas){
//            this.cuentasInscritas = cuentasInscritas;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setMessage("Borrando cuenta...");
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                String cedula = params[0];
//                String token = params[1];
//                String response = "";
//                for (CuentasInscritas ci : cuentas_selected) {
//                    String cedulaInscripcion = ci.getAanumnit();
//                    String cuentaInscripcion = ci.getN_numcta();
//                    JSONObject param = new JSONObject();
//                    param.put("cedula", Encripcion.getInstance().encriptar(cedula));
//                    param.put("token", token);.0
//                    param.put("cedulaInscrita", cedulaInscripcion);
//                    param.put("numeroCuenta", cuentaInscripcion);
//                    NetworkHelper networkHelper = new NetworkHelper(context.obtenerConfiguracionSecureUrl());
//                    response = networkHelper.writeService(param, SincroHelper.BORRAR_CUENTA);
//                }
//                return response;
//
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
//            mostrarResultadoBorrarCuenta(result);
//        }
//    }
//
//    private void mostrarResultadoBorrarCuenta(String result) {
//        try {
//            AlertDialog.Builder d = new AlertDialog.Builder(context);
//            d.setTitle(getResources().getString(R.string.app_name));
//            d.setIcon(R.mipmap.icon_presente);
//            d.setMessage(result = SincroHelper.procesarJsonCrearSolicitudAhorro(result));
//            d.setCancelable(true);
//            d.setPositiveButton("Aceptar",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialogo1, int id) {
//                            context.onBackPressed();
//                        }
//                    });
//            d.show();
//        } catch (Exception e) {
//
//            context.makeDialog(result);
//        }
//    }
}
