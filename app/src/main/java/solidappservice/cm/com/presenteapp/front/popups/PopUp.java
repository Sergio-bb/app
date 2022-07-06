package solidappservice.cm.com.presenteapp.front.popups;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.tools.helpers.DialogHelpers;

public  class PopUp implements  AutoCloseable {
    public void showExpiredToken(String message, ActivityBase context){
        final Dialog dialog = getDialog(context);
        dialog.setContentView(R.layout.pop_up_closedsession);
        Button buttonClosedSession =  dialog.findViewById(R.id.btnVolverAIngresar);
        buttonClosedSession.setOnClickListener(view -> {
            context.finish();
            dialog.dismiss();
        });
        dialog.show();
    }

    public void showError(String title , String message, ActivityBase context , GlobalState state){
        if(TextUtils.isEmpty(message)){
            message = DialogHelpers.validateMessageDialog(7, state);
        }
        final Dialog dialog = getDialog(context);
        dialog.setContentView(R.layout.pop_up_error);
        TextView titleMessage =  dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText(title);
        TextView contentMessage =  dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        ImageButton buttonClose =  dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(view -> {
            assert state != null;
            state.setTextQR("");
            context.finish();
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void close() throws Exception {

    }
    private Dialog getDialog(ActivityBase context){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

}

