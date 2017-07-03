package no.ezet.fasttrack.popularmovies.api.model;

import com.google.gson.annotations.Expose;

public class RequestToken {

    public final String requestToken;

    @Expose(serialize = false)
    public final String statusMessage;

    @Expose(serialize = false)
    public final boolean Success;

    @Expose(serialize = false)
    public final int statusCode;

    @Expose(serialize = false)
    public final String expiresAt;

    public RequestToken(String requestToken) {
        this.requestToken = requestToken;
        statusCode = 0;
        Success = true;
        statusMessage = null;
        expiresAt = null;
    }

    public RequestToken(String requestToken, boolean success, String expiresAt) {
        this.requestToken = requestToken;
        Success = success;
        this.expiresAt = expiresAt;
        statusCode = 0;
        statusMessage = null;
    }

    public RequestToken(String statusMessage, String requestToken, boolean success, int statusCode) {
        this.statusMessage = statusMessage;
        this.requestToken = requestToken;
        Success = success;
        this.statusCode = statusCode;
        this.expiresAt = null;
    }
}
