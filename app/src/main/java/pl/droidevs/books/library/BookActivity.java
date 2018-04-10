package pl.droidevs.books.library;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import pl.droidevs.books.R;
import pl.droidevs.books.animation.TransitionNameProvider;
import pl.droidevs.books.domain.Book;
import pl.droidevs.books.domain.BookId;
import pl.droidevs.books.savebook.SaveBookActivity;

import static android.text.TextUtils.isEmpty;
import static pl.droidevs.books.Resource.Status.SUCCESS;
import static pl.droidevs.books.savebook.SaveBookActivity.EDIT_BOOK_REQUEST_CODE;
import static pl.droidevs.books.savebook.SaveBookActivity.RESULT_BOOK_REMOVED;

public class BookActivity extends AppCompatActivity {
    private static final String EXTRAS_BOOK_ID = "EXTRAS_BOOK_ID";

    private static final int BOOK_REQUEST = 711;

    private BookId bookId;

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.album_iv)
    ImageView albumImageView;

    @BindView(R.id.author_tv)
    TextView authorTextView;

    @BindView(R.id.year_tv)
    TextView yearTextView;

    @BindView(R.id.publisher_tv)
    TextView publisherTextView;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.category_tv)
    TextView categoryTextView;

    @BindView(R.id.description_tv)
    TextView descriptionTextView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        AndroidInjection.inject(this);
        ButterKnife.bind(this);

        setupActionBar();
        setupBookId(savedInstanceState);
        setupTransitionNames();
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupBookId(Bundle savedInstanceState) {
        if (savedInstanceState != null)
            bookId = (BookId) savedInstanceState.getSerializable(EXTRAS_BOOK_ID);
        else bookId = (BookId) getIntent().getSerializableExtra(EXTRAS_BOOK_ID);
    }

    private void setupTransitionNames() {
        final TransitionNameProvider nameProvider = new TransitionNameProvider(bookId);
        albumImageView.setTransitionName(nameProvider.getImageTransition());
        authorTextView.setTransitionName(nameProvider.getAuthorTransition());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupViewModel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_details_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.edit_book) {
            SaveBookActivity.startForResult(this, bookId);
            return true;
        } else if (android.R.id.home == item.getItemId()) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_BOOK_REQUEST_CODE && resultCode == RESULT_BOOK_REMOVED) {
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRAS_BOOK_ID, bookId);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);

        super.onBackPressed();
    }

    private void setupViewModel() {
        final BookViewModel viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(BookViewModel.class);

        viewModel.getBook(bookId)
                .observe(this, resource -> {
                    if (resource == null) return;

                    if (SUCCESS == resource.getStatus()) {
                        populateView(resource.getData());
                    }
                });
    }

    private void populateView(Book book) {
        if (book == null) return;

        authorTextView.setText(book.getAuthor());
        yearTextView.setText(book.getYear());
        publisherTextView.setText(book.getPublisher());
        categoryTextView.setText(book.getCategory().toString());
        ratingBar.setRating(book.getRating());
        descriptionTextView.setText(book.getDescription());

        loadCover(book.getImageUrl());
        setTitle(book.getTitle());
    }

    private void setTitle(String title) {
        collapsingToolbar.setTitle(title);
    }

    private void loadCover(String imageUrl) {
        if (isEmpty(imageUrl)) return;

        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(albumImageView);
    }

    public static void startForResult(final Activity context, final BookId bookId, final ActivityOptionsCompat options) {
        final Intent intent = new Intent(context, BookActivity.class);
        intent.putExtra(EXTRAS_BOOK_ID, bookId);

        ActivityCompat.startActivityForResult(context, intent, BOOK_REQUEST, options.toBundle());
    }
}
