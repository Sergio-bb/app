package solidappservice.cm.com.presenteapp.tools.helpers;

import static solidappservice.cm.com.presenteapp.tools.constants.Constans.ERROR_CONTACTA_PRESENTE;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;

import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;

public   class DialogHelpers {
    public static Dialog getDialog(Context context, int idContemDialog){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(idContemDialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        return dialog;
    }
    public static String validateMessageDialog(int idMessage, GlobalState state){
        String finalMessage = ERROR_CONTACTA_PRESENTE;
        if(state != null && state.getMensajesRespuesta() != null && state.getMensajesRespuesta().size()>0){
            for(ResponseMensajesRespuesta rm : state.getMensajesRespuesta()){
                if(rm.getIdMensaje() == idMessage){
                    finalMessage = rm.getMensaje();
                }
            }
        }
        return finalMessage;
    }
}
