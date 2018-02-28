package pl.droidevs.books.repository.csv;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import pl.droidevs.books.dao.BookEntity;

import static java.lang.reflect.Modifier.isStatic;

class CsvHelper {
    private CsvHelper() {
    }

    static String[] getBookEntityCsvHeaders() {
        final Field[] fields = BookEntity.class.getDeclaredFields();
        final List<String> fieldNames = new ArrayList<>();

        for (Field field : fields) {
            if (!isStatic(field.getModifiers())) {
                fieldNames.add(field.getName());
            }
        }

        return fieldNames.toArray(new String[fieldNames.size()]);
    }

    static CellProcessor[] getProcessors() {
        return new CellProcessor[]{
                null,
                null,
                null,
                new Optional(new ParseLong()),
                null,
                null,
        };
    }
}
