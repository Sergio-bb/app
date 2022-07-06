package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.MovementsCards.FragmentProductsCard;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.tarjetapresente.TarjetasAdapter;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProductos;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseTarjeta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 07/12/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public class FragmentProductsCardView extends Fragment implements FragmentProductsCardContract.View{

    private FragmentProductsCardPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private ProgressDialog pd;
    private FirebaseAnalytics firebaseAnalytics;
    private List<ResponseTarjeta> tarjetas;
    private ResponseTarjeta tarjetaSeleccionada;

    @BindView(R.id.listProductos)
    ListView listProductos;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de tarjeta productos");
        firebaseAnalytics.logEvent("pantalla_tarjeta_productos", params);
        View view = inflater.inflate(R.layout.fragment_presentecard_products, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentProductsCardPresenter(this, new FragmentProductsCardModel());
        context = (ActivityBase) getActivity();
        state = context.getState();
        pd = new ProgressDialog(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if (state == null || state.getUsuario() == null) {
            context.salir();
        } else {
            if(state.getTarjetas() == null || state.getTarjetas().size() <= 0){
//                Usuario usuario = state.getUsuario();
//                new TarjetaTask().execute(usuario.cedula, usuario.token);
                fetchPresenteCards();
            }else{
                showPresenteCards(state.getTarjetas());
//                mostrarTarjetas(state.getTarjetas());
            }
        }
    }

    @OnItemClick(R.id.listProductos)
    public void OnItemClickListProductos(AdapterView<?> parent, View view, int position, long id){
        if (tarjetas != null && tarjetas.size() > 0) {
            tarjetaSeleccionada = tarjetas.get(position);
            List<ResponseProductos> productos = state.getProductos();
            if (productos != null && productos.size() > 0) {
//                buscarProducto(tarjeta, productos);
                showAccountsPresenteCard(productos);
            } else {
                GlobalState state = context.getState();
                if (state == null || state.getUsuario() == null) {
                    context.salir();
                } else {
                    fetchAccounts();
//                    Usuario usuario = state.getUsuario();
//                    new EstadoCuentaTask(tarjeta).execute(usuario.cedula, usuario.token);
                }
            }
        }
    }

    @Override
    public void fetchPresenteCards(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchPresenteCards(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void showPresenteCards(List<ResponseTarjeta> tarjetas){
        TarjetasAdapter adapter = new TarjetasAdapter(context, tarjetas);
        this.tarjetas = tarjetas;
        listProductos.setAdapter(adapter);
        state.setTarjetas(tarjetas);
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
            showDataFetchError("");
        }
    }

    @Override
    public void showAccountsPresenteCard(List<ResponseProductos> productos){
        state.setProductos(productos);
        if (productos != null && productos.size() > 0) {
            ResponseProductos productoSeleccionado = null;
            if(!tarjetaSeleccionada.getA_numcta().equalsIgnoreCase("X")){
                for (ResponseProductos producto : productos) {
                    if (producto.getA_numdoc().equals(tarjetaSeleccionada.getA_numcta())) {
                        productoSeleccionado = producto;
                        break;
                    }
                }
            }else{
                for (ResponseProductos producto : productos) {
                    if (producto.getA_tipodr().equals(tarjetaSeleccionada.getA_tipodr()) && producto.getA_numdoc().equals(tarjetaSeleccionada.getA_obliga())) {
                        productoSeleccionado = producto;
                        break;
                    }
                }
            }
            if (productoSeleccionado == null) {
                context.makeErrorDialog("Actualmente su tarjeta no presenta movimientos");
            } else {
                context.getState().setProductoSeleccionado(productoSeleccionado);
                context.getState().getmTabHost().setCurrentTab(ActivityTabsView.TAB_12_PRESENTE_CARD_MOVEMENTS_PRODUCT_TAG);
            }
        } else {
            context.makeErrorDialog("Actualmente su tarjeta no presenta movimientos");
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
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_2_PRESENTE_CARD_MENU_TAG);
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
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_2_PRESENTE_CARD_MENU_TAG);
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


//    private void buscarProducto(ResponseTarjeta tarjeta, ArrayList<Producto> productos) {
//        Producto productoSeleccionado = null;
//
//        if(!tarjeta.getA_numcta().equalsIgnoreCase("X")){
//            for (Producto producto : productos) {
//                if (producto.a_numdoc.equals(tarjeta.getA_numcta())) {
//                    productoSeleccionado = producto;
//                    break;
//                }
//            }
//        }else{
//            for (Producto producto : productos) {
//                if (producto.a_tipodr.equals(tarjeta.getA_tipodr()) && producto.a_numdoc.equals(tarjeta.getA_obliga())) {
//                    productoSeleccionado = producto;
//                    break;
//                }
//            }
//        }
//
//
////        if (productoSeleccionado == null || productoSeleccionado.movimientos.size() <= 0) {
////            context.makeErrorDialog("Actualmente su tarjeta no presenta movimientos");
////        } else {
//            context.getState().setProducto_ver_movimientos(productoSeleccionado);
//            context.getState().getmTabHost().setCurrentTab(14);
////        }
//    }
//
//
//    private void mostrarTarjetas(List<ResponseTarjeta> tarjetas) {
//        try {
//            TarjetasAdapter adapter = new TarjetasAdapter(context, tarjetas);
//            this.tarjetas = tarjetas;
//            listProductos.setAdapter(adapter);
//        } catch (Exception e) {
//            context.makeErrorDialog("Error cargando las tarjetas del usuario");
//        }
//    }
//
//    class TarjetaTask extends AsyncTask<String, String, String> {
//
//        String cedula = null;
//        String token = null;
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Obteniendo tarjetas...");
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
//                return networkHelper.writeService(param, SincroHelper.TARJETAS);
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
//            procesarJsonTarjetas(result);
//        }
//    }
//
//    private void procesarJsonTarjetas(String jsonRespuesta) {
//        try {
//            ArrayList<ResponseTarjeta> tarjetas = SincroHelper.procesarJsonTarjetas(jsonRespuesta);
//            mostrarTarjetas(tarjetas);
//            context.getState().setTarjetas(tarjetas);
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
//    class EstadoCuentaTask extends AsyncTask<String, String, String> {
//
//        String cedula = null;
//        String token = null;
//        ResponseTarjeta tarjeta = null;
//
//        EstadoCuentaTask(ResponseTarjeta tarjeta){
//            this.tarjeta = tarjeta;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Consultando Cuentas...");
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
////                return networkHelper.writeService(param, SincroHelper.ESTADO_CUENTA);
//                return networkHelper.writeService(param, SincroHelper.CONSULTAR_CUENTAS);
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
//            procesarJsonProductos(result, tarjeta);
//        }
//    }
//
//    private void procesarJsonProductos(String result, ResponseTarjeta tarjeta) {
//        try {
//            ArrayList<Producto> productos = SincroHelper.procesarJsonEstadoCuenta(result);
//            context.getState().setProductos(productos);
//            if (productos != null && productos.size() > 0) {
//                buscarProducto(tarjeta, productos);
//            } else {
//                context.makeErrorDialog("Actualmente su tarjeta no presenta movimientos");
//            }
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