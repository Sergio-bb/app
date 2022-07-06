package solidappservice.cm.com.presenteapp.adapters.tutorial;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 19/01/2016.
 */
public class TutorialAdapter extends PagerAdapter {

    ActivityBase context;
    List<Integer> resourceIds;

    public TutorialAdapter(ActivityBase context, List<Integer> resourceIds) {
        this.context = context;
        this.resourceIds = resourceIds;
    }

    @Override
    public int getCount() {
        return resourceIds.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.vp_beneficios, container, false);
        Integer resourceID = resourceIds.get(position);

        ImageView mImageView = (ImageView) view.findViewById(R.id.image_display);
        if(mImageView != null){
            //((BitmapDrawable)mImageView.getDrawable()).getBitmap().recycle();
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), resourceID);
            mImageView.setImageBitmap(bm);
        }
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