package pl.droidevs.books.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.droidevs.books.app.BookDataBase;
import pl.droidevs.books.library.LibraryViewModel;
import pl.droidevs.books.repository.BookRepository;

@Module
public class DataSourceModule {

    @Singleton
    @Provides
    BookRepository bookRepository(BookDataBase bookDataBase) {
        return new BookRepository(bookDataBase);
    }

    @Singleton
    @Provides
    BookDataBase bookDataBase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), BookDataBase.class, BookDataBase.BOOK_DATA_BASE_NAME).build();
    }

    @Provides
    ViewModel libraryViewModel(LibraryViewModel viewModel) {
        return viewModel;
    }

    @Provides
    ViewModelProvider.Factory libraryViewModelFactory(ViewModelFactory factory) {
        return factory;
    }
}
