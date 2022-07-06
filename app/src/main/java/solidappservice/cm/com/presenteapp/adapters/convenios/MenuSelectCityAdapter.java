package solidappservice.cm.com.presenteapp.adapters.convenios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Ciudad;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;

public class MenuSelectCityAdapter extends ArrayAdapter<Ciudad> {

    private ActivityBase context;
    private List<Ciudad> ciudades;

    public MenuSelectCityAdapter(ActivityBase context, List<Ciudad> ciudades){
        super(context, R.layout.list_item_selec_ciudad, ciudades);
        this.context = context;
        this.ciudades = ciudades;
    }

    @Override
    public Ciudad getItem(int position) {
        return ciudades.get(position);
    }

    @Override
    public int getCount() {
        return (ciudades == null ? 0 : ciudades.size());
    }

    @Override
    public @NonNull
    View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        if(view == null){
            view = inflater.inflate(R.layout.list_item_selec_ciudad, parent, false);
        }

        TextView tv_name_city = view.findViewById(R.id.tv_name_city);
        ImageView img_selected = view.findViewById(R.id.img_selected);

        Ciudad ciudad = ciudades.get(position);
        tv_name_city.setText(ciudad.getNombre());

        if(ciudad.isCurrentLocation()){
            //tv_name_city.setTypeface(tv_name_city.getTypeface(), Typeface.BOLD);
            img_selected.setVisibility(View.VISIBLE);
        }else{
            //tv_name_city.setTypeface(tv_name_city.getTypeface(), Typeface.NORMAL);
            img_selected.setVisibility(View.GONE);
        }

        return view;
    }
}
