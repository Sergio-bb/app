package solidappservice.cm.com.presenteapp.adapters.adelantonomina;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseTips;

public class PagerAdapterAdelantoNomina extends PagerAdapter {

    Context context;
    List<ResponseTips> tipsANArray;
    LayoutInflater inflater;


    public PagerAdapterAdelantoNomina(Context context, List<ResponseTips> tipsAN){
        this.context = context;
        this.tipsANArray = tipsAN;
        inflater = ((Activity) context).getLayoutInflater();
    }

    @Override
    public int getCount() {
        return tipsANArray.size();
    }

     @Override
     public Object instantiateItem(ViewGroup container, int position){

        View view = inflater.inflate(R.layout.list_item_pager_tips, container, false);
        TextView tv = (TextView) view.findViewById(R.id.tv_tips);
        view.setTag(position);
        ((ViewPager) container).addView(view);
        ResponseTips tips = tipsANArray.get(position);
        tv.setText(tips.getN_TIP());
        return view;
     }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View)object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        ((ViewPager)container).removeView((View)object);
    }

}
