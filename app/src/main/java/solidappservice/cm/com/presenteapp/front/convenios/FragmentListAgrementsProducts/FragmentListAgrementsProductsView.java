package solidappservice.cm.com.presenteapp.front.convenios.FragmentListAgrementsProducts;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.convenios.ProductosAdapter;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Convenio;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Producto;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Resumen;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.ActivityAgreementsView;
import solidappservice.cm.com.presenteapp.front.convenios.ActivityHtmlViewer.ActivityHtmlView;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;
import solidappservice.cm.com.presenteapp.tools.OnRecyclerViewItemClickListener;

/**
 * CREADO POR JORGE ANDR??S DAVID CARDONA EL 31/07/2018.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public class FragmentListAgrementsProductsView extends Fragment implements OnRecyclerViewItemClickListener, FragmentListAgrementsProductsContract.View{

    private FragmentListAgrementsProductsPresenter presenter;
    private ActivityBase context;
    private ActivityAgreementsView baseView;
    private GlobalState state;
    private Resumen resumen;
    private List<Producto> productos;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.my_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_info_convenio_resumen)
    TextView tv_info_convenio_resumen;
    @BindView(R.id.tv_info_convenio_full)
    TextView tv_info_convenio_full;
    @BindView(R.id.tv_nombre_convenio)
    TextView tv_nombre_convenio;

    @BindView(R.id.contentProductsAgreements)
    LinearLayout contentProductsAgreements;

    @BindView(R.id.layout_circular_progress_bar)
    LinearLayout layoutCircularProgressBar;
    @BindView(R.id.circular_progress_bar)
    ProgressBar circularProgressBar;
    @BindView(R.id.text_circular_progress_Bar)
    TextView textCircularProgressBar;
    @BindView(R.id.imageReferesh)
    ImageView buttonReferesh;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripci??n", "Interacci??n con pantalla de convenios productos");
        firebaseAnalytics.logEvent("pantalla_convenios_productos", params);
        View view = inflater.inflate(R.layout.fragment_agreements_listproducts, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentListAgrementsProductsPresenter(this, new FragmentListAgrementsProductsModel());
        context = (ActivityBase) getActivity();
        baseView = (ActivityAgreementsView) getActivity();
        state = baseView.getState();
        resumen = state.getResumen();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (baseView != null) {
            baseView.btn_back.setVisibility(View.VISIBLE);
            baseView.header.setImageResource(R.drawable.logo_internal);
        }

        showAgreement();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onItemClick(View v, int position) {
        switch (v.getId()) {
            case R.id.imgb_comprar:
                Producto productoSeleccionado = productos.get(position);
                state.getResumen().setProductoSeleccionado(productoSeleccionado);
                baseView.setFragment(IFragmentCoordinator.Pantalla.ConveniosCompraProducto);
                break;
            case R.id.imgb_condiciones:
                Producto productoSeleccionadoCondiciones = productos.get(position);
                Intent intent_condiciones = new Intent(getContext(), ActivityHtmlView.class);
                intent_condiciones.putExtra("html", productoSeleccionadoCondiciones.getHtmlRestriccion());
                startActivityForResult(intent_condiciones, GlobalState.VIEW_RESULT_AGREEMENT);
                break;
            case R.id.imgb_ver_mas:
                Producto productoSeleccionadoVerMas = productos.get(position);
                Intent intent_ver_mas = new Intent(getContext(), ActivityHtmlView.class);
                intent_ver_mas.putExtra("html", productoSeleccionadoVerMas.getHtmlDescripcion());
                startActivityForResult(intent_ver_mas, GlobalState.VIEW_RESULT_AGREEMENT);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GlobalState.VIEW_RESULT_AGREEMENT){// && resultCode == Activity.RESULT_OK){
            if(resultCode == GlobalState.CLOSED_SESSION){
                baseView.setResult(GlobalState.CLOSED_SESSION);
                baseView.finish();
            }
        }
    }

    @OnClick(R.id.imageReferesh)
    public void onClickRefresh(){
        showAgreement();
    }

    @Override
    public void showAgreement(){
        if (resumen != null) {
            Convenio convenio = resumen.getConvenioSeleccionado();
            if (convenio != null) {
                tv_info_convenio_resumen.setText(convenio.getBeneficio());
                tv_info_convenio_full.setText(convenio.getDetalle());
                tv_nombre_convenio.setText(convenio.getNombre());
                fetchProductsByAgreements(convenio.getId());
            }
        }else{
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void fetchProductsByAgreements(String idAgreement){
        try{
            presenter.fetchProductsByAgreements(new BaseRequest(
                            state.getUsuario().getCedula(),
                            state.getUsuario().getToken()
                    ),
                    idAgreement
            );
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void showProductsByAgreements(List<Producto> productos) {
        resumen.setProductosPorConvenio(productos);
        this.productos = productos;
        RecyclerView.Adapter mAdapter = new ProductosAdapter(productos, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void showContentProductsByAgreements(){
        contentProductsAgreements.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideContentProductsByAgreements(){
        contentProductsAgreements.setVisibility(View.GONE);
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
        contentProductsAgreements.setVisibility(View.GONE);
        layoutCircularProgressBar.setVisibility(View.VISIBLE);
        circularProgressBar.setVisibility(View.GONE);
        textCircularProgressBar.setText("Ha ocurrido un error, int??ntalo de nuevo ");
        buttonReferesh.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDialogError(String title, String message){
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
                dialog.dismiss();
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

//    private void loadConvenio() {
//
//        if (resumen != null) {
//            Convenio convenio = resumen.getConvenioSeleccionado();
//
//            if (convenio != null) {
//                tv_info_convenio_resumen.setText(convenio.getBeneficio());
//                tv_info_convenio_full.setText(convenio.getDetalle());
//                tv_nombre_convenio.setText(convenio.getNombre());
//                new ProductosPorConvenioTask().execute(convenio.getId());
//            }
//        }
//    }
//
//    private void loadData(List<Producto> productos) {
//        this.productos = productos;
//        RecyclerView.Adapter mAdapter = new ProductosAdapter(productos, this);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setHasFixedSize(true);
//    }
//
//
//    private class ProductosPorConvenioTask extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Consultando productos...");
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
//                ProductoResult result = new ConveniosRestClient().productsCall(usuario.token, usuario.cedula, params[0]).execute().body();
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
//                    ArrayList<Producto> newProductos = new ArrayList<>();
//
//                    if(result.getRespuesta() != null){
//                        for(ResponseProducto
//                                p : result.getRespuesta()){
//                            newProductos.add(p.generateProducto());
//                        }
//                    }
//
//                    productos = newProductos;
//                    resumen.setProductosPorConvenio(productos);
//                    return ConveniosRestClient.RESULT_OK;
//
//                }else{
//                    return result.getDescripcionError();
//                }
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
//            procesarJsonConvenios(result);
//        }
//
//    }
//
//    private void procesarJsonConvenios(String result) {
//
//        if (result.equals(ConveniosRestClient.RESULT_OK)) {
//            loadData(resumen.getProductosPorConvenio());
//            return;
//        }
//
//        if (result.equals(ConveniosRestClient.TOKEN_EXCEPTION)) {
//            AlertDialog.Builder d = new AlertDialog.Builder(context);
//            d.setTitle("Sesi??n finalizada");
//            d.setIcon(R.mipmap.icon_presente);
//            d.setMessage("Por favor ingrese nuevamente");
//            d.setCancelable(false);
//            d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    context.salir();
//                }
//            });
//            d.show();
//        }else {
//            context.makeErrorDialog(result);
//        }
//
//    }
}
