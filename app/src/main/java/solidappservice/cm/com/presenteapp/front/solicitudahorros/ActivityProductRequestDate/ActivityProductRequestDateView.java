package solidappservice.cm.com.presenteapp.front.solicitudahorros.ActivityProductRequestDate;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.front.base.main.ActivityMainView;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 28/11/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 17/09/2021.
 */
public class ActivityProductRequestDateView extends ActivityBase implements ActivityProductRequestDateContract.View{

    private ActivityProductRequestDatePresenter presenter;
    private ActivityBase context;
    private GlobalState state;

    @BindView(R.id.spinnerDia)
    Spinner spinnerDia;
    @BindView(R.id.spinnerAnio)
    Spinner spinnerAnio;
    @BindView(R.id.spinnerMes)
    Spinner spinnerMes;
    @BindView(R.id.btnAceptar)
    Button btnAceptar;
    @BindView(R.id.button_close)
    ImageButton buttonClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savingssolicity_dateproductssolicity);
        ButterKnife.bind(this);
        setControls();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void setControls() {
        if (android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        presenter = new ActivityProductRequestDatePresenter(this);
        context = this;
        state = (GlobalState) getApplicationContext();
        spinnerDia.setEnabled(false);
        spinnerMes.setEnabled(false);
        showYears();
    }

    @OnClick(R.id.button_close)
    public void onClickButtonClose(){
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalState state = (GlobalState) getApplicationContext();
        if(state!= null && !state.validarEstado()){
            Intent intent = new Intent(this, ActivityMainView.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        GlobalState state = (GlobalState) getApplicationContext();
        state.setFechaSeleccionada(null);
        setResult(RESULT_CANCELED);
        finish();
    }

    @OnItemSelected(R.id.spinnerAnio)
    public void onItemSelectedAnio(AdapterView<?> parent, View view, int position, long id) {
        if (spinnerAnio.getSelectedItem() != null && !TextUtils.isEmpty(spinnerAnio.getSelectedItem().toString())) {
            showMonths(position);
            spinnerMes.setEnabled(true);
            spinnerDia.setEnabled(true);

        }
    }

    @OnItemSelected(value = R.id.spinnerAnio, callback = OnItemSelected.Callback.NOTHING_SELECTED)
    public void onNothingSelected(AdapterView<?> parent) {
        spinnerMes.setEnabled(false);
        spinnerDia.setEnabled(false);
    }

    @OnItemSelected(R.id.spinnerMes)
    public void onItemSelectedMes(AdapterView<?> parent, View view, int position, long id) {
        if (spinnerMes.getSelectedItem() != null && !TextUtils.isEmpty(spinnerMes.getSelectedItem().toString())) {
            spinnerDia.setEnabled(true);

            showDays(selectMonthIndex(spinnerMes.getSelectedItem().toString()),
                    Integer.parseInt(spinnerAnio.getSelectedItem().toString()));
        }
    }

    @OnClick(R.id.btnAceptar)
    public void onClick(View v) {
        Object anio = spinnerAnio.getSelectedItem();
        Object mes = spinnerMes.getSelectedItem();
        Object dia = spinnerDia.getSelectedItem();

        if (anio == null || TextUtils.isEmpty(anio.toString())) {
            return;
        }

        if (mes == null || TextUtils.isEmpty(mes.toString())) {
            return;
        }

        if (dia == null || TextUtils.isEmpty(dia.toString())) {
            return;
        }

        final Calendar c = Calendar.getInstance();
        int yearActual = c.get(Calendar.YEAR);
        int monthActual = c.get(Calendar.MONTH) + 1;
        int dayActual = c.get(Calendar.DAY_OF_MONTH);

        int int_mes = selectMonthIndex(mes.toString()) + 1;

        if(Integer.parseInt(anio.toString()) < yearActual){
            showDataFetchError("Datos erróneos", "Por favor ingrese una fecha válida");
            return;
        }

        if(int_mes < monthActual && Integer.parseInt(anio.toString()) == yearActual){
            showDataFetchError("Datos erróneos", "Por favor ingrese una fecha válida");
            return;
        }

        if(monthActual == int_mes && Integer.parseInt(dia.toString()) < dayActual
                && Integer.parseInt(anio.toString()) == yearActual){
            showDataFetchError("Datos erróneos", "Por favor ingrese una fecha válida");
            return;
        }

        final Calendar c_validacion = Calendar.getInstance();
        c_validacion.set(Calendar.YEAR, Integer.parseInt(anio.toString()));
        c_validacion.set(Calendar.MONTH, selectMonthIndex(mes.toString()));
        c_validacion.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dia.toString()));

        c.add(Calendar.YEAR, 3);

        if(c_validacion.compareTo(c) > 0){
            showDataFetchError("Datos erróneos", "Por favor ingrese una fecha válida, no superior a tres años desde la fecha actual");
            return;
        }

        String fecha = (dia + "/" + (int_mes <10 ? "0"+int_mes:int_mes) + "/" + anio);
        state.setFechaSeleccionada(fecha);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void showYears(){
        final Calendar c = Calendar.getInstance();
        int anioActual = c.get(Calendar.YEAR);
        int actualMonth = c.get(Calendar.MONTH);
        ArrayList<String> anios = new ArrayList<>();
        for(int i = anioActual; i <= (anioActual + 3); i++){
            anios.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item_spinner, anios);
        spinnerAnio.setAdapter(adapter);

        String[] meses = getResources().getStringArray(R.array.mes_solicitud_producto);
        List<String> mesesString = new ArrayList<>();

        actualMonth = actualMonth + 1;

        for (int i = actualMonth; i < meses.length; i++) {
            mesesString.add(meses[i]);
        }

        adapter = new ArrayAdapter<>(this, R.layout.list_item_spinner, mesesString);
        spinnerMes.setAdapter(adapter);
    }

    @Override
    public void showMonths(int selectedYear){
        final Calendar c = Calendar.getInstance();
        int anioActual = c.get(Calendar.YEAR);
        int actualMonth = c.get(Calendar.MONTH);

        ArrayList<String> anios = new ArrayList<>();
        for(int i = anioActual; i <= (anioActual + 3); i++){
            anios.add(String.valueOf(i));
        }
        selectedYear = Integer.parseInt(anios.get(selectedYear));

        String[] meses = getResources().getStringArray(R.array.mes_solicitud_producto);
        List<String> mesesString = new ArrayList<>();

        actualMonth = actualMonth + 1;
        if (anioActual != selectedYear){
            actualMonth = 0;
        }

        for (int i = actualMonth; i < meses.length; i++) {
            mesesString.add(meses[i]);
        }
        ArrayAdapter<String> adapter  = new ArrayAdapter<>(this, R.layout.list_item_spinner, mesesString);
        spinnerMes.setAdapter(adapter);
    }

    @Override
    public void showDays(int month, int year){
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);
        int ultimoDia = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        ArrayList<String> dias = new ArrayList<>();
        dias.add("15");
        dias.add(String.valueOf(ultimoDia));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item_spinner, dias);
        spinnerDia.setAdapter(arrayAdapter);
    }

    @Override
    public int selectMonthIndex(String mes){
        switch (mes){
            case "Enero":
                return 0;
            case "Febrero":
                return 1;
            case "Marzo":
                return 2;
            case "Abril":
                return 3;
            case "Mayo":
                return 4;
            case "Junio":
                return 5;
            case "Julio":
                return 6;
            case "Agosto":
                return 7;
            case "Septiembre":
                return 8;
            case "Octubre":
                return 9;
            case "Noviembre":
                return 10;
            case "Diciembre":
                return 11;
            default:
                return 0;
        }
    }

    @Override
    public void showDataFetchError(String title, String message){
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
            }
        });
        dialog.show();
    }




