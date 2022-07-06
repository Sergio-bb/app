package solidappservice.cm.com.presenteapp.adapters.convenios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.dto.ProductoDebitable;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;

public class BolisllosMESpinnerAdapter extends ArrayAdapter<ProductoDebitable> {

    private List<ProductoDebitable> productos;
    private ActivityBase context;
    private boolean showSaldo = false;

    public BolisllosMESpinnerAdapter(ActivityBase context, List<ProductoDebitable> productos, boolean showSaldo) {
        super(context, R.layout.list_item_cuentas_me_spinner, productos);
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
            view = inflater.inflate(R.layout.list_item_cuentas_me_spinner, parent, false);
        }
        ProductoDebitable producto = productos.get(position);
        TextView lblNombreProducto = (TextView) view.findViewById(R.id.lblNombreProducto);
        TextView lblNombreAhorro = (TextView) view.findViewById(R.id.lblNombreAhorro);
        TextView lblSaldo = (TextView) view.findViewById(R.id.lblSaldo);
        TextView lblNumeroProducto = (TextView) view.findViewById(R.id.lblNumeroProducto);
        View separatorItem = (View) view.findViewById(R.id.separator_item3);
        ImageView icon = (ImageView) view.findViewById(R.id.imgArrow);

        lblNombreProducto.setText(producto.getNombreProducto());
        lblNombreProducto.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        lblNombreProducto.setTextSize(15);
        lblNumeroProducto.setVisibility(View.GONE);
        lblNombreAhorro.setVisibility(View.GONE);
        separatorItem.setVisibility(View.GONE);
        icon.setVisibility(View.VISIBLE);
        if(showSaldo && !producto.getNombreProducto().contains("Selecciona")){
            lblSaldo.setVisibility(View.VISIBLE);
            lblSaldo.setText(context.getMonedaWithDotSeparator(producto.getSaldo()));
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
            view = inflater.inflate(R.layout.list_item_cuentas_me_spinner, parent, false);
        }

        ProductoDebitable producto = productos.get(position);
        TextView lblNombreProducto = (TextView) view.findViewById(R.id.lblNombreProducto);
        TextView lblNombreAhorro = (TextView) view.findViewById(R.id.lblNombreAhorro);
        TextView lblNumeroProducto = (TextView) view.findViewById(R.id.lblNumeroProducto);
        TextView lblSaldo = (TextView) view.findViewById(R.id.lblSaldo);
        ImageView icon = (ImageView) view.findViewById(R.id.imgArrow);
        View separatorItem = (View) view.findViewById(R.id.separator_item3);

        lblNombreProducto.setText(producto.getNombreProducto());
        lblNombreAhorro.setText(producto.getNombreCuenta() != null ? producto.getNombreCuenta() : "");
        lblNumeroProducto.setVisibility(View.VISIBLE);
        lblNombreAhorro.setVisibility(View.VISIBLE);
        separatorItem.setVisibility(View.VISIBLE);
        icon.setVisibility(View.GONE);

        if(producto.getNumeroProducto() != null){
            lblNumeroProducto.setText(producto.getNumeroProducto());
            lblNumeroProducto.setVisibility(View.VISIBLE);
        }else{
            lblNumeroProducto.setVisibility(View.VISIBLE);
            lblNumeroProducto.setText("");
        }

        if(showSaldo && !producto.getNombreProducto().contains("Selecciona")){
            lblSaldo.setVisibility(View.VISIBLE);
            lblSaldo.setText(context.getMonedaWithDotSeparatorWithOutDecimals(producto.getSaldo()));
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
