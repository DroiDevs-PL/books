package pl.droidevs.books.addBook;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import pl.droidevs.books.R;

public class AddBookActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_add_book);

        setupViewModel();
    }

    private void setupViewModel() {
        final AddBookViewModel addBookViewModel = ViewModelProviders.of(this, viewModelFactory).get(AddBookViewModel.class);
    }
}
