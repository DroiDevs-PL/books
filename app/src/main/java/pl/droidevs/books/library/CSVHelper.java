package pl.droidevs.books.library;


import java.util.List;

import pl.droidevs.books.model.Book;
import pl.droidevs.books.model.BookId;

public class CSVHelper {

    private static final Character SEPARATOR = ',';
    private static final String NEW_LINE = "\n";

    public static String getCSVContentFromBooksList(List<Book> books) {
        StringBuilder builder = new StringBuilder("");

        for (Book book : books) {
            builder.append(getBookCSVString(book)).append(NEW_LINE);
        }

        return builder.toString();
    }

    private static String getBookCSVString(Book book) {
        StringBuilder builder = new StringBuilder("");

        builder.append(book.getId().getId()).append(SEPARATOR)
                .append(book.getTitle()).append(SEPARATOR)
                .append(book.getAuthor()).append(SEPARATOR)
                .append(book.getCategory()).append(SEPARATOR)
                .append(book.getDescription()).append(SEPARATOR)
                .append(book.getImageUrl());

        return builder.toString();
    }

}
