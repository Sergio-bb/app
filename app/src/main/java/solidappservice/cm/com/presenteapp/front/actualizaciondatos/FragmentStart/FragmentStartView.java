package solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentStart;

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
import solidappservice.cm.com.presenteapp.front.actualizaciondatos.ActivityUpdatePersonalData.ActivityUpdatePersonalDataView;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 11/07/2021.
 */
public class FragmentStartView extends Fragment implements FragmentStartContract.View{

    private FragmentStartPresenter presenter;
    private ActivityUpdatePersonalDataView baseView;
    private ActivityBase context;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.btnContinuar_actDatos)
    Button buttonContinuar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de inicio de actualización de datos");
        firebaseAnalytics.logEvent("pantalla_act_datos_inicio", params);
        View view = inflater.inflate(R.layout.fragment_updatepersonaldata_start, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentStartPresenter(this);
        baseView = (ActivityUpdatePersonalDataView) getActivity();
        context = (ActivityBase) getActivity();
        if (baseView != null) {
            baseView.buttonBack.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btnContinuar_actDatos)
    public void onClickContinuar(){
        if(context != null){
            if(baseView.actualizaPrimeraVez != null && baseView.actualizaPrimeraVez.equals("N")){
                baseView.basePresenter.loadFragmentUpdatePersonalData(IFragmentCoordinator.Pantalla.ActDatosValidateData);
            } else {
                baseView.basePresenter.loadFragmentUpdatePersonalData(IFragmentCoordinator.Pantalla.ActDatosEditData);
            }
        } else {
            assert false;
            context.salir();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null || state.getUsuario() == null){
            context.salir();
        }
    }

}
