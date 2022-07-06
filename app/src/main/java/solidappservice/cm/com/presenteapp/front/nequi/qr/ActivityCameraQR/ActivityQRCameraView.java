package solidappservice.cm.com.presenteapp.front.nequi.qr.ActivityCameraQR;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestGetDataCommerceQR;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseDataCommerceQR;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.login.FragmentLogin.FragmentLoginView;
import solidappservice.cm.com.presenteapp.front.nequi.qr.ActivityQRDetail.ActivityQRDetailView;
import solidappservice.cm.com.presenteapp.front.nequi.qr.FragmentQRCamera.FragmentQRCameraView;
import solidappservice.cm.com.presenteapp.front.popups.PopUp;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static solidappservice.cm.com.presenteapp.tools.constants.Constans.ERROR_CONTACTA_PRESENTE;

public class ActivityQRCameraView extends ActivityBase implements ActivityQRCameraContract.View{
    private ActivityQRCameraPresenter presenter;
    private ActivityQRCameraView qrView;
    private ActivityBase context;
    private GlobalState state;
    private Dialog pd;
    private CameraManager cameraManager;
    private boolean isLightOn = false;
    public DecoratedBarcodeView.TorchListener torchListener;
    public DecoratedBarcodeView barcodeView;
    public String textQR;

