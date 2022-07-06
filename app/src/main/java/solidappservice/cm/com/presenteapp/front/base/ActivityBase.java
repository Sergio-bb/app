package solidappservice.cm.com.presenteapp.front.base;

import android.Manifest;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.number.NumberFormatter;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTabHost;
import androidx.core.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.mensajes.response.ResponseObtenerMensajes;
import solidappservice.cm.com.presenteapp.entities.login.Response.Usuario;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 23/11/2015.
 */
public abstract class ActivityBase extends FragmentActivity implements IFragmentCoordinator{

    private static GlobalState state;
    private static FragmentActivity activity;
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    protected abstract void setControls();

    public void makeLToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    public void makeSToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    public void makeDialog(String titulo, String mensaje) {
        Builder d = new Builder(this);
        d.setTitle(titulo);
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(mensaje);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        d.show();
    }

    public String getMaxTope() {
        String tope = state.getTopeTransacciones();
        double parsed = Double.parseDouble(tope);
        return this.getMoneda(parsed);
    }

    public void makeDialog(String mensaje) {
        makeDialog(getResources().getString(R.string.app_name), mensaje);
    }

    public void makeErrorDialog(String mensaje) {
        makeDialog(getResources().getString(R.string.app_name), mensaje);
    }

    void makeNotification(String titulo, String mensaje, boolean error) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent li = new Intent(getApplicationContext(), ActivityBase.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, li, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon((error ? android.R.drawable.stat_notify_error : android.R.drawable.stat_notify_sync))
                .setContentTitle(titulo)
                .setContentIntent(pi)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentText(mensaje);

        if(manager != null) manager.notify(100, mBuilder.build());
    }

    public static boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validateCellPhone(String cellPhone){
        if(TextUtils.isEmpty(cellPhone)){
            return false;
        }
        if(cellPhone.length() != 10){
            return false;
        }
        if(cellPhone.charAt(0) != '3'){
            return false;
        }
        return true;
    }

    public static boolean validateContactPhone(String cellPhone){
        if(TextUtils.isEmpty(cellPhone)){
            return false;
        }
        if(cellPhone.length() < 7){
            return false;
        }
        if(cellPhone.length() > 10){
            return false;
        }
        return true;
    }


    //-----Control user inactivity-----

    public static final long DISCONNECT_TIMEOUT = 2 * 60 * 1000; // 2 min
    private static Handler disconnectHandler = new IncomingHandler();

    private static class IncomingHandler extends Handler {
        IncomingHandler() {
        }
        @Override
        public void handleMessage(Message msg) {
            Log.d("IncomingHandler", msg.toString());
        }
    }

