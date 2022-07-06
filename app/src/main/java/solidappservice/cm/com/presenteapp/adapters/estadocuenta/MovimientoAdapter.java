package solidappservice.cm.com.presenteapp.adapters.estadocuenta;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseMovimientoProducto;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;

public class MovimientoAdapter extends ArrayAdapter<ResponseMovimientoProducto> {

    private List<ResponseMovimientoProducto> movimientos;
    private ActivityBase context;

    public MovimientoAdapter(ActivityBase context, List<ResponseMovimientoProducto> movimientos){
        super(context, R.layout.list_item_movimiento, movimientos);
        this.movimientos = movimientos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return (movimientos == null ? 0 : movimientos.size());
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        if(view == null){
            view = inflater.inflate(R.layout.list_item_movimiento, parent, false);
        }

        ResponseMovimientoProducto movimiento = movimientos.get(position);

        TextView lblFecha = (TextView)view.findViewById(R.id.lblFecha);
        TextView lblTipodoc = (TextView)view.findViewById(R.id.lblTipodoc);
        TextView lblValor = (TextView)view.findViewById(R.id.lblValor);
        lblFecha.setText(movimiento.getF_movimi());
        lblTipodoc.setText(movimiento.getN_tipdoc());
        lblValor.setText(context.getMoneda(movimiento.getV_valor()));
        if(movimiento.getV_valor() < 0){
            lblValor.setTextColor(Color.RED);
        }else{
            lblValor.setTextColor(Color.parseColor("#777777"));
        }

        return view;
    }
}
