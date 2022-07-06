package solidappservice.cm.com.presenteapp.adapters.pagoobligaciones;

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
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 01/12/2015.
 */
public class ProductoSpinnerAdapter extends ArrayAdapter<ResponseProducto> {

    private List<ResponseProducto> productos;
    private ActivityBase context;
    private boolean showSaldo = false;

    public ProductoSpinnerAdapter(ActivityBase context, List<ResponseProducto> productos, boolean showSaldo) {
        super(context, R.layout.list_item_producto_spinner, productos);
        this.productos = productos;
        this.context = context;
        this.showSaldo = showSaldo;
    }

    @Override
    public int getCount() {
        return (productos == null ? 0 : productos.size());
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        if (view == null) {
            view = inflater.inflate(R.layout.list_item_producto_spinner, parent, false);
        }

        ResponseProducto producto = productos.get(position);

        TextView lblNtipoDR = (TextView) view.findViewById(R.id.lblNtipoDR);
        TextView lblNombreProducto = (TextView) view.findViewById(R.id.lblNombreProducto);
        TextView lblNumeroProducto = (TextView) view.findViewById(R.id.lblNumeroProducto);
        TextView lblSaldo = (TextView) view.findViewById(R.id.lblSaldo);

        if(producto.getI_debito() != null && producto.getI_debito().equals("Y")){
            lblNtipoDR.setText(producto.getN_tipodr());
            lblNombreProducto.setText(producto.getN_produc());
        }else{
            lblNtipoDR.setText(producto.getN_produc());
            lblNombreProducto.setText("");
        }

        lblNumeroProducto.setText(producto.getA_numdoc());
        if(showSaldo && !producto.getN_produc().contains("Selecciona")){
            lblSaldo.setVisibility(View.VISIBLE);
            lblSaldo.setText(context.getMoneda(producto.getV_saldo()));
        }else{
            lblSaldo.setVisibility(View.GONE);
            lblSaldo.setText("");
        }

        return view;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        if (view == null) {
            view = inflater.inflate(R.layout.list_item_producto_spinner, parent, false);
        }

        ResponseProducto producto = productos.get(position);

        TextView lblNtipoDR = (TextView) view.findViewById(R.id.lblNtipoDR);
        TextView lblNombreProducto = (TextView) view.findViewById(R.id.lblNombreProducto);
        TextView lblNumeroProducto = (TextView) view.findViewById(R.id.lblNumeroProducto);
        TextView lblSaldo = (TextView) view.findViewById(R.id.lblSaldo);

        if(producto.getI_debito() != null && producto.getI_debito().equals("Y")){
            lblNtipoDR.setText(producto.getN_tipodr());
            lblNombreProducto.setText(producto.getN_produc());
        }else{
            lblNtipoDR.setText(producto.getN_produc());
            lblNombreProducto.setText("");
        }

        lblNumeroProducto.setText(producto.getA_numdoc());
        if(showSaldo && !producto.getN_produc().contains("Selecciona")){
            lblSaldo.setVisibility(View.VISIBLE);
            lblSaldo.setText(context.getMoneda(producto.getV_saldo()));
        }else{
            lblSaldo.setVisibility(View.GONE);
            lblSaldo.setText("");
        }

        return view;
    }
}