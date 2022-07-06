package solidappservice.cm.com.presenteapp.adapters.nequi;

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
public class CuentasNequiSpinnerAdapter extends ArrayAdapter<ResponseProducto> {

    private List<ResponseProducto> productos;
    private ActivityBase context;
    private boolean showSaldo = false;

    public CuentasNequiSpinnerAdapter(ActivityBase context, List<ResponseProducto> productos, boolean showSaldo) {
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
            view = inflater.inflate(R.layout.list_item_cuentas_nequi_spinner, parent, false);
        }
        ResponseProducto producto = productos.get(position);
        TextView lblNombreProducto = (TextView) view.findViewById(R.id.lblNombreProducto);
        TextView lblNombreAhorro = (TextView) view.findViewById(R.id.lblNombreAhorro);
        TextView lblSaldo = (TextView) view.findViewById(R.id.lblSaldo);
        TextView lblNumeroProducto = (TextView) view.findViewById(R.id.lblNumeroProducto);
        View separator = (View) view.findViewById(R.id.separator_item3);

        if(producto.getI_debito() != null && producto.getI_debito().equals("Y")){
            lblNombreProducto.setText(producto.getN_produc());
        }else{
            lblNombreProducto.setText(" - ");
        }
        lblNumeroProducto.setVisibility(View.GONE);
        lblNombreAhorro.setVisibility(View.GONE);
        separator.setVisibility(View.GONE);

        if(showSaldo && !producto.getN_produc().contains("Selecciona")){
            lblSaldo.setVisibility(View.VISIBLE);
            lblSaldo.setText(context.getMonedaWithOutDecimals(producto.getV_saldo()));
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
            view = inflater.inflate(R.layout.list_item_cuentas_nequi_spinner, parent, false);
        }

        ResponseProducto producto = productos.get(position);
        TextView lblNombreProducto = (TextView) view.findViewById(R.id.lblNombreProducto);
        TextView lblNombreAhorro = (TextView) view.findViewById(R.id.lblNombreAhorro);
        TextView lblNumeroProducto = (TextView) view.findViewById(R.id.lblNumeroProducto);
        TextView lblSaldo = (TextView) view.findViewById(R.id.lblSaldo);
        View separator = (View) view.findViewById(R.id.separator_item3);

        if(producto.getI_debito() != null && producto.getI_debito().equals("Y")){
            lblNombreProducto.setText(producto.getN_produc());
        }else{
            lblNombreProducto.setText(" - ");
        }
        lblNombreAhorro.setText(producto.getN_tipodr() != null ? producto.getN_tipodr() : "");
        lblNumeroProducto.setVisibility(View.VISIBLE);
        lblNombreAhorro.setVisibility(View.VISIBLE);
        separator.setVisibility(View.VISIBLE);

        if(producto.getA_numdoc() != null){
            lblNumeroProducto.setText(producto.getA_numdoc());
            lblNumeroProducto.setVisibility(View.VISIBLE);
        }else{
            lblNumeroProducto.setVisibility(View.VISIBLE);
            lblNumeroProducto.setText("");
        }

        if(showSaldo && !producto.getN_produc().contains("Selecciona")){
            lblSaldo.setVisibility(View.VISIBLE);
            lblSaldo.setText(context.getMonedaWithOutDecimals(producto.getV_saldo()));
        }else{
            lblSaldo.setVisibility(View.GONE);
            lblSaldo.setText("");
        }

        return view;
    }

    public void setError(View v, CharSequence s) {
        TextView name = (TextView) v.findViewById(R.id.lblNombreProducto);
        name.setError(s);
    }
}