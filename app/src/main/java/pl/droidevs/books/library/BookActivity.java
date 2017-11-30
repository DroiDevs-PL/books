package pl.droidevs.books.library;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import pl.droidevs.books.R;

public class BookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_book);

        ImageView imageView = (ImageView) findViewById(R.id.albumImageView);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);

//        ColorStateList list = imageView.getImageTintList();
//        int color = list.getDefaultColor();
//        s

//        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//        /*Bitmap*/Drawable drawable = /*(BitmapDrawable)*/ imageView.getDrawable();
//        Bitmap bitmap = drawable.getBitmap();
        /*Palette palette = Palette.from(bitmap).generate();
        Palette.Swatch swatch = palette.getVibrantSwatch();

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);*/
        collapsingToolbarLayout.setContentScrimColor(Color.rgb(85, 119, 120));
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.rgb(42, 75, 76));

//        AverageRGBLoader rgbLoader = new AverageRGBLoader(this);
//        rgbLoader.execute(bitmap);

        /*Bitmap bitmap =*/
        Glide.with(this).load(R.drawable.orient_express).into(imageView);


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.orient_express);
        int i = 0;


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