    @BindView(R.id.containerQr)
    FrameLayout frameLayout;
    @BindView(R.id.btn_gallery)
    ImageButton btnGallery;
    @BindView(R.id.btn_flashlight)
    ImageButton btnFlashLight;
    @BindView(R.id.barcodePreview)
    ImageView barcodePreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nequi_qr);
        ButterKnife.bind(this);
        try {
            setControls();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setControls() throws Exception {
        presenter = new ActivityQRCameraPresenter(this, new ActivityQRCameraModel());
        context = this;
        state = context.getState();
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            validatePermissionsCamera();
        }else{
            showDataFetchError("Lo sentimos", "Este dispositivo no tiene cámara.");
        }
        torchListener = new DecoratedBarcodeView.TorchListener() {
            @Override
            public void onTorchOn() {
                isLightOn = true;
            }

            @Override
            public void onTorchOff() {
                isLightOn = false;
            }
        };
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.btn_gallery)
    public void onClickGallery(View v){
        validatePermissionsReadGallery();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick(R.id.btn_flashlight)
    public void onClickFlashLight(View v){
        try{
            if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
                if (cameraManager == null){
                    cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                }

                if (!isLightOn) {
                    btnFlashLight.setBackgroundResource(R.drawable.background_circle_gray);
                    barcodeView.setTorchOn();
                } else {
                    btnFlashLight.setBackground(null);
                    barcodeView.setTorchOff();
                }
            }else{
                btnFlashLight.setEnabled(false);
                showDialogError("Lo sentimos", "Este dispositivo no tiene flash.");
            }
        }catch (Exception ex){
            showDialogError("Lo sentimos", "");
        }
    }

    @OnClick(R.id.btn_back)
    public void onClickBack(View v) {
        onBackPressed();
    }

    @Override
    public void validatePermissionsCamera(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)){
                showFragmentNequi(IFragmentCoordinator.Pantalla.NequiQRCamera);
                return;
            }
            if((shouldShowRequestPermissionRationale((CAMERA)))){
                showDialogPermissions(CAMERA, GlobalState.PERMISSION_CAMERA);
            }else{
                requestPermissions(new String[]{CAMERA}, GlobalState.PERMISSION_CAMERA);
            }
        }
    }

    @Override
    public void validatePermissionsReadGallery(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if((checkSelfPermission(READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)){
                Intent pickIntent = new Intent(Intent.ACTION_PICK);
                pickIntent.setDataAndType( android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(pickIntent, GlobalState.GALLERY);
                return;
            }
            if((shouldShowRequestPermissionRationale((READ_EXTERNAL_STORAGE)))){
                showDialogPermissions(READ_EXTERNAL_STORAGE, GlobalState.PERMISSION_GALLERY);
            }else{
                requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, GlobalState.PERMISSION_GALLERY);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void showDialogPermissions(String typeOfPermission, int requestCode){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_confirm);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView titleMessage = (TextView) dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText("Permisos desactivados");
        TextView contentMessage = (TextView) dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText("Debes aceptar los permisos para acceder a esta función.");
        ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.buttonClose);
        buttonClose.setOnClickListener(view -> dialog.dismiss());
        Button buttonAceptar = (Button) dialog.findViewById(R.id.btnAceptar);
        buttonAceptar.setOnClickListener(view -> {
            requestPermissions(new String[]{typeOfPermission}, requestCode);
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == GlobalState.PERMISSION_CAMERA){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                showFragmentNequi(IFragmentCoordinator.Pantalla.NequiQRCamera);
            }else{
                requestPermits();
            }
        }
        if(requestCode == GlobalState.PERMISSION_GALLERY){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent pickIntent = new Intent(Intent.ACTION_PICK);
                pickIntent.setDataAndType( android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(pickIntent,  GlobalState.GALLERY);
            }else{
                requestPermits();
            }
        }
    }

    @Override
    public void requestPermits(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_confirm);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView titleMessage = (TextView) dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText("Permisos desactivados");
        TextView contentMessage = (TextView) dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText("¿Desea configurar los permisos de forma manual?");
        ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.buttonClose);
        buttonClose.setOnClickListener(view -> {
            finish();
            context.makeSToast("Los permisos no fueron aceptados");
            dialog.dismiss();
        });
        Button buttonAceptar = (Button) dialog.findViewById(R.id.btnAceptar);
        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(),null);
                intent.setData(uri);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GlobalState.GALLERY){
            if(data == null || data.getData()==null) {
                return;
            }
            Uri uri = data.getData();
            try{
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (bitmap == null){
                    return;
                }else{
                    scanQRImage(bitmap);
                }
            }catch (FileNotFoundException e){
                Log.e("TAG", "can not open file" + uri.toString(), e);
            }
        }
    }

    @Override
    public void scanQRImage(Bitmap bMap) {
        int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];
        bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());
        LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Reader reader = new MultiFormatReader();
        try {
            Result result = reader.decode(bitmap);
            textQR = result.getText();
            state.setTextQR(textQR);
            scannerCodeQR(textQR);
        }
        catch (Exception e) {
            Log.e("QrTest", "Error decoding barcode", e);
        }
    }

    @Override
    public void scannerCodeQR(String text) {
        try{
            presenter.fetchDataCommerceQR(new RequestGetDataCommerceQR(
                    Encripcion.getInstance().encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken(),
                    text
            ));
        }catch (Exception ex){
            showDialogErrorScannerQR();
        }
    }

    @Override
    public void resultScannerCodeQR(ResponseDataCommerceQR.DataReceivedQR dataCommerceQR){
        state.setDataCommerceQR(dataCommerceQR);
        Intent intent = new Intent(context, ActivityQRDetailView.class);
        startActivityForResult(intent, GlobalState.QR_DETAIL);
    }

    @Override
    public void showDialogErrorScannerQR(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_nequi_qr_error_scanner);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        Button buttonVolverAIntentar = (Button)dialog.findViewById(R.id.btnVolverAIntentar);
        buttonVolverAIntentar.setOnClickListener(v -> {
            textQR = "";
            state.setTextQR("");
            dialog.dismiss();
        });
        ImageButton buttonClose = (ImageButton)dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(v -> {
            textQR = "";
            state.setTextQR("");
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void showDialogErrorQR(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_nequi_qr_error);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        Button buttonCerrar = (Button)dialog.findViewById(R.id.btn_cerrar);
        buttonCerrar.setOnClickListener(v -> {
            state.setTextQR("");
            finish();
            dialog.dismiss();
        });
        ImageButton buttonClose = (ImageButton)dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(v -> {
            state.setTextQR("");
            finish();
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void showProgressDialog(String message) {
        pd = new Dialog(context);
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.setCanceledOnTouchOutside(false);
        pd.setContentView(R.layout.pop_up_loading);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView contentMessage = (TextView) pd.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        pd.show();
    }

    @Override
    public void hideProgressDialog(){
        pd.dismiss();
    }

    @Override
    public void showFragmentNequi(IFragmentCoordinator.Pantalla pantalla) {
        GlobalState state = getState();
        Fragment fragment;
        switch (pantalla) {
            case NequiQRCamera:
                fragment = new FragmentQRCameraView();
                break;
            default:
                fragment = new FragmentLoginView();
                break;
        }
        if (state != null && state.getFragmentActual() != null) {
            state.setFragmentAnterior(state.getFragmentActual());
        }
        hideKeyBoard();
        state.setFragmentActual(pantalla);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.containerQr, fragment).commit();
    }

    @Override
    public void showDialogError(String title, String message) {
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
        buttonClose.setOnClickListener(view -> {
            textQR = "";
            state.setTextQR("");
            dialog.dismiss();
        });
        dialog.show();
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
                state.setTextQR("");
                finish();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void showDataFetchError(String title, String message) throws Exception {
        if(TextUtils.isEmpty(message)){
            message = ERROR_CONTACTA_PRESENTE;
            if(state != null && state.getMensajesRespuesta() != null && state.getMensajesRespuesta().size()>0){
                for(ResponseMensajesRespuesta rm : state.getMensajesRespuesta()){
                    if(rm.getIdMensaje() == 7){
                        message = rm.getMensaje();
                    }
                }
            }
        }
        try(PopUp popUp = new PopUp()) {
            popUp.showError(title,message, context, state);
        }
        /*final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.pop_up_error);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView titleMessage =  dialog.findViewById(R.id.lbl_title_message);
        titleMessage.setText(title);
        TextView contentMessage =  dialog.findViewById(R.id.lbl_content_message);
        contentMessage.setText(message);
        ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(view -> {
            state.setTextQR("");
            finish();
            dialog.dismiss();
        });
        dialog.show();*/
    }

    @Override
    public void showExpiredToken(String message) throws Exception {
        try(PopUp popUp = new PopUp()) {
            popUp.showExpiredToken(message, context);
        }
    }
}
