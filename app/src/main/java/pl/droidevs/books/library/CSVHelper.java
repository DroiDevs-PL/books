package pl.droidevs.books.library;


import android.util.Log;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.lang.reflect.Field;
import java.util.List;

import pl.droidevs.books.entity.BookEntity;
import pl.droidevs.books.model.Book;

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

        builder.append(book.getId()).append(SEPARATOR)
                .append(book.getTitle()).append(SEPARATOR)
                .append(book.getAuthor()).append(SEPARATOR)
                .append(book.getCategory()).append(SEPARATOR)
                .append(book.getDescription()).append(SEPARATOR)
                .append(book.getImageUrl());

        return builder.toString();
    }

    public static String[] getBookEntityCsvHeaders() {
        Class bookEntityClass = BookEntity.class;
        Field[] fields = bookEntityClass.getDeclaredFields();
        String[] fieldNames = new String[fields.length - 1];

        for (int i = 0; i < fields.length - 1; i++) {
            fieldNames[i] = fields[i].getName();
        }

        return fieldNames;
    }

    public static CellProcessor[] getProcessors() {

        final CellProcessor[] processors = new CellProcessor[] {
                null,
                null,
                null,
                new Optional(new ParseInt()),
                null,
                null,
        };

        return processors;
    }
}
