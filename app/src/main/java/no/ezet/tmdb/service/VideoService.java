package no.ezet.tmdb.service;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public class VideoService {

    private static final String BASE = "https://img.youtube.com/vi/";
    private static final String POSTFIX = "/maxresdefault.jpg";


    private final Picasso picasso;

    @Inject
    VideoService(Picasso picasso) {
        this.picasso = picasso;
    }

    public void loadThumbnail(String videoId, ImageView target) {
        picasso.load(buildUrl(videoId)).into(target);
    }

    private String buildUrl(String videoId) {
        return BASE + videoId + POSTFIX;
    }
}
