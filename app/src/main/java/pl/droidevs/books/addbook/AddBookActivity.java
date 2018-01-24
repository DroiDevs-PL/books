package pl.droidevs.books.addbook;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import dagger.android.AndroidInjection;
import pl.droidevs.books.R;
import pl.droidevs.books.model.Book;

import static com.bumptech.glide.Priority.HIGH;

public class AddBookActivity extends AppCompatActivity {

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

    @BindView(R.id.descriptionEditText)
    EditText descriptionEditText;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    AddBookViewModel addBookViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        AndroidInjection.inject(this);
        ButterKnife.bind(this);

        setupViewModel();
        setupSpinner();
    }

    private void setupViewModel() {
        this.addBookViewModel = ViewModelProviders.of(this, viewModelFactory).get(AddBookViewModel.class);
        this.addBookViewModel.wasAddingSuccessful()
                .observe(this, wasAddingSuccessful -> {
                    if (wasAddingSuccessful) {
                        finish();
                    } else {
                        displaySnackBar(R.string.saving_book_error);
                    }
        });
    }

    private void displaySnackBar(int messageResource) {
        Snackbar.make(container, messageResource, Snackbar.LENGTH_SHORT).show();
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getCategoryNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(adapter);
    }

    private List<String> getCategoryNames() {
        Book.Category[] categories = Book.Category.values();
        List<String> categoryNames = new ArrayList<>();

        for (int i = 0; i < categories.length; i++) {
            categoryNames.add(categories[i].toString());
        }

        return categoryNames;
    }

    @OnTextChanged(R.id.coverImageUrlEditText)
    void onCoverUrlChanged() {
        String imageUrl = coverUrlEditText.getText().toString();

        if (this.addBookViewModel.isCoverUrlValid(imageUrl)) {
            loadCoverImage(imageUrl);
            addBookViewModel.setImageUrl(imageUrl);
        }
    }

    void loadCoverImage(String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_book)
                        .priority(HIGH))
                .into(coverImageView);
    }

    @OnClick(R.id.addButton)
    void onAddButtonClicked() {
        setDataToViewModel();

        if (this.addBookViewModel.isDataValid()) {
           this.addBookViewModel.saveBook();
        } else {
            //TODO would be nice to provide more detailed feedback ex. author should have at least 3 chars
            displaySnackBar(R.string.saving_book_validation_error);
        }
    }

    void setDataToViewModel() {
        this.addBookViewModel.setTitle(this.titleEditText.getText().toString());
        this.addBookViewModel.setAuthor(this.authorEditText.getText().toString());
        this.addBookViewModel.setDescription(this.descriptionEditText.getText().toString());
        this.addBookViewModel.setCategory(this.categorySpinner.getSelectedItem().toString());
    }
}
