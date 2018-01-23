package pl.droidevs.books.di;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.droidevs.books.removebook.RemoveBookDialogFragment;
import pl.droidevs.books.savebook.SaveBookActivity;
import pl.droidevs.books.library.LibraryActivity;
import pl.droidevs.books.login.LoginActivity;

/**
 * Module for all sub-components within the app.
 */

@Module
public abstract class BuildersModule {
    @ContributesAndroidInjector
    abstract LibraryActivity bindLibraryActivity();

    @ContributesAndroidInjector
    abstract LoginActivity bindLoginActivity();

    @ContributesAndroidInjector
    abstract SaveBookActivity bindSaveBookActivity();

    // Bindings for other sub-components belong here
}
