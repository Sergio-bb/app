package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.MovementsCards.FragmentMovementsProductsCard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.estadocuenta.MovimientoAdapter;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseMovimientoProducto;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.request.RequestMovimientosProducto;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseTarjeta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 02/12/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public class FragmentMovementsProductsCardView extends Fragment implements FragmentMovementsProductsCardContract.View{

    private FragmentMovementsProductsCardPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.lblMovimientos)
    TextView lblMovimientos;
    @BindView(R.id.lblSaldoValue)
    TextView lblSaldoValue;
    @BindView(R.id.list_movimientos)
    ListView list_movimientos;

    @BindView(R.id.layout_circular_progress_bar)
    LinearLayout layoutCircularProgressBar;
    @BindView(R.id.circular_progress_bar)
    ProgressBar circularProgressBar;
    @BindView(R.id.text_circular_progress_Bar)
    TextView textCircularProgressBar;
    @BindView(R.id.imageReferesh)
    ImageView buttonReferesh;
    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;

    @BindView(R.id.contentMovementsProductsCard)
    LinearLayout contentMovementsProductsCard;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de movimientos tarjeta");
        firebaseAnalytics.logEvent("pantalla_movimientos_tarjeta", params);
        View view = inflater.inflate(R.layout.fragment_presentecard_movementsproducts, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentMovementsProductsCardPresenter(this, new FragmentMovementsProductsCardModel());
        context = (ActivityBase) getActivity();
        state = context.getState();
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchMovementsPresenteCards(state.getProductoSeleccionado());
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchAccounts();
    }

    @OnClick(R.id.imageReferesh)
    public void onClickRefresh(){
        fetchMovementsPresenteCards(state.getProductoSeleccionado());
    }

    @Override
    public void showTitleProductPresenteCards(){
        try{
            ResponseProducto producto = state.getProductoSeleccionado();
            lblMovimientos.setText(String.format("%s:  %s", producto.getN_produc(), producto.getA_numdoc()));
            lblSaldoValue.setText(context.getMoneda(producto.getV_saldo()));
        }catch (Exception ex){
            lblSaldoValue.setText("");
        }
    }

    @Override
    public void fetchAccounts(){
        try{
            if (state != null && state.getProductos() != null && state.getProductos().size() > 0) {
                showAccountsPresenteCard(state.getProductos());
            } else {
                Encripcion encripcion = Encripcion.getInstance();
                presenter.fetchAccounts(new BaseRequest(
                        encripcion.encriptar(state.getUsuario().getCedula()),
                        state.getUsuario().getToken()
                ));
            }
        }catch (Exception ex){
            showDialogError("Lo sentimos", "");
            showErrorWithRefresh();
        }
    }

    @Override
    public void showAccountsPresenteCard(List<ResponseProducto> productos){
        try{
            state.setProductos(productos);
            if (productos != null && productos.size() > 0) {
                ResponseTarjeta tarjetaSeleccionada = state.getTarjetaSeleccionada();
                ResponseProducto productoSeleccionado = null;
                if(!tarjetaSeleccionada.getA_numcta().equalsIgnoreCase("X")){
                    for (ResponseProducto producto : productos) {
                        if (producto.getA_numdoc().equals(tarjetaSeleccionada.getA_numcta())) {
                            productoSeleccionado = producto;
                            break;
                        }
                    }
                }else{
                    for (ResponseProducto producto : productos) {
                        if (producto.getA_tipodr().equals(tarjetaSeleccionada.getA_tipodr()) && producto.getA_numdoc().equals(tarjetaSeleccionada.getA_obliga())) {
                            productoSeleccionado = producto;
                            break;
                        }
                    }
                }
                if (productoSeleccionado == null) {
                    pullToRefresh.setVisibility(View.GONE);
                    layoutCircularProgressBar.setVisibility(View.VISIBLE);
                    circularProgressBar.setVisibility(View.GONE);
                    textCircularProgressBar.setText("No hay productos disponibles");
                    buttonReferesh.setVisibility(View.VISIBLE);
                } else {
                    state.setProductoSeleccionado(productoSeleccionado);
                    fetchMovementsPresenteCards(productoSeleccionado);
                }
            } else {
                contentMovementsProductsCard.setVisibility(View.GONE);
                layoutCircularProgressBar.setVisibility(View.VISIBLE);
                circularProgressBar.setVisibility(View.GONE);
                textCircularProgressBar.setText("No hay movimientos");
                buttonReferesh.setVisibility(View.VISIBLE);
            }
        }catch (Exception ex){
            showDialogError("Lo sentimos", "");
            showErrorWithRefresh();
        }
    }

    @Override
    public void fetchMovementsPresenteCards(ResponseProducto producto){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchMovementsPresenteCards(new RequestMovimientosProducto(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    producto.getK_tipcuent(),
                    producto.getA_tipodr(),
                    producto.getA_numdoc()
            ));
        }catch (Exception ex){
            showDialogError("Lo sentimos", "");
            showErrorWithRefresh();
        }
    }

    @Override
    public void showMovementsPresenteCards(List<ResponseMovimientoProducto> movimientos){
        try{
            if(movimientos != null && movimientos.size() > 0){
                MovimientoAdapter adapter = new MovimientoAdapter(context, movimientos);
                list_movimientos.setAdapter(adapter);
            }else{
                contentMovementsProductsCard.setVisibility(View.GONE);
                layoutCircularProgressBar.setVisibility(View.VISIBLE);
                circularProgressBar.setVisibility(View.GONE);
                textCircularProgressBar.setText("No hay movimientos");
                buttonReferesh.setVisibility(View.VISIBLE);
            }
        }catch (Exception ex){
            showDialogError("Lo sentimos", "");
            showErrorWithRefresh();
        }
    }

    @Override
    public void showSectionMovementsPresenteCards(){
        contentMovementsProductsCard.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideSectionMovementsPresenteCards(){
        contentMovementsProductsCard.setVisibility(View.GONE);
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
        contentMovementsProductsCard.setVisibility(View.GONE);
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
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_2_PRESENTE_CARD_MENU_TAG);
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
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_2_PRESENTE_CARD_MENU_TAG);
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
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_2_PRESENTE_CARD_MENU_TAG);
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




