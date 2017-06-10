package no.ezet.fasttrack.popularmovies;

import android.app.Application;

public class App extends Application {



    @Override
    public void onCreate() {
        super.onCreate();
//        movieComponent = DaggerMovieComponent.builder().appModule(new AppModule(this)).imageModule(new MovieModule()).build();
    }

}
