package no.ezet.tmdb.api;

import no.ezet.tmdb.api.model.AccessToken;
import no.ezet.tmdb.api.model.RequestToken;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Mdb4Api {

    @POST("auth/request_token")
    Call<RequestToken> createRequestToken();

    @POST("auth/access_token")
    Call<AccessToken> createAccessToken(@Body() RequestToken requestToken);
}
