package solidappservice.cm.com.presenteapp.adapters.tarjetapresente;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseTarjeta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;

public class TarjetasAdapter extends ArrayAdapter<ResponseTarjeta> {

    private List<ResponseTarjeta> productos;
    private ActivityBase context;

    public TarjetasAdapter(ActivityBase context, List<ResponseTarjeta> productos){
        super(context, R.layout.list_item_tarjeta, productos);
        this.productos = productos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return (productos == null ? 0 : productos.size());
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        if(view == null){
            view = inflater.inflate(R.layout.list_item_tarjeta, parent, false);
        }

        ResponseTarjeta producto = productos.get(position);

        TextView lblNombreTipoDR = (TextView)view.findViewById(R.id.lblNombreProducto);
        //TextView lblNumeroProducto = (TextView)view.findViewById(R.id.lblNumeroProducto);
        //TextView lblNumValue = (TextView)view.findViewById(R.id.lblNumValue);
        TextView lblCuota = (TextView)view.findViewById(R.id.lblCuota);
        TextView lblCuotaValue = (TextView)view.findViewById(R.id.lblCuotaValue);

        lblNombreTipoDR.setText(producto.getN_percol());
        if(!producto.getA_numcta().equalsIgnoreCase("X")) {
            lblCuota.setText("");
            lblCuotaValue.setText("");
        }else{
            lblCuota.setText("Cupo:");
            lblCuotaValue.setText(context.getMoneda(producto.getV_cupo()));
        }


        return view;
    }
}
