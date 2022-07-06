package solidappservice.cm.com.presenteapp.tools.tabsbasecontainers;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.front.solicitudahorros.FragmentSavingsSolicity.FragmentSavingsSolicityView;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 29/11/2015.
 */
public class SavingsSolicityViewContainerFragment extends BaseContainerFragment{

    private boolean mIsViewInited;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("test", "tab 1 oncreateview");
        return inflater.inflate(R.layout.container_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("test", "tab 1 container on activity created");
        if (!mIsViewInited) {
            mIsViewInited = true;
            initView();
        }
    }

    public void initView() {
        Log.e("test", "tab 1 init view");
        replaceFragment(new FragmentSavingsSolicityView(), false);
    }
}
