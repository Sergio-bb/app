package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityFrequentQuestions;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponsePreguntasFrecuente;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 01/02/17.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 20/09/2021
 */
public class ActivityFrequentQuestionsView extends ActivityBase implements ActivityFrequentQuestionsContract.View{

    private ActivityFrequentQuestionsPresenter presenter;
    private ActivityBase context;
    private GlobalState state;

    @BindView(R.id.btn_back)
    ImageButton btnBack;

    @BindView(R.id.wvPreguntasFrecuentes)
    WebView webView;

    @BindView(R.id.layout_circular_progress_bar)
    LinearLayout layoutCircularProgressBar;
    @BindView(R.id.circular_progress_bar)
    ProgressBar circularProgressBar;
    @BindView(R.id.text_circular_progress_Bar)
    TextView textCircularProgressBar;
    @BindView(R.id.imageReferesh)
    ImageView buttonReferesh;
    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequency_questions);
        ButterKnife.bind(this);
        setControls();
    }

    @Override
    protected void setControls() {
        presenter = new ActivityFrequentQuestionsPresenter(this, new ActivityFrequentQuestionsModel());
        context = this;
        state = context.getState();
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null){
            context.salir();
        }else {
            fetchFrequentQuestions();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.btn_back)
    public void onClick(View v) {
        onBackPressed();
    }

    @OnClick(R.id.imageReferesh)
    public void onClickRefresh(){
        state.setPreguntaFrecuentes(null);
        fetchFrequentQuestions();
    }

    @Override
    public void fetchFrequentQuestions(){
        try{
            if (state.getPreguntaFrecuentes() !=null && state.getPreguntaFrecuentes().size()>0) {
                hideCircularProgressBar();
                showSectionFrequentQuestions();
                showFrequentQuestions(state.getPreguntaFrecuentes());
            }else{
                presenter.fetchFrequentQuestions();
            }
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "Se ha producido un error, inténtalo nuevamente en unos minutos.");
            showErrorWithRefresh();
        }
    }

    @Override
    public void showFrequentQuestions(List<ResponsePreguntasFrecuente> frequentQuestions){
        try {
            if(frequentQuestions != null && frequentQuestions.size()>0){
                StringBuilder sb = new StringBuilder();
                sb.append("<html><body>");
                for (ResponsePreguntasFrecuente item: frequentQuestions){
                    sb.append("<h3>");
                    sb.append(item.getPreguntas().get(0).getPregunta());
                    sb.append("</h3>");
                    sb.append(item.getPreguntas().get(0).getRespuesta());
                    sb.append("<hr>");
                }
                sb.append("</body></html>");
                webView.getSettings().setDefaultTextEncodingName("utf-8");
                webView.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);
            }else{
                hideSectionFrequentQuestions();
                layoutCircularProgressBar.setVisibility(View.VISIBLE);
                circularProgressBar.setVisibility(View.GONE);
                buttonReferesh.setVisibility(View.GONE);
                textCircularProgressBar.setVisibility(View.VISIBLE);
                textCircularProgressBar.setText("No hay datos disponibles");
            }
        } catch (Exception ex) {
            showDataFetchError("Lo sentimos", "Se ha producido un error, inténtalo nuevamente en unos minutos.");
            showErrorWithRefresh();
        }
    }

    @Override
    public void showSectionFrequentQuestions() {
        webView.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideSectionFrequentQuestions() {
        webView.setVisibility(View.GONE);
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
        webView.setVisibility(View.GONE);
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
            }
        });
        dialog.show();
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

//    private void cargarPreguntas(ArrayList<PreguntaFrecuente> preguntas) {
//        try {
//            StringBuilder sb = new StringBuilder();
//            //sb.append("<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" ?>");
//            sb.append("<html><body>");
//            for (PreguntaFrecuente item: preguntas){
//                sb.append("<h3>");
//                sb.append(item.getPregunta());
//                sb.append("</h3>");
//                sb.append(item.getRespuesta());
//                sb.append("<hr>");
//            }
//            sb.append("</body></html>");
//            //this.webView.loadData(sb.toString(), "text/html", "UTF-8");
//            //WebSettings settings = webView.getSettings();
//            webView.getSettings().setDefaultTextEncodingName("utf-8");
//            //settings.setDefaultTextEncodingName("utf-8");
//            webView.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);
//        } catch (Exception ex) {
//            context.makeErrorDialog("Error cargando los productos");
//        }
//    }
//
//    class PreguntasFrecuentesTask extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Consultando preguntas frecuentes...");
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                return networkHelper.readService(SincroHelper.PREGUNTAS_FRECUENTES);
//            } catch (Exception e) {
//                return e.getMessage();
//            }
//        }
//
//        @Override
//        protected void onProgressUpdate(String... values) {
//            pd.setMessage(values[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            pd.dismiss();
//            procesarJsonPreguntasFrecuentes(result);
//        }
//    }
//
//    private void procesarJsonPreguntasFrecuentes(String jsonRespuesta) {
//        try {
//            ArrayList<PreguntaFrecuente> preguntas = SincroHelper.procesarJsonPreguntasFrecuentes(jsonRespuesta);
//            GlobalState state = context.getState();
//            state.setPreguntaFrecuentes(preguntas);
//            cargarPreguntas(preguntas);
//        } catch (ErrorTokenException e) {
//            AlertDialog.Builder d = new AlertDialog.Builder(context);
//            d.setTitle("Sesión finalizada");
//            d.setIcon(R.mipmap.icon_presente);
//            d.setMessage(e.getMessage());
//            d.setCancelable(false);
//            d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    context.salir();
//                }
//            });
//            d.show();
//        } catch (Exception e) {
//            context.makeErrorDialog(e.getMessage());
//        }
//    }

}
