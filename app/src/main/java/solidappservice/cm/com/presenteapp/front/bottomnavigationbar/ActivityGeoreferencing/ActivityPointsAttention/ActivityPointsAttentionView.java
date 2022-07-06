package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityGeoreferencing.ActivityPointsAttention;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponseLocationsAgencies;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 12/05/2017.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 28/08/2021.
 */
public class ActivityPointsAttentionView extends ActivityBase  implements ActivityPointsAttentionContract.View {

    private ActivityPointsAttentionPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private ResponseLocationsAgencies agencia;

    @BindView(R.id.lblNombrePunto)
    TextView lblNombrePunto;
    @BindView(R.id.lblDireccionPunto)
    TextView lblDireccionPunto;
    @BindView(R.id.lblDescripcion)
    TextView lblDescripcion;
    @BindView(R.id.pin)
    ImageView pin;
    @BindView(R.id.btnComoLlegar)
    Button btnComoLlegar;
    @BindView(R.id.button_close)
    ImageButton buttonClose;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_georeferencing_pointsattention);
        ButterKnife.bind(this);
        setControls();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void setControls() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        presenter = new ActivityPointsAttentionPresenter(this);
        context = this;
        state = context.getState();
        presenter.getPointAttention(context.getState().getNombreAgenciaSeleccionada(),
                context.getState().getAgencias());
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        mostrarDatosAgencia(context.getState().getNombre_agencia_seleccionada());
    }

    @OnClick(R.id.btnComoLlegar)
    public void onClickButtonComoLLegar(){
        try {
            if(agencia != null){
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr="+agencia.getN_latitu()+","+agencia.getN_longit()));
                startActivity(intent);
            }
        } catch (Exception e) {
            showDataFetchError("Lo sentimos", "");
        }
    }

    @OnClick(R.id.button_close)
    public void onClickButtonClose(){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void showPointAttention(String nombrePunto, ResponseLocationsAgencies agencia) {
        lblNombrePunto.setText(nombrePunto);
        lblDireccionPunto.setText(agencia.getD_sucurs());
        lblDescripcion.setText(showDescriptionAgencie(agencia.getContactos()));
        pin.setImageResource(getMapIconId(agencia));
        this.agencia = agencia;
    }

    @Override
    public String showDescriptionAgencie(List<ResponseLocationsAgencies.Contacto> contactos){
        if(contactos == null || contactos.size() <= 0){return "";}
        StringBuilder result = new StringBuilder();
        for (ResponseLocationsAgencies.Contacto contacto: contactos){
            result.append(contacto.toString());
            result.append("\n");
            result.append("\n");
        }
        return result.toString();
    }

    @Override
    public void showDataFetchError(String title, String message){
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
        d.setTitle(title);
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(message);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        d.show();
    }

    private int getMapIconId(ResponseLocationsAgencies agencia) {
        switch (agencia.getI_tipage()) {
            case "SUC":
                return R.drawable.pin_map;
            case "CV":
                return R.drawable.pin_centrosv;
            case "CAJ":
                return R.drawable.pin_cajeros;
            case "ALM":
                if (agencia.getT_sucurs().equalsIgnoreCase("CARULLA") ||
                        agencia.getT_sucurs().equalsIgnoreCase("CARULLA EXPRESS")) {
                    return R.drawable.pin_carulla;
                } else if (agencia.getT_sucurs().equalsIgnoreCase("SURTIMAX")) {
                    return R.drawable.pin_surtimax;
                } else if (agencia.getT_sucurs().equalsIgnoreCase("SUPER INTER")) {
                    return R.drawable.pin_superinter;
                } else if(agencia.getT_sucurs().equalsIgnoreCase("SURTIMAYORISTA")){
                    return R.drawable.surti_mayorista;
                }
                else {
                    return R.drawable.pin_exito;
                }
            default:
                return R.drawable.pin_map;
        }
    }

}

