package Adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.internal.IMapFragmentDelegate;
import com.koeksworld.homenet.R;

import java.util.List;

import Models.HouseAnnouncement;

/**
 * Created by Okuhle on 2017/06/16.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private List<HouseAnnouncement> houseAnnouncementList;

    public MessagesAdapter(List<HouseAnnouncement> houseAnnouncementList) {
        this.houseAnnouncementList = houseAnnouncementList;
    }
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View currentView = inflater.inflate(R.layout.messages_card, parent, false);
        return new MessageViewHolder(currentView);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        if (position % 2 == 0) {
            holder.readReceiptImageView.setBackgroundColor(Color.GRAY);
        } else {
            holder.readReceiptImageView.setBackgroundColor(Color.RED);
        }
        holder.titleTextView.setText(houseAnnouncementList.get(position).getTitle());
        holder.descriptionTextView.setText(houseAnnouncementList.get(position).getComment());
        holder.userImageView.setImageResource(R.drawable.post_it);


    }

    @Override
    public int getItemCount() {
        return houseAnnouncementList.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        private ImageView readReceiptImageView;
        private ImageView userImageView;
        private TextView titleTextView;
        private TextView descriptionTextView;
        private ImageView optionsImageView;
        public MessageViewHolder(View itemView) {
            super(itemView);
            readReceiptImageView = (ImageView) itemView.findViewById(R.id.MessageReadReceiptImageView);
            userImageView = (ImageView) itemView.findViewById(R.id.MessageImageView);
            titleTextView = (TextView) itemView.findViewById(R.id.MessageTitleTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.MessageAnnouncementDescriptionTextView);
            optionsImageView = (ImageView) itemView.findViewById(R.id.AnnouncementMoreOptionsImageView);
            userImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }
}
