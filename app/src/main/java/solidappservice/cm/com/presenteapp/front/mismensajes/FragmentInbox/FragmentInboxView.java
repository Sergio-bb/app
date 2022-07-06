package solidappservice.cm.com.presenteapp.front.mismensajes.FragmentInbox;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTabHost;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.mensajes.MensajesBuzonAdapter;
import solidappservice.cm.com.presenteapp.entities.mensajes.response.ResponseObtenerMensajes;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.tabs.ActivityTabs.ActivityTabsView;
import solidappservice.cm.com.presenteapp.tools.security.Encripcion;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 30/11/2015.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 15/09/2021.
 */
public class FragmentInboxView extends Fragment implements FragmentInboxContract.View{

    private FragmentInboxPresenter presenter;
    private ActivityBase context;
    private GlobalState state;
    private FirebaseAnalytics firebaseAnalytics;
    private List<ResponseObtenerMensajes> mensajesBuzon;

    @BindView(R.id.list_mensajes)
    ListView listMensajes;

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
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de buzon de mensajes");
        firebaseAnalytics.logEvent("pantalla_buzon_mensajes", params);
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentInboxPresenter(this, new FragmentInboxModel());
        context = (ActivityBase) getActivity();
        state = context.getState();
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchMessages();
                pullToRefresh.setRefreshing(false);
            }
        });
        fetchMessages();
    }

    @Override
    public void onResume() {
        super.onResume();
        showUnreadMessages(null);
        GlobalState state = context.getState();
        if (state == null || state.getUsuario() == null) {
            context.salir();
        }
    }

    @OnItemClick(R.id.list_mensajes)
    public void onItemClickMensajes(AdapterView<?> parent, View view, int position, long id) {
        if (mensajesBuzon != null && mensajesBuzon.size() > 0) {
            ResponseObtenerMensajes mensaje = mensajesBuzon.get(position);
            state.setMensajesBuzonSeleccionado(mensaje);
            state.getmTabHost().setCurrentTab(ActivityTabsView.TAB_5_INBOX_READ_MESSAGE_TAG);
        }
    }

    @OnClick(R.id.imageReferesh)
    public void onClickRefresh(){
        fetchMessages();
    }

    @Override
    public void fetchMessages(){
        try{
            Encripcion encripcion = Encripcion.getInstance();
            presenter.fetchMessages(new BaseRequest(
                    encripcion.encriptar(state.getUsuario().getCedula()),
                    state.getUsuario().getToken()
            ));
        }catch (Exception ex){
            showDataFetchError("Lo sentimos", "");
            showErrorWithRefresh();
        }
    }

    @Override
    public void showMessages(List<ResponseObtenerMensajes> inbox){
        try{
            if(inbox!= null && inbox.size()>0){
                listMensajes.setVisibility(View.VISIBLE);
                MensajesBuzonAdapter adapter = new MensajesBuzonAdapter(context, inbox);
                this.mensajesBuzon = inbox;
                listMensajes.setAdapter(adapter);
            }else{
                layoutCircularProgressBar.setVisibility(View.VISIBLE);
                pullToRefresh.setVisibility(View.GONE);
                circularProgressBar.setVisibility(View.GONE);
                buttonReferesh.setVisibility(View.VISIBLE);
                textCircularProgressBar.setVisibility(View.VISIBLE);
                textCircularProgressBar.setText("No hay mensajes");
            }
            showUnreadMessages(null);
        }catch (Exception e){
            showDataFetchError("Lo sentimos", "");
            showErrorWithRefresh();
        }
    }

    @Override
    public void showUnreadMessages(ResponseObtenerMensajes inboxMessage){
        try{
            if(state != null){
                FragmentTabHost mTabHost = state.getmTabHost();
                if(mTabHost != null){
                    int numberOfUnReadMessages = 0;
                    if(state.getMensajesBuzon() != null && state.getMensajesBuzon().size() > 0){
                        int counter = 0;
                        for(ResponseObtenerMensajes m : state.getMensajesBuzon()){
                            if(m.getLeido().equals("N")){
                                counter++;
                            }
                        }
                        numberOfUnReadMessages = counter;
                    }

                    int currentTab = mTabHost.getCurrentTab();
                    if(currentTab == 3) {
                        View view_tab = mTabHost.getCurrentTabView();
                        if(view_tab == null) return;
                        TextView c = view_tab.findViewById(R.id.cantMessages);
                        if(c == null) return;
                        if (numberOfUnReadMessages > 0) {
                            c.setText((numberOfUnReadMessages > 9 ? String.valueOf(numberOfUnReadMessages):" "+String.valueOf(numberOfUnReadMessages)+" "));
                            c.setVisibility(View.VISIBLE);
                        } else {
                            c.setText("");
                            c.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void showSectionMessages(){
        pullToRefresh.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideSectionMessages(){
        pullToRefresh.setVisibility(View.GONE);
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
        pullToRefresh.setVisibility(View.GONE);
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
                context.finish();
                dialog.dismiss();
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
                context.finish();
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

//
//    private class BuzonTask extends AsyncTask<String, String, String> {
//
//        String cedula = null;
//        String token = null;
//
//        @Override
//        protected void onPreExecute() {
//            pd.setTitle(context.getResources().getString(R.string.app_name));
//            pd.setMessage("Actualizando mensajes...");
//            pd.setIcon(R.mipmap.icon_presente);
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                NetworkHelper networkHelper = new NetworkHelper();
//                Encripcion encripcion = Encripcion.getInstance();
//                JSONObject param = new JSONObject();
//                param.put("cedula", cedula = encripcion.encriptar(params[0]));
//                param.put("token", token = params[1]);
//                return networkHelper.writeService(param, SincroHelper.MENSAJES_BUZON);
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
//            procesarResultMensajesBuzon(result);
//        }
//    }
//
//    private void procesarResultMensajesBuzon(String result){
//        try{
//            ArrayList<MensajesBuzon> mensajesBuzon = SincroHelper.procesarJsonMensajesBuzon(result);
//
//            /*
//                MensajesBuzon mensajesBuzon1 = new MensajesBuzon();
//                String html = "<!DOCTYPE html>\n" +
//                        "<html>\n" +
//                        "<head>\n" +
//                        "<title>Page Title</title>\n" +
//                        "</head>\n" +
//                        "<body>\n" +
//                        "\n" +
//                        "<h1>This is a Heading</h1>\n" +
//                        "<p>This is a paragraph.</p>\n" +
//                        "<img src='https://as01.epimg.net/colombia/imagenes/2017/01/03/futbol/1483404069_897588_1483406699_noticia_normal.jpg' alt='Mountain View'>" +
//                        "\n" +
//                        "</body>\n" +
//                        "</html>";
//                mensajesBuzon1.idMensaje = "23";
//                mensajesBuzon1.leido = "N";
//                mensajesBuzon1.titulo = "Hola";
//                mensajesBuzon1.contenido = html;
//                mensajesBuzon.add(mensajesBuzon1);
//            */
//
//            context.getState().setMensajesBuzon(mensajesBuzon);
//            mostrarMensajes(mensajesBuzon);
//        }catch (ErrorTokenException e){
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
//        }catch (Exception ex){
//            context.makeErrorDialog(ex.getMessage());
//        }
//    }
//
//    private void mostrarMensajes(ArrayList<MensajesBuzon> mensajesBuzon){
//        try{
//            MensajesBuzonAdapter adapter = new MensajesBuzonAdapter(context, mensajesBuzon);
//            this.mensajesBuzon = mensajesBuzon;
//            listMensajes.setAdapter(adapter);
//            context.showUnreadMessages(null);
//        }catch (Exception e){
//            context.makeErrorDialog("Error cargando los mensajes");
//        }
//    }
}