    private static final Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            salir_timer();
        }
    };

    public static void resetDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }

    public static void stopDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
    }

    @Override
    public void onUserInteraction() {
        resetDisconnectTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        //stopDisconnectTimer();
    }

    public String obtenerIdDispositivo() throws Exception {
        try {
            GlobalState state = (GlobalState) getApplicationContext();
            if(state == null || state.getUsuario() == null){
                salir();
                throw new Exception("La sesión ha finalizado");
            }else{
                Usuario usuario = state.getUsuario();
                String imei = Build.VERSION.SDK_INT <= Build.VERSION_CODES.O ? "" : getImei();
                SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.US);
                String id_pattern = "%s_%s_%s";
                return String.format(id_pattern, usuario.getCedula(), dt.format(new Date()).replace(" ", "-"), imei);
            }
        } catch (Exception ex) {
            throw new Exception("Error obteniendo el ID del dispositivo");
        }
    }

    public String getImei() {
        String imei;
        try {
            TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                if(manager != null){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        imei = manager.getImei();
                    }else{
                        imei = manager.getDeviceId();
                    }
                }else{
                    imei = null;
                }
            } else {
                requestImeiPermission();
                imei = null;
            }
        } catch (Exception e) {
            imei = null;
        }
        return imei;
    }

    public String getIdDispositivo() {
        String idDispositivo;
        try {
            idDispositivo = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            idDispositivo = null;
        }
        return idDispositivo;
    }

    public String getFabricante() {
        String fabricante;
        try {
            fabricante = Build.MANUFACTURER;
        } catch (Exception e) {
            fabricante = null;
        }
        return fabricante;
    }

    public String getModelo() {
        String modelo;
        try {
            modelo = Build.MODEL;
        } catch (Exception e) {
            modelo = null;
        }
        return modelo;
    }

    public String getCelPrincipal() {
        String celPrincipal;
        try {
            TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                if(manager != null){
                    celPrincipal = manager.getLine1Number();
                }else{
                    celPrincipal = null;
                }
            } else {
                requestImeiPermission();
                celPrincipal = null;
            }

        } catch (Exception e) {
            celPrincipal = null;
        }
        return celPrincipal;
    }

    public String getVersionSistemaOperativo() {
        String versionSistemaOperativo = "";
        try {
            int versionSdk = (Build.VERSION.SDK_INT);
            String versionAndroid = (Build.VERSION.RELEASE);
            versionSistemaOperativo = versionSdk + " "+ versionAndroid;
        } catch (Exception e) {
        }
        return versionSistemaOperativo;
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getIP(){
        List<InetAddress> addrs;
        String address = "";
        try{
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for(NetworkInterface intf : interfaces){
                addrs = Collections.list(intf.getInetAddresses());
                for(InetAddress addr : addrs){
                    if(!addr.isLoopbackAddress() && addr instanceof Inet4Address){
                        address = addr.getHostAddress().toUpperCase(new Locale("es", "CO"));
                    }
                }
            }
        }catch (Exception e){
            address = "";
        }
        return address;
    }


    public void reiniciarEstado() {
        if (state != null) {
            state.reiniciarEstado();
        }
    }

    private void requestImeiPermission() {

        Log.i("Imei", "Requesting permission for reading IMEI");

        if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityBase.this,
                Manifest.permission.READ_PHONE_STATE)) {
            //Display message with explanation and a button to trigger the request
            Builder d = new Builder(ActivityBase.this);
            d.setTitle("Solicitud de permiso");
            d.setMessage("Presente solicita permiso para configurar correctamente la aplicación");
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                d.setIcon(getResources().getDrawable(R.mipmap.icon_presente, getTheme()));
            }else{
                d.setIcon(getResources().getDrawable(R.mipmap.icon_presente));
            }
            d.setCancelable(false);
            d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    ActivityCompat.requestPermissions(
                            ActivityBase.this,
                            PERMISSIONS_IMEI,
                            IMEI_REQUEST);
                }
            });
            d.show();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_IMEI,
                    IMEI_REQUEST);
        }
    }

    public static final int IMEI_REQUEST = 0;
    public static final String[] PERMISSIONS_IMEI = {Manifest.permission.READ_PHONE_STATE};

    @Override
    public void onBackPressed() {
        if (state != null && state.getFragmentActual() == Pantalla.Ingreso) {
            Builder d = new Builder(this);
            d.setTitle(getResources().getString(R.string.app_name));
            d.setIcon(R.mipmap.icon_presente);
            d.setMessage("¿Desea salir de la aplicación?");
            d.setCancelable(false);
            d.setPositiveButton("Aceptar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            reiniciarEstado();
                            finish();
                        }
                    });
            d.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            d.show();
        } else if (state != null && state.getFragmentActual() == Pantalla.MenuPrincipal) {
            Builder d = new Builder(this);
            d.setTitle(getResources().getString(R.string.app_name));
            d.setIcon(R.mipmap.icon_presente);
            d.setMessage("¿Desea cerrar sesión?");
            d.setCancelable(false);
            d.setPositiveButton("Aceptar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            reiniciarEstado();
                            setFragment(Pantalla.Ingreso);
                        }
                    });
            d.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            d.show();
        }else if(state != null && (state.getFragmentActual() == Pantalla.Candidatos ||
                state.getFragmentActual() == Pantalla.Votacion )  ) {

            if (state.getFragmentActual() == Pantalla.Candidatos) {
                state.setFragmentActual(Pantalla.MenuPrincipal);

            } else {
                state.setFragmentActual(Pantalla.Candidatos);
            }
            super.onBackPressed();
        }else if(state != null && state.getFragmentActual() == Pantalla.MenuFinanzas){

            setFragment(Pantalla.MenuPrincipal);

        }else if(state != null && state.getFragmentActual() == Pantalla.ConveniosCompraProducto){
            if(state.haveFinishedBuy()){
                setFragment(Pantalla.ConveniosMenuPrincipal);
            }else{
                setFragment(Pantalla.ConveniosProductos);
            }
        }else if(state != null && state.getFragmentActual() == Pantalla.ConveniosProductos){
            if(state.haveJumpedToProducts()){
                setFragment(Pantalla.ConveniosMenuPrincipal);
            }else{
                setFragment(Pantalla.ConveniosLista);
            }
        }else if(state != null && state.getFragmentActual() == Pantalla.ConveniosLista){
            setFragment(Pantalla.ConveniosMenuPrincipal);
        }else if(state != null && state.getFragmentActual() == Pantalla.ConveniosMenuPrincipal){
            state.setFragmentActual(Pantalla.MenuPrincipal);
            super.onBackPressed();
        }else if(state != null && state.getFragmentActual() == Pantalla.ConveniosMELandingME){
            setFragment(Pantalla.ConveniosMenuPrincipal);
        }else if(state != null && state.getFragmentActual() == Pantalla.ConveniosMEMostrarResumen){
            setFragment(Pantalla.ConveniosMELandingME);
        } else {
            setFragment(Pantalla.Ingreso);
        }
    }

    public GlobalState getState() {
        if(state == null) {
            state = (GlobalState) getApplicationContext();
        }
        return state;
    }

    @Override
    protected void onResume() {
        state = (GlobalState) getApplicationContext();
        activity = this;
        resetDisconnectTimer();
        super.onResume();
    }


