package solidappservice.cm.com.presenteapp.rest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkHelper {

    //PRODUCCION
//    public final static String URL_APIPRESENTEAPP = "https://servicios.presente.com.co/SolidAppService/rest/";//PRESENTEAPP
//    public final static String URL_APICONVENIOS = "https://apps.presente.com.co/servicioconvenios/api/Convenios/"; //CONVENIOS
//    public final static String URL_APIBABEL = "https://servicios.presente.com.co/babel/";//ELECCIONES
//    public final static String URL_APINEQUI = "https://apinq.presente.com.co/api/";//NEQUI
//    public final static String URL_APIMOVILEXITO = "https://einstenio.grupo-exito.com/";//RECARGAS MOVIL EXITO
//    public final static String URL_APIPRESENTEMOVILEXITO = "https://apps.presente.com.co/PresenteME/api/";//PRESENTE-MOVILEXITO
//    public final static String URL_ENDPOINTRECARGAS_APIMOVILEXITO = "apiew/api/v1/DFXRWFBFZM/Suscription/suscribirYrecargar";//ENDPOINT
//    public final static int CERTIFICATE_BAN = 2;// VALOR = 2 INDICA PRODUCTIVO

     //PRUEBAS
    public final static String  URL_APIPRESENTEAPP = "https://servicios.presente.com.co/solidappservicepruebas/rest/";//PRESENTEAPP
    public final static String URL_APICONVENIOS = "https://apps.presente.com.co/servicioconvenios/api/Convenios/"; //CONVENIOS
    public final static String  URL_APIBABEL = "https://servicios.presente.com.co/babel/";//ELECCIONES
    public final static String URL_APINEQUI = "https://apinequipru.presente.com.co/api/";//NEQUI
    public final static String URL_APIMOVILEXITO = "https://kripton.grupo-exito.com/";//RECARGAS MOVIL EXITO
    public final static String URL_APIPRESENTEMOVILEXITO = "https://apps.presente.com.co/PresenteME2/api/";//PRESENTE-MOVILEXITO*/
    public final static String URL_ENDPOINTRECARGAS_APIMOVILEXITO = "apiew/api/v1/VLDEPA3X25J5/Suscription/suscribirYrecargar";//ENDPOINT
    public final static int CERTIFICATE_BAN = 1;// VALOR = 1 INDICA PRUEBAS

    public static boolean isConnectionAvailable(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}