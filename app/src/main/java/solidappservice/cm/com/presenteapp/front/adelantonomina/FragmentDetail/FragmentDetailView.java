package solidappservice.cm.com.presenteapp.front.adelantonomina.FragmentDetail;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestInsertarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestLogs;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestProcesarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 19/10/2020.
 */
public class FragmentDetailView extends Fragment implements FragmentDetailContract.View{

    private FragmentDetailPresenter presenter;
    private ActivityBase context;
    private ActivityBase baseView;
    private GlobalState state;
    private Dialog pd;
    private FirebaseAnalytics firebaseAnalytics;

    private String valorsolicitado;
    private String valorComision;
    private Integer numeroflujo;
    private String aceptacionterminos;
    private String fechaaceptacion;

    @BindView(R.id.tvValorSolicitado)
    TextView tvValorSolicitado;
    @BindView(R.id.tvValorComision)
    TextView tvValorComision;
    @BindView(R.id.circularProgressBarValue)
    ProgressBar circularProgressBarValue;
    @BindView(R.id.cbTyCAdelanto)
    CheckBox cbTyCAdelanto;
    @BindView(R.id.btnAdelantarNomina)
    Button btnAdelantarNomina;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de adelanto nomina detalle");
        firebaseAnalytics.logEvent("pantalla_adelanto_nomina_detalle", params);
        View view = inflater.inflate(R.layout.fragment_salaryadvance_detail, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentDetailPresenter(this, new FragmentDetailModel());
        context = (ActivityBase) getActivity();
        baseView = (ActivityTabsView) getActivity();
        state = context.getState();
        DecimalFormat formato = new DecimalFormat("#,###");
        valorsolicitado = formato.format(Integer.parseInt(state.getValorSolicitado()));
        tvValorSolicitado.setText("Valor solicitado a consignar: $"+valorsolicitado);
        fetchCommissionValue();
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null || state.getUsuario() == null){
            context.salir();
        }
    }

    @OnClick(R.id.btnAdelantarNomina)
    public void onClickAdelantarNomina(){
        fetchRegisterSalaryAdvance();
    }

    @OnCheckedChanged(R.id.cbTyCAdelanto)
    public void onCheckedChangedAceptoTerminos(CompoundButton buttonView, boolean isChecked){
        if(isChecked){
            btnAdelantarNomina.setEnabled(true);
            aceptacionterminos = "S";
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            fechaaceptacion = format.format(date);
        }else{
            aceptacionterminos = "N";
            fechaaceptacion = "";
            btnAdelantarNomina.setEnabled(false);
        }
    }

    @Override
    public void fetchCommissionValue(){
        try{
            presenter.fetchCommissionValue();
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void showCommissionValue(String valorComision){
        DecimalFormat formato = new DecimalFormat("#,###");
        this.valorComision = formato.format(Integer.parseInt(valorComision));
        tvValorComision.setText(String.format("Costo de la transacción: $%s", this.valorComision));
    }

    @Override
    public void showCircularProgressBarValue() {
        circularProgressBarValue.setVisibility(View.VISIBLE);
        tvValorComision.setVisibility(View.GONE);
    }
    @Override
    public void hideCircularProgressBarValue(){
        circularProgressBarValue.setVisibility(View.GONE);
        tvValorComision.setVisibility(View.VISIBLE);
    }

    @Override
    public void fetchRegisterSalaryAdvance(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date fechasolicitud = new Date();
            Integer valorfinal = Integer.parseInt(valorsolicitado.replaceAll("[,.]", ""))+Integer.parseInt(valorComision.replaceAll("[,.]", ""));
            presenter.fetchRegisterSalaryAdvance(new RequestInsertarAdelantoNomina(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    format.format(fechasolicitud),
                    Integer.parseInt(valorsolicitado.replaceAll("[,.]", "")),
                    valorfinal,
                    state.getTopes().getV_cupo(),
                    "E",
                    "",
                    null,
                    aceptacionterminos,
                    fechaaceptacion,
                    context.getIP()
            ));
        }catch(Exception ex){
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void fetchProcessSalaryAdvance(String numeroTransaccion){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date fechasolicitd = new Date();
            Integer valorfinal = Integer.parseInt(valorsolicitado.replaceAll("[,.]", ""))+Integer.parseInt(valorComision.replaceAll("[,.]", ""));
            presenter.fetchProcessSalaryAdvance(new RequestProcesarAdelantoNomina(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    valorfinal.toString(),
                    format.format(fechasolicitd),
                    numeroTransaccion
            ));
        } catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void enterLogs(String accion, String descripcion){
        try{
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Encripcion encripcion = Encripcion.getInstance();
            presenter.enterLogs(new RequestLogs(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    accion,
                    descripcion,
                    format.format(new Date())
            ));
        }catch(Exception ex){
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void showSuccessfulSalaryAdvance(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_success);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView titleMessage = (TextView) dialog.findViewById(R.id.titleSuccess);
        titleMessage.setText("Solicitud Enviada");
        TextView contentMessage = (TextView) dialog.findViewById(R.id.contentSuccess);
        contentMessage.setText("Tu solicitud ha sido enviada con éxito. El valor que te será consignado en tu cuenta de nómina es de $" + valorsolicitado +
                " valida la transacción en los movimientos de tu cuenta de nómina en unos minutos.");
        ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.buttonClose);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state.setRequisitos(null);
                state.setTopes(null);
                state.setValorSolicitado(null);
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void showProgressDialog(String message) {
        pd = new Dialog(context);
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.setCanceledOnTouchOutside(false);
        pd.setContentView(R.layout.pop_up_loading);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView contentMessage = (TextView) pd.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
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
                state.setRequisitos(null);
                state.setTopes(null);
                state.setValorSolicitado(null);
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void showDataFetchError(String title, String message) {
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
                state.setRequisitos(null);
                state.setTopes(null);
                state.setValorSolicitado(null);
                state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_1_TRANSACTIONS_MENU_TAG);
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
