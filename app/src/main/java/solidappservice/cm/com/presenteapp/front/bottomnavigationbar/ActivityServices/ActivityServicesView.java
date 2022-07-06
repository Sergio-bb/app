package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityServices;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.bottomnavigationbar.ServiciosAdapter;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponseServicios;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 13/07/2020
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 28/08/2021
 */
public class ActivityServicesView extends ActivityBase implements ActivityServicesContract.View {

    private ActivityServicesPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private FirebaseAnalytics firebaseAnalytics;
    private List<ResponseServicios> servicios = null;

    @BindView(R.id.list_servicios)
    ListView list_servicios;
    @BindView(R.id.image_destacada1)
    ImageView imageDestacada1;
    @BindView(R.id.image_destacada2)
    ImageView imageDestacada2;
    @BindView(R.id.btn_back)
    ImageButton btnBack;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        ButterKnife.bind(this);
        setControls();
    }

    @Override
    protected void setControls() {
        presenter = new ActivityServicesPresenter(this, new ActivityServicesModel());
        context = this;
        state = context.getState();
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla del menú de otros servicios");
        firebaseAnalytics.logEvent("pantalla_menu_servicios", params);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                state.setServicios(null);
                fetchServices();
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null){
            context.salir();
        }else {
            fetchServices();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.btn_back)
    public void onClickBack(){
        onBackPressed();
    }

    @OnClick(R.id.imageReferesh)
    public void onClickRefresh(){
        state.setServicios(null);
        fetchServices();
    }

    @Override
    public void fetchServices(){
        if(state != null && state.getServicios() != null){
            hideCircularProgressBar();
            showSectionServices();
            showServices(state.getServicios());
        }else{
            presenter.fetchServices();
        }
    }

    @Override
    public void showServices(List<ResponseServicios> servicios) {
        try {
            state.setServicios(servicios);            for (Iterator<ResponseServicios> it = servicios.iterator(); it.hasNext();) {
                ResponseServicios p = it.next();
                if(p.getQ_orden() == 1){
                    showFeaturedService1(p);
                    it.remove();
                }
                if(p.getQ_orden() == 2){
                    showFeaturedService2(p);
                    it.remove();
                }
            }
            this.servicios = servicios;
            ServiciosAdapter sadapter = new ServiciosAdapter(this, servicios, getState().getUsuario());
            list_servicios.setAdapter(sadapter);
        } catch (Exception ex) {
            showDataFetchError("Lo sentimos", "");
            showErrorWithRefresh();
        }
    }

    @Override
    public void showFeaturedService1(ResponseServicios servicioDestacado1) {
        if(servicioDestacado1 != null){
            imageDestacada1.setBackgroundColor(Color.TRANSPARENT);
            Picasso.get().load(servicioDestacado1.getN_url_icono()).into(imageDestacada1);
            state.setServicioDestacado1(servicioDestacado1);
            imageDestacada1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GlobalState state = context.getState();
                    ResponseServicios destacado1 = state.getServicioDestacado1();
                    Bundle params = new Bundle();
                    params.putString("Descripción", "Interacción con el servicio "+destacado1.getN_servicio());
                    firebaseAnalytics.logEvent("pantalla_menu_servicio_"+destacado1.getK_servicio(), params);
                    Uri uri = Uri.parse(destacado1.getN_url_enlace());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void showFeaturedService2(ResponseServicios servicioDestacado2) {
        if(servicioDestacado2 != null){
            imageDestacada2.setBackgroundColor(Color.TRANSPARENT);
            Picasso.get().load(servicioDestacado2.getN_url_icono()).into(imageDestacada2);
            state.setServicioDestacado2(servicioDestacado2);
            imageDestacada2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ResponseServicios destacado2 = state.getServicioDestacado2();
                    Bundle params = new Bundle();
                    params.putString("Descripción", "Interacción con servicio "+destacado2.getN_servicio());
                    firebaseAnalytics.logEvent("pantalla_menu_servicios", params);
                    Uri uri = Uri.parse(destacado2.getN_url_enlace());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }
    }


    @Override
    public void showSectionServices(){
        pullToRefresh.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideSectionServices(){
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
        pullToRefresh.setVisibility(View.GONE);
        layoutCircularProgressBar.setVisibility(View.VISIBLE);
        circularProgressBar.setVisibility(View.GONE);
        textCircularProgressBar.setText("Ha ocurrido un error, inténtalo de nuevo ");
        buttonReferesh.setVisibility(View.VISIBLE);
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

//    private void cargarServicios(ArrayList<Servicios> servicios) {
//        try {
//            this.servicios = servicios;
//            ServiciosAdapter sadapter = new ServiciosAdapter(this, servicios, getState().getUsuario());
//            list_servicios.setAdapter(sadapter);
//        } catch (Exception ex) {
//            context.makeErrorDialog("Error cargando los productos");
//        }
//    }
//
//    private class ServiciosTask extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Consultando servicios...");
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                return networkHelper.readService(SincroHelper.SERVICIOS);
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
//            procesarJsonServicios(result);
//        }
//    }
//
//    private void procesarJsonServicios(String jsonRespuesta) {
//        try {
//            servicios = SincroHelper.procesarJsonServicios(jsonRespuesta);
//            GlobalState state = context.getState();
//            state.setServicios(servicios);
//
//            Servicios serviciodestacado1 = null;
//            Servicios serviciodestacado2 = null;
//
//            for(Servicios p : servicios){
//                if(p.getQ_orden() == 1){
//                    serviciodestacado1 = p;
//                }
//                if(p.getQ_orden() == 2){
//                    serviciodestacado2 = p;
//                }
//            }
//
//            if(serviciodestacado1 != null){
//                imageDestacada1.setBackgroundColor(Color.TRANSPARENT);
//                Picasso.get().load(serviciodestacado1.n_url_icono).into(imageDestacada1);
//                state.setServiciodestacado1(serviciodestacado1);
//                imageDestacada1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        GlobalState state = context.getState();
//                        Servicios destacado1 = state.getServiciodestacado1();
//                        Bundle params = new Bundle();
//                        params.putString("Descripción", "Interacción con el servicio "+destacado1.n_servicio);
//                        firebaseAnalytics.logEvent("pantalla_menu_servicio_"+destacado1.k_servicio, params);
//
//                        Uri uri = Uri.parse(destacado1.n_url_enlace);
//                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                        startActivity(intent);
//                    }
//                });
//            }
//
//            if(serviciodestacado2 != null){
//                imageDestacada2.setBackgroundColor(Color.TRANSPARENT);
//                Picasso.get().load(serviciodestacado2.n_url_icono).into(imageDestacada2);
//                state.setServiciodestacado2(serviciodestacado2);
//                imageDestacada2.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        GlobalState state = context.getState();
//                        Servicios destacado2 = state.getServiciodestacado2();
//                        Bundle params = new Bundle();
//                        params.putString("Descripción", "Interacción con servicio "+destacado2.n_servicio);
//                        firebaseAnalytics.logEvent("pantalla_menu_servicios", params);
//
//                        Uri uri = Uri.parse(destacado2.n_url_enlace);
//                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                        startActivity(intent);
//                    }
//                });
//            }
//
//            servicios.remove(0);
//            servicios.remove(0);
//            cargarServicios(servicios);
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
