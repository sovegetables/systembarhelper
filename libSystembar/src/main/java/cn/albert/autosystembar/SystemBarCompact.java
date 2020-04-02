package cn.albert.autosystembar;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;

interface SystemBarCompact {
    void setStatusBarColor(@ColorInt int statusBarColor);
    void setStatusBarColorRes(@ColorRes int statusBarColorRes);
    void setStatusBarDrawable(Drawable drawable);
    void setNavigationBarDrawable(Drawable drawable);
    void setNavigationBarColor(@ColorInt int navigationBarColor);
    void setNavigationBarColorRes(@ColorRes int navigationBarColorRes);
    void enableImmersedStatusBar(boolean immersed);
    void enableImmersedNavigationBar(boolean immersed);
    void statusBarFontStyle(@SystemBarHelper.StatusBarFontStyle int statusBarFontStyle);
    void navigationBarStyle(int navigationBarStyle);
}
