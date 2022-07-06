package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityPortfolio.ActivityPortfolioProductsExpanded;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.bottomnavigationbar.PortafolioAdapter;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.dto.PortafolioPadre;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityPortfolio.ActivityPortfolioProductsDetail.ActivityPortfolioProductsDetailView;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 27/03/18.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 28/08/2021.
 */
public class ActivityPortfolioProductsExpandedView extends ActivityBase implements ActivityPortfolioProductsExpandedContract.View {

    private ActivityPortfolioProductsExpandedPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private PortafolioPadre categoriaSeleccionada;

    @BindView(R.id.list_portafolio)
    ListView list_productos;
    @BindView(R.id.btn_back)
    ImageButton btnBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolioproducts);
        ButterKnife.bind(this);
        setControls();
    }

    @Override
    protected void setControls() {
        presenter = new ActivityPortfolioProductsExpandedPresenter(this);
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
            categoriaSeleccionada = state.getCategoriaPortafolioSeleccionada();
            showPortfolioProductsExpanded(categoriaSeleccionada);
//            cargarProductos(categoriaSeleccionada);
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
        if (categoriaSeleccionada != null && categoriaSeleccionada.getPortafolios() != null
                && categoriaSeleccionada.getPortafolios().size() > 0) {
            state.setPortafolioSeleccionado(categoriaSeleccionada.getPortafolios().get(position));
            Intent intent = new Intent(this, ActivityPortfolioProductsDetailView.class);
            startActivity(intent);
        }
    }

    @Override
    public void showPortfolioProductsExpanded(PortafolioPadre categoria) {
        try {
            PortafolioAdapter portafolioAdapter = new PortafolioAdapter(this, categoria.getPortafolios());
            list_productos.setAdapter(portafolioAdapter);
        } catch (Exception ex) {
            context.makeErrorDialog("Error cargando los productos");
        }
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (categoriaSeleccionada != null && categoriaSeleccionada.getPortafolios() != null
//                && categoriaSeleccionada.getPortafolios().size() > 0) {
//            context.getState().setPortafolioSeleccionado(categoriaSeleccionada.getPortafolios().get(position));
//            Intent intent = new Intent(this, ActivityPortfolioProductsDetailView.class);
//            startActivity(intent);
//        }
//    }

//    private void cargarProductos(PortafolioPadre categoria) {
//        try {
//            PortafolioAdapter portafolioAdapter = new PortafolioAdapter(this, categoria.getPortafolios());
//            list_productos.setAdapter(portafolioAdapter);
//        } catch (Exception ex) {
//            context.makeErrorDialog("Error cargando los productos");
//        }
//    }

}