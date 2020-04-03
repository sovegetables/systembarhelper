package cn.albert.autosystembar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IntDef;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by albert on 2017/9/27.
 *
 */

@SuppressWarnings("unused")
public class SystemBarHelper implements SystemBarCompact{

    private static final String TAG = "SystemBarHelper";
    private Builder mBuilder;

    public static final int STATUS_BAR_DARK_FONT_STYLE = 2;
    public static final int STATUS_BAR_LIGHT_FONT_STYLE = 1;
    @IntDef({STATUS_BAR_DARK_FONT_STYLE, STATUS_BAR_LIGHT_FONT_STYLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StatusBarFontStyle {}

    public static final int NAVIGATION_BAR_DARK_ICON_STYLE = 2;
    public static final int NAVIGATION_BAR_LIGHT_ICON_STYLE = 1;
    @IntDef({NAVIGATION_BAR_DARK_ICON_STYLE, NAVIGATION_BAR_LIGHT_ICON_STYLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NavigationBarIconStyle {}

    static final List<IStatusBar> STATUS_BARS = new ArrayList<>();

    static final List<INavigationBar> NAVIGATION_BARS = new ArrayList<>();

    static final List<IStatusBarFontStyle> STATUS_BAR_FONT_STYLES = new ArrayList<>();

    static final List<INavigationBarStyle> NAVIGATION_BAR_STYLES = new ArrayList<>();

    static {
        STATUS_BARS.add(new IStatusBar.EMUI3_1());
        STATUS_BARS.add(new IStatusBar.Lollipop());
        STATUS_BARS.add(new IStatusBar.Kitkat());
        STATUS_BARS.add(new IStatusBar.Base());

        NAVIGATION_BARS.add(new INavigationBar.Lollipop());
        NAVIGATION_BARS.add(new INavigationBar.Kitkat());
        NAVIGATION_BARS.add(new INavigationBar.Base());

        STATUS_BAR_FONT_STYLES.add(new IStatusBarFontStyle.Meizu());
        STATUS_BAR_FONT_STYLES.add(new IStatusBarFontStyle.MIUI());
        STATUS_BAR_FONT_STYLES.add(new IStatusBarFontStyle.M());
        STATUS_BAR_FONT_STYLES.add(new IStatusBarFontStyle.Base());

        NAVIGATION_BAR_STYLES.add(new INavigationBarStyle.O());
        NAVIGATION_BAR_STYLES.add(new INavigationBarStyle.Base());
    }

    private final SystemBarCompact mSystemBarCompact;

    private SystemBarHelper(Builder builder){
        mBuilder = builder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            mSystemBarCompact = new Lollipop(builder);
        }else {
            mSystemBarCompact = new Base(builder);
        }
    }

    public void setStatusBarColor(@ColorInt int statusBarColor) {
        mSystemBarCompact.setStatusBarColor(statusBarColor);
    }

    @Override
    public void setStatusBarColorRes(@ColorRes int statusBarColorRes) {
        mSystemBarCompact.setStatusBarColorRes(statusBarColorRes);
    }

    public void setStatusBarDrawable(Drawable drawable){
        mSystemBarCompact.setStatusBarDrawable(drawable);
    }

    public void enableImmersedStatusBar(boolean immersed){
        mSystemBarCompact.enableImmersedStatusBar(immersed);
    }

    public void enableImmersedNavigationBar(boolean immersed){
        mSystemBarCompact.enableImmersedNavigationBar(immersed);
    }

    public void setNavigationBarDrawable(Drawable drawable){
        mSystemBarCompact.setNavigationBarDrawable(drawable);
    }

    public void setNavigationBarColor(@ColorInt int navigationBarColor) {
        mSystemBarCompact.setNavigationBarColor(navigationBarColor);
    }

    @Override
    public void setNavigationBarColorRes(@ColorRes int navigationBarColorRes) {
        mSystemBarCompact.setNavigationBarColorRes(navigationBarColorRes);
    }

    public void statusBarFontStyle(@StatusBarFontStyle int statusBarFontStyle) {
        mSystemBarCompact.statusBarFontStyle(statusBarFontStyle);
    }

    public void navigationBarStyle(@NavigationBarIconStyle int navigationBarStyle) {
        mSystemBarCompact.navigationBarStyle(navigationBarStyle);
    }

    public static class Builder{

        static final int NOT_SET = -1;

        @ColorInt
        int mStatusBarColor = NOT_SET;
        boolean mIsStatusBarColorSet = false;
        @StatusBarFontStyle private int mStatusBarFontStyle = STATUS_BAR_LIGHT_FONT_STYLE;
        @ColorInt
        private int mNavigationBarColor = NOT_SET;
        boolean mIsNavigationBarColorSet = false;
        Drawable mStatusBarDrawable;
        Drawable mNavigationBarDrawable;
        InternalLayout mInternalLayout;
        boolean mIsImmersedStatusBar;
        boolean mIsImmersedNavigationBar;

        Activity mActivity;
        boolean mIsAuto = false;

        private static final int PADDING = 10;
        private static final int ACTION_BAR_DEFAULT_HEIGHT = 48; // dp

        @NavigationBarIconStyle
        private int mNavigationBarStyle = NAVIGATION_BAR_LIGHT_ICON_STYLE;

        public Builder enableAutoSystemBar(boolean isAuto){
            mIsAuto = isAuto;
            return this;
        }

        public Builder statusBarDrawable(Drawable drawable){
            mStatusBarDrawable = drawable;
            return this;
        }

        public Builder statusBarColor(@ColorInt int statusBarColor){
            mIsStatusBarColorSet = true;
            mStatusBarColor = statusBarColor;
            return this;
        }

        public Builder enableImmersedStatusBar(boolean enable){
            mIsImmersedStatusBar = enable;
            return this;
        }

        public Builder enableImmersedNavigationBar(boolean enable){
            mIsImmersedNavigationBar = enable;
            return this;
        }

        public Builder navigationBarColor(@ColorInt int navigationBarColor){
            mIsNavigationBarColorSet = true;
            mNavigationBarColor = navigationBarColor;
            return this;
        }

        public Builder navigationBarDrawable(Drawable drawable){
            mNavigationBarDrawable = drawable;
            return this;
        }

        public Builder statusBarFontStyle(@StatusBarFontStyle int style){
            mStatusBarFontStyle = style;
            return this;
        }

        public Builder navigationBarStyle(@NavigationBarIconStyle int style){
            mNavigationBarStyle = style;
            return this;
        }

        public SystemBarHelper into(Activity activity){
            mActivity = activity;
            SystemBarHelper systemBarHelper = new SystemBarHelper(this);
            boolean isExpandedLayout2StatusBar = false;
            if(mIsImmersedStatusBar){
                for (IStatusBar statusBar: STATUS_BARS){
                    if(statusBar.verify()){
                        isExpandedLayout2StatusBar = statusBar.expandLayoutToStatusBar(activity);
                        break;
                    }
                }
            }else {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                }
            }
            boolean isExpandedLayout2NavigationBar = false;
            if(mIsImmersedNavigationBar){
                for (INavigationBar navigationBar: NAVIGATION_BARS){
                    if(navigationBar.verify()){
                        isExpandedLayout2NavigationBar = navigationBar.expandLayoutToNavigationBar(activity);
                        break;
                    }
                }
            }else {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                }
            }
            systemBarHelper.statusBarFontStyle(mStatusBarFontStyle);
            systemBarHelper.navigationBarStyle(mNavigationBarStyle);
            if(!isExpandedLayout2StatusBar && mIsStatusBarColorSet){
                systemBarHelper.setStatusBarColor(mStatusBarColor);
            }
            if(!isExpandedLayout2NavigationBar && mIsNavigationBarColorSet){
                systemBarHelper.setNavigationBarColor(mNavigationBarColor);
            }
            if(isExpandedLayout2StatusBar || isExpandedLayout2NavigationBar){
                //沉浸StatusBar或者NavigationBar
                initialize(activity, systemBarHelper, isExpandedLayout2StatusBar, isExpandedLayout2NavigationBar);
            }
            return systemBarHelper;
        }

        private void initialize(final Activity activity, final SystemBarHelper helper,
                                final boolean isExpandedLayout2StatusBar, final boolean isExpandedLayout2NavigationBar) {
            Window window = activity.getWindow();
            final View decorView = window.getDecorView();
            final View androidContent = window.getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
            if(androidContent instanceof ViewGroup){
                androidContent.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        if(mIsAuto) {
                            decorView.setDrawingCacheEnabled(true);
                            final Bitmap bitmap = Bitmap.createBitmap(decorView.getDrawingCache());
                            decorView.setDrawingCacheEnabled(false);
                            if (bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
                                int top = Utils.sStatusBarHeight + PADDING;
                                int bottom = (int) (top + ACTION_BAR_DEFAULT_HEIGHT * decorView.getResources().getDisplayMetrics().density);
                                Rect rect = new Rect(0, top, bitmap.getWidth(), bottom);
                                PaletteHelper paletteHelper = new PaletteHelper(bitmap, rect);
                                PaletteHelper.Model model = paletteHelper.findCloseColorWithSync();
                                mStatusBarColor = model.color;
                                mStatusBarFontStyle = model.isDarkStyle ? STATUS_BAR_DARK_FONT_STYLE : STATUS_BAR_LIGHT_FONT_STYLE;
                                helper.statusBarFontStyle(mStatusBarFontStyle);
                            }
                        }
                        final ViewGroup realContent = ((ViewGroup) androidContent);
                        View content = realContent.getChildAt(0);
                        insertContentView(realContent, content, activity);
                        if(mInternalLayout != null){
                            if(isExpandedLayout2StatusBar){
                                mInternalLayout.setStatusBarVisibility(true);
                            }
                            if(isExpandedLayout2NavigationBar){
                                mInternalLayout.setNavigationBarVisibility(true);
                            }
                        }
                        helper.enableImmersedStatusBar(mIsImmersedStatusBar);
                        helper.enableImmersedNavigationBar(mIsImmersedNavigationBar);
                        if(mIsNavigationBarColorSet && isExpandedLayout2NavigationBar){
                            helper.setNavigationBarColor(mNavigationBarColor);
                        }
                        if(mNavigationBarDrawable != null){
                            helper.setNavigationBarDrawable(mNavigationBarDrawable);
                        }
                        if (mIsStatusBarColorSet && isExpandedLayout2StatusBar) {
                            helper.setStatusBarColor(mStatusBarColor);
                        }
                        if (mStatusBarDrawable != null) {
                            helper.setStatusBarDrawable(mStatusBarDrawable);
                        }
                        androidContent.getViewTreeObserver().removeOnPreDrawListener(this);
                        return true;
                    }
                });
            }
        }

        private void insertContentView(ViewGroup realContent, View content, Activity activity) {
            DrawerLayout drawerLayout  = findDrawerLayout(realContent);
            if(drawerLayout != null){
                ViewCompat.requestApplyInsets(realContent);
                ViewCompat.setOnApplyWindowInsetsListener(realContent, new OnApplyWindowInsetsListener() {
                    @Override
                    public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                        return insets.consumeSystemWindowInsets();
                    }
                });
                InternalLayout newContent = new InternalLayout(activity);
                View originContent = drawerLayout.getChildAt(0);
                View originDrawer = drawerLayout.getChildAt(1);
                drawerLayout.removeView(originContent);
                newContent.setStatusBarVisibility(true);
                newContent.setContentView(originContent);
                drawerLayout.addView(newContent, 0);
                mInternalLayout = newContent;
            }else {
                realContent.removeView(content);
                InternalLayout layout = new InternalLayout(activity);
                layout.setContentView(content);
                realContent.addView(layout);
                mInternalLayout = layout;
            }
        }

        private DrawerLayout findDrawerLayout(ViewGroup parent){
            if(parent instanceof DrawerLayout){
                return ((DrawerLayout) parent);
            }
            int childCount = parent.getChildCount();
            View child ;
            for (int i = 0; i < childCount; i++){
                child = parent.getChildAt(i);
                if(child instanceof DrawerLayout){
                    return ((DrawerLayout) child);
                }
                if(child instanceof  ViewGroup){
                    DrawerLayout drawer = findDrawerLayout(((ViewGroup) child));
                    if(drawer != null){
                        return drawer;
                    }
                }
            }
            return null;
        }



    }


}
