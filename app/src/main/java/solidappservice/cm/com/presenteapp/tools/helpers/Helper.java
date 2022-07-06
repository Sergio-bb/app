package solidappservice.cm.com.presenteapp.tools.helpers;


/**
 * CREADO POR JORGE ANDRES DAVID CARDONA 10/06/16.
 */
public class Helper {


    public static String CleanFormatNumber(String text){
        String v= text.replaceAll("[$,.]", "");
        v= v.replaceAll(" ", "");

        return v;
    }

}
