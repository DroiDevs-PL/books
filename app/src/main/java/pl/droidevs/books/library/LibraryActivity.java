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
import android.support.annotation.StringRes;
import android.support.design.widget.BaseTransientBottomBar;
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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import pl.droidevs.books.R;
import pl.droidevs.books.Resource;
import pl.droidevs.books.domain.Book;
import pl.droidevs.books.domain.BookId;
import pl.droidevs.books.removebook.RemoveBookViewModel;
import pl.droidevs.books.savebook.SaveBookActivity;
import pl.droidevs.books.ui.SwipeCallback;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static pl.droidevs.books.Resource.Status.ERROR;
import static pl.droidevs.books.Resource.Status.LOADING;
import static pl.droidevs.books.Resource.Status.SUCCESS;
import static pl.droidevs.books.library.BookActivity.BUNDLE_EXTRAS;
import static pl.droidevs.books.library.BookActivity.EXTRAS_AUTHOR_TRANSITION_NAME;
import static pl.droidevs.books.library.BookActivity.EXTRAS_BOOK_ID;
import static pl.droidevs.books.library.BookActivity.EXTRAS_IMAGE_TRANSITION_NAME;
import static pl.droidevs.books.library.BookActivity.EXTRAS_LAST_SELECTED_INDEX;
import static pl.droidevs.books.library.BookActivity.EXTRAS_SHARED_AUTHOR_TEXT_SIZE;
import static pl.droidevs.books.library.BookActivity.EXTRAS_SHARED_TITLE_TEXT_SIZE;
import static pl.droidevs.books.library.BookActivity.EXTRAS_TITLE_TRANSITION_NAME;

