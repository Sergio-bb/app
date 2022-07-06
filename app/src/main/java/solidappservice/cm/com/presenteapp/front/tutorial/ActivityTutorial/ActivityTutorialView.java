package solidappservice.cm.com.presenteapp.front.tutorial.ActivityTutorial;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.adapters.tutorial.TutorialAdapter;
import solidappservice.cm.com.presenteapp.front.base.ActivityBase;
import solidappservice.cm.com.presenteapp.tools.PageIndicator;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 19/01/2016.
 * ACTUALIZADO POR MIGUEL DAVID CABEZAS EL 20/09/2021
 */
public class ActivityTutorialView extends ActivityBase implements ActivityTutorialContract.View{

    private ActivityTutorialPresenter presenter;
    private ActivityBase context;
    private List<Integer> resourceIds;

    private boolean stopSliding = false;
    private static final long ANIM_VIEWPAGER_DELAY = 5000;
    private static final long ANIM_VIEWPAGER_DELAY_USER_VIEW = 10000;

    private Handler handler;
    private Runnable animateViewPager;
    @BindView(R.id.indicator)
    PageIndicator mIndicator;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.lblSalirTutorial)
    TextView lblSalirTutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        ButterKnife.bind(this);
        setControls();
    }

    @Override
    protected void setControls() {
        presenter = new ActivityTutorialPresenter(this);
        context = this;
        lblSalirTutorial.setVisibility(View.GONE);
        fetchTutorialImages();
//        mIndicator.setOnPageChangeListener(new PageChangeListener());
//        mViewPager.setOnPageChangeListener(new PageChangeListener());
    }

    @OnClick(R.id.lblSalirTutorial)
    public void onClickSalir(View v) {
        setResult(RESULT_OK);
        context.finish();
    }

    @OnTouch(R.id.view_pager)
    public boolean onTouch(View v, MotionEvent event) {
        v.performClick();
        v.getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                // calls when touch release on ViewPager
                if (resourceIds != null && resourceIds.size() != 0) {
                    stopSliding = false;
                    runnable(resourceIds.size());
                    handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY_USER_VIEW);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                // calls when ViewPager touch
                if (handler != null && !stopSliding) {
                    stopSliding = true;
                    handler.removeCallbacks(animateViewPager);
                }

                if (mViewPager.getCurrentItem() == resourceIds.size() - 2) {
                    lblSalirTutorial.setVisibility(View.VISIBLE);
                }
                break;
        }
        return false;
    }

    @Override
    public void fetchTutorialImages() {
        resourceIds = new ArrayList<>();
        resourceIds.add(R.drawable.tutorial1);
        resourceIds.add(R.drawable.tutorial2);
        resourceIds.add(R.drawable.tutorial3);
        resourceIds.add(R.drawable.tutorial4);
        resourceIds.add(R.drawable.tutorial5);
        showTutorialImages();
    }

    @Override
    public void showTutorialImages(){
        if (resourceIds != null && resourceIds.size() > 0) {
            mViewPager.setAdapter(new TutorialAdapter(this, resourceIds));
            mIndicator.setViewPager(mViewPager);
            runnable(resourceIds.size());
            handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
        }
    }

    public void runnable(final int size) {
        handler = new Handler();
        animateViewPager = new Runnable() {
            public void run() {
                if (!stopSliding) {

                    if (mViewPager.getCurrentItem() == resourceIds.size() - 2) {
                        lblSalirTutorial.setVisibility(View.VISIBLE);
                    }

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


//    private class PageChangeListener implements ViewPager.OnPageChangeListener {
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//
//        }
//
//        @Override
//        public void onPageScrolled(int arg0, float arg1, int arg2) {
//        }
//
//        @Override
//        public void onPageSelected(int arg0) {
//        }
//    }

//    private void setPageAdapter() {
//        resourceIds = new ArrayList<>();
//        resourceIds.add(R.drawable.tutorial1);
//        resourceIds.add(R.drawable.tutorial2);
//        resourceIds.add(R.drawable.tutorial3);
//        resourceIds.add(R.drawable.tutorial4);
//        resourceIds.add(R.drawable.tutorial5);
//        mostrarImagenes();
//    }

//    private void mostrarImagenes() {
//        if (resourceIds != null && resourceIds.size() > 0) {
//            mViewPager.setAdapter(new TutorialAdapter(this, resourceIds));
//            mIndicator.setViewPager(mViewPager);
//            runnable(resourceIds.size());
//            // Re-run callback
//            handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
//        }
//    }


}

