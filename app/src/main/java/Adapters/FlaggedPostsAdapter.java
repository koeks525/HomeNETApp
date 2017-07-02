package Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koeksworld.homenet.R;

import java.util.List;

import Models.HousePostFlag;

/**
 * Created by Okuhle on 2017/06/15.
 */

public class FlaggedPostsAdapter extends RecyclerView.Adapter<FlaggedPostsAdapter.FlaggedPostHolder> {

    private List<HousePostFlag> flaggedPostList;

    public FlaggedPostsAdapter(List<HousePostFlag> flaggedPostList) {
        this.flaggedPostList = flaggedPostList;
    }

    @Override
    public FlaggedPostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View currentView = inflater.inflate(R.layout.manage_posts_card, parent, false);
        return new FlaggedPostHolder(currentView);
    }

    @Override
    public void onBindViewHolder(FlaggedPostHolder holder, int position) {
        holder.postImageView.setImageResource(R.drawable.ic_description);
        holder.postTitleTextView.setText(flaggedPostList.get(position).getMessage());
        holder.postDescriptionTextView.setText(flaggedPostList.get(position).getResponseMessage()); //Horrible, disgusting
    }

    @Override
    public int getItemCount() {
        return flaggedPostList.size();
    }

    class FlaggedPostHolder extends RecyclerView.ViewHolder {

        private ImageView postImageView;
        private TextView postTitleTextView;
        private TextView postDescriptionTextView;

        public FlaggedPostHolder(View itemView) {
            super(itemView);
            postImageView = (ImageView) itemView.findViewById(R.id.HouseManagerPostImageView);
            postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            postTitleTextView = (TextView) itemView.findViewById(R.id.HouseManagePostTitleTextView);
            postDescriptionTextView = (TextView) itemView.findViewById(R.id.HouseManagerFlaggedPostDescriptionTextView);
        }
    }
}
