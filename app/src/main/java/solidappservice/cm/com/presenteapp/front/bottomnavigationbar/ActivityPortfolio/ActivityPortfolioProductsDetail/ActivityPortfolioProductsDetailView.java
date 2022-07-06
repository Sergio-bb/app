package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityPortfolio.ActivityPortfolioProductsDetail;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponsePortafolio;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 12/05/2017.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 28/08/2021.
 */
public class ActivityPortfolioProductsDetailView extends ActivityBase implements ActivityPortfolioProductsDetailContract.View{

    private ActivityPortfolioProductsDetailPresenter presenter;
    private ActivityBase context;
    private GlobalState state;

    @BindView(R.id.wvDetallePortafolio)
    WebView webView = null;
    @BindView(R.id.btn_back)
    ImageButton btnBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolioproducts_detail);
        ButterKnife.bind(this);
        setControls();
    }

    @Override
    protected void setControls() {
        presenter = new ActivityPortfolioProductsDetailPresenter(this);
        context = this;
        state = context.getState();
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if (state == null) {
            context.salir();
        } else {
            ResponsePortafolio seleccionado = state.getPortafolioSeleccionado();
            showPortfolioProductsDetails(seleccionado);
        }
    }

    @OnClick(R.id.btn_back)
    public void onClickBack(){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void showPortfolioProductsDetails(ResponsePortafolio producto) {
        try {
            String styles = "<style>" +
                    "body{padding:2px 10px;}"+
                    ".card{text-align:justify;margin-top:13px;padding:4px 14px;-webkit-box-shadow: 0px 0px 5px -1px rgba(204,202,204,1);-moz-box-shadow: 0px 0px 5px -1px rgba(204,202,204,1);box-shadow: 0px 0px 5px -1px rgba(204,202,204,1);" +
                    "border-radius: 10px 10px 10px 10px;-moz-border-radius: 10px 10px 10px 10px;-webkit-border-radius: 10px 10px 10px 10px;border: 0px solid #000000;}"+
                    ".card h4{text-align:Center}"+
                    ".card a{text-align:center;text-decoration:none;font-size:16px;color:white;background-color: gray;padding: 5px; 13px;" +
                    "border-radius: 10px 10px 10px 10px;-moz-border-radius: 10px 10px 10px 10px;-webkit-border-radius: 10px 10px 10px 10px;border: 0px solid #000000;}"+
                    "</style>";
            StringBuilder sb = new StringBuilder();
            sb.append("<html>"+styles+"<body>");
            for (ResponsePortafolio.DetallePortafolio item : producto.detalle) {
                sb.append("<div class='card'>");
                sb.append(item.getDescripcionLarga());
                sb.append("</div>");
            }
            sb.append("</body></html>");
            webView.getSettings().setDefaultTextEncodingName("utf-8");
            webView.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);
        } catch (Exception ex) {
            showDataFetchError("Lo sentimos", "Se ha producido un error, inténtalo nuevamente en unos minutos.");
        }
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
                finish();
            }
        });
        dialog.show();
    }

}
