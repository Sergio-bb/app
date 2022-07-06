package solidappservice.cm.com.presenteapp.adapters.bottomnavigationbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import java.util.List;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponseServicios;
import solidappservice.cm.com.presenteapp.entities.login.Response.Usuario;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 10/07/2020.
 */
public class ServiciosAdapter extends BaseAdapter {

    private Usuario usuario;
    private Context context;
    private List<ResponseServicios> servicios;
    private LayoutInflater inflater;
    private ImageView image_servicio;

    private FirebaseAnalytics firebaseAnalytics;

    public ServiciosAdapter(Context context, List<ResponseServicios> servicios, Usuario usuario){
        this.context = context;
        this.servicios = servicios;
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        this.usuario = usuario;
        inflater = ((Activity) context).getLayoutInflater();
    }

    @Override
    public ResponseServicios getItem(int position) {
        return servicios.get(position);
    }

    @Override
    public int getCount() {
        return servicios.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View view, ViewGroup parent){

        final ResponseServicios item = servicios.get(position);
        view = inflater.inflate(R.layout.list_item_servicios, parent, false);
        image_servicio = (ImageView) view.findViewById(R.id.image_servicio);

        image_servicio.setBackgroundColor(Color.TRANSPARENT);
        Picasso.get().load(item.getN_url_icono()).into(image_servicio);
        image_servicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle params = new Bundle();
                params.putString("Descripción", "Interacción con el servicio "+item.getN_servicio());
                firebaseAnalytics.logEvent("pantalla_menu_servicio_"+item.getK_servicio(), params);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(item.getN_url_enlace()));
                context.startActivity(i);
            }
        });
        return view;
    }


}
