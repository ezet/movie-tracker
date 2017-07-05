package no.ezet.tmdb.api.model;

public class AccessToken {

    public final String statusMessage;

    public final String accessToken;

    public final boolean success;

    public final int statusCode;

    public final String accountId;

    public AccessToken(String statusMessage, String accessToken, boolean success, int statusCode, String accountId) {
        this.statusMessage = statusMessage;
        this.accessToken = accessToken;
        this.success = success;
        this.statusCode = statusCode;
        this.accountId = accountId;
    }
}
