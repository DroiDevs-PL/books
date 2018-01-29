package pl.droidevs.books.apphelper;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.graphics.Palette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by micha on 18.12.2017.
 */

public class ColorHelper {

    private static final int HSL_HUE = 0;
    private static final int HSL_SATURATION = 1;
    private static final int HSL_LIGHT = 2;

    private static final float MAX_ACTION_BAR_LIGHT = 0.7f;
    private static final float MIN_ACTION_BAR_COLOR = 0.075f;

    public static Palette.Swatch getDominantColor(Bitmap bitmap) {
        List<Palette.Swatch> swatchesTemp = Palette.from(bitmap).generate().getSwatches();
        List<Palette.Swatch> swatches = new ArrayList<>(swatchesTemp);
        Collections.sort(swatches, (swatch1, swatch2) -> swatch2.getPopulation() - swatch1.getPopulation());

        return swatches.size() > 0 ? swatches.get(0) : null;
    }

    public static int getActionBarColorFromSwatch(@NonNull Palette.Swatch swatch) {
        float[] swatchHsl = swatch.getHsl();
        float actionBarLight = swatchHsl[HSL_LIGHT];

        if (actionBarLight > MAX_ACTION_BAR_LIGHT) {
            actionBarLight = MAX_ACTION_BAR_LIGHT;
        }
        if (actionBarLight <= MIN_ACTION_BAR_COLOR) {
            actionBarLight += MIN_ACTION_BAR_COLOR;
        }

        float[] actionBarHsl = {swatchHsl[HSL_HUE], swatchHsl[HSL_SATURATION], actionBarLight};

        return ColorUtils.HSLToColor(actionBarHsl);
    }

    public static int getStatusBarColorFromSwatch(@NonNull Palette.Swatch swatch) {
        float[] swatchHsl = swatch.getHsl();
        float actionBarLight = swatchHsl[HSL_LIGHT];
        float statusBarLight;

        if (actionBarLight > MIN_ACTION_BAR_COLOR) {
            statusBarLight = actionBarLight - MIN_ACTION_BAR_COLOR;
        } else {
            statusBarLight = actionBarLight;
        }

        float[] statusBarHsl = {swatchHsl[HSL_HUE], swatchHsl[HSL_SATURATION], statusBarLight};

        return ColorUtils.HSLToColor(statusBarHsl);
    }
}
