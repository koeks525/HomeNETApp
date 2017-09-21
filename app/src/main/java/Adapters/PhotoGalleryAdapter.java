package Adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.koeksworld.homenet.R;

import java.util.List;

import DialogFragments.PhotoDetailsFragment;
import Models.HousePostViewModel;
import Models.Picture;

/**
 * Created by Okuhle on 2017/09/03.
 */

public class PhotoGalleryAdapter extends RecyclerView.Adapter<PhotoGalleryAdapter.PhotoGalleryViewHolder> {

    private List<Picture> pictureList;
    private List<HousePostViewModel> postList;
    private Activity currentActivity;

    public PhotoGalleryAdapter(Activity currentActivity, List<Picture> pictureList, List<HousePostViewModel> postList) {
        this.pictureList = pictureList;
        this.currentActivity = currentActivity;
        this.postList = postList;
    }


    @Override
    public PhotoGalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View currentView = inflater.inflate(R.layout.photo_gallery_card, parent, false);
        return new PhotoGalleryViewHolder(currentView);
    }

    @Override
    public void onBindViewHolder(PhotoGalleryViewHolder holder, int position) {
        Glide.with(currentActivity).load(pictureList.get(position).getImageFile()).into(holder.profileImageView);
    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }


    public class PhotoGalleryViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImageView;


        public PhotoGalleryViewHolder(View itemView) {
            super(itemView);
            profileImageView = (ImageView) itemView.findViewById(R.id.PhotoGalleryImageView);
            profileImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HousePostViewModel selectedPost = postList.get(getAdapterPosition());
                    Picture selectedPicture = pictureList.get(getAdapterPosition());
                    Bundle newBundle = new Bundle();
                    newBundle.putParcelable("SelectedPost", selectedPost);
                    newBundle.putParcelable("SelectedPhoto", selectedPicture);
                    PhotoDetailsFragment detailsFragment = new PhotoDetailsFragment();
                    detailsFragment.setArguments(newBundle);
                    detailsFragment.show(currentActivity.getFragmentManager(), null);
                }
            });
        }
    }
}
