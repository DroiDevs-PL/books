package pl.droidevs.books.resource;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static pl.droidevs.books.resource.Resource.Status.ERROR;
import static pl.droidevs.books.resource.Resource.Status.LOADING;
import static pl.droidevs.books.resource.Resource.Status.SUCCESS;

public final class Resource<T> {

    @NonNull
    private final Status status;

    @Nullable
    private final ErrorCode errorCode;

    @Nullable
    private final T data;

    private Resource(@NonNull final Status status, final @Nullable T data, final @Nullable ErrorCode errorCode) {
        this.status = status;
        this.data = data;
        this.errorCode = errorCode;
    }

    @NonNull
    public Status getStatus() {
        return status;
    }

    @Nullable
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public boolean isError() {
        return errorCode != null;
    }

    @Nullable
    public T getData() {
        return data;
    }

    public boolean isData() {
        return data != null;
    }

    public static <T> Resource<T> success(@Nullable T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static <T> Resource<T> error(@Nullable T data, @Nullable final ErrorCode errorCode) {
        return new Resource<>(ERROR, data, errorCode);
    }

    public static <T> Resource<T> error(@Nullable final ErrorCode errorCode) {
        return new Resource<>(ERROR, null, errorCode);
    }

    public static <T> Resource<T> loading() {
        return new Resource<>(LOADING, null, null);
    }

    public enum Status {
        ERROR, LOADING, SUCCESS
    }

    public interface ErrorCode {
        int getCode();
    }
}
