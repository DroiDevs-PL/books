package pl.droidevs.books.library;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import pl.droidevs.books.R;

public class BookActivity extends AppCompatActivity {
    private static final String EXTRAS_BOOK_ID = "EXTRAS_BOOK_ID";
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private BookViewModel viewModel;

    private String bookId;

    //region Butter binding
    @BindView(R.id.album_image_view)
    ImageView mImageView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.shadow_view)
    View mShadowView;

    @BindView(R.id.author_tv)
    TextView authorTextView;

    @BindView(R.id.category_tv)
    TextView categoryTextView;

    @BindView(R.id.description_tv)
    TextView descryptionTextView;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_book);

        AndroidInjection.inject(this);
        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this).get(BookViewModel.class);

        /*viewModel.getBook().observe(this, book -> {
            if (book == null) {
                mImageView.setImageDrawable(null);
                authorTextView.setText("");
                categoryTextView.setText("");
                descryptionTextView.setText("");
            } else {
                Glide.with(this).load(book.getImageUrl()).into(mImageView);
                authorTextView.setText(book.getAuthor());
                categoryTextView.setText(book.getDescription());
                descryptionTextView.setText(book.getDescription());
            }
        });*/

        TextView titleText = getTitleTextView(mToolbar);
        if (titleText != null)
            titleText.setText("Hello");

        final Bundle extras = getIntent().getBundleExtra(BUNDLE_EXTRAS);
        bookId = extras.getString(EXTRAS_BOOK_ID);


        Glide.with(this).load(R.drawable.orient_express).into(mImageView);
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
        Collections.sort(swatches, (swatch1, swatch2) -> swatch2.getPopulation() - swatch1.getPopulation());

        return swatches.size() > 0 ? swatches.get(0) : null;
    }

    private TextView getTitleTextView(Toolbar toolbar) {
        try {
            Class<?> toolbarClass = Toolbar.class;
            Field titleTextViewField = toolbarClass.getDeclaredField("mTitleTextView");
            titleTextViewField.setAccessible(true);
            TextView titleTextView = (TextView) titleTextViewField.get(toolbar);

            return titleTextView;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}

