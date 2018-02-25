package pl.droidevs.books.repository;

import pl.droidevs.books.model.Book;
import pl.droidevs.books.model.BookId;
import pl.droidevs.data.repository.RxCrudRepository;

public interface BookRepository extends RxCrudRepository<Book, BookId> {
}
