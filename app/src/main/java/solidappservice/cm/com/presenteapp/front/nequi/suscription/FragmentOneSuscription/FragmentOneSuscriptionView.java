package solidappservice.cm.com.presenteapp.front.nequi.suscription.FragmentOneSuscription;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.DatosAsociado;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.login.Response.Usuario;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.actualizaciondatos.ActivityUpdatePersonalData.ActivityUpdatePersonalDataView;
import solidappservice.cm.com.presenteapp.front.nequi.suscription.ActivitySuscription.ActivitySuscriptionView;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.popups.PopUp;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.helpers.DialogHelpers;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;


public class FragmentOneSuscriptionView extends Fragment implements FragmentOneSuscriptionContract.View{

    private FragmentOneSuscriptionPresenter presenterOneSuscripcion;
    private ActivitySuscriptionView baseView;
    private ActivityBase context;
    private GlobalState state;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.et_celularSusc)
    EditText et_celular;
    @BindView(R.id.modificarNumeroCelular)
    TextView modificarNumeroCelular;


    @BindView(R.id.btnSiguiente)
    Button buttonSiguiente;
    @BindView(R.id.circular_progress_bar)
    ProgressBar circularProgressBar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de primer paso suscricion nequi");
        firebaseAnalytics.logEvent("pantalla_one_suscripcion", params);
        View view = inflater.inflate(R.layout.fragment_nequi_suscription_one, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenterOneSuscripcion = new FragmentOneSuscriptionPresenter(this, new FragmentOneSuscriptionModel());
        baseView = (ActivitySuscriptionView) getActivity();
        context = (ActivityBase) getActivity();
        state = context.getState();
        if (baseView != null) {
            baseView.img_nequipresente.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null || state.getUsuario() == null){
            context.salir();
            return;
        } else {
            processPersonalData();
        }
    }

    @OnClick(R.id.btnSiguiente)
    public void onClickSiguiente(){
        if(baseView != null) {
            baseView.basePresenter.loadFragmentSuscripcionNequi(IFragmentCoordinator.Pantalla.SuscNequiSecond);
        }
    }

    @OnClick(R.id.modificarNumeroCelular)
    public void onClickModificarCelular(){
        try{
            Usuario usuario = state.getUsuario();
            Intent intent = new Intent(context, ActivityUpdatePersonalDataView.class);
            intent.putExtra("actualizaPrimeraVez", usuario.getDatosActualizados().getActualizaPrimeraVez());
            intent.putExtra("datosActualizados", usuario.getDatosActualizados().getTieneDatosActualizados());
            startActivityForResult(intent, GlobalState.UPDATE_PERSONAL_DATA);
        }catch(Exception ex){
            showDataFetchError("Lo sentimos", "");
            Log.d("Error", ex.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GlobalState.UPDATE_PERSONAL_DATA){
            baseView.datosAsociado = null;
            processPersonalData();
        }
    }

    @Override
    public void processPersonalData() {
        if (baseView != null) {
            if (baseView.datosAsociado != null && baseView.datosAsociado.getCelular() != null) {
                et_celular.setText(baseView.datosAsociado.getCelular());
                showSuscriptionPhone();
                enabledButtonNext();
            } else {
                fetchPersonalData();            }
        } else {
            baseView.basePresenter.loadFragmentSuscripcionNequi(IFragmentCoordinator.Pantalla.SuscNequiInicio);
        }
    }

    @Override
    public void fetchPersonalData() {
        GlobalState state = context.getState();
        Encripcion encripcion = Encripcion.getInstance();
        presenterOneSuscripcion.fetchPersonalData(new BaseRequest(
                encripcion.encriptar(state.getUsuario().getCedula()),
                state.getUsuario().getToken()
        ));
    }

    @Override
    public void showPersonalData(DatosAsociado datos) {
        if(baseView != null){
            if(datos != null){
                baseView.datosAsociado = datos;
                et_celular.setText(datos.getCelular());
            }
        } else{
            baseView.basePresenter.loadFragmentSuscripcionNequi(IFragmentCoordinator.Pantalla.SuscNequiInicio);
        }
    }

    @Override
    public void enabledButtonNext() {
        buttonSiguiente.setEnabled(true);
    }

    @Override
    public void disabledButtonNext(){
        buttonSiguiente.setEnabled(false);
    }

    @Override
    public void showSuscriptionPhone() {
        et_celular.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSuscriptionPhone(){
        et_celular.setVisibility(View.GONE);
    }

    @Override
    public void showCircularProgressBar() {
        circularProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCircularProgressBar() {
        circularProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorTimeOut() {
        String message = DialogHelpers.validateMessageDialog(6, state);
        final Dialog dialog = DialogHelpers.getDialog(context, R.layout.pop_up_error);
        TextView titleMessage = dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText("Lo sentimos");
        TextView contentMessage = dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        ImageButton buttonClose = dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void showDataFetchError(String title, String message){
        if(TextUtils.isEmpty(message)){
            message = DialogHelpers.validateMessageDialog(7, state);
        }
        final Dialog dialog = DialogHelpers.getDialog(context, R.layout.pop_up_error);
        TextView titleMessage = dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText(title);
        TextView contentMessage = dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        ImageButton buttonClose = dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void showExpiredToken(String message) {
        try(PopUp popUp = new PopUp()) {
            popUp.showExpiredToken(message, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

