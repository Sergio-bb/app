package solidappservice.cm.com.presenteapp.front.menuprincipal.FragmentHome;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.banercomercial.response.ResponseBanerComercial;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.mensajes.response.ResponseObtenerMensajes;
import solidappservice.cm.com.presenteapp.entities.login.Response.Usuario;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityMainView;
import solidappservice.cm.com.presenteapp.front.actualizaciondatos.ActivityUpdatePersonalData.ActivityUpdatePersonalDataView;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityDirectory.ActivityDirectoryView;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityGeoreferencing.ActivityLocationsGms.ActivityLocationsGmsView;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityGeoreferencing.ActivityLocationsHms.ActivityLocationsHmsView;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityPortfolio.ActivityPortfolioProducts.ActivityPortfolioProductsView;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityServices.ActivityServicesView;
import solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.ActivityAgreementsView;
import solidappservice.cm.com.presenteapp.tools.BadgeView;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 24/11/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 17/09/2021.
 */
public class FragmentHomeView extends Fragment implements FragmentHomeContract.View {

    private FragmentHomePresenter presenter;
    private ActivityMainView context;
    private GlobalState state;
    private ProgressDialog pd;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.lblHolaUsuario)
    TextView lblHolaUsuario;
    @BindView(R.id.row_cant_messages)
    ImageView row_cant_messages;
    @BindView(R.id.btnElecciones)
    ImageButton btnElecciones;
    @BindView(R.id.btn_mi_informacion)
    Button btnMiInformacion;
    @BindView(R.id.tv_ultimaactualizacion)
    TextView fechaActualizacion;

    @BindView(R.id.flConvenios)
    RelativeLayout flConvenios;
    @BindView(R.id.imgb_finanzas)
    LinearLayout imgb_finanzas;
    @BindView(R.id.imgb_alianzas)
    LinearLayout imgb_alianzas;
    @BindView(R.id.btn_mis_mensajes)
    LinearLayout btn_mis_mensajes;
    @BindView(R.id.btnPortafolio)
    ImageButton btnPortafolio;
    @BindView(R.id.btnPreguntasFrecuentes)
    ImageButton btnPreguntasFrecuentes;
    @BindView(R.id.btnDirectorio)
    ImageButton btnDirectorio;
    @BindView(R.id.btnEncuentranos)
    ImageButton btnEncuentranos;

    @BindView(R.id.circular_progress_bar)
    ProgressBar circularProgressBar;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de menu principal");
        firebaseAnalytics.logEvent("pantalla_menu_principal", params);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentHomePresenter(this, new FragmentHomeModel());
        context = (ActivityMainView) getActivity();
        state = context.getState();
        pd = new ProgressDialog(context);
        if (context != null) {
            context.btn_back.setVisibility(View.VISIBLE);
            context.header.setImageResource(R.drawable.logo_internal);
            context.btnSalir.setVisibility(View.VISIBLE);
        }
        String user = "Hola " + state.getUsuario().getNombreAsociado()+", tu información personal en un solo lugar";
        Long time = state.getUsuario().getDatosActualizados().getFechaUltimaActualizacion();
        Date date = new Date(time);
        SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = formatFecha.format(date != null ? date : new Date());
        fechaActualizacion.setText(fecha);
        if (lblHolaUsuario != null) lblHolaUsuario.setText(user);
    }

    @OnClick(R.id.btn_mis_mensajes)
    public void onClickMisMensajes(View v) {
        context.verMisMensajes();
    }

    @OnClick(R.id.btnPortafolio)
    public void onClickPortafolio(View v) {
        Intent intent_p = new Intent(context, ActivityPortfolioProductsView.class);
        startActivity(intent_p);
    }

    @OnClick(R.id.btnPreguntasFrecuentes)
    public void onClickPreguntasFrecuentes(View v) {
        Intent intent_s = new Intent(context, ActivityServicesView.class);
        startActivity(intent_s);
    }

    @OnClick(R.id.btnElecciones)
    public void onClickElecciones(View v) {
        //context.verCandidatos();
    }

    @OnClick(R.id.btnDirectorio)
    public void onClickDirectorio(View v) {
        Intent intent_dir = new Intent(context, ActivityDirectoryView.class);
        startActivity(intent_dir);
    }

    @OnClick(R.id.btnEncuentranos)
    public void onClickEncuentranos(View v) {
        if(state != null && state.isHmsSystem()){
            Intent intent = new Intent(context, ActivityLocationsHmsView.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(context, ActivityLocationsGmsView.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.imgb_alianzas)
    public void onClickAlianzas(View v) {
        Intent intent_convenios = new Intent(context, ActivityAgreementsView.class);
        startActivity(intent_convenios);
    }

    @OnClick(R.id.imgb_finanzas)
    public void onClickFinanzas(View v) {
        context.verMenuFinanzas();
    }

    @OnClick(R.id.btn_mi_informacion)
    public void onClickMiInformacopn(View v) {
        GlobalState state = context.getState();
        Usuario usuario = state.getUsuario();
        Intent i = new Intent(context, ActivityUpdatePersonalDataView.class);
        i.putExtra("actualizaPrimeraVez", usuario.getDatosActualizados().getActualizaPrimeraVez());
        i.putExtra("datosActualizados", usuario.getDatosActualizados().getTieneDatosActualizados());
        context.startActivityForResult(i, 904);
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        Usuario usuario = state.getUsuario();
        if(usuario == null){
            context.reiniciarEstado();
            context.setFragment(IFragmentCoordinator.Pantalla.Ingreso);
            return;
        }
//        new EleccionesTask().execute();
//        new ObtenerTerminosUsuarioTask().execute(usuario.cedula, usuario.token);
        fetchButtonStateAgreements();
        fetchCommercialBanner();
//        new ButtonBannerTask().execute(usuario.cedula, usuario.token);
        fetchMessageInbox();
//        new BuzonTask().execute(usuario.cedula, usuario.token);
    }

    @Override
    public void fetchButtonStateAgreements() {
        try{
            presenter.fetchButtonStateAgreements();
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void showButtonAgreements() {
        flConvenios.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideButtonAgreements() {
        flConvenios.setVisibility(View.GONE);
    }

    @Override
    public void fetchCommercialBanner(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchCommercialBanner(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void showCommercialBanner(ResponseBanerComercial commercialBanner){
        btnElecciones.setVisibility(View.VISIBLE);
        Picasso.get().load(commercialBanner.getN_url_imagen()).into(btnElecciones);
        btnElecciones.setBackgroundColor(Color.TRANSPARENT);
        btnElecciones.setScaleType(ImageView.ScaleType.CENTER);
        btnElecciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(commercialBanner.getI_autenticportal() != null && commercialBanner.getI_autenticportal().equals("Y")){
                    Encripcion encripcion = new Encripcion();
                    String tokenSession = encripcion.encryptAES(state.getUsuario().getCedula()+":"+state.getUsuario().getToken());
                    Uri uri = Uri.parse(commercialBanner.getN_url_enlace().replace("${tokenSession}", tokenSession));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }else{
                    Uri uri = Uri.parse(commercialBanner.getN_url_enlace());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void hideCommercialBanner(){
        btnElecciones.setVisibility(View.GONE);
    }

    @Override
    public void fetchMessageInbox(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchMessageInbox(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void showMessageInbox(List<ResponseObtenerMensajes> messagesInbox){
        state.setMensajesBuzon(messagesInbox);
        int cant = context.getUnreadMessagesCount();
        if (cant > 0 && row_cant_messages != null) {
            BadgeView badgeView = new BadgeView(context, row_cant_messages);
            badgeView.setText(String.valueOf(cant));
            badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
            badgeView.setBadgeMargin(40, 20);
            badgeView.setBadgeBackgroundColor(Color.parseColor("#FFEA00"));
            TranslateAnimation anim = new TranslateAnimation(-100, 0, 0, 0);
            anim.setInterpolator(new BounceInterpolator());
            anim.setDuration(1000);
            badgeView.toggle(anim, null);
        }
    }

    @Override
    public void showCircularProgressBar() {
        circularProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCircularProgressBar() {
        circularProgressBar.setVisibility(View.GONE);
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
    //Valida los mensajes no leidos
//    private void showCantMessages(){
//        int cant = context.getUnreadMessagesCount();
//        if (cant > 0 && row_cant_messages != null) {
//            BadgeView badgeView = new BadgeView(context, row_cant_messages);
//            badgeView.setText(String.valueOf(cant));
//            badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
//            badgeView.setBadgeMargin(40, 20);
//            badgeView.setBadgeBackgroundColor(Color.parseColor("#FFEA00"));
//            TranslateAnimation anim = new TranslateAnimation(-100, 0, 0, 0);
//            anim.setInterpolator(new BounceInterpolator());
//            anim.setDuration(1000);
//            badgeView.toggle(anim, null);
//        }
//    }
//
//    class BuzonTask extends AsyncTask<String, String, String> {
//
//        String cedula = null;
//        String token = null;
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                Encripcion encripcion = Encripcion.getInstance();
//                JSONObject param = new JSONObject();
//                param.put("cedula", cedula = encripcion.encriptar(params[0]));
//                param.put("token", token = params[1]);
//                return networkHelper.writeService(param, SincroHelper.MENSAJES_BUZON);
//            } catch (Exception e) {
//                return e.getMessage();
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            procesarResultMensajesBuzon(result);
//            pd.dismiss();
//        }
//    }
//
//    private void procesarResultMensajesBuzon(String result) {
//        try {
//            ArrayList<ResponseObtenerMensajes> mensajesBuzon = SincroHelper.procesarJsonMensajesBuzon(result);
//            context.getState().setMensajesBuzon(mensajesBuzon);
//
//            //Mostramos la cantidad de mensajes sin leer
//            showCantMessages();
//
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
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    //Valida el banner activo en el menu principal
//    private class ButtonBannerTask extends AsyncTask<String, String, String> {
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
//                Encripcion encripcion = Encripcion.getInstance();
//                JSONObject param = new JSONObject();
//
//                param.put("cedula", encripcion.encriptar(params[0]));
//                param.put("token", params[1]);
//                return networkHelper.writeService(param, SincroHelper.OBTENER_BANNER_MENUP);
//            } catch (Exception e) {
//                return e.getMessage();
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            procesarResultJSONBannerMP(result);
//        }
//    }
//
//    private void procesarResultJSONBannerMP(String result){
//        try {
//            final BannerMenuPrincipal banneractivo = SincroHelper.procesarJsonObtenerBannerMenuP(result);
//
//            if(banneractivo != null && banneractivo.i_estado.equals("S")){
//                btnElecciones.setVisibility(View.VISIBLE);
//                Picasso.get().load(banneractivo.n_url_imagen).into(btnElecciones);
//                btnElecciones.setBackgroundColor(Color.TRANSPARENT);
//                btnElecciones.setScaleType(ImageView.ScaleType.CENTER);
//                btnElecciones.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Uri uri = Uri.parse(banneractivo.n_url_enlace);
//                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                        startActivity(intent);
//                    }
//                });
//            }else{
//                btnElecciones.setVisibility(View.GONE);
//            }
//
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
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }


    //Valida los terminos y condiciones del usuario, comprueba que tenga activo los ultimos
//    private class ObtenerTerminosUsuarioTask extends AsyncTask<String, String, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                Encripcion encripcion = Encripcion.getInstance();
//                JSONObject param = new JSONObject();
//
//                param.put("cedula", encripcion.encriptar(params[0]));
//                param.put("token", params[1]);
//                return networkHelper.writeService(param, SincroHelper.OBTENER_TERMINOS_ACEPTADOS);
//            } catch (Exception e) {
//                return e.getMessage();
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            procesarResultJSONSelectTerminosAceptados(result);
//        }
//    }

//    private void procesarResultJSONSelectTerminosAceptados(String result){
//        try {
//            GlobalState state = context.getState();
//            TerminosyCondiciones terminosactuales = state.getTerminos();
//            TerminosyCondicionesUsuario terminosusuario = SincroHelper.procesarJsonSelectTerminos(result, terminosactuales.k_termycond);
//
//            if(terminosusuario == null){
//                Intent i = new Intent(context, ActivityTerminosUso.class);
//                startActivityForResult(i, ActivityMain.TERMS_AND_CONDITIONS);
//            }
//
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
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

}
