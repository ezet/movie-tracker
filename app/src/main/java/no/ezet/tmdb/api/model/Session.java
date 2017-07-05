package no.ezet.tmdb.api.model;

public class Session {

    public final boolean Success;

    public final String sessionId;
    private int accountId;

    public Session(boolean success, String sessionId) {
        Success = success;
        this.sessionId = sessionId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
