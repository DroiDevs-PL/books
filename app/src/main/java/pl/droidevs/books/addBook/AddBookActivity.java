package pl.droidevs.books.addBook;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import dagger.android.AndroidInjection;
import pl.droidevs.books.R;

public class AddBookActivity extends AppCompatActivity {

    @BindView(R.id.coverImageUrlEditText)
    EditText coverUrlEditText;

    @BindView(R.id.coverImageView)
    ImageView coverImageView;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        AndroidInjection.inject(this);
        ButterKnife.bind(this);

        setupViewModel();
    }

    private void setupViewModel() {
        final AddBookViewModel addBookViewModel = ViewModelProviders.of(this, viewModelFactory).get(AddBookViewModel.class);
    }

    @OnTextChanged(R.id.coverImageUrlEditText)
    void onCoverUrlChanged() {
        Glide.with(this)
                .load(coverUrlEditText.getText().toString())
                .into(coverImageView);
    }
}
