package no.ezet.fasttrack.popularmovies.service;

import dagger.Component;
import no.ezet.fasttrack.popularmovies.MovieDetailFragment;
import no.ezet.fasttrack.popularmovies.MovieListActivity;

@Component(modules = {MovieModule.class})
public interface MovieComponent {

    void inject(MovieListActivity activity);

    void inject(MovieDetailFragment activity);

}
