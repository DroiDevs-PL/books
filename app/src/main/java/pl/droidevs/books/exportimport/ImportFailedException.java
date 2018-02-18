package pl.droidevs.books.exportimport;

public class ImportFailedException extends RuntimeException {
    ImportFailedException(Exception e) {
        super(e);
    }
}
