package no.ezet.fasttrack.popularmovies.service;

import android.content.Context;

import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;

@Module
public class MovieModule {

    private static final String BASE_URL = "http://image.tmdb.org/t/p/";
    private final Context context;


    public MovieModule(Context context) {
        this.context = context;
    }

    @Provides
    public ImageService provideImageService(Picasso picasso) {
        return new ImageService(BASE_URL, picasso);
    }

    @Provides
    public Picasso providePicasso() {
        return new Picasso.Builder(context).loggingEnabled(true).build();
    }


}
