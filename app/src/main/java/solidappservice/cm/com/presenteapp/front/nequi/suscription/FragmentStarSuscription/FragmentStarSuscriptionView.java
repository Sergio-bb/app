package solidappservice.cm.com.presenteapp.front.nequi.suscription.FragmentStarSuscription;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.front.nequi.suscription.ActivitySuscription.ActivitySuscriptionView;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;

public class FragmentStarSuscriptionView extends Fragment implements FragmentStartSuscriptionContract.View {

    private FragmentStartSuscriptionPresenter presenterStart;
    private ActivitySuscriptionView baseView;
    private ActivityBase context;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.btnComenzar)
    Button buttonComenzar;

    @BindView(R.id.btnAhoraNo)
    Button buttonAhoraNo;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de inicio de suscripcion nequi");
        firebaseAnalytics.logEvent("pantalla_start_suscripcion", params);
        View view = inflater.inflate(R.layout.fragment_nequi_suscription_start, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenterStart = new FragmentStartSuscriptionPresenter(this);
        baseView = (ActivitySuscriptionView) getActivity();
        context = (ActivityBase) getActivity();
        if (baseView != null) {
            baseView.img_nequipresente.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btnComenzar)
    public void onClikComenzar(){
        if(context != null){
            baseView.basePresenter.loadFragmentSuscripcionNequi(IFragmentCoordinator.Pantalla.SuscNequiOne);
        }else{
            context.salir();
        }
    }

    @OnClick(R.id.btnAhoraNo)
    public void onClikAhoraNo(){
        baseView.finish();
    }
}
