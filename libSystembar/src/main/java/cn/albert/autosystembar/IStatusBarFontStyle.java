package cn.albert.autosystembar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static cn.albert.autosystembar.Utils.isAfterMIUI_7_7_13;
import static cn.albert.autosystembar.Utils.isFlymeOS;

/**
 * Created by albert on 2017/9/26.
 *
 */

interface IStatusBarFontStyle {

    boolean statusBarFontStyle(Activity activity, boolean darkFont);
    boolean verify();

    class Base implements IStatusBarFontStyle {

        @Override
        public boolean statusBarFontStyle(Activity activity, boolean darkFont) {
            return false;
        }

        @Override
        public boolean verify() {
            return true;
        }
    }


    class Meizu implements IStatusBarFontStyle {

        @Override
        public boolean statusBarFontStyle(Activity activity, boolean darkFont) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (darkFont) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                return true;
            } catch (Exception ignored) {
            }
            return false;
        }

        @Override
        public boolean verify() {
            return isFlymeOS();
        }
    }

    class MIUI implements IStatusBarFontStyle {

        @Override
        public boolean statusBarFontStyle(Activity activity, boolean darkFont) {
            Window window = activity.getWindow();
            Class<?> clazz = window.getClass();
            try {
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                int darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (darkFont) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);
                }
                return true;
            } catch (Exception ignored) {
            }
            return false;
        }

        @Override
        public boolean verify() {
            return isAfterMIUI_7_7_13();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    class M extends Base {

        @Override
        public boolean statusBarFontStyle(Activity activity, boolean darkFont) {
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            if(darkFont){
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }else {
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            return true;
        }

        @Override
        public boolean verify() {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
        }
    }
}
