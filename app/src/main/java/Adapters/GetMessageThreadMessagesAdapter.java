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

import Models.MessagesViewModel;

/**
 * Created by Okuhle on 2017/08/03.
 */

public class GetMessageThreadMessagesAdapter extends RecyclerView.Adapter<GetMessageThreadMessagesAdapter.GetMessageThreadMessagesViewHolder> {

    private List<MessagesViewModel> messagesList;

    public GetMessageThreadMessagesAdapter(List<MessagesViewModel> messagesList) {
        this.messagesList = messagesList;
    }

    @Override
    public GetMessageThreadMessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View currentView = inflater.inflate(R.layout.conversation_message_card, parent, false);
        return new GetMessageThreadMessagesViewHolder(currentView);
    }

    @Override
    public void onBindViewHolder(GetMessageThreadMessagesViewHolder holder, int position) {
        holder.nameSurnameTextView.setText(messagesList.get(position).getName() + " "+messagesList.get(position).getSurname());
        holder.messageTextView.setText(messagesList.get(position).getMessage());
        holder.dateSentTextView.setText("Date Sent: " + messagesList.get(position).getDateSent());
        TextDrawable drawable = TextDrawable.builder().buildRound(messagesList.get(position).getName().substring(0,1) + messagesList.get(position).getSurname().substring(0,1), Color.RED);
        holder.iconImageView.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public class GetMessageThreadMessagesViewHolder extends RecyclerView.ViewHolder {

        CardView conversationCardView;
        TextView nameSurnameTextView;
        TextView messageTextView;
        ImageView iconImageView;
        TextView dateSentTextView;


        public GetMessageThreadMessagesViewHolder(View itemView) {
            super(itemView);
            conversationCardView = (CardView) itemView.findViewById(R.id.ConversationCardView);
            nameSurnameTextView = (TextView) itemView.findViewById(R.id.ConversationNameSurnameTextView);
            messageTextView = (TextView) itemView.findViewById(R.id.ConversationMessageTextView);
            iconImageView = (ImageView) itemView.findViewById(R.id.ConversationImageView);
            dateSentTextView = (TextView) itemView.findViewById(R.id.ConversationDateSentTextView);
        }
    }
}
