package no.ezet.fasttrack.popularmovies.service;

import android.content.Context;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class ImageService {


    private static final String baseUrl = "http://image.tmdb.org/t/p/";
    public static final String SIZE_W185 = "w185";
    public static final String SIZE_W342 = "w342";


    public static ImageService getImageService(Context context) {
        return new ImageService(new Picasso.Builder(context).loggingEnabled(true).build());
    }

    private Picasso picasso;

    private ImageService(Picasso picasso) {
        this.picasso = picasso;
    }

    public void loadImage(String path, String size, ImageView imageView) {
        RequestCreator creator = picasso.load(baseUrl + size + path);
        creator.into(imageView);
    }
}
