package pl.droidevs.books.library;

import android.content.SearchRecentSuggestionsProvider;

public final class LibrarySearchSuggestionProvider extends SearchRecentSuggestionsProvider {
    static final String AUTHORITY = "pl.droidevs.books.library.LibrarySearchSuggestionProvider";
    static final int MODE = DATABASE_MODE_QUERIES;

    public LibrarySearchSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

}
