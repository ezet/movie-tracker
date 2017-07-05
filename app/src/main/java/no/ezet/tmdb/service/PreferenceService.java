package no.ezet.tmdb.service;

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

    public void putString(@StringRes int resId, String value) {
        defaultSharedPreferences.edit().putString(resources.getString(resId), value).apply();
    }

    public boolean contains(@StringRes int resId) {
        return defaultSharedPreferences.contains(resources.getString(resId));
    }


    public int getInt(@StringRes int resId) {
        return defaultSharedPreferences.getInt(resources.getString(resId), 0);
    }

    public void putInt(@StringRes int resId, int value) {
        defaultSharedPreferences.edit().putInt(resources.getString(resId), value).apply();
    }
}