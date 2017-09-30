package Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koeksworld.homenet.R;

import java.util.List;

import Models.HousePostFlag;
import Models.HousePostViewModel;

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
    public void onBindViewHolder(final FlaggedPostHolder holder, final int position) {
        holder.postImageView.setImageResource(R.drawable.ic_description);
        holder.postDescriptionTextView.setText(flaggedPostList.get(position).getMessage());
        holder.optionsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(holder.optionsImageView.getContext(), view);
                popupMenu.inflate(R.menu.flagged_posts_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.FlaggedPostViewDetailsOption:
                                //HousePostViewModel selectedPost = flaggedPostList.get(position);


                                break;
                            case R.id.FlaggedPostReverseBanOption:


                                break;
                        }
                        return true;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return flaggedPostList.size();
    }

    class FlaggedPostHolder extends RecyclerView.ViewHolder {

        private ImageView postImageView;
        private TextView postDescriptionTextView;
        private ImageView optionsImageView;

        public FlaggedPostHolder(View itemView) {
            super(itemView);
            postImageView = (ImageView) itemView.findViewById(R.id.HouseManagerPostImageView);
            postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            postDescriptionTextView = (TextView) itemView.findViewById(R.id.HouseManagerFlaggedPostDescriptionTextView);
            optionsImageView = (ImageView) itemView.findViewById(R.id.HouseManagerFlaggedPostOptionImageView);


        }
    }
}
