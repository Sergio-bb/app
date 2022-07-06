package solidappservice.cm.com.presenteapp.front.actualizaciondatos.ActivityUpdatePersonalData;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.DatosAsociado;
import solidappservice.cm.com.presenteapp.front.menuprincipal.FragmentHome.FragmentHomeView;
import solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentEditData.FragmentEditDataView;
import solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentFinal.FragmentFinalView;
import solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentStart.FragmentStartView;
import solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentValidateData.FragmentValidateDataView;
import solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentVerifyCode.FragmentVerifyCodeView;
import solidappservice.cm.com.presenteapp.front.login.FragmentLogin.FragmentLoginView;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 11/07/2021.
 */
public class ActivityUpdatePersonalDataView extends ActivityBase implements ActivityUpdatePersonalDataContract.View{

    public ActivityUpdatePersonalDataContract.Presenter basePresenter;
    private ActivityBase context;

    public String actualizaPrimeraVez;
    public String datosActualizados;
    public DatosAsociado datosAsociado;
    public Boolean isDatosEditados;

    @BindView(R.id.btn_back)
    public ImageButton buttonBack;
    @BindView(R.id.container_actualizacion_datos)
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepersonaldata);
        actualizaPrimeraVez = getIntent().getStringExtra("actualizaPrimeraVez");
        datosActualizados = getIntent().getStringExtra("datosActualizados");
        ButterKnife.bind(this);
        setControls();
    }

    @Override
    protected void setControls() {
        basePresenter = new ActivityUpdatePersonalDataPresenter(this);
        context = this;
        datosAsociado = new DatosAsociado();
        isDatosEditados = false;
        basePresenter.loadFragmentUpdatePersonalData(Pantalla.ActDatosStart);
    }

    @OnClick(R.id.btn_back)
    public void onBackPreseed(){
        GlobalState state = getState();
        if(context != null && state != null){
            if(state.getFragmentActual() == Pantalla.ActDatosStart){
                context.salir();
            }
            if(state.getFragmentActual() == Pantalla.ActDatosEditData){
                basePresenter.loadFragmentUpdatePersonalData(Pantalla.ActDatosStart);
            }
            if(state.getFragmentActual() == Pantalla.ActDatosValidateData){
                basePresenter.loadFragmentUpdatePersonalData(Pantalla.ActDatosEditData);
            }
            if(state.getFragmentActual() == Pantalla.ActDatosVerifyCode){
                basePresenter.loadFragmentUpdatePersonalData(Pantalla.ActDatosValidateData);
            }
        }else{
            context.salir();
        }
    }

    @Override
    public void showFragmentUpdatePersonalData(IFragmentCoordinator.Pantalla pantalla) {
        GlobalState state = getState();
        Fragment fragment;
        switch (pantalla) {
            case ActDatosStart:
                fragment = new FragmentStartView();
                break;
            case ActDatosEditData:
                fragment = new FragmentEditDataView();
                break;
            case ActDatosValidateData:
                fragment = new FragmentValidateDataView();
                break;
            case ActDatosVerifyCode:
                fragment = new FragmentVerifyCodeView();
                break;
            case ActDatosFinal:
                fragment = new FragmentFinalView();
                break;
            case MenuPrincipal:
                fragment = new FragmentHomeView();
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
        fragmentManager.beginTransaction().replace(R.id.container_actualizacion_datos, fragment).commit();
    }

}
