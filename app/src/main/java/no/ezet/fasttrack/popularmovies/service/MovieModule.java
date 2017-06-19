package no.ezet.fasttrack.popularmovies.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class MovieModule {

    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String API_KEY = "f1b9458c5a22388abc326bc55eab3216";
    private static final String API_URL = "https://api.themoviedb.org/3/";

    private static OkHttpClient createHttpClient(final String apiKey) {

        OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE);
        httpClient.addInterceptor(loggingInterceptor);
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
        return new ImageService(IMAGE_URL, picasso);
    }

    @Singleton
    @Provides
    IMovieService provideMovieService() {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(createHttpClient(API_KEY))
                .build();
        return retrofit.create(IMovieService.class);
    }


}
