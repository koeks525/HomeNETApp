package Adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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

import HomeNETStream.MessageFeedFragment;
import Models.MessageThread;
import Models.MessageThreadMessage;
import Models.MessagesViewModel;

/**
 * Created by Okuhle on 2017/08/02.
 */

public class UserMessagesAdapter extends RecyclerView.Adapter<UserMessagesAdapter.UserMessagesViewHolder> {

    private List<MessageThread> messageThreadList;
    private Activity currentActivity;

    public UserMessagesAdapter(List<MessageThread> messageThreadList, Activity currentActivity) {
        this.messageThreadList = messageThreadList;
        this.currentActivity = currentActivity;
    }

    @Override
    public UserMessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View currentView = inflater.inflate(R.layout.messages_card, parent, false);
        return new UserMessagesViewHolder(currentView);
    }

    @Override
    public void onBindViewHolder(final UserMessagesViewHolder holder, final int position) {
        MessagesViewModel firstMessage = null;
        if (messageThreadList.get(position).getMessageList() != null) {
            firstMessage = messageThreadList.get(position).getMessageList().get(0);
        }

        holder.titleTextView.setText(returnTrimmedString(messageThreadList.get(position).getTitle(), 1));
        if (firstMessage != null) {
            holder.descriptionTextView.setText(returnTrimmedString(firstMessage.getMessage(), 1));
        } else {
            holder.descriptionTextView.setVisibility(View.INVISIBLE);
            holder.descriptionTextView.setEnabled(false);
        }
        TextDrawable drawable = TextDrawable.builder().buildRect(messageThreadList.get(position).getTitle().substring(0, 2).toUpperCase(), Color.RED);
        holder.messageImageView.setImageDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return messageThreadList.size();
    }

    public class UserMessagesViewHolder extends RecyclerView.ViewHolder {

        CardView messageCardView;
        ImageView messageImageView;
        TextView titleTextView;
        TextView descriptionTextView;
        ImageView optionsImageView;
        ImageView readReceipt;


        public UserMessagesViewHolder(final View itemView) {
            super(itemView);
            messageCardView = (CardView) itemView.findViewById(R.id.MessagesCardView);
            messageCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    MessageThread selectedThread = messageThreadList.get(index);
                    MessageFeedFragment fragment = new MessageFeedFragment();
                    Bundle newBundle = new Bundle();
                    newBundle.putParcelable("SelectedThread", selectedThread);
                    fragment.setArguments(newBundle);
                    FragmentManager manager = currentActivity.getFragmentManager();
                    FragmentTransaction toMessages = manager.beginTransaction();
                    toMessages.replace(R.id.HomeNetFeedContentView, fragment, null);
                    toMessages.addToBackStack(null);
                    toMessages.commit();
                }
            });
            messageImageView = (ImageView) itemView.findViewById(R.id.MessageImageView);
            titleTextView = (TextView) itemView.findViewById(R.id.MessageTitleTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.MessageAnnouncementDescriptionTextView);
            optionsImageView = (ImageView) itemView.findViewById(R.id.AnnouncementMoreOptionsImageView);
            readReceipt = (ImageView) itemView.findViewById(R.id.MessageReadReceiptImageView);
            optionsImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu menu = new PopupMenu(view.getContext(), optionsImageView);
                    menu.inflate(R.menu.messages_options_menu);
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.MessagesDeleteMessageOption:


                                    break;
                                case R.id.MessagesViewParticipants:


                                    break;
                            }

                            return true;

                        }
                    });

                }
            });
        }
    }

    public String returnTrimmedString(String sourceString, int type) {
        String resultString = "";
        int length = sourceString.length();
        if (type == 1) {
            if (length > 25) {
                resultString = sourceString.substring(0, 25);
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
