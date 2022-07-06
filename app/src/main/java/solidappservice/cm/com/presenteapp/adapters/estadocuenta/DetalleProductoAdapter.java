package solidappservice.cm.com.presenteapp.adapters.estadocuenta;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 26/11/2015.
 */
public class DetalleProductoAdapter extends ArrayAdapter<ResponseProducto>{

    private List<ResponseProducto> productos;
    private ActivityBase context;

    public DetalleProductoAdapter(ActivityBase context, List<ResponseProducto> productos){
        super(context, R.layout.list_item_detalle_producto, productos);
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
            view = inflater.inflate(R.layout.list_item_detalle_producto, parent, false);
        }

        ResponseProducto producto = productos.get(position);

        TextView lblNombreTipoDR = (TextView)view.findViewById(R.id.lblNombreProducto);
        TextView lblNumeroProducto = (TextView)view.findViewById(R.id.lblNumeroProducto);

        TextView lblSaldoValue = (TextView)view.findViewById(R.id.lblSaldoValue);
        TextView lblCuotaValue = (TextView)view.findViewById(R.id.lblCuotaValue);
        TextView lblCuota = (TextView)view.findViewById(R.id.lblCuota);

        TextView lblFecha = (TextView)view.findViewById(R.id.lblFecha);
        TextView lblFechaValue = (TextView)view.findViewById(R.id.lblFechaValue);

        if(producto.getV_cuota() > 0){
            lblCuotaValue.setVisibility(View.VISIBLE);
            lblCuota.setVisibility(View.VISIBLE);
            lblCuotaValue.setText(context.getMoneda(producto.getV_cuota()));
        }else{
            lblCuotaValue.setVisibility(View.GONE);
            lblCuota.setVisibility(View.GONE);
        }

        if(producto.getF_vencim() != null && !TextUtils.isEmpty(producto.getF_vencim())){
            lblFecha.setVisibility(View.VISIBLE);
            lblFechaValue.setVisibility(View.VISIBLE);
            lblFechaValue.setText(producto.getF_vencim());
        }else{
            lblFecha.setVisibility(View.GONE);
            lblFechaValue.setVisibility(View.GONE);
        }

        lblNombreTipoDR.setText(producto.getN_produc());
        lblNumeroProducto.setText(producto.getA_numdoc() == null ? "" : producto.getA_numdoc());
        lblSaldoValue.setText(context.getMoneda(producto.getV_saldo()));



        return view;
    }
}
