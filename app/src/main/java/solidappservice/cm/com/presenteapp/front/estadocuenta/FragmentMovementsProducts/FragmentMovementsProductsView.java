package solidappservice.cm.com.presenteapp.front.estadocuenta.FragmentMovementsProducts;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.estadocuenta.MovimientoAdapter;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.request.RequestMovimientosProducto;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseMovimientoProducto;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProductos;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 30/11/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public class FragmentMovementsProductsView extends Fragment implements FragmentMovementsProductsContract.View {

    private FragmentMovementsProductsPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private ProgressDialog pd;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.lblMovimientos)
    TextView lblMovimientos;
    @BindView(R.id.lblSaldoValue)
    TextView lblSaldoValue;
    @BindView(R.id.list_movimientos)
    ListView list_movimientos;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de movimientos");
        firebaseAnalytics.logEvent("pantalla_movimientos", params);
        View view = inflater.inflate(R.layout.fragment_statusaccount_movementsproducts, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentMovementsProductsPresenter(this, new FragmentMovementsProductsModel());
        context = (ActivityBase) getActivity();
        state = context.getState();
        pd = new ProgressDialog(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        ResponseProductos producto = context.getState().getProductoSeleccionado();
        if(producto != null){
//            mostrarproducto(producto);
            fetchMovementsProducts(producto);
        }
    }

    @Override
    public void fetchMovementsProducts(ResponseProductos producto){
        try{
            lblMovimientos.setText(String.format("%s:  %s", producto.getN_produc(), producto.getA_numdoc()));
            lblSaldoValue.setText(context.getMoneda(producto.getV_saldo()));
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchMovementsProducts(new RequestMovimientosProducto(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    producto.getK_tipcuent(),
                    producto.getA_tipodr(),
                    producto.getA_numdoc()
            ));
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void showMovementsProducts(List<ResponseMovimientoProducto> movimientos){
        try{
            if(movimientos != null && movimientos.size() > 0){
                MovimientoAdapter adapter = new MovimientoAdapter(context, movimientos);
                list_movimientos.setAdapter(adapter);
            }else{
                AlertDialog.Builder d = new AlertDialog.Builder(context);
                d.setTitle(context.getResources().getString(R.string.app_name));
                d.setIcon(R.mipmap.icon_presente);
                d.setMessage("Este producto no tiene movimientos recientes");
                d.setCancelable(false);
                d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.getState().getmTabHost().setCurrentTab(ActivityTabsView.TAB_6_STATUS_ACCOUNT_PRODUCTS_TAG);
                        dialog.dismiss();
                    }
                });
                d.show();
            }
        }catch (Exception ex){
            showDataFetchError("");
        }
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
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_6_STATUS_ACCOUNT_PRODUCTS_TAG);
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
                dialog.dismiss();
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_6_STATUS_ACCOUNT_PRODUCTS_TAG);
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


//    private void mostrarproducto(Producto producto){
//        lblMovimientos.setText(producto.n_produc + ":  " + producto.a_numdoc);
//        lblSaldoValue.setText(context.getMoneda(producto.v_saldo));
//
//        MovimientoAdapter adapter = new MovimientoAdapter(context, producto.movimientos);
//        list_movimientos.setAdapter(adapter);
//    }


//    private void mostrarproducto(ResponseProducto producto){
//        try{
//            lblMovimientos.setText(String.format("%s:  %s", producto.getN_produc(), producto.getA_numdoc()));
//            lblSaldoValue.setText(context.getMoneda(producto.getV_saldo()));
//
//            GlobalState state = context.getState();
//            Usuario usuario = state.getUsuario();
//            Encripcion encripcion = Encripcion.getInstance();
//            JSONObject param = new JSONObject();
//            param.put("cedula", encripcion.encriptar(usuario.cedula));
//            param.put("token", usuario.token);
//            param.put("k_tipcuent", producto.getK_tipcuent());
//            param.put("v_tipodr", producto.getA_tipodr());
//            param.put("v_numdoc",producto.getA_numdoc());
//            new ObtenerMovimientosProductoTask().execute(param);
//
//        } catch (Exception e) {
//            context.makeErrorDialog(e.getMessage());
//        }
//    }
//
//
////    Obtiene los movimientos del producto seleccionado
//    private class ObtenerMovimientosProductoTask extends AsyncTask<JSONObject, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle("Espera un momento");
//            pd.setMessage("Estamos realizando la consulta de tus saldos y productos...");
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(JSONObject... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                return networkHelper.writeService(params[0], SincroHelper.CONSULTAR_DETALLE_CUENTA);
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
//            procesarJsonMovimientosCuenta(result);
//            pd.dismiss();
//        }
//    }
//
//    private void procesarJsonMovimientosCuenta(String jsonRespuesta) {
//        try {
//            ArrayList<ResponseMovimientoProducto> listaMovimientos = SincroHelper.procesarJsonMovimientosCuenta(jsonRespuesta);
//
//            if(listaMovimientos != null && listaMovimientos.size() > 0){
//                MovimientoAdapter adapter = new MovimientoAdapter(context, listaMovimientos);
//                list_movimientos.setAdapter(adapter);
//            }else{
//                AlertDialog.Builder d = new AlertDialog.Builder(context);
//                d.setTitle(context.getResources().getString(R.string.app_name));
//                d.setIcon(R.mipmap.icon_presente);
//                d.setMessage("Este producto no tiene movimientos recientes");
//                d.setCancelable(false);
//                d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        context.getState().getmTabHost().setCurrentTab(8);
//                        dialog.dismiss();
//                    }
//                });
//                d.show();
//            }
//        } catch (Exception e) {
//            context.makeErrorDialog(e.getMessage());
//        }
//    }


}
