package solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.agreements.ActivityHtmlViewer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import solidappservice.cm.com.presenteapp.R;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 21/08/2018.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */

public class ActivityHtmlViewer extends FragmentActivity implements ActivityHtmlViewerContract.View {

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.btn_back)
    ImageButton btn_back;
    @BindView(R.id.btnSalir)
    TextView btnSalir;
    @BindView(R.id.header)
    ImageView header;
    @BindView(R.id.btn_aceptar)
    Button btn_aceptar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreements_htmlviewer);
        ButterKnife.bind(this);
        setControls();
    }

    protected void setControls() {
        btn_back.setVisibility(View.VISIBLE);
        header.setImageResource(R.drawable.logo_internal);
        btnSalir.setVisibility(View.GONE);

        String html = getIntent().getStringExtra("html");
        showHtml(html);
    }

    @OnClick(R.id.btn_aceptar)
    public void onClickAceptar(View v) {
        setResult(RESULT_OK);
        finish();
    }

    @OnClick({R.id.btnSalir, R.id.btn_back})
    public void onClickSalir(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
    }

    @Override
    public void showHtml(String html) {
        if(html != null){
            webView.getSettings().setDefaultTextEncodingName("utf-8");
            webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
        }else{
            showDataFetchError("Se ha producido un error, inténtalo nuevamente en unos minutos.");
        }
    }

    @Override
    public void showDataFetchError(String message) {
        AlertDialog.Builder d = new AlertDialog.Builder(this);
        d.setTitle(this.getResources().getString(R.string.app_name));
        d.setIcon(R.mipmap.icon_presente);
        d.setMessage(message);
        d.setCancelable(false);
        d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
            }
        });
        d.show();
    }
}
