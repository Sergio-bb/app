package solidappservice.cm.com.presenteapp.front.nequi.suscription.FragmentFinalSuscription;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.front.nequi.suscription.ActivitySuscription.ActivitySuscriptionView;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

public class FragmentFinalSuscriptionView extends Fragment implements FragmentFinalSuscriptionContract.View{

    private ActivityBase context;
    private ActivitySuscriptionView baseView;
    private FragmentFinalSuscriptionPresenter presenter;
    private ProgressDialog pd;
    private GlobalState state;
    private Contador timer;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.buttonCerrar)
    Button buttonCerrar;
    @BindView(R.id.counter_timer)
    TextView tiempoExpiracion;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla final suscripcion nequi");
        firebaseAnalytics.logEvent("pantalla_final_suscripcion", params);
        View view = inflater.inflate(R.layout.fragment_nequi_suscription_final, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentFinalSuscriptionPresenter(this, new FragmentFinalSuscriptionModel());
        baseView = (ActivitySuscriptionView) getActivity();
        context = (ActivityBase) getActivity();
        state = context.getState();
        pd = new ProgressDialog(context);
        if (baseView != null) {
            baseView.img_nequipresente.setVisibility(View.VISIBLE);
        }
        getTimeOfSuscription();
    }

    @OnClick(R.id.buttonCerrar)
    public void onClickCerrar(){
        baseView.finish();
    }

    @OnClick(R.id.btnIrANequi)
    public void onClickIrANequi(){
        Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.nequi.MobileApp");
        if (intent != null) {
            // We found the activity now start the activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + "com.nequi.MobileApp"));
        }
        startActivity(intent);
    }

    @Override
    public void initializeCounter(Integer minutes, Integer seconds) {
        if(tiempoExpiracion != null){
            tiempoExpiracion.setTextColor(Color.parseColor("#4A4A49"));
        }
        if (timer != null) {
            timer.cancel();
        }
        long differenceTime = 900000-((minutes*60000)+(seconds*1000));
        if(minutes != null && minutes == 15){
            differenceTime = 900000;
        }
        timer = new Contador(differenceTime,1000);
        timer.start();
    }

    @Override
    public void getTimeOfSuscription(){
        try {
            presenter.getTimeOfsuscription(new BaseRequestNequi(
                    Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        } catch (Exception e) {
            initializeCounter(15, 0);
            Log.d("Error", e.getMessage());
        }
    }

    public class Contador extends CountDownTimer {

        public Contador(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            tiempoExpiracion.setText("00:00");
            tiempoExpiracion.setTextColor(Color.RED);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long minutos =  millisUntilFinished / 60000;
            long segundos = millisUntilFinished % 60000 / 1000;
            tiempoExpiracion.setText(String.format("%s:%s", (minutos < 10 ? "0" + minutos : minutos), segundos < 10 ? "0" + segundos : segundos));
        }
    }


}


