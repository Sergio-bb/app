package solidappservice.cm.com.presenteapp.front.convenios.FragmentMainMenuAgreements;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.convenios.CategoriasAdapter;
import solidappservice.cm.com.presenteapp.adapters.convenios.ConveniosSlideAdapter;
import solidappservice.cm.com.presenteapp.entities.base.GlobalState;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Categoria;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Convenio;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Resumen;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.ActivityAgreementsView;
import solidappservice.cm.com.presenteapp.front.convenios.FragmentMainMenuAgreements.ActivitySelectCity.ActivitySelectCityView;
import solidappservice.cm.com.presenteapp.front.popups.PopUp;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;
import solidappservice.cm.com.presenteapp.tools.PageIndicator;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 31/07/2018.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 13/09/2021.
 */
public class FragmentAgreementsMainMenuView extends Fragment implements FragmentAgreementsMainMenuContract.View{

    private FragmentAgreementsMainMenuPresenter presenter;
    private ActivityBase activityBase;
    private ActivityAgreementsView context;
    private GlobalState state;
    private List<Convenio> convenios_destacados;
    private List<Categoria> categorias;
    private FirebaseAnalytics firebaseAnalytics;

    private static final long ANIM_VIEWPAGER_DELAY = 5000;
    private static final long ANIM_VIEWPAGER_DELAY_USER_VIEW = 10000;
    private boolean stopSliding = false;
    private Runnable animateViewPager;
    private Handler handler;
    private static final int REQ_SELECT_CITY = 3;

    @BindView(R.id.my_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.indicator)
    PageIndicator mIndicator;
    @BindView(R.id.imgb_menu_popup)
    ImageButton imgb_menu_popup;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle params = new Bundle();
        params.putString("Descripción", "Interacción con pantalla de convenios menu principal");
        firebaseAnalytics.logEvent("pantalla_convenios_menu_principal", params);
        View view = inflater.inflate(R.layout.fragment_agreements_mainmenu, container, false);
        ButterKnife.bind(this, view);
        setControls();
        return view;
    }

    protected void setControls() {
        presenter = new FragmentAgreementsMainMenuPresenter(this);
        activityBase = (ActivityBase) getActivity();
        context = (ActivityAgreementsView) getActivity();
        state = activityBase.getState();
        state.setFragmentActual(IFragmentCoordinator.Pantalla.ConveniosMenuPrincipal);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (context != null) {
            context.btn_back.setVisibility(View.VISIBLE);
            context.header.setImageResource(R.drawable.logo_internal);
        }

        presenter.fetchAgreementCategoriesByUserLocation(state.getResumen());
    }

    @SuppressLint("RestrictedApi")
    @OnClick(R.id.imgb_menu_popup)
    public void onClickMenu(View view) {
        if (context != null) {
            @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(context);
            MenuInflater inflater = new MenuInflater(context);
            inflater.inflate(R.menu.menu_convenios, menuBuilder);
            @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(context, menuBuilder, view);
            optionsMenu.setForceShowIcon(true);
            // Set Item Click Listener
            menuBuilder.setCallback(new MenuBuilder.Callback() {
                @Override
                public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.item_filter_city:
                            Intent intent = new Intent(context, ActivitySelectCityView.class);
                            startActivityForResult(intent, REQ_SELECT_CITY);
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onMenuModeChange(MenuBuilder menu) {}
            });
            optionsMenu.show();
        }
    }

    @OnTouch(R.id.view_pager)
    public boolean onTouchViewPager(View v, MotionEvent event) {
        v.performClick();
        v.getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {

            case MotionEvent.ACTION_CANCEL:
                break;

            case MotionEvent.ACTION_UP:
                // calls when touch release on ViewPager
                if (convenios_destacados != null && convenios_destacados.size() != 0) {
                    stopSliding = false;
                    runnable(convenios_destacados.size());
                    handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY_USER_VIEW);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                // calls when ViewPager touch
                if (handler != null && !stopSliding) {
                    stopSliding = true;
                    handler.removeCallbacks(animateViewPager);
                }
                break;
        }
        return false;
    }

    @Override
    public void showCategories(List<Categoria> categorias, Resumen resumen) {
        this.categorias = categorias;
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.Adapter mAdapter = new CategoriasAdapter(categorias);
        ((CategoriasAdapter) mAdapter).setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView recyclerView = (RecyclerView) v.getParent();
                CategoriasAdapter.ViewHolder currentViewHolder = (CategoriasAdapter.ViewHolder) recyclerView.getChildViewHolder(v);
                int position = currentViewHolder.getAdapterPosition();
                showAgreementByCategories(position);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        presenter.fetchFeaturedAgreements(resumen.getConveniosDestacados());
    }

    @Override
    public void showAgreementByCategories(int positionCategory){
        Categoria categorySelected = categorias.get(positionCategory);
        if(context != null){
            state.haveJumpedToProducts(false);
            state.getResumen().setCategoriaSeleccionada(categorySelected);
            context.setFragment(IFragmentCoordinator.Pantalla.ConveniosLista);
        }
    }

    @Override
    public void showFeaturedAgreements(List<Convenio> featuredAgreements, boolean genericUsed) {
        convenios_destacados = featuredAgreements;
        ConveniosSlideAdapter conveniosSlideAdapter = new ConveniosSlideAdapter((ActivityBase) getActivity(), convenios_destacados);
        conveniosSlideAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int posSelected = mViewPager.getCurrentItem();
                showProductsByAgreements(posSelected);
            }
        });
        mViewPager.setAdapter(conveniosSlideAdapter);
        if(genericUsed){
            mIndicator.setViewPager(null);
        }else{
            mIndicator.setViewPager(mViewPager);
        }
        runnable(convenios_destacados.size());
        handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
    }

    @Override
    public void showProductsByAgreements(int positionAgreement){
        Convenio convenioSeleccionado = convenios_destacados.get(positionAgreement);
        if(context != null && !convenioSeleccionado.getNombre().equalsIgnoreCase("GENERIC")){
            state.haveJumpedToProducts(true);
            state.getResumen().setConvenioSeleccionado(convenioSeleccionado);
            context.setFragment(IFragmentCoordinator.Pantalla.ConveniosProductos);
        }
    }

    public void runnable(final int size) {
        handler = new Handler();
        animateViewPager = new Runnable() {
            public void run() {
                if (!stopSliding) {
                    if (mViewPager.getCurrentItem() == size - 1) {
                        mViewPager.setCurrentItem(0);
                    } else {
                        mViewPager.setCurrentItem(
                                mViewPager.getCurrentItem() + 1, true);
                    }
                    handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
                }
            }
        };
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_SELECT_CITY){
            if(resultCode == Activity.RESULT_OK){
                String selectedCity = data.getStringExtra("city");
                Resumen resumen = state.getResumen();
                resumen.setUbicacionAsociado(selectedCity);
                presenter.fetchAgreementCategoriesByUserLocation(resumen);
//                loadData(filtrarCategorias(selectedCity, resumen), resumen);
            }
        }
    }


}