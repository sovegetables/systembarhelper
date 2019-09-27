package cn.albert.autosystembar;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Created by albert on 2017/9/26.
 */

public class SystemBarView extends View{

    private int type;

    private static final int TYPE_STATUS_BAR = 1;
    private static final int TYPE_NAVIGATION_BAR = 2;

    public SystemBarView(Context context) {
        super(context);
        init(context, null);
    }

    public SystemBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SystemBarView);
        type = attributes.getInt(R.styleable.SystemBarView_type, 1);
        attributes.recycle();
        Utils.initializeStatusBar(this);
        Utils.initializeNavigationBar(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(type == TYPE_STATUS_BAR){
                setMeasuredDimension(getResources().getDisplayMetrics().widthPixels, Utils.sStatusBarHeight);
            }else if( type == TYPE_NAVIGATION_BAR){
                setMeasuredDimension(getResources().getDisplayMetrics().widthPixels, Utils.sNavigationBarHeight);
            }
        }else {
            setMeasuredDimension(0, 0);
            setVisibility(GONE);
        }
    }
}
