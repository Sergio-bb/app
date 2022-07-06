package solidappservice.cm.com.presenteapp.adapters.convenios;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Producto;
import solidappservice.cm.com.presenteapp.tools.OnRecyclerViewItemClickListener;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ViewHolder> {

    private List<Producto> mProductos;
    private OnRecyclerViewItemClickListener onClickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        LinearLayout layout_producto;
        ImageView img_producto_dcto;
        TextView tv_info_producto;
        ImageView imgb_abrir_opciones_producto;

        LinearLayout layout_opciones_producto;
        ImageView imgb_comprar;
        ImageView imgb_condiciones;
        ImageView imgb_ver_mas;
        ImageView imgb_ocultar_opciones;

        LinearLayout box_comprar;
        LinearLayout box_condiciones;
        LinearLayout box_vermas;

        ViewHolder(View view) {
            super(view);

            layout_producto = view.findViewById(R.id.layout_producto);
            img_producto_dcto = view.findViewById(R.id.img_producto_dcto);
            tv_info_producto = view.findViewById(R.id.tv_info_producto);
            imgb_abrir_opciones_producto = view.findViewById(R.id.imgb_abrir_opciones_producto);

            layout_opciones_producto = view.findViewById(R.id.layout_opciones_producto);
            imgb_comprar = view.findViewById(R.id.imgb_comprar);
            imgb_condiciones = view.findViewById(R.id.imgb_condiciones);
            imgb_ver_mas = view.findViewById(R.id.imgb_ver_mas);
            imgb_ocultar_opciones = view.findViewById(R.id.imgb_ocultar_opciones);

            box_comprar = view.findViewById(R.id.box_comprar);
            box_condiciones = view.findViewById(R.id.box_condiciones);
            box_vermas= view.findViewById(R.id.box_vermas);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ProductosAdapter(List<Producto> productos, OnRecyclerViewItemClickListener listener) {
        mProductos = productos;
        onClickListener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public @NonNull
    ProductosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                   int viewType) {
        // create a new view
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_convenio_producto, parent, false);
        return new ProductosAdapter.ViewHolder(cv);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final @NonNull ProductosAdapter.ViewHolder holder, final int position) {

        final Producto producto = mProductos.get(position);
        if(producto.getImagen() != null){
            Picasso.get().load(producto.getImagen()).fit().into(holder.img_producto_dcto);
        }

        holder.tv_info_producto.setText(producto.getResumen());

        holder.layout_opciones_producto.setVisibility(producto.isOpcionesVisible() ? View.VISIBLE : View.GONE);
        holder.imgb_abrir_opciones_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!producto.isOpcionesVisible()) {
                    holder.layout_opciones_producto.setVisibility(View.VISIBLE);
                    resetOpcionesVisible();
                    producto.setOpcionesVisible(true);
                    notifyDataSetChanged();
                }
            }
        });

        holder.layout_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!producto.isOpcionesVisible()) {
                    holder.layout_opciones_producto.setVisibility(View.VISIBLE);
                    resetOpcionesVisible();
                    producto.setOpcionesVisible(true);
                    notifyDataSetChanged();
                }
            }
        });

        holder.imgb_ocultar_opciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (producto.isOpcionesVisible()) {
                    holder.layout_opciones_producto.setVisibility(View.GONE);
                    producto.setOpcionesVisible(false);
                }
            }
        });

        holder.layout_opciones_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (producto.isOpcionesVisible()) {
                    holder.layout_opciones_producto.setVisibility(View.GONE);
                    producto.setOpcionesVisible(false);
                }
            }
        });

        if (producto.getFormasPago() != null && producto.getFormasPago().size() > 0) {
            holder.imgb_comprar.setVisibility(View.VISIBLE);
            holder.imgb_comprar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClick(v, holder.getAdapterPosition());
                }
            });
        }else{
            holder.box_comprar.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(producto.getHtmlRestriccion())) {
            holder.imgb_condiciones.setVisibility(View.VISIBLE);
            holder.imgb_condiciones.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClick(v, holder.getAdapterPosition());
                }
            });
        }else{
            holder.box_condiciones.setVisibility(View.GONE);

        }

        if (!TextUtils.isEmpty(producto.getHtmlDescripcion())) {
            holder.imgb_ver_mas.setVisibility(View.VISIBLE);
            holder.imgb_ver_mas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClick(v, holder.getAdapterPosition());
                }
            });
        } else {
            holder.box_vermas.setVisibility(View.GONE);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mProductos.size();
    }

    private void resetOpcionesVisible() {
        for (Producto p :
                mProductos) {
            p.setOpcionesVisible(false);
        }
    }
}
