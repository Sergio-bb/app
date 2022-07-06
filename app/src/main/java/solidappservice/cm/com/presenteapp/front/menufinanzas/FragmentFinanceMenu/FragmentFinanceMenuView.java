package solidappservice.cm.com.presenteapp.front.menufinanzas.FragmentFinanceMenu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
import solidappservice.cm.com.presenteapp.front.base.ActivityMainView;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityDirectory.ActivityDirectoryView;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityGeoreferencing.ActivityLocationsGms.ActivityLocationsGmsView;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityGeoreferencing.ActivityLocationsHms.ActivityLocationsHmsView;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityPortfolio.ActivityPortfolioProducts.ActivityPortfolioProductsView;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityServices.ActivityServicesView;
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
    @BindView(R.id.btnGeoReferenciacion)
    Button btnGeoReferenciacion;
    @BindView(R.id.btnPortafolio)
    ImageButton btnPortafolio;
    @BindView(R.id.btnPreguntasFrecuentes)
    ImageButton btnPreguntasFrecuentes;
    @BindView(R.id.btnDirectorio)
    ImageButton btnDirectorio;
    @BindView(R.id.btnEncuentranos)
    ImageButton btnEncuentranos;
    @BindView(R.id.lblHolaUsuario)
    TextView lblHolaUsuario;

    //@BindView(R.id.btnElecciones)
    //Button btnElecciones;


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
            context.btnSalir.setVisibility(View.VISIBLE);
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
        String user = "Hola " + usuario.getNombreAsociado();
        if (lblHolaUsuario != null) lblHolaUsuario.setText(user);
        //new EleccionesTask().execute();
    }

    @OnClick(R.id.btnEstadoCuenta)
    public void onClickEstadoCuenta(View v) {context.verEstadoCuenta();}

    @OnClick(R.id.btnTransacciones)
    public void onClickTransacciones(View v) {context.verTransacciones();}

    @OnClick(R.id.btnTarjetaPte)
    public void onClickTarjetaPresente(View v) {context.verTarjetaPresente();}

    @OnClick(R.id.btnMisMensajes)
    public void onClickMisMensajes(View v) {context.verMisMensajes();}

    @OnClick(R.id.btnConvenios)
    public void onClickConvenios(View v) {
        Intent intent_convenios = new Intent(context, ActivityAgreementsView.class);
        startActivity(intent_convenios);
    }

    @OnClick(R.id.btnGeoReferenciacion)
    public void onClickGeoreferenciacion(View v) {context.verGeoReferenciacion();}

    @OnClick(R.id.btnPortafolio)
    public void onClickPortafolio(View v) {
        Intent intent_p = new Intent(context, ActivityPortfolioProductsView.class);
        startActivity(intent_p);
    }

    @OnClick(R.id.btnPreguntasFrecuentes)
    public void onClickPreguntasFrecuentes(View v) {
        Intent intent_s = new Intent(context, ActivityServicesView.class);
        startActivity(intent_s);
    }

    @OnClick(R.id.btnDirectorio)
    public void onClickDirectorio(View v) {
        Intent intent_dir = new Intent(context, ActivityDirectoryView.class);
        startActivity(intent_dir);
    }

    @OnClick(R.id.btnEncuentranos)
    public void onClickEncuentranos(View v) {
        if(state != null && state.isHmsSystem()){
            Intent intent = new Intent(context, ActivityLocationsHmsView.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(context, ActivityLocationsGmsView.class);
            startActivity(intent);
        }
    }

//    @OnClick(R.id.btnElecciones);
//    public void onClickEleccioes(View v) {context.verCandidatos();}




    /*private class EleccionesTask extends AsyncTask<String, String, String> {

        String cedula = null;
        String token = null;

        @Override
        protected String doInBackground(String... params) {
            try {
                NetworkHelper networkHelper = new NetworkHelper(NetworkHelper.ELECCIONES_WS);
                String request = "MostrarEleccion.php";
                return networkHelper.readService(request);
            } catch (Exception e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            procesarResultJSONMostrarElecciones(result);
        }
    }

    private void procesarResultJSONMostrarElecciones(String result){
        try {
            String estado = SincroHelper.procesarJsonMostrarElecciones(result);

            if(estado.equals("1")){
                btnElecciones.setVisibility(View.VISIBLE);
            }
        } catch (ErrorTokenException e) {
                AlertDialog.Builder d = new AlertDialog.Builder(context);
                d.setTitle("Sesión finalizada");
                d.setIcon(R.mipmap.icon_presente);
                d.setMessage(e.getMessage());
                d.setCancelable(false);
                d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        context.salir();
                    }
                });
                d.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }*/

}
