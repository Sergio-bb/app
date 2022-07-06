package solidappservice.cm.com.presenteapp.front.bottomnavigationbar.ActivityDirectory;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.bottomnavigationbar.DirectorioAdapter;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponseDirectorio;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 24/08/2021.
 */
public class ActivityDirectoryView extends ActivityBase implements ActivityDirectoryContract.View{

    private ActivityDirectoryPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private ProgressDialog pd;
    private List<ResponseDirectorio> directorios;
    private ResponseDirectorio contactoDirectorio;

    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 98;

    @BindView(R.id.list_directorio)
    ListView list_directorio;
    @BindView(R.id.btn_back)
    ImageButton btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);
        ButterKnife.bind(this);
        setControls();
    }

    @Override
    protected void setControls() {
        presenter = new ActivityDirectoryPresenter(this, new ActivityDirectoryModel());
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
            if (context.getState().getDirectorios() == null ||
                    context.getState().getDirectorios().size() == 0) {
                presenter.fetchDirectory();
            }else{
                directorios = context.getState().getDirectorios();
                showDirectory(directorios);
            }
        }
    }

    @OnClick(R.id.btn_back)
    public void onClickBack(){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnItemClick(R.id.list_directorio)
    void onItemClickDirectorio(AdapterView<?> parent, View view, int position, long id) {
        if(directorios != null && directorios.size() > 0){
            contactoDirectorio = directorios.get(position);
            callDirectoryContact(contactoDirectorio);
        }
    }

    @Override
    public void showDirectory(List<ResponseDirectorio> directorios) {
        try {
            if(directorios == null) directorios = new ArrayList<>();
            this.directorios = directorios;
            DirectorioAdapter pa = new DirectorioAdapter(context, directorios);
            list_directorio.setAdapter(pa);
        } catch (Exception ex) {
            context.makeErrorDialog("Error cargando el directorio");
        }
    }

    @Override
    public void callDirectoryContact(ResponseDirectorio dir){
        try{
            String uri = "tel:" + dir.getN_teleusu().trim();
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse(uri));
            startActivity(call);
        } catch (Exception e){
            context.makeDialog("Erro haciendo la llamada: " + e.toString());
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

//    private void cargarDirectorio(ArrayList<Directorio> directorio) {
//        try {
//            if(directorio == null) directorio = new ArrayList<>();
//            DirectorioAdapter pa = new DirectorioAdapter(context, directorio);
//            list_directorio.setAdapter(pa);
//        } catch (Exception ex) {
//            context.makeErrorDialog("Error cargando los productos");
//        }
//    }

//    private void call(Directorio dir){
//        try{
//            String uri = "tel:" + dir.getN_teleusu().trim();
//            Intent call = new Intent(Intent.ACTION_DIAL);
//            call.setData(Uri.parse(uri));
//            startActivity(call);
//
//        } catch (Exception e){
//            context.makeDialog("Erro haciendo la llamada: " + e.toString());
//        }
//    }


//    private class DirectorioTask extends AsyncTask<String, String, String> {
//
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Consultando directorio...");
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected void onProgressUpdate(String... values) {
//            pd.setMessage(values[0]);
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                return networkHelper.readService(SincroHelper.DIRECTORIO);
//            } catch (Exception e) {
//                return e.getMessage();
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            pd.dismiss();
//            procesarResultDirectorio(result);
//        }
//    }
//
//    private void procesarResultDirectorio(String result){
//        try{
//            ArrayList<Directorio> list = SincroHelper.procesarJsonDirectorio(result);
//            context.getState().setDirectorios(list);
//            directorios = list;
//            cargarDirectorio(directorios);
//        }catch (Exception ex){
//            makeErrorDialog(ex.getMessage());
//        }
//    }

}
