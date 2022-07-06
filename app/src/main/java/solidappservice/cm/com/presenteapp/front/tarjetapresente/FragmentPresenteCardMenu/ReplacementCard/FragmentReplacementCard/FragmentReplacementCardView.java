package solidappservice.cm.com.presenteapp.front.tarjetapresente.FragmentPresenteCardMenu.ReplacementCard.FragmentReplacementCard;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.dto.ReposicionTarjeta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 22/07/2020.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 30/08/2021.
 */
public class FragmentReplacementCardView extends Fragment implements FragmentReplacementCardContract.View{

    private FragmentReplacementCardPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.rt_correo_electronico)
    EditText rt_correo_electronico;
    @BindView(R.id.rt_numero_contacto)
    EditText rt_numero_contacto;
    @BindView(R.id.btnContinuar)
    Button btnContinuar;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de reposición tarjeta pte");
        firebaseAnalytics.logEvent("pantalla_reposicion_tarjeta_pte", params);
        View view = inflater.inflate(R.layout.fragment_presentecard_replacement, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentReplacementCardPresenter(this);
        context = (ActivityBase) getActivity();
        state = context.getState();
    }

    @OnClick(R.id.btnContinuar)
    public void onClickContinuar(){
        String correo = rt_correo_electronico.getText().toString();
        String telefonocontacto = rt_numero_contacto.getText().toString();
        Boolean validarDatos = true;

        if(!ActivityBase.validateEmail(correo)){
            if(!TextUtils.isEmpty(correo)){
                rt_correo_electronico.setError("Ingrese un email válido");
                validarDatos = false;
            }
        }

        if(TextUtils.isEmpty(telefonocontacto)){
            rt_numero_contacto.setError("Campo requerido");
        }

        if(!ActivityBase.validateContactPhone(telefonocontacto)){
            rt_numero_contacto.setError("Ingrese un número de contacto válido");
            validarDatos = false;
        }

        if(validarDatos){
            ReposicionTarjeta tarjeta = new ReposicionTarjeta();
            tarjeta.setD_email(correo);
            tarjeta.setT_telcel(telefonocontacto);
            state.setReposicionTarjeta(tarjeta);
            state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_22_PRESENTE_CARD_REPLACEMENTDETAIL_TAG);
        }
    }


}
