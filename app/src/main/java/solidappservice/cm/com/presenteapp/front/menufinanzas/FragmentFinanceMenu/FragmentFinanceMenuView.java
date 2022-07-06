package solidappservice.cm.com.presenteapp.front.menufinanzas.FragmentFinanceMenu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.login.Response.Usuario;
import solidappservice.cm.com.presenteapp.front.base.main.ActivityMainView;
import solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.ActivityAgreementsView;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA 24/11/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS 17/09/2021.
 */
public class FragmentFinanceMenuView extends Fragment implements FragmentFinanceMenuContract.View {

    private FragmentFinanceMenuPresenter presenter;
    private ActivityMainView context;
    private GlobalState state;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.btnEstadoCuenta)
    Button btnEstadoDeCuenta;
    @BindView(R.id.btnTransacciones)
    Button btnTransacciones;
    @BindView(R.id.btnTarjetaPte)
    Button btnTarjetaPte;
    @BindView(R.id.btnMisMensajes)
    Button btnMisMensajes;
    @BindView(R.id.btnConvenios)
    Button btnConvenios;
    @BindView(R.id.lblHolaUsuario)
    TextView lblHolaUsuario;

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
        params.putString("Descripción", "Interacción con pantalla de menu finanzas");
        firebaseAnalytics.logEvent("pantalla_menu_finanzas", params);
        View view = inflater.inflate(R.layout.fragment_financemenu, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentFinanceMenuPresenter(this);
        context = (ActivityMainView) getActivity();
        state = context.getState();
        if (context != null) {
            context.btn_back.setVisibility(View.VISIBLE);
            context.header.setImageResource(R.drawable.logo_internal);
        }
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
        String user = "Hola " + usuario.getNombreAsociado() +", ";
        if (lblHolaUsuario != null) lblHolaUsuario.setText(user);
    }

    @OnClick(R.id.btnEstadoCuenta)
    public void onClickEstadoCuenta(View v) {context.showScreenStatusAccount();}

    @OnClick(R.id.btnTransacciones)
    public void onClickTransacciones(View v) {context.showScreenTransactionsMenu();}

    @OnClick(R.id.btnTarjetaPte)
    public void onClickTarjetaPresente(View v) {context.showScreenPresenteCard();}

    @OnClick(R.id.btnConvenios)
    public void onClickConvenios(View v) {
        Intent intent_convenios = new Intent(context, ActivityAgreementsView.class);
        startActivity(intent_convenios);
    }

}
