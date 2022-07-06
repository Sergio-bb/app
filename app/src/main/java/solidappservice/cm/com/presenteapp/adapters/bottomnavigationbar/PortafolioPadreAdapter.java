package solidappservice.cm.com.presenteapp.adapters.bottomnavigationbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.dto.PortafolioPadre;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;

public class PortafolioPadreAdapter extends ArrayAdapter<PortafolioPadre> {

    private ActivityBase context;
    private List<PortafolioPadre> categorias;

    public PortafolioPadreAdapter(ActivityBase context, List<PortafolioPadre> categorias){
        super(context, R.layout.list_item_portafolio, categorias);
        this.context = context;
        this.categorias = categorias;
    }

    @Override
    public PortafolioPadre getItem(int position) {
        return categorias.get(position);
    }

    @Override
    public int getCount() {
        return (categorias == null ? 0 : categorias.size());
    }

    @Override
    public @NonNull
    View getView(int position, View view, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        if(view == null){
            view = inflater.inflate(R.layout.list_item_portafolio, parent, false);
        }

        TextView lblNombreProducto = view.findViewById(R.id.lblNombreProducto);

        PortafolioPadre producto = categorias.get(position);
        lblNombreProducto.setText(producto.getCategoriaPadre());

        return view;
    }

}