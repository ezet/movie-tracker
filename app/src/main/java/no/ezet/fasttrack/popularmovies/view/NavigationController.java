package no.ezet.fasttrack.popularmovies.view;

import javax.inject.Inject;

public class NavigationController {

    MainActivity activity;

    @Inject
    public NavigationController(MainActivity activity) {
        this.activity = activity;
    }
}
