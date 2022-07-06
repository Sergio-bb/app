package solidappservice.cm.com.presenteapp.front.login.FragmentLogin;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import solidappservice.cm.com.presenteapp.BuildConfig;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.login.ImageSlideAdapter;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.entities.dispositivo.apirequest.Dispositivo;
import solidappservice.cm.com.presenteapp.entities.dispositivo.apiresponse.ResponseValidarDispositivo;
import solidappservice.cm.com.presenteapp.entities.login.Request.RequestLogin;
import solidappservice.cm.com.presenteapp.entities.login.Response.Usuario;
import solidappservice.cm.com.presenteapp.entities.mensajesbanner.ResponseMensajesBanner;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.actualizaciondatos.ActivityUpdatePersonalData.ActivityUpdatePersonalDataView;
import solidappservice.cm.com.presenteapp.front.base.main.ActivityMainView;
import solidappservice.cm.com.presenteapp.front.terminosycondiciones.ActivityTermsAndConditions.ActivityTermsAndConditionsView;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;
import solidappservice.cm.com.presenteapp.tools.PageIndicator;
import solidappservice.cm.com.presenteapp.tools.helpers.DialogHelpers;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 23/11/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 20/09/2021
 */
public class FragmentLoginView extends Fragment implements FragmentLoginContract.View {

    private FragmentLoginPresenter presenter;
    private ActivityMainView context;
    private GlobalState state;
    private Dialog pd;
    private List<ResponseMensajesBanner> mensajes;
    private FirebaseAnalytics firebaseAnalytics;

    private boolean stopSliding = false;
    private static final long ANIM_VIEWPAGER_DELAY = 5000;
    private static final long ANIM_VIEWPAGER_DELAY_USER_VIEW = 10000;

