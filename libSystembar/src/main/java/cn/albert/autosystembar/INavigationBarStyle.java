package cn.albert.autosystembar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;

public interface INavigationBarStyle {
    boolean navigationStyle(Activity activity, boolean darkStyle);
    boolean verify();

    class Base implements INavigationBarStyle{
        @Override
        public boolean navigationStyle(Activity activity, boolean darkStyle) {
            return false;
        }

        @Override
        public boolean verify() {
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    class O implements INavigationBarStyle{

        @Override
        public boolean navigationStyle(Activity activity, boolean darkStyle) {
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            if(darkStyle){
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            }else {
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            }
            return true;
        }

        @Override
        public boolean verify() {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
        }
    }
}
