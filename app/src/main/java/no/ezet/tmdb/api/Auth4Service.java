package no.ezet.tmdb.api;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.net.Uri;

import javax.inject.Inject;

import no.ezet.tmdb.R;
import no.ezet.tmdb.api.model.AccessToken;
import no.ezet.tmdb.api.model.RequestToken;
import no.ezet.tmdb.network.Resource;
import no.ezet.tmdb.service.PreferenceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class Auth4Service {

    private final PreferenceService preferenceService;
    private final Mdb4Api Mdb4Api;
    private final MutableLiveData<Resource<AccessToken>> accessToken = new MutableLiveData<>();
    private final String AUTH_URL = "https://www.themoviedb.org/auth/access?request_token=";
    private RequestToken requestToken;

    @Inject
    public Auth4Service(PreferenceService preferenceService, Mdb4Api Mdb4Api) {
        this.preferenceService = preferenceService;
        this.Mdb4Api = Mdb4Api;
    }


    public LiveData<Resource<AccessToken>> authenticate(Activity activity) {
        if (!preferenceService.contains(R.string.pk_request_token_v4)) {
            createRequestToken(activity);
        } else {
            requestToken = new RequestToken(preferenceService.getString(R.string.pk_request_token_v4));
            createAccessToken(requestToken);
        }
        return accessToken;
    }

    private void createRequestToken(Activity activity) {
        Mdb4Api.createRequestToken().enqueue(new Callback<RequestToken>() {
            @Override
            public void onResponse(Call<RequestToken> call, Response<RequestToken> response) {
                Timber.d("onResponse: ");
                requestToken = response.body();
                authorizeToken(activity, requestToken);
            }

            @Override
            public void onFailure(Call<RequestToken> call, Throwable t) {
            }
        });
    }

    private void authorizeToken(Activity context, RequestToken requestToken) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AUTH_URL + requestToken.requestToken));
        context.startActivityForResult(intent, 42);
    }

    private void createAccessToken(RequestToken requestToken) {
        Mdb4Api.createAccessToken(requestToken).enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                accessToken.setValue(Resource.success(response.body()));
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                accessToken.setValue(Resource.error(t.getLocalizedMessage(), null));
            }
        });
    }


    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 42) return false;
        preferenceService.putString(R.string.pk_request_token_v4, requestToken.requestToken);
        createAccessToken(requestToken);
        return true;
    }
}
