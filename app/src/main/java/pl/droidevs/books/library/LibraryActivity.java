package pl.droidevs.books.library;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.AndroidInjection;
import pl.droidevs.books.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LibraryActivity extends AppCompatActivity {

    @BindView(R.id.layout_books)
    RecyclerView recyclerView;

    @BindView(R.id.progress_books)
    ProgressBar progressBar;

    @BindView(R.id.button_add_book)
    FloatingActionButton floatingActionButton;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        AndroidInjection.inject(this);
        this.unbinder = ButterKnife.bind(this);

        final LibraryAdapter adapter = new LibraryAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        progressBar.setVisibility(VISIBLE);

        floatingActionButton.setOnClickListener(view -> {
            // TODO: Start Add book activity
        });

        final LibraryViewModel libraryViewModel = ViewModelProviders.of(this, viewModelFactory).get(LibraryViewModel.class);
        libraryViewModel.getBooks().observe(this, books -> {
            progressBar.setVisibility(GONE);
            if (books != null)
                adapter.setItems(books);
        });
    }

    @Override
    protected void onDestroy() {
        this.unbinder.unbind();

        super.onDestroy();
    }
}
