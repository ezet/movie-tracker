package no.ezet.fasttrack.popularmovies.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import no.ezet.fasttrack.popularmovies.R;
import no.ezet.fasttrack.popularmovies.model.MovieReview;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {


    private List<MovieReview> reviews;
    private ReviewClickListener reviewClickListener;

    public ReviewListAdapter(ReviewClickListener reviewClickListener) {
        this.reviewClickListener = reviewClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.review_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.bind(reviews.get(i));
        viewHolder.itemView.setOnClickListener(v -> reviewClickListener.read(viewHolder.movieReview, viewHolder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return reviews == null ? 0 : reviews.size();
    }

    public void setReviews(List<MovieReview> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    interface ReviewClickListener {
        void read(MovieReview movieReview, int adapterPosition);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final View itemView;
        final TextView reviewContent;
        final TextView reviewAuthor;
        MovieReview movieReview;

        ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
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
