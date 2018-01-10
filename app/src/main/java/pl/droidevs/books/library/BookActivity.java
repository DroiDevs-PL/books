package pl.droidevs.books.library;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

import static pl.droidevs.books.AppHelper.getColorsForBarsFromBitmap;

public class BookActivity extends AppCompatActivity {
    private static final String EXTRAS_BOOK_ID = "EXTRAS_BOOK_ID";
    private static final String EXTRAS_IMAGE_TRANSITION_NAME = "EXTRAS_IMAGE_TRANSITION_NAME";
    private static final String EXTRAS_TITLE_TRANSITION_NAME = "EXTRAS_TITLE_TRANSITION_NAME";
    private static final String EXTRAS_AUTHOR_TRANSITION_NAME = "EXTRAS_AUTHOR_TRANSITION_NAME";
    private static final String EXTRAS_SHADOW_TRANSITION_NAME = "EXTRAS_SHADOW_TRANSITION_NAME";
    private static final String EXTRAS_LAST_SELECTED_INDEX = "EXTRAS_LAST_SELECTED_INDEX";
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private BookViewModel viewModel;

    private String bookId;
    private String imageTransitionName;
    private String titleTransitionName;
    private String authorTransitionName;
    private String shadowTransitionName;
    private int lastSelectedIndex;

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
            imageTransitionName = savedInstanceState.getString(EXTRAS_IMAGE_TRANSITION_NAME);
            titleTransitionName = savedInstanceState.getString(EXTRAS_TITLE_TRANSITION_NAME);
            authorTransitionName = savedInstanceState.getString(EXTRAS_AUTHOR_TRANSITION_NAME);
            shadowTransitionName = savedInstanceState.getString(EXTRAS_SHADOW_TRANSITION_NAME);
            lastSelectedIndex = savedInstanceState.getInt(EXTRAS_LAST_SELECTED_INDEX);
        } else {
            final Bundle extras = getIntent().getBundleExtra(BUNDLE_EXTRAS);
            bookId = extras.getString(EXTRAS_BOOK_ID);
            imageTransitionName = extras.getString(EXTRAS_IMAGE_TRANSITION_NAME);
            titleTransitionName = extras.getString(EXTRAS_TITLE_TRANSITION_NAME);
            authorTransitionName = extras.getString(EXTRAS_AUTHOR_TRANSITION_NAME);
            shadowTransitionName = extras.getString(EXTRAS_SHADOW_TRANSITION_NAME);
            lastSelectedIndex = extras.getInt(EXTRAS_LAST_SELECTED_INDEX);
        }
        setupViewModel();

        imageView.setTransitionName(imageTransitionName);
        //ToDo: title transitionName
        authorTextView.setTransitionName(authorTransitionName);
        shadowView.setTransitionName(shadowTransitionName);
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(BookViewModel.class);
        /*viewModel.getBooks().observe(this, books -> {
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

                Glide.with(BookActivity.this).asBitmap().load(selectedBook.getImageUrl()).into(new BitmapImageViewTarget(imageView) {
                    @Override
                    public void onResourceReady(Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        super.onResourceReady(resource, transition);

                        int[] colors = getColorsForBarsFromBitmap(resource);
                        if (colors != null) {
                            collapsingToolbarLayout.setContentScrimColor(colors[0]);
                            Window window = getWindow();
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.setStatusBarColor(colors[1]);
                        }
                    }
                });
            }
        });*/
        viewModel.getBookById(bookId).observe(this,book -> {
            collapsingToolbarLayout.setTitle(book.getTitle());
            authorTextView.setText(book.getAuthor());
            categoryTextView.setText(book.getCategory().toString());
            descryptionTextView.setText(book.getDescription());

            Glide.with(BookActivity.this).asBitmap().load(book.getImageUrl()).into(new BitmapImageViewTarget(imageView) {
                @Override
                public void onResourceReady(Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    super.onResourceReady(resource, transition);

                    int[] colors = getColorsForBarsFromBitmap(resource);
                    if (colors != null) {
                        collapsingToolbarLayout.setContentScrimColor(colors[0]);
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(colors[1]);
                    }
                }
            });
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRAS_BOOK_ID, bookId);
        outState.putString(EXTRAS_IMAGE_TRANSITION_NAME, imageTransitionName);
        outState.putString(EXTRAS_TITLE_TRANSITION_NAME, titleTransitionName);
        outState.putString(EXTRAS_AUTHOR_TRANSITION_NAME, authorTransitionName);
        outState.putString(EXTRAS_SHADOW_TRANSITION_NAME, shadowTransitionName);
        outState.putInt(EXTRAS_LAST_SELECTED_INDEX, lastSelectedIndex);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(EXTRAS_IMAGE_TRANSITION_NAME, imageTransitionName);
        intent.putExtra(EXTRAS_TITLE_TRANSITION_NAME, titleTransitionName);
        intent.putExtra(EXTRAS_AUTHOR_TRANSITION_NAME, authorTransitionName);
        intent.putExtra(EXTRAS_SHADOW_TRANSITION_NAME, shadowTransitionName);
        intent.putExtra(EXTRAS_LAST_SELECTED_INDEX, lastSelectedIndex);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}

