package pl.droidevs.books.repository;

import pl.droidevs.books.domain.Book;
import pl.droidevs.books.domain.BookId;
import pl.droidevs.data.repository.reactive.FilterableRxCrudRepository;

public interface BookRepository extends FilterableRxCrudRepository<Book, BookId, BookFilter> {
}

