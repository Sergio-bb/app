package solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.movilexito.FragmentLandingME;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseConsultarDatosAsociado;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseParametrosAPP;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.ActivityAgreementsView;
import solidappservice.cm.com.presenteapp.rest.retrofit.apiconvenios.ConveniosRestClient;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

public class FragmentLandingMEView extends Fragment implements FragmentLandingMEContract.View{

    private FragmentLandingMEPresenter presenter;
    private ActivityAgreementsView context;
    private ActivityBase activityBase;
    private GlobalState state;
    private ProgressDialog pd;
    private ResponseParametrosAPP paramsMovilExito;
    private ResponseConsultarDatosAsociado datosAsociado;

    @BindView(R.id.webViewMovilExito)
    WebView webViewMovilExito;
    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;
//    @BindView(R.id.btnRealizarCompra)
//    Button btnRealizarCompra;

    @BindView(R.id.layout_circular_progress_bar)
    LinearLayout layoutCircularProgressBar;
    @BindView(R.id.circular_progress_bar)
    ProgressBar circularProgressBar;
    @BindView(R.id.text_circular_progress_Bar)
    TextView textCircularProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agreements_me_landingme, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    public void setControls() {
        presenter = new FragmentLandingMEPresenter(this, new FragmentLandingMEModel());
        context = (ActivityAgreementsView) getActivity();
        activityBase = (ActivityBase) getActivity();
        state = context.getState();
//        pd = new ProgressDialog(context);
        fetchUrlLandingMovilExito();
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                openLandingMovilExito(datosAsociado);
            }
        });
    }

    @Override
    public void fetchUrlLandingMovilExito(){
        try{
            presenter.getUrlLandingMovilExito();
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void resultUrlLandingMovilExito(ResponseParametrosAPP params){
        this.paramsMovilExito = params;
        state.setParamsMovilExito(params);
    }

    @Override
    public void fetchAssociatedData() {
        presenter.getAssociatedData(new BaseRequest(
                Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
                state.getUsuario().getToken()
        ));
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void openLandingMovilExito(ResponseConsultarDatosAsociado asociado) {
        datosAsociado = asociado;
        String nombre = datosAsociado.getNombreCompleto();
        String cedula = datosAsociado.getCedula();
        String idTransaccionPresente = activityBase.generateUniqueId();
        state.setIdTransaccionPresente(idTransaccionPresente);
        String correo = datosAsociado.getEmail();
        String origen = "APP";

        String urlParameters = Encripcion.getInstance().encryptAES(
        nombre + "|" +
            cedula + "|" +
            idTransaccionPresente + "|" +
            correo + "|" +
            origen);

        webViewMovilExito.getSettings().setJavaScriptEnabled(true);
        webViewMovilExito.getSettings().setUseWideViewPort(true);  //ajusta la pagina al tamaño del webView
        webViewMovilExito.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webViewMovilExito.getSettings().setAllowUniversalAccessFromFileURLs(false);
        webViewMovilExito.setWebChromeClient(new WebChromeClient());
        if(state.getParamsMovilExito() != null && state.getParamsMovilExito().getValue4().equals("Y")){
            webViewMovilExito.loadUrl("file:///android_asset/index.html");
        }else{
            webViewMovilExito.loadUrl(paramsMovilExito.getValue1() + urlParameters);
        }
        webViewMovilExito.addJavascriptInterface(new MyJavaCallback(), "JCB");
        webViewMovilExito.clearFormData(); //Elimina la ventana emergente de autocompletar del campo de formulario actualmente enfocado, si está presente

//        webViewMovilExito.getSettings().setLoadsImagesAutomatically(true); //indica que se cargan recursos de imagen
//        webViewMovilExito.getSettings().setBlockNetworkImage(true); //es por si las imagenes que se cargan utilizab internet
//        webViewMovilExito.getSettings().setDomStorageEnabled(true);
//        webViewMovilExito.getSettings().setDatabaseEnabled(true);
//        webViewMovilExito.getSettings().setSaveFormData(true);
//        webViewMovilExito.loadUrl("file:///android_asset/index.html");
        webViewMovilExito.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == keyEvent.KEYCODE_BACK && webViewMovilExito.canGoBack()) {
                    webViewMovilExito.goBack();
                    return true;
                }
                return false;
            }
        });
