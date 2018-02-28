package pl.droidevs.books.dao;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import pl.droidevs.books.app.BookDataBase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNot.not;
import static pl.droidevs.books.entity.BookEntityBuilder.aBook;

@RunWith(AndroidJUnit4.class)
public class BookDaoTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    private static final int NONE_EXISTING_BOOK_ID = Integer.MIN_VALUE;

    private BookDao objectUnderTest;
    private BookDataBase database;

    @Before
    public void setup() {
        final Context context = InstrumentationRegistry.getTargetContext();
        database = Room.inMemoryDatabaseBuilder(context, BookDataBase.class)
                .allowMainThreadQueries()
                .build();
        objectUnderTest = database.bookDao();
    }

    @Test
    public void whenThereIsNoBookWithGivenId_NullShouldBeReturned() throws Exception {
        // When: fetching an none existing entity
        final BookEntity entity = objectUnderTest.getBookById(NONE_EXISTING_BOOK_ID).blockingGet();

        // Then: Null is returned
        assertThat(entity, is(nullValue()));
    }

    @Test
    public void whenThereIsABookWithAGivenId_ItShouldBeReturned() throws Exception {
        // Given: entity in the database
        final long id = objectUnderTest.addBook(aBook().build());

        // When: fetching entity with given id
        final BookEntity entity = objectUnderTest.getBookById(id).blockingGet();

        // Then: There's an entity with given id
        assertThat(entity, is(not(nullValue())));
    }

    @Test
    public void whenThereIsNoBooks_EmptyListShouldBeReturned() throws Exception {
        // When: fetching all books
        final List<BookEntity> books = objectUnderTest.getAllBooks().blockingFirst();

        // Then: Empty list is returned
        assertThat(books, is(empty()));
    }

    @Test
    public void whenThereAreBooks_TheyAreReturned() throws Exception {
        // Given: Some books in the database
        objectUnderTest.addBook(aBook().build());

        // When: fetching all books
        final List<BookEntity> books = objectUnderTest.getAllBooks().blockingFirst();

        // Then: All books are returned
        assertThat(books, hasSize(1));
    }

    @Test
    public void whenThereAreBooks_AndTitleIsDefined_OnlyMatchingBooksAreReturned() throws Exception {
        // Given: Some books in the database
        objectUnderTest.addBook(aBook().withTitle("Clean Code").build());
        objectUnderTest.addBook(aBook().withTitle("Clean Architecture").build());

        // When: fetching for given title
        final List<BookEntity> books = objectUnderTest.getByTitleOrAuthor("Code", null).blockingFirst();

        // Then: matching books are returned
        assertThat(books, hasSize(1));
    }

    @Test
    public void whenThereAreBooks_AndAuthorIsDefined_OnlyMatchingBooksAreReturned() throws Exception {
        // Given: Some books in the database
        objectUnderTest.addBook(aBook().writtenBy("Robert C. Martin").build());
        objectUnderTest.addBook(aBook().writtenBy("Robert Browning").build());

        // When: fetching for given author
        final List<BookEntity> books = objectUnderTest.getByTitleOrAuthor(null, "Martin").blockingFirst();

        // Then: matching books are returned
        assertThat(books, hasSize(1));
    }

    @Test
    public void whenThereAreBooks_TitleAndAuthorIsDefined_OnlyMatchingBooksAreReturned() throws Exception {
        // Given: Some books in the database
        objectUnderTest.addBook(aBook().withTitle("Clean Code").writtenBy("Robert C. Martin").build());
        objectUnderTest.addBook(aBook().withTitle("The Ring and the Book").writtenBy("Robert Browning").build());

        // When: fetching for given title or author
        final List<BookEntity> books = objectUnderTest.getByTitleOrAuthor("Clean", "Browning").blockingFirst();

        // Then: matching books are returned
        assertThat(books, hasSize(2));
    }

    @Test
    public void whenRemovingBooks_OnlyBooksWithGivenIdAreDeleted() {
        // Given: Database with books
        final long book1Id = objectUnderTest.addBook(aBook().build());
        final long book2Id = objectUnderTest.addBook(aBook().build());
        final long book3Id = objectUnderTest.addBook(aBook().build());

        // When: Removing multiple books
        objectUnderTest.removeBooks(new Long[]{book1Id, book3Id});

        // Then: Only one book remains in the database
        assertThat(objectUnderTest.getBookById(book1Id).blockingGet(), is(nullValue()));
        assertThat(objectUnderTest.getBookById(book2Id).blockingGet(), is(not(nullValue())));
        assertThat(objectUnderTest.getBookById(book3Id).blockingGet(), is(nullValue()));
    }

    @After
    public void tearDown() throws IOException {
        database.close();
    }
}
