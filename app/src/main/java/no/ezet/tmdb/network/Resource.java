package no.ezet.tmdb.network;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * A generic class that describes data with a status
 *
 * @param <T> Data type
 */
public class Resource<T> {

    public static final int SUCCESS = 0;
    public static final int ERROR = -1;
    public static final int LOADING = 1;

    @NonNull
    public final int status;
    public final T data;
    public final String message;

    private Resource(@NonNull int status, @Nullable T data, @Nullable String message) {
        if (status < ERROR || status > LOADING)
            throw new IllegalArgumentException("Invalid status: " + status);
        this.status = status;
        this.data = data;
        this.message = message;
    }

    @NonNull
    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    @NonNull
    public static <T> Resource<T> error(@Nullable String msg, @Nullable T data) {
        return new Resource<>(ERROR, data, msg);
    }

    @NonNull
    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(LOADING, data, null);
    }


}
