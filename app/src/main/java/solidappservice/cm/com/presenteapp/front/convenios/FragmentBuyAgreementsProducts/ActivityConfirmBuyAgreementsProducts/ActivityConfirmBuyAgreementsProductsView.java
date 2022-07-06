package solidappservice.cm.com.presenteapp.front.convenios.FragmentBuyAgreementsProducts.ActivityConfirmBuyAgreementsProducts;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Resumen;
import solidappservice.cm.com.presenteapp.entities.convenios.request.RequestSolicitudProducto;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 31/07/2018.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */

public class ActivityConfirmBuyAgreementsProductsView extends AppCompatActivity implements ActivityConfirmBuyAgreementsProductsContract.View{

    @BindView(R.id.tv_producto_nombre)
    TextView tv_producto_nombre;
    @BindView(R.id.tv_beneficio)
    TextView tv_beneficio;
    @BindView(R.id.tv_valor)
    TextView tv_valor;
    @BindView(R.id.tv_forma_pago)
    TextView tv_forma_pago;
    @BindView(R.id.tv_email)
    TextView tv_email;
    @BindView(R.id.lblCelular)
    TextView lbl_celular;
    @BindView(R.id.tv_celular)
    TextView tv_celular;
    @BindView(R.id.btnAtras)
    Button btnAtras;
    @BindView(R.id.btnConfirmar)
    Button btnConfirmar;
    @BindView(R.id.button_close)
    ImageButton buttonClose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_agreements_confirmbuyproduct);
        ButterKnife.bind(this);
        setControls();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    protected void setControls() {
        if (android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        showDetailsBuyProduct();
    }

    @Override
    public void onBackPressed() {
        Log.d("Presente", "on back pressed canceled");
    }

    @OnClick(R.id.btnAtras)
    public void onClickAtras(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }

    @OnClick(R.id.btnConfirmar)
    public void onClickConfirmar(View v) {
        setResult(RESULT_OK);
        finish();
    }

    @OnClick(R.id.button_close)
    public void onClickButtonClose(){
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void showDetailsBuyProduct(){
        GlobalState state = (GlobalState) getApplicationContext();
        Resumen resumen = state.getResumen();
        if(resumen != null){
            RequestSolicitudProducto solicitudProducto = resumen.getSolicitudProducto();
            tv_producto_nombre.setText(solicitudProducto.getNombreProducto());
            tv_beneficio.setText(solicitudProducto.getBeneficio());
            tv_valor.setText(solicitudProducto.getValor());
            tv_forma_pago.setText(solicitudProducto.getNombreFormaPago());
            tv_email.setText(solicitudProducto.getEmail());
            lbl_celular.setText(solicitudProducto.getEtiquetaCelular());
            tv_celular.setText(solicitudProducto.getCelular());
        }else{
            showDataFetchError("Lo sentimos", "Se ha producido un error, inténtalo nuevamente en unos minutos.");
        }
    }

    @Override
    public void showDataFetchError(String title, String message){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_error);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView titleMessage = (TextView) dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText(title);
        TextView contentMessage = (TextView) dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        dialog.show();
    }
}
