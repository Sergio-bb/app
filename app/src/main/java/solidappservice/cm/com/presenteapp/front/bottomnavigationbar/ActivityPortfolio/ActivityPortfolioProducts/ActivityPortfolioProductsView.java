package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityPortfolio.ActivityPortfolioProducts;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;

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
    private ProgressDialog pd;
    private List<ResponsePortafolio> productos = null;
    private List<PortafolioPadre> categorias = null;

    @BindView(R.id.list_portafolio)
    ListView list_productos;
    @BindView(R.id.btn_back)
    ImageButton btnBack;

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
        pd = new ProgressDialog(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null){
            context.salir();
        }else {
            if (context.getState().getPortafolio() == null ||
                    context.getState().getPortafolio().size() == 0) {
                presenter.fetchPortfolioProducts();
//                new PortafolioTask().execute();
            }else{
                showPortfolioProducts(context.getState().getPortafolio(), context.getState().getCategoriasPortafolio());
//                cargarProductos(context.getState().getPortafolio());
            }
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
    public void showPortfolioProducts(List<ResponsePortafolio> productos, List<PortafolioPadre> portafolioPadres) {
        try {
            state.setCategoriasPortafolio(portafolioPadres);
            state.setPortafolio(productos);
            this.categorias = portafolioPadres;
            PortafolioPadreAdapter pa = new PortafolioPadreAdapter(context, portafolioPadres);
            list_productos.setAdapter(pa);
        } catch (Exception ex) {
            context.makeErrorDialog("Error cargando los productos");
        }
    }

    @Override
    public void showProgressDialog(String message) {
        pd.setTitle(context.getResources().getString(R.string.app_name));
        pd.setMessage(message);
        pd.setIcon(R.mipmap.icon_presente);
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    public void hideProgressDialog() {
        pd.dismiss();
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
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle(context.getResources().getString(R.string.app_name));
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(message);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        d.show();
    }

    @Override
    public void showDataFetchError(String message) {
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
        d.setTitle(context.getResources().getString(R.string.app_name));
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(message);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        d.show();
    }

    @Override
    public void showExpiredToken(String message) {
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle("Sesión finalizada");
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(message);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                context.salir();
            }
        });
        d.show();
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
