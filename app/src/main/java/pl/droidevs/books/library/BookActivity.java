package pl.droidevs.books.library;

import android.animation.ValueAnimator;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.TypedValue;
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
    public static final String EXTRAS_SHARED_TITLE_TEXT_SIZE = "EXTRAS_SHARED_TITLE_TEXT_SIZE";
    public static final String EXTRAS_SHARED_AUTHOR_TEXT_SIZE = "EXTRAS_SHARED_AUTHOR_TEXT_SIZE";
    public static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";

    private Bundle animationBundle;
    private BookViewModel viewModel;

    private BookId bookId;
    private float masterTitleTextSize;
    private float masterAuthorTextSize;

    //region Butter binding
    @BindView(R.id.album_iv)
    ImageView imageView;

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.shadow_view)
    View shadowView;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.author_tv)
    TextView authorTextView;

    @BindView(R.id.category_tv)
    TextView categoryTextView;

    @BindView(R.id.description_tv)
    TextView descriptionTextView;
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
            this.animationBundle = savedInstanceState.getBundle(BUNDLE_EXTRAS);
            this.bookId = (BookId) savedInstanceState.getSerializable(EXTRAS_BOOK_ID);
        } else {
            this.animationBundle = getIntent().getBundleExtra(BUNDLE_EXTRAS);
            this.bookId = (BookId) getIntent().getSerializableExtra(EXTRAS_BOOK_ID);
        }

        setupAnimations();
        setupViewModel();
    }

    void setupAnimations() {
        imageView.setTransitionName(animationBundle.getString(EXTRAS_IMAGE_TRANSITION_NAME));
        tvTitle.setTransitionName(animationBundle.getString(EXTRAS_TITLE_TRANSITION_NAME));
        authorTextView.setTransitionName(animationBundle.getString(EXTRAS_AUTHOR_TRANSITION_NAME));
        shadowView.setTransitionName(animationBundle.getString(EXTRAS_SHADOW_TRANSITION_NAME));
        masterTitleTextSize = animationBundle.getFloat(EXTRAS_SHARED_TITLE_TEXT_SIZE);
        masterAuthorTextSize = animationBundle.getFloat(EXTRAS_SHARED_AUTHOR_TEXT_SIZE);

        getWindow().getSharedElementEnterTransition().addListener(new android.transition.Transition.TransitionListener() {
            private float detailsTitleTextSize = -1;
            private float detailsAuthorTextSize = -1;

            @Override
            public void onTransitionStart(android.transition.Transition transition) {
                detailsAuthorTextSize = authorTextView.getTextSize();
                if (detailsAuthorTextSize >= 0) {
                    authorTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, masterAuthorTextSize);
                    ValueAnimator animator = ValueAnimator.ofFloat(masterAuthorTextSize, detailsAuthorTextSize);
                    animator.setDuration(250);
                    animator.addUpdateListener(valueAnimator -> {
                        float textSize = (float) valueAnimator.getAnimatedValue();
                        authorTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                    });
                    animator.start();
                }
                detailsTitleTextSize = tvTitle.getTextSize();
                if (detailsTitleTextSize >= 0) {
                    tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, masterTitleTextSize);
                    ValueAnimator animator = ValueAnimator.ofFloat(masterTitleTextSize, detailsTitleTextSize);
                    animator.setDuration(250);
                    animator.addUpdateListener(valueAnimator -> {
                        float textSize = (float) valueAnimator.getAnimatedValue();
                        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                    });
                    animator.start();
                }

                tvTitle.setVisibility(View.VISIBLE);
                collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.TransparentText);
                ValueAnimator titleColorAnimator = ValueAnimator.ofArgb(R.color.defaultTextViewTextColor, Color.WHITE);
                titleColorAnimator.setDuration(250);
                titleColorAnimator.addUpdateListener(valueAnimator -> {
                    int color = (int) valueAnimator.getAnimatedValue();
                    tvTitle.setTextColor(color);
                });
                titleColorAnimator.start();
            }

            @Override
            public void onTransitionEnd(android.transition.Transition transition) {
                if (detailsAuthorTextSize >= 0) {
                    authorTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, detailsAuthorTextSize);
                }
                if (detailsTitleTextSize >= 0) {
                    tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, detailsTitleTextSize);
                }

                tvTitle.setVisibility(View.GONE);
                collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
            }

            @Override
            public void onTransitionCancel(android.transition.Transition transition) {

            }

            @Override
            public void onTransitionPause(android.transition.Transition transition) {

            }

            @Override
            public void onTransitionResume(android.transition.Transition transition) {

            }
        });
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(BookViewModel.class);

        viewModel.getBook(bookId)
                .observe(this, book -> {

                    if (book != null) {
                        setupBookViews(book);
                    }
                });
    }

    void setupBookViews(Book book) {
        tvTitle.setText(book.getTitle());
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBundle(BUNDLE_EXTRAS, animationBundle);
        outState.putSerializable(EXTRAS_BOOK_ID, bookId);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(BUNDLE_EXTRAS, animationBundle);
        setResult(RESULT_OK, intent);
        super.onBackPressed();

        getWindow().getSharedElementReturnTransition().addListener(new android.transition.Transition.TransitionListener() {
            private float detailsTitleTextSize = -1;
            private float detailsAuthorTextSize = -1;

            @Override
            public void onTransitionStart(android.transition.Transition transition) {
                detailsAuthorTextSize = authorTextView.getTextSize();
                if (detailsAuthorTextSize >= 0) {
                    authorTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, detailsAuthorTextSize);
                    ValueAnimator animator = ValueAnimator.ofFloat(detailsAuthorTextSize, masterAuthorTextSize);
                    animator.setDuration(250);
                    animator.addUpdateListener(valueAnimator -> {
                        float textSize = (float) valueAnimator.getAnimatedValue();
                        authorTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                    });
                    animator.start();
                }
                detailsTitleTextSize = tvTitle.getTextSize();
                if (detailsTitleTextSize >= 0) {
                    tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, detailsTitleTextSize);
                    ValueAnimator animator = ValueAnimator.ofFloat(detailsTitleTextSize, masterTitleTextSize);
                    animator.setDuration(250);
                    animator.addUpdateListener(valueAnimator -> {
                        float textSize = (float) valueAnimator.getAnimatedValue();
                        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                    });
                    animator.start();
                }

                tvTitle.setVisibility(View.VISIBLE);
                collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.TransparentText);
                int defColor;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    defColor = getResources().getColor(R.color.defaultTextViewTextColor, getTheme());
                } else {
                    defColor = getResources().getColor(R.color.defaultTextViewTextColor);
                }
                ValueAnimator titleColorAnimator = ValueAnimator.ofArgb(Color.WHITE, defColor);
                titleColorAnimator.setDuration(250);
                titleColorAnimator.addUpdateListener(valueAnimator -> {
                    int color = (int) valueAnimator.getAnimatedValue();
                    tvTitle.setTextColor(color);
                });
                titleColorAnimator.start();
            }

            @Override
            public void onTransitionEnd(android.transition.Transition transition) {
                if (detailsAuthorTextSize >= 0) {
                    authorTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, masterAuthorTextSize);
                }
            }

            @Override
            public void onTransitionCancel(android.transition.Transition transition) {

            }

            @Override
            public void onTransitionPause(android.transition.Transition transition) {

            }

            @Override
            public void onTransitionResume(android.transition.Transition transition) {

            }
        });
    }
}

