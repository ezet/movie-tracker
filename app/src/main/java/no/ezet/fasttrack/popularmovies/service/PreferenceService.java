package no.ezet.fasttrack.popularmovies.service;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.StringRes;

import javax.inject.Inject;

public class PreferenceService {


    private final SharedPreferences defaultSharedPreferences;
    private final Resources resources;

    @Inject
    public PreferenceService(Application application) {
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        resources = application.getResources();
    }

    public boolean getBoolean(@StringRes int resId) {
        return defaultSharedPreferences.getBoolean(resources.getString(resId), false);
    }

    public String getString(@StringRes int resId) {
        return defaultSharedPreferences.getString(resources.getString(resId), "");
    }


}
