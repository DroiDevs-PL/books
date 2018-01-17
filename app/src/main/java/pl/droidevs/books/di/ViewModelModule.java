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
import pl.droidevs.books.addbook.AddBookViewModel;
import pl.droidevs.books.exportimport.ExportImportViewModel;
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
    @ViewModelKey(AddBookViewModel.class)
    abstract ViewModel addBookViewModel(AddBookViewModel viewModel);

    @Binds
    abstract ViewModelProvider.Factory viewModelFactory(ViewModelFactory factory);
}
