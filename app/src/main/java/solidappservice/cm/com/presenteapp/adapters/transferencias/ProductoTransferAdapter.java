package solidappservice.cm.com.presenteapp.adapters.transferencias;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.dto.ProductosPorTipoDR;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 6/10/17.
 */
public class ProductoTransferAdapter extends ArrayAdapter<ProductosPorTipoDR> {

    private ActivityBase context;
    ArrayList<ProductosPorTipoDR> productos;

    public ProductoTransferAdapter(ActivityBase context, ArrayList<ProductosPorTipoDR> productos){
        super(context, R.layout.list_item_producto, productos);
        this.context = context;
        this.productos = productos;
    }

    @Override
    public ProductosPorTipoDR getItem(int position) {
        return productos.get(position);
    }

    @Override
    public int getCount() {
        return (productos == null ? 0 : productos.size());
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        if(view == null){
            view = inflater.inflate(R.layout.list_item_producto, parent, false);
        }

        TextView lblNombreProducto = (TextView) view.findViewById(R.id.lblNombreProducto);
        TextView lblCantidadProducto = (TextView) view.findViewById(R.id.lblCantidadProducto);

        ProductosPorTipoDR producto = productos.get(position);
        lblNombreProducto.setText(producto.getN_tipodr());
        lblCantidadProducto.setText("Productos("+producto.getProductos().size()+")");

        return view;
    }
}
