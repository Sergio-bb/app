package solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Resumen;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.agreements.FragmentMainMenuAgreements.FragmentAgreementsMainMenuView;
import solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.agreements.FragmentBuyAgreementsProducts.FragmentBuyAgreementsProductsView;
import solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.agreements.FragmentListAgreements.FragmentListAgreementsView;
import solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.agreements.FragmentListAgrementsProducts.FragmentListAgrementsProductsView;
import solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.movilexito.FragmentLandingME.FragmentLandingMEView;
import solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.movilexito.FragmentTransactionSummary.FragmentTransactionSummaryView;
import solidappservice.cm.com.presenteapp.rest.retrofit.apiconvenios.ConveniosRestClient;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;

/**
 * CREADA POR JORGE ANDRÉS DAVID CARDONA EL 31/07/2018.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public class ActivityAgreementsView extends ActivityBase implements ActivityAgreementsContract.View {

    private ActivityAgreementsPresenter presenter;
    private ActivityBase context;
    private ProgressDialog pd;
    private GlobalState state;

    @BindView(R.id.header)
    public ImageView header;
    @BindView(R.id.btn_back)
    public ImageButton btn_back;
    @BindView(R.id.btnSalir)
    public TextView btnSalir;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreements);
        state = getState();
        ButterKnife.bind(this);
        setControls();
        //setResumen(MockupConvenios.getResumen());
//        new ResumenConveniosTask().execute();
    }

    @Override
    protected void setControls() {
        presenter = new ActivityAgreementsPresenter(this, new ActivityAgreementsModel());
        context = this;
        state = context.getState();
        pd = new ProgressDialog(context);
        fetchAgreements();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.btnSalir)
    public void onClickSalir(View v) {
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle(getResources().getString(R.string.app_name));
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage("¿Desea cerrar sesión?");
        d.setCancelable(false);
        d.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        context.reiniciarEstado();
                        state.setmTabHost(null);
                        finish();
                    }
                });
        d.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        d.show();
    }

    @OnClick(R.id.btn_back)
    public void onClickBack(View v) {
        onBackPressed();
    }

    @Override
    public void setFragment(IFragmentCoordinator.Pantalla pantalla) {
        Fragment fragment;

        switch (pantalla) {
            case ConveniosMenuPrincipal:
                fragment = new FragmentAgreementsMainMenuView();
                break;
            case ConveniosLista:
                fragment = new FragmentListAgreementsView();
                break;
            case ConveniosProductos:
                fragment = new FragmentListAgrementsProductsView();
                break;
            case ConveniosCompraProducto:
                fragment = new FragmentBuyAgreementsProductsView();
                break;
            case ConveniosMEMostrarResumen:
                fragment = new FragmentTransactionSummaryView();
                break;
            case ConveniosMELandingME:
                fragment = new FragmentLandingMEView();
                break;
//            case Ingreso:
//                fragment = new FragmentLoginView();
//                break;
//            case MenuPrincipal:
//                fragment = new FragmentFinanceMenuView();
//                break;
            default:
                finish();
                return;
        }
        if (state != null && state.getFragmentActual() != null) {
            state.setFragmentAnterior(state.getFragmentActual());
        }
        hideKeyBoard();
        state.setFragmentActual(pantalla);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    @Override
    public void fetchAgreements(){
        try{
            presenter.fetchAgreements(new BaseRequest(
                    state.getUsuario().getCedula(),
                    state.getUsuario().getToken()
            ));
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void showResultAgreements(Resumen resumen){
        state.setResumen(resumen);
        setFragment(Pantalla.ConveniosMenuPrincipal);
    }

    @Override
    public void showProgressDialog(String message) {
        pd.setTitle(context.getResources().getString(R.string.app_name));
        pd.setMessage(message);
        pd.setIcon(R.mipmap.icon_presente);
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    public void hideProgressDialog() {
        pd.dismiss();
    }

    @Override
    public void showErrorTimeOut() {
        String message = "Ha ocurrido un error. Intenta de nuevo y si el error persiste, contacta a PRESENTE.";
        if(state != null && state.getMensajesRespuesta() != null && state.getMensajesRespuesta().size()>0){
            for(ResponseMensajesRespuesta rm : state.getMensajesRespuesta()){
                if(rm.getIdMensaje() == 6){
                    message = rm.getMensaje();
                }
            }
        }
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle(context.getResources().getString(R.string.app_name));
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(message);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        d.show();
    }

    @Override
    public void showDataFetchError(String message) {
        if(TextUtils.isEmpty(message)){
            message = "Ha ocurrido un error. Intenta de nuevo y si el error persiste, contacta a PRESENTE.";
            if(state != null && state.getMensajesRespuesta() != null && state.getMensajesRespuesta().size()>0){
                for(ResponseMensajesRespuesta rm : state.getMensajesRespuesta()){
                    if(rm.getIdMensaje() == 7){
                        message = rm.getMensaje();
                    }
                }
            }
        }
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle(context.getResources().getString(R.string.app_name));
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(message);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        d.show();
    }

    @Override
    public void showExpiredToken(String message) {
        if(message == ConveniosRestClient.TOKEN_EXCEPTION){
            message = "Por favor ingrese nuevamente";
        }
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle("Sesión finalizada");
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(message);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                context.salir();
            }
        });
        d.show();
    }

//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    public void setFragment(IFragmentCoordinator.Pantalla pantalla) {
//        Fragment fragment;
//
//        switch (pantalla) {
//            case ConveniosMenuPrincipal:
//                fragment = new FragmentAgreementsMainMenuView();
//                break;
//            case ConveniosLista:
//                fragment = new FragmentConveniosLista();
//                break;
//            case ConveniosProductos:
//                fragment = new FragmentConveniosProductos();
//                break;
//            case ConveniosCompraProducto:
//                fragment = new FragmentConveniosCompraProducto();
//                break;
//            case Ingreso:
//                fragment = new FragmentLogin();
//                break;
//            case MenuPrincipal:
//                fragment = new FragmentMenuFinanzas();
//                break;
//            default:
//                fragment = new FragmentLogin();
//                break;
//        }
//
//        if (state != null && state.getFragmentActual() != null) {
//            state.setFragmentAnterior(state.getFragmentActual());
//        }
//
//        hideKeyBoard();
//        state.setFragmentActual(pantalla);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
//    }
//
//    private class ResumenConveniosTask extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Consultando portafolio...");
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//
//                Usuario usuario = state.getUsuario();
//                ResumenResult result = new ConveniosRestClient()
//                        .resumeCall(usuario.token, usuario.cedula).execute().body();
//
//                if(result == null){
//                    return ConveniosRestClient.SERVER_ERROR;
//                }
//
//                if(result.isErrorAutenticacion()){
//                    return ConveniosRestClient.TOKEN_EXCEPTION;
//                }
//
//                if(result.isExitoso()){
//                    Resumen resumen = new Resumen();
//                    if(result.getRespuesta() != null){
//                        resumen.setEmailAsociado(result.getRespuesta().getEmailAsociado());
//                        resumen.setUbicacionAsociado(result.getRespuesta().getUbicacionAsociado());
//                        resumen.setCategorias(result.getRespuesta().generateCategorias());
//                        resumen.setConvenios(result.getRespuesta().generateConvenios());
//                        resumen.setCiudades(result.getRespuesta().generateCiudades());
//                    }
//
//                    setResumen(resumen);
//                    return ConveniosRestClient.RESULT_OK;
//
//                }else{
//                    return result.getDescripcionError();
//                }
//
//            } catch (Exception e) {
//                return e.getMessage();
//            }
//        }
//
//        @Override
//        protected void onProgressUpdate(String... values) {
//            pd.setMessage(values[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            pd.dismiss();
//            procesarJsonConvenios(result);
//        }
//
//    }
//
//    private void procesarJsonConvenios(String result) {
//
//        if (result.equals(ConveniosRestClient.RESULT_OK)) {
//            setFragment(Pantalla.ConveniosMenuPrincipal);
//            return;
//        }
//
//        if (result.equals(ConveniosRestClient.TOKEN_EXCEPTION)) {
//            AlertDialog.Builder d = new AlertDialog.Builder(context);
//            d.setTitle("Sesión finalizada");
//            d.setIcon(R.mipmap.icon_presente);
//            d.setMessage("Por favor ingrese nuevamente");
//            d.setCancelable(false);
//            d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    context.salir();
//                }
//            });
//            d.show();
//        }else {
//            context.makeErrorDialog(result);
//        }
//
//    }
//
//    private void setResumen(Resumen resumen){
//        GlobalState state = getState();
//        state.setResumen(resumen);
//    }
}
