package pl.droidevs.books;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static pl.droidevs.books.Resource.Status.ERROR;
import static pl.droidevs.books.Resource.Status.LOADING;
import static pl.droidevs.books.Resource.Status.SUCCESS;

public final class Resource<T> {
    @NonNull
    private final Status status;

    @Nullable
    private final Throwable error;

    @Nullable
    private final T data;

    private Resource(@NonNull final Status status, final @Nullable T data, final @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T> Resource<T> success(@Nullable T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static <T> Resource<T> success() {
        return new Resource<>(SUCCESS, null, null);
    }

    public static <T> Resource<T> error(@Nullable T data, @Nullable final Throwable error) {
        return new Resource<>(ERROR, data, error);
    }

    public static <T> Resource<T> error(@Nullable final Throwable errorCode) {
        return new Resource<>(ERROR, null, errorCode);
    }

    public static <T> Resource<T> loading() {
        return new Resource<>(LOADING, null, null);
    }

    @NonNull
    public Status getStatus() {
        return status;
    }

    @Nullable
    public Throwable getError() {
        return error;
    }

    public boolean isError() {
        return error != null;
    }

    @Nullable
    public T getData() {
        return data;
    }

    public boolean isData() {
        return data != null;
    }

    /**
     * Possible status types of a resource provided to the UI
     */
    public enum Status {
        ERROR, LOADING, SUCCESS
    }
}
