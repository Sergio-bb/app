package solidappservice.cm.com.presenteapp.adapters.mensajes;

import android.graphics.Bitmap;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA 03/12/2015.
 */
public class BeneficiosImageSlideAdapter extends PagerAdapter {

    ActivityBase context;
    ArrayList<Bitmap> bitmaps;

    public BeneficiosImageSlideAdapter(ActivityBase context, ArrayList<Bitmap> bitmaps) {
        this.context = context;
        this.bitmaps = bitmaps;
    }

    @Override
    public int getCount() {
        return bitmaps.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.vp_beneficios, container, false);
        Bitmap bitmap = bitmaps.get(position);

        ImageView mImageView = (ImageView) view.findViewById(R.id.image_display);
        mImageView.setImageBitmap(bitmap);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}