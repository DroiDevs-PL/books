package pl.droidevs.books.dao;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import pl.droidevs.books.entity.BookEntity;

public final class InMemoryBookDao implements BookDao {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);

    private final Map<Integer, BookEntity> books = new HashMap<>();
    private final MutableLiveData<List<BookEntity>> booksLiveData = new MutableLiveData<>();

    public InMemoryBookDao() {
        addBook("Don Quixote", "Miguel de Cervantes", "ENTERTAINEMENT",
                "http://www.nexto.pl/upload/sklep/masterlab/ebook/don_kichot_z_la_manchy-miguel_de_cervantes-masterlab/public/don_kichot_z_la_manchy-masterlab-ebook-cov.jpg");
        addBook("The Lord of the Rings", "J. R. R. Tolkien", "FANTASY",
                "https://images-na.ssl-images-amazon.com/images/I/51EstVXM1UL._SX331_BO1,204,203,200_.jpg");
        addBook("Clean Code", "Robert C. Martin", "COMPUTERS",
                "https://images-na.ssl-images-amazon.com/images/I/41wGTnmRTFL._SX375_BO1,204,203,200_.jpg");
        addBook("The Little Prince", "Antoine de Saint-Exupery", "KIDS",
                "https://upload.wikimedia.org/wikipedia/en/0/05/Littleprince.JPG");
        addBook("I'm Zlatan", "David Lagercrantz", "BIOGRAPHY",
                "http://www.iamzlatan.com/images/ENG/app-page-1.jpg");
        addBook("It", "Stephen King", "HORROR",
                "https://prodimage.images-bn.com/pimages/9781501141232_p0_v4_s550x406.jpg");
        addBook("On the Road", "Jack Kerouac", "TRAVEL",
                "https://images-na.ssl-images-amazon.com/images/I/51ZL9txL7bL._SX311_BO1,204,203,200_.jpg");
        addBook("Murder on the Orient Express", "Agatha Christie", "MYSTERY",
                "https://images-na.ssl-images-amazon.com/images/I/51%2B2QZIRWfL._SY344_BO1,204,203,200_.jpg");
        addBook("The Hunt for Red October", "Tom Clancy", "SCIENCE_FICTION",
                "https://images-na.ssl-images-amazon.com/images/I/515kw2u7b3L._SY344_BO1,204,203,200_.jpg");
        addBook("Romeo and Juliet", "William Shakespeare", "ROMANCE",
                "http://www.loyalbooks.com/image/detail/Romeo-and-Juliet.jpg");
    }

    private void addBook(String title, String author, String category, String imageUrl) {
        final BookEntity book = new BookEntity();
        book.setTitle(title);
        book.setAuthor(author);
        book.setCategory(category);
        book.setImageUrl(imageUrl);

        addBook(book);
    }

    @Override
    public LiveData<List<BookEntity>> getAllBooks() {
        final ArrayList<BookEntity> sortedBooks = new ArrayList<>(this.books.values());
        Collections.sort(sortedBooks, (t1, t2) -> t1.getTitle().compareTo(t2.getTitle()));
        booksLiveData.setValue(sortedBooks);

        return booksLiveData;
    }

    @Override
    public void addBook(@NonNull BookEntity book) {
        final int id = ID_GENERATOR.incrementAndGet();
        book.setId(id);
        books.put(id, book);
    }

    @Override
    public void removeBook(@NonNull BookEntity book) {
        books.remove(book.getId());
    }

    @Override
    public void updateBook(@NonNull BookEntity book) {
        books.put(book.getId(), book);
    }
}
