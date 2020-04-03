package cn.albert.autosystembar;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class Lollipop extends Base {

    Lollipop(SystemBarHelper.Builder builder) {
        super(builder);
    }

    @Override
    public void setStatusBarColor(int statusBarColor) {
        if(mBuilder.mIsImmersedStatusBar){
            final InternalLayout internalLayout = mBuilder.mInternalLayout;
            if(internalLayout != null){
                internalLayout.setStatusBarColor(statusBarColor);
            }
        }else {
            mBuilder.mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            mBuilder.mActivity.getWindow().setStatusBarColor(statusBarColor);
        }
    }

    @Override
    public void setStatusBarDrawable(Drawable drawable) {
        if(mBuilder.mIsImmersedStatusBar){
            final InternalLayout internalLayout = mBuilder.mInternalLayout;
            if(internalLayout != null){
                internalLayout.setStatusBarDrawable(drawable);
            }
        }else {
            //非沉浸式不支持Drawable
        }
    }

    @Override
    public void enableImmersedStatusBar(boolean immersed) {
        if(mBuilder.mIsImmersedStatusBar){
            final InternalLayout internalLayout = mBuilder.mInternalLayout;
            if(internalLayout != null){
                internalLayout.enableImmersedStatusBar(immersed);
            }
        }
    }

    @Override
    public void enableImmersedNavigationBar(boolean immersed) {
        if(mBuilder.mIsImmersedNavigationBar){
            final InternalLayout internalLayout = mBuilder.mInternalLayout;
            if(internalLayout != null){
                internalLayout.enableImmersedNavigationBar(immersed);
            }
        }
    }

    @Override
    public void setNavigationBarDrawable(Drawable drawable) {
        if(mBuilder.mIsImmersedNavigationBar){
            final InternalLayout internalLayout = mBuilder.mInternalLayout;
            if(internalLayout != null){
                internalLayout.setNavigationDrawable(drawable);
            }
        }
    }

    @Override
    public void setNavigationBarColor(int navigationBarColor) {
        if(mBuilder.mIsImmersedNavigationBar) {
            final InternalLayout internalLayout = mBuilder.mInternalLayout;
            if(internalLayout != null){
                internalLayout.setNavigationBarColor(navigationBarColor);
            }
        }else {
            mBuilder.mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            mBuilder.mActivity.getWindow().setNavigationBarColor(navigationBarColor);
        }
    }

    @Override
    public void statusBarFontStyle(int statusBarFontStyle) {
        boolean isDarkFont = statusBarFontStyle == SystemBarHelper.STATUS_BAR_DARK_FONT_STYLE;
        for (IStatusBarFontStyle style: SystemBarHelper.STATUS_BAR_FONT_STYLES){
            if(style.verify()){
                style.statusBarFontStyle(mBuilder.mActivity, isDarkFont);
                break;
            }
        }
    }

    @Override
    public void navigationBarStyle(int navigationBarStyle) {
        boolean isDark = navigationBarStyle == SystemBarHelper.NAVIGATION_BAR_DARK_ICON_STYLE;
        for (INavigationBarStyle style: SystemBarHelper.NAVIGATION_BAR_STYLES){
            if(style.verify()){
                style.navigationStyle(mBuilder.mActivity, isDark);
                break;
            }
        }
    }

    @Override
    public void setStatusBarColorRes(int statusBarColorRes) {
        setStatusBarColor(ContextCompat.getColor(mBuilder.mActivity, statusBarColorRes));
    }

    @Override
    public void setNavigationBarColorRes(int navigationBarColorRes) {
        setNavigationBarColor(ContextCompat.getColor(mBuilder.mActivity, navigationBarColorRes));
    }
}
