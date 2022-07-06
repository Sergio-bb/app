package solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.agreements.FragmentListAgreements;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
            context.btnSalir.setVisibility(View.VISIBLE);
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
            showDataFetchError("");
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
                context.salir();
            }
        });
        d.show();
    }

//    private void loadConvenios(){
//        if(activityBase != null){
//            GlobalState state = activityBase.getState();
//            Resumen resumen = state.getResumen();
//            if(resumen != null){
//                Categoria categoria = resumen.getCategoriaSeleccionada();
//                loadData(resumen.getConveniosPorCategoria(categoria.getId()), resumen.getUbicacionAsociado());
//            }
//        }
//    }
//
//    private void loadData(List<Convenio> convenios, String ubicacionActualAsociado){
//
//        this.convenios = new ArrayList<>();
//
//        Convenio conTemp;
//        ArrayList<Convenio> conveniosOrdenados = new ArrayList<>();
//
//
//        for(int i = 0; i < convenios.size(); i++){
//            for(int j=1; j < convenios.size()-i; j++){
//                if(convenios.get(j-1).getOrden() < convenios.get(j).getOrden()){
//                    conTemp = convenios.get(j-1);
//                    convenios.set(j-1, convenios.get(j));
//                    convenios.set(j, conTemp);
//                }
//            }
//        }
//
//
//
//        for (Convenio c: convenios){
//            ArrayList<Ubicacion> ubicaciones = c.getUbicaciones();
//            for(Ubicacion u: ubicaciones){
//                if(u.getCiudad().toUpperCase().contains(ubicacionActualAsociado.toUpperCase())){
//                    this.convenios.add(c);
//                    break;
//                }
//            }
//        }
//
//        RecyclerView.Adapter mAdapter = new ConveniosAdapter(this.convenios);
//        ((ConveniosAdapter) mAdapter).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RecyclerView recyclerView = (RecyclerView) v.getParent();
//                ConveniosAdapter.ViewHolder currentViewHolder = (ConveniosAdapter.ViewHolder) recyclerView.getChildViewHolder(v);
//                int position = currentViewHolder.getAdapterPosition();
//                verProductosPorConvenio(position);
//            }
//        });
//
//        mRecyclerView.setAdapter(mAdapter);
//    }
//
//    private void verProductosPorConvenio(int posSelected){
//        Convenio convenioSeleccionado = convenios.get(posSelected);
//        ActivityAgreementsView context = (ActivityAgreementsView) getActivity();
//        if(context != null){
//            GlobalState state = context.getState();
//            state.getResumen().setConvenioSeleccionado(convenioSeleccionado);
//            context.setFragment(IFragmentCoordinator.Pantalla.ConveniosProductos);
//        }
//    }

}