//    @Override
//    public void ingresar(Usuario usuario) {
//
//    }

    @Override
    public void verMenuFinanzas() {

    }

    @Override
    public void verEstadoCuenta() {

    }

    @Override
    public void verTransacciones() {

    }

    @Override
    public void verTarjetaPresente() {

    }

    @Override
    public void verMisMensajes() {

    }

    @Override
    public void verGeoReferenciacion() {

    }

    @Override
    public void verVotacion() {

    }

    @Override
    public void verCandidatos() {

    }

    @Override
    public void verMenuPrincipal(){

    }

    @Override
    public void verRecuperarClave(){}

//    @Override
//    public void verBorrarCuentas() {
//
//    }
//
//    @Override
//    public void verInscribirCuentas() {
//
//    }

    @Override
    public void setFragment(Pantalla pantalla) {

    }

    public void hideKeyBoard() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if(imm !=null) imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public String getMonedaWithOutDecimals(double valor) {
        DecimalFormat formatter = new DecimalFormat("$#,###");
        return formatter.format(valor);
    }

    public String getMoneda(double valor) {
        DecimalFormat formatter = new DecimalFormat("$#,##0.00;$-#,##0.00");
        return formatter.format(valor);
    }

    public String getMonedaWithDotSeparatorWithOutDecimals(double valor) {
        DecimalFormatSymbols simbols = new DecimalFormatSymbols();
        simbols.setDecimalSeparator(',');
        simbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("$#,###", simbols);
        return formatter.format(valor);
    }

    public String getMonedaWithDotSeparator(double valor) {
        DecimalFormatSymbols simbols = new DecimalFormatSymbols();
        simbols.setDecimalSeparator(',');
        simbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("$#,##0.00", simbols);
        return formatter.format(valor);
    }

    public long getMoneda(String valor) throws Exception {
        DecimalFormat formatter = new DecimalFormat("$#,##0.00;$-#,##0.00");
        return (long) formatter.parse(valor);
    }

    public boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //-----VARIABLES ESTÁTICAS DE LA APLICACIÓN
    public static final int MAX_DIAS_ESTADIA = 30;

    public void salir(){
        state.reiniciarEstado();
        finish();
    }

    public static void salir_timer(){
        if (state != null) {
            state.reiniciarEstado();
        }
        stopDisconnectTimer();
        activity.finish();
    }


    public void showUnreadMessages(ResponseObtenerMensajes mensajeBuzon){
        try{
            GlobalState state = getState();
            if(state != null){

                if(mensajeBuzon != null){
                    state.leerMensaje(mensajeBuzon);
                }

                FragmentTabHost mTabHost = state.getmTabHost();
                if(mTabHost != null){
                    int cant = state.getUnreadMessagesCount();
                    int currentTab = mTabHost.getCurrentTab();
                    if(currentTab == 3) {
                        View view_tab = mTabHost.getCurrentTabView();
                        if(view_tab == null) return;
                        TextView c = view_tab.findViewById(R.id.cantMessages);
                        if(c == null) return;
                        if (cant > 0) {
                            c.setText((cant > 9 ? String.valueOf(cant):" "+String.valueOf(cant)+" "));
                            c.setVisibility(View.VISIBLE);
                        } else {
                            c.setText("");
                            c.setVisibility(View.GONE);
                        }
                    }else{
                        View messages_view = state.getMessages_view();
                        if(messages_view != null){
                            TextView c = messages_view.findViewById(R.id.cantMessages);
                            if(c == null) return;
                            if (cant > 0) {
                                c.setText((cant > 9 ? String.valueOf(cant):" "+String.valueOf(cant)+" "));
                                c.setVisibility(View.VISIBLE);
                            } else {
                                c.setText("");
                                c.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String generateUniqueId() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String id = String.valueOf(timestamp.getTime());
        return id;
    }

    public void guardarConfiguracionTerminos(boolean sw){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putBoolean("pref_terminos", sw).apply();
    }

    public boolean obtenerConfiguracionTerminos(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("pref_terminos", false);
    }

    /*public void guardarConfiguracionSecureUrl(String url){
        //// TODO: 14/12/2015 RECIBIR URL SEGURA DESDE EL SERVICIO Y GUARDARLA
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putString("pref_secure_url", url).apply();
    }*/

    public String obtenerConfiguracionSecureUrl(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString("pref_secure_url", NetworkHelper.URL_APIPRESENTEAPP);
    }

    public void guardarConfiguracionTutorial(boolean seen){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putBoolean("pref_tutorial_seen", seen).apply();
    }

    public boolean obtenerConfiguracionTutorial(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("pref_tutorial_seen", false);
    }
}
