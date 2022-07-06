package solidappservice.cm.com.presenteapp.adapters.login;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.mensajesbanner.ResponseMensajesBanner;
import solidappservice.cm.com.presenteapp.front.base.ActivityMainView;


public class ImageSlideAdapter extends PagerAdapter {
	
	ActivityMainView context;
	List<ResponseMensajesBanner> mensajes;

	public ImageSlideAdapter(ActivityMainView context, List<ResponseMensajesBanner> mensajes) {
		this.context = context;
		this.mensajes = mensajes;
	}

	@Override
	public int getCount() {
		return mensajes.size();
	}

	@Override
	public View instantiateItem(ViewGroup container, final int position) {
		LayoutInflater inflater = context.getLayoutInflater();
		View view = inflater.inflate(R.layout.vp_image, container, false);
		ResponseMensajesBanner mensaje = mensajes.get(position);
		
		TextView mImageView = view.findViewById(R.id.image_display);
		mImageView.setText(mensaje.getN_mensaje());
		mImageView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
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