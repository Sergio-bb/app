package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityPortfolio.ActivityPortfolioProductsDetail;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponsePortafolio;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 12/05/2017.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 28/08/2021.
 */
public class ActivityPortfolioProductsDetailView extends ActivityBase implements ActivityPortfolioProductsDetailContract.View{

    private ActivityPortfolioProductsDetailPresenter presenter;
    private ActivityBase context;

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
//            cargarProductos(seleccionado);
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
            context.makeErrorDialog("Error cargando los productos");
        }
    }

//    private void cargarProductos(ResponsePortafolio producto) {
//        try {
//            StringBuilder sb = new StringBuilder();
//            sb.append("<html><body>");
//            for (ResponsePortafolio.DetallePortafolio item : producto.detalle) {
//                //sb.append("<h3>");
//                //sb.append(item.getNombre());
//                //sb.append("</h3>");
//                sb.append(item.getDescripcionLarga());
//                sb.append("<hr>");
//            }
//            sb.append("</body></html>");
//            webView.getSettings().setDefaultTextEncodingName("utf-8");
//            webView.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);
//        } catch (Exception ex) {
//            context.makeErrorDialog("Error cargando los productos");
//        }
//    }

}
