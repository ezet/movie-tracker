package no.ezet.fasttrack.popularmovies.api;

import no.ezet.fasttrack.popularmovies.api.model.AccessToken;
import no.ezet.fasttrack.popularmovies.model.RequestToken;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Mdb4Api {

    @POST("auth/request_token")
    Call<RequestToken> createRequestToken();

    @POST("auth/access_token")
    Call<AccessToken> createAccessToken(@Body() RequestToken requestToken);
}