//    private void mostrarDias(int mes, int year){
//        final Calendar c = Calendar.getInstance();
//        c.set(Calendar.DAY_OF_MONTH, 1);
//        c.set(Calendar.MONTH, mes);
//        c.set(Calendar.YEAR, year);
//        int ultimoDia = c.getActualMaximum(Calendar.DAY_OF_MONTH);
//        ArrayList<String> dias = new ArrayList<>();
//        dias.add("15");
//        dias.add(String.valueOf(ultimoDia));
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item_spinner, dias);
//        spinnerDia.setAdapter(arrayAdapter);
//    }
//
//    private void mostrarAnios(){
//        final Calendar c = Calendar.getInstance();
//        int anioActual = c.get(Calendar.YEAR);
//        int actualMonth = c.get(Calendar.MONTH);
//        ArrayList<String> anios = new ArrayList<>();
//        for(int i = anioActual; i <= (anioActual + 3); i++){
//            anios.add(String.valueOf(i));
//        }
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item_spinner, anios);
//        spinnerAnio.setAdapter(adapter);
//
//        String[] meses = getResources().getStringArray(R.array.mes_solicitud_producto);
//        List<String> mesesString = new ArrayList<>();
//
//        actualMonth = actualMonth + 1;
//
//        for (int i = actualMonth; i < meses.length; i++) {
//            mesesString.add(meses[i]);
//        }
//
//        adapter = new ArrayAdapter<>(this, R.layout.list_item_spinner, mesesString);
//        spinnerMes.setAdapter(adapter);
//    }
//
//    private void mostrarMeses(int selectedYear){
//        final Calendar c = Calendar.getInstance();
//        int anioActual = c.get(Calendar.YEAR);
//        int actualMonth = c.get(Calendar.MONTH);
//
//        ArrayList<String> anios = new ArrayList<>();
//        for(int i = anioActual; i <= (anioActual + 3); i++){
//            anios.add(String.valueOf(i));
//        }
//        selectedYear = Integer.parseInt(anios.get(selectedYear));
//
//        String[] meses = getResources().getStringArray(R.array.mes_solicitud_producto);
//        List<String> mesesString = new ArrayList<>();
//
//        actualMonth = actualMonth + 1;
//        if (anioActual != selectedYear){
//            actualMonth = 0;
//        }
//
//        for (int i = actualMonth; i < meses.length; i++) {
//            mesesString.add(meses[i]);
//        }
//
//        ArrayAdapter<String> adapter  = new ArrayAdapter<>(this, R.layout.list_item_spinner, mesesString);
//        spinnerMes.setAdapter(adapter);
//
//    }
//
//
//
//    private int selectMesIndex(String mes){
//        switch (mes){
//            case "Enero":
//                return 0;
//            case "Febrero":
//                return 1;
//            case "Marzo":
//                return 2;
//            case "Abril":
//                return 3;
//            case "Mayo":
//                return 4;
//            case "Junio":
//                return 5;
//            case "Julio":
//                return 6;
//            case "Agosto":
//                return 7;
//            case "Septiembre":
//                return 8;
//            case "Octubre":
//                return 9;
//            case "Noviembre":
//                return 10;
//            case "Diciembre":
//                return 11;
//            default:
//                return 0;
//        }
//    }


}
