package pl.droidevs.books.exportimport;

public class ExportFailedException extends RuntimeException {
    ExportFailedException(Throwable cause) {
        super(cause);
    }
}
