package pl.droidevs.books.library;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import pl.droidevs.books.R;
import pl.droidevs.books.model.Book;
import pl.droidevs.books.model.BookId;
import pl.droidevs.books.savebook.SaveBookActivity;

import static pl.droidevs.books.apphelper.ColorHelper.getActionBarColorFromSwatch;
import static pl.droidevs.books.apphelper.ColorHelper.getDominantColor;
import static pl.droidevs.books.apphelper.ColorHelper.getStatusBarColorFromSwatch;

public class BookActivity extends AppCompatActivity {
    public static final String EXTRAS_BOOK_ID = "EXTRAS_BOOK_ID";
    public static final String EXTRAS_IMAGE_TRANSITION_NAME = "EXTRAS_IMAGE_TRANSITION_NAME";
    public static final String EXTRAS_TITLE_TRANSITION_NAME = "EXTRAS_TITLE_TRANSITION_NAME";
    public static final String EXTRAS_AUTHOR_TRANSITION_NAME = "EXTRAS_AUTHOR_TRANSITION_NAME";
    public static final String EXTRAS_SHADOW_TRANSITION_NAME = "EXTRAS_SHADOW_TRANSITION_NAME";
    public static final String EXTRAS_LAST_SELECTED_INDEX = "EXTRAS_LAST_SELECTED_INDEX";
    public static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";

    private static final int EDIT_BOOK_REQUEST_CODE = 205;

    private Bundle animationBundle;
    private BookViewModel viewModel;

    //region Butter binding
    @BindView(R.id.album_iv)
    ImageView imageView;

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.shadow_view)
    View shadowView;

    @BindView(R.id.author_tv)
    TextView authorTextView;

    @BindView(R.id.category_tv)
    TextView categoryTextView;

    @BindView(R.id.description_tv)
    TextView descriptionTextView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    //endregion

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        AndroidInjection.inject(this);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            this.animationBundle = savedInstanceState.getBundle(BUNDLE_EXTRAS);
        } else {
            this.animationBundle = getIntent().getBundleExtra(BUNDLE_EXTRAS);
        }

        setupAnimations();
        setupViewModel();
    }

    void setupAnimations() {
        imageView.setTransitionName(animationBundle.getString(EXTRAS_IMAGE_TRANSITION_NAME));
        //TODO title animation: animationBundle.getString(EXTRAS_TITLE_TRANSITION_NAME);
        authorTextView.setTransitionName(animationBundle.getString(EXTRAS_AUTHOR_TRANSITION_NAME));
        shadowView.setTransitionName(animationBundle.getString(EXTRAS_SHADOW_TRANSITION_NAME));
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(BookViewModel.class);

        viewModel.getBook((BookId) getIntent().getSerializableExtra(EXTRAS_BOOK_ID))
                .observe(this, book -> {

                    if (book != null) {
                        setupBookViews(book);
                    }
                });
    }

    void setupBookViews(Book book) {
        collapsingToolbarLayout.setTitle(book.getTitle());
        authorTextView.setText(book.getAuthor());
        categoryTextView.setText(book.getCategory().toString());
        descriptionTextView.setText(book.getDescription());

        if (book.getImageUrl() != null) {
            loadCover(book.getImageUrl());
        }
    }

    void loadCover(String imageUrl) {
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    public void onResourceReady(Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        super.onResourceReady(resource, transition);

                        setupBarColors(resource);
                    }
                });
    }

    void setupBarColors(Bitmap bitmap) {
        Palette.Swatch swatch = getDominantColor(bitmap);

        if (swatch != null) {
            collapsingToolbarLayout.setContentScrimColor(getActionBarColorFromSwatch(swatch));
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getStatusBarColorFromSwatch(swatch));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.edit_book) {
            Intent intent = new Intent(this, SaveBookActivity.class);
            intent.putExtra(SaveBookActivity.BOOK_ID_EXTRA, getIntent().getSerializableExtra(EXTRAS_BOOK_ID));

            startActivityForResult(intent, EDIT_BOOK_REQUEST_CODE, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_BOOK_REQUEST_CODE &&
                resultCode == SaveBookActivity.RESULT_BOOK_REMOVED) {
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBundle(BUNDLE_EXTRAS, animationBundle);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(BUNDLE_EXTRAS, animationBundle);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}

