package solidappservice.cm.com.presenteapp.front.estadocuenta.FragmentProducts;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.estadocuenta.DetalleProductoAdapter;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
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
    private List<ResponseProducto> mProductos;

    @BindView(R.id.contenedorProductos)
    ListView contenedorProductos;
    @BindView(R.id.lblNombreProducto)
    TextView lblNombreProducto;
    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;

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
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                state.setProductos(null);
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_0_STATUS_ACCOUNT_TAG);
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(state != null){
            context = (ActivityBase) getActivity();
            state = context.getState();
        }
        if (state.getProductosDetalle() != null && state.getProductosDetalle().size() > 0 &&
                state.getProductosDetalle().get(0).getN_tipodr() != null && state.getProductosDetalle().get(0).getN_tipodr().equals("Mis Aportes")) {
            fetchStatusMessageMisAportes();
        }
        List<ResponseProducto> productos = state.getProductosDetalle();
        if (productos != null && productos.size() > 0) {
//            mostrarProductos(productos);
            showProducts(productos);
        }
    }

    @OnItemClick(R.id.contenedorProductos)
    public void onItemClickProductos(AdapterView<?> parent, View view, int position, long id) {
        ResponseProducto producto = mProductos.get(position);
        state.setProductoSeleccionado(producto);
        if(producto.getA_numdoc() != null && !producto.getA_numdoc().equals("Nequi general")){
            state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_7_STATUS_ACCOUNT_MOVEMENTS_PRODUCT_TAG);
        }
    }

    @Override
    public void fetchStatusMessageMisAportes(){
        try{
            presenter.fetchStatusMessageMisAportes();
        }catch (Exception ex){}
    }

    @SuppressLint("WrongConstant")
    @Override
    public void showMessageMisAportes(){
        try{
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.pop_up_informative);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView titleMessage = (TextView) dialog.findViewById(R.id.lbl_title_message);
            titleMessage.setText("Mis Aportes:");
            TextView contentMessage = (TextView) dialog.findViewById(R.id.lbl_content_message);
            contentMessage.setText("El Ahorro Permanente y el Ahorro Aporte Social son ahorros a largo plazo que te " +
                    "brindan la posibilidad de ser asociado al Fondo de Empleados y disfrutar de los beneficios.\n\n" +
                    "Estos valores te serán devueltos junto con el porcentaje de valorización definido " +
                    "cada año por la Asamblea únicamente cuando te desvincules de PRESENTE y si en el " +
                    "momento del retiro no cuentas con saldos pendientes de pago en tus obligaciones.\n" +
                    "Para más información, consulta nuestros Estatutos y reglamentos en www.presente.com.co");
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.START;
            contentMessage.setLayoutParams(params);
            contentMessage.setGravity(Gravity.FILL_HORIZONTAL | Gravity.START);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                contentMessage.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            }
            ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.button_close);
            buttonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }catch (Exception ex){
        }
    }

    @Override
    public void showProducts(List<ResponseProducto> productos) {
        lblNombreProducto.setText(productos.get(0).getN_tipodr());
        DetalleProductoAdapter adapter = new DetalleProductoAdapter(context, productos);
        contenedorProductos.setAdapter(adapter);
        this.mProductos = productos;
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
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_0_STATUS_ACCOUNT_TAG);
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
