package cn.albert.autosystembar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Created by albert on 2017/9/26.
 *
 */

class InternalLayout extends RelativeLayout {

    private View mStatusView;
    private View mNavigationView;
    private ViewGroup mContentLayout;

    private static final int FLAG_BOTH_NOT_IMMERSED = 0x0;
    private static final int FLAG_IMMERSED_STATUS_BAR = 0x1;
    private static final int FLAG_IMMERSED_NAVIGATION_BAR = 0x2;
    private static final int FLAG_IMMERSED_STATUS_BAR_AND_NAVIGATION_BAR = FLAG_IMMERSED_STATUS_BAR | FLAG_IMMERSED_NAVIGATION_BAR;
    private int mImmersedFlag = FLAG_BOTH_NOT_IMMERSED;

    public InternalLayout(Context context) {
        this(context, null);
    }

    public InternalLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        Utils.initializeStatusBar(this);
        Utils.initializeNavigationBar(this);

        ViewCompat.setFitsSystemWindows(this, true);
        ViewCompat.requestApplyInsets(this);
        ViewCompat.setOnApplyWindowInsetsListener(this, new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                return insets.consumeSystemWindowInsets();
            }
        });
        inflate(context, R.layout.layout_content, this);
        mStatusView = findViewById(R.id.status_view);
        mStatusView.getLayoutParams().height = Utils.sStatusBarHeight;
        mNavigationView = findViewById(R.id.navigation_view);
        mNavigationView.getLayoutParams().height = Utils.sNavigationBarHeight;

        mContentLayout = findViewById(R.id.content);

    }

    void setStatusBarVisibility(boolean visible){
        mStatusView.setVisibility(visible? VISIBLE : GONE);
    }

    void setNavigationBarVisibility(boolean visible){
        mNavigationView.setVisibility(visible? VISIBLE : GONE);
    }

    void setContentView(View content) {
        if(content.getParent() == null){
            mContentLayout.addView(content);
        }
    }

    void setStatusBarColor(@ColorInt int statusBarColor) {
        mStatusView.setBackgroundColor(statusBarColor);
    }

    void setStatusBarColorRes(@ColorRes int statusBarColorRes) {
        mStatusView.setBackgroundResource(statusBarColorRes);
    }

    void setStatusBarDrawable(Drawable drawable){
        ViewCompat.setBackground(mStatusView, drawable);
    }

    void setNavigationBarDrawable(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mStatusView.setBackground(drawable);
        }else {
            mStatusView.setBackgroundDrawable(drawable);
        }
    }

    void setNavigationBarColor(@ColorInt int navigationBarColor){
        mNavigationView.setBackgroundColor(navigationBarColor);
    }

    void setNavigationBarColorRes(@ColorRes int navigationBarColorRes) {
        mNavigationView.setBackgroundResource(navigationBarColorRes);
    }

    void setNavigationDrawable(Drawable drawable){
        ViewCompat.setBackground(mNavigationView, drawable);
    }

    void enableImmersedStatusBar(boolean immersed) {
        if(immersed){
            mImmersedFlag |= FLAG_IMMERSED_STATUS_BAR;
        }else {
            mImmersedFlag = (mImmersedFlag & ~FLAG_IMMERSED_STATUS_BAR);
        }
        ViewCompat.setAlpha(mStatusView, immersed? 0 : 1);
        configContentLayout();
    }

    void enableImmersedNavigationBar(boolean immersed) {
        if(immersed){
            mImmersedFlag |= FLAG_IMMERSED_NAVIGATION_BAR;
        }else {
            mImmersedFlag &= (mImmersedFlag & ~FLAG_IMMERSED_NAVIGATION_BAR);
        }
        ViewCompat.setAlpha(mNavigationView, immersed? 0 : 1);
        configContentLayout();
    }

    private void configContentLayout() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        switch (mImmersedFlag){
            case FLAG_IMMERSED_STATUS_BAR:
                params.addRule(RelativeLayout.ABOVE, R.id.navigation_view);
                bringChildToFront(mStatusView);
                break;
            case FLAG_IMMERSED_NAVIGATION_BAR:
                params.addRule(RelativeLayout.BELOW, R.id.status_view);
                break;
            case FLAG_IMMERSED_STATUS_BAR_AND_NAVIGATION_BAR:
                break;
            case FLAG_BOTH_NOT_IMMERSED:
                params.addRule(RelativeLayout.BELOW, R.id.status_view);
                params.addRule(RelativeLayout.ABOVE, R.id.navigation_view);
                break;
        }
        mContentLayout.setLayoutParams(params);
    }

    public void setStatusBarAlpha(int alpha) {
        ViewCompat.setAlpha(mStatusView, 1);
        final Drawable background = mStatusView.getBackground();
        if(background != null){
            mStatusView.getBackground().mutate().setAlpha(alpha);
        }
    }

    public void setNavigationBarAlpha(int alpha) {
        ViewCompat.setAlpha(mNavigationView, 1);
        final Drawable background = mNavigationView.getBackground();
        if(background != null){
            mNavigationView.getBackground().mutate().setAlpha(alpha);
        }
    }
}
