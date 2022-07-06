package solidappservice.cm.com.presenteapp.front.base;

import android.Manifest;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
    public static final long DISCONNECT_TIMEOUT = 3 * 60 * 1000; // 3 min
    private static Handler disconnectHandler = new IncomingHandler();
    public static final int IMEI_REQUEST = 0;
    public static final String[] PERMISSIONS_IMEI = {Manifest.permission.READ_PHONE_STATE};

    protected abstract void setControls() throws Exception;

    @Override
    protected void onResume() {
        state = (GlobalState) getApplicationContext();
        activity = this;
        resetDisconnectTimer();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (state != null && state.getFragmentActual() == IFragmentCoordinator.Pantalla.Ingreso) {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.pop_up_exit_application);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.buttonClose);
            buttonClose.setOnClickListener(view -> dialog.dismiss());
            Button buttonCancelar = (Button) dialog.findViewById(R.id.btnCancelar);
            buttonCancelar.setOnClickListener(view -> dialog.dismiss());
            Button buttonAceptar = dialog.findViewById(R.id.btnAceptar);
            buttonAceptar.setOnClickListener(view -> {
                reiniciarEstado();
                finish();
                dialog.dismiss();
            });
            dialog.show();
        } else if(state != null && state.getFragmentActual() == Pantalla.MenuFinanzas){

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
        } else {
            setFragment(Pantalla.Ingreso);
        }
    }

    public void reiniciarEstado() {
        if (state != null) {
            state.reiniciarEstado();
        }
    }

    //-----Control user inactivity-----
    @Override
    public void onUserInteraction() {
        resetDisconnectTimer();
    }

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

    public static void salir_timer(){
        if (state != null && state.getFragmentActual() != null && state.getFragmentActual() != Pantalla.Ingreso) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.pop_up_closedsession);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button buttonClosedSession = (Button) dialog.findViewById(R.id.btnVolverAIngresar);
            buttonClosedSession.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    state.reiniciarEstado();
                    dialog.dismiss();
                    activity.finish();
                }
            });
            dialog.show();
        }else{
            if(state != null){
                state.reiniciarEstado();
            }
        }
        stopDisconnectTimer();
    }

    public void salir(){
        state.reiniciarEstado();
        finish();
    }




    public void makeLToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    public void makeSToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    public String getMaxTope() {
        String tope = state.getTopeTransacciones();
        double parsed = Double.parseDouble(tope);
        return this.getMoneda(parsed);
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
        return cellPhone.length() <= 10;
    }

    @Override
    public void onStop() {
        super.onStop();
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

    public GlobalState getState() {
        if(state == null) {
            state = (GlobalState) getApplicationContext();
        }
        return state;
    }

    @Override
    public void setFragment(IFragmentCoordinator.Pantalla pantalla) {

    }
    public void hideKeyBoard() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if(imm !=null) imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public String getMoneda(double valor) {
        DecimalFormat formatter = new DecimalFormat("$#,##0.00;$-#,##0.00");
        return formatter.format(valor);
    }

    public String getMonedaWithOutDecimals(double valor) {
        DecimalFormat formatter = new DecimalFormat("$#,###");
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

    public boolean tryParseDouble(String value) {
        try {
            Double.valueOf(value).intValue();
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void guardarConfiguracionTerminos(boolean sw){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putBoolean("pref_terminos", sw).apply();
    }

    public boolean obtenerConfiguracionTerminos(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("pref_terminos", false);
    }

    public String obtenerConfiguracionSecureUrl(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString("pref_secure_url", NetworkHelper.DIRECCION_WS);
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
