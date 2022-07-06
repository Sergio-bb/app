package solidappservice.cm.com.presenteapp.front.convenios.FragmentBuyAgreementsProducts;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Convenio;
import solidappservice.cm.com.presenteapp.entities.convenios.FormaPago;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Producto;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Resumen;
import solidappservice.cm.com.presenteapp.entities.convenios.request.RequestSolicitudProducto;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.ActivityAgreementsView;
import solidappservice.cm.com.presenteapp.front.convenios.FragmentBuyAgreementsProducts.ActivityConfirmBuyAgreementsProducts.ActivityConfirmBuyAgreementsProductsView;
import solidappservice.cm.com.presenteapp.front.convenios.ActivityHtmlViewer.ActivityHtmlView;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;
import solidappservice.cm.com.presenteapp.tools.helpers.DialogHelpers;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 31/07/2018.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */

public class FragmentBuyAgreementsProductsView extends Fragment implements FragmentBuyAgreementsProductsContract.View{

    private FragmentBuyAgreementsProductsPresenter presenter;
    private ActivityAgreementsView baseView;
    private ActivityBase context;
    private Dialog pd;
    private GlobalState state;
    private Resumen resumen;
    private Producto producto;
    private Convenio convenio;
    private RequestSolicitudProducto solicitudProducto;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.tv_nombre_convenio)
    TextView tv_nombre_convenio;
    @BindView(R.id.tv_product_name)
    TextView tv_product_name;
    @BindView(R.id.tv_product_price)
    TextView tv_product_price;
    @BindView(R.id.spinner_forma_pago)
    Spinner spinner_forma_pago;
    @BindView(R.id.et_correo_electronico)
    EditText et_correo_electronico;
    @BindView(R.id.et_numer_celular)
    EditText et_numer_celular;
    @BindView(R.id.tv_legal)
    TextView tv_legal;
    @BindView(R.id.tv_title_condiciones_y_restriciones)
    TextView tv_title_condiciones_y_restriciones;
    @BindView(R.id.chk_acepto_terminos)
    CheckBox chk_acepto_terminos;
    @BindView(R.id.btnComprar)
    Button btnComprar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de convenio compra producto");
        firebaseAnalytics.logEvent("pantalla_convenio_compra_producto", params);
        View view = inflater.inflate(R.layout.fragment_agreements_buyproduct, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentBuyAgreementsProductsPresenter(this, new FragmentBuyAgreementsProductsModel());
        context = (ActivityBase) getActivity();
        baseView = (ActivityAgreementsView) getActivity();
        state = baseView.getState();
        resumen = state.getResumen();
        if (baseView != null) {
            state.haveFinishedBuy(false);
            baseView.btn_back.setVisibility(View.VISIBLE);
            baseView.header.setImageResource(R.drawable.logo_internal);
        }
        showProduct();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.btnComprar)
    public void onClickBuy(View v) {
        startBuyingProduct();
    }

    @OnCheckedChanged(R.id.chk_acepto_terminos)
    public void onCheckedChangedTyC(CompoundButton buttonView, boolean isChecked) {
        enableButtonComprar(isChecked);
    }

    @OnItemSelected(R.id.spinner_forma_pago)
    public void onItemSelectedFormaPago(AdapterView<?> parent, View view, int position, long id) {
        if(producto != null && producto.getFormasPago() != null && producto.getFormasPago().size() > 0){
            configureLegal(position);
        }else{
            tv_legal.setText("");
            chk_acepto_terminos.setVisibility(View.GONE);
            tv_title_condiciones_y_restriciones.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProduct(){
        solicitudProducto = null;
        if(resumen != null){
            String defaultEmailAsociado = resumen.getEmailAsociado();
            producto = resumen.getProductoSeleccionado();
            convenio = resumen.getConvenioSeleccionado();
            tv_nombre_convenio.setText(convenio.getNombre());
            tv_product_name.setText(producto.getNombre());
            tv_product_price.setText(producto.getValor());

            ArrayList<FormaPago> formaPagos =  producto.getFormasPago();
            ArrayAdapter<FormaPago> spinnerArrayAdapter = new ArrayAdapter<>
                    (baseView, android.R.layout.simple_spinner_item, formaPagos);

            spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                    .simple_spinner_dropdown_item);

            spinner_forma_pago.setAdapter(spinnerArrayAdapter);

            et_numer_celular.setHint(producto.getEtiquetaCampoCelular());
            configureLegal(0);

            if(!TextUtils.isEmpty(defaultEmailAsociado)){
                et_correo_electronico.setText(defaultEmailAsociado);
            }
        }
    }

    @Override
    public void configureLegal(int positionOfFormaPago){
        FormaPago fp = producto.getFormasPago().get(positionOfFormaPago);
        String legal = fp.getLegal();
        if(!TextUtils.isEmpty(legal)){
            tv_legal.setText(legal);
            chk_acepto_terminos.setVisibility(View.VISIBLE);
            chk_acepto_terminos.setChecked(false);
            tv_title_condiciones_y_restriciones.setVisibility(View.VISIBLE);
            enableButtonComprar(false);
        }else{
            tv_legal.setText("");
            chk_acepto_terminos.setVisibility(View.GONE);
            tv_title_condiciones_y_restriciones.setVisibility(View.GONE);
            enableButtonComprar(true);
        }
    }


    @Override
    public void enableButtonComprar(boolean enable){
        if(enable){
            btnComprar.setEnabled(true);
            btnComprar.setBackground(getResources().getDrawable(R.drawable.background_button_rounded_orange));

        }else{
            btnComprar.setEnabled(false);
            btnComprar.setBackground(getResources().getDrawable(R.drawable.background_button_rounded_gry_ligth));
        }
    }

    @Override
    public void  startBuyingProduct(){
        if(solicitudProducto == null){
            solicitudProducto = new RequestSolicitudProducto();
            solicitudProducto.setIdUso(UUID.randomUUID().toString());
        }

        if(producto != null && validateData()){
            String correo = et_correo_electronico.getText().toString();
            String celular = et_numer_celular.getText().toString();
            FormaPago formaPago = (FormaPago) spinner_forma_pago.getSelectedItem();

            if(!TextUtils.isEmpty(solicitudProducto.getCelular()) &&
                    !celular.equals(solicitudProducto.getCelular())){
                solicitudProducto.setIdUso(UUID.randomUUID().toString());

            }else if(!TextUtils.isEmpty(solicitudProducto.getIdFormaPago()) &&
                    !formaPago.getId().equals(solicitudProducto.getIdFormaPago())){
                solicitudProducto.setIdUso(UUID.randomUUID().toString());
            }

            solicitudProducto.setIdProducto(String.valueOf(producto.getId()));
            solicitudProducto.setValor(producto.getValor());
            solicitudProducto.setIdFormaPago(formaPago.getId());
            solicitudProducto.setEmail(correo);
            solicitudProducto.setCelular(celular);
            solicitudProducto.setEqtiquetaCelular(producto.getEtiquetaCampoCelular());
            solicitudProducto.setBeneficio(producto.getNombre());
            solicitudProducto.setNombreFormaPago(formaPago.getNombre());
            solicitudProducto.setNombreProducto(convenio.getNombre());

            state.getResumen().setSolicitudProducto(solicitudProducto);
            Intent intent = new Intent(getContext(), ActivityConfirmBuyAgreementsProductsView.class);
            startActivityForResult(intent, GlobalState.CONFIRM_BUY);
        }
    }

    @Override
    public boolean validateData(){
        try {
            String correo = et_correo_electronico.getText().toString();
            String celular = et_numer_celular.getText().toString();

            if(TextUtils.isEmpty(correo)){
                et_correo_electronico.setError("Campo requerido");
                return false;
            }

            if(TextUtils.isEmpty(celular)){
                et_numer_celular.setError("Campo requerido");
                return false;
            }

            if(!ActivityBase.validateEmail(et_correo_electronico.getText().toString())){
                et_correo_electronico.setError("Ingrese un email válido");
                return false;
            }

            if(!ActivityBase.validateCellPhone(et_numer_celular.getText().toString())){
                et_numer_celular.setError("Ingrese un número de celular válido");
                return false;
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public void buyProduct(){
        try{
            presenter.buyProduct(new BaseRequest(
                            state.getUsuario().getCedula(),
                            state.getUsuario().getToken()
                    ),
                    solicitudProducto
            );

        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void showResultBuyProduct(String html){
        Intent intent_condiciones = new Intent(getContext(), ActivityHtmlView.class);
        intent_condiciones.putExtra("html", html);
        startActivityForResult(intent_condiciones, GlobalState.VIEW_RESULT_AGREEMENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GlobalState.CONFIRM_BUY && resultCode == Activity.RESULT_OK){
            buyProduct();
        }
        else if(requestCode == GlobalState.VIEW_RESULT_AGREEMENT){
            if(resultCode == Activity.RESULT_OK){
                state.haveFinishedBuy(true);
                baseView.onBackPressed();
            }else if(resultCode == GlobalState.CLOSED_SESSION){
                baseView.setResult(GlobalState.CLOSED_SESSION);
            }
        }
    }

    @Override
    public void showProgressDialog(String message) {
        pd = new Dialog(baseView);
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.setCanceledOnTouchOutside(false);
        pd.setContentView(R.layout.pop_up_loading);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView contentMessage =  pd.findViewById(R.id.lbl_content_message);
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
                baseView.onBackPressed();
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
        final Dialog dialog = new Dialog(baseView);
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
                baseView.onBackPressed();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void showExpiredToken(String message) {
        final Dialog dialog = new Dialog(baseView);
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
                baseView.salir();
            }
        });
        dialog.show();
    }
}
