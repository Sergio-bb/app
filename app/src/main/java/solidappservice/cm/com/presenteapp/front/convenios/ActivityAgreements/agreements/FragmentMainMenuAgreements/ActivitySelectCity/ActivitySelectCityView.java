package solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.agreements.FragmentMainMenuAgreements.ActivitySelectCity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.convenios.MenuSelectCityAdapter;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Ciudad;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Resumen;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;


/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 31/07/2018.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public class ActivitySelectCityView extends ActivityBase implements ActivitySelectCityContract.View{

    private List<Ciudad> ciudades;
    private String ubicacionAsociado;

    @BindView(R.id.list_cities)
    ListView list_cities;
    @BindView(R.id.button_close)
    ImageButton buttonClose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_agreements_selectcity);
        ButterKnife.bind(this);
        setControls();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void setControls() {
        if (android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        ciudades = new ArrayList<>();
        Resumen resumen = getState().getResumen();
        ubicacionAsociado = resumen.getUbicacionAsociado();
        ciudades = resumen.getCiudades();
        for (Ciudad c: ciudades) {
            c.setCurrentLocation(c.getNombre().equals(ubicacionAsociado));
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        showCities();
    }

    @OnItemClick(R.id.list_cities)
    public void onItemClickCity(AdapterView<?> adapterView, View view, int i, long l) {
        Ciudad selectedCity = ciudades.get(i);
        Intent data = new Intent();
        data.putExtra("city", selectedCity.getNombre());
        setResult(RESULT_OK, data);
        finish();
    }

    @OnClick(R.id.button_close)
    public void onClickButtonClose(){
        finish();
    }

    @Override
    public void showCities(){
        if(ciudades != null){
            MenuSelectCityAdapter adapter = new MenuSelectCityAdapter(this, ciudades);
            list_cities.setAdapter(adapter);
        }
    }

    @Override
    public void showDataFetchError(String message) {
        AlertDialog.Builder d = new AlertDialog.Builder(this);
        d.setTitle(this.getResources().getString(R.string.app_name));
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(message);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
            }
        });
        d.show();
    }
}