//        webViewMovilExito.setWebViewClient(new WebViewClient());
        webViewMovilExito.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pullToRefresh.setRefreshing(false);
                if(state.getParamsMovilExito() != null && state.getParamsMovilExito().getValue4().equals("Y")){
                    webViewMovilExito.loadUrl("javascript:loadVariables('"+idTransaccionPresente+"')");
                }
                showContentWebView();
                hideCircularProgressBar();
            }
        });
        webViewMovilExito.setWebChromeClient(new WebChromeClient());
//        webViewMovilExito.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onCloseWindow(WebView window) {
//                super.onCloseWindow(window);
////                webViewMovilExito.clearHistory();
////                ((ViewGroup) webViewMovilExito.getParent()).removeView(webViewMovilExito);
////                webViewMovilExito.destroy();
//                state.setDatosAsociado(datosAsociado);
//                context.setFragment(IFragmentCoordinator.Pantalla.ConveniosMEMostrarResumen);
//            }
//
//            @Override
//            public void onPermissionRequest(PermissionRequest request) {
//                super.onPermissionRequest(request);
//            }
//
//            @Override
//            public void onPermissionRequestCanceled(PermissionRequest request) {
//                super.onPermissionRequestCanceled(request);
//            }
//        });
    }

    final public class MyJavaCallback {
        // this annotation is required in Jelly Bean and later:
        @JavascriptInterface
        public void finishActivity() {
//            webViewMovilExito.destroy();
            state.setDatosAsociado(datosAsociado);
            context.setFragment(IFragmentCoordinator.Pantalla.ConveniosMEMostrarResumen);
        }
    }


    @Override
    public void showContentWebView(){
        pullToRefresh.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCircularProgressBar(String textProgressBar) {
        layoutCircularProgressBar.setVisibility(View.VISIBLE);
        textCircularProgressBar.setText(textProgressBar);
    }

    @Override
    public void hideCircularProgressBar() {
        layoutCircularProgressBar.setVisibility(View.GONE);
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
                context.onBackPressed();
                dialog.dismiss();
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
                context.onBackPressed();
                dialog.dismiss();
            }
        });
        d.show();
    }

//    @Override
//    public void mostrarMensajeError() {
//        builderMensaje = new AlertDialog.Builder(context);
//        builderMensaje.setIcon(R.drawable.icon_presente);
//        builderMensaje.setTitle("¡Upsss!");
//        builderMensaje.setMessage(R.string.mensajeError);
//        builderMensaje.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                pd.dismiss();
//                context.setFragment(IFragmentCoordinator.Pantalla.ConveniosMenuPrincipal);
//            }
//        });
//        builderMensaje.setCancelable(false);
//        builderMensaje.show();
//    }

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

