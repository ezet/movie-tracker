package no.ezet.fasttrack.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import no.ezet.fasttrack.popularmovies.models.MovieList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    public void setMovies(MovieList movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    private MovieList movies;

    public MovieAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = movies.getMovies().get(position).getTitle();
        holder.title.setText(title);
    }

    @Override
    public int getItemCount() {
        if (movies == null) return 0;
        return movies.getMovies().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_movie_title);
        }

    }
}
