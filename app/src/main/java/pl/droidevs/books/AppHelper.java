package pl.droidevs.books;

import android.graphics.Bitmap;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.graphics.Palette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by micha on 18.12.2017.
 */

public class AppHelper {

    public static Palette.Swatch getDominantColor(Bitmap bitmap) {
        List<Palette.Swatch> swatchesTemp = Palette.from(bitmap).generate().getSwatches();
        List<Palette.Swatch> swatches = new ArrayList<>(swatchesTemp);
        Collections.sort(swatches, (swatch1, swatch2) -> swatch2.getPopulation() - swatch1.getPopulation());

        return swatches.size() > 0 ? swatches.get(0) : null;
    }

    public static int[] getColorsForBarsFromSwatch(Palette.Swatch swatch) {
        if (swatch != null) {
            float[] hsl = swatch.getHsl();
            float actionBarLight = hsl[2];
            float statusBarLight;
            if (actionBarLight > 0.7f) {
                actionBarLight = 0.7f;
            }
            if (actionBarLight > 0.075f) {
                statusBarLight = actionBarLight - 0.075f;
            } else {
                statusBarLight = actionBarLight;
                actionBarLight += 0.075f;
            }

            float[] actionBarHsl = {hsl[0], hsl[1], actionBarLight};
            int actionBarColor = ColorUtils.HSLToColor(actionBarHsl);

            float[] statusBarHsl = {hsl[0], hsl[1], statusBarLight};
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
