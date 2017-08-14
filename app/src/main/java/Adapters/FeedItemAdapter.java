package Adapters;

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

import Models.CommentViewModel;

/**
 * Created by Okuhle on 2017/08/08.
 */

public class FeedItemAdapter extends RecyclerView.Adapter<FeedItemAdapter.FeedItemViewHolder> {


    public List<CommentViewModel> commentList;
    public FeedItemAdapter(List<CommentViewModel> commentList) {
        this.commentList = commentList;
    }

    @Override
    public FeedItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View currentView = inflater.inflate(R.layout.conversation_message_card, parent, false);
        return new FeedItemViewHolder(currentView);
    }

    @Override
    public void onBindViewHolder(FeedItemViewHolder holder, int position) {
        holder.nameSurnameTextView.setText(commentList.get(position).getName() + " " +commentList.get(position).getSurname());
        holder.dateSentTextView.setText("Date Commented: " +commentList.get(position).getDateAdded() );
        holder.messageTextView.setText(commentList.get(position).getComment());
        TextDrawable drawable = TextDrawable.builder().buildRect(commentList.get(position).getName().substring(0,1).toUpperCase() + commentList.get(position).getSurname().substring(0,1).toUpperCase(), Color.CYAN);
        holder.conversationImageView.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class FeedItemViewHolder extends RecyclerView.ViewHolder {

        CardView conversationCardView;
        ImageView conversationImageView;
        TextView nameSurnameTextView, messageTextView, dateSentTextView;

        public FeedItemViewHolder(View itemView) {
            super(itemView);
            conversationCardView = (CardView) itemView.findViewById(R.id.ConversationCardView);
            conversationImageView = (ImageView) itemView.findViewById(R.id.ConversationImageView);
            nameSurnameTextView = (TextView) itemView.findViewById(R.id.ConversationNameSurnameTextView);
            messageTextView = (TextView) itemView.findViewById(R.id.ConversationMessageTextView);
            dateSentTextView = (TextView) itemView.findViewById(R.id.ConversationDateSentTextView);
        }
    }
}
