package solidappservice.cm.com.presenteapp.adapters.bottomnavigationbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponseDirectorio;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;

/**
 * CREADO POR JORGE ANDRES DAVID CARDONA EL 22/08/17.
 */
public class DirectorioAdapter extends ArrayAdapter<ResponseDirectorio> {

    private ActivityBase context;
    private List<ResponseDirectorio> directorios;

    public DirectorioAdapter(ActivityBase context, List<ResponseDirectorio> directorios){
        super(context, R.layout.list_item_directorio, directorios);
        this.context = context;
        this.directorios = directorios;
    }

    @Override
    public ResponseDirectorio getItem(int position) {
        return directorios.get(position);
    }

    @Override
    public int getCount() {
        return (directorios == null ? 0 : directorios.size());
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        if(view == null){
            view = layoutInflater.inflate(R.layout.list_item_directorio, parent, false);
        }

        ImageView image_mensaje_leido = (ImageView) view.findViewById(R.id.image_mensaje_leido);
        TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        TextView txtMensaje = (TextView) view.findViewById(R.id.txtMensaje);

        ResponseDirectorio directorio = getItem(position);

        if(directorio != null){
            txtTitle.setText(directorio.getN_nombre());
            txtMensaje.setText(directorio.getN_teleusu());
        }

        return view;
    }
}
