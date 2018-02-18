package pl.droidevs.books.exportimport;

public class ExportFailedException extends RuntimeException {
    ExportFailedException(Exception e) {
        super(e);
    }
}
