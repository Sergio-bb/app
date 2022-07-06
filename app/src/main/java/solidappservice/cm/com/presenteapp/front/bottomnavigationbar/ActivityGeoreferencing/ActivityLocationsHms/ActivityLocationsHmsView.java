package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityGeoreferencing.ActivityLocationsHms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.huawei.hms.maps.CameraUpdate;
import com.huawei.hms.maps.CameraUpdateFactory;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.MapFragment;
import com.huawei.hms.maps.model.BitmapDescriptorFactory;
import com.huawei.hms.maps.model.CameraPosition;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.Marker;
import com.huawei.hms.maps.model.MarkerOptions;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.dto.FiltroMapa;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponseLocationsAgencies;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityGeoreferencing.ActivityPointsAttention.ActivityPointsAttentionView;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.tools.PopUpWindowHms;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 11/05/2017.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 28/08/2021.
 */
public class ActivityLocationsHmsView extends ActivityBase implements ActivityLocationsHmsContract.View, HuaweiMap.OnMarkerClickListener {

    private ActivityLocationsHmsPresenter presenter;
    private ActivityBase context;
    private GlobalState state;

    private HuaweiMap huaweiMap;
    private LocationManager locationManager;
    private boolean isLocationEnabled;
    private String latitud;
    private String longitud;

    private List<ResponseLocationsAgencies> listAgencias;
    private List<FiltroMapa> listFiltros;
    private List<String> listFiltroTipo;
    private List<String> listFiltroCiudad;
    private List<String> listFiltroAlmacen;
    private View mapView;

    private static final long MIN_TIME_BW_UPDATES = 1000 * 20 * 1;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final String HUAWEIMAP_COMPASS = "HuaweiMapCompass";                   // [4]
    private static final String HUAWEIMAP_TOOLBAR = "HuaweiMapToolbar";                   // [3]
    private static final String HUAWEIMAP_ZOOMIN_BUTTON = "HuaweiMapZoomInButton";        // [2]child[0]
    private static final String HUAWEIMAP_MYLOCATION_BUTTON = "HuaweiMapMyLocationButton";// [0]

    @BindView(R.id.spinnerFiltroTipo)
    Spinner spinnerFiltroTipo;
    @BindView(R.id.spinnerFiltroCiudad)
    Spinner spinnerFiltroCiudad;
    @BindView(R.id.spinnerFiltroAlmacen)
    Spinner spinnerFiltroAlmacen;
    @BindView(R.id.btn_back)
    ImageButton btnBack;

    @BindView(R.id.container_maphuawei)
    LinearLayout mapContainerHuaweiLayout;
    @BindView(R.id.mapHuawei)
    FrameLayout fragmentMapHuawei;
    @BindView(R.id.controlsButtons)
    LinearLayout controlsButtons;
    @BindView(R.id.contentLocations)
    LinearLayout contentLocations;

