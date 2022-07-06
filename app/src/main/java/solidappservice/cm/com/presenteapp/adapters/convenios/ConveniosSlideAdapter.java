package solidappservice.cm.com.presenteapp.adapters.convenios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Convenio;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;

public class ConveniosSlideAdapter extends PagerAdapter implements View.OnClickListener{

    private ActivityBase context;
    private List<Convenio> convenios;
    private View.OnClickListener onClickListener;

    public ConveniosSlideAdapter(ActivityBase context, List<Convenio> convenios) {
        this.context = context;
        this.convenios = convenios;
    }

    @Override
    public int getCount() {
        return convenios.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public @NonNull
    View instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.list_item_convenio, container, false);

        Convenio convenio = convenios.get(position);
        ImageView img_convenio = view.findViewById(R.id.img_convenio);
        TextView tv_info_onvenio = view.findViewById(R.id.tv_info_convenio);
        TextView tv_info_onvenio_resumen = view.findViewById(R.id.tv_info_convenio_resumen);
        tv_info_onvenio.setText(convenio.getDescripcionCorta());
        tv_info_onvenio_resumen.setText(convenio.getBeneficio());

        if(convenio.getImagen() != null){
            img_convenio.setVisibility(View.VISIBLE);
            Picasso.get().load(convenio.getImagen()).fit().into(img_convenio);
            tv_info_onvenio_resumen.setVisibility(View.VISIBLE);
            //int heightInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 110, context.getResources().getDisplayMetrics());
            //img_convenio.getLayoutParams().height = heightInDp;
            img_convenio.requestLayout();
        }else if(convenio.getNombre().equalsIgnoreCase("GENERIC")){
            img_convenio.setVisibility(View.VISIBLE);
            Picasso.get().load(R.drawable.generic_convenios).into(img_convenio);
            tv_info_onvenio_resumen.setVisibility(View.GONE);
            //int heightInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, context.getResources().getDisplayMetrics());
            //img_convenio.getLayoutParams().height = heightInDp;
            img_convenio.requestLayout();
        }

        view.setOnClickListener(this);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void onClick(View v) {
        onClickListener.onClick(v);
    }
}
