package no.ezet.tmdb.service;

import android.app.Application;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import javax.inject.Inject;

import no.ezet.tmdb.R;
import timber.log.Timber;

public class ImageService {

    public static final String SIZE_W185 = "w185";
    public static final String SIZE_W342 = "w342";
    public static final String SIZE_W500 = "w500";
    public static final String SIZE_W780 = "w780";
    public static final String SIZE_ORIGINAL = "original";
    @SuppressWarnings("unused")
    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String BASE = "https://img.youtube.com/vi/";
    private static final String POSTFIX = "/hqdefault.jpg";
    private final String baseUrl;
    private final GlideRequests requestManager;

    @Inject
    public ImageService(Application activity) {
        Timber.d("ImageService: ");
        baseUrl = IMAGE_URL;
        requestManager = GlideApp.with(activity);
    }

    public void loadImage(String path, String size, ImageView imageView) {
        requestManager.load(baseUrl + size + path).placeholder(R.mipmap.placeholder_w342).into(imageView);
    }


    public void loadThumbnail(String videoId, ImageView target) {
        requestManager.load(buildUrl(videoId)).into(target);
    }

    private String buildUrl(String videoId) {
        return BASE + videoId + POSTFIX;
    }
}
