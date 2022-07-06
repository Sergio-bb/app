package solidappservice.cm.com.presenteapp.front.convenios.FragmentListAgreements;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.convenios.ConveniosAdapter;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Convenio;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.ActivityAgreementsView;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 31/07/2018.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */

public class FragmentListAgreementsView extends Fragment implements FragmentListAgreementsContract.View{

    private FragmentListAgreementsPresenter presenter;
    private ActivityAgreementsView context;
    private ActivityBase activityBase;
    private GlobalState state;
    private List<Convenio> convenios;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.my_recycler_view)
    RecyclerView mRecyclerView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de lista convenios");
        firebaseAnalytics.logEvent("pantalla_lista_convenios", params);
        View view = inflater.inflate(R.layout.fragment_agreements_list, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentListAgreementsPresenter(this);
        activityBase = (ActivityBase) getActivity();
        context = (ActivityAgreementsView) getActivity();
        state = activityBase.getState();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (context != null) {
            context.btn_back.setVisibility(View.VISIBLE);
            context.header.setImageResource(R.drawable.logo_internal);
        }

        presenter.fetchAgreementByCategoryByUserLocation(state.getResumen());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void showAgreements(List<Convenio> convenios){
        try{
            this.convenios = convenios;
            RecyclerView.Adapter mAdapter = new ConveniosAdapter(this.convenios);
            ((ConveniosAdapter) mAdapter).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecyclerView recyclerView = (RecyclerView) v.getParent();
                    ConveniosAdapter.ViewHolder currentViewHolder = (ConveniosAdapter.ViewHolder) recyclerView.getChildViewHolder(v);
                    int position = currentViewHolder.getAdapterPosition();
                    showProductsByAgreements(position);
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }catch(Exception ex){
            showDataFetchError("Lo sentimos", "");
        }

    }

    @Override
    public void showProductsByAgreements(int positionAgreement){
        Convenio convenioSeleccionado = convenios.get(positionAgreement);
        if(context != null){
            state.getResumen().setConvenioSeleccionado(convenioSeleccionado);
            context.setFragment(IFragmentCoordinator.Pantalla.ConveniosProductos);
        }
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
                context.onBackPressed();
            }
        });
        dialog.show();
    }
}
