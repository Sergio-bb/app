package solidappservice.cm.com.presenteapp.tools;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import androidx.fragment.app.Fragment;

public class HeightProviderFragment extends PopupWindow implements ViewTreeObserver.OnGlobalLayoutListener {

    private Fragment mFragment;
    private View rootView;
    private HeightListener listener;
    private int heightMax; // Record the maximum height of the pop content area

    public HeightProviderFragment(Fragment fragment) {
        super(fragment.getActivity());
        this.mFragment = fragment;

        // Basic configuration
        rootView = new View(fragment.getActivity());
        setContentView(rootView);

        // Monitor global Layout changes
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        setBackgroundDrawable(new ColorDrawable(0));

        // Set width to 0 and height to full screen
        setWidth(0);
        setHeight(FrameLayout.LayoutParams.MATCH_PARENT);

        // Set keyboard pop-up mode
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
    }

    public HeightProviderFragment init() {
        if (!isShowing()) {
            final View view = mFragment.getActivity().getWindow().getDecorView();
            // Delay loading popupwindow, if not, error will be reported
            view.post(new Runnable() {
                @Override
                public void run() {
                    showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);
                }
            });
        }
        return this;
    }

    public HeightProviderFragment setHeightListener(HeightListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void onGlobalLayout() {
        Rect rect = new Rect();
        rootView.getWindowVisibleDisplayFrame(rect);
        if (rect.bottom > heightMax) {
            heightMax = rect.bottom;
        }

        // The difference between the two is the height of the keyboard
        int keyboardHeight = heightMax - rect.bottom;
        if (listener != null) {
            listener.onHeightChanged(keyboardHeight);
        }
    }

    public interface HeightListener {
        void onHeightChanged(int height);
    }
}
