package solidappservice.cm.com.presenteapp.adapters.convenios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Categoria;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.ViewHolder>
implements View.OnClickListener{

    private List<Categoria> mCategorias;
    private View.OnClickListener clickListener;

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView cv;
        ImageView img_categoria;
        TextView tv_nombre_categoria;
        ViewHolder(View view) {
            super(view);
            cv = view.findViewById(R.id.card_view);
            img_categoria = view.findViewById(R.id.img_categoria);
            tv_nombre_categoria = view.findViewById(R.id.tv_nombre_categoria);
        }
    }

    // Constructor
    public CategoriasAdapter(List<Categoria> categorias) {
        mCategorias = categorias;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public @NonNull
    CategoriasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_convenio_categoria, parent, false);
        cv.setOnClickListener(this);
        return new CategoriasAdapter.ViewHolder(cv);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull final CategoriasAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Categoria categoria = mCategorias.get(position);

        holder.tv_nombre_categoria.setText(categoria.getNombre());
        if(categoria.getImagen() != null){
            Picasso.get()
                    .load(categoria.getImagen())
                    .into(holder.img_categoria);
        }

        //holder.img_categoria.setImageBitmap(categoria.getImg());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        //Log.d("Presente", String.valueOf(mCategorias.size()));
        return mCategorias.size();
    }

    @Override
    public void onClick(View v) {
        clickListener.onClick(v);
    }
}

