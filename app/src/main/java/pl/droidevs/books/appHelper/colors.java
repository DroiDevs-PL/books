package pl.droidevs.books.appHelper;

import android.graphics.Bitmap;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.graphics.Palette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by micha on 18.12.2017.
 */

public class colors {

    public static final int HSL_HUE = 0;
    public static final int HSL_SATURATION = 1;
    public static final int HSL_LIGHT = 2;

    private static final float MAX_ACTION_BAR_LIGHT = 0.7f;
    private static final float MIN_ACTION_BAR_COLOR = 0.075f;

    public static Palette.Swatch getDominantColor(Bitmap bitmap) {
        List<Palette.Swatch> swatchesTemp = Palette.from(bitmap).generate().getSwatches();
        List<Palette.Swatch> swatches = new ArrayList<>(swatchesTemp);
        Collections.sort(swatches, (swatch1, swatch2) -> swatch2.getPopulation() - swatch1.getPopulation());

        return swatches.size() > 0 ? swatches.get(0) : null;
    }

    public static int[] getColorsForBarsFromSwatch(Palette.Swatch swatch) {
        if (swatch != null) {
            float[] hsl = swatch.getHsl();
            float actionBarLight = hsl[HSL_LIGHT];
            float statusBarLight;
            if (actionBarLight > MAX_ACTION_BAR_LIGHT) {
                actionBarLight = MAX_ACTION_BAR_LIGHT;
            }
            if (actionBarLight > MIN_ACTION_BAR_COLOR) {
                statusBarLight = actionBarLight - MIN_ACTION_BAR_COLOR;
            } else {
                statusBarLight = actionBarLight;
                actionBarLight += MIN_ACTION_BAR_COLOR;
            }

            float[] actionBarHsl = {hsl[HSL_HUE], hsl[HSL_SATURATION], actionBarLight};
            int actionBarColor = ColorUtils.HSLToColor(actionBarHsl);

            float[] statusBarHsl = {hsl[HSL_HUE], hsl[HSL_SATURATION], statusBarLight};
            int statusBarColor = ColorUtils.HSLToColor(statusBarHsl);

            int[] colors = new int[]{actionBarColor, statusBarColor};
            return colors;
        }
        return null;
    }

    public static int[] getColorsForBarsFromBitmap(Bitmap bitmap) {
        Palette.Swatch swatch = getDominantColor(bitmap);
        if (swatch != null) {
            return getColorsForBarsFromSwatch(swatch);
        }
        return null;
    }
}
