package no.ezet.fasttrack.popularmovies.service;

import android.app.Application;

import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class MovieModule {

    private static final String BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String apiKey = "f1b9458c5a22388abc326bc55eab3216";
    private static final String baseUrl = "https://api.themoviedb.org/3/";

    private static OkHttpClient createHttpClient(final String apiKey) {
        OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();

            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", apiKey)
                    .build();

            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .url(url);

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });
        return httpClient.build();
    }

    @Singleton
    @Provides
    ImageService provideImageService(Picasso picasso) {
        return new ImageService(BASE_URL, picasso);
    }


    @Singleton
    @Provides
    IMovieService provideMovieService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(createHttpClient(apiKey))
                .build();
        return retrofit.create(IMovieService.class);
    }


}
