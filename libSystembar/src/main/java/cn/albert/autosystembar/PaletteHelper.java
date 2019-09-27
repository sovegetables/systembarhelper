package cn.albert.autosystembar;

import android.graphics.Bitmap;
import android.graphics.Rect;

import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by albert on 2017/9/27.
 *
 */

class PaletteHelper {

    private Palette.Builder mBuilder;
    private boolean mIsCanceled;
    private float[] mTemp = new float[3];
    private static final float BLACK_MAX_LIGHTNESS = 0.05f;
    private static final float WHITE_MIN_LIGHTNESS = 0.95f;
    private static final float MIDDLE_LIGHTNESS = 0.50f;
    private Rect mRect;
    private Bitmap mBitmap;

    private static final Palette.Filter FILTER = new Palette.Filter() {

        @Override
        public boolean isAllowed(int rgb, float[] hsl) {
            return true;
        }
    };

    public PaletteHelper(@NonNull Bitmap bitmap, Rect rect){
        if (bitmap.isRecycled()) {
            throw new IllegalArgumentException("Bitmap is not valid");
        }
        mRect = rect;
        mBitmap = bitmap;
        mBuilder = new Palette.Builder(bitmap)
                .addFilter(FILTER)
                .setRegion(rect.left, rect.top, rect.right, rect.bottom);
    }

    Model findCloseColorWithSync(){
        return findCloseColorModel(mBuilder.generate());
    }

    void findCloseColorWithAsync(final OnPaletteCallback onPaletteCallback){

        mBuilder.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Model model = findCloseColorModel(palette);
                if(!mIsCanceled && onPaletteCallback != null) {
                    onPaletteCallback.onSuccess(model);
                }
            }
        });
    }

    private Model findCloseColorModel(Palette palette) {
        List<Palette.Swatch> swatches = new ArrayList<>(palette.getSwatches());
        Palette.Swatch populationSwatch = null;
        if(!swatches.isEmpty()) {
            // get the max population Palette.Swatch
            Collections.sort(swatches, new Comparator<Palette.Swatch>() {
                @Override
                public int compare(Palette.Swatch lhs, Palette.Swatch rhs) {
                    if (lhs == null && rhs != null) {
                        return 1;
                    } else if (lhs != null && rhs == null) {
                        return -1;
                    } else if (lhs == null) {
                        return 0;
                    } else {
                        return rhs.getPopulation() - lhs.getPopulation();
                    }
                }
            });
            populationSwatch = swatches.get(0);
        }

        boolean isDarkStyle = false;
        int color = -1;
        if(populationSwatch == null) {
            try {
                // bitmap is get close to full black or white
                color = mBitmap.getPixel(mRect.right / 2, mRect.bottom / 2);
                isDarkStyle = isDarkStyle(color);
            } catch (Exception ignore) {
            }
        }else {
            color = populationSwatch.getRgb();
            isDarkStyle = isDarkStyle(color);
        }
        return new Model(color, isDarkStyle);
    }

    private boolean isDarkStyle(int color) {
        boolean isDarkStyle = false;
        ColorUtils.colorToHSL(color, mTemp);
        if (mTemp[2] <= BLACK_MAX_LIGHTNESS) {
            isDarkStyle = false;
        } else if (mTemp[2] >= WHITE_MIN_LIGHTNESS) {
            isDarkStyle = true;
        }
        return isDarkStyle;
    }

    static class Model{
        int color;
        boolean isDarkStyle;

        Model(int color, boolean isDarkStyle) {
            this.color = color;
            this.isDarkStyle = isDarkStyle;
        }
    }

    interface OnPaletteCallback{
        void onSuccess(Model model);
    }

    public void cancel(){
        mIsCanceled = true;
    }
}
