package pl.droidevs.books.library;

import android.app.Activity;
import android.content.Loader;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.security.PKCS12Attribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.droidevs.books.R;

public class BookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_book);

        ImageView imageView = (ImageView) findViewById(R.id.albumImageView);
        Glide.with(this).load(R.drawable.orient_express).into(imageView);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);

        Resources reso = this.getResources();
        Bitmap image = BitmapFactory.decodeResource(reso, R.drawable.orient_express);

        Palette.Swatch swatch = getDominantColor(image);
        if (swatch != null) {
            float[] hsl = swatch.getHsl();
            float light = hsl[2];
            float light2 = light > 0.075f ? light - 0.075f : 0f;
            float[] hsl2 = {hsl[0], hsl[1], light2};
            int color = ColorUtils.HSLToColor(hsl2);

            collapsingToolbarLayout.setContentScrimColor(swatch.getRgb());
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    public Palette.Swatch getDominantColor(Bitmap bitmap) {
        List<Palette.Swatch> swatchesTemp = Palette.from(bitmap).generate().getSwatches();
        List<Palette.Swatch> swatches = new ArrayList<Palette.Swatch>(swatchesTemp);
        Collections.sort(swatches, new Comparator<Palette.Swatch>() {
            @Override
            public int compare(Palette.Swatch swatch1, Palette.Swatch swatch2) {
                return swatch2.getPopulation() - swatch1.getPopulation();
            }
        });


        return swatches.size() > 0 ? swatches.get(0) : null;
    }

    public class AverageRGBLoader extends AsyncTask<Bitmap, Void, Integer> {

        private Activity mActivity;

        public AverageRGBLoader(Activity activity) {
            mActivity = activity;
        }

        @Override
        protected Integer doInBackground(Bitmap... bitmaps) {
            Bitmap bitmap = bitmaps.clone()[0];

            int redColors = 0;
            int greenColors = 0;
            int blueColors = 0;
            int pixelCount = 0;

            for (int y = 0; y < bitmap.getHeight(); y++) {
                for (int x = 0; x < bitmap.getWidth(); x++) {
                    int c = bitmap.getPixel(x, y);
                    pixelCount++;
                    redColors += Color.red(c);
                    greenColors += Color.green(c);
                    blueColors += Color.blue(c);
                }
            }
            // calculate average of bitmap r,g,b values
            int red = (redColors / pixelCount);
            int green = (greenColors / pixelCount);
            int blue = (blueColors / pixelCount);

            int color = Color.rgb(red, green, blue);
            return color;
        }
    }
}

