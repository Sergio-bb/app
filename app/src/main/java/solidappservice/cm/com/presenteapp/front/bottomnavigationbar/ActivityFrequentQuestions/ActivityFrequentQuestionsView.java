package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityFrequentQuestions;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;

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
    private ProgressDialog pd;

    @BindView(R.id.wvPreguntasFrecuentes)
    WebView webView;
    @BindView(R.id.btn_back)
    ImageButton btnBack;

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
        pd = new ProgressDialog(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalState state = context.getState();
        if(state == null){
            context.salir();
        }else {
            if (state.getPreguntaFrecuentes()!=null && state.getPreguntaFrecuentes().size()>0) {
//                cargarPreguntas(state.getPreguntaFrecuentes());
                showFrequentQuestions(state.getPreguntaFrecuentes());
            }else{
//                new PreguntasFrecuentesTask().execute();
                fetchFrequentQuestions();
            }
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

    @Override
    public void fetchFrequentQuestions(){
        try{
            presenter.fetchFrequentQuestions();
        }catch (Exception ex){
            showDataFetchError("");
        }
    }

    @Override
    public void showFrequentQuestions(List<ResponsePreguntasFrecuente> frequentQuestions){
        try {
            StringBuilder sb = new StringBuilder();
            //sb.append("<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" ?>");
            sb.append("<html><body>");
            for (ResponsePreguntasFrecuente item: frequentQuestions){
                sb.append("<h3>");
                sb.append(item.getPreguntas().get(0).getPregunta());
                sb.append("</h3>");
                sb.append(item.getPreguntas().get(0).getRespuesta());
                sb.append("<hr>");
            }
            sb.append("</body></html>");
            //this.webView.loadData(sb.toString(), "text/html", "UTF-8");
            //WebSettings settings = webView.getSettings();
            webView.getSettings().setDefaultTextEncodingName("utf-8");
            //settings.setDefaultTextEncodingName("utf-8");
            webView.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);
        } catch (Exception ex) {
            context.makeErrorDialog("Error cargando los productos");
        }
    }

    @Override
    public void showProgressDialog(String message) {
        pd.setTitle(context.getResources().getString(R.string.app_name));
        pd.setMessage(message);
        pd.setIcon(R.mipmap.icon_presente);
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    public void hideProgressDialog() {
        pd.dismiss();
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
                finish();
            }
        });
        d.show();
    }

    @Override
    public void showDataFetchError(String message) {
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
        d.setTitle(context.getResources().getString(R.string.app_name));
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(message);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
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
