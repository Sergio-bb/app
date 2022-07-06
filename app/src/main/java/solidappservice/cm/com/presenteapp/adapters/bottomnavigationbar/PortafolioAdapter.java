package solidappservice.cm.com.presenteapp.adapters.bottomnavigationbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponsePortafolio;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 12/05/17.
 */
public class PortafolioAdapter extends ArrayAdapter<ResponsePortafolio> {

    private ActivityBase context;
    private ArrayList<ResponsePortafolio> productos;

    public PortafolioAdapter(ActivityBase context, ArrayList<ResponsePortafolio> productos){
        super(context, R.layout.list_item_portafolio, productos);
        this.context = context;
        this.productos = productos;
    }

    @Override
    public ResponsePortafolio getItem(int position) {
        return productos.get(position);
    }

    @Override
    public int getCount() {
        return (productos == null ? 0 : productos.size());
    }

    @Override
    public @NonNull
    View getView(int position, View view, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        if(view == null){
            view = inflater.inflate(R.layout.list_item_portafolio, parent, false);
        }

        TextView lblNombreProducto = view.findViewById(R.id.lblNombreProducto);

        ResponsePortafolio producto = productos.get(position);
        lblNombreProducto.setText(producto.getCategoria());

        return view;
    }
}