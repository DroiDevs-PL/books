package pl.droidevs.books.exportimport;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.lang.reflect.Field;

import pl.droidevs.books.entity.BookEntity;

class CSVHelper {

    public static String[] getBookEntityCsvHeaders() {
        Field[] fields = BookEntity.class.getDeclaredFields();
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
