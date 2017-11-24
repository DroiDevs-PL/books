package pl.droidevs.books.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.droidevs.books.app.BookDataBase;
import pl.droidevs.books.dao.BookDao;
import pl.droidevs.books.library.LibraryViewModel;
import pl.droidevs.books.repository.BookRepository;

@Module(includes = AppModule.class)
public class DataSourceModule {

    @Singleton
    @Provides
    BookRepository bookRepository(BookDao bookDao) {
        return new BookRepository(bookDao);
    }

    @Singleton
    @Provides
    BookDataBase bookDataBase(Context context) {
        return Room.databaseBuilder(context, BookDataBase.class, BookDataBase.BOOK_DATA_BASE_NAME).build();
    }

    @Singleton
    @Provides
    BookDao bookDao(Context context) {
        return bookDataBase(context).bookDao();
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
