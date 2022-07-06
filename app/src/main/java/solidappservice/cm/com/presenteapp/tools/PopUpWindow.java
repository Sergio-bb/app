package solidappservice.cm.com.presenteapp.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class PopUpWindow implements InfoWindowAdapter {

	private View popup = null;
	private LayoutInflater inflater = null;
	private ActivityBase context;

	public PopUpWindow(ActivityBase context) {
		this.inflater = context.getLayoutInflater();
		this.context = context;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		return (null);
	}

	@SuppressLint("InflateParams")
	@Override
	public View getInfoContents(Marker marker) {
		if (popup == null) {
			popup = inflater.inflate(R.layout.popup_mapa, null);
		}
		LinearLayout layout = (LinearLayout) popup.findViewById(R.id.contenedorAsesores);
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout.removeAllViews();

		TextView tv = (TextView) popup.findViewById(R.id.title);
		TextView tv2 = (TextView) popup.findViewById(R.id.address);
		tv.setText(marker.getTitle());
		tv2.setText("");

		String snippet = marker.getSnippet();
		if (snippet != null) {
			String[] snippetArray = snippet.split("!");
			
			if (snippetArray.length > 0) {
				String direccion = snippetArray[0];
				
				tv2.setText(direccion);

				for (int i = 1; i < snippetArray.length; i++) {
					String asesor = snippetArray[i];
					String detalleAsesor[] = asesor.split("<");
					TextView txtDetalle;
					TextView txtTitle;
					layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
					View view;
					if (vi != null) {
						view = vi.inflate(R.layout.lista_item, null);
						if (view != null) {
							txtDetalle = (TextView) view.findViewById(R.id.layout_lista_item);
							txtTitle = (TextView) view.findViewById(R.id.layout_lista_item_title);
							if(detalleAsesor.length>0){
                                txtTitle.setText(detalleAsesor[0]);
                            }

                            if(detalleAsesor.length>1){
                                txtDetalle.setText("tel: " + detalleAsesor[1]);
                            }

							layout.addView(view);
						}
					}
				}
			}
		}
		return (popup);
	}
}
