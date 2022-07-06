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

import java.util.ArrayList;
import java.util.List;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Convenio;

public class ConveniosAdapter extends RecyclerView.Adapter<ConveniosAdapter.ViewHolder>
    implements View.OnClickListener{

    private List<Convenio>  mConvenios;
    private View.OnClickListener onClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView img_convenio;
        TextView tv_info_onvenio;
        TextView tv_info_onvenio_resumen;

        ViewHolder(View view) {
            super(view);
            cv = view.findViewById(R.id.card_view);
            img_convenio = view.findViewById(R.id.img_convenio);
            tv_info_onvenio = view.findViewById(R.id.tv_info_convenio);
            tv_info_onvenio_resumen = view.findViewById(R.id.tv_info_convenio_resumen);
        }
    }

    public ConveniosAdapter(List<Convenio> convenios) {
        mConvenios = convenios;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public @NonNull
    ConveniosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                   int viewType) {
        // create a new view
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_convenio, parent, false);
        cv.setOnClickListener(this);
        return new ViewHolder(cv);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Convenio convenio = mConvenios.get(position);
        holder.tv_info_onvenio.setText(convenio.getDescripcionCorta());
        holder.tv_info_onvenio_resumen.setText(convenio.getBeneficio());
        if(convenio.getImagen() != null){
            Picasso.get().load(convenio.getImagen())
                    .fit().into(holder.img_convenio);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mConvenios.size();
    }

    @Override
    public void onClick(View v) {
        onClickListener.onClick(v);
    }
}
