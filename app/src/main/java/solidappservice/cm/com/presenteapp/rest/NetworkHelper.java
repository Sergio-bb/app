package solidappservice.cm.com.presenteapp.rest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;


public class NetworkHelper {

public  static  String direccion_ws ="";

    //PRODUCCION
  /*public final static String  DIRECCION_WS = "https://servicios.presente.com.co/SolidAppService/rest/";//PRESENTEAPP
    public final static String CONVENIOS_WS = "https://apps.presente.com.co/servicioconvenios/api/Convenios/"; //CONVENIOS
    public final static String  ELECCIONES_WS = "https://servicios.presente.com.co/babel/";//ELECCIONES
    public final static String NEQUI_WS = "https://apinq.presente.com.co/api/";//NEQUI*/

    public final static String  DIRECCION_WS = "https://servicios.presente.com.co/solidappservicepruebas/rest/";//PRESENTEAPP
    public final static String CONVENIOS_WS = "https://apps.presente.com.co/servicioconvenios/api/Convenios/"; //CONVENIOS
    public final static String  ELECCIONES_WS = "https://servicios.presente.com.co/babel/";//ELECCIONES
    public final static String NEQUI_WS = "https://apinequipru.presente.com.co/api/";//NEQUI*/

    public NetworkHelper(String url_ws){
        direccion_ws = url_ws;
    }

    public NetworkHelper(){
        direccion_ws = DIRECCION_WS;
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable();
        }
        return false;
    }
}