package Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.koeksworld.homenet.R;

import java.util.List;

import HomeNETStream.AnnouncementDetailsFragment;
import Models.AnnouncementCommentViewModel;

/**
 * Created by Okuhle on 2017/08/05.
 */

public class AnnouncementCommentsAdapter extends RecyclerView.Adapter<AnnouncementCommentsAdapter.AnnouncementCommentsViewHolder> {

    private List<AnnouncementCommentViewModel> commentList;
    private Activity currentActivity;

    public AnnouncementCommentsAdapter(List<AnnouncementCommentViewModel> commentList, Activity currentActivity) {
        this.commentList = commentList;
        this.currentActivity = currentActivity;
    }
    @Override
    public AnnouncementCommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View currentView = inflater.inflate(R.layout.conversation_message_card, parent, false);
        return new AnnouncementCommentsViewHolder(currentView);
    }

    @Override
    public void onBindViewHolder(AnnouncementCommentsViewHolder holder, int position) {
        holder.nameSurnameTextView.setText(commentList.get(position).getName()+ " "+commentList.get(position).getSurname()+ " wrote: ");
        holder.commentTextView.setText(commentList.get(position).getComment());
        holder.dateTextView.setText("Date Posted: "+commentList.get(position).getDateAdded());
        TextDrawable drawable = TextDrawable.builder().buildRect(commentList.get(position).getName().substring(0,1) + commentList.get(position).getSurname().substring(0,1), Color.BLUE);
        holder.logo.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class AnnouncementCommentsViewHolder extends RecyclerView.ViewHolder {

        CardView commentCard;
        TextView nameSurnameTextView;
        TextView commentTextView;
        TextView dateTextView;
        ImageView logo;

        public AnnouncementCommentsViewHolder(View itemView) {
            super(itemView);
            commentCard = (CardView) itemView.findViewById(R.id.ConversationCardView);
            nameSurnameTextView = (TextView) itemView.findViewById(R.id.ConversationNameSurnameTextView);
            commentTextView = (TextView) itemView.findViewById(R.id.ConversationMessageTextView);
            dateTextView = (TextView) itemView.findViewById(R.id.ConversationDateSentTextView);
            logo = (ImageView) itemView.findViewById(R.id.ConversationImageView);
        }
    }
}
