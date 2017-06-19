package no.ezet.fasttrack.popularmovies.service;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import javax.inject.Inject;

public class ImageService {


    @SuppressWarnings("unused")
    public static final String SIZE_W185 = "w185";
    public static final String SIZE_W342 = "w342";
    private final String baseUrl;

    private Picasso picasso;

    @Inject
    ImageService(String base_url, Picasso picasso) {
        baseUrl = base_url;
        this.picasso = picasso;
    }

    public void loadImage(String path, String size, ImageView imageView) {
        RequestCreator creator = picasso.load(baseUrl + size + path);
        creator.into(imageView);
    }
}
