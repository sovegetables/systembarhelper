package cn.albert.autosystembar;

import android.graphics.drawable.Drawable;

class Base implements SystemBarCompact {

    SystemBarHelper.Builder mBuilder;

    Base(SystemBarHelper.Builder builder) {
        this.mBuilder = builder;
    }

    @Override
    public void setStatusBarColor(int statusBarColor) {
    }

    @Override
    public void setStatusBarColorRes(int statusBarColorRes) {
    }

    @Override
    public void setStatusBarDrawable(Drawable drawable) {
    }

    @Override
    public void enableImmersedStatusBar(boolean immersed) {
    }

    @Override
    public void enableImmersedNavigationBar(boolean immersed) {
    }

    @Override
    public void setNavigationBarDrawable(Drawable drawable) {
    }

    @Override
    public void setNavigationBarColor(int navigationBarColor) {
    }

    @Override
    public void setNavigationBarColorRes(int navigationBarColorRes) {
    }

    @Override
    public void statusBarFontStyle(int statusBarFontStyle) {
    }

    @Override
    public void navigationBarStyle(int navigationBarStyle) {
    }
}
