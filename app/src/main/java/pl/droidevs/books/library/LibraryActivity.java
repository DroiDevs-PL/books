package pl.droidevs.books.library;

import android.Manifest;
import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import pl.droidevs.books.R;
import pl.droidevs.books.exportimport.ExportFailedException;
import pl.droidevs.books.exportimport.ExportImportViewModel;
import pl.droidevs.books.model.Book;
import pl.droidevs.books.model.BookId;
import pl.droidevs.books.removebook.RemoveBookViewModel;
import pl.droidevs.books.savebook.SaveBookActivity;
import pl.droidevs.books.ui.SwipeCallback;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static pl.droidevs.books.library.BookActivity.BUNDLE_EXTRAS;
import static pl.droidevs.books.library.BookActivity.EXTRAS_AUTHOR_TRANSITION_NAME;
import static pl.droidevs.books.library.BookActivity.EXTRAS_BOOK_ID;
import static pl.droidevs.books.library.BookActivity.EXTRAS_IMAGE_TRANSITION_NAME;
import static pl.droidevs.books.library.BookActivity.EXTRAS_LAST_SELECTED_INDEX;
import static pl.droidevs.books.library.BookActivity.EXTRAS_TITLE_TRANSITION_NAME;

public class LibraryActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_SAVE_FILE_CODE = 501;
    private static final int BOOK_REQUEST = 711;

    private SearchView searchView;

    @BindView(R.id.layout_library_content)
    CoordinatorLayout coordinatorLayout;

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

        floatingActionButton.setOnClickListener(
                view -> startActivity(new Intent(this, SaveBookActivity.class), createSaveActivityOptions().toBundle()));

        progressBar.setVisibility(VISIBLE);
    }

    private ActivityOptionsCompat createSaveActivityOptions() {
        return ActivityOptionsCompat.makeSceneTransitionAnimation(this);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);

        setupAnimationForRecyclerViewItems(data.getBundleExtra(BUNDLE_EXTRAS));
    }

    private void setupAnimationForRecyclerViewItems(Bundle bundle) {
        int lastSelectedIndex = bundle.getInt(EXTRAS_LAST_SELECTED_INDEX, -1);

        if (lastSelectedIndex >= 0) {
            recyclerView.smoothScrollToPosition(lastSelectedIndex);
            setExitSharedElementCallback(getExitSharedElementCallback(bundle, lastSelectedIndex));
        }
    }

    private SharedElementCallback getExitSharedElementCallback(Bundle bundle, int positionToScroll) {
        return new SharedElementCallback() {

            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                LibraryAdapter.BookViewHolder viewHolder = (LibraryAdapter.BookViewHolder) recyclerView.findViewHolderForAdapterPosition(positionToScroll);

                if (viewHolder != null) {
                    sharedElements.put(bundle.getString(EXTRAS_IMAGE_TRANSITION_NAME), viewHolder.ivBook);
                    sharedElements.put(bundle.getString(EXTRAS_TITLE_TRANSITION_NAME), viewHolder.tvBookTitle);
                    sharedElements.put(bundle.getString(EXTRAS_AUTHOR_TRANSITION_NAME), viewHolder.tvBookAuthor);
                }
            }
        };
    }

    private void setupAdapter() {
        adapter = new LibraryAdapter();
        adapter.setItemClickListener((view, bookId, index) -> {

            ActivityCompat.startActivityForResult(this,
                    createBookIntent(view, index, bookId),
                    BOOK_REQUEST,
                    createBookActivityOptions(view).toBundle());
        });
    }

    private Intent createBookIntent(View view, int index, BookId bookId) {
        Intent intent = new Intent(this, BookActivity.class);
        intent.putExtra(BUNDLE_EXTRAS, createAnimationBundle(view, index));
        intent.putExtra(EXTRAS_BOOK_ID, bookId);

        return intent;
    }

    private Bundle createAnimationBundle(View view, int index) {
        Bundle animationBundle = new Bundle();
        animationBundle.putString(EXTRAS_IMAGE_TRANSITION_NAME, view.findViewById(R.id.iv_book).getTransitionName());
        animationBundle.putString(EXTRAS_TITLE_TRANSITION_NAME, view.findViewById(R.id.tv_book_title).getTransitionName());
        animationBundle.putString(EXTRAS_AUTHOR_TRANSITION_NAME, view.findViewById(R.id.tv_book_author).getTransitionName());
        animationBundle.putInt(EXTRAS_LAST_SELECTED_INDEX, index);

        return animationBundle;
    }

    private ActivityOptionsCompat createBookActivityOptions(View view) {
        return ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                new Pair<>(view.findViewById(R.id.iv_book),
                        view.findViewById(R.id.iv_book).getTransitionName()),
                new Pair<>(view.findViewById(R.id.tv_book_title),
                        view.findViewById(R.id.tv_book_title).getTransitionName()),
                new Pair<>(view.findViewById(R.id.tv_book_author),
                        view.findViewById(R.id.tv_book_author).getTransitionName())
        );
    }

    private void setupRecyclerView() {
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        setItemSwipe();
    }

    private void setItemSwipe() {
        SwipeCallback swipeCallback = new SwipeCallback() {
            @Override
            public void onSwiped(int position) {
                adapter.removeItem(position,
                        book -> showRemoveBookSnackbar(book, position));
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void showRemoveBookSnackbar(@NonNull Book book, int position) {
        Snackbar.make(floatingActionButton, R.string.removing_book_snackbar, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, v -> adapter.addItem(book, position))
                .addCallback(
                        new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                super.onDismissed(transientBottomBar, event);

                                removeBook(book);
                            }
                        })
                .show();
    }

    private void removeBook(Book book) {
        RemoveBookViewModel removeBookViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(RemoveBookViewModel.class);

        removeBookViewModel.wasAnError()
                .observe(this, error ->
                        Snackbar.make(floatingActionButton, error, Snackbar.LENGTH_LONG)
                                .show());

        removeBookViewModel.removeBook(book);
    }


    private void setupViewModel() {
        LibraryViewModel libraryViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(LibraryViewModel.class);

        libraryViewModel.getBooks().observe(this, books -> {
            progressBar.setVisibility(GONE);

            if (books != null) {
                adapter.setItems(books);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.library_menu, menu);

        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search_item).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryRefinementEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO: Build a filter and send a request
                Log.v(LibraryActivity.class.getName(), query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO: Go for suggestions
                return false;
            }
        });
        searchView.setOnCloseListener(() -> {
            // TODO: Clear the filter and send the request
            return false;
        });

        return true;
    }

    @Override
    public void onBackPressed() {
        searchView.onActionViewCollapsed();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.export_item) {
            exportOptionSelected();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void exportOptionSelected() {
        if (hasWriteStoragePermission()) {
            exportLibrary();
        } else {
            requestWriteStoragePermissions();
        }
    }

    private boolean hasWriteStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void exportLibrary() {
        final ExportImportViewModel exportImportViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(ExportImportViewModel.class);

        try {
            exportImportViewModel.exportBooks();
            displayMessage(R.string.message_export_successful);
        } catch (ExportFailedException e) {
            displayMessage(R.string.message_export_not_successful);
        }
    }

    private void displayMessage(int messageResourceId) {
        Snackbar.make(coordinatorLayout, messageResourceId, Snackbar.LENGTH_SHORT)
                .show();
    }

    private void requestWriteStoragePermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_PERMISSION_SAVE_FILE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_SAVE_FILE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportOptionSelected();
            } else {
                displayMessage(R.string.message_permission_denied_cant_export);
            }
        }
    }
}
