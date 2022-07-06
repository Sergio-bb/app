package solidappservice.cm.com.presenteapp.front.nequi.suscription.ActivitySuscription;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.DatosAsociado;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.front.nequi.suscription.FragmentFinalSuscription.FragmentFinalSuscriptionView;
import solidappservice.cm.com.presenteapp.front.nequi.suscription.FragmentOneSuscription.FragmentOneSuscriptionView;
import solidappservice.cm.com.presenteapp.front.nequi.suscription.FragmentSecondSuscription.FragmentSecondSuscriptionView;
import solidappservice.cm.com.presenteapp.front.nequi.suscription.FragmentStarSuscription.FragmentStarSuscriptionView;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.login.FragmentLogin.FragmentLoginView;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

public class ActivitySuscriptionView extends ActivityBase implements ActivitySuscriptionContract.View {

    public ActivityBase context;
    public ActivitySuscriptionPresenter basePresenter;
    private GlobalState state;
//    private ProgressDialog pd;
    public DatosAsociado datosAsociado;

//    @BindView(R.id.containerSuscriptionNequi)
    FrameLayout frameLayout;
    @BindView(R.id.btn_volver)
    public TextView buttonVolver;
    @BindView(R.id.header)
    public ImageView img_nequipresente;

    @BindView(R.id.layout_circular_progress_bar)
    LinearLayout layoutCircularProgressBar;
    @BindView(R.id.circular_progress_bar)
    ProgressBar circularProgressBar;
    @BindView(R.id.text_circular_progress_Bar)
    TextView textCircularProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nequi_suscription);
        frameLayout = (FrameLayout) findViewById(R.id.containerSuscriptionNequi);
        ButterKnife.bind(this);
        setControls();
    }

    @Override
    protected void setControls() {
        basePresenter = new ActivitySuscriptionPresenter(this, new ActivitySuscriptionModel());
        datosAsociado = new DatosAsociado();
        context = this;
        state = context.getState();
        validateSuscriptionNequi();
    }

    @OnClick(R.id.btn_volver)
    public void onClickMenuPrincipal() {
        finish();
    }

    @Override
    public void showFragmentSuscripcion(IFragmentCoordinator.Pantalla pantalla) {
        GlobalState state = getState();
        Fragment fragment;

        switch (pantalla) {
            case SuscNequiInicio:
                fragment = new FragmentStarSuscriptionView();
                break;
            case SuscNequiOne:
                fragment = new FragmentOneSuscriptionView();
                break;
            case SuscNequiSecond:
                fragment = new FragmentSecondSuscriptionView();
                break;
            case SuscNequiFinal:
                fragment = new FragmentFinalSuscriptionView();
                break;

            default:
                fragment = new FragmentLoginView();
                break;
        }
        if (state != null && state.getFragmentActual() != null) {
            state.setFragmentAnterior(state.getFragmentActual());
        }
        hideKeyBoard();
        state.setFragmentActual(pantalla);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.containerSuscriptionNequi, fragment).commit();
    }

    @Override
    public void resultStatusSuscription(String status){
        if(!TextUtils.isEmpty(status) && status.equals("0")){
            getTimeExpiration();
        }else{
            basePresenter.loadFragmentSuscripcionNequi(Pantalla.SuscNequiInicio);
        }
    }

    @Override
    public void validateSuscriptionNequi(){
        try{
            basePresenter.validateSuscriptionNequi(new BaseRequest(
                    Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch(Exception ex){
            basePresenter.loadFragmentSuscripcionNequi(Pantalla.SuscNequiInicio);
        }
    }

    @Override
    public void getTimeExpiration(){
        try{
            if(state != null && state.getTiempoExpiracionAutorizacion() != null && state.getTiempoExpiracionAutorizacion() > 0){
                getTimeOfSuscription();
            }else{
                basePresenter.getTimeExpiration();
            }
        }catch(Exception ex){
            resultTimeExpiration(15);
            getTimeOfSuscription();
        }
    }

    @Override
    public void resultTimeExpiration(Integer timeExpiration){
        state.setTiempoExpiracionAutorizacion(timeExpiration);
    }

    @Override
    public void getTimeOfSuscription(){
        basePresenter.getTimeOfsuscription(new BaseRequestNequi(
                Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
                state.getUsuario().getToken()
        ));
    }

    @Override
    public void calculeMinutes(Integer days, Integer hour, Integer minute, Integer second){
        hideCircularProgressBar();
        showContentSuscription();
        if(days != null && days >= 0 &&
            hour != null && hour <=0 &&
                minute != null && minute < state.getTiempoExpiracionAutorizacion()){
            basePresenter.loadFragmentSuscripcionNequi(Pantalla.SuscNequiFinal);
        }else{
            basePresenter.loadFragmentSuscripcionNequi(Pantalla.SuscNequiInicio);
        }
    }

    @Override
    public void showContentSuscription(){
        frameLayout.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideContentSuscription(){
        frameLayout.setVisibility(View.GONE);
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
    public void showExpiredToken(String message) {
        final Dialog dialog = new Dialog(this);
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

