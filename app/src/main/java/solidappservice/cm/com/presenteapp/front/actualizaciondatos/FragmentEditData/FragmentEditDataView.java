package solidappservice.cm.com.presenteapp.front.actualizaciondatos.FragmentEditData;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.DatosAsociado;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseFormatoDirecciones;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseUbicaciones;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.actualizaciondatos.ActivityUpdatePersonalData.ActivityUpdatePersonalDataView;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.HeightProviderFragment;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 11/07/2021.
 */
public class FragmentEditDataView extends Fragment implements FragmentEditDataContract.View, View.OnClickListener {

    private FragmentEditDataPresenter presenterEditData;
    private ActivityUpdatePersonalDataView baseView;
    private ActivityBase context;
    private GlobalState state;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.contentEditData)
    RelativeLayout contentEditData;

    @BindView(R.id.layout_circular_progress_bar)
    LinearLayout layoutCircularProgressBar;
    @BindView(R.id.circular_progress_bar)
    ProgressBar circularProgressBar;
    @BindView(R.id.text_circular_progress_Bar)
    TextView textCircularProgressBar;
    @BindView(R.id.imageReferesh)
    ImageView buttonReferesh;

    @BindView(R.id.view_final_Editar)
    View viewFinal;
    @BindView(R.id.tv_act_datos_primernombre)
    TextView tvPrimerNombre;
    //Datos Personales
    @BindView(R.id.et_act_datos_nombre)
    EditText etNombre;
    @BindView(R.id.et_act_datos_celular)
    EditText etCelular;
    @BindView(R.id.et_act_datos_correo)
    EditText etCorreo;
    @BindView(R.id.sp_act_datos_pais)
    Spinner spPais;
    @BindView(R.id.sp_act_datos_departamento)
    Spinner spDepartamento;
    @BindView(R.id.sp_act_datos_ciudad)
    Spinner spCiudad;
    @BindView(R.id.et_act_datos_barrio)
    EditText etBarrio;
    //Dreccion Via Principal
    @BindView(R.id.spinner_tipoVia)
    Spinner spTipoVia;
    @BindView(R.id.et_numero_via)
    EditText etNumeroVia;
    @BindView(R.id.spinner_letra)
    Spinner spLetraVia;
    @BindView(R.id.spinner_complemento_via)
    Spinner spComplementoVia;
    //Dreccion Via Secundaria
    @BindView(R.id.et_numero_via2)
    EditText etNumeroVia2;
    @BindView(R.id.spinner_letraVia2)
    Spinner spLetraVia2;
    @BindView(R.id.spinner_complemento_via2)
    Spinner spComplementoVia2;
    @BindView(R.id.et_numeroComplementoVia2)
    EditText etNumeroComplemento;
    @BindView(R.id.et_informacion_adicional)
    EditText etIformacionAdicional;
    //Vista Direccion Completa
    @BindView(R.id.tv_direccion_completa)
    TextView tvDireccionCompleta;
    //Boton Guardar


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de editar de actualización de datos");
        firebaseAnalytics.logEvent("pantalla_act_datos_editar", params);
        View view = inflater.inflate(R.layout.fragment_updatepersonaldata_editdata, container, false);
        ButterKnife.bind(this, view);
        new HeightProviderFragment(this).init().setHeightListener(new HeightProviderFragment.HeightListener() {
            @Override
            public void onHeightChanged(int height) {
                viewFinal.setLayoutParams(new LinearLayout.LayoutParams(100,height));
            }
        });
        setControls();
        return view;
    }

    protected void setControls() {
        presenterEditData = new FragmentEditDataPresenter(this, new FragmentEditDataModel());
        baseView = (ActivityUpdatePersonalDataView) getActivity();
        context = (ActivityBase) getActivity();
        state = context.getState();
        tvPrimerNombre.setText(context.getState() == null ? "" : context.getState().getUsuario().getNombreAsociado()+"...");
        tvPrimerNombre.setTypeface(null, Typeface.BOLD);
        if (baseView != null) {
            baseView.buttonBack.setVisibility(View.VISIBLE);
            baseView.buttonBack.setOnClickListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null || state.getUsuario() == null){
            context.salir();
        }else{
            processPersonalData();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            GlobalState state = context.getState();
            if (baseView != null && state != null) {
                baseView.basePresenter.loadFragmentUpdatePersonalData(IFragmentCoordinator.Pantalla.ActDatosValidateData);
            } else {
                context.salir();
            }
        }
    }

    @OnItemSelected({R.id.spinner_tipoVia, R.id.spinner_letra, R.id.spinner_complemento_via, R.id.spinner_letraVia2, R.id.spinner_complemento_via2, })
    void onItemSelected(int position) {
        obtenerDireccionCompleta();
    }

    @OnTextChanged({R.id.et_numero_via, R.id.et_numero_via2, R.id.et_numeroComplementoVia2, R.id.et_informacion_adicional})
    void afterTextChanged(Editable arg0) {
        obtenerDireccionCompleta();
    }

    @Override
    public void processPersonalData() {
        if(baseView.actualizaPrimeraVez != null){
            switch (baseView.actualizaPrimeraVez){
                case "Y":
                    if(baseView.datosAsociado != null && baseView.isDatosEditados){
                        etNombre.setText(baseView.datosAsociado.getNombreCompleto());
                        etCorreo.setText(baseView.datosAsociado.getEmail());
                        etCelular.setText(baseView.datosAsociado.getCelular());
                        etBarrio.setText(baseView.datosAsociado.getBarrio());
                    }else{
                        fetchPersonalData();
                    }
                    break;
                case "N":
                    if(baseView.datosAsociado != null){
                        etNombre.setText(baseView.datosAsociado.getNombreCompleto());
                        etCelular.setText(baseView.datosAsociado.getCelular());
                        etCorreo.setText(baseView.datosAsociado.getEmail());
                        etBarrio.setText(baseView.datosAsociado.getBarrio());
                    }else{
                        fetchPersonalData();
                    }
                    break;
            }
            if(state.getUbicaciones() == null){
                fetchLocations();
            } else {
                showLocations(state.getUbicaciones());
            }
        }
        else {
            baseView.basePresenter.loadFragmentUpdatePersonalData(IFragmentCoordinator.Pantalla.ActDatosStart);
        }
    }

    @Override
    public void fetchPersonalData() {
        try{
            GlobalState state = context.getState();
            Encripcion encripcion = Encripcion.getInstance();
            presenterEditData.fetchPersonalData(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch(Exception ex){
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void showPersonalData(DatosAsociado datos) {
        etNombre.setText(datos.getNombreCompleto());
    }

    @Override
    public void fetchLocations() {
        try{
            presenterEditData.fetchLocations();
        }catch(Exception ex){
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void showLocations(ResponseUbicaciones locations) {
        state.setUbicaciones(locations);
        if(locations.getPaises() != null){
            fillLocations(locations);
        }

        if(state.getFormatoDirecciones() == null){
            fetchAddressFormat();
        } else {
            showAddressFormat(state.getFormatoDirecciones());
            hideCircularProgressBar();
            showContentEditData();
        }
    }

    public void fillLocations(ResponseUbicaciones locations){
        fillSpinnerCountries(locations.getPaises());
        fillSpinnerDepartments(new ArrayList<ResponseUbicaciones.Departamento>());
        fillSpinnerCities(new ArrayList<ResponseUbicaciones.Ciudad>());
    }

    @OnItemSelected(R.id.sp_act_datos_pais)
    void onItemSelectedCountry(AdapterView<?> parent, View view, int positionPais, long id) {
        ResponseUbicaciones.Pais pais = (ResponseUbicaciones.Pais) parent.getItemAtPosition(positionPais);
        if (!TextUtils.isEmpty(pais.getIdPais())){
            baseView.datosAsociado.setIdPais(pais.getIdPais());
            fillSpinnerDepartments(pais.getDepartamentos());
        }else{
            fillSpinnerDepartments(new ArrayList<ResponseUbicaciones.Departamento>());
            fillSpinnerCities(new ArrayList<ResponseUbicaciones.Ciudad>());
        }
    }

    @OnItemSelected(R.id.sp_act_datos_departamento)
    void onItemSelectedDepartment(AdapterView<?> parent, View view, int positionPais, long id) {
        ResponseUbicaciones.Departamento depart = (ResponseUbicaciones.Departamento) parent.getItemAtPosition(spDepartamento.getSelectedItemPosition());
        if (!TextUtils.isEmpty(depart.getIdDepartamento())){
            baseView.datosAsociado.setIdDepartamento(depart.getIdDepartamento());
            fillSpinnerCities(depart.getCiudades());
        }else{
            fillSpinnerCities(new ArrayList<ResponseUbicaciones.Ciudad>());
        }
    }

    @OnItemSelected(R.id.sp_act_datos_ciudad)
    void onItemSelectedCity(AdapterView<?> parent, View view, int positionPais, long id) {
        ResponseUbicaciones.Ciudad ciudad = (ResponseUbicaciones.Ciudad) parent.getItemAtPosition(spCiudad.getSelectedItemPosition());
        if (!TextUtils.isEmpty(ciudad.getIdCiudad())){
            baseView.datosAsociado.setIdCiudad(ciudad.getIdCiudad());
        }
    }

    public void fillSpinnerCountries(List<ResponseUbicaciones.Pais> listaCountries){
        ArrayAdapter<ResponseUbicaciones.Pais> adapterPais = new ArrayAdapter<ResponseUbicaciones.Pais>(context, R.layout.support_simple_spinner_dropdown_item){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (position == 0) {
                    ((TextView)view.findViewById(android.R.id.text1)).setText(getItem(position).getNombrePais());
                    ((TextView)view.findViewById(android.R.id.text1)).setTextColor(Color.GRAY);
                    ((TextView)view.findViewById(android.R.id.text1)).setTypeface(null, Typeface.ITALIC);
                }
                return view;
            }
        };
        adapterPais.add(new ResponseUbicaciones.Pais("", "Selecciona una opción"));
        adapterPais.addAll(listaCountries);
        spPais.setAdapter(adapterPais);
        if(!TextUtils.isEmpty(baseView.datosAsociado.getIdPais())){
            for (ResponseUbicaciones.Pais pais : listaCountries) {
                if (pais.getIdPais().equals(baseView.datosAsociado.getIdPais())) {
                    int spinnerPosition = adapterPais.getPosition(pais);
                    spPais.setSelection(spinnerPosition);
                }
            }
        }
    }
    public void fillSpinnerDepartments(List<ResponseUbicaciones.Departamento> listDepartaments){
        ArrayAdapter<ResponseUbicaciones.Departamento> adapterDepartamento = new ArrayAdapter<ResponseUbicaciones.Departamento>
                (context, R.layout.support_simple_spinner_dropdown_item){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (position == 0) {
                    ((TextView)view.findViewById(android.R.id.text1)).setText(getItem(position).getNombreDepartamento());
                    ((TextView)view.findViewById(android.R.id.text1)).setTextColor(Color.GRAY);
                    ((TextView)view.findViewById(android.R.id.text1)).setTypeface(null, Typeface.ITALIC);
                }
                return view;
            }
        };
        adapterDepartamento.add(new ResponseUbicaciones.Departamento("", "Selecciona una opción"));
        adapterDepartamento.addAll(listDepartaments);
        spDepartamento.setAdapter(adapterDepartamento);
        if(!TextUtils.isEmpty(baseView.datosAsociado.getIdPais()) &&
                !TextUtils.isEmpty(baseView.datosAsociado.getIdDepartamento())){
            for (ResponseUbicaciones.Departamento depart : listDepartaments) {
                if (depart.getIdDepartamento().equals(baseView.datosAsociado.getIdDepartamento())) {
                    int spinnerPosition = adapterDepartamento.getPosition(depart);
                    spDepartamento.setSelection(spinnerPosition);
                }
            }
        }
    }

    public void fillSpinnerCities(List<ResponseUbicaciones.Ciudad> listCities){
        ArrayAdapter<ResponseUbicaciones.Ciudad> adapterCiudad = new ArrayAdapter<ResponseUbicaciones.Ciudad>
                (context, R.layout.support_simple_spinner_dropdown_item){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (position == 0) {
                    ((TextView)view.findViewById(android.R.id.text1)).setText(getItem(position).getNombreCiudad());
                    ((TextView)view.findViewById(android.R.id.text1)).setTextColor(Color.GRAY);
                    ((TextView)view.findViewById(android.R.id.text1)).setTypeface(null, Typeface.ITALIC);
                }
                return view;
            }
        };
        adapterCiudad.add(new ResponseUbicaciones.Ciudad("", "Selecciona una opción"));
        adapterCiudad.addAll(listCities);
        spCiudad.setAdapter(adapterCiudad);
        if(!TextUtils.isEmpty(baseView.datosAsociado.getIdPais()) &&
                !TextUtils.isEmpty(baseView.datosAsociado.getIdDepartamento()) &&
                !TextUtils.isEmpty(baseView.datosAsociado.getIdCiudad())){
            for (ResponseUbicaciones.Ciudad ciudad : listCities) {
                if (ciudad.getIdCiudad().equals(baseView.datosAsociado.getIdCiudad())) {
                    int spinnerPosition = adapterCiudad.getPosition(ciudad);
                    spCiudad.setSelection(spinnerPosition);
                }
            }
        }
    }

    @Override
    public void fetchAddressFormat(){
        try{
            presenterEditData.fetchAddressFormat();
        }catch(Exception ex){
            showDataFetchError("Lo sentimos", "");
        }
    }

    @Override
    public void showAddressFormat(ResponseFormatoDirecciones addressFormat) {
        state.setFormatoDirecciones(addressFormat);
        if(addressFormat != null){
            fillTypesOfRoad(addressFormat.getTiposVia());
            fillLettersOfRoad(addressFormat.getLetrasVia());
            fillAddOnsOfRoad(addressFormat.getComplementosVia());
        }
        if(baseView.datosAsociado != null && baseView.datosAsociado.getDetalleDireccion() != null){
            etNumeroVia.setText(baseView.datosAsociado.getDetalleDireccion().getNumeroVia1() != null ?
                    baseView.datosAsociado.getDetalleDireccion().getNumeroVia1() : "");
            etNumeroVia2.setText(baseView.datosAsociado.getDetalleDireccion().getNumeroVia2() != null ?
                    baseView.datosAsociado.getDetalleDireccion().getNumeroVia2() : "");
            etNumeroComplemento.setText(baseView.datosAsociado.getDetalleDireccion().getNumeroComplemento() != null ?
                    baseView.datosAsociado.getDetalleDireccion().getNumeroComplemento() : "");
            etIformacionAdicional.setText(baseView.datosAsociado.getDetalleDireccion().getInformacionAdicional() != null ?
                    baseView.datosAsociado.getDetalleDireccion().getInformacionAdicional() : "");
        }
    }

    public void fillTypesOfRoad(List<ResponseFormatoDirecciones.ItemsDirecciones> listTypesOfRoad){
        ArrayAdapter<ResponseFormatoDirecciones.ItemsDirecciones> adapterTiposVia = new ArrayAdapter<ResponseFormatoDirecciones.ItemsDirecciones>
                (context, R.layout.support_simple_spinner_dropdown_item){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (position == 0) {
                    ((TextView)view.findViewById(android.R.id.text1)).setText(getItem(position).getValue());
                    ((TextView)view.findViewById(android.R.id.text1)).setTextColor(Color.GRAY);
                    ((TextView)view.findViewById(android.R.id.text1)).setTypeface(null, Typeface.ITALIC);
                }
                return view;
            }
        };
        adapterTiposVia.add(new ResponseFormatoDirecciones.ItemsDirecciones("","Ingresa aquí el tipo de vía"));
        adapterTiposVia.addAll(listTypesOfRoad);
        spTipoVia.setAdapter(adapterTiposVia);
        if(baseView.datosAsociado != null && baseView.datosAsociado.getDetalleDireccion() != null &&
                !TextUtils.isEmpty(baseView.datosAsociado.getDetalleDireccion().getTipoVia())){
            for (ResponseFormatoDirecciones.ItemsDirecciones typeOfRoad : listTypesOfRoad) {
                if (typeOfRoad.getValue().equals(baseView.datosAsociado.getDetalleDireccion().getTipoVia())) {
                    int spinnerPosition = adapterTiposVia.getPosition(typeOfRoad);
                    spTipoVia.setSelection(spinnerPosition);
                }
            }
        }
    }

    public void fillLettersOfRoad(List<ResponseFormatoDirecciones.ItemsDirecciones> listLettersOfRoad){
        ArrayAdapter<ResponseFormatoDirecciones.ItemsDirecciones> adapterLetrasVia = new ArrayAdapter<ResponseFormatoDirecciones.ItemsDirecciones>
                (context, R.layout.support_simple_spinner_dropdown_item){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (position == 0) {
                    ((TextView)view.findViewById(android.R.id.text1)).setText(getItem(position).getValue());
                    ((TextView)view.findViewById(android.R.id.text1)).setTextColor(Color.GRAY);
                    ((TextView)view.findViewById(android.R.id.text1)).setTypeface(null, Typeface.ITALIC);
                }
                return view;
            }
        };
        adapterLetrasVia.add(new ResponseFormatoDirecciones.ItemsDirecciones("","Selecciona una letra"));
        adapterLetrasVia.addAll(listLettersOfRoad);
        spLetraVia.setAdapter(adapterLetrasVia);
        spLetraVia2.setAdapter(adapterLetrasVia);
        if(baseView.datosAsociado != null && baseView.datosAsociado.getDetalleDireccion() != null &&
                !TextUtils.isEmpty(baseView.datosAsociado.getDetalleDireccion().getLetraVia1())){
            for (ResponseFormatoDirecciones.ItemsDirecciones lettersOfRoad : listLettersOfRoad) {
                if (lettersOfRoad.getValue().equals(baseView.datosAsociado.getDetalleDireccion().getLetraVia1())) {
                    int spinnerPosition = adapterLetrasVia.getPosition(lettersOfRoad);
                    spLetraVia.setSelection(spinnerPosition);
                }
            }
        }
        if(baseView.datosAsociado != null && baseView.datosAsociado.getDetalleDireccion() != null &&
                !TextUtils.isEmpty(baseView.datosAsociado.getDetalleDireccion().getLetraVia2())){
            for (ResponseFormatoDirecciones.ItemsDirecciones lettersOfRoad : listLettersOfRoad) {
                if (lettersOfRoad.getValue().equals(baseView.datosAsociado.getDetalleDireccion().getLetraVia2())) {
                    int spinnerPosition = adapterLetrasVia.getPosition(lettersOfRoad);
                    spLetraVia2.setSelection(spinnerPosition);
                }
            }
        }
    }

    public void fillAddOnsOfRoad(List<ResponseFormatoDirecciones.ItemsDirecciones> listAddOnsOfRoad){
        ArrayAdapter<ResponseFormatoDirecciones.ItemsDirecciones> adapterComplementosVia = new ArrayAdapter<ResponseFormatoDirecciones.ItemsDirecciones>
                (context, R.layout.support_simple_spinner_dropdown_item){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (position == 0) {
                    ((TextView)view.findViewById(android.R.id.text1)).setText(getItem(position).getValue());
                    ((TextView)view.findViewById(android.R.id.text1)).setTextColor(Color.GRAY);
                    ((TextView)view.findViewById(android.R.id.text1)).setTypeface(null, Typeface.ITALIC);
                }
                return view;
            }
        };
        adapterComplementosVia.add(new ResponseFormatoDirecciones.ItemsDirecciones("","Selecciona un complemento"));
        adapterComplementosVia.addAll(listAddOnsOfRoad);
        spComplementoVia.setAdapter(adapterComplementosVia);
        spComplementoVia2.setAdapter(adapterComplementosVia);
        if(baseView.datosAsociado != null && baseView.datosAsociado.getDetalleDireccion() != null &&
                !TextUtils.isEmpty(baseView.datosAsociado.getDetalleDireccion().getComplementoVia1())){
            for (ResponseFormatoDirecciones.ItemsDirecciones addOnsOfRoad : listAddOnsOfRoad) {
                if (addOnsOfRoad.getValue().equals(baseView.datosAsociado.getDetalleDireccion().getComplementoVia1())) {
                    int spinnerPosition = adapterComplementosVia.getPosition(addOnsOfRoad);
                    spComplementoVia.setSelection(spinnerPosition);
                }
            }
        }
        if(baseView.datosAsociado != null && baseView.datosAsociado.getDetalleDireccion() != null &&
                !TextUtils.isEmpty(baseView.datosAsociado.getDetalleDireccion().getComplementoVia2())){
            for (ResponseFormatoDirecciones.ItemsDirecciones addOnsOfRoad : listAddOnsOfRoad) {
                if (addOnsOfRoad.getValue().equals(baseView.datosAsociado.getDetalleDireccion().getComplementoVia2())) {
                    int spinnerPosition = adapterComplementosVia.getPosition(addOnsOfRoad);
                    spComplementoVia2.setSelection(spinnerPosition);
                }
            }
        }
    }

    @Override
    public void showContentEditData(){
        contentEditData.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideContentEditData(){
        contentEditData.setVisibility(View.GONE);
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
        contentEditData.setVisibility(View.GONE);
        layoutCircularProgressBar.setVisibility(View.VISIBLE);
        circularProgressBar.setVisibility(View.GONE);
        textCircularProgressBar.setText("Ha ocurrido un error, inténtalo de nuevo ");
        buttonReferesh.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btnGuardar_actDatos_guardar)
    public void onClickGuardar(){
        if(validarDatosIngresados()){
            ResponseUbicaciones.Pais pais = (ResponseUbicaciones.Pais) spPais.getSelectedItem();
            ResponseUbicaciones.Departamento departamento = (ResponseUbicaciones.Departamento) spDepartamento.getSelectedItem();
            ResponseUbicaciones.Ciudad ciudad = (ResponseUbicaciones.Ciudad) spCiudad.getSelectedItem();
            //Detalle Direccion
            ResponseFormatoDirecciones.ItemsDirecciones tipoVia = (ResponseFormatoDirecciones.ItemsDirecciones) spTipoVia.getSelectedItem();
            String numeroVia = etNumeroVia.getText().toString();
            ResponseFormatoDirecciones.ItemsDirecciones letraVia1 = (ResponseFormatoDirecciones.ItemsDirecciones) spLetraVia.getSelectedItem();
            ResponseFormatoDirecciones.ItemsDirecciones complementoVia1 = (ResponseFormatoDirecciones.ItemsDirecciones) spComplementoVia.getSelectedItem();
            String numeroVia2 = etNumeroVia2.getText().toString();
            ResponseFormatoDirecciones.ItemsDirecciones letraVia2 = (ResponseFormatoDirecciones.ItemsDirecciones) spLetraVia2.getSelectedItem();
            ResponseFormatoDirecciones.ItemsDirecciones complementoVia2 = (ResponseFormatoDirecciones.ItemsDirecciones) spComplementoVia2.getSelectedItem();
            String numeroComplemento = etNumeroComplemento.getText().toString();
            String informacionAdicional = etIformacionAdicional.getText().toString();

            DatosAsociado.Direccion direccionDetalle = new DatosAsociado.Direccion(
                    tipoVia.getValue(),
                    numeroVia,
                    TextUtils.isEmpty(letraVia1.getValue()) ? "" : letraVia1.getValue(),
                    TextUtils.isEmpty(complementoVia1.getValue()) ? "" : complementoVia1.getValue(),
                    numeroVia2,
                    TextUtils.isEmpty(letraVia2.getValue()) ? "" : letraVia2.getValue(),
                    TextUtils.isEmpty(complementoVia2.getValue()) ? "" : complementoVia2.getValue(),
                    numeroComplemento,
                    informacionAdicional
            );
            baseView.datosAsociado = new DatosAsociado(
                    etNombre.getText().toString(),
                    tvDireccionCompleta.getText().toString(), //Direccion completa
                    etCelular.getText().toString(),
                    etCorreo.getText().toString(),
                    etBarrio.getText().toString(),
                    ciudad.getIdCiudad(),
                    ciudad.getNombreCiudad(),
                    departamento.getIdDepartamento(),
                    departamento.getNombreDepartamento(),
                    pais.getIdPais(),
                    pais.getNombrePais()
            );
            baseView.datosAsociado.setDetalleDireccion(direccionDetalle);
            baseView.isDatosEditados = true;
            baseView.basePresenter.loadFragmentUpdatePersonalData(IFragmentCoordinator.Pantalla.ActDatosValidateData);
        }else{
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.pop_up_message);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView titleMessage = (TextView) dialog.findViewById(R.id.lbl_title_message);
            titleMessage.setText(Html.fromHtml("<b>Nos faltó algo</b>"));
            TextView contentMessage = (TextView) dialog.findViewById(R.id.lbl_content_message);
            contentMessage.setText("Revisa la información que ingresaste y verifica que no te falte ningún dato");
            ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.button_close);
            buttonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }


    public boolean validarDatosIngresados(){
        boolean datosValidos = true;
        if(TextUtils.isEmpty(etNombre.getText())){
            etNombre.setError("Campo obligatorio");
            datosValidos = false;
        }

        if(TextUtils.isEmpty(etCelular.getText())){
            etCelular.setError("Campo obligatorio");
            etCelular.requestFocus();
            datosValidos = false;
        } else if(!context.validateCellPhone(etCelular.getText().toString())){
            etCelular.setError("Ingrese un número de celular válido");
            etCelular.requestFocus();
            datosValidos = false;
        }

        if(TextUtils.isEmpty(etCorreo.getText())){
            etCorreo.setError("Campo obligatorio");
            etCorreo.requestFocus();
            datosValidos = false;
        } else if(!ActivityBase.validateEmail(etCorreo.getText().toString())){
            etCorreo.setError("Ingrese un email válido");
            etCorreo.requestFocus();
            datosValidos = false;
        }

        ResponseUbicaciones.Departamento departamento = (ResponseUbicaciones.Departamento)spDepartamento.getSelectedItem();
        if(TextUtils.isEmpty(departamento.getIdDepartamento())){
            ((TextView)spDepartamento.getChildAt(0)).setError("Campo obligatorio");
            spDepartamento.requestFocus();
            datosValidos = false;
        }

        ResponseUbicaciones.Ciudad ciudad = (ResponseUbicaciones.Ciudad)spCiudad.getSelectedItem();
        if(TextUtils.isEmpty(ciudad.getIdCiudad())){
            ((TextView)spCiudad.getChildAt(0)).setError("Campo obligatorio");
            spCiudad.requestFocus();
            datosValidos = false;
        }

        if(TextUtils.isEmpty(etBarrio.getText())){
            etBarrio.setError("Campo obligatorio");
            etBarrio.requestFocus();
            datosValidos = false;
        }

        ResponseFormatoDirecciones.ItemsDirecciones tipoVia = (ResponseFormatoDirecciones.ItemsDirecciones)spTipoVia.getSelectedItem();
        if(TextUtils.isEmpty(tipoVia.getId())){
            ((TextView)spTipoVia.getChildAt(0)).setError("Campo obligatorio");
            spTipoVia.requestFocus();
            datosValidos = false;
        }

        if(TextUtils.isEmpty(etNumeroVia.getText())){
            etNumeroVia.setError("Campo obligatorio");
            etNumeroVia.requestFocus();
            datosValidos = false;
        }

        if(TextUtils.isEmpty(etNumeroVia2.getText())){
            etNumeroVia2.setError("Campo obligatorio");
            etNumeroVia2.requestFocus();
            datosValidos = false;
        }

        if(TextUtils.isEmpty(etNumeroComplemento.getText())){
            etNumeroComplemento.setError("Campo obligatorio");
            etNumeroComplemento.requestFocus();
            datosValidos = false;
        }

        return datosValidos;
    }

    private void obtenerDireccionCompleta() {
        String direccionCompleta = "";

        ResponseFormatoDirecciones.ItemsDirecciones tipoVia = (ResponseFormatoDirecciones.ItemsDirecciones)spTipoVia.getSelectedItem();
        direccionCompleta += tipoVia.getId().isEmpty() ? "" : tipoVia.getValue() + " ";

        String numeroVia = etNumeroVia.getText().toString();
        direccionCompleta += numeroVia.isEmpty() ? "" : numeroVia+" ";

        ResponseFormatoDirecciones.ItemsDirecciones letraVia = (ResponseFormatoDirecciones.ItemsDirecciones)spLetraVia.getSelectedItem();
        direccionCompleta += letraVia.getId().isEmpty() ? "" : letraVia.getValue()+" ";

        ResponseFormatoDirecciones.ItemsDirecciones compleVia = (ResponseFormatoDirecciones.ItemsDirecciones)spComplementoVia.getSelectedItem();
        direccionCompleta += compleVia.getId().isEmpty() ? "" : compleVia.getValue()+" ";

        String numeroVia2 = etNumeroVia2.getText().toString();
        direccionCompleta += numeroVia2.isEmpty() ? "# " : "# "+numeroVia2+" ";

        ResponseFormatoDirecciones.ItemsDirecciones letraVia2 = (ResponseFormatoDirecciones.ItemsDirecciones)spLetraVia2.getSelectedItem();
        direccionCompleta += letraVia2.getId().isEmpty() ? "" : letraVia2.getValue()+" ";

        ResponseFormatoDirecciones.ItemsDirecciones compleVia2 = (ResponseFormatoDirecciones.ItemsDirecciones)spComplementoVia2.getSelectedItem();
        direccionCompleta += compleVia2.getId().isEmpty() ? "" : compleVia2.getValue()+" ";

        String numeroComplemento = etNumeroComplemento.getText().toString();
        direccionCompleta += numeroComplemento.isEmpty() ? "" : numeroComplemento+" ";

        String informacionAdicional = etIformacionAdicional.getText().toString();
        direccionCompleta += informacionAdicional;

        tvDireccionCompleta.setText(direccionCompleta);
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
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_error);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView titleMessage = (TextView) dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText("Lo sentimos");
        TextView contentMessage = (TextView) dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                baseView.finish();
            }
        });
        dialog.show();
    }

    @Override
    public void showDataFetchError(String title, String message) {
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
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_error);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView titleMessage = (TextView) dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText(title);
        TextView contentMessage = (TextView) dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                baseView.finish();
            }
        });
        dialog.show();
    }

    @Override
    public void showExpiredToken(String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_closedsession);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button buttonClosedSession = (Button) dialog.findViewById(R.id.btnVolverAIngresar);
        buttonClosedSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                context.salir();
            }
        });
        dialog.show();

    }

}
