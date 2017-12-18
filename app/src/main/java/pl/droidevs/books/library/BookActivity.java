package pl.droidevs.books.library;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
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
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;


import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import pl.droidevs.books.R;
import pl.droidevs.books.model.Book;

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

    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.shadow_view)
    View mShadowView;

    @BindView(R.id.author_tv)
    TextView authorTextView;

    @BindView(R.id.category_tv)
    TextView categoryTextView;

    @BindView(R.id.description_tv)
    TextView descryptionTextView;
    //endregion

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_book);

        AndroidInjection.inject(this);
        ButterKnife.bind(this);


        if (savedInstanceState != null) {
            bookId = savedInstanceState.getString(EXTRAS_BOOK_ID);
        } else {
            final Bundle extras = getIntent().getBundleExtra(BUNDLE_EXTRAS);
            bookId = extras.getString(EXTRAS_BOOK_ID);
        }
        setupViewModel();
    }

    private Palette.Swatch getDominantColor(Bitmap bitmap) {
        List<Palette.Swatch> swatchesTemp = Palette.from(bitmap).generate().getSwatches();
        List<Palette.Swatch> swatches = new ArrayList<Palette.Swatch>(swatchesTemp);
        Collections.sort(swatches, (swatch1, swatch2) -> swatch2.getPopulation() - swatch1.getPopulation());

        return swatches.size() > 0 ? swatches.get(0) : null;
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(BookViewModel.class);
        viewModel.getBooks().observe(this, books -> {
            Book selectedBook = null;
            if (books != null) {
                for (Book book : books) {
                    String thisId = book.getId().getId();
                    if (thisId.equals(bookId)) {
                        selectedBook = book;
                        break;
                    }
                }
            }

            if (selectedBook != null) {
                collapsingToolbarLayout.setTitle(selectedBook.getTitle());
                authorTextView.setText(selectedBook.getAuthor());
                categoryTextView.setText(selectedBook.getCategory().toString());
                descryptionTextView.setText(selectedBook.getDescription());

                BitmapImageViewTarget target =Glide.with(BookActivity.this).asBitmap().load(selectedBook.getImageUrl()).into(new BitmapImageViewTarget(mImageView){
                    @Override
                    public void onResourceReady(Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        super.onResourceReady(resource, transition);

                        Palette.Swatch swatch = getDominantColor(resource);
                        if (swatch != null) {
                            float[] hsl = swatch.getHsl();
                            float actionBarLight = hsl[2];
                            float statusBarLight;
                            if(actionBarLight > 0.7f){
                                actionBarLight = 0.7f;
                            }
                            if(actionBarLight > 0.075f){
                                statusBarLight =actionBarLight - 0.075f;
                            }else{
                                statusBarLight  = actionBarLight;
                                actionBarLight += 0.075f;
                            }

                            float[] actionBarHsl = {hsl[0], hsl[1], actionBarLight};
                            int actionBarColor = ColorUtils.HSLToColor(actionBarHsl);
                            collapsingToolbarLayout.setContentScrimColor(actionBarColor);

                            float[] statusBarHsl = {hsl[0], hsl[1], statusBarLight};
                            int statusBarColor = ColorUtils.HSLToColor(statusBarHsl);
                            Window window = getWindow();
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.setStatusBarColor(statusBarColor);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRAS_BOOK_ID, bookId);
    }
}

