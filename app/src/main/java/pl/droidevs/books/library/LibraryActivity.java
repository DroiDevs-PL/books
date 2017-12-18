package pl.droidevs.books.library;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.AndroidInjection;
import pl.droidevs.books.R;
import pl.droidevs.books.model.Book;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LibraryActivity extends AppCompatActivity {
    private static final String EXTRAS_BOOK_ID = "EXTRAS_BOOK_ID";
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";

    @BindView(R.id.layout_books)
    RecyclerView recyclerView;

    @BindView(R.id.progress_books)
    ProgressBar progressBar;

    @BindView(R.id.button_add_book)
    FloatingActionButton floatingActionButton;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private LibraryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        AndroidInjection.inject(this);
        ButterKnife.bind(this);

        setupAdapter();
        setupRecyclerView();
        setupViewModel();

        floatingActionButton.setOnClickListener(view -> {
            // TODO: Start Add book activity
        });

        progressBar.setVisibility(VISIBLE);
    }

    private void setupAdapter() {
        adapter = new LibraryAdapter();
        adapter.setItemClickListener((view, bookId) -> {
            // TODO: Start details activity and pass the book id
//            ImageView imageView=(ImageView)view.findViewById(R.id.iv_book);
//            int imageId = imageView.getTag();
            Intent intent = new Intent(this, BookActivity.class);
            Bundle extras = new Bundle();
            extras.putString(EXTRAS_BOOK_ID, bookId);
            intent.putExtra(BUNDLE_EXTRAS, extras);

            /*getWindow().setEnterTransition(new Fade(Fade.IN));
            getWindow().setExitTransition(new Fade(Fade.OUT));*/

            //ToDo: Chek whats wrong with positioning back items
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    new Pair<>(view.findViewById(R.id.iv_book), getString(R.string.transition_image)),
                    new Pair<>(view.findViewById(R.id.tv_book_title), getString(R.string.transition_title)),
                    new Pair<>(view.findViewById(R.id.tv_book_author), getString(R.string.transition_author)),
                    new Pair<>(view.findViewById(R.id.shadow_view), getString(R.string.transition_shadow))
            );

            ActivityCompat.startActivity(this, intent, options.toBundle());
        });
    }

    private void setupRecyclerView() {
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void setupViewModel() {
        final LibraryViewModel libraryViewModel = ViewModelProviders.of(this, viewModelFactory).get(LibraryViewModel.class);
        libraryViewModel.getBooks().observe(this, books -> {
            progressBar.setVisibility(GONE);
            if (books != null) {
                adapter.setItems(books);
            }
        });
    }
}
