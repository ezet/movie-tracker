package no.ezet.tmdb.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import no.ezet.tmdb.R;
import no.ezet.tmdb.api.model.MovieTrailer;
import no.ezet.tmdb.service.ImageService;

public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.ViewHolder> {

    private final List<MovieTrailer> videos = new ArrayList<>();
    private final ImageService imageService;
    private ItemClickListener itemClickListener;

    TrailerListAdapter(ImageService imageService, ItemClickListener itemClickListener) {
        this.imageService = imageService;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trailer_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        viewHolder.movieTrailer = videos.get(i);
        imageService.loadThumbnail(viewHolder.movieTrailer.key, viewHolder.trailerThumbnail);
        viewHolder.itemView.setOnClickListener(v -> itemClickListener.onClick(viewHolder.movieTrailer, viewHolder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    void setVideos(List<MovieTrailer> videos) {
        this.videos.clear();
        this.videos.addAll(videos);
        notifyDataSetChanged();
    }

    interface ItemClickListener {
        void onClick(MovieTrailer movieTrailer, int adapterPosition);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView trailerThumbnail;
        MovieTrailer movieTrailer;

        ViewHolder(View itemView) {
            super(itemView);
            trailerThumbnail = (ImageView) itemView.findViewById(R.id.trailer_thumbnail);
        }

    }


}
