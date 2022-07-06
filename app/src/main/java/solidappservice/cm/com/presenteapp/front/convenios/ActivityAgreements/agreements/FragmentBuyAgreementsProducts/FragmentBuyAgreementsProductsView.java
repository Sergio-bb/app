package solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.agreements.FragmentBuyAgreementsProducts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.agreements.FragmentBuyAgreementsProducts.ActivityConfirmBuyProduct.ActivityConfirmBuyProductView;
import solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.agreements.ActivityHtmlViewer.ActivityHtmlViewer;
import solidappservice.cm.com.presenteapp.rest.retrofit.apiconvenios.ConveniosRestClient;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 31/07/2018.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */

public class FragmentBuyAgreementsProductsView extends Fragment implements FragmentBuyAgreementsProductsContract.View{

    private FragmentBuyAgreementsProductsPresenter presenter;
    private ActivityAgreementsView context;
    private ActivityBase activityBase;
    private ProgressDialog pd;
    private GlobalState state;
    private Resumen resumen;
    private Producto producto;
    private Convenio convenio;
    private RequestSolicitudProducto solicitudProducto;
    private FirebaseAnalytics firebaseAnalytics;

    private final static int REQ_CONFIRM = 2;
    private final static int REQ_FINISH = 3;

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
        activityBase = (ActivityBase) getActivity();
        context = (ActivityAgreementsView) getActivity();
        state = context.getState();
        pd = new ProgressDialog(context);
        resumen = state.getResumen();
        if (context != null) {
            state.haveFinishedBuy(false);
            context.btn_back.setVisibility(View.VISIBLE);
            context.header.setImageResource(R.drawable.logo_internal);
            context.btnSalir.setVisibility(View.VISIBLE);
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
                    (context, android.R.layout.simple_spinner_item, formaPagos);

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
            btnComprar.setBackgroundColor(getResources().getColor(R.color.orange));
        }else{
            btnComprar.setEnabled(false);
            btnComprar.setBackgroundColor(getResources().getColor(R.color.gris));
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
            Intent intent = new Intent(getContext(), ActivityConfirmBuyProductView.class);
            startActivityForResult(intent, REQ_CONFIRM);
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
            showDataFetchError("");
        }
    }

    @Override
    public void showResultBuyProduct(String html){
        Intent intent_condiciones = new Intent(getContext(), ActivityHtmlViewer.class);
        intent_condiciones.putExtra("html", html);
        startActivityForResult(intent_condiciones,  REQ_FINISH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_CONFIRM && resultCode == Activity.RESULT_OK){
            buyProduct();
        }
        else if(requestCode == REQ_FINISH){// && resultCode == Activity.RESULT_OK){
            state.haveFinishedBuy(true);
            context.onBackPressed();
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
                context.onBackPressed();
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
                context.onBackPressed();
                dialog.dismiss();
            }
        });
        d.show();
    }

    @Override
    public void showExpiredToken(String message) {
        if(message == ConveniosRestClient.TOKEN_EXCEPTION){
            message = "Por favor ingrese nuevamente";
        }
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

//    private void loadProducto(){
//        solicitudProducto = null;
//        if(resumen != null){
//            String defaultEmailAsociado = resumen.getEmailAsociado();
//            producto = resumen.getProductoSeleccionado();
//            convenio = resumen.getConvenioSeleccionado();
//            tv_nombre_convenio.setText(convenio.getNombre());
//            tv_product_name.setText(producto.getNombre());
//            tv_product_price.setText(producto.getValor());
//
//            ArrayList<FormaPago> formaPagos =  producto.getFormasPago();
//            ArrayAdapter<FormaPago> spinnerArrayAdapter = new ArrayAdapter<>
//                    (context, android.R.layout.simple_spinner_item, formaPagos);
//
//            spinnerArrayAdapter.setDropDownViewResource(android.R.layout
//                    .simple_spinner_dropdown_item);
//
//            spinner_forma_pago.setAdapter(spinnerArrayAdapter);
//
//            et_numer_celular.setHint(producto.getEtiquetaCampoCelular());
//            configLegal(0);
//
//            if(!TextUtils.isEmpty(defaultEmailAsociado)){
//                et_correo_electronico.setText(defaultEmailAsociado);
//            }
//        }
//    }
//
//    private void configLegal(int positionOfFormaPago){
//        FormaPago fp = producto.getFormasPago().get(positionOfFormaPago);
//        String legal = fp.getLegal();
//        if(!TextUtils.isEmpty(legal)){
//            tv_legal.setText(legal);
//            chk_acepto_terminos.setVisibility(View.VISIBLE);
//            chk_acepto_terminos.setChecked(false);
//            tv_title_condiciones_y_restriciones.setVisibility(View.VISIBLE);
//            enableBtnComprar(false);
//        }else{
//            tv_legal.setText("");
//            chk_acepto_terminos.setVisibility(View.GONE);
//            tv_title_condiciones_y_restriciones.setVisibility(View.GONE);
//            enableBtnComprar(true);
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(requestCode == REQ_CONFIRM && resultCode == Activity.RESULT_OK){
//            comprar();
//        }
//        else if(requestCode == REQ_FINISH){// && resultCode == Activity.RESULT_OK){
//            state.haveFinishedBuy(true);
//            context.onBackPressed();
//        }
//    }
//
//    private String newIdUso(){
//        UUID uuid = UUID.randomUUID();
//        return uuid.toString();
//    }
//
//    private void crearCompra(){
//
//        if(solicitudProducto == null){
//            solicitudProducto = new SolicitudProducto();
//            solicitudProducto.setIdUso(newIdUso());
//        }
//
//        if(producto != null && validarDatos()){
//
//            String correo = et_correo_electronico.getText().toString();
//            String celular = et_numer_celular.getText().toString();
//            FormaPago formaPago = (FormaPago) spinner_forma_pago.getSelectedItem();
//
//            if(!TextUtils.isEmpty(solicitudProducto.getCelular()) &&
//                    !celular.equals(solicitudProducto.getCelular())){
//
//                solicitudProducto.setIdUso(newIdUso());
//
//            }else if(!TextUtils.isEmpty(solicitudProducto.getIdFormaPago()) &&
//                    !formaPago.getId().equals(solicitudProducto.getIdFormaPago())){
//
//                solicitudProducto.setIdUso(newIdUso());
//
//            }
//
//            solicitudProducto.setIdProducto(String.valueOf(producto.getId()));
//            solicitudProducto.setValor(producto.getValor());
//            solicitudProducto.setIdFormaPago(formaPago.getId());
//            solicitudProducto.setEmail(correo);
//            solicitudProducto.setCelular(celular);
//            solicitudProducto.setEqtiquetaCelular(producto.getEtiquetaCampoCelular());
//            solicitudProducto.setBeneficio(producto.getNombre());
//            solicitudProducto.setNombreFormaPago(formaPago.getNombre());
//            solicitudProducto.setNombreProducto(convenio.getNombre());
//
//            state.getResumen().setSolicitudProducto(solicitudProducto);
//            Intent intent = new Intent(getContext(), ActivityConveniosConfirmacionCompraDialog.class);
//            startActivityForResult(intent, REQ_CONFIRM);
//        }
//    }
//
//    private void comprar(){
//        new SolicitarProductoTask().execute();
//    }
//
//
//    private void finalizarCompra(String html){
//        Intent intent_condiciones = new Intent(getContext(), ActivityHtmlViewer.class);
//        intent_condiciones.putExtra("html", html);
//        startActivityForResult(intent_condiciones,  REQ_FINISH);
//    }
//
//    private boolean validarDatos(){
//        try {
//            String correo = et_correo_electronico.getText().toString();
//            String celular = et_numer_celular.getText().toString();
//
//            if(TextUtils.isEmpty(correo)){
//                et_correo_electronico.setError("Campo requerido");
//                return false;
//            }
//
//            if(TextUtils.isEmpty(celular)){
//                et_numer_celular.setError("Campo requerido");
//                return false;
//            }
//
//            if(!ActivityBase.validateEmail(et_correo_electronico.getText().toString())){
//                et_correo_electronico.setError("Ingrese un email válido");
//                return false;
//            }
//
//            if(!ActivityBase.validateCellPhone(et_numer_celular.getText().toString())){
//                et_numer_celular.setError("Ingrese un número de celular válido");
//                return false;
//            }
//            return true;
//        } catch (Exception ex) {
//            return false;
//        }
//
//    }
//
//    private void enableBtnComprar(boolean enable){
//        if(enable){
//            btnComprar.setEnabled(true);
//            btnComprar.setBackgroundColor(getResources().getColor(R.color.orange));
//        }else{
//            btnComprar.setEnabled(false);
//            btnComprar.setBackgroundColor(getResources().getColor(R.color.gris));
//        }
//    }
//
//    private class SolicitarProductoTask extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Solicitando producto...");
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//
//                Usuario usuario = state.getUsuario();
//                SolicitudProductoResult result = new ConveniosRestClient()
//                        .requestNewProductCall(usuario.token, usuario.cedula,
//                                solicitudProducto).execute().body();
//
//                if(result == null){
//                    return ConveniosRestClient.SERVER_ERROR;
//                }
//
//                if(result.isErrorAutenticacion()){
//                    return ConveniosRestClient.TOKEN_EXCEPTION;
//                }
//
//                if(result.isExitoso()){
//
//                    if(result.getRespuesta() != null){
//
//                        resultadoTransaccion = result.getRespuesta();
//
//                    }
//
//                    return ConveniosRestClient.RESULT_OK;
//
//                }else{
//                    return result.getDescripcionError();
//                }
//
//            } catch (Exception e) {
//                return "Ha ocurrido un error, por favor intente de nuevo.";
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
//            procesarJsonConvenios(result);
//        }
//
//    }
//
//    private void procesarJsonConvenios(String result) {
//
//        if (result.equals(ConveniosRestClient.RESULT_OK)) {
//            finalizarCompra(resultadoTransaccion.getResultadoHTML());
//            return;
//        }
//
//        if (result.equals(ConveniosRestClient.TOKEN_EXCEPTION)) {
//
//            AlertDialog.Builder d = new AlertDialog.Builder(context);
//            d.setTitle("Sesión finalizada");
//            d.setIcon(R.mipmap.icon_presente);
//            d.setMessage("Por favor ingrese nuevamente");
//            d.setCancelable(false);
//            d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    context.salir();
//                }
//            });
//            d.show();
//
//        }else {
//            context.makeErrorDialog(result);
//        }
//
//    }


}
