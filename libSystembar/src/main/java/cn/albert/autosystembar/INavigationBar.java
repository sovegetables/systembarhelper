package cn.albert.autosystembar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by albert on 2017/9/27.
 *
 */

interface INavigationBar {

    boolean expandLayoutToNavigationBar(Activity activity);
    boolean verify();

    class Base implements INavigationBar {

        @Override
        public boolean expandLayoutToNavigationBar(Activity activity) {
            return false;
        }

        @Override
        public boolean verify() {
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    class Kitkat extends Base{

        @Override
        public boolean expandLayoutToNavigationBar(Activity activity) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            return true;
        }

        @Override
        public boolean verify() {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    class Lollipop extends Kitkat {

        @Override
        public boolean expandLayoutToNavigationBar(Activity activity) {
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(Color.TRANSPARENT);
            return true;
        }

        @Override
        public boolean verify() {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
        }
    }
}
