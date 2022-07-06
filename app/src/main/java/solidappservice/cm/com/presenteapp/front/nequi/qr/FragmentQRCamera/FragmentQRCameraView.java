package solidappservice.cm.com.presenteapp.front.nequi.qr.FragmentQRCamera;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.nequi.qr.ActivityCameraQR.ActivityQRCameraView;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

public class FragmentQRCameraView extends Fragment implements FragmentQRCameraContract.View{

    private FragmentQRCameraPresenter presenter;
    private ActivityQRCameraView baseView;
    private ActivityBase context;
    private GlobalState state;
    private FirebaseAnalytics firebaseAnalytics;
    private Dialog pd;
    private boolean isLightOn = false;

    private BeepManager beepManager;

    @BindView(R.id.barcode_scanner)
    DecoratedBarcodeView barcodeView;
    @BindView(R.id.barcodePreview)
    ImageView imageView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla camára QR nequi");
        firebaseAnalytics.logEvent("pantalla_qr_camera", params);
        View view = inflater.inflate(R.layout.fragment_nequi_qr_camera, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentQRCameraPresenter(this, new FragmentQRCameraModel());
        context = (ActivityBase)getActivity();
        baseView = (ActivityQRCameraView)getActivity();
        state = context.getState();
        baseView.barcodeView = barcodeView;
        configureScannerQR();
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null || result.getText().equals(baseView.textQR)) {
                return;
            }
            try{
                baseView.textQR = result.getText();
                state.setTextQR(baseView.textQR);
                beepManager.playBeepSoundAndVibrate();
//                imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
                baseView.scannerCodeQR(state.getTextQR());
            }catch (Exception ex){
                baseView.showDialogErrorScannerQR();
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GlobalState.QR_DETAIL){
            baseView.finish();
        }
    }

    @Override
    public void configureScannerQR(){
        try{
            Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
            barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
            barcodeView.initializeFromIntent(context.getIntent());
            barcodeView.setTorchListener(baseView.torchListener);
            barcodeView.decodeContinuous(callback);
            barcodeView.setStatusText("");
            beepManager = new BeepManager(context);
        }catch(Exception ex){
            baseView.showDialogErrorQR();
        }
    }

}
