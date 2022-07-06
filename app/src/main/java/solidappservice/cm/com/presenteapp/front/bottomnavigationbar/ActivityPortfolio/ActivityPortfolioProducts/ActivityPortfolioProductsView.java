package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityPortfolio.ActivityPortfolioProducts;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.bottomnavigationbar.PortafolioPadreAdapter;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponsePortafolio;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.dto.PortafolioPadre;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityPortfolio.ActivityPortfolioProductsExpanded.ActivityPortfolioProductsExpandedView;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityPortfolio.ActivityPortfolioProductsDetail.ActivityPortfolioProductsDetailView;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 12/05/2017.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 28/08/2021.
 */
public class ActivityPortfolioProductsView extends ActivityBase implements ActivityPortfolioProductsContract.View{

    private ActivityPortfolioProductsPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private List<ResponsePortafolio> productos = null;
    private List<PortafolioPadre> categorias = null;

    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.list_portafolio)
    ListView list_productos;

    @BindView(R.id.layout_circular_progress_bar)
    LinearLayout layoutCircularProgressBar;
    @BindView(R.id.circular_progress_bar)
    ProgressBar circularProgressBar;
    @BindView(R.id.text_circular_progress_Bar)
    TextView textCircularProgressBar;
    @BindView(R.id.imageReferesh)
    ImageView buttonReferesh;
    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolioproducts);
        ButterKnife.bind(this);
        setControls();
    }

    @Override
    protected void setControls() {
        presenter = new ActivityPortfolioProductsPresenter(this, new ActivityPortfolioProductsModel());
        context = this;
        state = context.getState();
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                state.setPortafolio(null);
                fetchPortfolioProducts();
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null){
            context.salir();
        }else {
            fetchPortfolioProducts();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.btn_back)
    public void onClickBack(){
        onBackPressed();
    }

    @OnClick(R.id.imageReferesh)
    public void onClickRefresh(){
        state.setPortafolio(null);
        fetchPortfolioProducts();
    }


    @OnItemClick(R.id.list_portafolio)
    void onItemClickDirectorio(AdapterView<?> parent, View view, int position, long id) {
        if(categorias != null){
            PortafolioPadre pp = categorias.get(position);
            state.setCategoriaPortafolioSeleccionada(pp);
            if(pp.getPortafolios().size() == 1){
                state.setPortafolioSeleccionado(pp.getPortafolios().get(0));
                Intent intent = new Intent(this, ActivityPortfolioProductsDetailView.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(this, ActivityPortfolioProductsExpandedView.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void fetchPortfolioProducts() {
        if (state.getPortafolio() == null || state.getPortafolio().size() == 0) {
            presenter.fetchPortfolioProducts();
        }else{
            hideCircularProgressBar();
            showSectionPortfolioProducts();
            showPortfolioProducts(state.getPortafolio(), state.getCategoriasPortafolio());
        }
    }

    @Override
    public void showPortfolioProducts(List<ResponsePortafolio> productos, List<PortafolioPadre> portafolioPadres) {
        try {
            state.setCategoriasPortafolio(portafolioPadres);
            state.setPortafolio(productos);
            this.categorias = portafolioPadres;
            PortafolioPadreAdapter pa = new PortafolioPadreAdapter(context, portafolioPadres);
            list_productos.setAdapter(pa);
        } catch (Exception ex) {
            showDataFetchError("Lo sentimos", "");
            showErrorWithRefresh();
        }
    }

    @Override
    public void showSectionPortfolioProducts() {
        pullToRefresh.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideSectionPortfolioProducts() {
        pullToRefresh.setVisibility(View.GONE);
    }

    @Override
    public void showCircularProgressBar(String textProgressBar) {
        layoutCircularProgressBar.setVisibility(View.VISIBLE);
        textCircularProgressBar.setText(textProgressBar);
    }

    @Override
    public void hideCircularProgressBar() {
        layoutCircularProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorWithRefresh(){
        pullToRefresh.setVisibility(View.GONE);
        layoutCircularProgressBar.setVisibility(View.VISIBLE);
        circularProgressBar.setVisibility(View.GONE);
        textCircularProgressBar.setText("Ha ocurrido un error, inténtalo de nuevo ");
        buttonReferesh.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorTimeOut() {
        String message = "Ha ocurrido un error. Intenta de nuevo y si el error persiste, contacta a PRESENTE.";
        if(state != null && state.getMensajesRespuesta() != null && state.getMensajesRespuesta().size()>0){
            for(ResponseMensajesRespuesta rm : state.getMensajesRespuesta()){
                if(rm.getIdMensaje() == 6){
                    message = rm.getMensaje();
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
        titleMessage.setText("Lo sentimos");
        TextView contentMessage = (TextView) dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
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
            }
        });
        dialog.show();
    }

    @Override
    public void showExpiredToken(String message) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_closedsession);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button buttonClosedSession = (Button) dialog.findViewById(R.id.btnVolverAIngresar);
        buttonClosedSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                context.salir();
            }
        });
        dialog.show();

    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if(categorias != null){
//            GlobalState state = context.getState();
//            PortafolioPadre pp = categorias.get(position);
//            state.setCategoriaPortafolioSeleccionada(pp);
//            if(pp.portafolios.size() == 1){
//                state.setPortafolioSeleccionado(pp.portafolios.get(0));
//                Intent intent = new Intent(this, ActivityDetallesDetallePortafolio.class);
//                startActivity(intent);
//            }else{
//                Intent intent = new Intent(this, ActivityDetallePortafolio.class);
//                startActivity(intent);
//            }
//
//        }
//    }
//

//    private void cargarProductos(ArrayList<Portafolio> productos) {
//        try {
//
//            ArrayList<PortafolioPadre> portafolioPadres = new ArrayList<>();
//            ArrayList<String> categorias = new ArrayList<>();
//            for(Portafolio p: productos){
//                if(!categorias.contains(p.getCategoriaPadre())){
//                    categorias.add(p.getCategoriaPadre());
//                }
//            }
//
//            for (String c: categorias){
//                PortafolioPadre portafolioPadre = new PortafolioPadre();
//                portafolioPadre.categoriaPadre = c;
//
//                ArrayList<Portafolio> portafolios = new ArrayList<>();
//                for(Portafolio p: productos){
//                    if(p.getCategoriaPadre().equals(c)){
//                        portafolios.add(p);
//                    }
//                }
//
//                portafolioPadre.portafolios = portafolios;
//                portafolioPadres.add(portafolioPadre);
//            }
//
//            GlobalState state = context.getState();
//            state.setCategoriasPortafolio(portafolioPadres);
//            state.setPortafolio(productos);
//            this.categorias = portafolioPadres;
//            PortafolioPadreAdapter pa = new PortafolioPadreAdapter(context, portafolioPadres);
//            list_productos.setAdapter(pa);
//
//        } catch (Exception ex) {
//            context.makeErrorDialog("Error cargando los productos");
//        }
//    }
//
//    private class PortafolioTask extends AsyncTask<String, String, String> {
//
//        String cedula = null;
//        String token = null;
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Consultando portafolio...");
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                return networkHelper.readService(SincroHelper.PORTAFOLIO);
//            } catch (Exception e) {
//                return e.getMessage();
//            }
//        }
//
//        @Override
//        protected void onProgressUpdate(String... values) {
//            pd.setMessage(values[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            pd.dismiss();
//            procesarJsonPortafolio(result);
//        }
//    }
//
//    private void procesarJsonPortafolio(String jsonRespuesta) {
//        try {
//            productos = SincroHelper.procesarJsonPortafolio(jsonRespuesta);
//            cargarProductos(productos);
//        } catch (ErrorTokenException e) {
//            AlertDialog.Builder d = new AlertDialog.Builder(context);
//            d.setTitle("Sesión finalizada");
//            d.setIcon(R.mipmap.icon_presente);
//            d.setMessage(e.getMessage());
//            d.setCancelable(false);
//            d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    context.salir();
//                }
//            });
//            d.show();
//        } catch (Exception e) {
//            context.makeErrorDialog(e.getMessage());
//        }
//    }

}
