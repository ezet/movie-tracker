package no.ezet.fasttrack.popularmovies;

import android.app.Application;

import no.ezet.fasttrack.popularmovies.service.DaggerMovieComponent;
import no.ezet.fasttrack.popularmovies.service.MovieComponent;
import no.ezet.fasttrack.popularmovies.service.MovieModule;

public class App extends Application {

    private static App instance;
    private MovieComponent movieComponent;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        movieComponent = DaggerMovieComponent.builder().appModule(new AppModule(this)).movieModule(new MovieModule()).build();
    }

    public MovieComponent getMovieComponent() {
        return movieComponent;
    }
}
