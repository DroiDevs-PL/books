package pl.droidevs.books.savebook;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;
import dagger.android.AndroidInjection;
import pl.droidevs.books.R;
import pl.droidevs.books.domain.Book;
import pl.droidevs.books.domain.BookId;
import pl.droidevs.books.removebook.RemoveBookDialogFragment;
import pl.droidevs.books.removebook.RemoveBookViewModel;

import static com.bumptech.glide.Priority.HIGH;
import static pl.droidevs.books.Resource.Status.ERROR;
import static pl.droidevs.books.Resource.Status.SUCCESS;

public class SaveBookActivity extends AppCompatActivity implements RemoveBookDialogFragment.OnRemoveListener {

    private static final String BOOK_ID_EXTRA = "book id";
    public static final int RESULT_BOOK_REMOVED = 302;
    public static final int EDIT_BOOK_REQUEST_CODE = 205;

    @BindView(R.id.addBookConstraintLayout)
    ConstraintLayout container;

    @BindView(R.id.coverImageUrlEditText)
    EditText coverUrlEditText;

    @BindView(R.id.coverImageView)
    ImageView coverImageView;

    @BindView(R.id.categorySpinner)
    Spinner categorySpinner;

    @BindView(R.id.titleEditText)
    EditText titleEditText;

    @BindView(R.id.authorEditText)
    EditText authorEditText;

    @BindView(R.id.yearEditText)
    EditText yearEditText;

    @BindView(R.id.publisherEditText)
    EditText publisherEditText;

    @BindView(R.id.descriptionEditText)
    EditText descriptionEditText;

    @BindView(R.id.saveButton)
    Button saveButton;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private SaveBookViewModel saveBookViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_book);

        getWindow().setEnterTransition(new Fade().setDuration(getResources().getInteger(R.integer.animation_base_duration)));

        AndroidInjection.inject(this);
        ButterKnife.bind(this);

        setupSpinner();
        setupViewModel();
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getCategoryNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(adapter);
    }

    private List<String> getCategoryNames() {
        Book.Category[] categories = Book.Category.values();
        List<String> categoryNames = new ArrayList<>();

        for (Book.Category category : categories) {
            categoryNames.add(category.toString());
        }

        return categoryNames;
    }

    private void setupViewModel() {
        saveBookViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(SaveBookViewModel.class);

        if (shouldDisplayEdit()) {
            setupForEdit();
        }
    }

    private void displaySnackBar(int messageResource) {
        Snackbar.make(container, messageResource, Snackbar.LENGTH_SHORT).show();
    }

    private boolean shouldDisplayEdit() {
        return getIntent().hasExtra(BOOK_ID_EXTRA);
    }

    private void setupForEdit() {
        saveBookViewModel.setBookId((BookId) getIntent().getSerializableExtra(BOOK_ID_EXTRA));
        saveBookViewModel.getBook().observe(this, resource -> {
            if (resource == null) return;

            if (resource.getStatus() == SUCCESS) {
                showBook(resource.getData());
            } else if (resource.getStatus() == ERROR) {
                Snackbar.make(container, R.string.no_book, Snackbar.LENGTH_SHORT).show();
                finish();
            }
        });

        saveButton.setText(R.string.edit_book);
    }

    private void showBook(@Nullable final Book book) {
        if (book == null) {
            return;
        }

        titleEditText.setText(book.getTitle());
        authorEditText.setText(book.getAuthor());
        yearEditText.setText(book.getYear());
        publisherEditText.setText(book.getPublisher());
        coverUrlEditText.setText(book.getImageUrl());
        descriptionEditText.setText(book.getDescription());
        categorySpinner.setSelection(book.getCategory().ordinal());

        setDataToViewModel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (shouldDisplayEdit()) {
            getMenuInflater().inflate(R.menu.book_save_menu, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.remove_book_item) {
            RemoveBookDialogFragment removeBookDialogFragment = new RemoveBookDialogFragment();
            removeBookDialogFragment.show(getSupportFragmentManager(), "");
        }

        return super.onOptionsItemSelected(item);
    }

    @OnTextChanged(R.id.coverImageUrlEditText)
    void onCoverUrlChanged() {
        String imageUrl = coverUrlEditText.getText().toString();

        if (saveBookViewModel.isCoverUrlValid(imageUrl)) {
            loadCoverImage(imageUrl);
            saveBookViewModel.setImageUrl(imageUrl);
        }
    }


    void loadCoverImage(String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.ic_launcher)
                        .priority(HIGH))
                .into(coverImageView);
    }

    @OnClick(R.id.saveButton)
    void onSaveButtonClicked() {
        setDataToViewModel();

        if (saveBookViewModel.isDataValid()) {
            saveBookViewModel.saveBook()
                    .observe(this, resource -> {
                        if (resource == null) return;

                        if (resource.getStatus() == SUCCESS) {
                            finish();
                        } else if (resource.getStatus() == ERROR) {
                            displaySnackBar(R.string.saving_book_error);
                        }
                    });
        }
    }

    @OnEditorAction(R.id.descriptionEditText)
    boolean onDescriptionDone() {
        onSaveButtonClicked();

        return true;
    }

    void setDataToViewModel() {
        saveBookViewModel.setTitle(titleEditText.getText().toString());
        saveBookViewModel.setAuthor(authorEditText.getText().toString());
        saveBookViewModel.setYear(yearEditText.getText().toString());
        saveBookViewModel.setPublisher(publisherEditText.getText().toString());
        saveBookViewModel.setDescription(descriptionEditText.getText().toString());
        saveBookViewModel.setCategory(categorySpinner.getSelectedItem().toString());
    }

    @Override
    public void removeChosen() {
        final RemoveBookViewModel removeBookViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(RemoveBookViewModel.class);
        removeBookViewModel.removeBook(saveBookViewModel.createBook());

        setResult(RESULT_BOOK_REMOVED);
        finish();
    }

    public static void startForResult(@NonNull final Activity context, @NonNull final BookId bookId) {
        Intent intent = new Intent(context, SaveBookActivity.class);
        intent.putExtra(SaveBookActivity.BOOK_ID_EXTRA, bookId);

        context.startActivityForResult(intent, EDIT_BOOK_REQUEST_CODE,
                ActivityOptionsCompat.makeSceneTransitionAnimation(context).toBundle());
    }
}
