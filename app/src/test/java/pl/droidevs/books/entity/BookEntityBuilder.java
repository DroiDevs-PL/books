package pl.droidevs.books.entity;

import pl.droidevs.books.dao.BookEntity;

public final class BookEntityBuilder {
    private final BookEntity entity;

    private BookEntityBuilder() {
        entity = new BookEntity();
        entity.setTitle("Title");
        entity.setAuthor("Author");
    }

    public BookEntityBuilder withTitle(final String title) {
        entity.setTitle(title);
        return this;
    }

    public BookEntityBuilder writtenBy(final String author) {
        entity.setAuthor(author);
        return this;
    }

    public BookEntityBuilder belongsTo(final String category) {
        entity.setCategory(category);
        return this;
    }

    public BookEntity build() {
        return entity;
    }

    public static BookEntityBuilder aBook() {
        return new BookEntityBuilder();
    }
}
