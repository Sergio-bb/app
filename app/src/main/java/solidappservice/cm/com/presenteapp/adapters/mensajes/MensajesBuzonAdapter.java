package solidappservice.cm.com.presenteapp.adapters.mensajes;

import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.mensajes.response.ResponseObtenerMensajes;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 30/11/2015.
 */
public class MensajesBuzonAdapter extends ArrayAdapter<ResponseObtenerMensajes>{

    private ActivityBase context;
    private List<ResponseObtenerMensajes> mensajesBuzon;

    public MensajesBuzonAdapter(ActivityBase context, List<ResponseObtenerMensajes> mensajesBuzon){
        super(context, R.layout.list_item_mensaje, mensajesBuzon);
        this.context = context;
        this.mensajesBuzon = mensajesBuzon;
    }

    @Override
    public ResponseObtenerMensajes getItem(int position) {
        return mensajesBuzon.get(position);
    }

    @Override
    public int getCount() {
        return (mensajesBuzon == null ? 0 : mensajesBuzon.size());
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NonNull
    View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        if(view == null){
            view = layoutInflater.inflate(R.layout.list_item_mensaje, parent, false);
        }

        ImageView image_mensaje_leido = view.findViewById(R.id.image_mensaje_leido);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        TextView txtMensaje = view.findViewById(R.id.txtMensaje);

        ResponseObtenerMensajes mensaje = getItem(position);
        if(mensaje != null){
            txtTitle.setText(mensaje.getTitulo());
            String _mensaje;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                _mensaje = Html.fromHtml(mensaje.getContenido(), Html.FROM_HTML_MODE_LEGACY).toString();
            } else {
                _mensaje = Html.fromHtml(mensaje.getContenido()).toString();
            }

            //MAX LENGHT:85 caracteres
            if(mensaje.getContenido().length() > 85){
                String subMensaje = _mensaje;
                if(_mensaje.length() > 86){
                    subMensaje = _mensaje.substring(0, 85) + "...";
                }
                txtMensaje.setText(subMensaje);
            }else{
                txtMensaje.setText(_mensaje);
            }

            if(mensaje.getLeido().equals("Y")){
                image_mensaje_leido.setImageResource(R.drawable.icon_open_message);
                view.setBackgroundColor(Color.parseColor("#f2f2f2"));
            }else{
                image_mensaje_leido.setImageResource(R.drawable.icon_new_message);
                view.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        }

        return view;
    }
}
