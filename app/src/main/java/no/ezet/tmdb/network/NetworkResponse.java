package no.ezet.tmdb.network;

import java.io.IOException;

import retrofit2.Response;

public class NetworkResponse<T> {

    public final String errorMessage;

    public final T body;

    public final int code;

    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }

    public NetworkResponse(Response<T> response) {
        code = response.code();
        if (response.isSuccessful()) {
            body = response.body();
            errorMessage = null;
        } else {
            String message = null;
            if (response.errorBody() != null) {
                try {
                    message = response.errorBody().string();
                } catch (IOException ignored) {
                    ignored.printStackTrace();
                }
            }
            if (message == null || message.trim().length() == 0) {
                message = response.message();
            }
            errorMessage = message;
            body = null;
        }
    }

    public NetworkResponse(Throwable error) {
        code = 500;
        body = null;
        errorMessage = error.getMessage();
    }


}
