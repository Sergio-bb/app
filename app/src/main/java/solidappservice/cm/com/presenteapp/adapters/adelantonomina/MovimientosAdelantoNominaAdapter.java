package solidappservice.cm.com.presenteapp.adapters.adelantonomina;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseMovimientos;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 10/07/2020.
 */
public class MovimientosAdelantoNominaAdapter extends BaseAdapter {

    private Context context;
    private List<ResponseMovimientos> movimientosAN;
    private LayoutInflater inflater;

    public MovimientosAdelantoNominaAdapter(Context context, List<ResponseMovimientos> movimientosAN){
        this.context = context;
        this.movimientosAN = movimientosAN;
        inflater = ((Activity) context).getLayoutInflater();
    }

    @Override
    public int getCount() {
        return movimientosAN.size();
    }

    @Override
    public Object getItem(int position) {
        return movimientosAN.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View view, ViewGroup parent){
        ResponseMovimientos item = (ResponseMovimientos)getItem(position);
        DecimalFormat formato = new DecimalFormat("#,###");
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        view = inflater.inflate(R.layout.list_item_movimientosan, parent, false);
        TextView tv = (TextView) view.findViewById(R.id.lblValorAdelantado);
        TextView tv2 = (TextView) view.findViewById(R.id.lblFechaSolicitud);

        tv.setText("$"+formato.format(item.getV_solicitado()));
        try{
            tv2.setText(format.format(item.getF_solictud()));
        }catch(Exception e){
            tv2.setText("");
        }

        return view;
    }
}