//    private void mostrarproducto(Producto producto){
//        lblMovimientos.setText(producto.n_produc + ":  " + producto.a_numdoc);
//        lblSaldoValue.setText(context.getMoneda(producto.v_saldo));
//
//        MovimientoAdapter adapter = new MovimientoAdapter(context, producto.movimientos);
//        list_movimientos.setAdapter(adapter);
//    }


//    private void mostrarproducto(Producto producto){
//        try{
//            lblMovimientos.setText(producto.n_produc + ":  " + producto.a_numdoc);
//            lblSaldoValue.setText(context.getMoneda(producto.v_saldo));
//
//            GlobalState state = context.getState();
//            Usuario usuario = state.getUsuario();
//            Encripcion encripcion = Encripcion.getInstance();
//            JSONObject param = new JSONObject();
//            param.put("cedula", encripcion.encriptar(usuario.cedula));
//            param.put("token", usuario.token);
//            param.put("k_tipcuent", producto.k_tipcuent);
//            param.put("v_tipodr", producto.a_tipodr);
//            param.put("v_numdoc",producto.a_numdoc);
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
//            ArrayList<Movimiento> listaMovimientos = SincroHelper.procesarJsonMovimientosCuenta(jsonRespuesta);
//
//            if(listaMovimientos != null && listaMovimientos.size() > 0){
//                MovimientoAdapter adapter = new MovimientoAdapter(context, listaMovimientos);
//                list_movimientos.setAdapter(adapter);
//            }else{
//                AlertDialog.Builder d = new AlertDialog.Builder(context);
//                d.setTitle(context.getResources().getString(R.string.app_name));
//                d.setIcon(R.mipmap.icon_presente);
//                d.setMessage("No hay ningún movimiento cargado para este producto");
//                d.setCancelable(false);
//                d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        context.getState().getmTabHost().setCurrentTab(15);
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

