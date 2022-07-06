package solidappservice.cm.com.presenteapp.front.splash.ActivitySplash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huawei.hms.api.HuaweiApiAvailability;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.mensajesbanner.ResponseMensajesBanner;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.entities.tyc.response.ReponseTyC;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.base.main.ActivityMainView;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 02/12/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 19/09/2021
 */
public class ActivitySplashView extends ActivityBase implements ActivitySplashContract.View{

    private ActivitySplashPresenter presenter;
    private GlobalState state;
    private ActivityBase context;
    private static final int SPLASH_DELAY = 10000;
    public MyReceiver myreceiver;

    @BindView(R.id.circular_progress_bar)
    ProgressBar ciruclarProgressBar;
    @BindView(R.id.text_circular_progressbar)
    TextView textCiruclarProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        setControls();
    }

    @Override
    protected void setControls() {
        presenter = new ActivitySplashPresenter(this, new ActivitySplashModel());
        context = this;
        state = (GlobalState) getApplicationContext();
        if(isHmsAvailable(this)){
            myreceiver = new MyReceiver();
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            filter.addAction("com.huawei.codelabpush.ON_NEW_TOKEN");
            this.registerReceiver(myreceiver, filter);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        initialStateVariables();
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.huawei.codelabpush.ON_NEW_TOKEN".equals(intent.getAction())) {
                String token = intent.getStringExtra("token");
                Log.d("token", "token:"+token);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(state.isHmsSystem() && myreceiver != null){
            unregisterReceiver(myreceiver);
        }
    }

    @Override
    public void initialStateVariables(){
        if (NetworkHelper.isConnectionAvailable(context)) {
            boolean isHmsSystem = isHmsAvailable(context);
            loadIsHmsAvailable(isHmsSystem);
            fetchResponseMessages();
        }else{
            startApp();
        }
    }

    @Override
    public void startApp() {
        Intent mainIntent = new Intent().setClass(ActivitySplashView.this, ActivityMainView.class);
        startActivity(mainIntent);
        finish();
    }

    @Override
    public void fetchResponseMessages(){
        try{
            presenter.fetchResponseMessages();
        }catch (Exception ex){
            fetchBannerMessages();
        }
    }

    @Override
    public void loadResponseMessages(List<ResponseMensajesRespuesta> responseMessages){
        if (responseMessages != null && state != null) {
            state.setMensajesRespuesta(responseMessages);
        }
    }

    @Override
    public void fetchBannerMessages(){
        try{
            presenter.fetchBannerMessages();
        }catch (Exception ex){
        }
    }

    @Override
    public void loadBannerMessages(List<ResponseMensajesBanner> bannerMessages){
        if (bannerMessages != null && state != null) {
            state.setMensajes(bannerMessages);
        }
    }

    @Override
    public void fetchLoginImage(){
        try{
            presenter.fetchLoginImage();
        }catch (Exception ex){
        }
    }

    @Override
    public void loadLoginImage(String loginImage){
        if(!TextUtils.isEmpty(loginImage)){
            new DownloadImageLoginTask(loginImage).execute();
        }
    }

    @Override
    public void fetchAppVersion(){
        try{
            presenter.fetchAppVersion();
        }catch (Exception ex){
        }
    }

    @Override
    public void loadAppVersion(String appVersion){
        if (appVersion != null && state != null) {
            state.setCurrentVersion(appVersion);
        }
    }

    @Override
    public void fetchTermsAndConditions(){
        try{
            presenter.fetchTermsAndConditions();
        }catch (Exception ex){

        }
    }

    @Override
    public void loadTermsAndConditions(ReponseTyC termsAndConditions){
        if (termsAndConditions != null && state != null) {
            state.setTerminos(termsAndConditions);
        }
    }

    @Override
    public void fetchButtonStatePaymentQR(){
        try{
            presenter.fetchButtonStatePaymentQR();
        }catch (Exception ex){
            state.setActiveButtonPaymentQR(false);
        }
    }

    @Override
    public void isActiveButtonPaymentQR(boolean isActiveSuscription){
        state.setActiveButtonPaymentQR(isActiveSuscription);
    }

    @Override
    public void fetchButtonStatePaymentDispersiones(){
        try{
            presenter.fetchButtonStatePaymentDispersiones();
        }catch (Exception ex){
            state.setActiveButtonPaymentDispersiones(false);
        }
    }

    @Override
    public void isActiveButtonPaymentDispersiones(boolean isActiveButtonDispersion){
        state.setActiveButtonPaymentDispersiones(isActiveButtonDispersion);
    }

    @Override
    public void fetchStateButtonPaymentSuscriptions(){
        try{
            presenter.fetchStateButtonPaymentSuscriptions();
        }catch (Exception ex){
            state.setActiveButtonPaymentSuscriptions(false);
        }
    }

    @Override
    public void isActiveButtonPaymentSuscriptions(boolean isActiveButtonSuscriptions){
        state.setActiveButtonPaymentSuscriptions(isActiveButtonSuscriptions);
    }

    @Override
    public void fetchStateNequiBalance(){
        try{
            presenter.fetchStateNequiBalance();
        }catch (Exception ex){
            state.setActiveStateNequiBalance(false);
        }
    }

    @Override
    public void isActiveStateNequiBalance(boolean isActiveNequiBalance){
        state.setActiveStateNequiBalance(isActiveNequiBalance);
    }

    @Override
    public void fetchStateSuscriptions(){
        try{
            presenter.fetchStateSuscriptions();
        }catch (Exception ex){
            state.setActiveStateSuscriptions(false);
        }
    }

    @Override
    public void isActiveStateSuscriptions(boolean isActiveButtonQR){
        state.setActiveStateSuscriptions(isActiveButtonQR);
    }


    public static boolean isHmsAvailable(Context context) {
        boolean isAvailable = false;
        if (null != context) {
            int result = HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(context);
            isAvailable = (com.huawei.hms.api.ConnectionResult.SUCCESS == result);
        }
        return isAvailable;
    }

    @Override
    public void loadIsHmsAvailable(boolean isHmsSystem){
        if (state != null) {
            state.setHmsSystem(isHmsSystem);
        }
    }

    public class DownloadImageLoginTask extends AsyncTask<String, Void, Bitmap> {
        private String imageUrl;
        public DownloadImageLoginTask(String imageUrl) {
            this.imageUrl = imageUrl;
        }
        protected Bitmap doInBackground(String... urls) {
            Bitmap bitmap = null;
            try {
                bitmap = drawableFromUrl(this.imageUrl);
            } catch (IOException e) {
            }
            return bitmap;
        }
        protected void onPostExecute(Bitmap result) {
            state.setBitmapImgBanner(result);
        }
    }

    @Override
    public Bitmap drawableFromUrl(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection)new java.net.URL(url) .openConnection();
        connection.setRequestProperty("User-agent","Mozilla/4.0");
        connection.connect();
        InputStream input = connection.getInputStream();
        return BitmapFactory.decodeStream(input);
    }


    @Override
    public void showCircularProgressBar() {
        ciruclarProgressBar.setVisibility(View.VISIBLE);
        textCiruclarProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCircularProgressBar() {
        ciruclarProgressBar.setVisibility(View.GONE);
        textCiruclarProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void tempThread(int delay){
        Thread splashThread = new Thread(){
            int wait = 0;
            @Override
            public void run() {
                try {
                    super.run();
                    while(wait < delay){
                        sleep(100);
                        wait += 100;
                    }
                } catch (Exception e) {
                }
            }
        };
        splashThread.start();
    }
}