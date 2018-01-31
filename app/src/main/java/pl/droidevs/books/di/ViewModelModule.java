package pl.droidevs.books.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dagger.Binds;
import dagger.MapKey;
import dagger.Module;
import dagger.multibindings.IntoMap;
import pl.droidevs.books.removebook.RemoveBookViewModel;
import pl.droidevs.books.savebook.SaveBookViewModel;
import pl.droidevs.books.exportimport.ExportImportViewModel;
import pl.droidevs.books.library.BookViewModel;
import pl.droidevs.books.library.LibraryViewModel;
import pl.droidevs.books.login.LoginViewModel;

@Module
public abstract class ViewModelModule {

    @Documented
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @MapKey
    @interface ViewModelKey {
        Class<? extends ViewModel> value();
    }

    @IntoMap
    @Binds
    @ViewModelKey(LibraryViewModel.class)
    abstract ViewModel libraryViewModel(LibraryViewModel viewModel);

    @IntoMap
    @Binds
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel loginViewModel(LoginViewModel viewModel);

    @IntoMap
    @Binds
    @ViewModelKey(ExportImportViewModel.class)
    abstract ViewModel exportImportViewModel(ExportImportViewModel viewModel);

    @IntoMap
    @Binds
    @ViewModelKey(SaveBookViewModel.class)
    abstract ViewModel saveBookViewModel(SaveBookViewModel viewModel);

    @IntoMap
    @Binds
    @ViewModelKey(RemoveBookViewModel.class)
    abstract ViewModel removeBookViewModel(RemoveBookViewModel viewModel);

    @IntoMap
    @Binds
    @ViewModelKey(BookViewModel.class)
    abstract ViewModel bookViewModel(BookViewModel viewModel);

    @Binds
    abstract ViewModelProvider.Factory viewModelFactory(ViewModelFactory factory);
}
