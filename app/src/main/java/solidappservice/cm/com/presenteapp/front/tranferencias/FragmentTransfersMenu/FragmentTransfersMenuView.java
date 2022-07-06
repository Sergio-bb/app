package solidappservice.cm.com.presenteapp.front.tranferencias.FragmentTransfersMenu;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 03/11/2016.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 11/09/2021.
 */
public class FragmentTransfersMenuView extends Fragment implements FragmentTransfersMenuContract.View {

    private FragmentTransfersMenuPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.layoutInscribirCuenta)
    LinearLayout btnlayoutInscribirCuenta;
    @BindView(R.id.layoutEliminarCuenta)
    LinearLayout btnlayoutEliminarCuenta;
    @BindView(R.id.layoutRealizarTransferencia)
    LinearLayout btnlayoutRealizarTransferencia;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de menu transferencias");
        firebaseAnalytics.logEvent("pantalla_menu_transferencias", params);
        View view = inflater.inflate(R.layout.fragment_transfers_menu, container,false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentTransfersMenuPresenter(this);
        context = (ActivityBase) getActivity();
        state = context.getState();
    }

    @OnClick(R.id.layoutInscribirCuenta)
    public void onClickRegisterAccount(){
        btnlayoutInscribirCuenta.setBackgroundColor(Color.parseColor("#F2F2F2"));
        state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_15_TRANSFERS_REGISTER_ACCOUNT_TAG);
    }

    @OnClick(R.id.layoutEliminarCuenta)
    public void onClickDeleteAccount(){
        btnlayoutEliminarCuenta.setBackgroundColor(Color.parseColor("#F2F2F2"));
        state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_16_TRANSFERS_DELETE_ACCOUNT_TAG);
    }

    @OnClick(R.id.layoutRealizarTransferencia)
    public void onClickMakeTransfer(){
        btnlayoutRealizarTransferencia.setBackgroundColor(Color.parseColor("#F2F2F2"));
        state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_18_TRANSFERS_MAKE_TRANSFER_TAG);
    }

}