package pl.droidevs.books.reposistory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.Collections;

import io.reactivex.Flowable;
import pl.droidevs.books.dao.BookDao;
import pl.droidevs.books.entity.BookEntity;
import pl.droidevs.books.model.Book;
import pl.droidevs.books.model.BookId;
import pl.droidevs.books.reactive.TestSchedulers;
import pl.droidevs.books.repository.DatabaseBookRepository;

import static java.lang.String.valueOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.droidevs.books.entity.BookEntityBuilder.aBook;
import static pl.droidevs.books.model.Book.Category.SPORT;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseBookRepositoryTest {
    private DatabaseBookRepository objectUnderTest;

    @Mock
    private BookDao bookDao;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        objectUnderTest = new DatabaseBookRepository(bookDao, new TestSchedulers());
    }

    @Test
    public void whenThereAreBooksInDatabase_TheyShouldBeReturned() {
        // Given
        when(bookDao.getAllBooks()).thenReturn(Flowable.just(
                Collections.singletonList(aBook()
                        .withTitle("Kowal. Prawdziwa historia")
                        .writtenBy("Wojciech Kowalczyk")
                        .belongsTo(valueOf(SPORT)).build()))
        );

        // When
        final Collection<Book> books = objectUnderTest.fetchAll().blockingFirst();

        // Then
        assertThat(books, hasSize(1));
//        assertThat(books, contains(new Book(BookId.of(0L), "Kowal. Prawdziwa historia", "Wojciech Kowalczyk", SPORT)));
    }

    @Test
    public void whenBookISaved_ItsIdIsBeingUpdated() {
        // Given
        when(bookDao.addBook(any(BookEntity.class))).thenReturn(1L);
        final Book aBook = new Book("Kowal. Prawdziwa historia", "Wojciech Kowalczyk", SPORT);

        // When
        objectUnderTest.save(aBook).blockingAwait();

        // Then
        assertThat(aBook.getId(), is(BookId.of(1L)));
    }
}
