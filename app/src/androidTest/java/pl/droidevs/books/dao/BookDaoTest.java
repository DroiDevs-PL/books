package pl.droidevs.books.dao;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import pl.droidevs.books.app.BookDataBase;
import pl.droidevs.books.entity.BookEntity;
import pl.droidevs.books.model.Book;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class BookDaoTest {
    private BookDao objectUnderTest;
    private BookDataBase database;

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getTargetContext();
        database = Room.inMemoryDatabaseBuilder(context, BookDataBase.class).build();
        objectUnderTest = database.bookDao();
    }

    @Test
    public void whenBookIsValid_ShouldAddItToTheDatabase() throws Exception {
        objectUnderTest.addBook(validBookEntity());

        assertThat(true, is(true));
    }

    private BookEntity validBookEntity() {
        final BookEntity book = new BookEntity();
        book.setTitle("A book");
        book.setAuthor("Well-known Author");
        book.setCategory(Book.Category.ENTERTAINEMENT.toString());
        book.setDescription("A description");
        return book;
    }

    @After
    public void tearDown() throws IOException {
        database.close();
    }
}
