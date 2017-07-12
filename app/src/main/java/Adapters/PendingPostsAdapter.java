package Adapters;

import android.graphics.Color;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.koeksworld.homenet.R;

import java.util.List;

import Models.HousePostFlag;

/**
 * Created by Okuhle on 2017/07/12.
 */

public class PendingPostsAdapter extends RecyclerView.Adapter<PendingPostsAdapter.PendingPostsViewHolder> {

    private List<HousePostFlag>  pendingPostList;

    public PendingPostsAdapter(List<HousePostFlag> pendingPostList) {
        this.pendingPostList = pendingPostList;
    }

    @Override
    public PendingPostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View currentView = inflater.inflate(R.layout.manage_posts_card, parent, false);
        return new PendingPostsViewHolder(currentView);
    }

    @Override
    public void onBindViewHolder(final PendingPostsViewHolder holder, int position) {
        HousePostFlag post = pendingPostList.get(position);
        holder.postDescriptionTextView.setText(post.getMessage());
        TextDrawable drawable = TextDrawable.builder().buildRound(post.getMessage().substring(0,1).toUpperCase(), Color.BLUE);
        holder.postImageView.setImageDrawable(drawable);
        holder.optionsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(holder.optionsImageView.getContext(), view);
                popupMenu.inflate(R.menu.pending_posts_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.FlagPost:


                                break;
                            case R.id.MarkSafe:



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
        return pendingPostList.size();
    }

    class PendingPostsViewHolder extends RecyclerView.ViewHolder {

        private ImageView postImageView;
        private TextView postDescriptionTextView;
        private ImageView optionsImageView;

        public PendingPostsViewHolder(View itemView) {
            super(itemView);
            postImageView = (ImageView) itemView.findViewById(R.id.HouseManagerPostImageView);
            postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            postDescriptionTextView = (TextView) itemView.findViewById(R.id.HouseManagerFlaggedPostDescriptionTextView);
            optionsImageView = (ImageView) itemView.findViewById(R.id.HouseManagerFlaggedPostOptionImageView);
        }
    }
}
