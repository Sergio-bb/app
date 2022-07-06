package solidappservice.cm.com.presenteapp.adapters.home;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.banercomercial.response.ResponseBannerComercial;
import solidappservice.cm.com.presenteapp.entities.login.Response.Usuario;
import solidappservice.cm.com.presenteapp.front.base.main.ActivityMainView;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

public class ImageHomeSlideAdapter extends RecyclerView.Adapter<ImageHomeSlideAdapter.SliderViewHolder> {

	private List<ResponseBannerComercial> commercialBanners;
	private ViewPager2 viewPagerHome;
	private ActivityMainView context;
	private Usuario user;

	public ImageHomeSlideAdapter(ActivityMainView context, List<ResponseBannerComercial> commercialBanners, ViewPager2 viewPagerHome, Usuario user) {
		this.context = context;
		this.commercialBanners = commercialBanners;
		this.viewPagerHome = viewPagerHome;
		this.user = user;
	}

	@Override
	public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.vp_image_home, parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
		holder.setImage(commercialBanners.get(position));
		if (position == commercialBanners.size()- 2){
			viewPagerHome.post(runnable);
		}
	}

	@Override
	public int getItemCount() {
		return commercialBanners.size();
	}

	class SliderViewHolder extends RecyclerView.ViewHolder {

		private RoundedImageView imageView;
		SliderViewHolder(@NonNull View itemView) {
			super(itemView);
			imageView = itemView.findViewById(R.id.imageButton);
		}
		void setImage(ResponseBannerComercial commercialBanner){
			Picasso.get().load(commercialBanner.getN_url_imagen()).into(imageView);
			imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(commercialBanner.getI_autenticportal() != null && commercialBanner.getI_autenticportal().equals("Y")){
						Encripcion encripcion = new Encripcion();
						String tokenSession = encripcion.encryptAES(user.getCedula()+":"+user.getToken());
						Uri uri = Uri.parse(commercialBanner.getN_url_enlace().replace("${tokenSession}", tokenSession));
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						context.startActivity(intent);
					}else{
						Uri uri = Uri.parse(commercialBanner.getN_url_enlace());
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						context.startActivity(intent);
					}
				}
			});
		}
	}

	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			commercialBanners.addAll(commercialBanners);
			notifyDataSetChanged();
		}
	};
}