//    public void enviarCorreoError(){
//        String contenido = "Ocurrio un fallo en la transaccion. Las causas pueden ser: por time out(innactividad) o debido a que no se completo la recarga";
//        presenter.postEmailFallo(contenido);
//    }

    /*public void setControls() {
        presenter = new FragmentWebViewPresenter(this, new FragmentWebViewModel());
        context = (ActivityAgreementsView) getActivity();
        activityBase = (ActivityBase) getActivity();
        state = activityBase.getState();
        encripcion = Encripcion.getInstance();

        prgsbarCargandoWebView.setMax(100);
        dialog = new ProgressDialog(context);

        int creacionIdTransaccionPResente = generateUniqueId();
        state.setIdTransaccionPresente(String.valueOf(creacionIdTransaccionPResente));
        state.setFragmentActual(IFragmentCoordinator.Pantalla.WebViewMovilExito);

        //presenter
        usuario = state.getUsuario();
        cedulaEncriptada = state.getUsuarioEncriptado_cedula();
        presenter.goToUrlWebView();

        swprfsWebView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                levantarWebView();
            }
        });
    }

    public void setControlsUrl(String ruta){
        URL_WEBVIEW = ruta;
    }

    public void setControlsAsociado(ResponseConsultarDatosAsociado asociado){
        datosAsociado = asociado;
    }

    public void mostrarMensajeError() {
        builderMensaje = new AlertDialog.Builder(context);
        builderMensaje.setIcon(R.drawable.icon_presente);
        builderMensaje.setTitle("¡Upsss!");
        builderMensaje.setMessage(R.string.mensajeError);
        builderMensaje.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
                context.setFragment(IFragmentCoordinator.Pantalla.ConveniosMenuPrincipal);
            }
        });
        builderMensaje.setCancelable(false);
        builderMensaje.show();
    }

    public void message() {
        dialog.setIcon(R.mipmap.icon_presente);
        dialog.setTitle(context.getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.setMessage("Cargando información...");
        dialog.show();
    }

    public void dismissMessage(){
        dialog.dismiss();
    }

    public void levantarWebView() {
        webViewMovilExito.getSettings().setJavaScriptEnabled(true);
        webViewMovilExito.getSettings().setAllowFileAccess(true); //permitir acceso a los archivos
        webViewMovilExito.getSettings().setAllowFileAccessFromFileURLs(true); //permitir que el código Js cargado por la URL lea otros archivos locales
        webViewMovilExito.getSettings().setUseWideViewPort(true);  //ajusta la pagina al tamaño del webView
        webViewMovilExito.getSettings().setLoadWithOverviewMode(true);
        webViewMovilExito.getSettings().setLoadsImagesAutomatically(true); //indica que se cargan recursos de imagen
        //webViewMovilExito.getSettings().setBlockNetworkImage(true); //es por si las imagenes que se cargan utilizab internet

        webViewMovilExito.getSettings().setDomStorageEnabled(true);
        webViewMovilExito.getSettings().setDatabaseEnabled(true);
        webViewMovilExito.getSettings().setSaveFormData(true);
        webViewMovilExito.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        //webViewMovilExito.loadUrl(URL_WEBVIEW + token);
        webViewMovilExito.loadUrl("file:///android_asset/index.html");
        webViewMovilExito.clearFormData(); //Elimina la ventana emergente de autocompletar del campo de formulario actualmente enfocado, si está presente
        webViewMovilExito.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == keyEvent.KEYCODE_BACK && webViewMovilExito.canGoBack()) {
                    webViewMovilExito.goBack();
                    return true;
                }
                return false;
            }
        });
        webViewMovilExito.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                swprfsWebView.setRefreshing(false);
                prgsbarCargandoWebView.setVisibility(View.GONE);
                dismissMessage();
            }
        });
        webViewMovilExito.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onCloseWindow(WebView window) {
                super.onCloseWindow(window);
                destruirWebView();
                //Toast.makeText(getContext(), "se cerro el webView", Toast.LENGTH_SHORT).show();
                goShowResumen();
            }

            @Override
            public void onPermissionRequest(PermissionRequest request) {
                super.onPermissionRequest(request);
            }

            @Override
            public void onPermissionRequestCanceled(PermissionRequest request) {
                super.onPermissionRequestCanceled(request);
            }
        });
    }

    public void destruirWebView() {
        webViewMovilExito.clearHistory();
        ((ViewGroup) webViewMovilExito.getParent()).removeView(webViewMovilExito);
        webViewMovilExito.destroy();
    }

    public void goShowResumen() {
        state.setDatosAsociado(datosAsociado);
        context.setFragment(IFragmentCoordinator.Pantalla.MostrarResumen);
    }

    public void cargarDatosLanding() {
        nombre = datosAsociado.getNombreCompleto();
        cedula = datosAsociado.getCedula();
        idTransaccionPresente = state.getIdTransaccionPresente();
        correo = datosAsociado.getEmail();
        origen = "app";

        token = encripcion.encryptAES(nombre + "|" + cedula + "|" + idTransaccionPresente + "|" + correo + "|" + origen);
        levantarWebView();
    }

    public int generateUniqueId() {
        String timeStamp = new SimpleDateFormat("yyyymmss").format(Calendar.getInstance().getTime());
        String cadena = String.valueOf(System.currentTimeMillis());
        char[] cadenaMilisegundos = cadena.toCharArray();
        String milisegundos = "";

        for (int i = 0; i < cadenaMilisegundos.length; i++) {
            if (i > (cadenaMilisegundos.length - 3)){
                milisegundos += cadenaMilisegundos[i];
            }
        }
        return Integer.parseInt(timeStamp + milisegundos);
    }


    public void goToBuscarDatosAsociado() {
        presenter.getDataAsociado(usuario, cedulaEncriptada);
    }*/
}