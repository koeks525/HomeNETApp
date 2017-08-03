package Adapters;

import android.graphics.Color;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.koeksworld.homenet.R;

import java.util.List;

import Models.HouseAnnouncement;

/**
 * Created by Okuhle on 2017/07/13.
 */

public class HouseAnnouncementsAdapter extends RecyclerView.Adapter<HouseAnnouncementsAdapter.HouseAnnouncementViewHolder> {

    private List<HouseAnnouncement> announcementList;
    public HouseAnnouncementsAdapter(List<HouseAnnouncement> announcementList) {
        this.announcementList = announcementList;
    }
    @Override
    public HouseAnnouncementsAdapter.HouseAnnouncementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View currentView = inflater.inflate(R.layout.messages_card, parent, false);
        return new HouseAnnouncementViewHolder(currentView);
    }

    @Override
    public void onBindViewHolder(final HouseAnnouncementsAdapter.HouseAnnouncementViewHolder holder, int position) {
        HouseAnnouncement cur = announcementList.get(position);
        holder.messageTitleTextView.setText(returnTrimmedString(cur.getTitle(), 1));
        holder.messageAnnouncementDescriptionTextView.setText(returnTrimmedString(cur.getMessage(), 2));
        TextDrawable drawable = TextDrawable.builder().buildRoundRect(cur.getTitle().substring(0,2), Color.BLUE, 5);
        holder.messageImageView.setImageDrawable(drawable);
        holder.messageReadReceiptImageView.setVisibility(View.INVISIBLE);
        holder.messageReadReceiptImageView.setEnabled(false);
        holder.announcementMoreOptionsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(holder.announcementMoreOptionsTextView.getContext(), view);
                popupMenu.inflate(R.menu.announcements_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.DeleteAnnouncementOption:


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
        return announcementList.size();
    }

    class HouseAnnouncementViewHolder extends RecyclerView.ViewHolder {
        CardView messageCardView;
        ImageView messageReadReceiptImageView;
        ImageView messageImageView;
        TextView messageTitleTextView;
        TextView messageAnnouncementDescriptionTextView;
        ImageView announcementMoreOptionsTextView;

        public HouseAnnouncementViewHolder(View itemView) {
            super(itemView);
            messageCardView = (CardView) itemView.findViewById(R.id.MessagesCardView);
            messageReadReceiptImageView = (ImageView) itemView.findViewById(R.id.MessageReadReceiptImageView);
            messageImageView = (ImageView) itemView.findViewById(R.id.MessageImageView);
            messageTitleTextView = (TextView) itemView.findViewById(R.id.MessageTitleTextView);
            messageAnnouncementDescriptionTextView = (TextView) itemView.findViewById(R.id.MessageAnnouncementDescriptionTextView);
            announcementMoreOptionsTextView = (ImageView) itemView.findViewById(R.id.AnnouncementMoreOptionsImageView);
        }
    }

    public String returnTrimmedString(String sourceString, int type) {
        String resultString = "";
        int length = sourceString.length();
        if (type == 1) {
            if (length > 25) {
                resultString = sourceString.substring(0, 40);
                resultString += "...";
                return resultString;
            } else {
                return sourceString;
            }
        } else {
            if (length > 40) {
                resultString = sourceString.substring(0, 40);
                resultString += "...";
                return resultString;
            } else {
                return sourceString;
            }


        }
    }
}