    private Runnable animateViewPager;
    private Handler handler;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.indicator)
    PageIndicator mIndicator;
    @BindView(R.id.imgBannerLogin)
    ImageButton _imgBannerLogin;
    @BindView(R.id.txtUsuario)
    EditText txtUsuario;
    @BindView(R.id.txtPassword)
    EditText txtPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.lblRecuperarContrasena)
    TextView lblRecuperarContrasena;
    @BindView(R.id.lblTerminos)
    TextView lblTerminos;
    @BindView(R.id.img_arrow_left)
    ImageView img_arrow_left ;
    @BindView(R.id.img_arrow_right)
    ImageView img_arrow_right;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interaccion con pantalla de inicio de sesión");
        firebaseAnalytics.logEvent("pantalla_inicio_sesion", params);
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {

        presenter = new FragmentLoginPresenter(this, new FragmentLoginModel());
        context = (ActivityMainView) getActivity();
        state = context.getState();
        txtPassword.setTypeface(Typeface.DEFAULT);
        txtPassword.setTransformationMethod(new PasswordTransformationMethod());
        mensajes = new ArrayList<>();

        if(context != null){
            context.header.setImageResource(R.drawable.logo_login);
            context.btn_back.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String versionCodeFirebase ;
                if(state.isHmsSystem()){
                    versionCodeFirebase = firebaseRemoteConfig.getString("versionCodeHms");
                }else{
                    versionCodeFirebase = firebaseRemoteConfig.getString("versionCode");
                }
                showMessageUpdateApp(versionCodeFirebase);
            } else {
                String versionCodeFirebase ;
                if(state.isHmsSystem()){
                    versionCodeFirebase = firebaseRemoteConfig.getString("versionCodeHms");
                }else{
                    versionCodeFirebase = firebaseRemoteConfig.getString("versionCode");
                }
                showMessageUpdateApp(versionCodeFirebase);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if (state != null && state.getMensajes() != null  && state.getMensajes().size() > 0) {
            setPageAdapter();
        } else {
            fetchBannerMessages();
        }

        if (state != null && state.getBitmapImgBanner() != null) {
            Drawable drawable;
            if(state.getDrawableImgBanner() != null){
                drawable = state.getDrawableImgBanner();
            }else {
                Bitmap bitmap = transformation.transform(state.getBitmapImgBanner());
                drawable = new BitmapDrawable(getResources(), bitmap);
                state.setDrawableImgBanner(drawable);
            }
            _imgBannerLogin.setImageDrawable(drawable);
        }else{
            fetchLoginImage();
        }
    }


    @OnTouch(R.id.view_pager)
    public boolean onTouchViewPager(View v, MotionEvent event) {
        v.performClick();
        v.getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                if (mensajes != null && mensajes.size() != 0) {
                    stopSliding = false;
                    runnable(mensajes.size());
                    handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY_USER_VIEW);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (handler != null && !stopSliding) {
                    stopSliding = true;
                    handler.removeCallbacks(animateViewPager);
                }
                break;
        }
        return false;
    }



    @OnClick(R.id.btnLogin)
    public void onClickLoginButton(View v) {
        Editable usuario = txtUsuario.getText();
        Editable password = txtPassword.getText();
        if (usuario == null || TextUtils.isEmpty(usuario.toString())) {
            txtUsuario.setError("Campo requerido");
            return;
        }
        if (password == null || TextUtils.isEmpty(password.toString())) {
            txtPassword.setError("Campo requerido");
            return;
        }
        validateLogin(usuario.toString(), password.toString());
    }

    @OnClick(R.id.lblRecuperarContrasena)
    public void onClickRecoverPassword(View v) {
        context.setFragment(IFragmentCoordinator.Pantalla.RecuperarContrasena);
    }

    @OnClick(R.id.lblTerminos)
    public void onClickTyC(View v){
        Intent i = new Intent(context, ActivityTermsAndConditionsView.class);
        context.startActivityForResult(i, GlobalState.TERMS_AND_CONDITIONS);
    }

    @OnClick(R.id.img_arrow_left)
    public void onClickImageLeft(View v){
        stopSliding = true;
        if (mViewPager.getCurrentItem() == 0) {
            mViewPager.setCurrentItem(mensajes.size() - 1);
        } else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }
    @OnClick(R.id.img_arrow_right)
    public void onClickImageRight(View v){
        stopSliding = true;
        if (mViewPager.getCurrentItem() == (mensajes.size() - 1)) {
            mViewPager.setCurrentItem(0);
        } else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
        }
    }

    @Override
    public void validateLogin(String usuario, String clave){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.validateLogin(new RequestLogin(
                    encripcion.encriptar(usuario),
                    encripcion.encriptar(clave),
                    "APPPRESENTE"
            ));
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void resultValidateLogin(Usuario usuario){
        if (usuario != null && !TextUtils.isEmpty(usuario.getNombreAsociado()) && !TextUtils.isEmpty(usuario.getToken())) {
            Encripcion encripcion = Encripcion.getInstance();
            usuario.setClave(encripcion.desencriptar(usuario.getClave()));
            usuario.setCedula(encripcion.desencriptar(usuario.getCedula()));
            state.setTopeTransacciones(usuario.getTopeTransacciones());
            state.setUsuario(usuario);


            if (state.validarEstado()) {
                switch (usuario.getDatosActualizados().getActualizaPrimeraVez()){
                    case "Y":
                        context.setFragment(IFragmentCoordinator.Pantalla.MenuPrincipal);
                        hideProgressDialog();
                        if(usuario.getAceptoUltimosTyC().equals("N")){
                            Intent i = new Intent(context, ActivityTermsAndConditionsView.class);
                            context.startActivityForResult(i, GlobalState.TERMS_AND_CONDITIONS);
                            return;
                        } else {
                            Intent i = new Intent(context, ActivityUpdatePersonalDataView.class);
                            i.putExtra("actualizaPrimeraVez", usuario.getDatosActualizados().getActualizaPrimeraVez());
                            i.putExtra("datosActualizados", usuario.getDatosActualizados().getTieneDatosActualizados());
                            context.startActivityForResult(i, GlobalState.UPDATE_PERSONAL_DATA);
                        }
                        break;
                    case "N":
                        validateRegisterDevice();
                        break;
                    default:
                        context.setFragment(IFragmentCoordinator.Pantalla.MenuPrincipal);
                        hideProgressDialog();
                        break;
                }
            }
        }else{
            hideProgressDialog();
            showDataFetchError("Lo sentimos","Usuario inválido");
        }
    }

    @Override
    public void fetchBannerMessages(){
        presenter.fetchBannerMessages();
    }

    @Override
    public void loadBannerMessages(List<ResponseMensajesBanner> bannerMessages){
        if (bannerMessages != null && state != null) {
            this.mensajes = bannerMessages;
            if (mensajes.size() > 0) {
                state.setMensajes(bannerMessages);
                setPageAdapter();
            }
        }
    }

    @Override
    public void fetchLoginImage(){
        try{
            presenter.fetchLoginImage();
        }catch (Exception ignored){
        }
    }

    @Override
    public void loadLoginImage(String loginImage){
        if(!TextUtils.isEmpty(loginImage)){
            Picasso.get()
                    .load(loginImage)
                    .placeholder(R.drawable.banner_login)
                    .transform(transformation)
                    .into(_imgBannerLogin);
        }
    }

    Transformation transformation = new Transformation() {
        @Override
        public Bitmap transform(Bitmap source) {
            try {
                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                if(wm == null) return null;
                Display display = wm.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int targetWidth = size.x;
                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int targetHeight = (int) (targetWidth * aspectRatio);
                Bitmap result = source;

                if(targetHeight > 0 && targetWidth > 0)
                    result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);

                if (source != result && !source.isRecycled()) {
                    source.recycle();
                    source = null;
                }
                return result;
            }catch (Exception e){
                return source;
            }
        }
        @Override
        public String key() {
            return "transformation" + " desiredWidth";
        }
    };

    @Override
    public void validateRegisterDevice() {
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.validateRegisterDevice(new Dispositivo(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    context.getFabricante(),
                    context.getModelo(),
                    context.getIdDispositivo(),
                    Build.VERSION.SDK_INT <= Build.VERSION_CODES.O ? "" : context.getImei(),
                    Build.VERSION.SDK_INT <= Build.VERSION_CODES.O ? "" : context.getCelPrincipal(),
                    state.isHmsSystem() ? "Android HMS" : "Android GMS",
                    context.getVersionSistemaOperativo()
            ));
        }catch(Exception ex){
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void resultValidateRegisterDevice(ResponseValidarDispositivo validateDevice){
        if(validateDevice != null){
            if(validateDevice.getDispositivoRegistrado() != null &&
                    validateDevice.getDispositivoRegistrado().equals("Y")){
                state.setIdDispositivoRegistrado(validateDevice.getIdRegistroDispositivo());
                context.showScreenHome();
                hideProgressDialog();
                if(state.getUsuario().getAceptoUltimosTyC().equals("N")){
                    Intent i = new Intent(context, ActivityTermsAndConditionsView.class);
                    context.startActivityForResult(i, GlobalState.TERMS_AND_CONDITIONS);
                }

                if(state.getUsuario().getDatosActualizados().getTieneDatosActualizados().equals("N")){
                    Intent i = new Intent(context, ActivityUpdatePersonalDataView.class);
                    i.putExtra("actualizaPrimeraVez", state.getUsuario().getDatosActualizados().getActualizaPrimeraVez());
                    i.putExtra("datosActualizados", state.getUsuario().getDatosActualizados().getTieneDatosActualizados());
                    context.startActivityForResult(i, GlobalState.UPDATE_PERSONAL_DATA);
                }
            } else {
                hideProgressDialog();
                context.showDialogValidateCode();
            }
        }else{
            hideProgressDialog();
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void showMessageUpdateApp(String versionCodeFirebase) {
        int versionActual = BuildConfig.VERSION_CODE;
        int codigoDesdeFirebase = versionCodeFirebase.isEmpty() ? versionActual : Integer.parseInt(versionCodeFirebase);
        if (codigoDesdeFirebase > versionActual) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.pop_up_versionapp_available);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button buttonAceptar =  dialog.findViewById(R.id.btnAceptar);
            buttonAceptar.setOnClickListener(view -> {
                if (state != null && state.isHmsSystem()) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("appmarket://details?id=" + "solidappservice.cm.com.presenteapp")));
                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + "solidappservice.cm.com.presenteapp")));
                }
            });
            dialog.show();
        }
    }

    @Override
    public void setPageAdapter() {
        mensajes = state.getMensajes();
        if (mensajes != null && mensajes.size() > 0) {
            mViewPager.setAdapter(new ImageSlideAdapter(context, mensajes));
            mIndicator.setViewPager(mViewPager);
            runnable(mensajes.size());
            handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
        }
    }

    public void runnable(final int size) {
        handler = new Handler();
        animateViewPager = () -> {
            if (!stopSliding) {
                if (mViewPager.getCurrentItem() == size - 1) {
                    mViewPager.setCurrentItem(0);
                } else {
                    mViewPager.setCurrentItem(
                            mViewPager.getCurrentItem() + 1, true);
                }
                handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
            }
        };
    }

    @Override
    public void showProgressDialog(String message) {
        pd = new Dialog(context);
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.setCanceledOnTouchOutside(false);
        pd.setContentView(R.layout.pop_up_loading);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView contentMessage = pd.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        pd.show();
    }

    @Override
    public void hideProgressDialog() {
        if(pd != null)
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
        TextView titleMessage = dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText("Lo sentimos");
        TextView contentMessage = dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        ImageButton buttonClose = dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(view -> {
            dialog.dismiss();
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
        TextView titleMessage = dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText(title);
        TextView contentMessage = dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        ImageButton buttonClose = dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();
    }
}