    @BindView(R.id.layout_circular_progress_bar)
    LinearLayout layoutCircularProgressBar;
    @BindView(R.id.circular_progress_bar)
    ProgressBar circularProgressBar;
    @BindView(R.id.text_circular_progress_Bar)
    TextView textCircularProgressBar;
    @BindView(R.id.imageReferesh)
    ImageView buttonReferesh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_georeferencing_locations_hms);
        ButterKnife.bind(this);
        setControls();
    }

    protected void setControls() {
        presenter = new ActivityLocationsHmsPresenter(this, new ActivityLocationsHmsModel());
        context = this;
        state = context.getState();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mapView = fragmentMapHuawei.getRootView();
        listFiltros = new ArrayList<>();
        listFiltroTipo = new ArrayList<>();
        listFiltroCiudad = new ArrayList<>();
        listFiltroAlmacen = new ArrayList<>();
        fetchLocationsAgencies();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.btn_back)
    public void onClickBack(){
        onBackPressed();
    }

    @OnClick(R.id.imageReferesh)
    public void onClickRefresh(){
        state.setAgencias(null);
        fetchLocationsAgencies();
    }

    @OnItemSelected(R.id.spinnerFiltroTipo)
    void onItemSelectedTipo(int position) {
        if(spinnerFiltroTipo.getSelectedItem() != null){
            FiltroMapa filtroMapa = (FiltroMapa) spinnerFiltroTipo.getSelectedItem();
            String tipo = filtroMapa.getI_tipage();
            String ciudad = spinnerFiltroCiudad.getSelectedItem().toString();

            if (tipo.equals("ALM")) {
                spinnerFiltroAlmacen.setEnabled(true);
                spinnerFiltroAlmacen.setClickable(true);
            } else {
                spinnerFiltroAlmacen.setSelection(0);
                spinnerFiltroAlmacen.setEnabled(false);
                spinnerFiltroAlmacen.setClickable(false);
            }

            listFiltroAlmacen.clear();
            listFiltroCiudad.clear();

            for (ResponseLocationsAgencies a : listAgencias) {
                if (!listFiltroCiudad.contains(a.getN_ciudad())) {
                    if(tipo.equals("TODO")){
                        listFiltroCiudad.add(a.getN_ciudad().trim());
                    }else if(a.getI_tipage().equals(tipo)){
                        listFiltroCiudad.add(a.getN_ciudad().trim());
                    }
                }
                if(!listFiltroAlmacen.contains(a.getT_sucurs().trim()) && a.getI_tipage().equals("ALM")){
                    if(ciudad.equals("TODO")){
                        listFiltroAlmacen.add(a.getT_sucurs().trim());
                    }else if(ciudad.equals(a.getN_ciudad())){
                        listFiltroAlmacen.add(a.getT_sucurs().trim());
                    }
                }
            }

            java.util.Collections.sort(listFiltroCiudad, Collator.getInstance());
            listFiltroCiudad.add(0, "TODO");
            ArrayAdapter<String> adapter_ciudad = new ArrayAdapter<>(context, R.layout.list_item_spinner, listFiltroCiudad);
            adapter_ciudad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerFiltroCiudad.setSelection(0);

            ArrayAdapter<String> adapter_almacen = new ArrayAdapter<>(context, R.layout.list_item_spinner, listFiltroAlmacen);
            listFiltroAlmacen.add(0, "TODO");
            adapter_almacen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerFiltroAlmacen.setSelection(0);

            drawPoints();
        }
    }

    @OnItemSelected(R.id.spinnerFiltroCiudad)
    void onItemSelectedCiudad(int position) {
        if(spinnerFiltroCiudad.getSelectedItem() != null){
            String ciudad = spinnerFiltroCiudad.getSelectedItem().toString();
            listFiltroAlmacen.clear();
            for (ResponseLocationsAgencies a : listAgencias) {
                if(!listFiltroAlmacen.contains(a.getT_sucurs().trim()) &&
                        a.getI_tipage().equals("ALM")){
                    if(ciudad.equals("TODO")){
                        listFiltroAlmacen.add(a.getT_sucurs().trim());
                    }else if(ciudad.equals(a.getN_ciudad())){
                        listFiltroAlmacen.add(a.getT_sucurs().trim());
                    }
                }
            }
            ArrayAdapter<String> adapter_almacen = new ArrayAdapter<>(context, R.layout.list_item_spinner, listFiltroAlmacen);
            listFiltroAlmacen.add(0, "TODO");
            adapter_almacen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerFiltroAlmacen.setSelection(0);
            drawPoints();
        }
    }

    @OnItemSelected(R.id.spinnerFiltroAlmacen)
    void onItemSelectedAlmacen(int position) {
        if(spinnerFiltroAlmacen.getSelectedItem() != null){
            drawPoints();
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitud = String.valueOf(location.getLongitude());
            latitud = String.valueOf(location.getLatitude());
        }
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }
        @Override
        public void onProviderEnabled(String s) {
        }
        @Override
        public void onProviderDisabled(String s) {
        }
    };

    @Override
    public void fetchLocationsAgencies(){
        try{
            if (state != null && state.getAgencias() != null && state.getAgencias().size() > 0) {
                showFilters(state.getAgencias());
                hideCircularProgressBar();
                showSectionLocations();
            }else{
                presenter.fetchLocationsAgencies();
            }
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
            showErrorWithRefresh();
        }
    }

    @Override
    public void showFilters(List<ResponseLocationsAgencies> agencias) {
        state.setAgencias(agencias);
        this.listAgencias = agencias;

        FiltroMapa filtro = new FiltroMapa();
        filtro.setI_tipage("TODO");
        filtro.setDescripcion("TODO");
        listFiltros.clear();
        listFiltros.add(filtro);
        for (ResponseLocationsAgencies a : agencias) {
            if (!listFiltroTipo.contains(a.getI_tipage())) {
                FiltroMapa fm = new FiltroMapa();
                fm.setI_tipage(a.getI_tipage());
                fm.setDescripcion(getNameAgencyType(a.getI_tipage()));
                listFiltros.add(fm);
                listFiltroTipo.add(a.getI_tipage());
            }
            if(!listFiltroCiudad.contains(a.getN_ciudad())){
                listFiltroCiudad.add(a.getN_ciudad().trim());
            }
            if(!listFiltroAlmacen.contains(a.getT_sucurs().trim()) && a.getI_tipage().equals("ALM")){
                listFiltroAlmacen.add(a.getT_sucurs().trim());
            }
        }
        ArrayAdapter<FiltroMapa> adapter = new ArrayAdapter<>(context, R.layout.list_item_spinner, listFiltros);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltroTipo.setAdapter(adapter);

        java.util.Collections.sort(listFiltroCiudad, Collator.getInstance());
        listFiltroCiudad.add(0, "TODO");
        ArrayAdapter<String> adapter_ciudad = new ArrayAdapter<>(context, R.layout.list_item_spinner, listFiltroCiudad);
        adapter_ciudad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltroCiudad.setAdapter(adapter_ciudad);

        ArrayAdapter<String> adapter_almacen = new ArrayAdapter<>(context, R.layout.list_item_spinner, listFiltroAlmacen);
        listFiltroAlmacen.add(0, "TODO");
        adapter_almacen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltroAlmacen.setAdapter(adapter_almacen);

        validatePermissionsLocation();
//        showAgencies(listAgencias, view.getZoomDesiredByKilometros(7));
    }

    @Override
    public void validatePermissionsLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                isLocationEnabled = true;
                showMap();
                drawPoints();
                return;
            }
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_COARSE_LOCATION)){
                isLocationEnabled = false;
                showMap();
                drawPoints();
            }else{
                requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, GlobalState.PERMISSION_LOCATION);
            }
        }else{
            isLocationEnabled = true;
            showMap();
            drawPoints();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == GlobalState.PERMISSION_LOCATION){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                isLocationEnabled = true;
            }else{
                isLocationEnabled = false;
            }
            showMap();
            drawPoints();
        }
    }

    @Override
    public void showMap(){
        try{
            if(context.getState().isHmsSystem()){
                MapFragment mapContainer = null;
                mapContainerHuaweiLayout.setVisibility(View.VISIBLE);
                mapContainer = ((MapFragment) context.getFragmentManager().findFragmentById(R.id.mapHuawei));
                mapContainer.getMapAsync(new com.huawei.hms.maps.OnMapReadyCallback() {
                    @Override
                    public void onMapReady(HuaweiMap huaweiMap) {
                        ActivityLocationsHmsView.this.huaweiMap = huaweiMap;
                        if (ActivityLocationsHmsView.this.huaweiMap != null) {
                            try{
                                ActivityLocationsHmsView.this.huaweiMap.setMapType(huaweiMap.MAP_TYPE_NORMAL);
                                ActivityLocationsHmsView.this.huaweiMap.setMyLocationEnabled(true);
                                ActivityLocationsHmsView.this.huaweiMap.setOnMarkerClickListener(ActivityLocationsHmsView.this);
                                ActivityLocationsHmsView.this.huaweiMap.getUiSettings().setZoomControlsEnabled(true);
                                ActivityLocationsHmsView.this.huaweiMap.getUiSettings().setCompassEnabled(true);
                                ActivityLocationsHmsView.this.huaweiMap.getUiSettings().setMapToolbarEnabled(true);
                                ActivityLocationsHmsView.this.huaweiMap.getUiSettings().setTiltGesturesEnabled(true);
                                ActivityLocationsHmsView.this.huaweiMap.getUiSettings().setRotateGesturesEnabled(true);

                                if (huaweiMap != null) {
                                    View toolbarButton = mapView.findViewWithTag(HUAWEIMAP_TOOLBAR);
                                    if(toolbarButton != null){
                                        if(toolbarButton.getParent() != null) {
                                            ((ViewGroup)toolbarButton.getParent()).removeView(toolbarButton); // <- fix
                                        }
                                        toolbarButton.setPadding(0,0,0,10);
                                        controlsButtons.addView(toolbarButton);
                                    }

                                    View compassButton = mapView.findViewWithTag(HUAWEIMAP_COMPASS);
                                    if(compassButton!= null){
                                        if(compassButton.getParent() != null) {
                                            ((ViewGroup)compassButton.getParent()).removeView(compassButton); // <- fix
                                        }
                                        compassButton.setPadding(0,0,0,10);
                                        controlsButtons.addView(compassButton);
                                    }

                                    View locationButton = mapView.findViewWithTag(HUAWEIMAP_MYLOCATION_BUTTON);
                                    if(locationButton != null){
                                        if(locationButton.getParent() != null) {
                                            ((ViewGroup)locationButton.getParent()).removeView(locationButton); // <- fix
                                        }
                                        locationButton.setPadding(0,0,0,10);
                                        controlsButtons.addView(locationButton);
                                    }

                                    View zoomIn = mapView.findViewWithTag(HUAWEIMAP_ZOOMIN_BUTTON);
                                    if(zoomIn != null){
                                        View zoomButtonInOut = (View) zoomIn.getParent();
                                        if(zoomButtonInOut != null){
                                            if(zoomButtonInOut.getParent() != null) {
                                                ((ViewGroup)zoomButtonInOut.getParent()).removeView(zoomButtonInOut); // <- fix
                                            }
                                            zoomButtonInOut.setPadding(0,0,0,10);
                                            controlsButtons.addView(zoomButtonInOut);
                                        }
                                    }
                                }
                            }catch (SecurityException e){
                                Log.d("GeoReferenciacion", e.getMessage());
                            }
                        }
                    }
                });

            }
        }catch(Exception ex){
            Log.d("Error", ex.getMessage());
        }
    }

    @Override
    public void showAgencies(List<ResponseLocationsAgencies> listAgencies, float zoomCamera) {
        if (huaweiMap != null) {
            huaweiMap.clear();
        }
        if (listAgencies != null) {

            for (ResponseLocationsAgencies agencia : listAgencies) {
                showMarker(agencia);
            }

            try {
                if (isLocationEnabled && locationManager != null) {
                    if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        toggleNetworkUpdates();
                    } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        toggleGPSUpdates();
                    } else {
                        toggleBestUpdates();
                    }
                }else{
                    longitud = null;
                    latitud = null;
                }
            }catch(Exception e){
                longitud = null;
                latitud = null;
            }

            LatLng latLng = null;
            if(latitud != null && longitud != null){
                latLng = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));
            }else{
                ResponseLocationsAgencies agenciaReferencia = listAgencies.size() > 0 ? listAgencies.get(0) : null;
                Object selectedItem = spinnerFiltroCiudad.getSelectedItem();
                String filtroCiudad = selectedItem != null ? selectedItem.toString() : null;

                if(!TextUtils.isEmpty(filtroCiudad) && !filtroCiudad.equalsIgnoreCase("TODO")
                        && agenciaReferencia != null){
                    latLng = new LatLng(Double.parseDouble(agenciaReferencia.getN_latitu()),
                            Double.parseDouble(agenciaReferencia.getN_longit()));
                }
            }
            zoomCameraHuawei(zoomCamera, latLng);
        }
    }

    @Override
    public void showMarker(ResponseLocationsAgencies agencia) {
        if (!agencia.getN_latitu().equals("0") && !agencia.getN_longit().equals("0")) {
            try {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(Double.valueOf(agencia.getN_latitu().trim()), Double.valueOf(agencia.getN_longit().trim())));
                markerOptions.title(agencia.getN_sucurs());
                markerOptions.icon(BitmapDescriptorFactory.fromResource(getIdDrawableByAgency(agencia)));
                markerOptions.snippet(agencia.toString());
                if(markerOptions==null || huaweiMap ==null) return;
                huaweiMap.addMarker(markerOptions).setVisible(true);
            } catch (Exception e) {
                Log.d("Error", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String sucursal = marker.getTitle();
        double latitud = marker.getPosition().latitude;
        double longitud = marker.getPosition().longitude;

        GlobalState state = context.getState();
        if(state != null){
            state.setNombreAgenciaSeleccionada(sucursal+"|"+latitud+"|"+longitud);
            Intent intent = new Intent(this, ActivityPointsAttentionView.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void zoomCameraHuawei(float zoomCamera, LatLng actual) {
        try {
            if (huaweiMap == null) return;

            if(actual == null ){
                actual = new LatLng(4.0830360, -73.6636180);
                zoomCamera = 10;
            }

            CameraPosition camPos = new CameraPosition.Builder()
                    .target(actual) // Centramos el mapa
                    .zoom(zoomCamera) // Establecemos el zoom
                    .bearing(45) // Establecemos la orientación con el
                    .build();

            CameraUpdate camUpd = CameraUpdateFactory.newCameraPosition(camPos);
            huaweiMap.animateCamera(camUpd);
            huaweiMap.setInfoWindowAdapter(new PopUpWindowHms(context));
        } catch (Exception e) {
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void toggleNetworkUpdates() {
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
    }

    @Override
    public void toggleGPSUpdates() {
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
    }

    @Override
    public void toggleBestUpdates() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            locationManager.requestLocationUpdates(
                    provider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
        }
    }

    @Override
    public void showDialogPermissions(int requestCode){
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle("Permisos desactivados");
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage("Debes aceptar los permisos para el corrego funcionamiento de la APP");
        d.setCancelable(true);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(context, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, requestCode);
                dialog.dismiss();
            }
        });
        d.show();
    }

    @Override
    public void requestLocationPermits(){
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle("Permisos desactivados");
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage("Desea configurar los permisos de forma manual");
        d.setCancelable(true);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(),null);
                intent.setData(uri);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        d.show();
    }

    @Override
    public void drawPoints() {
        FiltroMapa filtroMapa = (FiltroMapa) spinnerFiltroTipo.getSelectedItem();
        String filtroTipo = filtroMapa.getI_tipage();
        String filtroCiudad = spinnerFiltroCiudad.getSelectedItem().toString();
        String filtroAlmacen = spinnerFiltroAlmacen.getSelectedItem().toString();
        List<ResponseLocationsAgencies> listAgencies = null;

        if (state != null && state.getAgencias() != null && state.getAgencias().size() > 0) {
            listAgencies = state.getAgencias();
        } else if (NetworkHelper.isConnectionAvailable(context)) {
            fetchLocationsAgencies();
            return;
        } else {
            AlertDialog.Builder d = new AlertDialog.Builder(context);
            d.setTitle("Permisos desactivados");
            d.setIcon(R.mipmap.icon_presente);
            d.setMessage("Para poder acceder a las sucursales debe habilitar los servicios de Internet y GPS, ¿Desea configurar los permisos de forma manual?");
            d.setCancelable(true);
            d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                    dialog.dismiss();
                }
            });
            d.show();
            return;
        }

        ArrayList<ResponseLocationsAgencies> listadoFiltrado = new ArrayList<>();
        if (listAgencies != null) {

            for (ResponseLocationsAgencies a : listAgencies) {

                boolean igual_a_tipo = !TextUtils.isEmpty(filtroTipo) &&
                        (filtroTipo.equalsIgnoreCase("TODO") || a.getI_tipage().equalsIgnoreCase(filtroTipo));

                boolean igual_a_ciudad =    !TextUtils.isEmpty(filtroCiudad) &&
                        (filtroCiudad.equalsIgnoreCase("TODO") || a.getN_ciudad().equalsIgnoreCase(filtroCiudad));

                boolean igual_a_almacen = !TextUtils.isEmpty(filtroAlmacen) &&
                        (filtroAlmacen.equalsIgnoreCase("TODO") || a.getT_sucurs().equalsIgnoreCase(filtroAlmacen));

                if (igual_a_tipo && igual_a_ciudad && igual_a_almacen) {
                    listadoFiltrado.add(a);
                }
            }

        }

        if (listadoFiltrado.size() > 0) {
            showAgencies(listadoFiltrado, getZoomDesiredByKilometros(1));
        }else{
            makeLToast("Sin datos para mostrar para el filtro seleccionado");
        }
    }

    @Override
    public float getZoomDesiredByKilometros(int desiredKm) {
        float zoomCamera = 13;
        switch (desiredKm) {
            case 1:
                zoomCamera = 13;
                break;
            case 3:
                zoomCamera = 12;
                break;
            case 5:
                zoomCamera = 11;
                break;
            case 7:
                zoomCamera = 10;
                break;
            case 10:
                zoomCamera = 10;
                break;
        }
        return zoomCamera;
    }

    @Override
    public int getIdDrawableByAgency(ResponseLocationsAgencies agencia) {
        switch (agencia.getI_tipage()) {
            case "SUC":
                return R.drawable.pin_map;
            case "CV":
                return R.drawable.pin_centrosv;
            case "CAJ":
                return R.drawable.pin_cajeros;
            case "EDS":
                return R.drawable.pin_eds;
            case "ALM":
                if (agencia.getT_sucurs().equalsIgnoreCase("CARULLA") ||
                        agencia.getT_sucurs().equalsIgnoreCase("CARULLA EXPRESS")) {
                    return R.drawable.pin_carulla;
                } else if (agencia.getT_sucurs().equalsIgnoreCase("SURTIMAX")) {
                    return R.drawable.pin_surtimax;
                } else if (agencia.getT_sucurs().equalsIgnoreCase("SUPER INTER")) {
                    return R.drawable.pin_superinter;
                } else if(agencia.getT_sucurs().equalsIgnoreCase("SURTIMAYORISTA")){
                    return R.drawable.surti_mayorista;
                }
                else {
                    return R.drawable.pin_exito;
                }
            default:
                return R.drawable.pin_map;
        }
    }

    @Override
    public String getNameAgencyType(String idAgency) {
        switch (idAgency) {
            case "SUC":
                return "Oficinas PRESENTE";
            case "CV":
                return "Centros Vacacionales";
            case "CAJ":
                return "Cajeros PRESENTE";
            case "ALM":
                return "Almacenes";
            case "EDS":
                return "Estaciones de Servicio";
            default:
                return null;
        }
    }

    @Override
    public void showSectionLocations() {
        contentLocations.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSectionLocations() {
        contentLocations.setVisibility(View.GONE);
    }

    @Override
    public void showCircularProgressBar(String textProgressBar) {
        layoutCircularProgressBar.setVisibility(View.VISIBLE);
        textCircularProgressBar.setText(textProgressBar);
    }

    @Override
    public void hideCircularProgressBar() {
        layoutCircularProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorWithRefresh(){
        contentLocations.setVisibility(View.GONE);
        layoutCircularProgressBar.setVisibility(View.VISIBLE);
        circularProgressBar.setVisibility(View.GONE);
        textCircularProgressBar.setText("Ha ocurrido un error, inténtalo de nuevo ");
        buttonReferesh.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorTimeOut() {
        String message = "Ha ocurrido un error. Intenta de nuevo y si el error persiste, contacta a PRESENTE.";
        if(state != null && state.getMensajesRespuesta() != null && state.getMensajesRespuesta().size()>0){
            for(ResponseMensajesRespuesta rm : state.getMensajesRespuesta()){
                if(rm.getIdMensaje() == 6){
                    message = rm.getMensaje();
                }
            }
        }
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle(context.getResources().getString(R.string.app_name));
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(message);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        d.show();
    }

    @Override
    public void showDataFetchError(String title, String message){
        if(TextUtils.isEmpty(message)){
            message = "Ha ocurrido un error. Intenta de nuevo y si el error persiste, contacta a PRESENTE.";
            if(state != null && state.getMensajesRespuesta() != null && state.getMensajesRespuesta().size()>0){
                for(ResponseMensajesRespuesta rm : state.getMensajesRespuesta()){
                    if(rm.getIdMensaje() == 7){
                        message = rm.getMensaje();
                    }
                }
            }
        }
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle(title);
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(message);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        d.show();
    }

    @Override
    public void showExpiredToken(String message) {
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle("Sesión finalizada");
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(message);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                context.salir();
            }
        });
        d.show();
    }
}