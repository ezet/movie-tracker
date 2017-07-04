package no.ezet.fasttrack.popularmovies.view;

import android.support.v4.view.ViewCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import no.ezet.fasttrack.popularmovies.R;
import no.ezet.fasttrack.popularmovies.service.ImageService;
import no.ezet.fasttrack.popularmovies.viewmodel.MovieListItem;
import timber.log.Timber;

public class MovieListRecyclerViewAdapter
        extends RecyclerView.Adapter<MovieListRecyclerViewAdapter.ViewHolder> {

    private final List<MovieListItem> movies = new ArrayList<>();
    private ImageService imageService;
    private MovieListBaseFragment.FragmentListener listener;
    private int counter;

    MovieListRecyclerViewAdapter(ImageService imageService, MovieListBaseFragment.FragmentListener listener) {
        this.imageService = imageService;
        this.listener = listener;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return movies.get(position).id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MovieListItem movie = movies.get(position);
        loadImage(movie.posterPath, holder.posterImage);
        ViewCompat.setTransitionName(holder.itemView, String.valueOf(movie.id));
        holder.itemView.setOnClickListener((View view) -> listener.onItemClick(view, movie));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    private void loadImage(String relPath, ImageView imageView) {
        imageService.loadImage(relPath, ImageService.SIZE_W342, imageView);
    }

    void setMovies(List<MovieListItem> movieList) {
        Timber.d("setMovies: counter:" + counter++);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(this.movies, movieList));
        this.movies.clear();
        this.movies.addAll(movieList);
        diffResult.dispatchUpdatesTo(this);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView posterImage;

        ViewHolder(View itemView) {
            super(itemView);
            posterImage = (ImageView) itemView.findViewById(R.id.iv_poster_image);
        }
    }

    private static class DiffCallback extends DiffUtil.Callback {

        private final List<MovieListItem> newList;
        private final List<MovieListItem> oldList;

        DiffCallback(List<MovieListItem> oldList, List<MovieListItem> newList) {
            this.newList = newList;
            this.oldList = oldList;
        }

        @Override
        public int getOldListSize() {
            return oldList != null ? oldList.size() : 0;
        }

        @Override
        public int getNewListSize() {
            return newList != null ? newList.size() : 0;
        }

        @Override
        public boolean areItemsTheSame(int oldPosition, int newPosition) {
            return newList.get(newPosition) == oldList.get(oldPosition);
        }

        @Override
        public boolean areContentsTheSame(int oldPosition, int newPosition) {
            return newList.get(newPosition).posterPath.equals(oldList.get(oldPosition).posterPath);
        }
    }
}
