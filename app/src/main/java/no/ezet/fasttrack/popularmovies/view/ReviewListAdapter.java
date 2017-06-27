package no.ezet.fasttrack.popularmovies.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import no.ezet.fasttrack.popularmovies.R;
import no.ezet.fasttrack.popularmovies.db.MovieReview;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {


    private final List<MovieReview> reviews = new ArrayList<>();
    private ItemClickListener itemClickListener;

    ReviewListAdapter(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.review_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        viewHolder.bind(reviews.get(i));
        viewHolder.itemView.setOnClickListener(v -> itemClickListener.read(viewHolder.movieReview, viewHolder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    void setReviews(List<MovieReview> reviews) {
        this.reviews.clear();
        this.reviews.addAll(reviews);
        notifyDataSetChanged();
    }

    interface ItemClickListener {
        void read(MovieReview movieReview, int adapterPosition);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView reviewContent;
        final TextView reviewAuthor;
        MovieReview movieReview;

        ViewHolder(View itemView) {
            super(itemView);
            reviewContent = (TextView) itemView.findViewById(R.id.review_content);
            reviewAuthor = (TextView) itemView.findViewById(R.id.review_author);
        }

        void bind(MovieReview movieReview) {
            this.movieReview = movieReview;
            reviewContent.setText(movieReview.content);
            reviewAuthor.setText(movieReview.author);
        }
    }
}
