package no.ezet.tmdb.api;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import no.ezet.tmdb.R;
import no.ezet.tmdb.api.model.AccountDetails;
import no.ezet.tmdb.api.model.Session;
import no.ezet.tmdb.api.model.RequestToken;
import no.ezet.tmdb.network.Resource;
import no.ezet.tmdb.service.PreferenceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class Auth3Service {

    private static final int REQUEST_CODE = 42;
    private static final String AUTH_URL = "https://www.themoviedb.org/authenticate/";
    private final Mdb3Api api;
    private final PreferenceService preferenceService;
    private RequestToken requestToken;
    private MutableLiveData<Resource<Session>> session = new MutableLiveData<>();

    @Inject
    Auth3Service(Mdb3Api api, PreferenceService preferenceService) {
        this.api = api;
        this.preferenceService = preferenceService;
    }

    LiveData<Resource<Session>> authenticate(Activity activity) {
        if (!preferenceService.contains(R.string.pk_session_id)) {
            session.setValue(Resource.loading(null));
            createRequestToken(activity);
        } else {
            String sessionId = preferenceService.getString(R.string.pk_session_id);
            session.setValue(Resource.success(new Session(true, sessionId)));
        }
        return session;
    }

    private void createRequestToken(Activity activity) {
        api.createRequestToken().enqueue(new Callback<RequestToken>() {
            @Override
            public void onResponse(@NonNull Call<RequestToken> call, @NonNull Response<RequestToken> response) {
                Timber.d("onResponse: createRequestToken");
                requestToken = response.body();
                authorizeToken(activity, requestToken);
            }

            @Override
            public void onFailure(@NonNull Call<RequestToken> call, @NonNull Throwable t) {
                Timber.d("onFailure: createRequestToken");
                session.postValue(Resource.error(t.getLocalizedMessage(), null));
            }
        });
    }

    private void authorizeToken(Activity context, RequestToken requestToken) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AUTH_URL + requestToken.requestToken));
        context.startActivityForResult(intent, REQUEST_CODE);
    }

    @SuppressWarnings("unused")
    void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE) return;
        preferenceService.putString(R.string.pk_request_token_v3, requestToken.requestToken);
        preferenceService.putString(R.string.pk_request_token_v3_expires, requestToken.expiresAt);
        createSession(requestToken.requestToken);
    }


    private void createSession(String requestToken) {
        api.createSession(requestToken).enqueue(new Callback<Session>() {
            @Override
            public void onResponse(@NonNull Call<Session> call, @NonNull Response<Session> response) {
                Timber.d("onResponse: createSession");
                getDetails(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Session> call, @NonNull Throwable t) {
                Timber.d("onFailure: createSession");
                session.setValue(Resource.error(t.getLocalizedMessage(), null));
            }
        });
    }

    private void getDetails(Session sessionParam) {
        api.getAccountDetails(sessionParam.sessionId).enqueue(new Callback<AccountDetails>() {
            @Override
            public void onResponse(@NonNull Call<AccountDetails> call, @NonNull Response<AccountDetails> response) {
                Timber.d("onResponse: getAccountDetails");
                if (response.body() == null) return;
                //noinspection ConstantConditions
                preferenceService.putInt(R.string.pk_account_id, response.body().id);
                preferenceService.putString(R.string.pk_session_id, sessionParam.sessionId);
                //noinspection ConstantConditions
                sessionParam.setAccountId(response.body().id);
                session.setValue(Resource.success(sessionParam));
            }

            @Override
            public void onFailure(@NonNull Call<AccountDetails> call, @NonNull Throwable t) {
                Timber.d("onFailure: getAccountDetails");
            }
        });
    }
}