public class LibraryActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_SAVE_FILE_CODE = 501;
    private static final int REQUEST_PERMISSION_READ_FILE_CODE = 502;
    private static final int BOOK_REQUEST = 711;

    private SearchView searchView;

    @BindView(R.id.layout_library_content)
    View contentLayout;

    @BindView(R.id.layout_books)
    RecyclerView recyclerView;

    @BindView(R.id.tv_no_content)
    TextView noBooks;

    @BindView(R.id.layout_progress)
    View progressBar;

    @BindView(R.id.button_add_book)
    FloatingActionButton floatingActionButton;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private LibraryAdapter adapter;
    private LibraryViewModel libraryViewModel;

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

        showProgress();
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
        adapter.setItemClickListener((view, bookId, index) -> ActivityCompat.startActivityForResult(this,
                createBookIntent(view, index, bookId),
                BOOK_REQUEST,
                createBookActivityOptions(view).toBundle()));
    }

    private Intent createBookIntent(View view, int index, BookId bookId) {
        Intent intent = new Intent(this, BookActivity.class);
        intent.putExtra(BUNDLE_EXTRAS, createAnimationBundle(view, index));
        intent.putExtra(EXTRAS_BOOK_ID, bookId);

        return intent;
    }

    private Bundle createAnimationBundle(View view, int index) {
        TextView tvTitle = view.findViewById(R.id.tv_book_title);
        TextView tvAuthor = view.findViewById(R.id.tv_book_author);

        Bundle animationBundle = new Bundle();
        animationBundle.putString(EXTRAS_IMAGE_TRANSITION_NAME, view.findViewById(R.id.iv_book).getTransitionName());
        animationBundle.putString(EXTRAS_TITLE_TRANSITION_NAME, view.findViewById(R.id.tv_book_title).getTransitionName());
        animationBundle.putString(EXTRAS_AUTHOR_TRANSITION_NAME, view.findViewById(R.id.tv_book_author).getTransitionName());
        animationBundle.putInt(EXTRAS_LAST_SELECTED_INDEX, index);
        animationBundle.putFloat(EXTRAS_SHARED_TITLE_TEXT_SIZE, tvTitle.getTextSize());
        animationBundle.putFloat(EXTRAS_SHARED_AUTHOR_TEXT_SIZE, tvAuthor.getTextSize());

        return animationBundle;
    }

    private ActivityOptionsCompat createBookActivityOptions(View view) {
        return ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                new Pair<>(view.findViewById(R.id.iv_book), view.findViewById(R.id.iv_book).getTransitionName()),
                new Pair<>(view.findViewById(R.id.tv_book_title), view.findViewById(R.id.tv_book_title).getTransitionName()),
                new Pair<>(view.findViewById(R.id.tv_book_author), view.findViewById(R.id.tv_book_author).getTransitionName())
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
        final RemoveBookViewModel removeBookViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(RemoveBookViewModel.class);

        removeBookViewModel.removeBook(book)
                .observe(this, resource -> {
                            if (resource != null && resource.getStatus() == ERROR) {
                                Snackbar.make(floatingActionButton, R.string.remove_book_error, Snackbar.LENGTH_LONG).show();
                            }
                        }
                );
    }

    private void setupViewModel() {
        libraryViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(LibraryViewModel.class);

        libraryViewModel.getBooks().observe(this, this::processResponse);
    }

    private void processResponse(final Resource<Collection<Book>> resource) {
        if (LOADING == resource.getStatus()) showProgress();
        else hideProgress();

        if (ERROR == resource.getStatus()) showErrorMessage(resource.getError());
        else if (SUCCESS == resource.getStatus()) showBooks(resource.getData());
    }

    private void showProgress() {
        progressBar.setVisibility(VISIBLE);
        contentLayout.setVisibility(GONE);
    }

    private void hideProgress() {
        progressBar.setVisibility(GONE);
        contentLayout.setVisibility(VISIBLE);
    }

    private void showErrorMessage(final Throwable error) {
        // You should map a throwable to error message here
        displayMessage(R.string.error_fetching_failed);
    }

    private void showBooks(final Collection<Book> data) {
        adapter.setItems(data);

        noBooks.setText(libraryViewModel.isQuery() ? R.string.search_no_result : R.string.empty_library);
        noBooks.setVisibility(data.isEmpty() ? VISIBLE : GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.library_menu, menu);
        setupSearchView(menu.findItem(R.id.search_item));

        return true;
    }

    private void setupSearchView(final MenuItem searchItem) {
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryRefinementEnabled(true);
        searchView.setOnQueryTextListener(new LibraryQueryTextListener(libraryViewModel));
        searchView.setOnCloseListener(() -> {
            libraryViewModel.clearQuery();

            return false;
        });

        final String currentQuery = libraryViewModel.getQuery();
        if (!TextUtils.isEmpty(currentQuery)) {
            searchItem.expandActionView();
            searchView.setQuery(currentQuery, true);
            searchView.clearFocus();
        }
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

        if (item.getItemId() == R.id.import_item) {
            importOptionSelected();

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
        final LibraryTransferViewModel libraryTransferViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(LibraryTransferViewModel.class);

        libraryTransferViewModel.exportBooks().observe(this, resource ->
                handleBookTransfer(resource, R.string.message_export_successful, R.string.error_export_failed));
    }

    private void displayMessage(int messageResourceId) {
        Snackbar.make(contentLayout, messageResourceId, Snackbar.LENGTH_SHORT)
                .show();
    }

    private void requestWriteStoragePermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_PERMISSION_SAVE_FILE_CODE);
    }

    void importOptionSelected() {
        if (hasReadStoragePermission()) {
            importLibrary();
        } else {
            requestReadStoragePermissions();
        }
    }

    private boolean hasReadStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void importLibrary() {
        final LibraryTransferViewModel libraryTransferViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(LibraryTransferViewModel.class);

        libraryTransferViewModel.importBooks().observe(this, resource ->
                handleBookTransfer(resource, R.string.message_import_successful, R.string.error_import_failed));
    }

    private void handleBookTransfer(final Resource<Void> resource, @StringRes int successMessageId, @StringRes int errorMessageId) {
        if (LOADING == resource.getStatus()) showProgress();
        else hideProgress();

        if (SUCCESS == resource.getStatus()) displayMessage(successMessageId);
        else if (ERROR == resource.getStatus()) displayMessage(errorMessageId);
    }

    private void requestReadStoragePermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_PERMISSION_READ_FILE_CODE);
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

        if (requestCode == REQUEST_PERMISSION_READ_FILE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                importOptionSelected();
            } else {
                displayMessage(R.string.message_permission_denied_cant_import);
            }
        }
    }

    private static class LibraryQueryTextListener implements SearchView.OnQueryTextListener {
        private final LibraryViewModel libraryViewModel;

        private LibraryQueryTextListener(@NonNull final LibraryViewModel libraryViewModel) {
            this.libraryViewModel = libraryViewModel;
        }

        @Override
        public boolean onQueryTextSubmit(final String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            libraryViewModel.setQuery(newText);

            return false;
        }
    }
}
