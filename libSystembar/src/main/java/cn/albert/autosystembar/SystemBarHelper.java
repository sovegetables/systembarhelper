package cn.albert.autosystembar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by albert on 2017/9/27.
 *
 */

@SuppressWarnings("unused")
public class SystemBarHelper {

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

    private static final List<IStatusBar> STATUS_BARS = new ArrayList<>();

    private static final List<INavigationBar> NAVIGATION_BARS = new ArrayList<>();

    private static final List<IStatusBarFontStyle> STATUS_BAR_FONT_STYLES = new ArrayList<>();

    private static final List<INavigationBarStyle> NAVIGATION_BAR_STYLES = new ArrayList<>();

    static {
        STATUS_BARS.add(new IStatusBar.EMUI3_1());
        STATUS_BARS.add(new IStatusBar.Lollipop());
        STATUS_BARS.add(new IStatusBar.Kitkat());
        STATUS_BARS.add(new IStatusBar.Base());
        Collections.unmodifiableCollection(STATUS_BARS);

        NAVIGATION_BARS.add(new INavigationBar.Lollipop());
        NAVIGATION_BARS.add(new INavigationBar.Kitkat());
        NAVIGATION_BARS.add(new INavigationBar.Base());
        Collections.unmodifiableCollection(NAVIGATION_BARS);

        STATUS_BAR_FONT_STYLES.add(new IStatusBarFontStyle.Meizu());
        STATUS_BAR_FONT_STYLES.add(new IStatusBarFontStyle.MIUI());
        STATUS_BAR_FONT_STYLES.add(new IStatusBarFontStyle.M());
        STATUS_BAR_FONT_STYLES.add(new IStatusBarFontStyle.Base());
        Collections.unmodifiableCollection(STATUS_BAR_FONT_STYLES);

        NAVIGATION_BAR_STYLES.add(new INavigationBarStyle.O());
        NAVIGATION_BAR_STYLES.add(new INavigationBarStyle.Base());
        Collections.unmodifiableCollection(NAVIGATION_BAR_STYLES);
    }

    private SystemBarHelper(Builder builder){
        mBuilder = builder;
    }

    public void setStatusBarColor(@ColorInt int statusBarColor) {
        if(mBuilder.mIsImmersedStatusBar){
            final InternalLayout internalLayout = mBuilder.mInternalLayout;
            if(internalLayout != null){
                internalLayout.setStatusBarColor(statusBarColor);
            }
        }else {
            mBuilder.mActivity.getWindow().setStatusBarColor(statusBarColor);
        }
    }

    public void setStatusBarDrawable(Drawable drawable){
        if(mBuilder.mIsImmersedStatusBar){
            final InternalLayout internalLayout = mBuilder.mInternalLayout;
            if(internalLayout != null){
                internalLayout.setStatusBarDrawable(drawable);
            }
        }else {
            //非沉浸式不支持Drawable
        }

    }

    public void enableImmersedStatusBar(boolean immersed){
        if(mBuilder.mIsImmersedStatusBar){
            final InternalLayout internalLayout = mBuilder.mInternalLayout;
            if(internalLayout != null){
                internalLayout.enableImmersedStatusBar(immersed);
            }
        }
    }

    public void enableImmersedNavigationBar(boolean immersed){
        if(mBuilder.mIsImmersedNavigationBar){
            final InternalLayout internalLayout = mBuilder.mInternalLayout;
            if(internalLayout != null){
                internalLayout.enableImmersedNavigationBar(immersed);
            }
        }
    }

    public void setNavigationBarDrawable(Drawable drawable){
        if(mBuilder.mIsImmersedNavigationBar){
            final InternalLayout internalLayout = mBuilder.mInternalLayout;
            if(internalLayout != null){
                internalLayout.setNavigationDrawable(drawable);
            }
        }
    }

    public void setNavigationBarColor(@ColorInt int navigationBarColor) {
        if(mBuilder.mIsImmersedNavigationBar) {
            final InternalLayout internalLayout = mBuilder.mInternalLayout;
            if(internalLayout != null){
                internalLayout.setNavigationBarColor(navigationBarColor);
            }
        }else {
            mBuilder.mActivity.getWindow().setNavigationBarColor(navigationBarColor);
        }
    }

    public void statusBarFontStyle(@StatusBarFontStyle int statusBarFontStyle) {
        boolean isDarkFont = statusBarFontStyle == STATUS_BAR_DARK_FONT_STYLE;
        for (IStatusBarFontStyle style: STATUS_BAR_FONT_STYLES){
            if(style.verify()){
                style.statusBarFontStyle(mBuilder.mActivity, isDarkFont);
                break;
            }
        }
    }

    public void navigationBarStyle(int navigationBarStyle) {
        boolean isDark = navigationBarStyle == NAVIGATION_BAR_DARK_ICON_STYLE;
        for (INavigationBarStyle style: NAVIGATION_BAR_STYLES){
            if(style.verify()){
                style.navigationStyle(mBuilder.mActivity, isDark);
                break;
            }
        }
    }

    public static class Builder{

        @ColorInt
        private int mStatusBarColor;
        @StatusBarFontStyle private int mStatusBarFontStyle = STATUS_BAR_LIGHT_FONT_STYLE;
        @ColorInt private int mNavigationBarColor;
        private Drawable mStatusBarDrawable;
        private Drawable mNavigationBarDrawable;
        private boolean mIsSetStatusBarColor;
        private boolean mIsSetNavigationBarColor;
        private InternalLayout mInternalLayout;
        private boolean mIsImmersedStatusBar;
        private boolean mIsImmersedNavigationBar;

        private Activity mActivity;
        private boolean mIsAuto = false;

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
            mIsSetStatusBarColor = true;
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
            mIsSetNavigationBarColor = true;
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
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
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
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
            systemBarHelper.statusBarFontStyle(mStatusBarFontStyle);
            systemBarHelper.navigationBarStyle(mNavigationBarStyle);
            if(!isExpandedLayout2StatusBar && mIsSetStatusBarColor){
                systemBarHelper.setStatusBarColor(mStatusBarColor);
            }
            if(!isExpandedLayout2NavigationBar && mIsSetNavigationBarColor){
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
                                mIsSetStatusBarColor = true;
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
                        if(mIsSetNavigationBarColor && isExpandedLayout2NavigationBar){
                            helper.setNavigationBarColor(mNavigationBarColor);
                        }
                        if(mNavigationBarDrawable != null){
                            helper.setNavigationBarDrawable(mNavigationBarDrawable);
                        }
                        if (mIsSetStatusBarColor && isExpandedLayout2StatusBar) {
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
