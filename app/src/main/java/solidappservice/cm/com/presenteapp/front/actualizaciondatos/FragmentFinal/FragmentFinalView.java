package solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentFinal;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.front.actualizaciondatos.ActivityUpdatePersonalData.ActivityUpdatePersonalDataView;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 11/07/2021.
 */
public class FragmentFinalView extends Fragment implements FragmentFinalContract.View{

    private FragmentFinalPresenter presenter;
    private ActivityUpdatePersonalDataView baseView;
    private ActivityBase context;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.btnContinuar_actDatos_guardar)
    Button btnGuardar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de guardar de actualización de datos");
        firebaseAnalytics.logEvent("pantalla_act_datos_guardar", params);
        View view = inflater.inflate(R.layout.fragment_updatepersonaldata_final, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentFinalPresenter(this);
        baseView = (ActivityUpdatePersonalDataView) getActivity();
        context = (ActivityBase) getActivity();
        if (baseView != null) {
            baseView.buttonBack.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null || state.getUsuario() == null){
            context.salir();
            return;
        }
    }

    @OnClick(R.id.btnContinuar_actDatos_guardar)
    public void onClickGuardar(){
        GlobalState state = context.getState();
        if(baseView.actualizaPrimeraVez != null && baseView.actualizaPrimeraVez.equals("Y")){
            state.getUsuario().getDatosActualizados().setActualizaPrimeraVez("N");
            state.getUsuario().getDatosActualizados().setTieneDatosActualizados("Y");
            state.getUsuario().getDatosActualizados().setFechaUltimaActualizacion(new Date().getTime());
        }
        baseView.finish();
    }

}
