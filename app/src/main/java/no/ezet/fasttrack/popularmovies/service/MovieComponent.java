package no.ezet.fasttrack.popularmovies.service;

import javax.inject.Singleton;

import dagger.Component;
import no.ezet.fasttrack.popularmovies.AppModule;
import no.ezet.fasttrack.popularmovies.view.MovieDetailFragment;
import no.ezet.fasttrack.popularmovies.view.MovieListActivity;

@Singleton
@Component(modules = {MovieModule.class, AppModule.class})
public interface MovieComponent {

    void inject(MovieListActivity activity);

    void inject(MovieDetailFragment activity);

}
