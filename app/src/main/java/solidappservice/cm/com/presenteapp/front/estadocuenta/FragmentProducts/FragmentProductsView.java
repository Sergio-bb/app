package solidappservice.cm.com.presenteapp.front.estadocuenta.FragmentProducts;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.estadocuenta.DetalleProductoAdapter;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProductos;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 27/11/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public class FragmentProductsView extends Fragment implements FragmentProductsContract.View {

    private FragmentProductsPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private ProgressDialog pd;
    private FirebaseAnalytics firebaseAnalytics;
    private List<ResponseProductos> mProductos;

    @BindView(R.id.contenedorProductos)
    ListView contenedorProductos;
    @BindView(R.id.lblNombreProducto)
    TextView lblNombreProducto;

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
        params.putString("Descripción", "Interacción con pantalla de productos");
        firebaseAnalytics.logEvent("pantalla_productos", params);
        View view = inflater.inflate(R.layout.fragment_statusaccount_products, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentProductsPresenter(this, new FragmentProductsModel());
        context = (ActivityBase) getActivity();
        state = context.getState();
        pd = new ProgressDialog(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (state.getProductosDetalle().get(0).getN_tipodr().equals("Mis Aportes")) {
//            new obtenerEstadoMensajeMisAportesTask().execute();
            fetchStatusMessageMisAportes();
        }
        List<ResponseProductos> productos = state.getProductosDetalle();
        if (productos != null && productos.size() > 0) {
//            mostrarProductos(productos);
            showProducts(productos);
        }
    }

    @OnItemClick(R.id.contenedorProductos)
    public void onItemClickProductos(AdapterView<?> parent, View view, int position, long id) {
        ResponseProductos producto = mProductos.get(position);
        state.setProductoSeleccionado(producto);
        state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_7_STATUS_ACCOUNT_MOVEMENTS_PRODUCT_TAG);
    }

    @Override
    public void fetchStatusMessageMisAportes(){
        try{
            presenter.fetchStatusMessageMisAportes();
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void showMessageMisAportes(){
        try{
            AlertDialog.Builder d = new AlertDialog.Builder(context);
            d.setTitle("Mis Aportes:");
            d.setIcon(R.mipmap.icon_presente);
            d.setMessage("El Ahorro Permanente y el Ahorro Aporte Social son ahorros a largo plazo que te " +
                    "brindan la posibilidad de ser asociado al Fondo de Empleados y disfrutar de los beneficios.\n\n" +
                    "Estos valores te serán devueltos junto con el porcentaje de valorización definido " +
                    "cada año por la Asamblea únicamente cuando te desvincules de PRESENTE y si en el " +
                    "momento del retiro no cuentas con saldos pendientes de pago en tus obligaciones.\n" +
                    "Para más información, consulta nuestros Estatutos y reglamentos en www.presente.com.co");
            d.setCancelable(false);
            d.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = d.show();
            TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.START;
            messageView.setLayoutParams(params);
            messageView.setGravity(Gravity.FILL_HORIZONTAL | Gravity.START);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                messageView.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            }
        }catch (Exception ex){
        }
    }

    @Override
    public void showProducts(List<ResponseProductos> productos) {
        lblNombreProducto.setText(productos.get(0).getN_tipodr());
        DetalleProductoAdapter adapter = new DetalleProductoAdapter(context, productos);
        contenedorProductos.setAdapter(adapter);
        this.mProductos = productos;
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
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_0_STATUS_ACCOUNT_TAG);
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
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_0_STATUS_ACCOUNT_TAG);
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


//    private void mostrarProductos(final ArrayList<ResponseProducto> productos) {
//        lblNombreProducto.setText(productos.get(0).getN_tipodr());
//        DetalleProductoAdapter adapter = new DetalleProductoAdapter(context, productos);
//        contenedorProductos.setAdapter(adapter);
//        this.mProductos = productos;
//        contenedorProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ResponseProducto producto = mProductos.get(position);
//                state.setProducto_ver_movimientos(producto);
//                state.getmTabHost().setCurrentTab(9);
//            }
//        });
//    }


    //Obtiene el estado del mensaje para mostrar al usuario en mis aportes
//    private class obtenerEstadoMensajeMisAportesTask extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Un momento...");
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                return networkHelper.readService(SincroHelper.CONSULTAR_ESTADO_MENSAJE_MISAPORTES);
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
//            procesarResultEstadosMensajeMisAportes(result);
//        }
//    }
//
//    private void procesarResultEstadosMensajeMisAportes(String resultEstadoMensajeMisAportes) {
//        try {
//            String estado = SincroHelper.procesarJsonEstadoMensajeMisAPortes(resultEstadoMensajeMisAportes);
//
//            if (estado.equals("Y")) {
//                AlertDialog.Builder d = new AlertDialog.Builder(context);
//                d.setTitle("Mis Aportes:");
//                d.setIcon(R.mipmap.icon_presente);
//                d.setMessage("El Ahorro Permanente y el Ahorro Aporte Social son ahorros a largo plazo que te " +
//                        "brindan la posibilidad de ser asociado al Fondo de Empleados y disfrutar de los beneficios.\n\n" +
//                        "Estos valores te serán devueltos junto con el porcentaje de valorización definido " +
//                        "cada año por la Asamblea únicamente cuando te desvincules de PRESENTE y si en el " +
//                        "momento del retiro no cuentas con saldos pendientes de pago en tus obligaciones.\n" +
//                        "Para más información, consulta nuestros Estatutos y reglamentos en www.presente.com.co");
//                d.setCancelable(false);
//                d.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                AlertDialog dialog = d.show();
//                TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.START;
//                messageView.setLayoutParams(params);
//                messageView.setGravity(Gravity.FILL_HORIZONTAL | Gravity.START);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    messageView.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
//                }
//            }
//
//        } catch (ErrorTokenException e) {
//            pd.dismiss();
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
//            pd.dismiss();
//            context.makeErrorDialog(ex.getMessage());
//        }
//    }

}
