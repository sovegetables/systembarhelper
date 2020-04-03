package cn.albert.autosystembar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by albert on 2017/9/26.
 *
 */

class Utils {

    private static final String TAG = "Utils";
    private static final String KEY_DISPLAY = "ro.build.display.id";

    private static boolean sIsInitializeStatusBar;
    static int sStatusBarHeight;
    private static boolean sIsInitializeNavigationBar;
    static int sNavigationBarHeight;

    static boolean hasNavigationBar(Context context){
        boolean hasNavigationBar = false;
        boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        if(!hasMenuKey && !hasBackKey) {
            hasNavigationBar = true;
        }
        return hasNavigationBar;
    }

    private static int getStatusBarHeight(Resources resources){
        int  result = 0;
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private static int getNavigationBarHeight(Resources resources){
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    static void initializeStatusBar(View view) {
        if (sIsInitializeStatusBar) {
            return;
        }
        sIsInitializeStatusBar = true;
        Context context = view.getContext();
        try {
            sStatusBarHeight = getStatusBarHeight(context.getResources());
            if(sStatusBarHeight <= 0){
                throw new SystemBarErrorException();
            }
        } catch (Throwable e) {
            try {
                Class<?> clazz = Class.forName("com.android.internal.R$dimen");
                Field field = clazz.getField("status_bar_height");
                int x = Integer.parseInt(field.get(clazz.newInstance()).toString());
                sStatusBarHeight = context.getResources().getDimensionPixelSize(x);
            } catch (Throwable e1) {
                Rect outRect = new Rect();
                View decorView = getParentView(view);
                decorView.getWindowVisibleDisplayFrame(outRect);
                sStatusBarHeight = outRect.top;
            }
        }
    }

    private static class SystemBarErrorException extends Exception{
    }

    private static View getParentView(View view) {
        ViewParent viewParent = view.getParent();
        if (viewParent != null) {
            return getParentView((View) viewParent);
        }
        return view;
    }

    static void initializeNavigationBar(View view) {
        if (sIsInitializeNavigationBar) {
            return;
        }
        sIsInitializeNavigationBar = true;
        final Context context = view.getContext();
        try {
            sNavigationBarHeight = getNavigationBarHeight(context.getResources());
            if(sStatusBarHeight <= 0 && hasNavigationBar(context)){
                throw new SystemBarErrorException();
            }
        } catch (Exception e) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            Class<?> displayClass = display.getClass();
            Method method;
            try {
                method = displayClass.getMethod("getRealMetrics", DisplayMetrics.class);
                method.invoke(display, displayMetrics);
                sNavigationBarHeight = displayMetrics.heightPixels - getDisplayHeight(display);
            } catch (Exception ignored) {
            }

        }
    }

    private static int getDisplayHeight(Display display) {
        Point point = new Point();
        display.getSize(point);
        return point.y;
    }


    private static final String KEY_MIUI_VERSION = "ro.build.version.incremental";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_EMUI_VERSION_NAME = "ro.build.version.emui";

    private static String getSystemProperty(String key, String defaultValue) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getMethod("get", String.class, String.class);
            return (String) get.invoke(clz, key, defaultValue);
        } catch (Exception ignored) {
        }
        return defaultValue;
    }


    static boolean isEMUI3_1() {
        String property = getSystemProperty(KEY_EMUI_VERSION_NAME, "");
        return !TextUtils.isEmpty(property) && ("EmotionUI 3".equals(property) || property.contains("EmotionUI_3.1"));
    }

    static boolean isMIUI() {
        String property = getSystemProperty(KEY_MIUI_VERSION_NAME, "");
        return !TextUtils.isEmpty(property);
    }

    static boolean isAfterMIUI_7_7_13(){
        if(!isMIUI()){
            return false;
        }
        String property = getSystemProperty(KEY_MIUI_VERSION, "");
        boolean flag;
        try {
            String[] str = property.split(".");
            int tempVersion = Integer.valueOf(str[0]);
            if(tempVersion > 7){
                flag =  true;
            }else if (tempVersion == 7) {
                tempVersion = Integer.valueOf(str[1]);
                flag =  tempVersion > 7 || tempVersion == 7 && Integer.valueOf(str[2]) >= 13;
            }else {
                flag =  false;
            }
        }catch (Exception ignored){
            flag = false;
        }
        return flag;
    }

    static boolean isFlymeOS() {
        String property = getSystemProperty(KEY_DISPLAY, "");
        return !TextUtils.isEmpty(property) && property.toLowerCase().contains("flyme");
    }
}
