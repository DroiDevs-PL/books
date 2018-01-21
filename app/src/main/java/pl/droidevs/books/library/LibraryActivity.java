package pl.droidevs.books.library;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import pl.droidevs.books.R;
import pl.droidevs.books.savebook.SaveBookActivity;
import pl.droidevs.books.exportimport.ExportFailedException;
import pl.droidevs.books.exportimport.ExportImportViewModel;
import pl.droidevs.books.model.Book;
import pl.droidevs.books.ui.SwipeCallback;

public class LibraryActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_SAVE_FILE_CODE = 501;

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

        floatingActionButton.setOnClickListener(view -> {
            startActivity(new Intent(this, SaveBookActivity.class));
        });

        progressBar.setVisibility(VISIBLE);
    }

    private void setupAdapter() {
        adapter = new LibraryAdapter();
        adapter.setItemClickListener(bookId -> {
            // TODO: Start details activity and pass the book id
        });
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
        Snackbar.make(floatingActionButton, R.string.book_delete_snackbar, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, v -> adapter.addItem(book, position))
                .addCallback(
                        new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                super.onDismissed(transientBottomBar, event);
                                libraryViewModel.removeBook(book);
                            }
                        })
                .show();
    }

    private void setupViewModel() {
        libraryViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(LibraryViewModel.class);

        libraryViewModel.getBooks().observe(this, books -> {
            progressBar.setVisibility(GONE);

            if (books != null) {
                adapter.setItems(books);
            }
        });

        libraryViewModel.wasAnError().observe(this, error ->
                Snackbar.make(floatingActionButton, error, Snackbar.LENGTH_LONG)
                        .show());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
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
                new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_PERMISSION_SAVE_FILE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_PERMISSION_SAVE_FILE_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportOptionSelected();
            } else {
                displayMessage(R.string.message_permission_denied_cant_export);
            }
        }
    }
}
