package solidappservice.cm.com.presenteapp.front.estadocuenta.FragmentStatusAccount;

import static solidappservice.cm.com.presenteapp.tools.constants.Constans.ERROR_CONTACTA_PRESENTE;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.estadocuenta.ProductosAdapter;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestActualizarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestConsultarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseConsultaAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.dto.ProductosPorTipoDR;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.mensajes.request.RequestEnviarMensaje;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.nequi.transfieredinero.ActivityDialogNequiBalance.ActivityDialogNequiBalanceView;
import solidappservice.cm.com.presenteapp.front.popups.PopUp;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.tools.helpers.DialogHelpers;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 24/11/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public class FragmentStatusAccountView extends Fragment implements FragmentStatusAccountContract.View {

    private FragmentStatusAccountPresenter presenter;
    private ActivityBase context;
    private ActivityTabsView tabsContext;
    private GlobalState state;
    private FirebaseAnalytics firebaseAnalytics;
    private ArrayList<ProductosPorTipoDR> tipoDRs;
    private String valorSolicitudAN;

    @BindView(R.id.list_productos)
    ListView list_productos;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de estado de cuenta");
        firebaseAnalytics.logEvent("pantalla_estado_cuenta", params);
        View view = inflater.inflate(R.layout.fragment_statusaccount, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentStatusAccountPresenter(this, new FragmentStatusAccountModel());
        context = (ActivityBase) getActivity();
        tabsContext = (ActivityTabsView) getActivity();
        state = context.getState();
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                state.setProductos(null);
                state.setMovimientos(null);
                state.setSaldoNequi(null);
                fetchSalaryAdvanceMovements();
                pullToRefresh.setRefreshing(false);
            }
        });
        fetchSalaryAdvanceMovements();
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null || state.getUsuario() == null){
            context.salir();
        }
    }

    @OnItemClick(R.id.list_productos)
    public void onItemClickListProducts(AdapterView<?> parent, View view, int position, long id) {
        state.setProductosDetalle(tipoDRs.get(position).getProductos());
        state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_6_STATUS_ACCOUNT_PRODUCTS_TAG);
    }

    @OnClick(R.id.imageReferesh)
    public void onClickRefresh(){
        state.setProductos(null);
        state.setMovimientos(null);
        state.setSaldoNequi(null);
        fetchSalaryAdvanceMovements();
    }

    @Override
    public void fetchSalaryAdvanceMovements(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchSalaryAdvanceMovements(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
            showErrorWithRefresh();
        }
    }

    @Override
    public void processSalaryAdvancePending(Integer idFlujo, String valorSolicitado){
        try{
            this.valorSolicitudAN = valorSolicitado;
            Encripcion encripcion = Encripcion.getInstance();
            presenter.processSalaryAdvancePending(new RequestConsultarAdelantoNomina(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    idFlujo
            ));
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
            showErrorWithRefresh();
        }
    }

    @Override
    public void updateSalaryAdvanceStatus(ResponseConsultaAdelantoNomina consulta){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
            presenter.updateSalaryAdvanceStatus(new RequestActualizarAdelantoNomina(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    consulta.v_k_flujo,
                    "C",
                    consulta.n_error,
                    consulta.k_numdoc,
                    format2.format(format1.parse(consulta.f_primera))
            ));
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
            showErrorWithRefresh();
        }
    }

    @Override
    public void sendSalaryAdvanceNotification(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            String fechaInicio = formatFecha.format(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR , 3);
            String fechaFinal = formatFecha.format(calendar.getTime());
            presenter.sendSalaryAdvanceNotification(new RequestEnviarMensaje(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    fechaInicio,
                    fechaFinal,
                    "Solicitud de adelanto de nómina",
                    "El adelanto de nómina por el valor de $"+valorSolicitudAN+" ha sido exitoso, válida la transacción en los movimientos de tu cuenta de nómina."
            ));
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
            showErrorWithRefresh();
        }
    }

    @Override
    public void fetchAccountStatus(){
        try{
            state.setProductos(null);
            if(state != null && state.getProductos() != null && state.getProductos().size()>0){
                hideCircularProgressBar();
                showSectionAcccountStatus();
                showAccountStatus(state.getProductos());
                fetchNequiBalance();
            }else{
                state.setMovimientos(null);
                Encripcion encripcion = Encripcion.getInstance();
                presenter.fetchAccountStatus(new BaseRequest(
                        encripcion.encriptar(state.getUsuario().getCedula()),
                        state.getUsuario().getToken()
                ));
            }
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
            showErrorWithRefresh();
        }
    }

    @Override
    public void showAccountStatus(List<ResponseProducto> cuentas){
        try {
            state.setProductos(cuentas);
            this.tipoDRs = new ArrayList<>();

            //CREAMOS UN MAP PARA ORGANIZAR LOS PRODUCTOS POR N_TIPODR
            Map<String, ArrayList<ResponseProducto>> mapTipoDRs = new HashMap<>();
            for (ResponseProducto p : cuentas) {
                if (!mapTipoDRs.containsKey(p.getN_tipodr())) {
                    mapTipoDRs.put(p.getN_tipodr(), new ArrayList<ResponseProducto>());
                }
                p.setExpanded(false);
                mapTipoDRs.get(p.getN_tipodr()).add(p);
            }

            //RECORREMOS EL MAP, PARA CREAR LOS OBJETOS ProductoPorTipoDR
            final Iterator<String> i = mapTipoDRs.keySet().iterator();
            while (i.hasNext()) {
                ProductosPorTipoDR p = new ProductosPorTipoDR();
                p.setN_tipodr(i.next());
                p.setProductos(mapTipoDRs.get(p.getN_tipodr()));;
                this.tipoDRs.add(p);
            }
            ProductosAdapter pa = new ProductosAdapter(context, this.tipoDRs);
            list_productos.setAdapter(pa);
        } catch (Exception ex) {
            showDataFetchError("Lo sentimos", "");
            showErrorWithRefresh();
        }
    }

    @Override
    public void fetchNequiBalance() {
        if(state != null && state.isActiveStateNequiBalance()){
            if(state != null && !TextUtils.isEmpty(state.getSaldoNequi())){
                showNequiBalance(state.getSaldoNequi());
                hideCircularProgressBar();
                showSectionAcccountStatus();
            }else{
                presenter.fetchNequiBalance(new BaseRequestNequi(
                        Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
                        state.getUsuario().getToken(),
                        ""
                ));
            }
        }else{
            hideCircularProgressBar();
            showSectionAcccountStatus();
        }
    }

    @Override
    public void fetchAuthorizationNequiBalance(){
        try {
            presenter.fetchAuthorizationNequiBalance(new BaseRequestNequi(
                    Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    ""
            ));
        } catch (Exception ex) {
            if(state != null){
                state.setRefusedAuthorizationNequiBalance(false);
            }
            showNequiBalance("");
        }
    }

    @Override
    public void resultGetAuthorizationNequiBalance(String status){
        switch(status){
            case "0":
                break;
            case "1":
                if(state != null){
                    state.setRefusedAuthorizationNequiBalance(false);
                }
                break;
            case "2":
            case "3":
                if(state != null){
                    state.setRefusedAuthorizationNequiBalance(true);
                }
                break;
            default:
                if(state != null && !state.isRefusedAuthorizationNequiBalance() && !state.isAlreadyOpenDialogNequiBalance()){
                    state.setAlreadyOpenDialogNequiBalance(true);
                    showDialogGetBalanceNequi();
                }
                break;
        }
    }

    @Override
    public void showNequiBalance(String saldoNequi) {
        try {
            if (list_productos != null) {
                if(context.tryParseDouble(saldoNequi)){
                    ArrayList<ResponseProducto> productos = new ArrayList<ResponseProducto>();
                    productos.add(new ResponseProducto(
                            "Nequi",
                            "Nequi general",
                            "Nequi",
                            Double.valueOf(saldoNequi).intValue()
                    ));
                    tipoDRs.add(new ProductosPorTipoDR("Nequi", productos));
                    list_productos.deferNotifyDataSetChanged();
                }
            }
        } catch (Exception ex) {}
    }

    @Override
    public void showDialogGetBalanceNequi(){
        Intent intent = new Intent(context, ActivityDialogNequiBalanceView.class);
        startActivity(intent);
    }

    @Override
    public void showSectionAcccountStatus(){
        pullToRefresh.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSectionAcccountStatus(){
        pullToRefresh.setVisibility(View.GONE);
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
        list_productos.setVisibility(View.GONE);
        layoutCircularProgressBar.setVisibility(View.VISIBLE);
        circularProgressBar.setVisibility(View.GONE);
        textCircularProgressBar.setText("Ha ocurrido un error, inténtalo de nuevo ");
        buttonReferesh.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorTimeOut() {
        String message = ERROR_CONTACTA_PRESENTE;
        if(state != null && state.getMensajesRespuesta() != null && state.getMensajesRespuesta().size()>0){
            for(ResponseMensajesRespuesta rm : state.getMensajesRespuesta()){
                if(rm.getIdMensaje() == 6){
                    message = rm.getMensaje();
                }
            }
        }
        final Dialog dialog = new Dialog(tabsContext);
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
        buttonClose.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void showDataFetchError(String title, String message){
        if(TextUtils.isEmpty(message)){
            message = ERROR_CONTACTA_PRESENTE;
            if(state != null && state.getMensajesRespuesta() != null && state.getMensajesRespuesta().size()>0){
                for(ResponseMensajesRespuesta rm : state.getMensajesRespuesta()){
                    if(rm.getIdMensaje() == 7){
                        message = rm.getMensaje();
                    }
                }
            }
        }
        final Dialog dialog = new Dialog(tabsContext);
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
            }
        });
        dialog.show();
    }

    @Override
    public void showExpiredToken(String message) {
        final Dialog dialog = new Dialog(tabsContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_closedsession);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button buttonClosedSession = (Button) dialog.findViewById(R.id.btnVolverAIngresar);
        buttonClosedSession.setOnClickListener(view -> {
            dialog.dismiss();
            context.salir();
        });
        dialog.show();
    }
}
